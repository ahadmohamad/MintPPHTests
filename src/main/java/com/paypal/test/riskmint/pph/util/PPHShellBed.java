package com.paypal.test.riskmint.pph.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.paypal.test.bluefin.platform.config.BluefinConfig;
import com.paypal.test.bluefin.platform.config.BluefinConfig.BluefinConfigProperty;
import com.paypal.test.jaws.utilities.ExecuteScript;

public class PPHShellBed {

    private static final String ACQUIRINGSERV_CERT = "src/test/resources/certificates/acquiringservCert.txt";
    private static String stageName = BluefinConfig.getConfigProperty(BluefinConfigProperty.STAGE_NAME);

    public static void enableLogOutput() {
        String cdCmd = "cd /x/web/" + stageName + "/amqriskpaymentd;";
        String addXomOutputCmd = cdCmd
                + "echo test_qa_rules = true>>amqriskpaymentd.txt;echo test_xom_output = xom.txt>>amqriskpaymentd.txt;";
        String cdbmakeCmd = cdCmd + "cdbmake3 amqriskpaymentd.cdb amqriskpaymentd.txt;";

        try {

            ExecuteScript exeSh = new ExecuteScript();
            exeSh.runScript(addXomOutputCmd);
            exeSh.runScript(cdbmakeCmd);
        } catch (Exception e) {
            String errorMsg = "An error occured while executing enqueue request shell script.";
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
    }

    public static void executeEnqueueRequest(String account_num, String txn_id) {

        System.out.println("Stage name is: " + stageName);

        String cdCmd = "cd /x/web/" + stageName + "/component_test/tools/HadesFixture;";
        String sedAccNumCmd = cdCmd + "sed -i 's/>[0-9]" + "\\" + "{19" + "\\" + "}</>" + account_num
                + "</g' RPD-GT-enq.xml;";
        String sedTxnIdCmd = cdCmd + "sed -i 's/>[0-9]" + "\\" + "{7,8" + "\\" + "}</>" + txn_id
                + "</g' RPD-GT-enq.xml";

        String enqueueCmd = "csh -c 'source /x/web/" + stageName + "/qatools/qa.cshrc;cd /x/web/" + stageName
                + "/component_test/tools/AMQFixture;pp_amqops -i ../HadesFixture/RPD-GT-enq.xml -o out.txt;'";

        try {

            ExecuteScript exeSh = new ExecuteScript();
            exeSh.runScript(sedAccNumCmd);
            exeSh.runScript(sedTxnIdCmd);
            exeSh.runScript(enqueueCmd);
        } catch (Exception e) {
            String errorMsg = "An error occured while executing enqueue request shell script.";
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }

    }

    // check xom log file for checking invoice and product bom data
    public static Boolean checkServLog(String comp_name, String rule_id) {

        String script = "grep -E '" + rule_id + "' /x/web/" + stageName + "/" + comp_name + "/logs/*";
        HashMap<String, String> r = null;
        Boolean tmp = false;
        try {

            ExecuteScript answer = new ExecuteScript();
            if(answer.runScript(script)) {
                r = answer.getResponseFromScript();
                String out = r.get("output_log");
                tmp = out.equals("") ? false : true;

            }

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
        return tmp;
    }

    public static Boolean checkXomLog(String comp_name, String search_key) {

        String logPath = "/x/web/" + stageName + "/" + comp_name + "/xom.txt";
        String script = "grep -E '" + search_key + "' " + logPath;
        HashMap<String, String> r = null;
        Boolean tmp = false;
        try {

            ExecuteScript answer = new ExecuteScript();
            if(answer.runScript(script)) {
                r = answer.getResponseFromScript();
                String out = r.get("output_log");
                tmp = out.equals("") ? false : true;

            }

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
        return tmp;
    }

    // check log file for checking invoice and product bom data
    public static Boolean checkLogConfig() {

        String logPath = "/x/web/" + stageName + "/amqriskpaymentd/amqriskpaymentd.txt";
        String script = "grep -E 'test_qa_rules' " + logPath;
        HashMap<String, String> r = null;
        Boolean tmp = false;
        try {

            ExecuteScript answer = new ExecuteScript();
            if(answer.runScript(script)) {
                r = answer.getResponseFromScript();
                String out = r.get("output_log");
                tmp = out.equals("") ? false : true;

            }

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
        return tmp;
    }

    public static Boolean checkServLog(String checkLogKey) {
        String script = "grep -E '" + checkLogKey + "' /x/web/" + stageName + "/riskpaymentserv/xom.txt";
        HashMap<String, String> r = null;
        Boolean tmp = false;
        try {

            ExecuteScript answer = new ExecuteScript();
            if(answer.runScript(script)) {
                r = answer.getResponseFromScript();
                String out = r.get("output_log");
                tmp = out.equals("") ? false : true;

            }

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
        return tmp;
    }

    // check log file for checking invoice and product bom data
    public static Boolean addAcquiringServTrustCert() throws Exception {

        String logPath = "/x/web/" + stageName + "/acquiringserv/protected/acquiringserv_trusted_clients.pem";

        File certTextFile = new File(ACQUIRINGSERV_CERT);
        InputStream in = null;
        in = new FileInputStream(certTextFile);
        String certTextContentLine = "";
        String certText = "\r";
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(in));

        while((certTextContentLine = bufferreader.readLine()) != null) {
            certText += certTextContentLine + "\r";
        }

        // certText = certText.replaceAll("\t", " ");
        System.out.println(certText);

        String script = "echo '" + certText + "' >> " + logPath;
        Boolean tmp = false;
        try {

            ExecuteScript answer = new ExecuteScript();
            answer.runScript(script);

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
        return tmp;
    }

    // Restart specified component and clean its logs
    public static void restartService(String comp_name, String user_privelidge) {
        String stageName = BluefinConfig.getConfigProperty(BluefinConfigProperty.STAGE_NAME);
        String cmdPath = "cd /x/web/" + stageName + "/" + comp_name + "/;";
        String cmdclean = "sudo -u " + user_privelidge + " rm -rf logs/*;";
        String cmdstop = "./shutdown.sh;";
        String cmdstart = "/x/home/coonradt/tools/bin/Linux/mystart ./start.sh;";
        String script = cmdPath + cmdstop + cmdclean + cmdstart;
        try {

            ExecuteScript answer = new ExecuteScript();
            answer.runScript(script);
        } catch (Exception e) {
            String errorMsg = "An error occured while executing restartService shell script.";
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }

    }

    public static void executeInvokeScriptForGmadapterserv() {

        String cdCmd = "cd /x/web/" + stageName + "/gmadapterserv;";

        String invokeCmd = cdCmd
                + "./invokeconfigcommand.sh \"id=com.paypal.mobile.services.gmadapter.impl.GMAdapterConfigBean&API_URL=https://api."
                + stageName + "/2.0/\"";

        try {

            ExecuteScript exeSh = new ExecuteScript();
            exeSh.runScript(invokeCmd);
        } catch (Exception e) {
            String errorMsg = "An error occured while executing invoke script in gmadapterserv of stage: " + stageName
                    + ".";
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }

    }
}
