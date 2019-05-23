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

OUTPUT_DIR = 'tables/'
AREA_OUTPUT_FILE = OUTPUT_DIR + 'area.csv'
STREET_OUTPUT_FILE = OUTPUT_DIR + 'street.csv'
SEGMENT_OUTPUT_FILE = OUTPUT_DIR + 'segment.csv'
STREET_SIDE_SECTION_OUTPUT_FILE = OUTPUT_DIR + 'street-side-section.csv'
PARKING_SIGN_OUTPUT_FILE = OUTPUT_DIR + 'parking-sign.csv'
PARKING_BAY_OUTPUT_FILE = OUTPUT_DIR + 'parking-bay.csv'
PARKING_EVENT_OUTPUT_FILE = OUTPUT_DIR + 'parking-event.csv'


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
      File.open(STREET_SIDE_SECTION_OUTPUT_FILE, 'w') do |street_side_section_file|
        File.open(PARKING_SIGN_OUTPUT_FILE, 'w') do |parking_sign_file|
          File.open(PARKING_BAY_OUTPUT_FILE, 'w') do |parking_bay_file|
            File.open(PARKING_EVENT_OUTPUT_FILE, 'w') do |parking_event_file|
              area_names = {}
              street_ids = {}
              segment_keys = {}
              side_sections = {}
              parking_signs = {}
              street_markers = {}
              street_maker_arrival_times = {}

              CSV.foreach(source_file, {:headers => true}) do |row|

                # Area
                area_name = row[AREA_COL]
                if area_name != nil && !area_names.has_key?(area_name)
                  # AREA_ID | AREA
                  area_file.write("#{area_names.length},#{area_name}\n")
                  area_names[area_name] = area_names.length
                end

                # Street
                area_id = nil
                if area_names.has_key?(area_name)
                  area_id = area_names[area_name]
                end
                street_id = row[STREET_ID_COL]
                if street_id != nil && !street_ids.has_key?(street_id)
                  # STREET_ID | STREET_NAME | AREA_ID
                  street_file.write("#{street_id},#{row[STREET_NAME_COL]},#{area_id}\n")
                  street_ids[street_id] = true
                end

                # Street segment
                # Create 2 keys just in case the same segment comes in but back to front
                between_street_1 = row[B_STREET_1_COL]
                between_street_2 = row[B_STREET_2_COL]
                segment_composite_key = "#{between_street_1}-#{between_street_2}"
                segment_composite_key2 = "#{between_street_2}-#{between_street_1}"
                if between_street_1 != nil && between_street_2 != nil && !segment_keys.has_key?(segment_composite_key) && !segment_keys.has_key?(segment_composite_key2)
                  length = segment_keys.length/2
                  segment_file.write("#{length},#{between_street_1},#{between_street_2}\n")
                  segment_keys[segment_composite_key] = length
                  if segment_composite_key == segment_composite_key2
                    segment_keys[segment_composite_key2 + "-2"] = length
                  else
                    segment_keys[segment_composite_key2] = length
                  end
                end

                # Street Side Section
                section_id = nil
                if segment_keys.has_key?(segment_composite_key)
                  section_id = segment_keys[segment_composite_key]
                else
                  if segment_keys.has_key?(segment_composite_key2)
                       section_id = segment_keys[segment_composite_key2]
                  end
                end
                side_of_street = row[SIDE_OF_STREET_COL]
                side_section = "#{street_id}-#{side_of_street}"
                if street_id != nil && side_of_street != nil && !side_sections.has_key?(side_section)
                  street_side_section_file.write("#{side_sections.length},#{street_id},#{side_of_street},#{section_id}\n")
                  side_sections[side_section] = side_sections.length
                end

                # Parking Sign
                parking_sign_details = row[PARKING_SIGN_COL]

                if parking_sign_details != nil && !parking_signs.has_key?(parking_sign_details)
                  parking_sign_file.write("#{parking_signs.length},#{parking_sign_details}\n")
                  parking_signs[parking_sign_details] = parking_signs.length
                end

                # Parking Bay
                sign_id = nil
                if parking_signs.has_key?(parking_sign_details)
                  sign_id = parking_signs[parking_sign_details]
                end

                side_segment_id = nil
                if side_sections.has_key?(side_section)
                  side_segment_id = side_sections[side_section]
                end

                street_marker = row[STREET_MARKER_COL]
                if street_marker != nil && !street_markers.has_key?(street_marker)
                  parking_bay_file.write("#{street_marker},#{row[DEVICE_ID_COL]},#{side_segment_id},#{sign_id}\n")
                  street_markers[street_marker] = true
                end

                # Parking Time Event
                arrival_time = row[ARRIVAL_TIME_COL]
                sa_id = "#{street_marker}-#{arrival_time}"
                if street_marker != nil && arrival_time != nil && !street_maker_arrival_times.has_key?(sa_id)
                  # Concatenate the street marker and arrival time and add to the hash, if it already exists then ignore this row
                  parking_event_file.write("#{arrival_time},#{street_marker},#{row[DEPARTURE_TIME_COL]},#{row[DURATION_SECONDS_COL]},#{row[IN_VIOLATION_COL]}\n")
                  street_maker_arrival_times[sa_id] = true
                end
              end
            end
          end
        end
      end
    end
  end
end




