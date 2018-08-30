package analyzers;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.opencsv.CSVReader;

import entities.Data;
import entities.DataRecord;
import entities.Mapping;

public class DataAnalyzer {
	public static double NUMERIC_THRESHOLD = .85;

	public static void analyze(List<Data> fullData) {
		for(Data data: fullData) {
			int ints = 0;
			int strs = 0;
			boolean isInt = false;
			for(DataRecord dr: data.getDataRecords()) {
				if(dr.getValue().isEmpty()) continue;
				
				String value = dr.getValue();
				int index = 0;
				
				int valInts = 0;
				int valStrs = 0;
				
				for(char c: value.toCharArray()) {
					if(index == 0 && c == '-') {
						if(value.length() == 1) valStrs++;
						else continue;
					}
					if(c == '.') continue; 
					if(Character.digit(c,10) < 0) valStrs++;
					else valInts++;
					index++;
				}
				if(new BigDecimal(valInts).divide( new BigDecimal(index),2,RoundingMode.HALF_UP).doubleValue() > NUMERIC_THRESHOLD) ints++;
				else strs++;
			}
			
			if(ints > strs) isInt = true;
			
			if(isInt) data.setDataType("NUMERIC");
			else data.setDataType("TEXT");
			
		}
		
	}

}
