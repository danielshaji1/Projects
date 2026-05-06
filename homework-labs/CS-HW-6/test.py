import pandas as pd

# Creating the DataFrame
data = {
    'height': [186, 160, 151, 153, 180, 178, 152, 185, 168, 169],
    'weight': [53, 42, 57, 85, 56, 78, 73, 88, 68, 69],
    'gender': ['female', 'male', 'male', 'female', 'male', 'female', 'male', 'male', 'male', 'female'],
    'age': [79, 68, 66, 71, 53, 49, 81, 72, 63, 46],
    'color': ['black', 'black', 'black', 'black', 'white', 'white', 'black', 'black', 'black', 'white'],
    'salary': [360901, 187795, 265852, 392481, 383255, 192312, 495282, 345569, 462385, 346069]
}

df = pd.DataFrame(data)
print(df.loc[0])
def gender_map(gender):
    if gender == 'female':
        return 1
    elif gender == 'male':
        return 0
    
df['gender'] = df['gender'].apply(gender_map)
# Replacing the 'gender' column values

# Display the updated DataFrame
#print(df)

import numpy as np

ones = np.ones(shape=[4, 4], dtype=int)
print(ones)

ones = np.ravel(ones)
print(ones)