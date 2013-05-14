package com.paypal.test.riskmint.pph.util;

import com.paypal.test.bluefin.platform.utilities.ExcelDataProvider;
import com.paypal.test.jaws.logging.JawsLogger;
import com.paypal.test.riskmint.pph.data.EnqueueDataSheet;

public class ExcelUtil {

	public static Object[][] getExcelData(String path, String filename) {
		ExcelDataProvider excel = null;
		EnqueueDataSheet eds = new EnqueueDataSheet();
		try {
			excel = new ExcelDataProvider(path, filename);

			Object[][] data;

			data = excel.getAllExcelRows(eds);
			return data;
		} catch (Exception e) {
			JawsLogger.getLogger().severe(
					"Error reading Excel rows: " + e.toString());
			return null;
		}

	}
}
