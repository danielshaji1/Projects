# Restaurant Food Ordering Simulation

## Design Pattern: Separation of Concerns (SoC)

### What is Separation of Concerns?

Separation of Concerns is a design principle where a software system is divided
into distinct sections, each addressing a single, well-defined responsibility.
No class knows about or duplicates the internal logic of another class.
This leads to high cohesion within each module and low coupling between them —
exactly as taught in the course.

### How It Is Implemented Here

The restaurant story maps naturally onto six independent subsystems:

| File           | Class      | Sole Responsibility                              |
|----------------|------------|--------------------------------------------------|
| `menu.py`      | `Menu`     | Store food items and their prices                |
| `order.py`     | `Order`    | Hold all data for one order (number, item, price, status) |
| `customer.py`  | `Customer` | Choose a food item and tender payment            |
| `cashier.py`   | `Cashier`  | Process payment and print a receipt              |
| `cook.py`      | `Cook`     | Prepare (cook) the food                         |
| `display.py`   | `Display`  | Announce ready order numbers on the screen       |
| `main.py`      | —          | Orchestrate the full order lifecycle             |

Each class is deliberately "ignorant" of the others' internals.
For example:
- The `Cashier` never touches the `Cook` — it only processes payment.
- The `Cook` never touches the `Menu` — it only prepares food.
- The `Display` never touches the `Cashier` — it only announces ready orders.

### Order Lifecycle (mirrors the story)

```
1. Customer tells the Cashier which food to order.
2. Cashier looks up the price on the Menu and creates an Order object.
3. Customer tenders payment → Cashier processes it and prints a receipt
   (with the order number).
4. Cook prepares the food in the back end.
5. Display announces the order number on screen.
6. Customer sees their number matches their receipt, picks up food, leaves.
```

### Class Diagram (textual)

```
  Customer ──uses──> Menu
      │
      │ places order via
      v
  Cashier ──creates──> Order <──prepares── Cook
                          │
                          └──read by──> Display
```

---

## How to Run

### Requirements
- Python 3.10 or higher (uses built-in `time` and `abc` modules only)
- No third-party packages needed

### Steps

```bash
# 1. Navigate to the project folder
cd restaurant_sim

# 2. Run the simulation
python main.py
```

### Expected Output

```
===== MENU =====
  Burger       $8.99
  Pizza        $11.49
  ...

New order from Alice
  [Customer] Alice wants to order: Burger
  [Cashier]  Order #1 created — Burger at $8.99
  [Customer] Alice tenders $9.99.
  -------- RECEIPT --------
    Order # : 1  ...
  [Cook] Preparing Burger for Order #1...
  [Cook] Order #1 (Burger) is READY!
  *** NOW SERVING Order #1 ***
  [Customer] Alice picks up Order #1 and leaves the queue.
  ...
```

---

## File Structure

```
restaurant_sim/
├── main.py        ← entry point / orchestrator
├── menu.py        ← Menu concern
├── order.py       ← Order data concern
├── customer.py    ← Customer concern
├── cashier.py     ← Payment & receipt concern
├── cook.py        ← Food preparation concern
├── display.py     ← Order status display concern
└── README.md      ← this file
```
