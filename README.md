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
Steps:  
This example was validated on a mac and AMI Linux terminal  

1. sudo su  
2. use git to clone this project to a dir of your choosing.  
`git clone https://username@github.com/hms-dbmi/ETLToolSuite-MappingGenerator`  
3. cd to root directory:  
`cd ETLToolSuite-MappingGenerator`   
4. execute following code block:  
`java -jar MappingGenerator.jar -destinationmappingfile ./example/GSE13168_Mapping.csv -datafile ./example/Asthma_Misior_GSE13168.txt -datafiledelimiter TAB -datafileanalyze Y`  
5. cd to example dir  
`cd example`  
6. list the directory's contents.  
`ls -la`  
7. You should see the newly generated mapping file GSE13168_Mapping.csv.  Using this and the associated data filee you can now process the data through the [data generator tool](https://github.com/hms-dbmi/ETLToolSuite-EntityGenerator).  

***
**Mapping Generators Flags**    
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
`java -jar MappingGenerator.jar -datafile ./example/GSE13168_Mapping.csv-datafileanalyze Y -analyzethreshold .90`     

