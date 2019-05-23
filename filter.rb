#!/usr/bin/env ruby

# NOTE: this file is used for the heap file implementation, the dates are stored differently for Derby

# This file is used to filter the data-set file provided, it will
# Remove the first line with the column headings
# Remove duplicate entries
# Remove entries with an invalid DurationSeconds (invalid negative values are present)
# Convert the ArrivalTime and DepartureTime to a long/big int

require 'csv.rb'
require 'time.rb'

OUTPUT_FILE = 'sample-filtered.csv'

# DeviceID and ArrivalTime together form the composite key so these must be unique
device_id_arrival_time = {}

if ARGV.length != 1
  puts "Please enter 1 argument, the path of the csv data-set file which you wish to filter"
  exit
end
source_file = ARGV[0]

if !File.file?(source_file) || !File.exist?(source_file)
  puts "Please enter path to a valid file"
  exit
end

File.open(OUTPUT_FILE, 'w') do |file|
  # :headers true will remove the first line
  CSV.foreach(source_file, {:headers => true}) do |row|
    # Concatenate the device id and arrival time and add to the hash, if it already exists then this row will be skipped
    if row[0] != nil && row[1] != nil && row[3] != nil
      # The 4th column contains the DurationSeconds, we must filter the rows with negative values
      num = row[3].to_i
      if num < 0 || num > 2000000000
        next
      end

      # Convert the ArrivalTime and DepartureTime to a long/big int
      # Time.strptime(row[x], '%m/%d/%Y %T %p')
      arrival_time = Time.strptime(row[1], '%m/%d/%Y %T %p').to_i
      key = ("#{row[0]}#{arrival_time}").to_i
      unless device_id_arrival_time.has_key?(key)
        departure_time = Time.strptime(row[2], '%m/%d/%Y %T %p').to_i

        # Write the row into the output csv
        file.write("#{row[0]},#{arrival_time},#{departure_time},#{row[3]},#{row[4]},#{row[5]},#{row[6]},#{row[7]},#{row[8]},#{row[9]},#{row[10]},#{row[11]},#{row[12]}\n")
        # add the unique identifier/primary key to the map
        device_id_arrival_time[key] = true
      end
    end
  end
end
