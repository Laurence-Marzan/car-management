package utils;

public class CarValidator
{
    public static boolean isValid(String name, String make, String yearStr) {
        if (name == null || name.isEmpty()) return false;
        if (make == null || make.isEmpty()) return false;
        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1886 || year > 2100) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
