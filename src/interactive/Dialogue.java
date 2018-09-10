package interactive;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import drivers.Driver;
import entities.Data;
import entities.PatientMapping2;

public class Dialogue {
	public static String patientfilediag = "Enter the path to data file that contains patient demographics: ";
	public static String greeting = "";
	public static String datafilediag = "Enter the path to the file or directory you would like to load: ";
	public static String datafiledelimiterdiag = "Enter the delimiter used in data file(s) ( tab/comma/other default=comma ): ";
	public static String datafiledelimiterdiag2 = "Enter the char used as delimiter used in data file(s): ";
	public static String datafilequotedstringdiag = "Enter the char used for quoted strings ( default=\" ): ";
	public static String datafilehasheadersdiag = "Does the data file contain a header(y/n default=y): ";
	public static String datafileanalyzediag = "Would you like to auto-analyze the data types ( y/n default=n ): ";
	public static String analyzethresholddiag = "Set the threshold limit( default=85% ): ";
	public static String destinationmappingfilediag = "Enter the path to your destination directory (default=working dir): ";
	
	public static String patientiddiag = "Enter the column that holds patient identifier: ";
	public static String patientdemodiag = "Does your data contain patient demographics? ( y/n default=n): ";
	public static String patientagediag = "Enter the column that holds patient's age: ";
	public static String patientgenderdiag = "Enter the column that holds patient's gender: ";
	public static String patientracediag = "Enter the column that holds patient's race: ";
	
	public static String[] promptFields = new String[] { "-datafile","-datafiledelimiter", "-datafilequotedstring", 
			"-datafilehasheaders", "-datafileanalyze", "-destinationmappingfile", "-patientfile", "-patientidcol", "-patientagecol", "-patientgendercol", "-patientracecol" };
	
	
	public static String[] checkArgs(String[] args) {
		List<String> argsL = new ArrayList<String>(Arrays.asList(args));
		
		for(String promptF: promptFields) {
			if(!argsL.contains(promptF)) {
				runDialogue(promptF);
			}
		}
		
		
		return args;
	}


	private static void runDialogue(String promptF) {
		Scanner reader = new Scanner(System.in);
		//switch(promptF) {
			if(promptF.equals("-datafile")){
				System.out.print(datafilediag);
				Driver.DATA_FILE = reader.nextLine();
			}
			if(promptF.equals("-datafiledelimiter")) {
				System.out.print(datafiledelimiterdiag);
				
				String in = reader.nextLine();
				if(in.equalsIgnoreCase("TAB")) Driver.DATA_FILE_DELIMITER = '\t';
				
				else if(in.equalsIgnoreCase("COMMA")) Driver.DATA_FILE_DELIMITER = ',';
				
				else if(in.equalsIgnoreCase("other")) {
					System.out.print(datafiledelimiterdiag2);
					 
					in = reader.nextLine();
					if(!in.replaceAll("\\s+","").isEmpty()){
						Driver.DATA_FILE_DELIMITER = in.toCharArray()[0];
					}
				} else if(!in.replaceAll("\\s+","").isEmpty()){
					
					Driver.DATA_FILE_DELIMITER = in.toCharArray()[0];
				}
				
			}
	
			//Driver.DATA_FILE_DELIMITER = reader.next().charAt(0);
			//case "-datafilequotedstring":System.out.println(datafilequotedstringdiag);
			//Driver.DATA_FILE_DELIMITER = reader.next().charAt(0);
			//case "-datafilehasheaders":System.out.println(datafilehasheadersdiag);
			
			if(promptF.equals("-datafileanalyze")){
				System.out.print(datafileanalyzediag);
				String in = reader.nextLine();
				Driver.DATA_FILE_ANALYZE = in.length() >  0 && ( in.charAt(0) =='Y' || in.charAt(0) == 'y' )? true : false;
			}
			
			//case "-analyzethreshold":System.out.println(analyzethresholddiag);
						
			if(promptF.equals("-destinationmappingfile")){
				System.out.print(destinationmappingfilediag);
				String in = reader.nextLine();
				
				if(!new File(in).isDirectory()) {
					Driver.DESTINATION_MAPPING_FILE = in;
				} else {
					if(in.endsWith("/")) in = in + "mapping.csv";
					else in = in + '/' + "mapping.csv";
					
					Driver.DESTINATION_MAPPING_FILE = in;
				}
			}
			
		
		//
		//	reader.close();
	}


	public static List<PatientMapping2> checkPatientArgs(String[] args, List<Data> dataListHeaders) {
		List<String> argsL = new ArrayList<String>(Arrays.asList(args));
		List<PatientMapping2> mappings = new ArrayList<PatientMapping2>();

		for(String promptF: promptFields) {
			if(!argsL.contains(promptF)) {
				mappings.addAll(runPatientDialogue(promptF, dataListHeaders));
			}
							
		}
		return mappings;
	}


	private static List<PatientMapping2> runPatientDialogue(String promptF, List<Data> dataListHeaders) {
		Scanner reader = new Scanner(System.in);
		List<PatientMapping2> mappings = new ArrayList<PatientMapping2>();
		//switch(promptF) {
		boolean multifile = false;
		if(promptF.equals("-patientfile")){
			if(new File(Driver.DATA_FILE).isDirectory()) {
				System.out.println("Are your patients demographics in multiple files? ( y/n )");
				String in = reader.nextLine();
				
				if(in.equalsIgnoreCase("Y") || in.equalsIgnoreCase("yes")) {
					multifile = true;
				} else {
					System.out.print(patientfilediag);
					PatientMapping2.FILE_NAME = reader.nextLine();

				}
								
			} else {
				PatientMapping2.FILE_NAME = new File(Driver.DATA_FILE).getName();
			}
			
		}
		
		if(promptF.equals("-patientidcol")){
			PatientMapping2 pm = new PatientMapping2();
			if(multifile) {
				
				System.out.println("Enter path to file containing all patient ids: ");
				String in = reader.nextLine();
				while(!new File(in).exists()) {
					System.err.println("File does not exist: ");
					System.out.println("Enter path to file containing all patient ids: ");
					in = reader.nextLine();
					
				} 
				PatientMapping2.FILE_NAME = reader.nextLine();
			}
			
			System.out.print(patientiddiag);
			String in = reader.nextLine();

			boolean isInt = false;
			for(char c: in.toCharArray()) {
				if( !Character.isDigit(c) ) {
					isInt = false;
					break;
				} else {
					isInt = true;
				}
			}
			if(isInt) {
				PatientMapping2.PATIENT_ID_COL = new Integer( new Integer(in) - 1 ).toString();
			} else {
				for(Data d: dataListHeaders) {
					if(d.getDataLabel().equalsIgnoreCase(in)) {
						PatientMapping2.PATIENT_ID_COL = new Integer(d.getColIndex()).toString();
					}
				}
			}
			pm.setPatientKey(PatientMapping2.FILE_NAME + ':' + PatientMapping2.PATIENT_ID_COL);
			pm.setPatientColumn("PatientNum");
			if(!in.isEmpty()) mappings.add(pm);
			
		} else if(promptF.equals("-patientagecol")){

			PatientMapping2 pm = new PatientMapping2();
			if(multifile) {
				
				System.out.println("Enter path to file containing patient age: ");
				String in = reader.nextLine();
				while(!new File(in).exists()) {
					System.err.println("File does not exist: ");
					System.out.println("Enter path to file containing patient age: ");
					in = reader.nextLine();
					
				} 
				PatientMapping2.FILE_NAME = reader.nextLine();
			}
			
			System.out.print(patientagediag);
			String in = reader.nextLine();
			boolean isInt = false;
			for(char c: in.toCharArray()) {
				if( !Character.isDigit(c) ) {
					isInt = false;
					break;
				} else {
					isInt = true;
				}
			}
			if(isInt) {
				PatientMapping2.PATIENT_AGE_COL = new Integer( new Integer(in) - 1 ).toString();;
			} else {
				for(Data d: dataListHeaders) {
					if(d.getDataLabel().equalsIgnoreCase(in)) {
						PatientMapping2.PATIENT_AGE_COL = new Integer(d.getColIndex()).toString();
					}
				}
			}
			pm.setPatientKey(PatientMapping2.FILE_NAME + ':' + PatientMapping2.PATIENT_AGE_COL);
			pm.setPatientColumn("ageInYearsNum");
			if(!in.isEmpty()) mappings.add(pm);
			
		} else if(promptF.equals("-patientgendercol")){

			PatientMapping2 pm = new PatientMapping2();
			if(multifile) {
				
				System.out.println("Enter path to file containing patient gender/sex: ");
				String in = reader.nextLine();
				while(!new File(in).exists()) {
					System.err.println("File does not exist: ");
					System.out.println("Enter path to file containing patient gender/sex: ");
					in = reader.nextLine();
					
				} 
				PatientMapping2.FILE_NAME = reader.nextLine();
			}
			
			System.out.print(patientgenderdiag);
			String in = reader.nextLine();
			
			boolean isInt = false;
			for(char c: in.toCharArray()) {
				if( !Character.isDigit(c) ) {
					isInt = false;
					break;
				} else {
					isInt = true;
				}
			}
			if(isInt) {
				PatientMapping2.PATIENT_GENDER_COL = new Integer( new Integer(in) - 1 ).toString();
			} else {
				for(Data d: dataListHeaders) {
					if(d.getDataLabel().equalsIgnoreCase(in)) {
						PatientMapping2.PATIENT_GENDER_COL = new Integer(d.getColIndex()).toString();
					}
				}
			}
			pm.setPatientKey(PatientMapping2.FILE_NAME + ':' + PatientMapping2.PATIENT_GENDER_COL);
			pm.setPatientColumn("sexCD");
			if(!in.isEmpty()) mappings.add(pm);
			
		} else if(promptF.equals("-patientracecol")){
			
			if(multifile) {
				
				System.out.println("Enter path to file containing patient race: ");
				String in = reader.nextLine();
				while(!new File(in).exists()) {
					System.err.println("File does not exist: ");
					System.out.println("Enter path to file containing patient race: ");
					in = reader.nextLine();
					
				} 
				PatientMapping2.FILE_NAME = reader.nextLine();
			}
			
			System.out.print(patientracediag);
			String in = reader.nextLine();
			PatientMapping2 pm = new PatientMapping2();

			boolean isInt = false;
			for(char c: in.toCharArray()) {
				if( !Character.isDigit(c) ) {
					isInt = false;
					break;
				} else {
					isInt = true;
				}
			}
			if(isInt) {
				PatientMapping2.PATIENT_RACE_COL = new Integer( new Integer(in) - 1 ).toString();
			} else {
				for(Data d: dataListHeaders) {
					if(d.getDataLabel().equalsIgnoreCase(in)) {
						PatientMapping2.PATIENT_RACE_COL = new Integer(d.getColIndex()).toString();
					}
				}
			}
			pm.setPatientKey(PatientMapping2.FILE_NAME + ':' + PatientMapping2.PATIENT_GENDER_COL);
			pm.setPatientColumn("raceCD");
			
			if(!in.isEmpty()) mappings.add(pm);
			
		}
		return mappings;
	}
}
