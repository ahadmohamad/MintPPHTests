package com.paypal.test.riskmint.pph.tpv;

import static org.testng.AssertJUnit.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.testng.annotations.Test;

import com.paypal.test.bluefin.platform.config.BluefinConfig;
import com.paypal.test.bluefin.platform.config.BluefinConfig.BluefinConfigProperty;
import com.paypal.test.bluefin.platform.grid.WebTest;
import com.paypal.test.riskmint.pph.util.AsfCall;
import com.paypal.test.riskmint.pph.util.PPHShellBed;

/**
 * @author yingdin
 * 
 */
public class TPVDataTest {
    static final String FILE_PATH = "src/test/resources/testdata/test_ukbond_vo.xml";
    static final String TPV_SWIPE_LAST_DAYS_KEY = "KB_SWIPE_AMT_LATEST_DAY";
    static final String TPV_NON_SWIPE_LAST_DAYS_KEY = "KB_NON_SWIPE_AMT_LATEST_DAY";
    static final String TPV_SWIPE_AMT_KEY = "KB_SWIPE_AMT_LAST_30_DAYS";
    static final String TPV_NON_SWIPE_AMT_KEY = "KB_NON_SWIPE_AMT_LAST_30_DAYS";
    private static String hostname = BluefinConfig.getConfigProperty(BluefinConfigProperty.HOSTNAME);

    @Test(groups = "regression")
    @WebTest
    public void getTPVNonSwipeLastDaysTest() throws Exception {
        boolean testResult = false;
        makeASFCall();
        testResult = PPHShellBed.checkServLog(TPV_NON_SWIPE_LAST_DAYS_KEY);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void getTPVSwipeLastDaysTest() throws Exception {
        boolean testResult = false;
        makeASFCall();
        testResult = PPHShellBed.checkServLog(TPV_SWIPE_LAST_DAYS_KEY);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void getTPVSwipeAmtTest() throws Exception {
        boolean testResult = false;
        makeASFCall();
        testResult = PPHShellBed.checkServLog(TPV_SWIPE_AMT_KEY);
        assertTrue(testResult);

    }

    @Test(groups = "regression")
    @WebTest
    public void getTPVNonSwipeAmtTest() throws Exception {
        boolean testResult = false;
        makeASFCall();
        testResult = PPHShellBed.checkServLog(TPV_NON_SWIPE_AMT_KEY);
        assertTrue(testResult);

    }

    private void makeASFCall() throws Exception {
        String asfURL = hostname + ":10295/AcquiringServ/1.0/";
        AsfCall asfTb = new AsfCall();
        File file = new File(FILE_PATH);
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
        asfTb.invokeAsfService(asfURL, reqMsg);
    }
}
