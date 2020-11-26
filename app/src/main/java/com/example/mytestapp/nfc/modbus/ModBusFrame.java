package com.example.mytestapp.nfc.modbus;


import com.example.mytestapp.nfc.utils.ByteArrayUtils;

/**
 * ModBus Frame
 * Created by 30035027 on 11/10/2017.
 */

public class ModBusFrame {
    private static final int MODBUS_FRAME_SIZE = 255;
    private static final int MODBUS_FUNCTION_CODE_INDEX = 0;
    private static final int MODBUS_ADDRESS_START_INDEX = 1;
    private static final int DATA_LENGTH_2 = 2;
    private static final int MODBUS_QUANTITY_REGISTERS_INDEX = 3;
    private static final int MODBUS_SIZE_INDEX = 5;
    private static final int MODBUS_DATA_START_INDEX = 6;
    private byte ModBusFrameBytes[] = new byte[MODBUS_FRAME_SIZE];
    private int functionCode;
    private int startAddress;
    private int quantityRegisters;
    private int size;
    private byte[] crc;
    private byte[] data;

    public int getFunctionCode() {
        return functionCode;
    }

    public byte[] getModBusFrameBytes() {
        return ModBusFrameBytes;
    }

    public ModBusFrame setFunctionCode(int functionCode) {
        ModBusFrameBytes[MODBUS_FUNCTION_CODE_INDEX] = (byte) functionCode;
        this.functionCode = functionCode;
        return this;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public ModBusFrame setStartAddress(int startAddress) {
        System.arraycopy(ByteArrayUtils.intToBytes(startAddress, 2), 0, ModBusFrameBytes, MODBUS_ADDRESS_START_INDEX, DATA_LENGTH_2);
        this.startAddress = startAddress;
        return this;
    }

    public int getQuantityRegisters() {
        return quantityRegisters;
    }

    public ModBusFrame setQuantityRegisters(int quantityRegisters) {
        System.arraycopy(ByteArrayUtils.intToBytes(quantityRegisters, 2), 0, ModBusFrameBytes, MODBUS_QUANTITY_REGISTERS_INDEX, DATA_LENGTH_2);
        this.quantityRegisters = quantityRegisters;
        return this;
    }

    public int getSize() {
        return size;
    }

    public ModBusFrame setSize(int size) {
        ModBusFrameBytes[MODBUS_SIZE_INDEX] = (byte) size;
        this.size = size;
        return this;
    }

    public byte[] getCrc() {
        return crc;
    }

    public ModBusFrame setCrc(byte[] crc, int startIndex) {
        System.arraycopy(crc, 0, ModBusFrameBytes, startIndex, DATA_LENGTH_2);
        this.crc = crc;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public ModBusFrame setData(byte[] data) {
        System.arraycopy(data, 0, ModBusFrameBytes, MODBUS_DATA_START_INDEX, data.length);
        this.data = data;
        return this;
    }
}
