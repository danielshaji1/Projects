const { 
  one, 
  two,
  three,
  four,
  five,
  six,
  seven
} = require('../index');

describe('Array Methods Tests', () => {
  describe('Filter Functions', () => {
    test('one should return only even numbers', () => {
      const input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
      const expected = [2, 4, 6, 8, 10];
      const result = one(input);
      expect(result).toEqual(expected);
    });

    test('two should return only odd numbers', () => {
      const input = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
      const expected = [1, 3, 5, 7, 9];
      const result = two(input);
      expect(result).toEqual(expected);
    });

    test('three should return users aged 18 and above', () => {
      const users = [
        { name: 'Alice', age: 17 },
        { name: 'Bob', age: 18 },
        { name: 'Charlie', age: 25 },
        { name: 'Diana', age: 16 }
      ];
      const expected = [
        { name: 'Bob', age: 18 },
        { name: 'Charlie', age: 25 }
      ];
      const result = three(users);
      expect(result).toEqual(expected);
    });

    test('four should filter users by type', () => {
      const users = [
        { name: 'Admin1', type: 'admin' },
        { name: 'User1', type: 'user' },
        { name: 'Admin2', type: 'admin' },
        { name: 'Moderator', type: 'moderator' }
      ];
      
      const adminUsers = four(users, 'admin');
      expect(adminUsers).toEqual([
        { name: 'Admin1', type: 'admin' },
        { name: 'Admin2', type: 'admin' }
      ]);

      const userUsers = four(users, 'user');
      expect(userUsers).toEqual([{ name: 'User1', type: 'user' }]);
    });

    test('filter functions should not modify original arrays', () => {
      const original = [1, 2, 3, 4, 5];
      const copy = [...original];
      
      one(original);
      expect(original).toEqual(copy);
    });
  });

  describe('Map Functions', () => {
    test('five should double each number', () => {
      const input = [1, 2, 3, 4, 5];
      const expected = [2, 4, 6, 8, 10];
      const result = five(input);
      expect(result).toEqual(expected);
    });

    test('six should square each number', () => {
      const input = [1, 2, 3, 4, 5];
      const expected = [1, 4, 9, 16, 25];
      const result = six(input);
      expect(result).toEqual(expected);
    });

    test('seven should format names to uppercase', () => {
      const users = [
        { name: 'alice', age: 25 },
        { name: 'bob', age: 30 },
        { name: 'charlie', age: 35 }
      ];
      const expected = ['ALICE', 'BOB', 'CHARLIE'];
      const result = seven(users);
      expect(result).toEqual(expected);
    });

    test('map functions should not modify original arrays', () => {
      const original = [1, 2, 3];
      const copy = [...original];
      
      five(original);
      expect(original).toEqual(copy);
    });
  });

  describe('Combined Operations', () => {
    test('should filter adults and then format their names', () => {
      const users = [
        { name: 'alice', age: 17 },
        { name: 'bob', age: 18 },
        { name: 'charlie', age: 25 },
        { name: 'diana', age: 16 }
      ];
      
      // First filter adults, then format names
      const adults = three(users);
      const formattedNames = seven(adults);

      expect(formattedNames).toEqual(['BOB', 'CHARLIE']);
    });

    test('should filter even numbers and then square them', () => {
      const numbers = [1, 2, 3, 4, 5, 6, 7, 8];
      
      const evens = one(numbers);
      const squaredEvens = six(evens);

      expect(squaredEvens).toEqual([4, 16, 36, 64]);
    });
  });
});