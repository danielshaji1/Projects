import pandas as pd
import matplotlib as plt

# Data for the top 10 teams
data = {
    "Team": [
        "Cleveland Cavaliers", "Boston Celtics", "Oklahoma City Thunder",
        "Houston Rockets", "Orlando Magic", "Memphis Grizzlies",
        "Golden State Warriors", "Dallas Mavericks", "Denver Nuggets",
        "Los Angeles Lakers"
    ],
    "GP": [20, 19, 19, 20, 22, 21, 19, 20, 17, 19],  # Games Played
    "W": [17, 16, 15, 14, 15, 14, 12, 12, 10, 11],   # Wins
    "L": [3, 3, 4, 6, 7, 7, 7, 8, 7, 8],            # Losses
    "Win%": [0.850, 0.842, 0.789, 0.700, 0.682, 0.667, 0.632, 0.600, 0.588, 0.579],
    "MIN": [48.0, 48.8, 48.0, 48.8, 48.0, 48.0, 48.3, 48.3, 48.6, 48.0],  # Minutes
    "PTS": [122.4, 121.2, 113.9, 114.4, 108.0, 121.7, 116.2, 117.0, 117.8, 114.4],  # Points
}

# Step 1: Create the initial DataFrame
df = pd.DataFrame(data)

# Step 2: Add a new column "Assists Per Game" (placeholder data)
df["Assists Per Game"] = [25.4, 27.6, 24.8, 22.7, 21.5, 26.1, 25.0, 23.4, 24.5, 24.2]

# Step 3: Create a new DataFrame for the Toronto Raptors
toronto_data = {
    "Team": ["Toronto Raptors"],
    "GP": [20],
    "W": [5],
    "L": [15],
    "Win%": [0.250],
    "MIN": [48.8],
    "PTS": [112.5],
    "Assists Per Game": [22.0]  # Placeholder for assists
}
toronto_df = pd.DataFrame(toronto_data)

# Step 4: Concatenate the two DataFrames
df = pd.concat([df, toronto_df], ignore_index=True)

# Step 5: Export the final DataFrame to a CSV file
output_file = "extra_credit.csv"
df.to_csv(output_file, index=False)
print(f"DataFrame exported to {output_file}")

# Display the final DataFrame
print(df)

