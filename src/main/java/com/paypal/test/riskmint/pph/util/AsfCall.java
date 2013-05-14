package com.paypal.test.riskmint.pph.util;


import org.openqa.selenium.By;

import com.paypal.test.bluefin.platform.grid.Grid;



public class AsfCall {

	public String requestType = "SWIPE";
	public String payerID;
	public long txnAmount;


	
	public void invokeAsfService(String asfURL,String asfRequest)
	{
		String asfTestBedURL = "http://devtoolslvs01.qa.paypal.com/~jwan/AcquiringServ_parse.cgi";
		Grid.driver().get(asfTestBedURL);
		String serviceURI = asfURL;
		Grid.driver().findElement(By.name("host")).sendKeys(serviceURI);
		Grid.driver().findElement(By.name("req")).sendKeys(asfRequest);
		Grid.driver().findElement(By.name("submit")).click();
		Grid.driver().waitForPageToLoad("40000");
		
	}
	



}
