import numpy as np

array1 = np.zeros([4, 6], dtype=int)
print(f'Shape of array: {array1.shape}\n')
print(f'Size of array: {array1.size}\n')

print("Array after step 3: ")
array1 = np.insert(array1, 2, 7, 0)
print(array1, '\n')

print('Array after step 4: ')
array2 = np.insert(array1, 0,  2, 1)
print(array2, '\n')

print("Array after step 5:")
array3 = np.sort(array2, axis=1)
print(array3, '\n')


print("Array after step 6: ")
array4 = np.resize(array3, (7, 5))
print(array4, '\n')

print("Array after step 7: ")
array5 = array4.flatten(order='F')
print(array5, '\n')


print("Array after step 8:")
array_of_ones = np.full(len(array5),1, dtype=int)
array6 = np.add(array5, array_of_ones)
print(array6)
