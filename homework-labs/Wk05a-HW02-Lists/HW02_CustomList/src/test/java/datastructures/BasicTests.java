package datastructures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PUBLIC sanity-check suite.
 * Covers one simple “happy-path” scenario for each list implementation.
 * Corner-case and performance tests live in the private autograder suite.
 */
class BasicTests {

    /* ───── Array-based list ────────────────────────────────────────── */

    @Test @DisplayName("ArrayList: add then size == 1")
    void addAndSize() {
        CustomList<Contact> list = new ArrayBasedList();
        list.addContact(new Contact("King", "Alice",
                                    Collections.singletonList("a@uga")));
        assertEquals(1, list.size());
    }

    @Test @DisplayName("ArrayList: delete existing contact")
    void deleteExisting() {
        CustomList<Contact> list = new ArrayBasedList();
        list.addContact(new Contact("King", "Alice",
                                    Collections.singletonList("a@uga")));
        assertTrue(list.deleteContact("King", "Alice"));
        assertEquals(0, list.size());
    }

    @Test @DisplayName("ArrayList: basic add / merge / delete / traversal")
    void arrayBased_basicOps() {
        CustomList<Contact> list = new ArrayBasedList();
        populate(list);
        assertEquals(3, list.size());

        assertTrue(list.contains(new Contact("SMITH", "alice")));

        List<String> emails = list.getEmailAddressesForContact("smith", "ALICE");
        assertEquals(2, emails.size());
        assertTrue(emails.contains("alice@uga.edu"));
        assertTrue(emails.contains("alice@cs.uga.edu"));

        assertTrue(list.deleteEmailFromContact("SMITH", "ALICE", "ALICE@UGA.EDU"));
        assertFalse(list.deleteEmailFromContact("SMITH", "ALICE", "notfound@uga.edu"));

        assertTrue(list.deleteContact("brown", "bob"));
        assertFalse(list.deleteContact("brown", "bob"));

        int first = list.getTraversalCount();
        int second = list.getTraversalCount();  // counter resets
        assertTrue(first >= 0);
        assertEquals(0, second);
    }

    /* ───── Singly linked list ──────────────────────────────────────── */

    @Test @DisplayName("SinglyLinkedList: basic ops")
    void singly_basicOps() {
        CustomList<Contact> list = new SinglyLinkedList();
        populate(list);
        assertEquals(3, list.size());
        assertTrue(list.contains(new Contact("SMITH", "alice")));
        assertTrue(list.deleteContact("brown", "bob"));
    }

    /* ───── Doubly linked (sorted) list ─────────────────────────────── */

    @Test @DisplayName("DoublyLinkedList: sorted merge and email merge")
    void doubly_sortedAndMerge() {
        DoublyLinkedList list = new DoublyLinkedList();
        populate(list);

        DoublyLinkedList other = new DoublyLinkedList();
        other.addContact(new Contact("Brown", "Zed", List.of("zed@uga.edu")));
        other.addContact(new Contact("Brown", "Bob", List.of("bob@cs.uga.edu")));
        other.addContact(new Contact("Aaron", "Zoe", List.of("zoe@uga.edu")));

        list.mergeLists(other);

        assertEquals(5, list.size());
        assertEquals(0, other.size());   // merge clears source

        List<Contact> all = list.getAllContacts();
        for (int i = 1; i < all.size(); i++) {           // still sorted
            assertTrue(all.get(i - 1).compareTo(all.get(i)) <= 0);
        }
        List<String> bob = list.getEmailAddressesForContact("Brown", "Bob");
        assertEquals(2, bob.size());
    }

    /* ───── helper ──────────────────────────────────────────────────── */

    private static void populate(CustomList<Contact> list) {
        list.addContact(new Contact("Smith",  "Alice",
                List.of("alice@uga.edu", "Alice@uga.edu")));
        list.addContact(new Contact("Brown",  "Bob",
                List.of("bob@uga.edu")));
        list.addContact(new Contact("Adams",  "Charlie",
                List.of("charlie@uga.edu")));
        // duplicate name merges emails
        list.addContact(new Contact("Smith",  "Alice",
                List.of("alice@cs.uga.edu")));
    }
}
