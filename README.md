# Database Systems assignment 2

Group members:
Luke Mason s3630120
David Ping s3562854

## B+ Tree

First filter the data-set using the script provided

``./filter.rb sample.csv``

If you are using bulk loading to load the data into the B+ Tree then the file needs to sorted with the following gnu unix command

``sort -T /temp-dir/ --parallel=4 --output sample-filtered-sorted.csv -k 1,1 -k 2,2 sample-filtered.csv``

Run dbload to create the heap file

``javac dbload.java``

``java dbload -p <pagesize> <datafile>``

Run LoadTree with the following command line arguments

``java LoadTree <load || bulk-load> <pagesize> <b+-tree-order (max num pointers/children of a node)>``

Remember to sort the filtered data before creating the heap file if you wish to use bulk load.

After loading the tree you may search for a record in the tree using the DA_NAME key of the record eg 212131501126537 (deviceId: 21213, arrivalDate: 1501126537).



## Loading data into Apache Derby

Run the "generate-table-data.rb" script at the root of the project, this will generate 2 csv files "parking-bay.csv" and "parking-event.csv".

To run the script pass in the name of the csv data file as an argument: 

``./generate-table-data.rb example.csv``

Compile and run the java app Derby

``mvn clean install``

``java -jar target/database-systems-1.0-SNAPSHOT.jar``

## Loading data into MongoDB

To load data into a MongoDB database simply run the "generate-json.rb" script while passing in the name of the csv data file as an argument.

``./generate-json.rb example.csv``

This will generate a JSON file "parking-records.json" which we will then load into MongoDB using another script "mongo-import.rb"

``./mongo-import.rb``

Ensure a connection is open

``mongod``
