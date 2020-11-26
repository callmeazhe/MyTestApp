package com.example.mytestapp.nfc.modbus;

/**
 * Created by 30035027 on 10/12/2017.
 * UI parameter Bean
 */

public class ModbusDataBean {
    private String key;
    private Object values;
    private int dataType;
    //unit byte
    private int dataLength;
    private int startAddress;

    /**
     * @param key          read or write value's key
     * @param values       read or write value
     * @param dataType     read or write value's type
     * @param dataLength   NFC Interface data length(unit --> byte)
     * @param startAddress NFC Interface data's start address
     */
    public ModbusDataBean(String key, Object values, int dataType, int dataLength, int startAddress) {
        this.key = key;
        this.values = values;
        this.dataType = dataType;
        this.dataLength = dataLength;
        this.startAddress = startAddress;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValues() {
        return values;
    }

    public void setValues(Object values) {
        this.values = values;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }
}
