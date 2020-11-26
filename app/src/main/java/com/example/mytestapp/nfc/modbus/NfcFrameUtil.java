package com.example.mytestapp.nfc.modbus;


import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.utils.ByteArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Nfc frame tool class
 * Created by 30035027 on 11/10/2017.
 */

public class NfcFrameUtil {


    /**
     * ModBus frame to Nfc write Frame
     * ArrayList<ArrayList<NfcFrame>>  ArrayList<NfcFrame> Nfc frames of one modbus frame
     */
    public static ArrayList<ArrayList<NfcFrame>> toNfcWriteFrameList(List<ModBusFrame> modBusList) {
        if (modBusList == null)
            throw new IllegalArgumentException("modBusList is not null");

        ArrayList<ArrayList<NfcFrame>> nfcList = new ArrayList();
        for (int i = 0; i < modBusList.size(); i++) {
            ArrayList<NfcFrame> framesOfOneModbus = new ArrayList<>();
            ModBusFrame modBusFrame = modBusList.get(i);
            byte[] modBusFrameBytes = modBusFrame.getModBusFrameBytes();
            int modBusFrameLength = modBusFrame.getQuantityRegisters() * 2 + Constants.WRITE_MODBUS_HEAD_LENGTH + Constants.CRC_LENGTH;
            // one NFC frame is 48 bytes
            // there is CRC(2bytes) for each NFC frame
            int nfcFrameNum = (int) Math.ceil((float) modBusFrameLength / Constants.NFC_PSDU_LENGTH);
            for (int j = 0; j < nfcFrameNum; j++) {
                byte nfcFramePsdu[] = new byte[Constants.NFC_PSDU_LENGTH];
                int psduLen = 0;
                int endLen = 0;
                if (modBusFrameLength % Constants.NFC_PSDU_LENGTH != 0 && (j + 1) == nfcFrameNum) {
                    psduLen = modBusFrameLength - (nfcFrameNum - 1) * Constants.NFC_PSDU_LENGTH;
                    endLen = modBusFrameLength;
                } else {
                    psduLen = Constants.NFC_PSDU_LENGTH;
                    endLen = (j + 1) * Constants.NFC_PSDU_LENGTH;
                }
                byte temp[] = Arrays.copyOfRange(modBusFrameBytes, Constants.NFC_PSDU_LENGTH * j, endLen);
                System.arraycopy(temp, 0, nfcFramePsdu, 0, temp.length);
                NfcFrame mNfcFrame = new NfcFrame();
                mNfcFrame.setFrameId((byte) Constants.FRAME_IDENTIFY)
                        .setFrameVersion((byte) Constants.FRAME_VERSION)
                        // One Modbus frame should be one sequence num
                        .setSequenceNo((byte) (i % Constants.UNSIGNED_BYTE_MAXIMUM_VALUES))
                        .setNumberOfNfcFrame(nfcFrameNum)
                        .setFrameNO((byte) j)
                        // Header+PSD,  include CRC
                        .setFrameSize(psduLen + Constants.NFC_HEAD_LENGTH)
                        .setNfcDirection((byte) Constants.NFC_DIRECTION_WRITE)
                        .setReserved(Constants.RESERVED)
                        .setHeadCRC()
                        .setPsdu(nfcFramePsdu);
                framesOfOneModbus.add(mNfcFrame);
            }
            nfcList.add(framesOfOneModbus);
        }
        return nfcList;
    }


    /**
     * ModBus frame to Nfc read Frame
     */
    public static List<NfcFrame> toNfcReadFrameList(List<ModBusFrame> modbuslist) {
        if (modbuslist == null)
            throw new IllegalArgumentException("modbuslist is not null");
        List<NfcFrame> frames = new ArrayList<>();
        for (int j = 0; j < modbuslist.size(); j++) {
            ModBusFrame modBusFrame = modbuslist.get(j);
            byte modbusFrameBytes[] = modBusFrame.getModBusFrameBytes();
            byte nfcFramePsdu[] = new byte[Constants.NFC_PSDU_LENGTH];
            System.arraycopy(modbusFrameBytes, 0, nfcFramePsdu, 0, Constants.READ_MODBUS_HEAD_LENGTH + Constants.CRC_LENGTH);
            NfcFrame mNfcFrame = new NfcFrame();
            //All data with frame

            mNfcFrame.setFrameId((byte) Constants.FRAME_IDENTIFY)
                    .setFrameVersion((byte) Constants.FRAME_VERSION)
                    .setSequenceNo((byte) (j % Constants.UNSIGNED_BYTE_MAXIMUM_VALUES))
                    .setNumberOfNfcFrame(Constants.READ_NUMBER_OF_NFC_FRAME)
                    .setFrameNO((byte) Constants.READ_FRAME_NUMBER)
                    .setFrameSize(Constants.READ_FRAME_SIZE)
                    .setNfcDirection((byte) Constants.NFC_DIRECTION_READ)
                    .setReserved(Constants.RESERVED)
                    .setHeadCRC()
                    .setPsdu(nfcFramePsdu);
            frames.add(mNfcFrame);
        }
        return frames;
    }

    /**
     * ModBus frame to Nfc write Frame
     */
    public static ArrayList<NfcFrame> toNfcWriteFrame(ModBusFrame modBusFrame, byte sequenceNo) {
        if (modBusFrame == null)
            throw new IllegalArgumentException("modBusFrame is not null");

        ArrayList<NfcFrame> frames = new ArrayList<>();
        byte[] modbusFrameBytes = modBusFrame.getModBusFrameBytes();
        //长度 = 寄存器（一个装俩字节） * 2 + 头 6 个
        int modBusFrameLength = modBusFrame.getQuantityRegisters() * 2 + Constants.WRITE_MODBUS_HEAD_LENGTH + Constants.CRC_LENGTH;
        int nfcFrameNum = (int) Math.ceil((float) modBusFrameLength / Constants.NFC_PSDU_LENGTH);
        for (int i = 0; i < nfcFrameNum; i++) {
            byte nfcFramePsdu[] = new byte[Constants.NFC_PSDU_LENGTH];
            int psduLen = 0;
            int endLen = 0;
            if (modBusFrameLength % Constants.NFC_PSDU_LENGTH != 0 && (i + 1) == nfcFrameNum) {
                psduLen = modBusFrameLength - (nfcFrameNum - 1) * Constants.NFC_PSDU_LENGTH;
                endLen = modBusFrameLength;
            } else {
                psduLen = Constants.NFC_PSDU_LENGTH;
                endLen = (i + 1) * Constants.NFC_PSDU_LENGTH;
            }
            byte temp[] = Arrays.copyOfRange(modbusFrameBytes, Constants.NFC_PSDU_LENGTH * i, endLen);
            System.arraycopy(temp, 0, nfcFramePsdu, 0, temp.length);
            NfcFrame mNfcFrame = new NfcFrame();
            mNfcFrame.setFrameId((byte) Constants.FRAME_IDENTIFY)
                    .setFrameVersion((byte) Constants.FRAME_VERSION)
                    .setSequenceNo((byte) (sequenceNo % Constants.UNSIGNED_BYTE_MAXIMUM_VALUES))
                    .setNumberOfNfcFrame(nfcFrameNum)
                    .setFrameNO((byte) i)
                    .setFrameSize(psduLen + Constants.HEAD_LENGTH)
                    .setNfcDirection((byte) Constants.NFC_DIRECTION_WRITE)
                    .setReserved(Constants.RESERVED)
                    .setHeadCRC()
                    .setPsdu(nfcFramePsdu);

            frames.add(mNfcFrame);
        }
        return frames;
    }


    /**
     * ModBus frame to Nfc read Frame
     */
    public static NfcFrame toNfcReadFrame(ModBusFrame modBusFrame) {
        byte modbusFrameBytes[] = modBusFrame.getModBusFrameBytes();
        byte nfcBytes[] = new byte[Constants.NFC_PSDU_LENGTH];
        System.arraycopy(modbusFrameBytes, 0, nfcBytes, 0, Constants.READ_MODBUS_HEAD_LENGTH + Constants.CRC_LENGTH);
        NfcFrame mNfcFrame = new NfcFrame();
        //All data with frame
        mNfcFrame.setFrameId((byte) Constants.FRAME_IDENTIFY)
                .setFrameVersion((byte) Constants.FRAME_VERSION)
                .setSequenceNo((byte) (0 % Constants.UNSIGNED_BYTE_MAXIMUM_VALUES))
                .setNumberOfNfcFrame(Constants.READ_NUMBER_OF_NFC_FRAME)
                .setFrameNO((byte) Constants.READ_FRAME_NUMBER)
                .setFrameSize(Constants.READ_FRAME_SIZE)
                .setNfcDirection((byte) Constants.NFC_DIRECTION_READ)
                .setReserved(Constants.RESERVED)
                .setHeadCRC()
                .setPsdu(nfcBytes);


        return mNfcFrame;
    }

    /**
     * Checked psdu(physical Service Data Unit) and head crc
     */
    public static boolean checkHeadAndDataCRC(byte[] res) {
        if (res != null) {
            int frameSize = ByteArrayUtils.bytesToInt(res, 7, 2);
            byte resData[] = Arrays.copyOfRange(res, Constants.HEAD_LENGTH, frameSize - Constants.CRC_LENGTH);
            byte[] crc = CRC16.crc16(resData);
            byte[] resCrc = Arrays.copyOfRange(res, frameSize - Constants.CRC_LENGTH, frameSize);
            boolean cecPsdu = ByteArrayUtils.isSameArray(resCrc, crc);
            return cecPsdu && checkHeadCRC(res);
        } else
            return false;
    }

    /**
     * Checked head crc
     */
    public static boolean checkHeadCRC(byte[] res) {
        if (res != null) {
            byte[] headCrc = CRC16.crc16(Arrays.copyOfRange(res, 0, Constants.HEAD_CRC_START_INDEX));
            boolean headCRC = ByteArrayUtils
                    .isSameArray(Arrays.copyOfRange(res, Constants.HEAD_CRC_START_INDEX,
                            Constants.HEAD_LENGTH), headCrc);
            return headCRC;
        } else
            return false;
    }
}
