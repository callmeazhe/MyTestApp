package com.example.mytestapp.nfc.modbus;

/**
 * Created by 30035027 on 11/21/2017.
 */

public interface Constants {
    //ModBus protocol
    int WRITE_FUNCTION_CODE = 0x10;
    int READ_FUNCTION_CODE = 0x03;
    int WRITE_MODBUS_HEAD_LENGTH = 6;
    int READ_MODBUS_HEAD_LENGTH = 5;
    int CRC_LENGTH = 2;
    int MODBUS_READ_MAXIMUM_SIZE = 255;
    //Nfc protocol
    int NFC_PSDU_LENGTH = 48;
    int NFC_HEAD_LENGTH = 16;
    int UNSIGNED_BYTE_MAXIMUM_VALUES = 256;
    int FRAME_IDENTIFY = 0x69;
    int FRAME_VERSION = 0x00;
    int NFC_DIRECTION_WRITE = 0x01;
    int NFC_DIRECTION_READ = 0x01;
    int HEAD_CRC_START_INDEX = 14;
    int HEAD_LENGTH = 16;
    int READ_FRAME_SIZE = 23;
    int READ_NUMBER_OF_NFC_FRAME = 1;
    int READ_FRAME_NUMBER = 0;
    int RESERVED = 4;
    int FUNCTION_CODE_INDEX = 16;
    int ERROR_CODE_INDEX = 17;
    int PROGRESS_MAXIMUM_VALUES = 100;
    int FRAME_NUMBER_START_INDEX = 3;
    int FRAME_NUMBER_LENGTH = 2;
    int READ_DATA_START_INDEX = 18;
    int READ_SIZE_INDEX = 17;
    int FRAME_SIZE_INDEX = 7;


    //Session
    int WAIT_TIME = 10;
    int LOOP_COUNT_500 = 500;
    int LOOP_COUNT_1000 = 1000;
    byte PV_READ_FLG_ADDRESS = 0x04;
    int WAIT_TOTAL_TIME = 10000;
    /**
     * Special Registers of the NTAG I2C.
     */

    byte SESSION_PLUS = (byte) 0xEC;
    byte CONFIGURATION = (byte) 0xE8;
    byte SRAM_BEGIN = (byte) 0xF0;
    byte SRAM_END = (byte) 0xFF;


    /**
     * I2C PLUS VERSION CODE
     */
    byte I2C_1K_PLUS[] = new byte[]{0x00, 0x04, 0x04, 0x05, 0x02, 0x02, 0x13, 0x03};
    byte I2C_2K_PLUS[] = new byte[]{0x00, 0x04, 0x04, 0x05, 0x02, 0x02, 0x15, 0x03};

    int NFC_ERROR_CODE = -1;
    int NFC_RETRY_COUNT = 3;


    int FIRMWARE_UPDATE_SUCCESS_CODE = 200;
    int FIRMWARE_UPDATE_ERROR_CODE = 500;
    String CHARSET_UTF8 = "UTF-8";

}
