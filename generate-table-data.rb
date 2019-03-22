#!/usr/bin/env ruby

require 'csv.rb'

SOURCE_FILE = 'sample.csv'
PARKING_BAY_OUTPUT_FILE = 'parking-bay.csv'
PARKING_EVENT_OUTPUT_FILE = 'parking-event.csv'

File.open(PARKING_BAY_OUTPUT_FILE, 'w') do |file|
  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
    file.write("'#{row[4]}','#{row[6]}','#{row[8]}','#{row[9]}','#{row[10]}'\n")
  end
end

File.open(PARKING_EVENT_OUTPUT_FILE, 'w') do |file|
  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
    file.write("#{row[0]},#{row[1]},#{row[2]},#{row[3]},'#{row[4]}','#{row[5]}',#{row[7]},#{row[11]},#{row[12]}\n")
  end
end

# DATASET COLUMNS
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

