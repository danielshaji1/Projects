import numpy as np
import pandas as pd


data = {
    "Name": ['Sam', 'Pam', 'Ham', 'Gam', 'Ralph'],
    "Homework": [0, 100, 100, 98, 50],
    "Grade": [9, 20, 30, 40, None ]

}
df = pd.DataFrame(data)


print (df)

