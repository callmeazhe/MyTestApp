package com.example.mytestapp.nfc.modbus;



import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.utils.ByteArrayUtils;

import java.util.Arrays;


/**
 * Created by 30035027 on 8/8/2017.
 * Nfc frame
 */

public class NfcFrame {
    public final static int NFC_FRAME_INDEX = 0;
    public final static int NFC_FRAME_VERSION_INDEX = 1;
    public final static int NFC_SEQUENCE_NO_INDEX = 2;
    public final static int NFC_NUMBER_OF_NFC_FRAME_START_INDEX = 3;
    public final static int NFC_FRAME_NO_START_INDEX = 5;
    public final static int NFC_FRAME_SIZE_START_INDEX = 7;
    public final static int NFC_DIRECTION_INDEX = 9;
    public final static int NFC_RESERVED_START_INDEX = 10;
    public final static int NFC_CRC_START_INDEX = 14;
    public final static int NFC_DATA_START_INDEX = 16;
    public final static int NFC_FRAME_LENGTH = 64;
    private static final int DATA_LENGTH_2 = 2;

    private byte[] nfcFrame = new byte[NFC_FRAME_LENGTH];
    private byte frameId;
    private byte frameVersion;
    private byte sequenceNo;
    private int numberOfNfcFrame;
    private int frameNO;
    private int frameSize;
    private byte nfcDirection;
    private int reserved;
    private byte[] headCRC;
    private byte[] psdu;

    public byte[] getNfcFrameByte() {
        return nfcFrame;
    }

    public NfcFrame() {
    }

    public byte getFrameId() {
        return frameId;
    }

    public NfcFrame setFrameId(byte frameId) {
        this.frameId = frameId;
        nfcFrame[NFC_FRAME_INDEX] = frameId;
        return this;
    }

    public byte getFrameVersion() {
        return frameVersion;
    }

    public NfcFrame setFrameVersion(byte frameVersion) {
        this.frameVersion = frameVersion;
        nfcFrame[NFC_FRAME_VERSION_INDEX] = frameVersion;
        return this;
    }

    public byte getSequenceNo() {
        return sequenceNo;
    }

    public NfcFrame setSequenceNo(byte sequenceNo) {
        this.sequenceNo = sequenceNo;
        nfcFrame[NFC_SEQUENCE_NO_INDEX] = sequenceNo;
        return this;
    }

    public int getNumberOfNfcFrame() {
        return numberOfNfcFrame;
    }

    public NfcFrame setNumberOfNfcFrame(int numberOfNfcFrame) {
        this.numberOfNfcFrame = numberOfNfcFrame;
        System.arraycopy(ByteArrayUtils.intToBytes(numberOfNfcFrame, 2),
                0, nfcFrame, NFC_NUMBER_OF_NFC_FRAME_START_INDEX, DATA_LENGTH_2);
        return this;
    }

    public int getFrameNO() {
        return frameNO;
    }

    public NfcFrame setFrameNO(int frameNO) {
        this.frameNO = frameNO;
        System.arraycopy(ByteArrayUtils.intToBytes(frameNO, 2), 0, nfcFrame, NFC_FRAME_NO_START_INDEX, DATA_LENGTH_2);
        return this;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public NfcFrame setFrameSize(int frameSize) {
        this.frameSize = frameSize;
        System.arraycopy(ByteArrayUtils.intToBytes(frameSize, 2), 0, nfcFrame, NFC_FRAME_SIZE_START_INDEX, DATA_LENGTH_2);
        return this;
    }

    public byte getNfcDirection() {
        return nfcDirection;
    }

    public NfcFrame setNfcDirection(byte nfcDirection) {
        this.nfcDirection = nfcDirection;
        nfcFrame[NFC_DIRECTION_INDEX] = nfcDirection;
        return this;
    }

    public int getReserved() {
        return reserved;
    }

    public NfcFrame setReserved(int reserved) {
        this.reserved = reserved;
        byte reservedByte[] = new byte[reserved];
        Arrays.fill(reservedByte, (byte) 0);
        System.arraycopy(reservedByte, 0, nfcFrame, NFC_RESERVED_START_INDEX, reserved);
        return this;
    }


    public byte[] getHeadCRC() {
        return headCRC;

    }

    public NfcFrame setHeadCRC() {
        this.headCRC = CRC16.crc16(Arrays.copyOfRange(nfcFrame, 0, NFC_CRC_START_INDEX));
        System.arraycopy(headCRC, 0, nfcFrame, NFC_CRC_START_INDEX, 2);
        return this;
    }

    public byte[] getPsdu() {
        return psdu;
    }

    public NfcFrame setPsdu(byte[] psdu) {
        this.psdu = psdu;
        System.arraycopy(psdu, 0, nfcFrame, NFC_DATA_START_INDEX, psdu.length);
        return this;
    }

}
