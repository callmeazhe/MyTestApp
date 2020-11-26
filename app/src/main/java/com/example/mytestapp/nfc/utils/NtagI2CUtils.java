package com.example.mytestapp.nfc.utils;

import android.nfc.FormatException;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.exceptions.FunctionCodeErrorException;
import com.example.mytestapp.nfc.exceptions.OutTimeException;
import com.example.mytestapp.nfc.exceptions.WriteErrorException;
import com.example.mytestapp.nfc.listener.SRAMListener;
import com.example.mytestapp.nfc.modbus.Constants;
import com.example.mytestapp.nfc.modbus.ModBusFrame;
import com.example.mytestapp.nfc.modbus.ModbusDataBean;
import com.example.mytestapp.nfc.modbus.ModbusFrameUtil;
import com.example.mytestapp.nfc.modbus.NfcFrame;
import com.example.mytestapp.nfc.modbus.NfcFrameUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 30035027 on 8/1/2017.\
 * Operate the i2c read-write tool class and use the modbus protocol
 * to interact with the CPU through the NTAG
 */

public class NtagI2CUtils {


    /**
     * Send a written request to the CPU via SRAM
     *
     * @param nfca
     * @param modbuslist modbus request lists
     * @param listener   squeence finshed listener
     * @return return response lists
     * @throws IOException
     * @throws FormatException
     */
    public static void writeSRAMFrames(NfcA nfca, ArrayList<ArrayList<ModbusDataBean>> modbuslist,
                                       SRAMListener listener) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException, FunctionCodeErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (modbuslist == null)
            throw new IllegalArgumentException("Lists is not null");
        if (modbuslist.size() <= 0)
            throw new IllegalArgumentException("Lists must be greater than zero");

        NtagI2CCommandUtils.selectSector(nfca, (byte) 0);
        List<ModBusFrame> modbusLists = ModbusFrameUtil.toModBusWriteFrameList(modbuslist);
        ArrayList<ArrayList<NfcFrame>> mLists = NfcFrameUtil.toNfcWriteFrameList(modbusLists);

        if (!NtagSessionUtils.checkPathThroughModePossible(nfca)) {
            throw new IllegalArgumentException("Path through mode don't use");
        }

        int index = 0;
        int retryCount = 0;
        writeWithRetry(nfca, mLists, listener, index, retryCount);
    }

    public static void writeWithRetry(NfcA nfca, ArrayList<ArrayList<NfcFrame>> list,
                                      SRAMListener listener, int index, int retryCount) throws WriteErrorException, OutTimeException, FormatException, InterruptedException, IOException, FunctionCodeErrorException {
        if (retryCount == Constants.NFC_RETRY_COUNT) {
            throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
        }
        for (; index < list.size(); index++) {
            ArrayList<NfcFrame> nfcFramesOfOneModbus = list.get(index);
            for (int ii = 0; ii < nfcFramesOfOneModbus.size(); ii++) {
                // watch RF_LOCKED
                NtagSessionUtils.waitCpuGetSRAMData(nfca);
                NfcFrame mf = nfcFramesOfOneModbus.get(ii);
                LogUtil.outLog("req--------------------" + ii);
                LogUtil.outLog(getStrByBytes(mf.getNfcFrameByte()));
                NtagI2CCommandUtils.fastWrite(nfca, mf.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
                        (byte) Constants.SRAM_END);
            }
            // watch RF_READY
            int resultCode = NtagSessionUtils.waitCpuPrepareSRAMDataWithRetry(nfca);
            if (resultCode == Constants.NFC_ERROR_CODE) {
                retryCount++;
                writeWithRetry(nfca, list, listener, index, retryCount);
            } else {
                retryCount = 0;
                byte res[] = readSRAMFrame(nfca);
                LogUtil.outLog("res-------------------------------------------------" + index);
                LogUtil.outLog(getStrByBytes(res));
                if (!NfcFrameUtil.checkHeadAndDataCRC(res) || res[Constants.FUNCTION_CODE_INDEX] != Constants.WRITE_FUNCTION_CODE) {
                    throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
                }
                if (listener != null) {
                    listener.onWriteSRAM((int) (((float) index / (list.size() - 1)) * Constants.PROGRESS_MAXIMUM_VALUES));
                }
            }
        }
    }

    /**
     * Send a read request to the CPU via SRAM
     *
     * @param nfca
     * @param modbuslist modbus request lists
     * @param listener
     * @return return response lists
     * @throws IOException
     * @throws FormatException
     * @throws OutTimeException
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public static Map<String, Object> readSRAMFrames(NfcA nfca, ArrayList<ArrayList<ModbusDataBean>> modbuslist, SRAMListener listener) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException, FunctionCodeErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (modbuslist == null)
            throw new IllegalArgumentException("Lists is not null");
        if (modbuslist.size() <= 0)
            throw new IllegalArgumentException("Lists must be greater than zero");

        LogUtil.outLog("isConnected");
        NtagI2CCommandUtils.selectSector(nfca, (byte) 0);
        LogUtil.outLog("Select sector 0");
        List<ModBusFrame> modbusLists = ModbusFrameUtil.toModBusReadFrameList(modbuslist);
        // One Modbus Read Frame could only include ONE NFC frame.
        LogUtil.outLog("Modbus frame list is created.");
        List<NfcFrame> nfcFrames = NfcFrameUtil.toNfcReadFrameList(modbusLists);
        LogUtil.outLog("Start to check path through mode.");
        if (!NtagSessionUtils.checkPathThroughModePossible(nfca)) {
            return null;
        }
        LogUtil.outLog("checkPathThroughModePossible");
        Map<String, Object> resParameter = new HashMap<>();
        int retryCount = 0;
        int index = 0;
        resParameter = readWithDoTry(nfca, nfcFrames, modbuslist, index, retryCount, resParameter, listener);
        return resParameter;

//        for (int i = 0; i < nfcFrames.size(); i++) {
//            NtagSessionUtils.waitCpuGetSRAMData(nfca);
//            NfcFrame mf = nfcFrames.get(i);
//            LogUtil.outLog("req--------------------" + i);
//            LogUtil.outLog(getStrByBytes(mf.getNfcFrameByte()));
//            LogUtil.outLog("getStrByBytes");
//            NtagI2CCommandUtils.fastWrite(nfca, mf.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
//                    (byte) Constants.SRAM_END);
//            LogUtil.outLog("waitCpuPrepareSRAMData");
//            NtagSessionUtils.waitCpuPrepareSRAMData(nfca);
//            byte res[] = readSRAMFrame(nfca);
//            LogUtil.outLog("res----------------------------------------------------" + i);
//            LogUtil.outLog(getStrByBytes(res));
//            int nfcFrameNumber = ByteArrayUtils.bytesToInt(res, Constants.FRAME_NUMBER_START_INDEX, Constants.FRAME_NUMBER_LENGTH);
//            if (nfcFrameNumber == 1) {
//                if (NfcFrameUtil.checkHeadAndDataCRC(res) && res[Constants.FUNCTION_CODE_INDEX] == Constants.READ_FUNCTION_CODE) {
//                    ModbusFrameUtil.bytesToUiDataMap(modbuslist.get(i), Arrays.copyOfRange(res, Constants.READ_DATA_START_INDEX, Constants.READ_DATA_START_INDEX + res[Constants.READ_SIZE_INDEX]), resParameter);
//                } else {
//                    throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
//                }
//            } else {
//                byte modbusFrameData[] = new byte[Constants.MODBUS_READ_MAXIMUM_SIZE];
//                int startIndex = 0;
//                if (NfcFrameUtil.checkHeadCRC(res) && res[Constants.FUNCTION_CODE_INDEX] == Constants.READ_FUNCTION_CODE) {
//                    // ByteArrayUtils.bytesToInt(res, FRAME_SIZE_INDEX, FRAME_SIZE_LENGTH) : is the value of "Frame size"
//                    byte firstNFCFrameData[] = Arrays.copyOfRange(res, Constants.FUNCTION_CODE_INDEX, ByteArrayUtils.bytesToInt(res, Constants.FRAME_SIZE_INDEX, Constants.FRAME_NUMBER_LENGTH));
//                    System.arraycopy(firstNFCFrameData, 0, modbusFrameData, startIndex, firstNFCFrameData.length);
//                    startIndex = firstNFCFrameData.length;
//                } else {
//                    throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
//                }
//                for (int ii = 1; ii < nfcFrameNumber; ii++) {
//                    NtagSessionUtils.waitCpuPrepareSRAMData(nfca);
//                    byte nextNFCFrame[] = readSRAMFrame(nfca);
////                    if (NfcFrameUtil.checkHeadCRC(nextNFCFrame)){
////                    if (NfcFrameUtil.checkHeadCRC(nextNFCFrame) &&
////                            nextNFCFrame[Constants.FUNCTION_CODE_INDEX] == Constants.READ_FUNCTION_CODE) {
//                        byte nextData[] = Arrays.copyOfRange(res, Constants.FUNCTION_CODE_INDEX, ByteArrayUtils.bytesToInt(nextNFCFrame, Constants.FRAME_SIZE_INDEX, Constants.FRAME_NUMBER_LENGTH));
//                        System.arraycopy(nextData, 0, modbusFrameData, startIndex, nextData.length);
//                        startIndex += nextData.length;
////                    } else {
////                        throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
////                    }
//                    if (ii == nfcFrameNumber) {
//                        byte crc[] = Arrays.copyOfRange(res, ByteArrayUtils.bytesToInt(res, 7, 2) - Constants.CRC_LENGTH, ByteArrayUtils.bytesToInt(res, 7, 2));
//                        if (ByteArrayUtils.isSameArray(crc, CRC16.crc16(Arrays.copyOfRange(modbusFrameData, 0, startIndex)))) {
//                            ModbusFrameUtil.bytesToUiDataMap(modbuslist.get(i), Arrays.copyOfRange(modbusFrameData, 2, modbusFrameData.length), resParameter);
//                        } else {
//                            throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
//                        }
//                    }
//                }
//
//            }
//            if (listener != null) {
//                listener.onWriteSRAM((int) (((float) i / (nfcFrames.size() - 1)) * Constants.PROGRESS_MAXIMUM_VALUES));
//            }
//        }
//
//        return resParameter;
    }

    public static Map<String, Object> readWithDoTry(NfcA nfca, List<NfcFrame> nfcFrames,
                                                    ArrayList<ArrayList<ModbusDataBean>> modbuslist,
                                                    int index, int retryCount,
                                                    Map<String, Object> resParameter,
                                                    SRAMListener listener) throws WriteErrorException, OutTimeException, FormatException, InterruptedException, IOException, FunctionCodeErrorException {
        if (retryCount == Constants.NFC_RETRY_COUNT) {
            throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
        }
        for (; index < nfcFrames.size(); index++) {
            NtagSessionUtils.waitCpuGetSRAMData(nfca);
            NfcFrame mf = nfcFrames.get(index);
            LogUtil.outLog("req--------------------" + index);
            LogUtil.outLog(getStrByBytes(mf.getNfcFrameByte()));
            LogUtil.outLog("getStrByBytes");
            NtagI2CCommandUtils.fastWrite(nfca, mf.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
                    (byte) Constants.SRAM_END);
            LogUtil.outLog("waitCpuPrepareSRAMData");
            int resultCode = NtagSessionUtils.waitCpuPrepareSRAMDataWithRetry(nfca);
            if (resultCode == Constants.NFC_ERROR_CODE) {
                retryCount++;
                readWithDoTry(nfca, nfcFrames, modbuslist, index, retryCount, resParameter, listener);
            } else {
                retryCount = 0;
                byte res[] = readSRAMFrame(nfca);
                LogUtil.outLog("res----------------------------------------------------" + index);
                LogUtil.outLog(getStrByBytes(res));
                int nfcFrameNumber = ByteArrayUtils.bytesToInt(res, Constants.FRAME_NUMBER_START_INDEX, Constants.FRAME_NUMBER_LENGTH);
                if (nfcFrameNumber == 1) {
                    if (NfcFrameUtil.checkHeadAndDataCRC(res) && res[Constants.FUNCTION_CODE_INDEX] == Constants.READ_FUNCTION_CODE) {
                        ModbusFrameUtil.bytesToUiDataMap(modbuslist.get(index), Arrays.copyOfRange(res, Constants.READ_DATA_START_INDEX, Constants.READ_DATA_START_INDEX + res[Constants.READ_SIZE_INDEX]), resParameter);
                    } else {
                        throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
                    }
                } else {
                    byte modbusFrameData[] = new byte[Constants.MODBUS_READ_MAXIMUM_SIZE];
                    int startIndex = 0;
                    if (NfcFrameUtil.checkHeadCRC(res) && res[Constants.FUNCTION_CODE_INDEX] == Constants.READ_FUNCTION_CODE) {
                        // ByteArrayUtils.bytesToInt(res, FRAME_SIZE_INDEX, FRAME_SIZE_LENGTH) : is the value of "Frame size"
                        byte firstNFCFrameData[] = Arrays.copyOfRange(res, Constants.FUNCTION_CODE_INDEX,
                                ByteArrayUtils.bytesToInt(res, Constants.FRAME_SIZE_INDEX, Constants.FRAME_NUMBER_LENGTH));
                        System.arraycopy(firstNFCFrameData, 0, modbusFrameData, startIndex, firstNFCFrameData.length);
                        startIndex = firstNFCFrameData.length;
                    } else {
                        throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
                    }
                    for (int ii = 1; ii < nfcFrameNumber; ii++) {
                        resultCode = NtagSessionUtils.waitCpuPrepareSRAMDataWithRetry(nfca);
                        if (resultCode == Constants.NFC_ERROR_CODE) {
                            retryCount++;
                            readWithDoTry(nfca, nfcFrames, modbuslist, index, retryCount, resParameter, listener);
                        } else {
                            retryCount = 0;
                            byte nextNFCFrame[] = readSRAMFrame(nfca);
                            byte nextData[] = Arrays.copyOfRange(nextNFCFrame, Constants.FUNCTION_CODE_INDEX,
                                    (ByteArrayUtils.bytesToInt(nextNFCFrame, Constants.FRAME_SIZE_INDEX, Constants.FRAME_NUMBER_LENGTH) - Constants.CRC_LENGTH));
                            System.arraycopy(nextData, 0, modbusFrameData, startIndex, nextData.length);
                            startIndex += nextData.length;
                            if (ii == (nfcFrameNumber - 1)) {
                                byte resCrc[] = Arrays.copyOfRange(nextNFCFrame, ByteArrayUtils.bytesToInt(nextNFCFrame, 7, 2) - Constants.CRC_LENGTH,
                                        ByteArrayUtils.bytesToInt(nextNFCFrame, 7, 2));
                                byte dataCrc[] = CRC16.crc16(Arrays.copyOfRange(modbusFrameData, 0, startIndex));
                                if (ByteArrayUtils.isSameArray(resCrc,dataCrc)) {
                                    ModbusFrameUtil.bytesToUiDataMap(modbuslist.get(index), Arrays.copyOfRange(modbusFrameData, 2, modbusFrameData.length), resParameter);
                                } else {
                                    throw new FunctionCodeErrorException(nextNFCFrame[Constants.ERROR_CODE_INDEX]);
                                }
                            }
                        }
                    }
                }
                if (listener != null) {
                    listener.onWriteSRAM((int) (((float) index / (nfcFrames.size() - 1)) * Constants.PROGRESS_MAXIMUM_VALUES));
                }
            }
        }

        return resParameter;
    }

    /**
     * Send a written request to the CPU via SRAM
     *
     * @param nfca
     * @return return response lists
     * @throws IOException
     * @throws FormatException
     */
    public static void writeOneModbusFrameBySRAM(NfcA nfca, List<NfcFrame> list) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException, FunctionCodeErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (list == null)
            throw new IllegalArgumentException("Lists is not null");
        for (int i = 0; i < list.size(); i++) {
            NfcFrame frame = list.get(i);
            long waitWriteTime = System.currentTimeMillis();
            NtagSessionUtils.waitCpuGetSRAMData(nfca);
            LogUtil.outLog("waitWriteTime=" + (System.currentTimeMillis() - waitWriteTime));
            LogUtil.outLog(getStrByBytes(frame.getNfcFrameByte()));

            long writeFrameTime = System.currentTimeMillis();
            NtagI2CCommandUtils.fastWrite(nfca, frame.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
                    (byte) Constants.SRAM_END);
            LogUtil.outLog("writeFrameTime=" + (System.currentTimeMillis() - writeFrameTime));
        }
        long waitcpuTime = System.currentTimeMillis();
        NtagSessionUtils.waitCpuPrepareSRAMData(nfca);
        LogUtil.outLog("waitcpuTime=" + (System.currentTimeMillis() - waitcpuTime));

        long readFrameTime = System.currentTimeMillis();
        byte res[] = readSRAMFrame(nfca);
        LogUtil.outLog(getStrByBytes(res));
        LogUtil.outLog("readFrameTime=" + (System.currentTimeMillis() - readFrameTime));

        long checkCRC = System.currentTimeMillis();
        if (!NfcFrameUtil.checkHeadAndDataCRC(res) || res[Constants.FUNCTION_CODE_INDEX] != Constants.WRITE_FUNCTION_CODE) {
            throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
        }
        LogUtil.outLog("checkCRC=" + (System.currentTimeMillis() - checkCRC));
    }


    /**
     * Send a written request to the CPU via SRAM
     *
     * @param nfca
     * @return return response lists
     * @throws IOException
     * @throws FormatException
     */
    private static int i = 0;

    public static int writeOneFirmwareModbusFrameBySRAM(NfcA nfca, List<NfcFrame> list, boolean isErrorDuringWriteNFCFrame) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException, FunctionCodeErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (list == null)
            throw new IllegalArgumentException("Lists is not null");

        if (!isErrorDuringWriteNFCFrame) {
            i = 0;
        }
        for (; i < list.size(); i++) {
            LogUtil.saveLogToFile("i=============" + i);
            NfcFrame frame = list.get(i);
            long waitWriteTime = System.currentTimeMillis();
            NtagSessionUtils.waitCpuGetSRAMData(nfca);
            LogUtil.outLog("waitWriteTime=" + (System.currentTimeMillis() - waitWriteTime));
            LogUtil.outLog(getStrByBytes(frame.getNfcFrameByte()));

            NtagI2CCommandUtils.fastWrite(nfca, frame.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
                    (byte) Constants.SRAM_END);

        }
        return readFirmwareResponse(nfca);
    }

    public static int readFirmwareResponse(NfcA nfca) {
        LogUtil.saveLogToFile("readFirmwareResponse() ");
        try {
            NtagSessionUtils.waitCpuPrepareSRAMData(nfca);
            byte res[] = readSRAMFrame(nfca);
            LogUtil.outLog(getStrByBytes(res));
            long checkCRC = System.currentTimeMillis();
            if (!NfcFrameUtil.checkHeadAndDataCRC(res) || res[Constants.FUNCTION_CODE_INDEX] != Constants.WRITE_FUNCTION_CODE) {
                throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
            }
            LogUtil.outLog("checkCRC=" + (System.currentTimeMillis() - checkCRC));
        } catch (Exception e) {
            return Constants.FIRMWARE_UPDATE_ERROR_CODE;
        }

        return Constants.FIRMWARE_UPDATE_SUCCESS_CODE;
    }


    /**
     * Send a read request to the CPU via SRAM
     *
     * @param nfca
     * @return Data of the modbus frame
     * @throws IOException
     * @throws FormatException
     * @throws OutTimeException
     */
    public static byte[] readOneNfcFrame(NfcA nfca, NfcFrame mf) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException, FunctionCodeErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (mf == null)
            throw new IllegalArgumentException("Lists is not null");
        NtagSessionUtils.waitCpuGetSRAMData(nfca);
        LogUtil.outLog(getStrByBytes(mf.getNfcFrameByte()));
        NtagI2CCommandUtils.fastWrite(nfca, mf.getNfcFrameByte(), (byte) Constants.SRAM_BEGIN,
                (byte) Constants.SRAM_END);
        NtagSessionUtils.waitCpuPrepareSRAMData(nfca);
        byte res[] = readSRAMFrame(nfca);
        LogUtil.outLog(getStrByBytes(res));
        if (!NfcFrameUtil.checkHeadAndDataCRC(res) || res[Constants.FUNCTION_CODE_INDEX] != Constants.READ_FUNCTION_CODE) {
            throw new FunctionCodeErrorException(res[Constants.ERROR_CODE_INDEX]);
        }
        return Arrays.copyOfRange(res, Constants.READ_DATA_START_INDEX, Constants.READ_DATA_START_INDEX + res[Constants.READ_SIZE_INDEX]);

    }

    /**
     * @param nfca
     * @return Read data form SRAM
     * @throws IOException
     * @throws FormatException
     */
    public static byte[] readSRAMFrame(NfcA nfca) throws IOException, FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        return NtagI2CCommandUtils.fastRead(nfca, (byte) Constants.SRAM_BEGIN, (byte) Constants.SRAM_END);
    }

    /**
     * Decide whether to support i2c
     *
     * @param nfca
     * @return support true nonsupport return false
     * @throws IOException
     */
    public static boolean isNtagI2C2KPlus(NfcA nfca) throws IOException, WriteErrorException {
        if (null == nfca)
            return false;
        if (!nfca.isConnected())
            nfca.connect();
        byte version[] = NtagI2CCommandUtils.getVersion(nfca);
        if (nfca.isConnected()) {
            nfca.close();
        }
        if (ByteArrayUtils.isSameArray(version, Constants.I2C_2K_PLUS))
            return true;
        else
            return false;
    }

    /**
     * byte array format to string (only out log)
     */
    public static String getStrByBytes(byte bb[]) {
        int i = 0;
        String str = "";
        for (byte b : bb) {
            str += String.format("%02x", b) + "  ";
            i++;
            if (i == 16) {
                str += "\r\n";
                i = 0;
            }
        }
        return str;
    }

    public static boolean isMifareClassicCard(Tag tag) {
        if (null == tag) {
            return false;
        }
        String[] techList = tag.getTechList();
        for (String tech : techList) {
            if (tech.indexOf("MifareClassic") >= 0) {
                return true;
            }
        }
        return false;
    }


//    public final static byte APP_KEY_CRC_ADDRESS = 0X44;
//
//    /**
//     * Write array to EEPROM
//     *
//     * @param nfca
//     * @param data         write data to I2C
//     * @param beginAddress start address
//     * @param sector       The sector that writes to the data
//     * @param listener
//     * @param crcAddress
//     * @param isCrc
//     * @throws UnsupportedEncodingException
//     * @throws FormatException
//     * @throws IOException
//     * @throws WriteErrorException
//     */
//    public static void writeEEPROM(NfcA nfca, byte[] data, byte beginAddress, byte sector, EEPROMListener listener, byte crcAddress, boolean isCrc)
//            throws IOException, FormatException, WriteErrorException {
//        if (nfca == null)
//            throw new IllegalArgumentException("Nfca is not null");
//        if (data == null)
//            throw new IllegalArgumentException("data is not null");
//        if (sector < 0)
//            throw new IllegalArgumentException("Sector must be greater than or equals to zero");
//        if ((beginAddress + Math.ceil((double) data.length / 4)) > 0xff) {
//            throw new IllegalArgumentException("data size is beyond the address range");
//        }
//        NtagI2CCommandUtils.selectSector(nfca, sector);
//        byte[] temp;
//        int index = 0;
//        byte page = beginAddress;
//        for (index = 0; index < data.length; index += 4) {
//            // NTAG I2C Plus sits the Config registers in Sector 0
//            if (page == (byte) 0xE2) {
//                break;
//            }
//            temp = Arrays.copyOfRange(data, index, index + 4);
//            NtagI2CCommandUtils.write(nfca, temp, page);
//            page++;
//            // Inform the listener about the writing
//            if (listener != null) {
//                listener.onWriteEEPROMProgressLisenter((index + 4) / data.length);
//            }
//
//        }
//        if (isCrc) {
//            byte intCrc[] = CRC16.crc16(data);
//            NtagI2CCommandUtils.write(nfca, intCrc, (byte) crcAddress);
//        }
//    }
//
//    /**
//     * Read array to EEPROM
//     *
//     * @param nfca
//     * @param beginAddress begin address
//     * @param size         end address
//     * @param sector       sector
//     * @return Read data form EEPROM
//     * @throws IOException
//     * @throws FormatException
//     */
//    public static byte[] readEEPROM(NfcA nfca, byte beginAddress, int size, byte sector) throws IOException, FormatException, WriteErrorException {
//        if (nfca == null)
//            throw new IllegalArgumentException("Nfca is not null");
//        if (size <= 0)
//            throw new IllegalArgumentException("Size must be greater than zero");
//        if (sector < 0)
//            throw new IllegalArgumentException("Sector must be greater than or equals to zero");
//        if ((beginAddress + Math.ceil((double) size / 4)) > 0xff) {
//            throw new IllegalArgumentException("data size is beyond the address range");
//        }
//        NtagI2CCommandUtils.selectSector(nfca, sector);
//        // e.g. size=8(2 pages), begin page=0x04, end page should be 0x05
//        int endAddress = (int) (beginAddress + Math.ceil(((double) size) / 4) - 1);
//        byte[] data = NtagI2CCommandUtils.fastRead(nfca, beginAddress, (byte) endAddress);
//        return data;
//    }

}
