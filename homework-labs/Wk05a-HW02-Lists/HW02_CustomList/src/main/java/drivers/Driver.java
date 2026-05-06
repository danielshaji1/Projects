package drivers;

import datastructures.*;
import helpers.DriverHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    public static void main(String[] args) {
        /* ── 1. Gather CLI options ─────────────────────────────────────── */
        List<String> contactFiles = new ArrayList<>();
        List<String> datastructs  = new ArrayList<>();          // parallel to contactFiles
        String lastSeenDS = "array";                            // default

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (a.equals("--datastruct") && i + 1 < args.length) {
                lastSeenDS = args[++i].toLowerCase();
            } else if (a.startsWith("--datastruct=")) {
                lastSeenDS = a.substring("--datastruct=".length()).toLowerCase();
            } else if (a.equals("--contacts") && i + 1 < args.length) {
                contactFiles.add(args[++i]);
                datastructs.add(lastSeenDS);
            } else if (a.startsWith("--contacts=")) {
                contactFiles.add(a.substring("--contacts=".length()));
                datastructs.add(lastSeenDS);
            } else {          // tolerate bare file names
                contactFiles.add(a);
                datastructs.add(lastSeenDS);
            }
        }
        if (contactFiles.isEmpty()) {
            contactFiles.add("data/contacts.csv");
            datastructs.add(lastSeenDS);
        }

        /* ── 2. Build each list ────────────────────────────────────────── */
        List<CustomList<Contact>> lists = new ArrayList<>();
        for (int i = 0; i < contactFiles.size(); i++) {
            String path = contactFiles.get(i);
            String ds   = datastructs.get(i);
            lists.add(loadList(ds, path));
        }

        /* ── 3. Merge sequentially, reporting each merger ─────────────── */
        CustomList<Contact> merged = lists.get(0);
        String mergedLabel = contactFiles.get(0) + "\" (" + datastructs.get(0) + ")";
        for (int i = 1; i < lists.size(); i++) {
            System.out.printf(
                "Merging \"%s\" (%s) into list built from \"%s%n",
                contactFiles.get(i), datastructs.get(i), mergedLabel
            );
            merged.mergeLists(lists.get(i));
        }

        /* ── 4. Demo output ────────────────────────────────────────────── */
        System.out.println("Final data-structure of main list: " + datastructs.get(0));
        System.out.println("Total contacts: " + merged.size());
        merged.printList();
    }

    /* Helper that instantiates the chosen implementation and loads a CSV */
    private static CustomList<Contact> loadList(String type, String csvPath) {
        CustomList<Contact> list = switch (type) {
            case "array"  -> new ArrayBasedList();
            case "singly" -> new SinglyLinkedList();
            case "doubly" -> new DoublyLinkedList();
            default -> throw new IllegalArgumentException("Unknown --datastruct: " + type);
        };

        try {
            if (Files.exists(Path.of(csvPath))) {
                DriverHelper.loadContactsCsv(csvPath).forEach(list::addContact);
            } else {
                System.err.println("Warning: file \"" + csvPath + "\" not found, skipping.");
            }
        } catch (Exception e) {
            System.err.println("Failed to load " + csvPath + ": " + e.getMessage());
        }
        return list;
    }

/* Example CLI commands:
// defaults
java -cp target/classes drivers.Driver

java -cp target/classes drivers.Driver \
      --datastruct=singly \
      --contacts=data/contactsA.csv \
      --datastruct=array \
      --contacts=data/contactsB.csv

     */

}
