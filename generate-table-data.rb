#!/usr/bin/env ruby

require 'csv.rb'
require 'time.rb'

AREA_OUTPUT_FILE = 'area.csv'
PARKING_SIGN_OUTPUT_FILE = 'parking-sign.csv'
STREET_OUTPUT_FILE = 'street.csv'
STREET_SEGMENT_OUTPUT_FILE = 'street-segment.csv'
PARKING_BAY_OUTPUT_FILE = 'parking-bay.csv'
PARKING_EVENT_OUTPUT_FILE = 'parking-event.csv'





if ARGV.length != 1
  puts "Please enter 1 argument, the path of the csv dataset file"
  exit
end

source_file = ARGV[0]

if !File.file?(source_file) || !File.exist?(source_file)
  puts "Please enter path to a valid file"
  exit
end

File.open(AREA_OUTPUT_FILE, 'w') do |area_file|
  File.open(PARKING_SIGN_OUTPUT_FILE, 'w') do |parking_sign_file|
    File.open(STREET_OUTPUT_FILE, 'w') do |street_file|
      File.open(STREET_SEGMENT_OUTPUT_FILE, 'w') do |street_segment_file|
        File.open(PARKING_BAY_OUTPUT_FILE, 'w') do |parking_bay_file|
          File.open(PARKING_EVENT_OUTPUT_FILE, 'w') do |parking_event_file|
            street_markers = {}
            device_id_arrival_time = {}

            CSV.foreach(source_file, {:headers => true}) do |row|
              if !street_markers.has_key?(row[4]) && row[4] != nil
                parking_bay_file.write("'#{row[4]}','#{row[6]}','#{row[8]}','#{row[9]}','#{row[10]}'\n")
                street_markers[row[4]] = true
              end

              if street_markers.has_key?(row[4])
                # FORMAT THE DATE AND TIME
                # FROM
                # 07/21/2017 11:42:28 PM
                # TO
                # 2017-07-21 23:42:28
                if row[1] != nil
                  row[1] = Time.strptime(row[1], '%m/%d/%Y %T %p').strftime('%Y-%m-%d %T')
                end

                if row[2] != nil
                  row[2] = Time.strptime(row[2], '%m/%d/%Y %T %p').strftime('%Y-%m-%d %T')
                end
                # Concatenate the device id and arrival time and add to the hash, if it already exists then ignore this row
                if row[0] != nil && row[1] != nil && !device_id_arrival_time.has_key?(row[0] + row[1])
                  file.write("#{row[0]},#{row[1]},#{row[2]},#{row[3]},'#{row[4]}','#{row[5]}',#{row[7]},#{row[11]},#{row[12]}\n")
                  device_id_arrival_time[row[0] + row[1]] = true
                end
              end

            end
          end
        end
      end
    end
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
