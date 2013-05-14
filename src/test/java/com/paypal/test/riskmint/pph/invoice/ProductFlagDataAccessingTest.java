package com.paypal.test.riskmint.pph.invoice;

import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

import com.paypal.test.riskmint.pph.data.EnqueueDataSheet;
import com.paypal.test.riskmint.pph.util.ExcelUtil;
import com.paypal.test.riskmint.pph.util.PPHShellBed;

/**
 * @author yingdin
 * 
 */
public class ProductFlagDataAccessingTest {

    // please set your test data in this excel file first before run this test
    final String PATH = "src/test/resources/testdata/";
    final String FILE_NAME = "EnqueueProductFlagDataSheet.xls";
    final String COMPONENT_NAME = "amqriskpaymentd";
    final String XOMSEARCHKEY = "account has pph product";
    EnqueueDataSheet eds = new EnqueueDataSheet();

    @Test(groups = { "pph" })
    public void invoiceDataAccessTest() throws Exception{
        

            // check whether the qa log output is enabled
            if(!PPHShellBed.checkLogConfig()) {
                PPHShellBed.enableLogOutput();
            }

            Object[][] data = ExcelUtil.getExcelData(PATH, FILE_NAME);

            for(int i = 0; i < data.length; i++) {
                EnqueueDataSheet sheet = (EnqueueDataSheet) data[i][0];
                String account_num = sheet.getAccount_num();
                String txn_id = sheet.getTxn_id();
                System.out.println("account number is : " + account_num);
                System.out.println("transaction id is : " + txn_id);

                PPHShellBed.executeEnqueueRequest(account_num, txn_id);

                Thread.sleep(300000);
                boolean testResult = PPHShellBed.checkXomLog("amqriskpaymentd", XOMSEARCHKEY);
                assertTrue(testResult);
            }
       

    }

}
