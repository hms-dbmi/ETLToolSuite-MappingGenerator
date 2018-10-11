# ETLToolSuite-MappingGenerator

Author: Thomas DeSain

Used to generate mapping files for I2B2TM ETL processes.  
***
**How to run the example for GSE13168**  

Prerequistes:  
Admin rights to machine hosting docker     
[Quick Start docker stack](https://github.com/hms-dbmi/docker-images/tree/master/deployments/i2b2transmart/quickstart)     
[ETL Client Docker](https://github.com/hms-dbmi/etl-client-docker)    
***  
**Overview**:    
This will be a simple walkthrough to load the example data contained in the MappingGenerator project.
Similar steps can be used to process other CSV data.

Steps:  
This example was validated on a mac and AMI Linux terminal  

1. Open bash connection to your ETL Client Docker  
`docker exec -it etl-client bash`   
2. use git to clone this project to a dir of your choosing.  
`git clone https://github.com/hms-dbmi/ETLToolSuite-MappingGenerator`  
3. cd to root directory:  
`cd ETLToolSuite-MappingGenerator`   
4. execute following code block:  
`java -jar MappingGenerator.jar -datafile ./example/Asthma_Misior_GSE13168.txt -datafileanalyze Y`   
5. Press enter to leave delimiter as comma by default when prompted "Enter the delimiter used in data file(s) ( tab/comma/other default=comma ): "   
6. Enter the value **example** when prompted "Enter the path to your destination directory (default=working dir): "   
`example` 
5. Enter the value **SUBJ_ID** when prompted "Enter the column that holds patient identifier: "    
`SUBJ_ID`
6. Enter the value **CurrentAge** when prompted "Enter the column that holds patients age: "    
`CurrentAge`
7. Press enter to skip when prompted "Enter the column that holds patient's gender: " as test data does not have gender information.   
8. Press enter to skip when prompted "Enter the column that holds patient's race: " as test data does not have race information.   
9. Mapping Generator will now run.   
10. Navigate to example directory   
`cd example`    
11. list the directory's contents.  
`ls -la`  
12. You should see the newly generated mapping files mapping.csv and mapping.csv.patient.     
13. exit docker container   
`exit`   
14. Using the files generated here you can now process the data through the [Entity Generator tool](https://github.com/hms-dbmi/ETLToolSuite-EntityGenerator).     

***
**Mapping Editor**    
Once you have generated your mapping file you can now update the concept paths that will be displayed in the Applications Navigate Terms explorer.

To get started using the concept editor follow these steps.  Same prerequistes as the mapping generator.  Can also use the same flags listed below.  
1. Open bash connection to your ETL Client Docker     
`docker exec -it etl-client bash`      
2. Navigate to root directory to MappingGenerator project:     
`cd ETLToolSuite-MappingGenerator`      
3. Run the Mapping Editor:    
`java -jar MappingEditor.jar`      
3.1 If you want to save your old mapping file make sure to add flag for the new destination mapping file to be written:    
``java -jar MappingEditor.jar -destinationmappingfile ./example/mapping_new.csv``    
3.2 You can use the -datafile flag and -mappingfile flag to also skip some dialogue.   
``java -jar MappingEditor.jar -datafile ./example/Asthma_Misior_GSE13168.txt -mappingfile ./example/mapping.csv``   


***
**Mapping Generator Flags**    
*-datafile*  
Location of the data file you wish to generate a mapping file for.  
Example:  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.csv`  

*-datafiledelimiter*   
Character used to seperate columns in the data file.  default is comma.  
Examples:  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.tsv -datafiledelimiter TAB`  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.csv -datafiledelimiter COMMA`  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.csv -datafiledelimiter ,`  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.txt -datafiledelimiter |`  

*-datafilequotedstring*  
Character used in datafile for quoted strings.  Default is double quotes "   
Example:  
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafilequotedstring "`  

*-destinationmappingfile*  
Path and Name of the mapping file to be generated. default behavior is to generate a filenamed mapping.txt in working folder.   
Example:  
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -destinationmappingfile ./example/GSE13168_Mapping.csv`  

*-datafilehasheaders*  
Specifies whether or not the data file has headers.  Headers will be used as the default concept path when generating the mapping file.  
If no headers are present it will just use the column index as the default concept path.  
Default value is Y  
Example:  
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafilehasheaders N`  

*-datafileanalyze*  
Analyze the data to determine its data type ( numeric or text ).   
Analyze uses following algorithm to weigh its logic:  
Groups all values in each variable ( column ).  
Analyzes each value to determine if it is numeric.   
If numeric values total a percentage higher than the numeric threshold ( default 85% ) it will assign it as numeric.   
Default value = N.  
Example:    
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafileanalyze Y`   

*-analyzethreshold*    
You can override the numeric threshold for the analyze algorithm here.  Default is .85% 
Example:    
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafileanalyze Y -analyzethreshold .90`     

