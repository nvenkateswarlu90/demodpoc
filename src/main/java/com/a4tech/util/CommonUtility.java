package com.a4tech.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.util.StringUtils;

public class CommonUtility {
	
   private static Logger _LOGGER = Logger.getLogger(CommonUtility.class);
	public static boolean isEmptyOrNull(String str) {
		return (str != null && !" ".equals(str));
	}
	
	public static String getFileExtension(String fileName){
		
		return  fileName.substring(fileName.lastIndexOf('.')+1);
	}
	
	public static List<String> getStringAsList(String value,String splitter){
		List<String> data = null;
		if(!StringUtils.isEmpty(value)){
			data = new ArrayList<String>();
			String[] values = value.split(splitter);
			for (String attribute : values) {
				if(!StringUtils.isEmpty(attribute)){
					data.add(attribute);
				}
			}
			return data;
		}
		return new ArrayList<String>();
	}
	public static List<String> getStringAsList(String value){
		List<String> listOfValues = Arrays.asList(value
				.split(ApplicationConstants.CONST_STRING_COMMA_SEP));
		return listOfValues;
	}
	public static String getCellValueDouble(Cell cell) {
		String value = ApplicationConstants.CONST_STRING_EMPTY;
		try {
			CellType cellType = cell.getCellTypeEnum();
			if(cellType == CellType.STRING){
				value = cell.getStringCellValue().trim();
			} else if(cellType == CellType.NUMERIC){
				double doubleValue = cell.getNumericCellValue();
				value = String.valueOf(doubleValue).trim();
			} else {
				value = "";
			}
		} catch (Exception e) {
			_LOGGER.error("Cell value convert into Double: " + e.getMessage());
		}

		return value;
	}
	
		
	public static String getCellValueStrinOrInt(Cell cell) {
		String value = ApplicationConstants.CONST_STRING_EMPTY;
		try {
			CellType cellType = cell.getCellTypeEnum();
			if(cellType == CellType.STRING){
				value = cell.getStringCellValue().trim();
			} else if(cellType == CellType.NUMERIC){
				int numericValue = (int) cell.getNumericCellValue();
				value = String.valueOf(numericValue).trim();
			} else {
				value = "";
			}
		} catch (Exception e) {
			/*_LOGGER.error("Cell value convert into String/Int format: "
					+ e.getMessage());*/
		}

		return value;
	}
	
	public static String getCellValueStrinOrLong(Cell cell) {
		String value = ApplicationConstants.CONST_STRING_EMPTY;
		try {
			CellType cellType = cell.getCellTypeEnum();
			if(cellType == CellType.STRING){
				value = cell.getStringCellValue().trim();
			} else if(cellType == CellType.NUMERIC){
				long numericValue = (long) cell.getNumericCellValue();
				value = String.valueOf(numericValue).trim();
			} else {
				value = "";
			}
		} catch (Exception e) {
			/*_LOGGER.error("Cell value convert into String/Int format: "
					+ e.getMessage());*/
		}

		return value;
	}
	
	public static String getCellValueStrinOrDecimal(Cell cell){
		String value = ApplicationConstants.CONST_STRING_EMPTY;
		try{
			CellType cellType = cell.getCellTypeEnum();
			if(cellType == CellType.STRING){
				value = cell.getStringCellValue().trim();
			} else if(cellType == CellType.NUMERIC){
				value = String.valueOf(BigDecimal.valueOf(cell.getNumericCellValue()));
			} else {
				value = "";
			}
	}catch(Exception e){
		_LOGGER.error("Cell value convert into String/decimal: "+e.getMessage());
	}
		return value;
	}
	public static boolean isPriceQuantity(int indexNumber){
		if(indexNumber >=9 && indexNumber <=22){
			return true;
		}else{
			return false;
		}	
	}
   public static String[] getValuesOfArray(String data,String delimiter){
	   if(!StringUtils.isEmpty(data)){
		   return data.split(delimiter);
	   }
	   return null;
   }
   /* @Author  Venkat ,13/09/2016
    * @Param   String (Value) 
    * @Description This method is checking value is zero or blank
    * @ReturnType boolean
    */
	public static boolean checkZeroAndEmpty(String value) {

		if (ApplicationConstants.CONST_STRING_ZERO.equals(value)
				|| ApplicationConstants.CONST_STRING_EMPTY.equals(value)) {
			return true;
		}
		return false;
	}
   
   public static boolean isBlank(String value){
	   if(value.equals(" ")){
		   return true;
	   }
	   return false;
   }
   /*@author Venkat
    *@param String,it is file extension name i.e xls,csv..
    *@description This method is valid for file extension weather it is xls,xlsx ,csv format or
    *                                                                           any other format
    * @ return boolean ,if filename having xls,xlsx ,csv then return true else false
    */
	public static boolean isValidFormat(String fileName) {

		if (ApplicationConstants.CONST_STRING_XLS.equalsIgnoreCase(fileName)
				|| ApplicationConstants.CONST_STRING_XLSX.equalsIgnoreCase(fileName)
				|| ApplicationConstants.CONST_STRING_CSV.equalsIgnoreCase(fileName)) {
			return true;

		}
		return false;
	}
	
	/*
	 * author Venkat 13/10/2016
	 * @param String OriginalValue,String String SpecialSymbol
	 * @description This method is remove special symbol in given value
	 * @return String,it returns finalValue  
	 */
	public static String removeSpecialSymbols(String value,String symbol){
		String finalValue = value.replaceAll(symbol, ApplicationConstants.CONST_STRING_EMPTY);
		return finalValue;
	}
	/*@author Venkat 18/10/2016
	 *@param String,String,String 
	 *@description This method design for concatenate two string by delimiter
	 *@return String 
	 */
	public static String appendStrings(String src, String destination ,String delimiter){
		  if(!StringUtils.isEmpty(destination)){
			  src = src.concat(delimiter).concat(destination);
			  return src;
		  }else {
			  return src;
		  }
	}
	
  public static String removeCurlyBraces(String source){
	  if(source.contains(ApplicationConstants.SQUARE_BRACKET_OPEN))
	  {
		  source = source.replace(ApplicationConstants.SQUARE_BRACKET_OPEN, 
				                                        ApplicationConstants.CONST_STRING_EMPTY);
	  }
	  if(source.contains(ApplicationConstants.SQUARE_BRACKET_CLOSE)){
		  source = source.replace(ApplicationConstants.SQUARE_BRACKET_CLOSE, 
				                                  ApplicationConstants.CONST_STRING_EMPTY);
	  }
	  return source; //exponential
  }
  
  public static String convertExponentValueIntoNumber(String exponentValue){
	  try{
		  BigDecimal bigDecimal = new BigDecimal(exponentValue);
		  long number = bigDecimal.longValue();
		  String value = Long.toString(number);
		  return value;  
	  }catch(NumberFormatException nfe){
		  return ApplicationConstants.CONST_STRING_EMPTY;
	  }
	  
  }
  
  public static String getStringLimitedChars(String value, int noOfCharacters){
	  int len=value.length();
      if(len>noOfCharacters){
      String strTemp=value.substring(ApplicationConstants.CONST_NUMBER_ZERO, noOfCharacters);
      if(strTemp.contains(" ")){
    	  int lenTemp= strTemp.lastIndexOf(ApplicationConstants.CONST_VALUE_TYPE_SPACE);
          value= (String) strTemp.subSequence(ApplicationConstants.CONST_NUMBER_ZERO, lenTemp);  
      } else {
    	  value = strTemp;
      }
    }
      return value;
  }
  
   /*
    * description : this method used to check price values is descending or not,
    *                      if values are not descending order then return false
    *      e.g. : 15,12,10  -- return true
    *             15,12,15  -- return false
    */
	public static boolean isdescending(String[] prices){
		double[] doubleprices=convertStringArrintoDoubleArr(prices);
		  for (int i = 0; i < doubleprices.length-1; i++) {
		      if (doubleprices[i] < doubleprices[i+1]) {
		          return false;
		      }
		  }
		  return true; 
		 }
  
	public static double[] convertStringArrintoDoubleArr(String[] value)
	{
		
		double[] doubleprices = Arrays.stream(value).mapToDouble(Double::parseDouble).toArray();
		return doubleprices;
		
	}
    public static String getChannels(String channel) {
    	 channel = channel.replaceAll("Channel", "").trim();
    	 channel = channel.replaceAll("\\+", ",");
    	 return channel;
    	
    }
	
	}
