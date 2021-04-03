package com.example.mytestapp.nfc.processor;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;

public class NfcProcessorFactory {
    public static INfcProcessor getNfcProcessor(Tag tag) {
        if (tag == null) {
            return null;
        }

        INfcProcessor nfcProcessor = null;

        // MifareClassic: techList = {NfcA, MifareClassic, NdefFormatable}
        // MifareUltralight: techList =  {NfcA, MifareUltralight, NdefFormatable}
        // NTag215: techList = {NfcA, MifareUltralight, Ndef}
        String[] techList = tag.getTechList();
        if (isSpecifiedTech(techList, new String[]{MifareClassic.class.getName()})) {
            nfcProcessor = new MifareClassicProcessor(tag);
        } else if(isSpecifiedTech(techList, new String[] {NfcA.class.getName()})) {
            nfcProcessor = new MifareUlNtagProcessor(tag);
        }

        return nfcProcessor;
    }

    private static boolean isSpecifiedTech(String[] techList, String[] specifiedTechList) {
        int count = 0;
        for(String tech : techList) {
            for(String specifiedTech : specifiedTechList) {
                if(tech.equals(specifiedTech)) {
                    count++;
                }
            }
        }

        return count == specifiedTechList.length;
    }
}
