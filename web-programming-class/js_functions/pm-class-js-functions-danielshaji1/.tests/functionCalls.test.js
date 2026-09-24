// functionCalls.test.js
const { addDecl, addExpr, addArrow } = require('../functionCalls');

describe('functionCalls.js', () => {
  //
  // Implementation style tests
  //
  describe('Function existence and types', () => {
    test('addDecl should be a traditional function declaration', () => {
      expect(typeof addDecl).toBe('function');
      const src = addDecl.toString().trim();
      expect(src.startsWith('function')).toBe(true);
    });

    test('addExpr should be a function expression', () => {
      expect(typeof addExpr).toBe('function');
      const src = addExpr.toString().trim();
      // Must use `function` keyword but not "function addExpr"
      expect(src.startsWith('function')).toBe(true);
      expect(src.startsWith('function addExpr')).toBe(false);
      expect(addExpr.name).toBe('addExpr'); // bound to variable
    });

    test('addArrow should be an arrow function', () => {
      expect(typeof addArrow).toBe('function');
      const src = addArrow.toString().trim();
      expect(src.includes('=>')).toBe(true);
      expect(addArrow.prototype).toBeUndefined(); 
    });
  });

  //
  // Correctness tests
  //
  describe('Correctness of addition', () => {
    const cases = [
      [2, 3, 5],
      [-1, 1, 0],
      [0, 0, 0],
      [10, -5, 5],
      [100, 200, 300],
      [1.5, 2.5, 4],
      [Number.MAX_SAFE_INTEGER, 1, Number.MAX_SAFE_INTEGER + 1],
    ];

    test.each(cases)('addDecl(%p, %p) = %p', (a, b, expected) => {
      expect(addDecl(a, b)).toBe(expected);
    });

    test.each(cases)('addExpr(%p, %p) = %p', (a, b, expected) => {
      expect(addExpr(a, b)).toBe(expected);
    });

    test.each(cases)('addArrow(%p, %p) = %p', (a, b, expected) => {
      expect(addArrow(a, b)).toBe(expected);
    });
  });

  //
  // Edge cases
  //
  describe('Edge cases', () => {
    test('adding NaN returns NaN', () => {
      expect(Number.isNaN(addDecl(NaN, 2))).toBe(true);
      expect(Number.isNaN(addExpr(2, NaN))).toBe(true);
      expect(Number.isNaN(addArrow(NaN, NaN))).toBe(true);
    });

    test('adding Infinity works correctly', () => {
      expect(addDecl(Infinity, 1)).toBe(Infinity);
      expect(addExpr(-Infinity, 1)).toBe(-Infinity);
      expect(addArrow(Infinity, -Infinity)).toBeNaN();
    });
  });

});
