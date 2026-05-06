import pandas as pd
import matplotlib as mpl
import matplotlib.pyplot as plt
import numpy as np

#read csv file into Pandas DF
df = pd.read_csv("car_info.csv")

print('\n')
#shape
print("Shape of cars_csv = ", df.shape)

#v6_japan
v6_cars = df[(df['origin'] == 'japan') & (df['cylinders'] == 6)]
print("Japanese cars with v6 engine: ")
for i in v6_cars['name']:
    print('[' + i + ']',end=', ')

print('\n')

#no hp cars
print("Cars with no horsepower listed: ")
no_hp_cars = (df[(df['horsepower'].isnull())])
for i in no_hp_cars['name']:
    print('[' + i + ']',end=', ')

print('\n')

#cars mpg > 20
cars_20_mpg_or_higher = df[(df['mpg'] >= 20)]
print("Cars with mpg >= 20: ", end='')
print(cars_20_mpg_or_higher['name'].count(), end='')

print('\n') 

max_mpg = df[(df['mpg'].max() == df['mpg'])]
print("Most fuel efficient car: ", end=": ")
for i in max_mpg['name']:
    print(i)
    print()


max_weight = df[(df['weight'].max() == df['weight'])]
min_weight = df[(df['weight'].min() == df['weight'])]
avg_weight = df['weight'].mean()
print('Max weight:', end=' ')
for i in max_weight['weight']:
    print (i)
print('Min weight:', end=' ')
for i in min_weight['weight']:
    print(i)
print('Average weight:', end=' ')
avg_weight = float(round(avg_weight, 2))
print(avg_weight)

df = df.dropna(axis=0)
print("Shape of dropped rows dataframe: ", end='')
print(df.shape)

print('\n')

origin_counts = df['origin'].value_counts()

plt.figure(figsize=(15, 8))
plt.pie(origin_counts, labels=origin_counts.index, autopct='%1.1f%%')

plt.title('Distribution of Car Origins')

x_mpg = np.array(df['mpg'])
y_weight = np.array(df['weight'])
y_displacement = np.array(df['displacement'])

# Subplot 1: Scatter plot for mpg vs. weight
plt.subplot(3, 1, 1)  # 2 rows, 1 column, first subplot
plt.scatter(x_mpg, y_weight, color='blue', label='Weight vs MPG')
plt.xlabel('MPG')
plt.ylabel('Weight')
plt.legend()
plt.title('Scatter Plot: MPG vs Weight')

# Subplot 2: Line plot for mpg vs. displacement
plt.subplot(3, 1, 3)  # 2 rows, 1 column, second subplot
plt.plot(x_mpg, y_displacement, color='red', label='Displacement vs MPG')
plt.xlabel('MPG')
plt.ylabel('Displacement')
plt.legend()
plt.title('Line Plot: MPG vs Displacement')

# Display the plots
plt.tight_layout() 
plt.show()
