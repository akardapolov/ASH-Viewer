/*
 *-------------------
 * The Utils.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.util;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.Period;

import blanco.commons.sql.format.BlancoSqlFormatter;
import blanco.commons.sql.format.BlancoSqlFormatterException;
import blanco.commons.sql.format.BlancoSqlRule;

public class Utils {

	/**
	 * Get scale
	 * 
	 * @param percent
	 * @return
	 */
	static public double getScale(int scaleToggle, double percent){
		/* < 30  => 2 
		 * 30-70  => 1
		 * > 70    => 0 */
		double tmp = 0.0;
		switch (scaleToggle){
			case 0: tmp = 125/(percent+51);
				break;
			case 1: tmp = 147/(percent+51);
				break;
			case 2: tmp = 200/(percent+51);
				break;
			default: tmp = 130/(percent+51);
		}
		return tmp;
	}
	
	/**
	 * Sort hash map by values key0.
	 * 
	 * @param passedMap
	 * @param key0 COUNT or SUM
	 * @return LinkedHashMap
	 */
	static public LinkedHashMap<String, HashMap<String, Object>> sortHashMapByValues(
			HashMap<String, HashMap<String, Object>> passedMap, String key0) {

		List mapKeys = new ArrayList();
		List mapValues = new ArrayList();

		for (Entry<String, HashMap<String, Object>> me : passedMap.entrySet()) {
			mapKeys.add(me.getKey());
			mapValues.add(me.getValue().get(key0));
		}

		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);

		LinkedHashMap<String, HashMap<String, Object>> sortedMap = new LinkedHashMap<String, HashMap<String, Object>>();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).get(key0).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					sortedMap.put((String) key, passedMap.get(key));
					passedMap.remove(key);
					mapKeys.remove(key);
					break;
				}
			}
		}
		return sortedMap;
	}
		
	/**
	 * Sort hashmap desc.
	 * 
	 * @param passedMap the passed map
	 * @return the LinkedHashMap
	 */
	static public LinkedHashMap sortHashMapByValuesDesc(
			HashMap<String, Double> passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);
		Collections.reverse(mapValues);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;
	}
	   
	/**
	 * Round double.
	 * 
	 * @param dthe d
	 * @param decimalPlace the decimal place
	 * @return the double
	 */
	static public double round(double d, int decimalPlace) {
		BigDecimal bd;
		try {
			bd = new BigDecimal(Double.toString(d));
			bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
			return bd.doubleValue();
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	/**
	 * Round bytes and split with K,M
	 * @param bytes
	 * @return
	 */
	static public String roundBytes(double bytes){		
		Double doubleBytes = bytes;
		Long integerBytes = (long)bytes; 
		
		if (integerBytes<102400){
			Double tmp = Utils.round(doubleBytes, 0);
			return tmp.longValue()+"";
		} else if(integerBytes>=102400 && integerBytes<1024000){
			Double tmp = Utils.round(doubleBytes/1024, 0);
			return tmp.longValue()+"K";
		} else if(integerBytes>=1024000){
			Double tmp = Utils.round(doubleBytes/1024/1024, 0);
			return tmp.longValue()+"M";
		} else {
		   return "";
		}
	}
	
	/**
	 * Format text of sql query (limit 100 chars).
	 * 
	 * @param sIn input string
	 * @return string formatted text
	 */
	static public StringBuffer formatSqlQueryShort(String sIn){

		StringBuffer sbOut = new StringBuffer();
		
		if (sIn.equalsIgnoreCase("No data")){
			sbOut.append("<html>");
				sbOut.append("<b>");
					sbOut.append("No data");
				sbOut.append("</b>");
			sbOut.append("</html>");
		} else {
			StringBuffer sbOutTmp = new StringBuffer();
			if (sIn.length()>90){
				String sTmpSubstr1 = sIn.substring(0, 90);
				String sTmpSubstr2 = 
					sTmpSubstr1.substring(0,
							sTmpSubstr1.lastIndexOf(" ") < 40 ? 80 : sTmpSubstr1.lastIndexOf(" "));
				sbOutTmp.append(sTmpSubstr2+" ...");
			} else {
				sbOutTmp.append(sIn);
			}
			sbOut.append("<html>");
				sbOut.append("<b>");
					sbOut.append(sbOutTmp);
				sbOut.append("</b>");
			sbOut.append("</html>");
		}

	   return sbOut;
	}
	
	/**
	 * Format all text of sql query.
	 * 
	 * @param sIn input string
	 * @return string formatted text
	 */
	static public StringBuffer formatSqlAll(String sIn){

		StringBuffer sbOut = new StringBuffer();
				
		// Formatter
		String formatedResult = "";
		BlancoSqlFormatter formatter = new BlancoSqlFormatter(
                new BlancoSqlRule());
		
		try {
			String replaceSCommentRes = sIn.replaceAll("--", "@@@~~~");
			String tempformatedResult = formatter.format(replaceSCommentRes);
			formatedResult = tempformatedResult.replaceAll("@@@~~~", "--");
		} catch (BlancoSqlFormatterException e) {
			System.out.println("Error on parsing sql_text 1 level! Sql: "+formatedResult);
			try {
				String substrUpTo = sIn.substring(0, sIn.lastIndexOf(" "));
				String replaceSCommentRes = substrUpTo.replaceAll("--", "@@@~~~");
				String tempformatedResult = formatter.format(replaceSCommentRes);
				formatedResult = tempformatedResult.replaceAll("@@@~~~", "--");
			} catch (BlancoSqlFormatterException e1) {
				System.out.println("Error on parsing sql_text 2 level! Sql: "+formatedResult);
				try {
					String substrUpTo = sIn.substring(0, sIn.lastIndexOf("/*"));
					String replaceSCommentRes = substrUpTo.replaceAll("--", "@@@~~~");
					String tempformatedResult = formatter.format(replaceSCommentRes);
					formatedResult = tempformatedResult.replaceAll("@@@~~~", "--");
				} catch (BlancoSqlFormatterException e2) {
					formatedResult = sIn;
					System.out.println("Error on parsing sql_text 3 level! Sql: "+formatedResult);
				}
			}
		}
		sbOut.append(formatedResult);
				
		return sbOut;
	}

	/**
	 * Set current clipboard content.
	 * 
	 * @param content
	 * @param owner
	 * @return 
	 */
	static public void setClipBoardContent(String content) {
		if (Options.getInstance().isCopySqlToClibpoard()) {
			StringSelection ssContent = new StringSelection(content.toString());
			StringSelection ssOwner = new StringSelection("ASH Viewer");
			// Clear clipboard
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					new StringSelection(" "), ssOwner);
			// Load data to clipboard
			try {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
						ssContent, ssOwner);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("System clipboard is busy.");
			}
		}
	}
	
	/**
	 *  Get size of folder .
	 *  
	 * @param dir
	 * @return
	 */
	static public long getFolderSize(File dir){
		long dirSize = 0;
		File[] filelist = dir.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			dirSize += filelist[i].length();
		} 
		return dirSize;
	}
	
	/**
	 *  Delete the directory.
	 *  
	 * @param dir
	 */
	static public boolean deleteDirectory(File dir){
		 if( dir.exists() ) {
		      File[] files = dir.listFiles();
		      for(int i=0; i<files.length; i++) {
		         if(files[i].isDirectory()) {
		           deleteDirectory(files[i]);
		         }
		         else {
		           files[i].delete();
		         }
		      }
		    }
		    return(dir.delete());
	}
		
	/**
	 * Get levels of all nodes of sql plan tree.
	 * @param idParentId
	 * @return
	 */
	static public HashMap<Long,Long> getLevels(HashMap<Long,Long> idParentId){
		long rootLevel = 1;
		HashMap<Long, Long> idLevel = new HashMap<Long, Long>();
		Set entries = idParentId.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Long key = (Long) entry.getKey();
			Long value = (Long) entry.getValue();
			if (key==0){
				idLevel.put(key, rootLevel);
			} else {
				idLevel.put(key, getLevel(idParentId,key));
			}
		}
		
		return idLevel;
	}
	
	/**
	 * Get node level.
	 * @param idParentId
	 * @param id
	 * @return
	 */
	static private Long getLevel (HashMap<Long,Long> idParentId, Long id){
		long level = 0;
		Set entries = idParentId.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Long key = (Long) entry.getKey();
			Long value = (Long) entry.getValue();
			if (key == id){
				if (value != -1){
				   if (value ==0){
					   level = 2 + getLevel(idParentId, value);
				   } else {
					   level = 1 + getLevel(idParentId, value);
				   }
				}
			} 
		}
		
		return level;
	}
	
	/**
	 * Returns a string with HTML special characters replaced by their entity
	 * equivalents.
	 *
	 * @param str the string to escape
	 * @return a new string without HTML special characters
	 */
	public static String escapeHTML(String str) {
	    if (str == null || str.length() == 0)
		return "";

	    StringBuffer buf = new StringBuffer();
	    int len = str.length();
	    for (int i = 0; i < len; ++i) {
		char c = str.charAt(i);
		switch (c) {
		case '&': buf.append("&amp;"); break;
		case '<': buf.append("&lt;"); break;
		case '>': buf.append("&gt;"); break;
		case '"': buf.append("&quot;"); break;
		//case '\'': buf.append("&apos;"); break;
		default: buf.append(c); break;
		}
	    }
	    return buf.toString();
	}
	
	public static String getTimeFormatting(double intervalSec){
		String out = "";
		
		// Exit when intervalSec == 0
		if (intervalSec == 0.0){
			return out;
		}
		
		DateTime start = new DateTime(0);
		DateTime end = new DateTime((long) (intervalSec * 1000));
		Period period = new Period(start, end);
		
		String hours = "";
		String minutes = "";
		String secunds = "";
		
		if (period.getHours()<10){
			hours = "0"+period.getHours();
		} else {
			hours = ""+period.getHours();
		}
		if (period.getMinutes()<10){
			minutes = "0"+period.getMinutes();
		} else {
			minutes = ""+period.getMinutes();
		}
		if (period.getSeconds()<10){
			secunds = "0"+period.getSeconds();
		} else {
			secunds = ""+period.getSeconds();
		}
		
		out = hours + ":" + minutes + ":" + secunds;
		
		return out;
	}
	
}
