package com.example.mytestapp.nfc.listener;

public interface EEPROMListener {
    /**
     * It informs the listener about the number of bytes written in the EEPROM
     * Used to inform about the progress during the SpeedTest
     *
     * @param bytes
     */
    public abstract void onWriteEEPROMProgressLisenter(float bytes);
}
