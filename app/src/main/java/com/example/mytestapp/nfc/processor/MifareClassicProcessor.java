package com.example.mytestapp.nfc.processor;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;


import com.example.mytestapp.nfc.crc16.CRC16;
import com.example.mytestapp.nfc.listener.NfcCommunicationResultListener;
import com.example.mytestapp.nfc.modbus.Constants;
import com.example.mytestapp.nfc.utils.ByteArrayUtils;
import com.example.mytestapp.utils.LogUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MifareClassicProcessor implements INfcProcessor{
    private MifareClassic mMifareClassic;
    public MifareClassicProcessor(Tag tag) {
        mMifareClassic = MifareClassic.get(tag);;
    }

    @Override
    public boolean isValidKeyCard() {
        return (mMifareClassic.getSize() == 1024);
    }

    @Override
    public void readInfo(NfcCommunicationResultListener listener) {
        boolean readResult = false;
        for (int i = 0; i < 4; i++) {
            Log.i("TAG", "retryCount=" + i);
            readResult = readParameters(listener);
            if (readResult) {
                break;
            }
        }

        if (!readResult) {
            listener.error(-1);
        }
    }

    private boolean readParameters(NfcCommunicationResultListener listener) {
        if(mMifareClassic == null) {
            return false;
        }

        try {
            if (!mMifareClassic.isConnected())
                mMifareClassic.connect();
            if (mMifareClassic.authenticateSectorWithKeyA(0, MifareClassic.KEY_DEFAULT)) {
                //read data (4*sector+block)
                byte[] data = mMifareClassic.readBlock(1);
                byte[] sectorSettingData = mMifareClassic.readBlock(2);

                byte magic[] = Arrays.copyOfRange(data, 0, 4);
                byte crc[] = Arrays.copyOfRange(data, 14, 16);
                byte crcData[] = ByteArrayUtils.concat(Arrays.copyOfRange(data, 0, 14), sectorSettingData);

                if (ByteArrayUtils.isSameArray(magic, MifareClassicConstant.MAGIC) && ByteArrayUtils.isSameArray(crc, CRC16.crc16(crcData)) && ByteArrayUtils.isSameArray(sectorSettingData, MifareClassicConstant.SECTOR_0_BLOCK_2)) {
                    if (mMifareClassic.authenticateSectorWithKeyB(1, MifareClassicConstant.KEY_B_1)) {
                        //read data (4*sector+block)
                        byte[] appKey = mMifareClassic.readBlock(4 * 1 + 2);
                        byte[] appName = Arrays.copyOfRange(mMifareClassic.readBlock(4 * 1 + 1), 0, 10);
                        byte[] appEui = Arrays.copyOfRange(mMifareClassic.readBlock(4 * 1 + 0), 8, 16);

                        Map<String, Object> map = new HashMap<>();
                        listener.success(map);
                    } else {
                        listener.error(0);
                    }
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
                if (mMifareClassic.isConnected())
                    mMifareClassic.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.outLog("Exception" + e.toString());
            }
        }
        return true;
    }

}
