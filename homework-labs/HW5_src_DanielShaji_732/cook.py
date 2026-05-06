"""
cook.py — Concern: Food Preparation
--------------------------------------
Responsible ONLY for preparing (cooking) an order.
No payment logic, no menu logic, no display logic.
"""

import time
from order import Order


class Cook:
    """Back-end cook who prepares orders."""

    # Simulated preparation times (seconds) per food item
    _prep_times: dict[str, float] = {
        "Burger":   1.5,
        "Pizza":    2.0,
        "Pasta":    1.8,
        "Salad":    0.8,
        "Sandwich": 1.0,
    }

    def prepare(self, order: Order) -> None:
        """
        Simulate cooking the food for the given order.
        Marks the order as ready when done.
        """
        wait = self._prep_times.get(order.food_item, 1.0)
        print(f"  [Cook] Preparing {order.food_item} "
              f"for Order #{order.order_number}...")
        time.sleep(wait)          # simulate cooking time
        order.mark_ready()
        print(f"  [Cook] Order #{order.order_number} "
              f"({order.food_item}) is READY!")
