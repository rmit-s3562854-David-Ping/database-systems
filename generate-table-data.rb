#!/usr/bin/env ruby

require 'csv.rb'
require 'time.rb'

SOURCE_FILE = 'sample.csv'
PARKING_BAY_FILE = 'parking-bay.csv'
PARKING_EVENT_FILE = 'parking-event.csv'

street_markers = {}
device_id_arrival_time = {}

File.open(PARKING_BAY_FILE, 'w') do |file|
  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
    if !street_markers.has_key?(row[4]) && row[4] != nil
      file.write("'#{row[4]}','#{row[6]}','#{row[8]}','#{row[9]}','#{row[10]}'\n")
      street_markers[row[4]] = true
    end
  end
end

File.open(PARKING_EVENT_FILE, 'w') do |file|
  CSV.foreach(SOURCE_FILE, {:headers => true}) do |row|
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
