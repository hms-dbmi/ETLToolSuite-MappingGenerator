package drivers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import analyzers.DataAnalyzer;
import entities.Mapping;
import entities.PatientMapping;

import interactive.Dialogue;
import writers.MappingWriter;
import entities.Data;

public class Driver {

	public static String MAPPING_FILE = "";

	public static String DESTINATION_MAPPING_FILE = "";
	
	public static char MAPPING_DELIMITER = ',';

	public static char MAPPING_QUOTED_STRING = '"';

	public static boolean MAPPING_SKIP_HEADER = false;

	public static String DATA_FILE = "";
	
	public static char DATA_FILE_DELIMITER = ',';
	
	public static char DATA_FILE_QUOTED_STRING = '"';
	
	public static boolean DATA_FILE_HAS_HEADERS = true;
	
	public static boolean DATA_FILE_ANALYZE = false;
	

	
	
	public static void main(String[] args) throws Exception {
		processArguments(args);
		
		Dialogue.checkArgs(args);
		
		List<Data> dataListHeaders = buildDataListHeaders();
		
		validateConfig();
		
		// read mapping file if given
		List<Mapping> mappings = MAPPING_FILE == null || MAPPING_FILE.isEmpty() ? new ArrayList<Mapping>(): generateMapping();
		List<PatientMapping> patientMappings = new ArrayList<PatientMapping>();

		// read data file 

		if(!DATA_FILE_ANALYZE) {
			
			mappings = Data.generateMappingList(DATA_FILE, dataListHeaders);
			patientMappings = Dialogue.checkPatientArgs(args, dataListHeaders);

		} 
		
		// do data type analysis
		if(DATA_FILE_ANALYZE) {
			// will update all mappings data type to a specific data type
			List<Data> fullData = Data.buildDataList(DATA_FILE, DATA_FILE_HAS_HEADERS, DATA_FILE_DELIMITER, DATA_FILE_QUOTED_STRING);
			
			DataAnalyzer.analyze(fullData);
			
			mappings = Data.generateMappingList(fullData);
			
			patientMappings = Dialogue.checkPatientArgs(args, dataListHeaders);

		}
		
		// print new mapping file
		MappingWriter.printMappingFile(DESTINATION_MAPPING_FILE, MAPPING_DELIMITER, MAPPING_QUOTED_STRING, mappings);
		MappingWriter.printPatientMappingFile(DESTINATION_MAPPING_FILE + ".patient", MAPPING_DELIMITER, MAPPING_QUOTED_STRING, patientMappings);

	}
	


	private static void validateConfig() {
		try {
			if(DATA_FILE.isEmpty()) {
				throw new Exception("Data file must be entered!");
			}
		
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	private static void printMappingFile(List<Mapping> mappings) {
		// TODO Auto-generated method stub
		
	}

	private static List<Data> buildDataListHeaders() {
		
		try {
			return Data.buildDataListHeaders(DATA_FILE, DATA_FILE_HAS_HEADERS, DATA_FILE_DELIMITER, DATA_FILE_QUOTED_STRING);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());;
		}	
		return new ArrayList<Data>();
	}

	private static List<Mapping> generateMapping() {
		List<Mapping> mappingFile = null;
		try {
			mappingFile = Mapping.class.newInstance().generateMappingList(MAPPING_FILE, MAPPING_SKIP_HEADER, MAPPING_DELIMITER, MAPPING_QUOTED_STRING);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mappingFile;
	}

	private static void processArguments(String[] args) throws Exception {
		for(String arg: args) {
			if(arg.equalsIgnoreCase( "-mappingdelimiter" )){
				
				MAPPING_DELIMITER = checkPassedArgs(arg, args).toCharArray()[0];
				
			} else if (arg.equalsIgnoreCase( "-mappingfile" )) {
			
				MAPPING_FILE = checkPassedArgs(arg, args);
			
			} else if (arg.equalsIgnoreCase( "-destinationmappingfile" )) {
			
				DESTINATION_MAPPING_FILE = checkPassedArgs(arg, args);
			
			} else if (arg.equalsIgnoreCase( "-mappingquotedstring" )) {
			
				MAPPING_QUOTED_STRING = checkPassedArgs(arg, args).toCharArray()[0];
			
			} else if (arg.equalsIgnoreCase( "-mappingskipheader" )) {
			
				MAPPING_SKIP_HEADER = checkPassedArgs(arg, args).equalsIgnoreCase("Y") || checkPassedArgs(arg, args).equalsIgnoreCase("YES") ? true : false;
				
			} else if (arg.equalsIgnoreCase( "-datafile" )) {
			
				DATA_FILE = checkPassedArgs(arg, args);
			
			} else if (arg.equalsIgnoreCase( "-datafiledelimiter" )) {
				if(checkPassedArgs(arg, args).equalsIgnoreCase("TAB")) DATA_FILE_DELIMITER = '\t';
				else if(checkPassedArgs(arg, args).equalsIgnoreCase("COMMA")) DATA_FILE_DELIMITER = ',';
				else {
					DATA_FILE_DELIMITER = checkPassedArgs(arg, args).toCharArray()[0];
				}
			} else if (arg.equalsIgnoreCase( "-datafilequotedstring" )) {
			
				DATA_FILE_QUOTED_STRING = checkPassedArgs(arg, args).toCharArray()[0];
			
			} else if (arg.equalsIgnoreCase( "-datafilehasheaders" )) {
				
				DATA_FILE_HAS_HEADERS = checkPassedArgs(arg, args).equalsIgnoreCase("Y") || checkPassedArgs(arg, args).equalsIgnoreCase("YES") ? true : false;;
			
			} else if (arg.equalsIgnoreCase( "-datafileanalyze" )) {
			
				DATA_FILE_ANALYZE = checkPassedArgs(arg, args).equalsIgnoreCase("Y") || checkPassedArgs(arg, args).equalsIgnoreCase("YES") ? true : false;;
			
			} else if (arg.equalsIgnoreCase( "-analyzethreshold" )) {
			
				DataAnalyzer.NUMERIC_THRESHOLD = new Double(checkPassedArgs(arg, args));
			
			}			
			
		}
		
	}

	// checks passed arguments and sends back value for that argument
	public static String checkPassedArgs(String arg, String[] args) throws Exception {
		
		int argcount = 0;
		
		String argv = new String();
		
		for(String thisarg: args) {
			
			if(thisarg.equals(arg)) {
				
				break;
				
			} else {
				
				argcount++;
				
			}
		}
		try {
			if(args.length > argcount) {
				
				argv = args[argcount + 1];
				
			} else {
				
				throw new Exception("Error in argument: " + arg );
				
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());;
		}
		return argv;

	}
}

