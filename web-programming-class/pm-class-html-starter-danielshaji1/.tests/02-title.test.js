const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('HTML Title', () => {
  let document;

  beforeAll(() => {
    const filePath = path.join(__dirname, '../index.html');
    const html = fs.readFileSync(filePath, 'utf-8');
    const dom = new JSDOM(html);
    document = dom.window.document;
  });

  test('has a <title> that is not "Document"', () => {
    const title = document.querySelector('title');
    expect(title).not.toBeNull();
    expect(title.textContent.trim().toLowerCase()).not.toBe('document');
    expect(title.textContent.trim().length).toBeGreaterThan(0);
  });
});
