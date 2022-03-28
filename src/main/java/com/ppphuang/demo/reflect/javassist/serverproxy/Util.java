package com.ppphuang.demo.reflect.javassist.serverproxy;

public class Util {

    public static String convertToString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static int convertToint(Object obj) {
        return new Integer(obj.toString()).intValue();
    }

    public static Integer convertToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Integer(obj.toString());
    }

    public static long convertTolong(Object obj) {
        return new Long(obj.toString()).longValue();
    }

    public static Long convertToLong(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Long(obj.toString());
    }

    public static short convertToshort(Object obj) {
        return new Short(obj.toString()).shortValue();
    }

    public static Short convertToShort(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Short(obj.toString());
    }

    public static float convertTofloat(Object obj) {
        return new Float(obj.toString()).floatValue();
    }

    public static Float convertToFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Float(obj.toString());
    }

    public static boolean convertToboolean(Object obj) {
        return new Boolean(obj.toString()).booleanValue();
    }

    public static Boolean convertToBoolean(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Boolean(obj.toString());
    }

    public static double convertTodouble(Object obj) {
        return new Double(obj.toString()).doubleValue();
    }

    public static Double convertToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Double(obj.toString());
    }

    public static byte convertTobyte(Object obj) {
        return new Byte(obj.toString()).byteValue();
    }

    public static Byte convertToByte(Object obj) {
        if (obj == null) {
            return null;
        }
        return new Byte(obj.toString());
    }

    public static char convertTochar(Object obj) {
        String str = obj.toString();
        if (str.length() > 1) {
            str = str.replaceFirst("\"", "");
        }
        if (!str.equals(null) && !str.equals("")) {
            return str.charAt(0);
        }
        return '\0';
    }

    public static Character convertToCharacter(Object obj) {
        if (obj == null) {
            return null;
        }
        String str = obj.toString();
        if (str.length() > 1) {
            str = str.replaceFirst("\"", "");
        }
        if (!str.equals(null) && !str.equals("")) {
            return new Character(str.charAt(0));
        }
        return new Character('\0');
    }

    public static Object convertToT(Object obj, Class<?> clazz) throws Exception {
        return obj;
    }

    public static Object convertToT(Object obj, Class<?> containClass, Class<?> itemClass) throws Exception {
        return obj;
    }

    public static Object convertToT(Object obj, String clazz) throws Exception {
        return obj;
    }
}
