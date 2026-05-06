"""
order.py — Concern: Order Data
--------------------------------
Responsible ONLY for storing the data that belongs to a single order.
No payment logic, no cooking logic, no display logic.
"""


class Order:
    """Represents a single customer order (data container)."""

    _counter: int = 0  # shared counter to auto-assign order numbers

    def __init__(self, food_item: str, price: float):
        Order._counter += 1
        self._order_number: int = Order._counter
        self._food_item: str   = food_item
        self._price: float     = price
        self._is_paid: bool    = False
        self._is_ready: bool   = False

    # ── Getters ───────────────────────────────────────────────────────────────

    @property
    def order_number(self) -> int:
        return self._order_number

    @property
    def food_item(self) -> str:
        return self._food_item

    @property
    def price(self) -> float:
        return self._price

    @property
    def is_paid(self) -> bool:
        return self._is_paid

    @property
    def is_ready(self) -> bool:
        return self._is_ready

    # ── State setters (called by other concerned classes) ─────────────────────

    def mark_paid(self) -> None:
        self._is_paid = True

    def mark_ready(self) -> None:
        self._is_ready = True

    def __str__(self) -> str:
        return (f"Order #{self._order_number} | {self._food_item} "
                f"| ${self._price:.2f} "
                f"| Paid: {self._is_paid} | Ready: {self._is_ready}")
