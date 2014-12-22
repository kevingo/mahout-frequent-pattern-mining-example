package com.chimpler.example.fpm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileFormat {

	static String fileName = "./helthin/7-1.csv";
	static String productMapping = "./helthin/product_mapping.csv";
	static String transFile = "./helthin/trans.csv";
	static String basketFile = "./helthin/basket.csv";
	
	public static void main(String [] args) {
		
		try {
			String line = "";
			
			Map<String, String> productMap = new HashMap<String, String>();
			Map<String, String> basketMap = new HashMap<String, String>();
			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			BufferedWriter mappingWriter = new BufferedWriter(new FileWriter(productMapping));
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(transFile));
			BufferedWriter basketWriter = new BufferedWriter(new FileWriter(basketFile));
			
			while((line=br.readLine())!=null) {
				
				String[] tokens = line.split(",");
				int total = tokens.length;
				
				// check the length of tokens is 35
				if(total == 35) {
					
					String date = tokens[0];
					String product_id = tokens[2];
					String product_name = tokens[3];
					String price = tokens[8];
					String user = tokens[21];
					
					// Only user id > 0 will be saved
					if(user.length()>0) 
						fileWriter.write(tokens[0] + "," + tokens[2] + "," + tokens[3] + "," + tokens[21] + "," + tokens[8] + "\n");
				
					String comboKey = user + "_" + date;
					
					if(basketMap.containsKey(comboKey)) {
						String newValue = "";
						newValue = basketMap.get(comboKey) + "," +product_id;
						basketMap.put(comboKey, newValue);
					} else {
						basketMap.put(comboKey, product_id);
					}
					
				}
				
				for(int i = 0 ; i < tokens.length ; i++) {
					productMap.put(tokens[2], tokens[3]);
				}
			}
			
			for(Object key : productMap.keySet()) {
	            mappingWriter.write(productMap.get(key) + "," + key + "\n");
	        }
			
			
			for(Object key : basketMap.keySet()) {
				basketWriter.write(basketMap.get(key) + "\n");
			}
			
			br.close();
			mappingWriter.close();
			basketWriter.close();
			fileWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
