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

	public static void main(String [] args) {
		
		String rawFile = "./helthin/7-1.csv";
		String transFile = "./helthin/trans.csv";
		String basketFile = "./helthin/basket_wo_medical.csv";
		
		String categoryBigFile = "./helthin/category_big_mapping.csv";
		String categoryMidFile = "./helthin/category_mid_mapping.csv";
		String categorySmaFile = "./helthin/category_sma_mapping.csv";
		
		String productMappingFile = "./helthin/product_mapping.csv";
		Map<String, String> productMap = new HashMap<String, String>();
		BufferedWriter productMappingWriter = null;
		
		boolean removeMedical = true;
		
		try {
			String line = "";
			
			Map<String, String> basketMap = new HashMap<String, String>();
			Map<String, String> categoryBigMap = new HashMap<String, String>();
			Map<String, String> categoryMidMap = new HashMap<String, String>();
			Map<String, String> categorySmaMap = new HashMap<String, String>();
			
			BufferedReader rawFileReader = null;
			BufferedWriter transWriter = null;
			BufferedWriter basketWriter = null;
			BufferedWriter categoryWriter = null;
			
			// raw transaction file reader	
			rawFileReader = new BufferedReader(new FileReader(rawFile));
			
			// transaction writer
			transWriter = new BufferedWriter(new FileWriter(transFile));
			
			// basket writer
			basketWriter = new BufferedWriter(new FileWriter(basketFile));
			
			// product-mapping writer
			productMappingWriter = new BufferedWriter(new FileWriter(productMappingFile)); 
			
			// read each transaction line by line
			while( (line = rawFileReader.readLine()) != null ) {
				
				String[] tokens = line.split(",");
				int total = tokens.length;
				
				// check the length of tokens is 35
				if(total == 35) {
					
					String date = tokens[0];
					String product_id = tokens[2];
					String product_name = tokens[3];
					String price = tokens[8];
					String user = tokens[21];
					String cat_big_id = tokens[23];
					String cat_big = tokens[24];
					String cat_mid_id = tokens[25];
					String cat_mid = tokens[26];
					String cat_sma_id = tokens[27];
					String cat_sma = tokens[28];
					
					// check the user id exist, remove the transaction contains 藥品
					if(user.length()>0 && !cat_big_id.equals("01")) {
						transWriter.write(date + "," + product_id + "," + product_name + "," + user + "," + price + "," 
									+ cat_big_id + "," + cat_big + "," + cat_mid_id + "," + cat_mid + "," + cat_sma_id + "," + cat_sma + "\n");
						
						// The combination key is userID + date
						String comboKey = user + "_" + date;
						
						// generating basket map, key = combination key, value can be decided
						if(basketMap.containsKey(comboKey)) {
							String newValue = "";
							newValue = basketMap.get(comboKey) + "," + product_id;
							basketMap.put(comboKey, newValue);
						} else {
							basketMap.put(comboKey, product_id);
						}
						
						// parsing token by each transaction 
						for(int i = 0 ; i < tokens.length ; i++) {
							productMap.put(product_id, product_name);
							categoryBigMap.put(cat_big_id, cat_big);
							categoryMidMap.put(cat_mid_id, cat_mid);
							categorySmaMap.put(cat_sma_id, cat_sma);
						}
					}
				}
			}
			
			// write product-mapping file from product map
			for(Object key : productMap.keySet()) {
	            productMappingWriter.write(productMap.get(key) + "," + key + "\n");
	        }
			
			// write basket file from basket map
			for(Object key : basketMap.keySet()) {
				basketWriter.write(basketMap.get(key) + "\n");
			}
			
			// write category mapping file from category big map
			categoryWriter = new BufferedWriter(new FileWriter(categoryBigFile));
			for(Object key : categoryBigMap.keySet()) {
				categoryWriter.write(categoryBigMap.get(key) + "," + key + "\n");
			}
			categoryWriter.close();

			// write category mapping file from category mid map
			categoryWriter = new BufferedWriter(new FileWriter(categoryMidFile));
			for(Object key : categoryMidMap.keySet()) {
				categoryWriter.write(categoryMidMap.get(key) + "," + key + "\n");
			}
			categoryWriter.close();

			// write category mapping file from category small map
			categoryWriter = new BufferedWriter(new FileWriter(categorySmaFile));
			for(Object key : categorySmaMap.keySet()) {
				categoryWriter.write(categorySmaMap.get(key) + "," + key + "\n");
			}
			categoryWriter.close();
			
			rawFileReader.close();
			productMappingWriter.close();
			basketWriter.close();
			transWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
