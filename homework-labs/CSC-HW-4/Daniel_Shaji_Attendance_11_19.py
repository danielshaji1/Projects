import pandas as pd
import numpy as np

df1 = pd.DataFrame()

series1 = np.random.randint(0, 99, size=10)
series2 = np.random.randint(0, 24, size=10)
print(series2[4])

df1['Age'] = series1
df1['Score'] = series2

new_row = {'Age': 45, 'Score': 10}
df1.loc[len(df1)] = new_row

company_names = ['Google', 'Apple', 'Microsoft']
df1['Company'] = np.random.choice(company_names, size=len(df1))

df1.loc[len(df1)] = {'Age': 30, 'Score': 15, 'Company': 'Tencent'}
df1.loc[len(df1)] = {'Age': 50, 'Score': 25, 'Company': 'Tencent'}

df1 = df1[['Company', 'Age', 'Score']]

print(f'Updated DataFrame:\n{df1}\n')

try:
    df1.to_csv('pd.exer.csv', index=False)
    print("Data frame saved to computer")
except Exception as e:
    print(f"Couldn't save file to computer: {e}")

try:
    df_from_csv = pd.read_csv('pd.exer.csv')
    print(f'Read from CSV:\n{df_from_csv}\n')
except Exception as e:
    print(f"Error reading CSV: {e}")

df_score_above_50 = df1[df1['Score'] > 50]
print("Rows with Score > 50:")
print(df_score_above_50)

df_age_above_20_score_above_50 = df1[(df1['Age'] > 20) & (df1['Score'] > 50)]
print("\nRows with Age > 20 and Score > 50:")
print(df_age_above_20_score_above_50)

grouped = df1.groupby('Company')

for company, group in grouped:
    print(f"\nGroup: {company}")
    print(group)

tencent_group = df1[df1['Company'] == 'Tencent']

aggregated_data = tencent_group.agg({'Age': 'median', 'Score': 'mean'})

print("\nAggregated data for Tencent group:")
print(aggregated_data)