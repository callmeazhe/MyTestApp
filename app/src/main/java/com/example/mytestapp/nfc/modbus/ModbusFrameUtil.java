package com.example.mytestapp.nfc.modbus;


import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.utils.ByteArrayUtils;
import com.example.mytestapp.nfc.utils.LogUtil;
import com.example.mytestapp.nfc.utils.NtagI2CUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by 30035027 on 10/12/2017.
 * Create the modBus communication frame,and parse frame
 */

public class ModbusFrameUtil {
    public static final int MODBUS_INT = 1;
    public static final int MODBUS_STR = 2;
    public static final int MODBUS_FLOAT16 = 3;
    public static final int MODBUS_HEX_STR = 4;
    public static final int MODBUS_FLOAT = 5;
    public static final int MODBUS_BYTE_ARR = 6;
    public static final int MODBUS_DOUBLE = 7;

    /**
     * Generate write's request ModBus byte array collection
     */
    public static List<ModBusFrame> toModBusWriteFrameList(ArrayList<ArrayList<ModbusDataBean>> modBusList) throws UnsupportedEncodingException {
        if (modBusList == null)
            throw new IllegalArgumentException("modBusList is not null");
        List<ModBusFrame> frames = new ArrayList<>();
        for (int i = 0; i < modBusList.size(); i++) {
            ModBusFrame modBusFrame = new ModBusFrame();
            ArrayList<ModbusDataBean> frameList = modBusList.get(i);
            //frame size unit is byte
            int frameDataLength = getFrameSize(frameList);
            if (frameDataLength % 2 != 0) {
                frameDataLength = frameDataLength + 1;
            }
            //All data
            byte frameData[] = getFrameTotalData(frameList, frameDataLength);

            modBusFrame.setFunctionCode(Constants.WRITE_FUNCTION_CODE)
                    .setStartAddress(frameList.get(0).getStartAddress())
                    .setQuantityRegisters((int) Math.ceil(frameDataLength / 2f))
                    .setSize(frameDataLength)
                    .setData(frameData);
            byte crc[] = CRC16.crc16(Arrays.copyOfRange(modBusFrame.getModBusFrameBytes(), 0, Constants.WRITE_MODBUS_HEAD_LENGTH + frameDataLength));
            modBusFrame.setCrc(crc, Constants.WRITE_MODBUS_HEAD_LENGTH + frameDataLength);
            frames.add(modBusFrame);
        }
        return frames;
    }

    private static int getFrameSize(List<ModbusDataBean> frameList) {
        int length = 0;
        if (frameList != null && frameList.size() > 0) {
            for (ModbusDataBean mModbusDataBean : frameList) {
                length += mModbusDataBean.getDataLength();
            }
        }
        return length;
    }


    /**
     * Generate read's request ModBus byte array collection
     */
    public static List<ModBusFrame> toModBusReadFrameList(ArrayList<ArrayList<ModbusDataBean>> modBusList) throws UnsupportedEncodingException {
        if (modBusList == null)
            throw new IllegalArgumentException("modbuslist is not null");
        List<ModBusFrame> frames = new ArrayList<>();
        for (int i = 0; i < modBusList.size(); i++) {
            ModBusFrame modBusFrame = new ModBusFrame();
            ArrayList<ModbusDataBean> frameList = modBusList.get(i);
            //frame size unit byte
            int frameDataLength = getFrameSize(frameList);
            //All data with frame
            modBusFrame.setFunctionCode(Constants.READ_FUNCTION_CODE)
                    .setStartAddress(frameList.get(0).getStartAddress())
                    .setQuantityRegisters((int) Math.ceil(frameDataLength / 2f));
            byte crc[] = CRC16.crc16(Arrays.copyOfRange(modBusFrame.getModBusFrameBytes(), 0, Constants.READ_MODBUS_HEAD_LENGTH));
            modBusFrame.setCrc(crc, Constants.READ_MODBUS_HEAD_LENGTH);
            frames.add(modBusFrame);
        }
        return frames;
    }


    /**
     * UI object data to ModBus byte data
     */

    public static byte[] getFrameTotalData(ArrayList<ModbusDataBean> modBusDataList, int frameDataLength) throws UnsupportedEncodingException {
        if (modBusDataList == null)
            throw new IllegalArgumentException("modbuslist is not null");

        byte[] totalityData = new byte[frameDataLength];
        int startIndex = 0;
        for (int j = 0; j < modBusDataList.size(); j++) {
            ModbusDataBean mb = modBusDataList.get(j);
            byte[] data = null;
            switch (mb.getDataType()) {
                case MODBUS_INT:
                    data = ByteArrayUtils.intToBytes(Integer.valueOf(mb.getValues()+""), mb.getDataLength());
                    break;
                case MODBUS_STR:
                    data = ByteArrayUtils.stringToByteArray((String) mb.getValues(), mb.getDataLength(), "utf-8");
                    break;
                case MODBUS_FLOAT16:
                    data = ByteArrayUtils.intToBytes(ByteArrayUtils.floatToHalfFloat((Float) mb.getValues()), (mb.getDataLength()));
                    break;
                case MODBUS_HEX_STR:
                    data = ByteArrayUtils.hexStringToByte((String) mb.getValues());
                    break;
                case MODBUS_FLOAT:
                    data = ByteArrayUtils.floatToBytes(Float.valueOf(mb.getValues()+""));
                    break;
                case MODBUS_BYTE_ARR:
                    data = (byte[]) mb.getValues();
                    break;
                case MODBUS_DOUBLE:
                    data = ByteArrayUtils.doubleToByte((double) mb.getValues());
                    break;
            }
            System.arraycopy(data, 0, totalityData, startIndex, mb.getDataLength());
            startIndex += mb.getDataLength();
        }
        return totalityData;
    }
    /**
     * Bytes to UI object data
     */
    public static void bytesToUiDataMap(ArrayList<ModbusDataBean> modbusDataList, byte frameData[], Map<String, Object> resParameter) throws UnsupportedEncodingException {
        if (modbusDataList == null)
            throw new IllegalArgumentException("modbusList is not null");
        if (frameData == null)
            throw new IllegalArgumentException("frameData is not null");
        int startIndex = 0;
        for (int j = 0; j < modbusDataList.size(); j++) {
            ModbusDataBean mb = modbusDataList.get(j);
            Object values = null;
            byte parameterByte[] = Arrays.copyOfRange(frameData, startIndex, startIndex + mb.getDataLength());
            switch (mb.getDataType()) {
                case MODBUS_INT:
                    values = ByteArrayUtils.bytesToInt(parameterByte, 0, parameterByte.length);
                    break;
                case MODBUS_STR:
                    values = ByteArrayUtils.byteArrayToString(parameterByte, Constants.CHARSET_UTF8);
                    values = values == null ? "" : values;
                    break;
                case MODBUS_FLOAT16:
                    values = ByteArrayUtils.halfFloatToFloat(ByteArrayUtils.bytesToInt(parameterByte, 0, parameterByte.length));
                    break;
                case MODBUS_HEX_STR:
                    values = ByteArrayUtils.bytesToHexString(parameterByte);
                    break;
                case MODBUS_FLOAT:
                    values = ByteArrayUtils.bytesToFloat(parameterByte);
                    break;
                case MODBUS_BYTE_ARR:
                    values = parameterByte;
                    break;

            }
            LogUtil.outLog(mb.getKey() + "=" + NtagI2CUtils.getStrByBytes(parameterByte));
            if (values!=null){
                LogUtil.outLog(mb.getKey() + "=" + values);
            }
            resParameter.put(mb.getKey(), values);
            startIndex += mb.getDataLength();
        }
    }


    /**
     * Generate write's request ModBus frame
     */
    public static ModBusFrame toModBusWriteFrame(byte[] frameData, int startAddress) throws UnsupportedEncodingException {
        if (frameData == null)
            throw new IllegalArgumentException("modbuslist is not null");
        int dataLength = frameData.length;
        if (dataLength % 2 != 0) {
            dataLength = dataLength + 1;
        }
        ModBusFrame modBusFrame = new ModBusFrame();
        modBusFrame.setFunctionCode(Constants.WRITE_FUNCTION_CODE)
                .setStartAddress(startAddress)
                .setQuantityRegisters((int) Math.ceil(dataLength / 2f))
                .setSize(dataLength)
                .setData(frameData);
        byte crc[] = CRC16.crc16(Arrays.copyOfRange(modBusFrame.getModBusFrameBytes(), 0, Constants.WRITE_MODBUS_HEAD_LENGTH + dataLength));
        modBusFrame.setCrc(crc, Constants.WRITE_MODBUS_HEAD_LENGTH + dataLength);
        return modBusFrame;
    }

    /**
     * Generate read's request ModBus frame
     */
    public static ModBusFrame toModBusReadFrame(int length, int StartAddress) throws UnsupportedEncodingException {

        ModBusFrame modBusFrame = new ModBusFrame();
        modBusFrame.setFunctionCode(Constants.READ_FUNCTION_CODE)
                .setStartAddress(StartAddress)
                .setQuantityRegisters((int) Math.ceil(length / 2f));
        byte crc[] = CRC16.crc16(Arrays.copyOfRange(modBusFrame.getModBusFrameBytes(), 0, Constants.READ_MODBUS_HEAD_LENGTH));
        modBusFrame.setCrc(crc, Constants.READ_MODBUS_HEAD_LENGTH);
        return modBusFrame;
    }


}
