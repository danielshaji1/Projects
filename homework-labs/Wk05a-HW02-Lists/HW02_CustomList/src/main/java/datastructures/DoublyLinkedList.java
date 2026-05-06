package datastructures;

import java.util.ArrayList;
import java.util.List;

import utility.TodoLogger;

/**
 * sorted doubly linked list -backed implementation of a simple contact list.
 *
 * TODO: Implement all TODO stubs &
 *      Note: Fallback implementations run but never bump(),
 *      so traversalCount stays 0.
 *
 *      TODO: Override BOTH e-mail helpers in every concrete list for full credit:
 *          *   getEmailAddressesForContact(...)
 *          *   deleteEmailFromContact(...)
 *
 */
public class DoublyLinkedList extends AbstractContactList {

    private static class Node {
        Contact data;
        Node prev, next;
        Node(Contact newContact) { this.data = newContact; }
    }

    private Node head;
    private Node tail;
    private int size;

    // TODO(Task 4): addContact(Contact) — insert maintaining sorted order; merge duplicate emails
    @Override
    public void addContact(Contact contact) {
        if (head == null) {  // empty list
            head = tail = new Node(contact);
            size++;
            return;
        }

        Node current = head;
        while (current != null) {
            bump();
            int cmp = current.data.compareTo(contact);
            if (cmp == 0) {  // same contact, merge emails
                for (String email : contact.getEmails()) {
                    bump();
                    current.data.addEmail(email);
                }
                return;  // no size++
            } else if (cmp > 0) {  // insert before current
                insertBefore(current, new Node(contact));
                size++;
                return;
            }
            current = current.next;
        }

        // If we reached the end, append
        append(new Node(contact));
        size++;
    }


    // TODO(Task 4): deleteContact(lastName, firstName)
    @Override
    public boolean deleteContact(String lastName, String firstName) {
        Node current = head;
        while (current != null) {
            bump();
            if (current.data.getLastName().equalsIgnoreCase(lastName) &&
                current.data.getFirstName().equalsIgnoreCase(firstName)) {
                // unlink node
                if (current.prev != null) current.prev.next = current.next;
                else head = current.next;

                if (current.next != null) current.next.prev = current.prev;
                else tail = current.prev;

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // TODO(Task 4): mergeLists(CustomList<Contact> otherList) — efficient merge if other is DoublyLinkedList
    @Override
    public void mergeLists(CustomList<Contact> otherList) {
        if (otherList instanceof DoublyLinkedList) {
            DoublyLinkedList other = (DoublyLinkedList) otherList;
            Node current = other.head;
            while (current != null) {
                addContact(current.data);
                current = current.next;
            }
            // clear the other list
            other.head = null;
            other.tail = null;
            other.size = 0;
        } else {
            for (Contact c : otherList.getAllContacts()) {
                addContact(c);
            }
        }
    }



    // TODO(Task 4): getAllContacts — traverse from head to tail and collect
    @Override
    public List<Contact> getAllContacts() {
        List<Contact> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            bump();
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    @Override
    public boolean contains(Contact target) {
        Node current = head;
        while (current != null) {
            bump();
            int cmp = current.data.compareTo(target);
            if (cmp == 0) return true;
            else if (cmp > 0) return false; // sorted, can exit early
            current = current.next;
        }
        return false;
    }

    @Override
    public List<String> getEmailAddressesForContact(String lastName, String firstName) {
        Node current = head;
        while (current != null) {
            bump();
            if (current.data.getLastName().equalsIgnoreCase(lastName) &&
                    current.data.getFirstName().equalsIgnoreCase(firstName)) {
                return new ArrayList<>(current.data.getEmails());
            }
            current = current.next;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteEmailFromContact(String lastName, String firstName, String email) {
        Node current = head;
        while (current != null) {
            bump();
            if (current.data.getLastName().equalsIgnoreCase(lastName) &&
                    current.data.getFirstName().equalsIgnoreCase(firstName)) {
                return current.data.deleteEmail(email);
            }
            current = current.next;
        }
        return false;
    }


    // DO NOT EDIT from here and to the end  ------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Insert the new node n immediately before the given node.
     *
     * @param node the existing node you want to insert before
     * @param newNode    the new node to be inserted
     */
    private void insertBefore(Node node, Node newNode) {
        newNode.prev = node.prev;
        newNode.next = node;
        if (node.prev != null) node.prev.next = newNode; else head = newNode;
        node.prev = newNode;
    }

    /**
     * Append the new node n to the end of the list.
     *
     * @param newNode the new node to append
     */
    private void append(Node newNode) {
        newNode.prev = tail;
        if (tail != null) tail.next = newNode;
        tail = newNode;
        if (head == null) head = newNode;
    }

    @Override
    public int size() { return size; }

}
