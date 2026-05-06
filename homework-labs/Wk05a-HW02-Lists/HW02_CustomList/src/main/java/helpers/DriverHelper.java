package helpers;

// DO NOT EDIT: Helper utilities for the Driver.

import datastructures.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DriverHelper {
    private DriverHelper() {}

    public static List<Contact> loadContactsCsv(String filePath) throws IOException {
        List<Contact> list = new ArrayList<>();
        Path p = Path.of(filePath);
        if (!Files.exists(p)) return list;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = splitCsv(line);
                if (parts.length < 2) continue;
                // Skip header if detected
                if (first && looksLikeHeader(parts)) { first = false; continue; }
                first = false;
                String lastName = parts[0].trim();
                String firstName = parts[1].trim();
                List<String> emails = new ArrayList<>();
                if (parts.length >= 3) {
                    emails.addAll(parseEmails(parts[2]));
                }
                list.add(new Contact(lastName, firstName, emails));
            }
        }
        return list;
    }

    private static String[] splitCsv(String line) {
        // Simple split: not handling quotes/escapes to keep helper minimal
        return line.split(",");
    }

    private static boolean looksLikeHeader(String[] parts) {
        String a = parts[0].toLowerCase();
        String b = parts[1].toLowerCase();
        return (a.contains("last") && b.contains("first")) || (a.contains("lastName".toLowerCase()));
    }

    private static List<String> parseEmails(String field) {
        if (field == null || field.isBlank()) return List.of();
        String[] tokens = field.split(";");
        List<String> out = new ArrayList<>();
        for (String t : tokens) {
            String s = t.trim();
            if (!s.isEmpty()) out.add(s);
        }
        return out;
    }
}
