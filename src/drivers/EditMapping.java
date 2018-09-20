package drivers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import analyzers.DataAnalyzer;
import entities.Data;
import entities.Mapping;
import interactive.Dialogue;
import interactive.MappingUpdateDialogue;
import writers.MappingWriter;

public class EditMapping {
		
	
	private static char MAPPING_DELIMITER = ',';
	private static String MAPPING_FILE = "";
	private static String DESTINATION_MAPPING_FILE = "";
	private static char MAPPING_QUOTED_STRING = '"';
	private static boolean MAPPING_SKIP_HEADER = true;
	private static String DATA_FILE = "";
	private static char DATA_FILE_DELIMITER = ',';
	private static char DATA_FILE_QUOTED_STRING = '"';
	private static boolean DATA_FILE_HAS_HEADERS = true;
	
	
	private static List<Mapping> mappings = new ArrayList<Mapping>();
	
	private static List<Data> headers = new ArrayList<Data>(); 
	
	public static void main(String[] args) throws Exception {
		
		processArguments(args);

		if(DATA_FILE.isEmpty()) {
			System.out.println(MappingUpdateDialogue.ASK_FOR_DATA_FILE_URL);
			Scanner reader = new Scanner(System.in);
			DATA_FILE = reader.nextLine();
		}
		
		if(MAPPING_FILE.isEmpty()) {
			System.out.println(MappingUpdateDialogue.ASK_FOR_MAPPING_FILE_URL);
			Scanner reader = new Scanner(System.in);
			MAPPING_FILE = reader.nextLine();
		}
		if(DESTINATION_MAPPING_FILE.isEmpty()) DESTINATION_MAPPING_FILE = MAPPING_FILE;

		headers = buildDataListHeaders();
		
		mappings = generateMapping();
		
		System.out.println(MappingUpdateDialogue.GREETINGS);
		
		Scanner reader = new Scanner(System.in);
		
		if(reader.nextLine().toUpperCase().contains("Y")) processAllColumns();
		else processColumn();
		
	}
	
	private static void processColumn() throws IOException {
		System.out.println(MappingUpdateDialogue.ASK_FOR_COLUMN);
		
		Scanner reader = new Scanner(System.in);
	
		String column = reader.nextLine();
		if(!column.isEmpty()) {
			for(Data data: headers) {
				if(data.getDataLabel().equalsIgnoreCase(column)) {
					updateMappingFile(data);
				} else if ( new Integer(data.getColIndex()).toString().equalsIgnoreCase(column)){
					updateMappingFile(data);
				} else {
					System.err.println(MappingUpdateDialogue.INVALID_COLUMN);
					processColumn();
				}
			}
		}
		System.out.println(MappingUpdateDialogue.ASK_FOR_ADDITIONAL_UPDATES);
		
		String additionUpdates = reader.nextLine();
		
		if(additionUpdates.toUpperCase().contains("Y")) {
			System.out.println(MappingUpdateDialogue.SAVE_FILE);
			if(reader.nextLine().toUpperCase().contains("Y")) {
				writefiles();
			}
			processColumn();
		} else {
			//save before exiting.
			writefiles();
			
		}
	}

	private static void writefiles() throws IOException {
		System.out.println(MappingUpdateDialogue.WRITING_UPDATES);
		MappingWriter.printMappingFile(DESTINATION_MAPPING_FILE, MAPPING_DELIMITER, MAPPING_QUOTED_STRING, mappings);
		System.out.println(DESTINATION_MAPPING_FILE + " File Updated!!");		
	}

	private static void updateMappingFile(Data data) {
		
		String key = data.getFileName() + ":" + data.getColIndex();
		
		for(Mapping mapping: mappings) {
			if(mapping.getKey().equalsIgnoreCase(key)) {
				updateMappingRecord(mapping);
				break;
			}
		}
	}

	private static void updateMappingRecord(Mapping mapping) {
		System.out.println(MappingUpdateDialogue.DISPLAY_CURRENT_PATH);
		
		System.out.println(mapping.getRootNode());
		
		System.out.println(MappingUpdateDialogue.ASK_FOR_UPDATED_PATH);
		
		Scanner reader = new Scanner(System.in);

		String newPath = reader.nextLine();
		
		String validatedPath = newPath.replaceAll("[*|/<\\?%>\":]", "");
		
		if(validatedPath.equals(newPath)) {
			mapping.setRootNode(newPath);
			System.out.println(MappingUpdateDialogue.PATH_SUCCESS);
		}
		else {
			System.err.println(MappingUpdateDialogue.PATH_FAIL);
			updateMappingRecord(mapping);
		}
	}

	private static boolean validatePath(String newPath) {
		// TODO Auto-generated method stub
		return false;
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
	
	private static List<Data> buildDataListHeaders() {
		
		try {
			return Data.buildDataListHeaders(DATA_FILE, DATA_FILE_HAS_HEADERS, DATA_FILE_DELIMITER, DATA_FILE_QUOTED_STRING);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());;
		}	
		return new ArrayList<Data>();
	}

	private static void processAllColumns() throws IOException {
		
		for(Mapping mapping: mappings) {
			updateMappingRecord(mapping);
			
			Scanner reader = new Scanner(System.in);
			
			System.out.println(MappingUpdateDialogue.SAVE_FILE);
			if(reader.nextLine().toUpperCase().contains("Y")) {
				writefiles();
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
			
			}		
			
		}
		
	}
}
