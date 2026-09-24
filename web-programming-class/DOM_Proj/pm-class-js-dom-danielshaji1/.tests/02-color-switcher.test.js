const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('Part 2: Switch the Background Color', () => {
  let dom;
  let document;
  let window;
  let htmlContent;

  beforeEach(() => {
    const htmlFilePath = path.join(__dirname, '../index.html');
    htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');
    
    dom = new JSDOM(htmlContent, {
      runScripts: 'dangerously',
      resources: 'usable'
    });
    
    document = dom.window.document;
    window = dom.window;
    
    const jsFilePath = path.join(__dirname, '../js/dom_js.js');
    const jsContent = fs.readFileSync(jsFilePath, 'utf-8');
    
    const script = document.createElement('script');
    script.textContent = jsContent;
    document.head.appendChild(script);
  });

  describe('Color changing functionality', () => {
    test('changes background to red when red button is clicked', () => {
    const redButton = document.getElementById('red');
    const colorDiv = document.getElementById('div_color');

    // Prove initial state
    expect(colorDiv.className).toBe('bg_grey');

    redButton.click();

    expect(colorDiv.className).toBe('bg_red');
  });


    test('changes background to yellow when yellow button is clicked', () => {
      const yellowButton = document.getElementById('yellow');
      const colorDiv = document.getElementById('div_color');

      // Prove initial state
      expect(colorDiv.className).toBe('bg_grey');
      
      yellowButton.click();
      
      expect(colorDiv.className).toBe('bg_yellow');
    });

    test('changes background to green when green button is clicked', () => {
      const greenButton = document.getElementById('green');
      const colorDiv = document.getElementById('div_color');
      // Prove initial state
      expect(colorDiv.className).toBe('bg_grey');
      
      greenButton.click();
      
      expect(colorDiv.className).toBe('bg_green');
    });

    test('changes background to blue when blue button is clicked', () => {
      const blueButton = document.getElementById('blue');
      const colorDiv = document.getElementById('div_color');
      // Prove initial state
      expect(colorDiv.className).toBe('bg_grey');
      
      blueButton.click();
      
      expect(colorDiv.className).toBe('bg_blue');
    });

    test('div color changes correctly between different colors', () => {
      const redButton = document.getElementById('red');
      const greenButton = document.getElementById('green');
      const blueButton = document.getElementById('blue');
      const colorDiv = document.getElementById('div_color');
      
      redButton.click();
      expect(colorDiv.className).toBe('bg_red');
      
      greenButton.click();
      expect(colorDiv.className).toBe('bg_green');
      
      blueButton.click();
      expect(colorDiv.className).toBe('bg_blue');
    });
  });
});
