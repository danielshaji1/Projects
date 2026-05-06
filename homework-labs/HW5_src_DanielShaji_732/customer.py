"""
customer.py — Concern: Customer Actions
-----------------------------------------
Responsible ONLY for:
  1. Choosing a food item to order.
  2. Tendering payment.
  3. Waiting and picking up food when their number is called.

No cooking logic, no payment processing, no menu management.
"""


class Customer:
    """Represents a restaurant customer."""

    def __init__(self, name: str, wallet: float):
        self._name: str    = name
        self._wallet: float = wallet  # money available to spend

    @property
    def name(self) -> str:
        return self._name

    @property
    def wallet(self) -> float:
        return self._wallet

    def choose_item(self, item_name: str) -> str:
        """Return the name of the item the customer wants to order."""
        print(f"  [Customer] {self._name} wants to order: {item_name}")
        return item_name

    def tender_payment(self, amount: float) -> float:
        """
        Hand over money to pay for the order.
        Raises ValueError if the customer does not have enough funds.
        """
        if amount > self._wallet:
            raise ValueError(
                f"{self._name} only has ${self._wallet:.2f} "
                f"but tried to pay ${amount:.2f}."
            )
        self._wallet -= amount
        print(f"  [Customer] {self._name} tenders ${amount:.2f}.")
        return amount

    def pick_up_food(self, order_number: int, food_item: str) -> None:
        """Customer collects their food after seeing their number on screen."""
        print(f"  [Customer] {self._name} picks up "
              f"Order #{order_number} ({food_item}) and leaves the queue.")
