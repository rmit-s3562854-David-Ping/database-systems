#!/usr/bin/env ruby

require 'csv.rb'

PARKING_BAY_FILE = 'parking-bay.csv'
PARKING_EVENT_FILE = 'parking-event.csv'
# Hash to maintain all the available street markers, if parking events has some that do not match then that entry breaks the Foreign key constraint and needs to be deleted
# This is expected since we are using a reduced portion of the original data set
street_markers = {}

device_id_arrival_time = {}

# Remove duplicates rows from parking bay table
CSV.open('output.csv', 'w') do |csv|
  CSV.read("#{PARKING_BAY_FILE}").uniq.each do |row|
    csv << row
    street_markers[row[0]] = true
  end
end
`yes | cp -rf output.csv #{PARKING_BAY_FILE}`
`rm output.csv`

# Remove rows in the parking events table that do not have a corresponding foreign key street marker from parking bay
CSV.open('output.csv', 'w') do |csv|
  CSV.read("#{PARKING_EVENT_FILE}").uniq.each do |row|
    if street_markers.has_key?(row[4])
      unless device_id_arrival_time.has_key?(row[0] + row[1])
        csv << row
        device_id_arrival_time[row[0] + row[1]] = true
      end
    end
  end
end
`yes | cp -rf output.csv #{PARKING_EVENT_FILE}`
`rm output.csv`
