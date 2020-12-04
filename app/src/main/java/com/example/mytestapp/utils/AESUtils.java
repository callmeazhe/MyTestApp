package com.example.mytestapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

    private final static String HEX = "0123456789ABCDEF";
    private static final int keyLength = 16;
    private static final String defaultV = "0";

    /**
     * Key default complement number, complement 16 digits, to ensure the security of completion at least 16 bit length,android and ios docking through.
     *
     * @param keyValue
     * @param strLength
     * @param defaultValue
     * @return
     */
    private static String toMakekey(String keyValue, int strLength, String defaultValue) {

        int strLen = keyValue.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(keyValue).append(defaultValue);
                keyValue = buffer.toString();
                strLen = keyValue.length();
            }
        }
        return keyValue;
    }


    /**
     * init AES Cipher
     *
     * @param sKey
     * @param cipherMode
     * @return
     */
    public static Cipher initAESCipher(String sKey, int cipherMode) {
        Cipher cipher = null;
        try {
            byte[] rawKey = toMakekey(sKey, keyLength, defaultV).getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(cipherMode, skeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        }

        return cipher;
    }

    /**
     * AES encryptFile
     *
     * @param sKey
     * @return
     */
    public static File encryptFile(InputStream inputStream, String outPath, String sKey) {
        File encrypfile = null;
        OutputStream outputStream = null;
        try {
            encrypfile = new File(outPath);
            outputStream = new FileOutputStream(encrypfile);
            Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] cache = new byte[1024];
            int nRead = 0;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
            cipherInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();  // To change body of catch statement use
                // File | Settings | File Templates.
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();  // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        return encrypfile;
    }


    public static String getSecretKey(int sensorType) {
        String secretKey = null;
//        switch (sensorType) {
//            case NfcDeviceTypeConstant.DEVICE_TYPE_VIBRATION:
//                secretKey = "2BC8A2DF";
//                break;
//            case NfcDeviceTypeConstant.DEVICE_TYPE_TEMPERATURE:
//                secretKey = "979C8A45";
//                break;
//            case NfcDeviceTypeConstant.DEVICE_TYPE_PRESSURE:
//                secretKey = "0B99F6A7";
//                break;
//            default:
//                LogUtil.outLog(MyApplication.mContext.getString(R.string.str_illegal_sensor));
//                break;
//        }
        return secretKey;
    }

    /**
     * AES decryptFile
     *
     * @return
     */
    public static byte[] decryptFile(int deviceType, InputStream inputStream) {
        String sKey = getSecretKey(deviceType);
        if (sKey == null) {
            return null;
        }
        byte[] decryptFile = null;
        ByteArrayOutputStream outputStream = null;
        try {
            Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
            outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }
            cipherOutputStream.close();
            if (outputStream != null)
                decryptFile = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();  // To change body of catch statement use File |
            // Settings | File Templates.
        } finally {
            try {
                if (null != outputStream)
                    outputStream.close();
                if (null != inputStream)
                    inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();  // To change body of catch statement use
                // File | Settings | File Templates.
            }
        }
        return decryptFile;
    }

}