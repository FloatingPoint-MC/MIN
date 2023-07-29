package cn.floatingpoint.min.utils.math;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:15:29
 */
public class DESUtil {

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String SALT = "hyt1nM1n";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    /**
     * 生成key
     *
     * @return Key
     */
    private static Key generateKey() throws Exception {
        DESKeySpec dks = new DESKeySpec("HYT_1_Love_U".getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String data) {

        if (data == null)
            return null;
        try {
            Key secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(SALT.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param data     待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String data) {
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(SALT.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return data;
        }
    }
}
