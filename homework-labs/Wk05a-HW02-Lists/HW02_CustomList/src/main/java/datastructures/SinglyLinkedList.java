package datastructures;

import utility.TodoLogger;

import java.util.ArrayList;
import java.util.List;

/**
 *  SKELETON — Unsorted singly linked list.
 *
 * TODO(Task 3): Implement the TODO stubs
 *      Note: Fallback implementations run but never bump(),
 *      so traversalCount stays 0.
 *
 *      TODO: Override BOTH e-mail helpers in every concrete list for full credit:
 *          *   getEmailAddressesForContact(...)
 *          *   deleteEmailFromContact(...)
 *
 */
public class SinglyLinkedList extends AbstractContactList {

    private static class Node {
        Contact data;
        Node next;
        Node(Contact d) { this.data = d; }
    }

    private Node head;
    private int size;

    // TODO(Task 3): addContact(Contact) — merge duplicates by name; otherwise insert (any position OK)
    @Override
    public void addContact(Contact contact) {
        Node current = head;
        while (current != null) {
            bump();
            if (current.data.equals(contact)) {
                for (String email : contact.getEmails()) {
                    bump();
                    if (!current.data.getEmails().contains(email)) {
                        current.data.getEmails().add(email);
                    }
                }
                return;
            }
            current = current.next;
        }

        Node newNode = new Node(contact);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // TODO(Task 3): deleteContact(lastName, firstName)
    @Override
    public boolean deleteContact(String lastName, String firstName) {
        Node current = head;
        Node prev = null;

        while (current != null) {
            bump(); // traversal count

            if (current.data.getLastName().trim().equalsIgnoreCase(lastName.trim()) &&
                    current.data.getFirstName().trim().equalsIgnoreCase(firstName.trim())) {

                if (prev == null) {
                    // deleting head

                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }

            prev = current;
            current = current.next;
        }
        return false; // not found
    }


    // TODO(Task 3): mergeLists(otherList) — iterate other and addContact
    @Override
    public void mergeLists(CustomList<Contact> otherList) {
        for (Contact c : otherList.getAllContacts()){
            bump();
            addContact(c);
        }
    }

    // TODO(Task 3): getAllContacts — traverse and collect
    @Override
    public List<Contact> getAllContacts() {
        List<Contact> result = new ArrayList<>(size);
        Node current = head;
        while (current != null) {
            bump();
            result.add(current.data);
            current = current.next;
        }
        return result;
    }


    @Override
    public int size() { return size; }
}
