#!/usr/bin/env ruby

require 'csv.rb'

SOURCE_FILE = 'sample.csv'
OUTPUT_FILE = 'init-database.sql'
CREATE_TABLES_FILE = 'create-tables.sql'

PARKING_BAY_TABLE = 'parking_bay'
PARKING_EVENT_TABLE = 'parking_event'

# COL1_NAME = 'device_id'
# COL2_NAME = 'arrival_time'
# COL3_NAME = 'departure_time'
# COL4_NAME = 'duration_seconds'
# COL5_NAME = 'street_marker'
# COL6_NAME = 'parking_sign'
# COL7_NAME = 'area'
# COL8_NAME = 'street_id'
# COL9_NAME = 'street_name'
# COL10_NAME = 'between_street_1'
# COL11_NAME = 'between_street_2'
# COL12_NAME = 'side_of_street'
# COL13_NAME = 'in_violation'


# Copy the table creation script
IO.copy_stream(CREATE_TABLES_FILE, OUTPUT_FILE)
# Remove all single quotes within the source file
`sed -i "s/'/ /g" #{SOURCE_FILE}`

File.open(OUTPUT_FILE, 'a') do |file|
  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
    file.write("INSERT INTO #{PARKING_BAY_TABLE} values('#{row[4]}', '#{row[6]}', '#{row[8]}', '#{row[9]}', '#{row[10]}')\n")
  end

  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
    file.write("INSERT INTO #{PARKING_EVENT_TABLE} values('#{row[0]}', '#{row[1]}', '#{row[2]}', '#{row[4]}', '#{row[5]}', #{row[3]}, #{row[12]}, #{row[11]}, #{row[0]})\n")
  end

  # Examples
  # -- INSERT INTO parking_bay values('street marker 1', 'area', 'street name', 'street 1', 'street 2');
  # -- INSERT INTO parking_event values('id-1', '08/11/2017 02:19:50 PM', '08/11/2017 07:12:02 PM', 'street marker 1', '2P MTR M-SAT 7:30-20:30', 17532, TRUE, 5, 001);
end


# start = Time.now
# `java -Dij.database="jdbc:derby:myDatabase";create=true org.apache.derby.tools.ij init-database.sql`
# java -Dij.database="jdbc:derby://someserver:1527//myDatabase" org.apache.derby.tools.ij init-database.sql

# `java org.apache.derby.tools.ij`
# `connect 'jdbc:derby:myDatabase;create=true';`
# `run 'init-database.sql';`

# `
# java -Dij.database=jdbc:derby:myDatabase;create=true org.apache.derby.tools.ij init-datbase.sql
# `

# finish = Time.now
# diff = finish - start
# puts diff

# `rm -rf myDatabase`
# `rm derby.log`
