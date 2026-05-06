"""
main.py — Restaurant Food Ordering Simulation
===============================================

Design Pattern Applied: Separation of Concerns (SoC)
------------------------------------------------------
The system is divided into distinct subsystems, each handling exactly
ONE responsibility. No class knows about another class's internal logic.

Subsystem breakdown:
  ┌──────────────┬────────────────────────────────────────────┐
  │ Class        │ Sole Responsibility                         │
  ├──────────────┼────────────────────────────────────────────┤
  │ Menu         │ Stores food items and prices               │
  │ Order        │ Holds the data for a single order          │
  │ Customer     │ Chooses food and tenders payment           │
  │ Cashier      │ Processes payment and prints receipt       │
  │ Cook         │ Prepares (cooks) the food                  │
  │ Display      │ Announces ready orders on the screen       │
  └──────────────┴────────────────────────────────────────────┘

Flow (mirrors the story):
  1. Customer tells the Cashier what to order.
  2. Cashier looks up the price on the Menu and creates an Order.
  3. Customer pays; Cashier processes payment and prints a receipt.
  4. Cook prepares the order in the back end.
  5. Display announces the order number on screen.
  6. Customer sees their number, picks up food, and leaves the queue.
"""

from menu import Menu
from order import Order
from customer import Customer
from cashier import Cashier
from cook import Cook
from display import Display


def simulate_order(customer: Customer, item_name: str,
                   menu: Menu, cashier: Cashier,
                   cook: Cook, display: Display) -> None:
    """Run the full lifecycle of a single restaurant order."""

    print(f"\n{'─' * 45}")
    print(f"  New order from {customer.name}")
    print(f"{'─' * 45}")

    # ── Step 1: Customer chooses a food item ──────────────────────────────────
    chosen = customer.choose_item(item_name)

    # ── Step 2: Cashier looks up price on Menu and creates an Order ───────────
    if not menu.is_available(chosen):
        print(f"  [Cashier] Sorry, '{chosen}' is not on the menu.")
        return

    price = menu.get_price(chosen)
    order = Order(food_item=chosen, price=price)
    print(f"  [Cashier] Order #{order.order_number} created — "
          f"{order.food_item} at ${order.price:.2f}")

    # ── Step 3: Customer pays; Cashier processes and prints receipt ───────────
    amount = customer.tender_payment(price + 1.00)   # customer gives a little extra
    change = cashier.process_payment(order, amount)
    cashier.print_receipt(order, change)

    # ── Step 4: Cook prepares the order in the back end ───────────────────────
    cook.prepare(order)

    # ── Step 5: Display announces the order number ────────────────────────────
    display.announce_ready(order)

    # ── Step 6: Customer picks up food and leaves ─────────────────────────────
    customer.pick_up_food(order.order_number, order.food_item)


def main() -> None:
    # ── Instantiate each subsystem once ───────────────────────────────────────
    menu     = Menu()
    cashier  = Cashier()
    cook     = Cook()
    display  = Display()

    # ── Show the menu ─────────────────────────────────────────────────────────
    menu.display()

    # ── Create customers ──────────────────────────────────────────────────────
    alice = Customer(name="Alice", wallet=50.00)
    bob   = Customer(name="Bob",   wallet=30.00)
    carol = Customer(name="Carol", wallet=20.00)

    # ── Simulate orders ───────────────────────────────────────────────────────
    simulate_order(alice, "Burger",   menu, cashier, cook, display)
    simulate_order(bob,   "Pizza",    menu, cashier, cook, display)
    simulate_order(carol, "Salad",    menu, cashier, cook, display)

    # ── Final screen state ────────────────────────────────────────────────────
    print("\n--- All ready orders on the display board ---")
    display.show_all_ready()


if __name__ == "__main__":
    main()
