#!/usr/bin/env ruby

DATABASE_NAME = 'myDB'
COLLECTION_NAME = 'parking_records'
FILE_NAME = 'parking-records.json'

start = Time.now

`mongoimport --db #{DATABASE_NAME} --collection #{COLLECTION_NAME} --file #{FILE_NAME} --jsonArray`

finish = Time.now
diff = finish - start
puts diff