create table Area(area_id unsigned smallint primary key, area varchar(20))
create table Street(street_id unsigned smallint primary key, street_name varchar(35), area_id unsigned smallint references Area(area_id))
create table Segment(between_street_1 varchar(35) not null, between_street_2 varchar(35) not null, primary key (between_street_1, between_street_2))
create table StreetSideSegment(street_id unsigned smallint references Street(street_id), side_of_street unsigned tinyint not null, between_street_1 unsigned int references Segment(between_street_1), between_street_2 unsigned int references Segment(between_street_2), primary key (street_id, side_of_street))
create table Sign(sign_id unsigned int primary key, sign_details varchar(50))
create table ParkingBay(street_marker varchar(6) primary key, device_id unsigned int, sign_id unsigned int references Sign(sign_id), side_of_street unsigned tinyint references StreetSegmentSide(side_of_street), street_id unsigned smallint references StreetSegmentSide(street_id))
create table ParkingEvent(arrival_time long not null, street_marker varchar(6) not null references ParkingBay(street_marker), departure_time long, duration unsigned int, in_violation boolean, primary key (street_marker, arrival_time))