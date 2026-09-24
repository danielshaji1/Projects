# Three Ways to Write a Function in JavaScript

## Overview of Assignment 

In this assignment, students will implement the same `add(a, b)` functionality using three different JavaScript function styles. The goal is to understand both the syntax and behavioral differences between function declarations, function expressions, and arrow functions.

---

## Learning Outcomes

At the end of this assignment, students will be able to:

* Implement functions using three different JavaScript syntaxes
* Differentiate between function declarations, expressions, and arrow functions

---

## Files Included

| File                    | Description                                                                    |
| ----------------------- | ------------------------------------------------------------------------------ |
| `functionCalls.js`      | Starter file where students implement the three versions of the `add` function |
| `functionCalls.test.js` | Jest test suite that verifies both implementation style and correctness        |

---

## What’s Inside the Repo

### Already Provided

* The file structure
* Export statements
* A demo runner inside `functionCalls.js`
* A complete Jest test suite

### You Must Implement

Inside `functionCalls.js`, you must write:

* `addDecl` → using a **function declaration**
* `addExpr` → using a **function expression**
* `addArrow` → using an **arrow function**

---

## Project Goal

By the end, running the file and the tests should confirm that all three implementations correctly add two numbers and follow the required function styles.

---

## Instructions

### Step 1 — Open the functionCalls.js and 

Open:

```
functionCalls.js
```

---

### Step 2 — Implement the three functions

Write your implementations for:

* `addDecl`
* `addExpr`
* `addArrow`

All three functions should return a + b.
The difference is how the function is written, not what it does.

---

### Step 3 — Ensure exports are present

At the bottom of the file, confirm:

```js
module.exports = { addDecl, addExpr, addArrow };
```

---

### Step 4 — Run the file directly

From your terminal:

```bash
node functionCalls.js
```

You should see:

```
addDecl(2, 3)  = 5
addExpr(2, 3)  = 5
addArrow(2, 3) = 5
```


### Step 5 — Test your work
Install the tests:
```bash
npm ci
```

Run all tests:

```bash
npm test
```

If everything is correct:

* All tests will pass
* Jest output will be green

---

## Important Note

If you see messages about `npm audit` or security vulnerabilities, **disregard them**. These do not affect the functionality of this assignment and may interfere with testing if followed.
