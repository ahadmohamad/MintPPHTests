package com.paypal.test.riskmint.pph.ukbond;

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
public class RiskUKBondIsImageTest {
    static final String FILE_PATH = "src/test/resources/testdata/test_ukbond_vo.xml";
    static final String SERV_CHECK_KEY = "is_mpa_image_transaction";
    private static String hostname = BluefinConfig.getConfigProperty(BluefinConfigProperty.HOSTNAME);

    @Test(groups = "pph")
    @WebTest
    public void riskUKBondTest() throws Exception {
        boolean testResult = false;
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

        testResult = PPHShellBed.checkServLog(SERV_CHECK_KEY);
        assertTrue(testResult);

    }
}
