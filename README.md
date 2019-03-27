# Database Systems assignment 1

The Java application takes in the following command line arguments

`1`: Load the generated csv files into Apache DB

## Loading data into Apache Derby

Run the "generate-table-data.rb" script at the root of the project, this will generate 2 csv files "parking-bay.csv" and "parking-event.csv".

To run the script pass in the name of the csv data file as an argument: 

``./generate-table-data.rb example.csv``

Compile and run the java app Derby

## Loading data into MongoDB

To load data into a MongoDB database simply run the "generate-json.rb" script while passing in the name of the csv data file as an argument.

``./generate-json.rb example.csv``

This will generate a JSON file "parking-records.json" which we will then load into MongoDB using another script "mongo-import.rb"

``./mongo-import.rb``

Ensure a connection is open