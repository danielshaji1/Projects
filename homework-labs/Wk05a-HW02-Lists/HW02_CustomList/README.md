# HW02_CustomList — Student Skeleton

This is a stripped student skeleton for HW02. Your task is to implement the TODOs in the datastructures package per the assignment HTML.

Implementations to complete:
- datastructures/Contact.java — addEmail, deleteEmail, mergeEmailsFrom, compareTo
- datastructures/ArrayBasedList.java — core list ops (unsorted)
- datastructures/SinglyLinkedList.java — core list ops (unsorted)
- datastructures/DoublyLinkedList.java — core list ops (sorted)

Provided:
- drivers/Driver.java (do not edit)
- helpers/DriverHelper.java (do not edit)
- datastructures/AbstractContactList.java with helpful defaults and traversal counting
- data sample files

Notes:
- Use bump() to count traversals when iterating
- Names are case-insensitive for identity and ordering (last, then first)
- Emails are case-insensitive; avoid duplicates; merge on duplicate contacts

Build on odin
- Java 17+: mvn clean package
- mvn -q -f pom.xml -Dmaven.compiler.release=17 -e -DskipTests package

Run
```
java -cp target/classes drivers.Driver
```

See schedule page HW02 .html for full specification and grading.
