# Database Systems assignment 1

The Java application takes in the following command line arguments

`1`: Load the generated csv files into Apache DB

## Loading data into Apache Derby

Run the "generate-table-data.rb" script at the root of the project, this will generate 2 csv files "parking-bay.csv" and "parking-event.csv".

To run the script pass in the name of the csv data file as an argument: 

``./generate-table-data.rb example.csv``

Run the java application and pass in the command line argument '1'

## Loading data into MongoDB

To load data into a MongoDB database simply run the "generate-json.rb" script while passing in the name of the csv data file as an argument.

``./generate-json.rb example.csv``

This will generate a JSON file "parking-records.json" which we will then load into MongoDB using another script "mongo-import.rb"

``./mongo-import.rb``

## Create an executable jar file

To create an executable jar run the command

``mvn clean package shade:shade``