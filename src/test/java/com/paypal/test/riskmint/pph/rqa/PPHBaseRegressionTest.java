package com.paypal.test.riskmint.pph.rqa;

import static org.testng.AssertJUnit.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.paypal.test.bluefin.platform.config.BluefinConfig;
import com.paypal.test.bluefin.platform.config.BluefinConfig.BluefinConfigProperty;
import com.paypal.test.bluefin.platform.grid.WebTest;
import com.paypal.test.jaws.utilities.ServiceHelper;
import com.paypal.test.riskmint.pph.util.AsfCall;
import com.paypal.test.riskmint.pph.util.DBOperation;
import com.paypal.test.riskmint.pph.util.PPHShellBed;

/**
 * @author yingdin
 * 
 */
public class PPHBaseRegressionTest {
    static final String CHECK_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Checked.xml";
    static final String CHIP_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Chip.xml";
    static final String KEY_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Key.xml";
    static final String TRACK_1_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Track_1.xml";
    static final String TRACK_2_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Track_2.xml";
    static final String TRACK_1_2_TYPE_FILE_PATH = "src/test/resources/testdata/regression/pph_regression_Track_1_2.xml";
    static final String ACCOUNT_NUMBER = "2195678538064441198";

    boolean testResult = false;

    @BeforeClass(groups = "regression")
    public void setTestEnv() throws Exception {

        // clean db
        System.out.println("Cleaning table wrisk_stats!");
        DBOperation.cleanWriskStats(ACCOUNT_NUMBER);

        // restart gmadpterserv
//        PPHShellBed.restartService("gmadapterserv", "qserv");
        ServiceHelper.restartService("gmadapterserv", "qserv");
//        Thread.sleep(30000);

        // run invoke script for gmadapterserv
        PPHShellBed.executeInvokeScriptForGmadapterserv();

        // add certificate in acquiring serv trust clients pem file and restart the serv
        PPHShellBed.addAcquiringServTrustCert();
        ServiceHelper.restartService("amqriskpostprocessd", "qserv");
        ServiceHelper.restartService("acquiringserv", "qserv");

    }

    @Test(groups = "regression")
    @WebTest
    public void chipTxnTest() throws Exception {

        asfCall(CHIP_TYPE_FILE_PATH);
        Thread.sleep(60000);
        testResult = DBOperation.getTxnDataFromWriskStats(ACCOUNT_NUMBER, true, 1500);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void keyTxnTest() throws Exception {

        asfCall(KEY_TYPE_FILE_PATH);
        testResult = DBOperation.getTxnDataFromWriskStats(ACCOUNT_NUMBER, false, 1500);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void track1TxnTest() throws Exception {

        asfCall(TRACK_1_TYPE_FILE_PATH);
        testResult = DBOperation.getTxnDataFromWriskStats(ACCOUNT_NUMBER, true, 1500);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void track2TxnTest() throws Exception {

        asfCall(TRACK_2_TYPE_FILE_PATH);
        testResult = DBOperation.getTxnDataFromWriskStats(ACCOUNT_NUMBER, true, 1500);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void track12TxnTest() throws Exception {

        asfCall(TRACK_1_2_TYPE_FILE_PATH);
        testResult = DBOperation.getTxnDataFromWriskStats(ACCOUNT_NUMBER, true, 1500);
        assertTrue(testResult);

    }

    private void asfCall(String vo_file) throws Exception {

        String stageName = BluefinConfig.getConfigProperty(BluefinConfigProperty.HOSTNAME);
        String ASFURL = stageName + ":10295/AcquiringServ/1.0/";
        System.out.println("Making asf call to : " + ASFURL);

        AsfCall asfTb = new AsfCall();

        File file = new File(vo_file);
        InputStream in = null;
        in = new FileInputStream(file);
        String reqMsgContentLine = "";
        String reqMsg = "";
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(in));

        while((reqMsgContentLine = bufferreader.readLine()) != null) {
            reqMsg += reqMsgContentLine;
        }

        reqMsg = reqMsg.replaceAll("\t", " ");
        System.out.println(reqMsg);
        asfTb.invokeAsfService(ASFURL, reqMsg);
    }

}
