package entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class PatientMapping {
	private String fileName;
	private String patId;
	private String dobCol;
	private String dodCol;
	private String ageCol;
	private String sexCol;
	private String raceCol;

	public static String FILE_NAME = ""; 

	public static String PATIENT_ID_COL = ""; 
	public static String PATIENT_GENDER_COL = "";
	public static String PATIENT_RACE_COL = "";

	public static String PATIENT_AGE_COL = "";
	public PatientMapping(){
		super();
	}
	
	public PatientMapping(String[] arr) {
		super();
		this.fileName = arr[0];
		this.patId = arr[1];
		this.dobCol = arr[2];
		this.dodCol = arr[3];
		this.ageCol = arr[4];
		this.sexCol = arr[5];
		this.raceCol = arr[6];
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getDobCol() {
		return dobCol;
	}
	public void setDobCol(String dobCol) {
		this.dobCol = dobCol;
	}
	public String getDodCol() {
		return dodCol;
	}
	public void setDodCol(String dodCol) {
		this.dodCol = dodCol;
	}
	public String getAgeCol() {
		return ageCol;
	}
	public void setAgeCol(String ageCol) {
		this.ageCol = ageCol;
	}
	public String getSexCol() {
		return sexCol;
	}
	public void setSexCol(String sexCol) {
		this.sexCol = sexCol;
	}
	public String getRaceCol() {
		return raceCol;
	}
	public void setRaceCol(String raceCol) {
		this.raceCol = raceCol;
	}

	public static Map<String, List<PatientMapping>> processMapping(
			CSVReader mappingReader) throws IOException {
		Map<String,List<PatientMapping>> map = new HashMap<String,List<PatientMapping>>();

		for(String[] arr: mappingReader.readAll()){
			
			if(map.containsKey(arr[0])){
				
				List<PatientMapping> list = map.get(arr[0]);

				list.add(new PatientMapping(arr));
				
				map.put(arr[0], list);
				
			} else {
				
				List<PatientMapping> list = new ArrayList<PatientMapping>();

				list.add(new PatientMapping(arr));
				
				map.put(arr[0], list);
			}
			
		}
			

		
		return map;
	}

	public PatientMapping(String fileName, String patId, String dobCol, String dodCol, String ageCol, String sexCol,
			String raceCol) {
		super();
		this.fileName = fileName;
		this.patId = patId;
		this.dobCol = dobCol;
		this.dodCol = dodCol;
		this.ageCol = ageCol;
		this.sexCol = sexCol;
		this.raceCol = raceCol;
	}
	
	public String[] toStringArray() {
		List<String> list = new ArrayList<String>();
		
		list.add(this.fileName);
		list.add(this.patId);
		list.add(this.dobCol);
		list.add(this.dodCol);
		list.add(this.ageCol);		
		list.add(this.sexCol);
		list.add(this.raceCol);
		return list.toArray(new String[0]);
	}
}
