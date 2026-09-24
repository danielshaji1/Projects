const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('Main Heading', () => {
  let document;

  beforeAll(() => {
    const filePath = path.join(__dirname, '../index.html');
    const html = fs.readFileSync(filePath, 'utf-8');
    const dom = new JSDOM(html);
    document = dom.window.document;
  });

  test('has an <h1> heading', () => {
    const h1 = document.querySelector('h1');
    expect(h1).not.toBeNull();
    expect(h1.textContent.trim().length).toBeGreaterThan(0);
  });
});
