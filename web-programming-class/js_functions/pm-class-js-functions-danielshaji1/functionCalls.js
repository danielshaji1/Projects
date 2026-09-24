// functionCalls.js
// A simple example function implemented three ways: add two numbers (a, b)

// 1) Traditional function declaration - addDecl

// 2) Function expression - addExpr

// 3) Arrow function - addArrow

function addDecl(a, b) {
    return a + b;
}

const addExpr = function(a, b) {
    return a + b;
};

const addArrow = (a, b) => a + b;  


  // Export for use in other modules
  if (typeof module !== 'undefined' && module.exports) {
    module.exports = { addDecl, addExpr, addArrow };
  }
  
  // Quick demo if you run this file directly with `node functionCalls.js`
  if (typeof require !== 'undefined' && require.main === module) {
    console.log('addDecl(2, 3)  =', addDecl(2, 3));
    console.log('addExpr(2, 3)  =', addExpr(2, 3));
    console.log('addArrow(2, 3) =', addArrow(2, 3));
  }