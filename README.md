# ETLToolSuite-MappingGenerator

Author: Thomas DeSain

Used to generate mapping files for I2B2TM ETL processes.  
***
**How to run the example for GSE13168**  

Prerequistes:  
git  
Java8  
admin rights on the machine  
***  
Overview:
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
10. cd to example dir 
`cd example`  
11. list the directory's contents.  
`ls -la`  
12. exit docker containter
`exit`
13. You should see the newly generated mapping files mapping.csv and mapping.csv.patient.  Using this and the associated data file you can now process the data through the [data generator tool](https://github.com/hms-dbmi/ETLToolSuite-EntityGenerator).  

***
**Mapping Generator Flags**    
*-datafile*  
Location of the data file you wish to generate a mapping file for.  
Example:  
`java -jar MappingGenerator.jar -datafile \Users\Home\data.csv`  

*-datafiledelimiter*   
Character used to seperate cells in the data file.  default is comma.  
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
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafiledelimiter -destinationmappingfile ./example/GSE13168_Mapping.csv`  

*-datafilehasheaders*  
Specifies whether or not the data file has headers.  Headers will be used as the default concept path when generating the mapping file.  
If no headers are present it will just use the column index as the default concept path.  
Default value is Y  
Example:  
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafilehasheaders N`  

*-datafileanalyze*  
Will analyze the data to determine its generic data type ( numeric or text ).   
Analyze uses following algorithm to weigh its logic:  
Groups all values by its concept ( column ).  
Analyzes each value to determine if it numeric.   
If numeric values total a percentage higher than the numeric threshold ( default 85% ) it will assign it as numeric.   
Default value = N.  
Example:    
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafileanalyze Y`   

*-analyzethreshold*    
You can override the numeric threshold for the analyze algorithm here.    
Example:    
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv -datafileanalyze Y -analyzethreshold .90`     

