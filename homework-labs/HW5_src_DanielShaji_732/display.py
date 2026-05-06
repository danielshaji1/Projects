"""
display.py — Concern: Order Status Display
--------------------------------------------
Responsible ONLY for showing the order-ready notification on the
front-end screen so customers know when to pick up their food.

No payment logic, no cooking logic, no menu logic.
"""

from order import Order


class Display:
    """Front-end screen that shows which order numbers are ready."""

    def __init__(self):
        # Keeps track of all order numbers announced as ready
        self._ready_orders: list[int] = []

    def announce_ready(self, order: Order) -> None:
        """
        Show the order number on screen when the order is ready.
        The customer matches this number against their receipt.
        """
        if not order.is_ready:
            print(f"  [Display] Order #{order.order_number} is not ready yet.")
            return

        self._ready_orders.append(order.order_number)
        print("\n" + "=" * 34)
        print(f"  *** NOW SERVING Order #{order.order_number} ***")
        print(f"      {order.food_item} — please pick up!")
        print("=" * 34 + "\n")

    def show_all_ready(self) -> None:
        """Display the list of all order numbers currently ready."""
        if not self._ready_orders:
            print("  [Display] No orders are ready yet.")
        else:
            nums = ", ".join(f"#{n}" for n in self._ready_orders)
            print(f"  [Display] Ready orders: {nums}")
