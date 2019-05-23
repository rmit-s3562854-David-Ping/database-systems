#!/usr/bin/env ruby

require 'csv.rb'

OUTPUT_FILE = 'parking-records.json'
STREET_DIRECTORY_NAME = 'street'
STREET_SEGMENT_DIRECTORY_NAME = 'street_segment'
PARKING_BAY_DIRECTORY_NAME = 'parking_bay'
PARKING_TIME_DIRECTORY_NAME = 'parking_time'



# DATASET COLUMNS
COL1_NAME = 'device_id'
COL2_NAME = 'arrival_time'
COL3_NAME = 'departure_time'
COL4_NAME = 'duration_seconds'
COL5_NAME = 'street_marker'
COL6_NAME = 'parking_sign'
COL7_NAME = 'area'
COL8_NAME = 'street_id'
COL9_NAME = 'street_name'
COL10_NAME = 'between_street_1'
COL11_NAME = 'between_street_2'
COL12_NAME = 'side_of_street'
COL13_NAME = 'in_violation'



if ARGV.length != 1
  puts "Please enter 1 argument, the path of the csv dataset file"
  exit
end

source_file = ARGV[0]

if !File.file?(source_file) || !File.exist?(source_file)
  puts "Please enter path to a valid file"
  exit
end


source = File.open(source_file, 'r')
file_size = source.readlines.size
count = 0

File.open(OUTPUT_FILE, 'w') do |file|
  file.write("[\n")
  CSV.foreach(source_file, {:headers => true}) do |row|
    file.write("\t{
\t\t\"#{COL7_NAME}\": \"#{row[6]}\",
\t\t\"#{COL6_NAME}\": \"#{row[5]}\",
\t\t\"#{STREET_DIRECTORY_NAME}\": {
\t\t\t\"#{COL8_NAME}\": \"#{row[7]}\",
\t\t\t\"#{COL9_NAME}\": \"#{row[8]}\"
\t\t},
\t\t\"#{STREET_SEGMENT_DIRECTORY_NAME}\": {
\t\t\t\"#{COL10_NAME}\": \"#{row[9]}\",
\t\t\t\"#{COL11_NAME}\": \"#{row[10]}\",
\t\t\t\"#{COL12_NAME}\": \"#{row[11]}\"
\t\t},
\t\t\"#{PARKING_BAY_DIRECTORY_NAME}\": {
\t\t\t\"#{COL1_NAME}\": \"#{row[0]}\",
\t\t\t\"#{COL5_NAME}\": \"#{row[4]}\"
\t\t},
\t\t\"#{PARKING_TIME_DIRECTORY_NAME}\": {
\t\t\t\"#{COL2_NAME}\": \"#{row[1]}\",
\t\t\t\"#{COL3_NAME}\": \"#{row[2]}\",
\t\t\t\"#{COL4_NAME}\": \"#{row[3]}\",
\t\t\t\"#{COL13_NAME}\": \"#{row[12]}\"
\t\t}
\t}")
    count += 1
    unless count >= file_size - 1
      file.write(",\n")
    end
  end
  file.write("\n]\n")
end

# JSON FORMATTING
# {
#   area: String,
#   sign: String,
#   street: {
#     id: number
#     name: String
#   },
#   street_segment: {
#     between_street_1: String,
#     between_street_2: String,
#     side_of_street: number,
#   },
#   parking_bay: {
#     device_id: number,
#     street_marker: String
#   },
#   parking_time: {
#     arrival_time: number,
#     departure_time: number,
#     duration: number,
#     in_violation: boolean,
#   }
# }
