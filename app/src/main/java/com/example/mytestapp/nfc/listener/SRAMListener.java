package com.example.mytestapp.nfc.listener;

public interface SRAMListener {
    /**
     * It informs the listener about new data written in the SRAM
     * Used to inform about the progress during the SpeedTest
     */
    public abstract void onWriteSRAM(int i);
}
