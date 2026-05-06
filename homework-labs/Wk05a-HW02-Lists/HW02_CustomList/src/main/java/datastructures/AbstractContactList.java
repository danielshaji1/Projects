
/**
 * Traversal-counter rule
 * ----------------------
 *  TODO: Every concrete list *must* override
 *     • contains(...)
 *     • getEmailAddressesForContact(...)
 *     • deleteEmailFromContact(...)
 *
 * These fallback versions exist so incomplete code still runs,
 * but they copy the whole list and leave traversalCount at 0.
 * Override them in every concrete list and add a bump  on each real
 * index / link move; otherwise the autograder will mark the
 * traversal-count tests as failed.
 *
 */

package datastructures;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractContactList implements CustomList<Contact> {
    protected int traversalCount = 0;

    protected void bump() { traversalCount++; }

    protected static boolean sameName(Contact a, Contact b) {
        return a.getLastName().equalsIgnoreCase(b.getLastName())
                && a.getFirstName().equalsIgnoreCase(b.getFirstName());
    }

    protected static boolean sameName(Contact a, String last, String first) {
        return a.getLastName().equalsIgnoreCase(last)
                && a.getFirstName().equalsIgnoreCase(first);
    }

    @Override
    public int getTraversalCount() {
        int t = traversalCount;
        traversalCount = 0;
        return t;
    }

    @Override
    public void resetTraversalCount() {
        traversalCount = 0;
    }

    @Override
    public boolean contains(Contact data) {
        for (Contact c : getAllContacts()) {
            if (c.equals(data)) return true;
        }
        return false;
    }

    // Same for the two e-mail helpers: they should not double-count.
    @Override
    public List<String> getEmailAddressesForContact(String lastName, String firstName) {
        for (Contact c : getAllContacts()) {
            if (sameName(c, lastName, firstName)) {
                return new ArrayList<>(c.getEmails());
            }
        }
        return List.of();
    }

    @Override
    public boolean deleteEmailFromContact(String lastName, String firstName, String email) {
        for (Contact c : getAllContacts()) {
            if (sameName(c, lastName, firstName)) {
                return c.deleteEmail(email);
            }
        }
        return false;
    }
}