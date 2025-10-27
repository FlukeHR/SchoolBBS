package com.bbs.util;

public class StringUtil {
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }
    
    public static String sanitizeInput(String input) {
        if (isEmpty(input)) {
            return "";
        }
        return input.trim()
                .replaceAll("<script[^>]*>.*?</script>", "")
                .replaceAll("<[^>]+>", "");
    }
    
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}