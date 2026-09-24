const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
global.document = dom.window.document;
global.window = dom.window;

describe('JavaScript DOM Demo Tests - Paragraph', () => {
  let html;

  beforeEach(() => {
    jest.resetModules();
    jest.useFakeTimers();

    html = fs.readFileSync(
      path.resolve(__dirname, '../index.html'),
      'utf8'
    );

    document.documentElement.innerHTML = html;

    require('../js/index.js');
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  test('New paragraph is created and appended', () => {
    const paragraphs = document.querySelectorAll('div p');

    expect(paragraphs.length).toBeGreaterThanOrEqual(1);

    const found = [...paragraphs].some(p =>
      p.textContent.includes('Go Dawgs!')
    );

    expect(found).toBe(true);
  });
});
