package com.example.mytestapp.nfc.utils;

import android.nfc.FormatException;
import android.nfc.tech.NfcA;

import com.example.mytestapp.nfc.exceptions.WriteErrorException;

import java.io.IOException;

/**
 * Send instructions to NtagI2C chip, with single address write and area write,
 * single address read and area address read, can select sector
 * <p>
 * Created by 30035027 on 8/8/2017.
 */

public class NtagI2CCommandUtils {

    // When timeout time is 20ms, FZ-N1 Toughpad cannot communicate by NFC
    // For addressing this issue, change the timeout from 20ms to 100ms
    private static int timeout = 100;

    /**
     * Writes data (4 bytes) to a specified memory address
     *
     * @param data Write data
     * @param page data address
     * @throws IOException     The data transfer error will release IOException
     * @throws FormatException The data Format error will release FormatException
     */
    public static byte[] write(NfcA nfca, byte[] data, byte page) throws IOException,
            FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca can't be null");
        if (data == null)
            throw new IllegalArgumentException("data can't be null");
        if (data.length > 4)
            throw new IllegalArgumentException("data can't be greater than 4");
        byte[] command = new byte[6];
        command[0] = (byte) 0xA2;
        command[1] = page;
        System.arraycopy(data, 0, command, 2, data.length);
        return transceive(nfca, command);
    }


    /**
     * Writes data to the specified area address
     *
     * @param data      Write data
     * @param startAddr Began to address
     * @param endAddr   End to address
     * @throws IOException     The data transfer error will release IOException
     * @throws FormatException The data Format error will release FormatException
     */
    public static byte[] fastWrite(NfcA nfca, byte[] data, byte startAddr, byte endAddr) throws IOException,
            FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca can't be null");
        if (data == null)
            throw new IllegalArgumentException("data can't be null");
        if ((startAddr & 0xff) > (endAddr & 0xff))
            throw new IllegalArgumentException("endAddr less than startAddr");
        if ((endAddr - startAddr + 1) * 4 < data.length) {
            throw new IllegalArgumentException("data size is beyond the specified address range");
        }
        byte[] command = new byte[3 + data.length];
        command[0] = (byte) 0xA6;
        command[1] = (byte) startAddr;
        command[2] = (byte) endAddr;
        System.arraycopy(data, 0, command, 3, data.length);
        nfca.setTimeout(500);
        byte[] result = transceive(nfca, command);
        nfca.setTimeout(timeout);
        return result;

    }


    /**
     * Read the data (4 bytes) from the specified address
     *
     * @param page data address
     * @return Returns the data of the specified address
     * @throws IOException
     * @throws FormatException
     */
    public static byte[] read(NfcA nfca, byte page) throws IOException, FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca can't be null");
        byte[] command = new byte[2];
        command[0] = (byte) 0x30;
        command[1] = page;
        return nfca.transceive(command);
    }


    /**
     * Read the data from the specified area address
     *
     * @param startAddr Began to address
     * @param endAddr   End to address
     * @return Returns the specified area address data
     * @throws IOException     The data transfer error will release IOException
     * @throws FormatException The data Format error will release FormatException
     */
    public static byte[] fastRead(NfcA nfca, byte startAddr, byte endAddr) throws IOException,
            FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca can't be null");
        if ((startAddr & 0xff) > (endAddr & 0xff))
            throw new IllegalArgumentException("endAddr less than startAddr");

        byte[] command = new byte[3];
        command[0] = (byte) 0x3A;
        command[1] = (byte) startAddr;
        command[2] = (byte) endAddr;
        nfca.setTimeout(500);
        byte[] result = nfca.transceive(command);
        nfca.setTimeout(timeout);
        return result;
    }


    /**
     * Switching sectors
     *
     * @param sector Operation sector
     * @throws IOException
     * @throws FormatException
     */
    public static void selectSector(NfcA nfca, byte sector) throws IOException, FormatException {
        // When card is already in this sector do nothing
        if (nfca == null) {
            LogUtil.outLog("Nfca can't be null");
            throw new IllegalArgumentException("Nfca can't be null");
        }
        if (sector < 0)
            throw new IllegalArgumentException("Sector can't be less than zero");
        try {
            byte command[] = new byte[2];
            command[0] = (byte) 0xc2;
            command[1] = (byte) 0xff;
            nfca.transceive(command);
            command = new byte[4];
            command[0] = (byte) sector;
            command[1] = (byte) 0x00;
            command[2] = (byte) 0x00;
            command[3] = (byte) 0x00;
            nfca.setTimeout(timeout);
            nfca.transceive(command);
        } catch (Exception e) {
            LogUtil.outLog("selectSector: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Performs a Get Version Command.
     *
     * @return Get Version Response
     * @throws IOException
     */
    public static byte[] getVersion(NfcA nfca) throws IOException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca can't be null");
        byte command[] = new byte[1];
        command[0] = (byte) 0x60;
        return nfca.transceive(command);
    }

    /**
     * @param nfca
     * @param data
     * @return succeed return result
     * @throws IOException
     * @throws WriteErrorException 8 result null
     *                             9 result empty
     *                             0 NAK for invalid argument (i.e. invalid page address or wrong password)
     *                             1 NAK for parity or CRC error
     *                             3 NAK for Arbiter locked to I²C
     *                             4 Number of negative PWD_AUTH command limit reached
     *                             7 NAK for EEPROM write error
     *                             2 write error
     */
    private static byte[] transceive(NfcA nfca, byte data[]) throws IOException, WriteErrorException {
        byte result[] = nfca.transceive(data);
        if (result != null && result.length > 0 && result[0] == 10) {
            return result;
        } else {
            if (result == null) {
                throw new WriteErrorException("8");
            } else if (result.length == 0) {
                throw new WriteErrorException("9");
            } else if (result[0] == 0) {
                throw new WriteErrorException("0");
            } else if (result[0] == 1) {
                throw new WriteErrorException("1");
            } else if (result[0] == 3) {
                throw new WriteErrorException("3");
            } else if (result[0] == 4) {
                throw new WriteErrorException("4");
            } else if (result[0] == 7) {
                throw new WriteErrorException("7");
            }
            throw new WriteErrorException("2");
        }

    }
}
