package com.example.mytestapp.nfc.exceptions;

/**
 * Created by 30035027 on 8/23/2017.
 */

public class FunctionCodeErrorException extends Exception {
    private int resultCode;

    public FunctionCodeErrorException(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
