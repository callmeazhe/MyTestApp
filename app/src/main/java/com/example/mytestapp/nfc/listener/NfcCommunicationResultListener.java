package com.example.mytestapp.nfc.listener;

import java.util.HashMap;
import java.util.Map;

public interface NfcCommunicationResultListener {

    void error(int errorCode);

    void success(Map map);

}
