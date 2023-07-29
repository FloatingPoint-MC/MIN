package cn.floatingpoint.min.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-29 13:56:59
 */
public class WebUtil {
    private static final SSLContext context;
    private static final int abroad;

    static {
        SSLContext ctx = null;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream in = WebUtil.class.getResourceAsStream("/ssl.jks")) {
                ks.load(in, "floatILoveU".toCharArray());
            }
            TrustManagerFactory customTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            customTmf.init(ks);
            TrustManagerFactory defaultTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            defaultTmf.init((KeyStore) null);
            List<X509TrustManager> managers = new ArrayList<>();
            managers.addAll(Arrays.stream(customTmf.getTrustManagers()).filter(tm -> tm instanceof X509TrustManager)
                    .map(tm -> (X509TrustManager) tm).collect(Collectors.toList()));
            managers.addAll(Arrays.stream(defaultTmf.getTrustManagers()).filter(tm -> tm instanceof X509TrustManager)
                    .map(tm -> (X509TrustManager) tm).collect(Collectors.toList()));
            TrustManager multiManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    CertificateException wrapper = new CertificateException("Unable to validate via any trust manager.");
                    for (X509TrustManager manager : managers) {
                        try {
                            manager.checkClientTrusted(chain, authType);
                            return;
                        } catch (Throwable t) {
                            wrapper.addSuppressed(t);
                        }
                    }
                    throw wrapper;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    CertificateException wrapper = new CertificateException("Unable to validate via any trust manager.");
                    for (X509TrustManager manager : managers) {
                        try {
                            manager.checkServerTrusted(chain, authType);
                            return;
                        } catch (Throwable t) {
                            wrapper.addSuppressed(t);
                        }
                    }
                    throw wrapper;
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    List<X509Certificate> certificates = new ArrayList<>();
                    for (X509TrustManager manager : managers) {
                        certificates.addAll(Arrays.asList(manager.getAcceptedIssuers()));
                    }
                    return certificates.toArray(new X509Certificate[0]);
                }
            };
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { multiManager }, new SecureRandom());
        } catch (Throwable ignored) {
        }
        context = ctx;
        abroad = getAddressByIP(getOutIPV4());
    }

    public static String getPlatform() {
        return abroad == 1 ? "https://raw.githubusercontent.com/FloatingPoint-MC/MIN/master/" : "https://gitee.com/FloatingPoint-MC/MIN/raw/master/";
    }

    public static String getDownloadUrl() {
        return abroad == 1 ? "https://github.com/FloatingPoint-MC/MIN/releases/download/" : "https://gitee.com/FloatingPoint-MC/MIN/releases/download/";
    }

    public static JSONObject getJSON(String url) throws IOException, JSONException, URISyntaxException {
        JSONObject jsonObject;
        HttpURLConnection connection = (HttpURLConnection) new URL(new URI(url).toASCIIString()).openConnection();
        try (InputStream inputStream = connection.getInputStream()) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int len;
                byte[] bytes = new byte[4096];
                while ((len = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                jsonObject = new JSONObject(out.toString("UTF8"));
            }
        }
        return jsonObject;
    }

    private static int getAddressByIP(String ip) {
        try {
            URL url = new URL("https://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=1433920989928&ie=utf8&oe=utf-8&format=json");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JSONObject jsStr = new JSONObject(result.toString());
            JSONArray jsData = (JSONArray) jsStr.get("data");
            JSONObject data = (JSONObject) jsData.get(0);//位置
            String[] provinces = {"北京市", "天津市", "河北省", "山西省", "内蒙古", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏", "陕西省", "甘肃省", "青海省", "宁夏", "新疆", "台湾省", "香港", "澳门"};
            String address = (String) data.get("location");
            boolean china = false;
            for (String province : provinces) {
                if (address.startsWith(province)) {
                    china = true;
                    break;
                }
            }
            return china ? 0 : 1;
        } catch (Exception e) {
            return 0;
        }
    }

    private static String getOutIPV4() {
        String myIp = "https://api.myip.la/";

        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpsURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(myIp);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read);
            }
        } catch (IOException ignored) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return inputLine.toString().replace("\r", "").replace("\n", "");
    }
}
