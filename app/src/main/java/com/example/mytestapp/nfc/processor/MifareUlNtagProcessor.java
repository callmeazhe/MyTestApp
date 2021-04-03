package com.example.mytestapp.nfc.processor;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;


import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.listener.NfcCommunicationResultListener;
import com.example.mytestapp.nfc.modbus.Constants;
import com.example.mytestapp.nfc.utils.ByteArrayUtils;
import com.example.mytestapp.nfc.utils.NtagI2CCommandUtils;
import com.example.mytestapp.utils.AESUtils;
import com.example.mytestapp.utils.LogUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MifareUlNtagProcessor implements INfcProcessor{
    private NfcA mNfcA = null;
    private final String CHARSET = "UTF-8";

    public MifareUlNtagProcessor(Tag tag) {
        mNfcA = NfcA.get(tag);
    }

    @Override
    public void readInfo(NfcCommunicationResultListener listener) {
        if(mNfcA == null) {
            listener.error(-1);
            return;
        }

        boolean readResult = false;
        for(int i = 0; i < 4; i++) {
            Log.i("TAG", "write:retryCount=" + i);
            readResult = readParameters(listener);
            if (readResult) {
                break;
            }
        }

        if (!readResult) {
            listener.error(-1);
        }
    }

    @Override
    public boolean isValidKeyCard() {
        return mNfcA != null;
    }

    private boolean readParameters(NfcCommunicationResultListener listener) {
        try {
            if (!mNfcA.isConnected())
                mNfcA.connect();

            byte magic[] = Arrays.copyOfRange(NtagI2CCommandUtils.read(mNfcA, (byte)4), 0, 4);
            if (ByteArrayUtils.isSameArray(magic, MifareClassicConstant.MAGIC)) {

                byte[] appKey = NtagI2CCommandUtils.read(mNfcA, (byte)5);
                byte[] euiData = NtagI2CCommandUtils.read(mNfcA, (byte)9);
                byte[] appEui = Arrays.copyOfRange(euiData, 0, 8);
                byte[] appName = NtagI2CCommandUtils.read(mNfcA, (byte)11);
                byte[] crc = Arrays.copyOfRange(NtagI2CCommandUtils.read(mNfcA, (byte)15), 0, 2);

                byte[] crcData = Arrays.copyOfRange(appKey, 0, 16);
                crcData = ByteArrayUtils.concat(crcData, appEui);
                crcData = ByteArrayUtils.concat(crcData, appName);
                byte[] calculatedCrc = CRC16.crc16(crcData);
                if (ByteArrayUtils.isSameArray(crc, calculatedCrc)) {

                    byte[] key = AESUtils.getUlNtagKeyCardSecretKey();
                    Map<String, Object> map = new HashMap<>();
                    listener.success(map);
                } else {
                    listener.error(0);
                }
            } else {
                listener.error(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.outLog("Exception" + e.toString());
            return false;
        } finally {
            try {
                if (mNfcA.isConnected())
                    mNfcA.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.outLog("Exception" + e.toString());
            }
        }
        return true;
    }
}
