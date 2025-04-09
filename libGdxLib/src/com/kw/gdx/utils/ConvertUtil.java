package com.kw.gdx.utils;


public class ConvertUtil {
    public final static float convertToFloat(Object value, float defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public final static byte convertToByte(Object value, byte defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString()).byteValue();
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).byteValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public final static long convertToLong(Object value, long defaultValue) {
        if (value == null || "".equals(value.toString().trim())) return defaultValue;
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).longValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }


    public final static boolean convertToBoolean(Object value, boolean defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Boolean.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
