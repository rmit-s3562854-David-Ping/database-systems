#!/usr/bin/env ruby

require 'csv.rb'
require 'time.rb'

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

DEVICE_ID_COL = 0
ARRIVAL_TIME_COL = 1
DEPARTURE_TIME_COL = 2
DURATION_SECONDS_COL = 3
STREET_MARKER_COL = 4
PARKING_SIGN_COL = 5
AREA_COL = 6
STREET_ID_COL = 7
STREET_NAME_COL = 8
B_STREET_1_COL = 9
B_STREET_2_COL = 10
SIDE_OF_STREET_COL = 11
IN_VIOLATION_COL = 12

AREA_OUTPUT_FILE = 'area.csv'
STREET_OUTPUT_FILE = 'street.csv'
SEGMENT_OUTPUT_FILE = 'segment.csv'
STREET_SIDE_SEGMENT_OUTPUT_FILE = 'street-side-segment.csv'
PARKING_SIGN_OUTPUT_FILE = 'parking-sign.csv'
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
  File.open(STREET_OUTPUT_FILE, 'w') do |street_file|
    File.open(SEGMENT_OUTPUT_FILE, 'w') do |segment_file|
      File.open(STREET_SIDE_SEGMENT_OUTPUT_FILE, 'w') do |street_side_segment_file|
        File.open(PARKING_SIGN_OUTPUT_FILE, 'w') do |parking_sign_file|
          File.open(PARKING_BAY_OUTPUT_FILE, 'w') do |parking_bay_file|
            File.open(PARKING_EVENT_OUTPUT_FILE, 'w') do |parking_event_file|
              area_names = {}
              street_ids = {}
              segment_keys = {}
              side_segments = {}
              parking_sign_ids = {}
              street_markers = {}
              device_id_arrival_time = {}

              CSV.foreach(source_file, {:headers => true}) do |row|

                # Area
                area_name = row[AREA_COL]
                if !area_names.has_key?(area_name) && area_name != nil
                  # AREA_ID | AREA
                  area_file.write("#{area_names.length},'#{area_name}'\n")
                  area_names[area_name] = true
                end

                # Street
                street_id = row[STREET_ID_COL]
                if !street_ids.has_key?(street_id) && street_id != nil
                  # STREET_ID | STREET_NAME | AREA_ID
                  street_file.write("#{street_id}, '#{row[STREET_NAME_COL]}', '#{area_name}'")
                  street_ids[street_id] = true
                end

                # Street segment
                # Create 2 keys just incase the same segment comes in but back to front
                between_street_1 = row[B_STREET_1_COL]
                between_street_2 = row[B_STREET_2_COL]
                segment_composite_key = "#{between_street_1}-#{between_street_2}"
                segment_composite_key2 = "#{between_street_2}-#{between_street_1}"
                if !segment_keys.has_key?(segment_composite_key) && !segment_keys.has_key?(segment_composite_key2) && between_street_1 != nil && between_street_2 != nil
                  segment_file.write("'#{between_street_1}', '#{between_street_2}'")
                  segment_keys[segment_composite_key] = true
                  segment_keys[segment_composite_key2] = true
                end

                # Street Side Segment
                side_of_street = row[SIDE_OF_STREET_COL]
                side_segment = "#{street_id}-#{side_of_street}"
                if !side_segments.has_key?(side_segment) && street_id != nil && side_of_street != nil
                  street_side_segment_file.write("#{street_id}, '#{side_of_street}', '#{between_street_1}', '#{between_street_2}'")
                end

                # Parking Sign
                parking_sign = row[PARKING_SIGN_COL]
                if !parking_sign_ids.has_key?()

                # Parking Bay

                # Parking Event




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
                    parking_event_file.write("#{row[0]},#{row[1]},#{row[2]},#{row[3]},'#{row[4]}','#{row[5]}',#{row[7]},#{row[11]},#{row[12]}\n")
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
end




