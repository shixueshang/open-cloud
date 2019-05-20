package com.opencloud.common.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * @author: flyme
 * @date: 2018/3/8 15:57
 * @desc: 参数加密类
 */
public class DesUtils {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    public static String appkey = "c306e6eb-fdba-11e7-9bb0-00163e0004bf";
    public static String sercret = "jZ0F9RTa5Y4NDZ95C4n38SuddBgtSw05";
    public static String DESIV;

    public static String encode(String key, String data) {
        try {
            String str = encode(key, data.getBytes());
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encode1(String key, String data, String iv) throws Exception {
        return encode1(key, data.getBytes(), iv);
    }

    public static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(DESIV.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String encode1(String key, byte[] data, String sic) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(sic.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @SuppressWarnings("finally")
    public static byte[] decode(String key, byte[] data) {
        byte[] result = null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(DESIV.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            result = cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            return result;
        }
    }

    public static String decodeValue(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            datas = decode(key, Base64.getDecoder().decode(data));
            if (ObjectUtils.isEmpty(datas)) {
                return "";
            }
            value = new String(datas);
            if (value.equals("")) {
                throw new Exception();
            }
        } catch (Exception e) {

        }
        return value;
    }

    public static String getkey(String time) {
        String a;
        StringBuffer buffer2 = new StringBuffer(md5Password(time, 1));
        String hh = buffer2.toString().substring(0, 8);
        a = hh.toUpperCase() + appkey;
        StringBuffer buffer = new StringBuffer(md5Password(a, 1));
        String b = buffer.toString();
        a = b.substring(12, 20).toLowerCase();
        return a;
    }

    public static String getiv(String time) {
        String a;
        StringBuffer buffer3 = new StringBuffer(md5Password(time, 1));
        String dd = buffer3.substring(12, 20);
        a = dd.toLowerCase() + sercret;
        StringBuffer buffer4 = new StringBuffer(md5Password(a, 1));
        a = buffer4.substring(24, 32).toUpperCase();
        return a;
    }

    public static String md5Password(String password, int code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        // 手机号
        String str_tel = "18739941307";
        String str_time = "44444444";
        DESIV = getiv(str_time);
        String key1 = getkey(str_time);
        String content = DesUtils.encode(key1, str_tel);
        System.out.println(DesUtils.encode(key1, str_tel));
        // 加密后的key
        String tempKey = getkey(str_time);
        System.out.println(DesUtils.decodeValue(tempKey, content));
        System.out.println("33333" + getTel("9f8aae55267dd59cecf18bd14970ce5e", "8888888888"));
    }

    public static String getTel(String str_tel, String str_time) {
        DESIV = getiv(str_time);
        String tempKey = getkey(str_time);
        return DesUtils.decodeValue(tempKey, str_tel);
    }
}
