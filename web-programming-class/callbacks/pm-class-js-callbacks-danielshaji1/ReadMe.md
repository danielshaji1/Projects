## Callback Tasks Assignment

This project demonstrates how to use callbacks to control the **execution order** in asynchronous JavaScript.


---

## Learning Outcomes
By the end of this assignment, you will:

- Explain why asynchronous functions may complete out of order
- Use callbacks to enforce execution order
- Apply the error-first callback pattern
- Recognize why deeply nested callbacks become difficult to maintain

---

## Files

1. `callback.js` — the single JavaScript file you will complete.
2. This README — instructions for each part.

---

## Instructions

You must complete the following tasks **in order**:

1. Do homework  
2. Clean room  
3. Play games  

Each task takes time and is simulated using `setTimeout`.
If you start all tasks at once, they will **not** finish in the correct order.

Your job is to use **callbacks** to guarantee the correct sequence.
---

## Callback Pattern (Important)

All callbacks in this assignment use the **error-first pattern**:

```js
callback(error, result)
```

On success:
```js
  callback(null, "Success message")
```
On failure:
```js
  callback("ERROR", null)
```

**Part1:** Task Functions

Implement the following functions in callback.js:

**doHomework(callback)**

- Uses setTimeout
- Waits 1000 milliseconds
- Calls:

  callback(null, "Homework done")

**cleanRoom(callback)**

- Uses setTimeout
- Waits 800 milliseconds
- Calls:

```js
  callback(null, "Room clean")
```
**playGames(callback)**

- Uses setTimeout
- Waits 500 milliseconds
- Calls:

```js
  callback(null, "Games played")
```

Each function must call its callback once and use the error-first pattern.

**Part 2** — Ordering with Callbacks Only

Implement runRoutine to ensure the tasks run strictly in this order:
1. doHomework
2. cleanRoom
3. playGames

```js
  runRoutine(finalCallback)
```

**Notes:**

- If any task produces an error, call:

```js
  finalCallback(error, null)
```

- and end execution.


- If all tasks succeed, call:

```js
  finalCallback(null, [
    "Homework done",
    "Room clean",
    "Games played"
  ])
```
---

## Testing

1. Install the tools by running:

```bash
npm install
```

2. Run your file with Node:

```bash
node callback.js
```

3. Run test:

```bash
npm test
```

--- 
## Important Note

If you see messages about `npm audit` or security vulnerabilities, **disregard them**. These do not affect the functionality of this assignment and may interfere with testing if followed.
