"""
cashier.py — Concern: Payment & Receipt
-----------------------------------------
Responsible ONLY for:
  1. Accepting payment from the customer.
  2. Printing a receipt with the order number.

No menu logic, no cooking logic, no queue logic.
"""

from order import Order


class Cashier:
    """Handles payment processing and receipt printing at the front end."""

    def process_payment(self, order: Order, amount_tendered: float) -> float:
        """
        Accept payment for an order.
        Marks the order as paid and returns the change.
        Raises ValueError if the amount tendered is insufficient.
        """
        if amount_tendered < order.price:
            raise ValueError(
                f"Insufficient payment. "
                f"Order total is ${order.price:.2f}, "
                f"but only ${amount_tendered:.2f} was provided."
            )

        order.mark_paid()
        change = round(amount_tendered - order.price, 2)
        return change

    def print_receipt(self, order: Order, change: float) -> None:
        """Print a receipt showing order details and change returned."""
        print("\n-------- RECEIPT --------")
        print(f"  Item      : {order.food_item}")
        print(f"  Order #   : {order.order_number}")
        print(f"  Price     : ${order.price:.2f}")
        print(f"  Change    : ${change:.2f}")
        print("  Thank you for your order!")
        print("-------------------------\n")
