#!/usr/bin/env ruby

require 'csv.rb'
require 'time.rb'

PARKING_BAY_FILE = 'parking-bay.csv'
PARKING_EVENT_FILE = 'parking-event.csv'

street_markers = {}
device_id_arrival_time = {}

# Remove duplicates rows from parking bay table
CSV.open('output.csv', 'w') do |csv|
  CSV.read("#{PARKING_BAY_FILE}").uniq.each do |row|
    unless street_markers.has_key?(row[0])
      csv << row
      street_markers[row[0]] = true
    end
  end
end
`yes | cp -rf output.csv #{PARKING_BAY_FILE}`
`rm output.csv`

# Remove rows in the parking events table that do not have a corresponding foreign key street marker from parking bay
CSV.open('output.csv', 'w') do |csv|
  CSV.read("#{PARKING_EVENT_FILE}").uniq.each do |row|
    if street_markers.has_key?(row[4])
      # FORMAT THE DATE AND TIME
      # FROM
      # 07/21/2017 11:42:28 PM
      # TO
      # 2017-07-21 23:42:28
      row[1] = Time.strptime(row[1], '%m/%d/%Y %T %p').strftime('%Y-%m-%d %T')
      row[2] = Time.strptime(row[2], '%m/%d/%Y %T %p').strftime('%Y-%m-%d %T')

      # Concatenate the device id and arrival time and add to the hash, if ti already exists then ignore this row
      unless device_id_arrival_time.has_key?(row[0] + row[1])
        csv << row
        device_id_arrival_time[row[0] + row[1]] = true
      end
    end
  end
end
`yes | cp -rf output.csv #{PARKING_EVENT_FILE}`
`rm output.csv`
