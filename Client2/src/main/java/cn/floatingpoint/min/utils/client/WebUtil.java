package cn.floatingpoint.min.utils.client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 15:03:24
 */
public class WebUtil {
    public static JSONObject getJSON(String url) throws IOException, JSONException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) new URL(new URI(url).toASCIIString()).openConnection();
        return getJsonObject(connection);
    }

    public static JSONObject getJSONFromPost(String url) throws URISyntaxException, IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) new URL(new URI(url).toASCIIString()).openConnection();
        connection.setRequestMethod("POST");
        return getJsonObject(connection);
    }

    public static JSONObject getJsonObject(HttpURLConnection connection) throws IOException {
        connection.connect();
        JSONObject jsonObject;
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
        connection.disconnect();
        return jsonObject;
    }
}
