#!/usr/bin/env ruby

require 'csv.rb'

OUTPUT_FILE = 'parking-records.json'
STREET_DIRECTORY_NAME = 'street'
STREET_SEGMENT_DIRECTORY_NAME = 'street_segment'
PARKING_BAY_DIRECTORY_NAME = 'parking_bay'
PARKING_TIME_DIRECTORY_NAME = 'parking_time'



# DATASET COLUMNS
DEVICE_ID = 'device_id'
ARRIVAL_TIME = 'arrival_time'
DEPARTURE_TIME = 'departure_time'
DURATION = 'duration_seconds'
STREET_MARKER = 'street_marker'
PARKING_SIGN = 'parking_sign'
AREA = 'area'
STREET_ID = 'street_id'
STREET_NAME = 'street_name'
BETWEEN_STREET_1 = 'between_street_1'
BETWEEN_STREET_2 = 'between_street_2'
SIDE_OF_STREET = 'side_of_street'
IN_VIOLATION = 'in_violation'



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
    print "#{count}\n"
    file.write("\t{
\t\t\"#{AREA}\": \"#{row[6]}\",
\t\t\"#{PARKING_SIGN}\": \"#{row[5]}\",
\t\t\"#{SIDE_OF_STREET}\": #{row[11]},
\t\t\"#{STREET_DIRECTORY_NAME}\": {
\t\t\t\"#{STREET_ID}\": #{row[7]},
\t\t\t\"#{STREET_NAME}\": \"#{row[8]}\"
\t\t},
\t\t\"#{STREET_SEGMENT_DIRECTORY_NAME}\": {
\t\t\t\"#{BETWEEN_STREET_1}\": \"#{row[9]}\",
\t\t\t\"#{BETWEEN_STREET_2}\": \"#{row[10]}\"
\t\t},
\t\t\"#{PARKING_BAY_DIRECTORY_NAME}\": {
\t\t\t\"#{DEVICE_ID}\": #{row[0]},
\t\t\t\"#{STREET_MARKER}\": \"#{row[4]}\"
\t\t},
\t\t\"#{PARKING_TIME_DIRECTORY_NAME}\": {
\t\t\t\"#{ARRIVAL_TIME}\": #{row[1]},
\t\t\t\"#{DEPARTURE_TIME}\": #{row[2]},
\t\t\t\"#{DURATION}\": #{row[3]},
\t\t\t\"#{IN_VIOLATION}\": #{row[12] == "True"}
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
#   side_of_street: number,
#   street: {
#     street_id: number
#     street_name: String
#   },
#   street_segment: {
#     between_street_1: String,
#     between_street_2: String
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
