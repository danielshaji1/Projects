const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');
const validator = require('html-validator');

describe('HTML Boilerplate', () => {
  let dom;
  let document;
  let htmlContent;

  beforeAll(() => {
    const filePath = path.join(__dirname, '../index.html');
    htmlContent = fs.readFileSync(filePath, 'utf-8');
    dom = new JSDOM(htmlContent);
    document = dom.window.document;
  });

  test('has valid HTML5 boilerplate', () => {
    expect(/^<!doctype html>/i.test(dom.serialize())).toBe(true);
    expect(document.documentElement.lang).toBe('en');
    expect(document.querySelector('meta[charset="UTF-8"]').getAttribute('charset')).toBe('UTF-8');
    expect(document.querySelector('meta[name="viewport"]').getAttribute('name')).toBe('viewport');
  });

  test('has valid HTML (no missing closing tags)', async () => {
    const options = {
      data: htmlContent,
      format: 'text',
    };

    const result = await validator(options);

    if (result.includes('Error')) {
      console.error('\nHTML validation errors:\n', result);
    }
    expect(result.includes('Error')).toBe(false);
  });
});
