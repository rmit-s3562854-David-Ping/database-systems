###### Database Systems assignment 2
# Loading data into the DBs

## Loading data into Apache Derby

1. First filter the csv file: \
``./filter.rb example.csv``\
The filter removed duplicates, gets rid of bad negative duration data and also changes the times and dates into a long.
This will generate a filtered sample of the csv file.

2. Run the "generate-table-data.rb" script at the root of the project, this will generate 2 csv files "parking-bay.csv" and "parking-event.csv".
To run the script pass in the name of the csv data file as an argument: \
``./generate-table-data.rb filtered-example.csv``

3. Compile and run the java app Derby\
``mvn clean install``

``java -jar target/database-systems-1.0-SNAPSHOT.jar``

## Loading data into MongoDB
1. First filter the csv file: \
``./filter.rb example.csv``\
The filter removed duplicates, gets rid of bad negative duration data and also changes the times and dates into a long.
This will generate a filtered sample of the csv file.

2. To load data into a MongoDB database simply run the "generate-json.rb" script while passing in the name of the csv data file as an argument.
``./generate-json.rb example.csv``

3. This will generate a JSON file "parking-records.json" which we will then load into MongoDB using another script "mongo-import.rb"
``./mongo-import.rb``

Ensure a connection is open

``mongod``

## B+ Tree

