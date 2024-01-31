package com.company.util;

public class MailUtil {
    public static String mailMessage(String name, String surname, String token) {
        StringBuilder builder = new StringBuilder();
        builder.append("Hello ");
        builder.append(name).append(" ");
        builder.append(surname);
        builder.append(".\n Please verify your account: ");
        builder.append("http://localhost:8080/api/auth/confirm?token=").append(token);
        return builder.toString();
    }
}
