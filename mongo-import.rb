#!/usr/bin/env ruby

DATABASE_NAME = 'myDB'
COLLECTION_NAME = 'parking_records'
FILE_NAME = 'parking-records.json'

startTime = Time.now

`mongoimport --db #{DATABASE_NAME} --collection #{COLLECTION_NAME} --file #{FILE_NAME} --jsonArray`

endTime = Time.now
timeInSeconds = endTime - startTime
puts "Time taken to load data (seconds): #{timeInSeconds}"