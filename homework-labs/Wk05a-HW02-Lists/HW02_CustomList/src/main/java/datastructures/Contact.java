package datastructures;

import java.util.*;

import utility.TodoLogger;

/**
 * TODO: Skeleton
 *   Fill in the TODOs.
 * Contact uniquely identified by case-insensitive first+last names.
 *
 * Core Java class Contacts providing unique email management and comparison
 * features for contacts. This lays the foundation for further development of the project.
 */
public class Contact implements Comparable<Contact> {
    private final String lastName;
    private final String firstName;
    private final List<String> emails; // list of normalized email addresses; preserve insertion order

    public Contact(String lastName, String firstName) {
        this(lastName, firstName, null);
    }

    public Contact(String lastName, String firstName, List<String> emails) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("Names cannot be null");

        }

        this.lastName = lastName.trim();
        this.firstName = firstName.trim();
        this.emails = new ArrayList<>();
        if (emails != null) {
            for (String e : emails) {
                addEmail(e);
            }
        }
    }

    // TODO(Task 1): Implement addEmail(String)
    //      — add normalized (trimmed+lowercased) emails;
    //      -- no duplicates
    public boolean addEmail(String email) {
    if (email == null) return false;
    String cleanEmail = normalizeEmail(email);
    if (cleanEmail.isEmpty()) return false;

    if (emails.contains(cleanEmail)) return false;
    emails.add(cleanEmail);
    return true;
}


    // TODO(Task 1): Implement deleteEmail(String) — remove by normalized value if present
   public boolean deleteEmail(String email) {
    if (email == null) return false;
    String cleanEmail = normalizeEmail(email);
    return emails.remove(cleanEmail);
}


    // TODO (Task 1): Merge emails from another contact into this one, normalizing and avoiding dups
   public void mergeEmailsFrom(Contact other) {
    if (other == null) return;
    for (String email : other.emails) {
        addEmail(email);
    }
}


    // TODO(Task 1): Implement compareTo by lastName, then firstName (both case-insensitive)
    @Override
public int compareTo(Contact other) {
    int cmp = lastName.compareToIgnoreCase(other.lastName);
    if (cmp != 0) return cmp;
    return firstName.compareToIgnoreCase(other.firstName);
}


    // DO NOT EDIT from here and to the end  ------------------------------------------
    // --------------------------------------------------------------------------------
    public String getLastName()     { return lastName; }
    public String getFirstName()    { return firstName; }
    public List<String> getEmails() { return new ArrayList<>(emails); }
    /**
     * Utility which can be used by addEmail / deleteEmail to enforce the
     *   “trim + lowercase” normalization rule.
     * You may call this method instead of repeating the logic inline.
     */
    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    // Uniqueness based on case-insensitive first+last
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact c)) return false;
        return firstName.equalsIgnoreCase(c.firstName) && lastName.equalsIgnoreCase(c.lastName);
    }
    /** Name used for width calculation. */
    public String displayName() {
        return "%s, %s".formatted(lastName, firstName);
    }
    @Override
    public String toString() {
        final int NAME_COL = 22; // pick a width that fits your typical names
        String fmt = "%-" + NAME_COL + "s  [%s]";
        return fmt.formatted(displayName(), String.join(", ", emails));
    }

}