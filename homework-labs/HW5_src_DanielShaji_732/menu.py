"""
menu.py — Concern: Menu Management
-----------------------------------
Responsible ONLY for storing available food items and their prices.
No ordering logic, no payment logic, no cooking logic.
"""


class Menu:
    """Holds available food items and their prices."""

    def __init__(self):
        # Internal price list — hidden from outside classes
        self._items: dict[str, float] = {
            "Burger":    8.99,
            "Pizza":     11.49,
            "Pasta":     9.99,
            "Salad":     6.49,
            "Sandwich":  7.29,
        }

    def get_price(self, item_name: str) -> float:
        """Return the price of an item. Raises KeyError if not found."""
        if item_name not in self._items:
            raise KeyError(f"'{item_name}' is not on the menu.")
        return self._items[item_name]

    def is_available(self, item_name: str) -> bool:
        """Check whether an item exists on the menu."""
        return item_name in self._items

    def display(self) -> None:
        """Print the menu to the screen."""
        print("\n===== MENU =====")
        for item, price in self._items.items():
            print(f"  {item:<12} ${price:.2f}")
        print("================\n")
