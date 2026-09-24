const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('Part 1: Greeting', () => {
  let dom;
  let document;

  beforeEach(() => {
    const htmlFilePath = path.join(__dirname, '../index.html');
    const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

    dom = new JSDOM(htmlContent, { runScripts: 'outside-only' });
    document = dom.window.document;

    const jsFilePath = path.join(__dirname, '../js/dom_js.js');
    const jsContent = fs.readFileSync(jsFilePath, 'utf-8');
    dom.window.eval(jsContent);
  });

  describe('Greeting functionality', () => {

    test('displays "Good Morning!" when AM button is clicked', () => {
      const amButton = document.getElementById('am');
      const greetingElement = document.getElementById('greeting');

      // Initial state must be empty
      expect(greetingElement.textContent).toBe('');

      amButton.click();

      expect(greetingElement.textContent).toBe('Good Morning!');
    });

    test('displays "Good Night!" when PM button is clicked', () => {
      const pmButton = document.getElementById('pm');
      const greetingElement = document.getElementById('greeting');

      // Initial state
      expect(greetingElement.textContent).toBe('');

      pmButton.click();

      expect(greetingElement.textContent).toBe('Good Night!');
    });

    test('greeting message changes from AM to PM correctly', () => {
      const amButton = document.getElementById('am');
      const pmButton = document.getElementById('pm');
      const greetingElement = document.getElementById('greeting');

      // Initial state
      expect(greetingElement.textContent).toBe('');

      amButton.click();
      expect(greetingElement.textContent).toBe('Good Morning!');

      pmButton.click();
      expect(greetingElement.textContent).toBe('Good Night!');
    });

  });
});
