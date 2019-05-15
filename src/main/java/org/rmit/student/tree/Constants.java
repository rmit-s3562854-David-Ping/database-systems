package org.rmit.student.tree;

public class Constants {

    public static final int BOOLEAN_BYTES = 1;
    public static final int SHORT_STRING_BYTES = 10;
    public static final int MEDIUM_STRING_BYTES = 30;
    public static final int LONG_STRING_BYTES = 50;

    public static final int DA_NAME_SIZE = MEDIUM_STRING_BYTES;
    public static final int DEVICE_ID_SIZE = Integer.BYTES;
    public static final int ARRIVAL_TIME_SIZE = Long.BYTES;
    public static final int DEPARTURE_TIME_SIZE = Long.BYTES;
    public static final int DURATION_SECONDS_SIZE = Long.BYTES;
    public static final int STREET_MARKER_SIZE = SHORT_STRING_BYTES;
    public static final int PARKING_SIGN_SIZE = LONG_STRING_BYTES;
    public static final int AREA_SIZE = MEDIUM_STRING_BYTES;
    public static final int STREET_ID_SIZE = Integer.BYTES;
    public static final int STREET_NAME_SIZE = MEDIUM_STRING_BYTES;
    public static final int BETWEEN_STREET_1_SIZE = MEDIUM_STRING_BYTES;
    public static final int BETWEEN_STREET_2_SIZE = MEDIUM_STRING_BYTES;
    public static final int SIDE_OF_STREET_SIZE = Integer.BYTES;
    public static final int IN_VIOLATION_SIZE = BOOLEAN_BYTES;

    public static final int RECORD_SIZE = DA_NAME_SIZE + DEVICE_ID_SIZE + ARRIVAL_TIME_SIZE + DEPARTURE_TIME_SIZE + DURATION_SECONDS_SIZE +
            STREET_MARKER_SIZE + PARKING_SIGN_SIZE + AREA_SIZE + STREET_ID_SIZE + STREET_NAME_SIZE +
            BETWEEN_STREET_1_SIZE + BETWEEN_STREET_2_SIZE + SIDE_OF_STREET_SIZE + IN_VIOLATION_SIZE;
}
