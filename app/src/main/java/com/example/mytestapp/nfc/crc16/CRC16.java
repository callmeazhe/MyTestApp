package com.example.mytestapp.nfc.crc16;

/**
 * Generate  CRC
 * Created by 30035027 on 8/9/2017.
 */

public class CRC16 {
    /**
     * Generate  CRC(Xmod CRC-16,x16 + x15 + x2 + 1)
     *
     * @return CRC16 check code
     */


    public static int ansiCrc16(byte[] data, int crc) {
        int polynomial = 0x1021;

        for (int i = 0; i < data.length; i++) {
            byte item = data[i];
            for (int j = 0; j < 8; j++) {
                int crcCopy = crc;
                crc <<= 1;
                if ((item & 0x80) != 0) {
                    crc |= 1;
                }
                if ((crcCopy & 0x8000) != 0) {
                    crc ^= 0x1021;
                }
                item <<= 1;
            }
        }

        crc &= 0xffff;
        return crc;
    }

    /**
     * Generate  CRC(ANSI(modbus) CRC-16,x16 + x15 + x2 + 1)
     *
     * @return CRC16 check code
     */

    public static byte[] crc16(byte[] data)

    {
        if (data == null)
            throw new IllegalArgumentException("data is not null");
        if (data.length <= 0)
            throw new IllegalArgumentException("data length must be greater than 0");
        int CRC = 0x0000FFFF;
        byte result[] = new byte[2];
        byte temp = 0;
        for (int i = 0; i < data.length; i++) {
            CRC ^= ((int) data[i] & 0x000000ff);
            for (int j = 0; j < 8; j++) {
                temp = (byte) (CRC & 0x00000001);
                CRC = CRC >> 1;
                if (temp != 0) {
                    CRC = CRC ^ 0x0000A001;
                }
            }
        }
        //高位在0，低位在1 --》 大端；反之，小端
        result[0] = (byte) ((CRC >> 8) & 0xff);//高位
        result[1] = (byte) (CRC & 0xff);//低位
        return result;
    }

}
