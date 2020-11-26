package com.example.mytestapp.nfc.utils;

import android.nfc.FormatException;
import android.nfc.tech.NfcA;

import com.example.mytestapp.nfc.exceptions.OutTimeException;
import com.example.mytestapp.nfc.exceptions.WriteErrorException;
import com.example.mytestapp.nfc.modbus.Constants;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Read session register tool
 * Created by 30035027 on 8/7/2017.
 */

public class NtagSessionUtils {


    /**
     * Byte of the session register
     */
    public enum SR_Offset {
        NC_REG((byte) 0x00), LAST_NDEF_PAGE((byte) 0x01), SM_REG((byte) 0x02), WDT_LS(
                (byte) 0x03), WDT_MS((byte) 0x04), I2C_CLOCK_STR((byte) 0x05), NS_REG(
                (byte) 0x06), FIXED((byte) 0x07);

        private byte value;

        private SR_Offset(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    /**
     * Bits of the NC_REG Register.
     */
    public enum NC_Reg_Func {
        PTHRU_DIR((byte) (0x01 << 0)), SRAM_MIRROR_ON_OFF((byte) (0x01 << 1)), FD_ON(
                (byte) (0x03 << 2)), FD_OFF((byte) (0x03 << 4)), PTHRU_ON_OFF(
                (byte) (0x01 << 6)), I2C_RST_ON_OFF((byte) (0x01 << 7)),;
        private byte value;

        private NC_Reg_Func(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    /**
     * Bits of the NS_REG Register.
     */
    public enum NS_Reg_Func {
        RF_FIELD_PRESENT((byte) (0x01 << 0)), EEPROM_WR_BUSY((byte) (0x01 << 1)), EEPROM_WR_ERR(
                (byte) (0x01 << 2)), SRAM_RF_READY((byte) (0x01 << 3)), SRAM_I2C_READY(
                (byte) (0x01 << 4)), RF_LOCKED((byte) (0x01 << 5)), I2C_LOCKED(
                (byte) (0x01 << 6)), NDEF_DATA_READ((byte) (0x01 << 7)),;
        private byte value;

        private NS_Reg_Func(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }


    /**
     * Read session register of index
     *
     * @param nfca
     * @param off  SessionRegister Address the subscript
     * @return The data on the address subscript
     * @throws IOException
     * @throws FormatException
     */
    public static byte getSessionRegister(NfcA nfca, SR_Offset off) throws IOException, FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        byte[] register = NtagI2CCommandUtils.read(nfca, Constants.SESSION_PLUS);
        if (register.length == 16) {
            return register[off.getValue()];
        } else {
            throw new IOException("Read session register failed");
        }
    }

    /**
     * @param nfca
     * @return read pvreadflg values
     * @throws IOException
     * @throws FormatException
     */
    public static int getPvReadFlg(NfcA nfca) throws IOException, FormatException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        byte flag[] = NtagI2CCommandUtils.read(nfca, (byte) Constants.PV_READ_FLG_ADDRESS);
        return ByteArrayUtils.bytesToInt(flag, 0, 2);
    }


    /**
     * Check path through mode possible
     *
     * @param nfca
     * @return
     * @throws IOException
     * @throws FormatException
     */
    public static Boolean checkPathThroughModePossible(NfcA nfca) throws IOException, FormatException, WriteErrorException, OutTimeException, InterruptedException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        long startTime = System.currentTimeMillis();
        for (int j = 0; j < Constants.LOOP_COUNT_1000; j++) {
            LogUtil.outLog("Get session register NC_REG");
            byte nc_reg = getSessionRegister(nfca, SR_Offset.NC_REG);
            LogUtil.outLog("Get session register NS_REG");
            byte ns_reg = getSessionRegister(nfca, SR_Offset.NS_REG);
            LogUtil.outLog("ns_reg=" + ns_reg + "        nc_reg=" + nc_reg);
            boolean pthru = (nc_reg & NC_Reg_Func.PTHRU_ON_OFF.getValue()) == NC_Reg_Func.PTHRU_ON_OFF.getValue();
            LogUtil.outLog("pthru=" + pthru);
            boolean pthruDir = (nc_reg & NC_Reg_Func.PTHRU_DIR.getValue()) == NC_Reg_Func.PTHRU_DIR.getValue();
            LogUtil.outLog("pthruDir=" + pthruDir);
            boolean rfLocked = (ns_reg & NS_Reg_Func.RF_LOCKED.getValue()) == NS_Reg_Func.RF_LOCKED.getValue();
            LogUtil.outLog("rfLocked=" + rfLocked);
            if (pthru && pthruDir && rfLocked) {
                return true;
            }
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) >= Constants.WAIT_TOTAL_TIME) {
                LogUtil.outLog("checkPathThroughModePossible time out");
                return false;
            }
            Thread.sleep(Constants.WAIT_TIME);
        }
        return false;
    }


    /**
     * Wait for the CPU to write the data, and OutTimeException if the time exceeds the timeoutMS
     *
     * @param nfca
     * @throws IOException
     * @throws FormatException
     * @throws OutTimeException
     */
    public static void waitCpuGetSRAMData(NfcA nfca) throws IOException, FormatException, OutTimeException, WriteErrorException, InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int j = 0; j < Constants.LOOP_COUNT_1000; j++) {
            byte ns_reg = NtagSessionUtils.getSessionRegister(nfca, SR_Offset.NS_REG);
            int RF_LOCKED = (ns_reg & NS_Reg_Func.RF_LOCKED.getValue()) >> 5;
            LogUtil.outLog("RF_LOCKED=" + RF_LOCKED);
            if (RF_LOCKED == 1) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) >= Constants.WAIT_TOTAL_TIME) {
                throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
            }
            Thread.sleep(Constants.WAIT_TIME);
        }
        throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
    }

    /**
     * Wait for the CPU to write the data, and OutTimeException if the time exceeds the timeoutMS
     *
     * @param nfca
     * @throws IOException
     * @throws FormatException
     * @throws OutTimeException
     */

    public static void waitCpuPrepareSRAMData(NfcA nfca) throws IOException,
            FormatException, OutTimeException, WriteErrorException, InterruptedException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < Constants.LOOP_COUNT_1000; i++) {
            Thread.sleep(Constants.WAIT_TIME);
            byte ns_reg = NtagSessionUtils.getSessionRegister(nfca, SR_Offset.NS_REG);
            int SRAM_RF_READY = (ns_reg & NS_Reg_Func.SRAM_RF_READY.getValue()) >> 3;
            LogUtil.outLog("SRAM_RF_READY=" + SRAM_RF_READY);
            if (SRAM_RF_READY == 1) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) >= Constants.WAIT_TOTAL_TIME) {
                throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
            }
        }
        throw new OutTimeException("waitCpuPrepareSRAMData OutTimeException");
    }

    public static int waitCpuPrepareSRAMDataWithRetry(NfcA nfca) throws IOException,
            FormatException, WriteErrorException, InterruptedException {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < Constants.LOOP_COUNT_500; i++) {
            Thread.sleep(Constants.WAIT_TIME);
            byte ns_reg = NtagSessionUtils.getSessionRegister(nfca, SR_Offset.NS_REG);
            int SRAM_RF_READY = (ns_reg & NS_Reg_Func.SRAM_RF_READY.getValue()) >> 3;
            LogUtil.outLog("SRAM_RF_READY=" + SRAM_RF_READY);
            if (SRAM_RF_READY == 1) {
                return SRAM_RF_READY;
            }
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) >= Constants.WAIT_TOTAL_TIME) {
                return Constants.NFC_ERROR_CODE;
            }
        }
        return Constants.NFC_ERROR_CODE;
    }

    /**
     * Waiting for the EEPROM to prepare the data, the poll reads the
     * PvReadFlg value, and if it is not 0, it is ready to end the loop
     *
     * @param nfca
     * @param timeoutMS
     * @throws IOException
     * @throws FormatException
     * @throws OutTimeException
     */
    private static boolean isCpuPrepareEEPROMData = false;

    public static void waitCpuPrepareEEPROMData(final NfcA nfca, int timeoutMS) throws IOException,
            FormatException, OutTimeException, WriteErrorException {
        if (nfca == null)
            throw new IllegalArgumentException("Nfca is not null");
        if (timeoutMS <= 0)
            throw new IllegalArgumentException("TimeoutMS must be greater than zreo");
        final Object lock = new Object();
        synchronized (lock) {
            isCpuPrepareEEPROMData = false;
        }
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    isCpuPrepareEEPROMData = true;
                }
            }
        }, timeoutMS);
        while (!(getPvReadFlg(nfca) == 0)) {
            LogUtil.outLog(getPvReadFlg(nfca) + "---");
            synchronized (lock) {
                if (isCpuPrepareEEPROMData) {
                    throw new OutTimeException("waitforI2Cwrite had a Timout");
                }
            }
        }
        mTimer.cancel();

    }

}
