/**
 * 
 */
package com.cmcu.mcc.controller;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author Raunak
 *
 */
public class UtilityClass {
	
	public static HSSFCellStyle getHeaderStyle(HSSFWorkbook hssfWorkbook){
		
		/*******Style Code******/
		HSSFFont hssfFont = hssfWorkbook.createFont();
		hssfFont.setFontHeightInPoints((short)10);
		hssfFont.setFontName("Arial");
		//hssfFont.setColor(IndexedColors.B.getIndex());
		hssfFont.setBold(true);
		
		HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
		hssfCellStyle.setWrapText(true);
		hssfCellStyle.setFont(hssfFont);
		/*******End Style Code******/
		
		return hssfCellStyle;
	}
	
public static HSSFCellStyle getStyle(HSSFWorkbook hssfWorkbook){
		
		/*******Style Code******/
		HSSFFont hssfFont = hssfWorkbook.createFont();
		hssfFont.setFontHeightInPoints((short)10);
		hssfFont.setFontName("Arial");
		//hssfFont.setColor(IndexedColors.B.getIndex());
		
		HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();

		hssfCellStyle.setWrapText(true);
		hssfCellStyle.setFont(hssfFont);
		/*******End Style Code******/
		
		return hssfCellStyle;
	}

}
