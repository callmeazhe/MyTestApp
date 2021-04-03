package com.example.mytestapp.nfc.processor;


import com.example.mytestapp.nfc.listener.NfcCommunicationResultListener;

public interface INfcProcessor {
    void readInfo(NfcCommunicationResultListener listener);
    boolean isValidKeyCard();
}
