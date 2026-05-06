package datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * CustomList interface. Implementations should complete required methods.
 * You may add default methods that can be built from other required methods.
 */
public interface CustomList<T> {

    // Core operations every implementation must provide
    void addContact(Contact contact);           // TODO(Task 3/4)
    boolean deleteContact(String lastName, String firstName); // TODO(Task 3/4)
    List<String> getEmailAddressesForContact(String lastName, String firstName); // TODO(Task 3/4)
    boolean deleteEmailFromContact(String lastName, String firstName, String email); // TODO(Task 3/4)
    void mergeLists(CustomList<Contact> otherList); // TODO(Task 3/4)
    boolean contains(T data); // May be overridden; default available in AbstractContactList
    int size();

    /**
     * Returns the current traversal count and resets it to zero.
     */
    int getTraversalCount();

    /**
     * Resets the traversal counter to zero without returning it.
     */
    void resetTraversalCount();

    /**
     * Return all contacts in this list in the list's natural order. For unsorted lists,
     * this is the insertion/traversal order; for the sorted list, this is sorted order.
     */
    List<Contact> getAllContacts(); // TODO(Task 2/3/4)

    // Minimal default helpers
    default boolean isEmpty() {
        return size() == 0;
    }

    // helper placed inside the default method – keeps interface clean
    static String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) +
                s.substring(1).toLowerCase(Locale.ROOT);
    }

    default void printList() {
        List<Contact> all = new ArrayList<>(getAllContacts());
        // Build display names (capitalized) and measure the widest
        List<String> nameParts = new ArrayList<>(all.size());
        int maxNameLen = 0;
        for (Contact c : all) {
            String name = String.format("%s, %s",
                    cap(c.getLastName()),   // capitalize last name
                    cap(c.getFirstName())); // capitalize first name
            nameParts.add(name);
            maxNameLen = Math.max(maxNameLen, name.length());
        }

        // print without altering contact order
        System.out.println("Contact List:");
        for (int i = 0; i < all.size(); i++) {
            Contact c = all.get(i);
            String paddedName = String.format("    %-"+maxNameLen+"s", nameParts.get(i));
            List<String> emails = c.getEmails();
            Collections.sort(emails);          // sorting emails is harmless; not part of homework
            System.out.println(paddedName + "  " + emails);
        }
    }

    /**
     * Optional helper: default contains via getAllContacts(). Implementations may override.
     */
    @SuppressWarnings("unchecked")
    default boolean containsDefault(T data) {
        if (data == null) return false;
        if (data instanceof Contact cd) {
            for (Contact c : getAllContacts()) {
                if (c.equals(cd)) return true;
            }
            return false;
        }
        for (Contact c : getAllContacts()) {
            if (c.equals(data)) return true;
        }
        return false;
    }

    /**
     * Convenience to get an immutable snapshot of all contacts
     */
    default List<Contact> snapshot() {
        return Collections.unmodifiableList(new ArrayList<>(getAllContacts()));
    }
}