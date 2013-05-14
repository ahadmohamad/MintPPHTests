package com.paypal.test.simpletools;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.paypal.test.jaws.utilities.ExecuteScript;




public class ServLogTester {

    static final String RULE_ID="1069";
    static final String STAGE = "STAGE2P2400";
    static final String COMPONENT = "riskpaymentserv";
    
    @Test(groups = "pph")
    public void getServLogTest(){
        String script = "grep -E '" + RULE_ID + "' /x/web/" + STAGE + "/"
                + COMPONENT + "/logs/current";
        HashMap<String, String> r = null;
        try {

            ExecuteScript answer = new ExecuteScript();
            if (answer.runScript(script)) {
                r = answer.getResponseFromScript();
                String out = r.get("output_log");
                System.out.println("Following is the grep result:");
                System.out.println(out);
            }

        } catch (Exception e) {
            String errorMsg = "An error occured while executing checkXomLog shell script.";
            System.out.println(errorMsg);
            throw new RuntimeException(errorMsg + " Root cause: ", e);
        }
    }
}
