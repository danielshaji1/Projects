package datastructures;

import utility.TodoLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Unsorted array-backed implementation of a simple contact list.
 *
 **
 * TODO: Implement all TODO stubs
 * also -
 *  * Override BOTH e-mail helpers in every concrete list for full credit:
 *  *   • getEmailAddressesForContact(...)
 *  *   • deleteEmailFromContact(...)
 *
 */

public class ArrayBasedList extends AbstractContactList {

    private final List<Contact> data = new ArrayList<>();

    // TODO(Task 3): addContact(Contact) — add or merge by case-insensitive name
    @Override
    public void addContact(Contact contact) {
        for (Contact existing : data) {
            bump();
            if (existing.equals(contact)) {
                existing.mergeEmailsFrom(contact); // merge emails from Contact object
                return; // duplicate found → merge and exit
            }
        }
        // If we reach here, no duplicate found → add new
        data.add(contact);
    }

    // TODO(Task 3): deleteContact(lastName, firstName)
    @Override
    public boolean deleteContact(String lastName, String firstName) {
        Iterator<Contact> iterator = data.iterator();
        while (iterator.hasNext()) {
            bump();
            Contact c = iterator.next();
            if (c.getLastName().equalsIgnoreCase(lastName) && c.getFirstName().equalsIgnoreCase(firstName)) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    // TODO(Task 3): mergeLists(otherList) — append / merge all contacts from other
    @Override
    public void mergeLists(CustomList<Contact> otherList) {
        for (Contact c : otherList.getAllContacts()){
            bump();
            addContact(c);
        }
    }

    // TODO(Task 3): getAllContacts — return a copy of internal array list
    @Override
    public List<Contact> getAllContacts() {
        return new ArrayList<>(data);
    }

    @Override
    public List<String> getEmailAddressesForContact(String lastName, String firstName) {
        lastName = lastName.toLowerCase().trim();
        firstName = firstName.toLowerCase().trim();
        for (Contact c : data){
            bump();
            if (c.getLastName().equalsIgnoreCase(lastName) && c.getFirstName().equalsIgnoreCase(firstName)) {
                return new ArrayList<>(c.getEmails());
            }

        }
        return new ArrayList<>(); // empty if not found
    }

    @Override
    public boolean deleteEmailFromContact(String lastName, String firstName, String email) {
        for (Contact c : data){
            bump();
            if  (c.getLastName().equalsIgnoreCase(lastName) && c.getFirstName().equalsIgnoreCase(firstName)) {
                return c.deleteEmail(email);
            }
        }
        return false;
    }

    // -- DO NOT EDIT ----
    @Override
    public int size() {
        return data.size();
    }
}
