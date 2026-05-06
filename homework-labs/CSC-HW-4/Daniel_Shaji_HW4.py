import tkinter as tk
from tkinter import messagebox
import math

class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

# Logic from previous assignment
class CircularLinkedList:
    def __init__(self):
        self.head = None

    def append(self, data):
        new_node = Node(data)
        if not self.head:
            self.head = new_node
            self.head.next = self.head
        else:
            temp = self.head
            while temp.next != self.head:
                temp = temp.next
            temp.next = new_node
            new_node.next = self.head

    def eliminate(self, k):
        if not self.head or not self.head.next:
            return None

        prev = None
        curr = self.head

        # Move `k-1` steps
        for _ in range(k - 1):
            prev = curr
            curr = curr.next

        eliminated = curr.data
        if prev:
            prev.next = curr.next
        if curr == self.head:
            self.head = curr.next
        if curr.next == curr:
            self.head = None

        return eliminated

# Game code
class CountingOutGame:
    def __init__(self, root):
        self.root = root
        self.root.title("Counting-Out Game")
        self.text_widget = None
        self.player_icons = []
        self.start_button = None
        self.eliminate_button = None
        self.cll = None
        self.n = 0
        self.k = 0
        self.init_ui()

    def init_ui(self):
        tk.Label(self.root, text="N (1<N<12):").grid(row=0, column=0)
        self.n_entry = tk.Entry(self.root)
        self.n_entry.grid(row=0, column=1)

        tk.Label(self.root, text="K (K>=1):").grid(row=1, column=0)
        self.k_entry = tk.Entry(self.root)
        self.k_entry.grid(row=1, column=1)

        self.start_button = tk.Button(self.root, text="Start", command=self.start_game)
        self.start_button.grid(row=2, column=0, columnspan=2)

        self.text_widget = tk.Text(self.root, height=10, width=40)
        self.text_widget.grid(row=3, column=0, columnspan=2)

    def start_game(self):
        try:
            self.n = int(self.n_entry.get())
            self.k = int(self.k_entry.get())

            if not (1 < self.n < 12) or self.k < 1:
                raise ValueError

        except ValueError:
            messagebox.showinfo("Invalid Input", "Please enter valid values for N and K.")
            return

        self.reset_ui()
        self.cll = CircularLinkedList()

        for i in range(self.n):
            self.cll.append(i)

        # Place players in a circular layout
        radius = 100  # Radius of the circle for player layout
        center_x, center_y = 200, 200  # Center of the circle

        self.player_icons = []
        for i in range(self.n):
            angle = 2 * math.pi * i / self.n
            x = center_x + radius * math.cos(angle)
            y = center_y + radius * math.sin(angle)
            btn = tk.Label(self.root, text=f"{i}", relief="solid")
            btn.place(x=x, y=y, width=40, height=30)
            self.player_icons.append(btn)

        self.eliminate_button = tk.Button(self.root, text="Eliminate", command=self.eliminate_player)
        self.eliminate_button.place(x=center_x - 40, y=center_y - 15, width=80, height=30)

        self.text_widget.insert(tk.END, f"Game started. N={self.n}, K={self.k}\n")

    def eliminate_player(self):
        eliminated = self.cll.eliminate(self.k)
        if eliminated is not None:
            self.text_widget.insert(tk.END, f"Player {eliminated} eliminated.\n")
            self.player_icons[eliminated].place_forget()
            self.player_icons[eliminated] = None

        if self.cll.head and self.cll.head.next == self.cll.head:
            winner = self.cll.head.data
            messagebox.showinfo("Game Over", f"The winner is Player {winner}!")
            self.reset_ui()
        elif not self.cll.head:
            messagebox.showinfo("Game Over", "No players remaining. Something went wrong!")
            self.reset_ui()

    def reset_ui(self):
        self.text_widget.delete("1.0", tk.END)
        if self.player_icons:
            for icon in self.player_icons:
                if icon:
                    icon.place_forget()
        self.player_icons = []
        if self.eliminate_button:
            self.eliminate_button.place_forget()

# Run
if __name__ == "__main__":
    root = tk.Tk()
    root.geometry("400x400")
    app = CountingOutGame(root)
    root.mainloop()