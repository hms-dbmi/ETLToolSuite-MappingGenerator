package writers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

import entities.Mapping;

public class MappingWriter {

	public static void printMappingFile(String mappingWriteURI, char mappingWriteDelimiter, char mappingQuoteChar, List<Mapping> mappings) throws IOException {
		
		File file = new File(mappingWriteURI);
		
		//if(file.canWrite()) {
			CSVWriter csvWriter = new CSVWriter(new FileWriter(file), mappingWriteDelimiter, mappingQuoteChar);
			
			for(Mapping mapping: mappings) {
				csvWriter.writeNext(mapping.toStringArray());
			}
			
			csvWriter.flush();
			csvWriter.close();
		//}
		
	}
	
	
	
}
