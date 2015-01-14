package com.chimpler.example.fpm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class AssoRuleAnalysis {

	public static void main(String [] args) throws IOException {
		
		BufferedReader ruleReader = null;
		String support, confidence = "";
		int numOfRules = 0;
		
		support = args[0];
		confidence = args[1];
		
		String fileName = "helthin_assoRule_" + support + "_" + confidence + ".csv";
		String file_NumOfRules = "num_of_rules.csv";
		BufferedWriter numOfRuleWriter = new BufferedWriter(new FileWriter(file_NumOfRules, true));

		try {
			ruleReader = new BufferedReader(new FileReader(fileName));
			String line = "";
			
			while(true) {
				line = ruleReader.readLine();
				if(line == null)
					break;
			
				numOfRules++;
			}
		
			// check whether the file exist or not
			boolean fileExist = checkFileExist(file_NumOfRules);
			
			if(!fileExist) {
				File file = new File(file_NumOfRules);
				file.createNewFile();
			}
			
			if(checkCountBefore(support, confidence)) {
				
				numOfRuleWriter.write(support + "," + confidence + "," + numOfRules);
				numOfRuleWriter.write("\n");
			} else {
				System.out.println("The support and confidence count is already written.");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			ruleReader.close();
			numOfRuleWriter.close();
		}
	}
	
	public static boolean checkFileExist(String fileName) throws IOException {
		File file = new File(fileName);
		if(file.exists() && !file.isDirectory()) 
			return true;
		else
			return false;
	}
	
	public static boolean checkCountBefore(String support, String confidence) throws IOException {
		BufferedReader num_of_ruleReader = null;
		try {
			num_of_ruleReader = new BufferedReader(new FileReader("num_of_rules.csv"));
			String line = "";
			Hashtable<String, String> hashtable = new Hashtable<String, String>();
			
			while(true) {
				line = num_of_ruleReader.readLine();
				if(line == null)
					break;
				
				String s = line.split(",")[0];
				String c = line.split(",")[1];
				hashtable.put(s, c);
			}
			
			if(hashtable.containsKey(support) && hashtable.containsValue(confidence))
				return false;
			else
				return true;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			num_of_ruleReader.close();
		}
		return true;
	}
	
}
