const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
global.document = dom.window.document;
global.window = dom.window;

describe('JavaScript DOM Demo Tests - Link', () => {
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

  test('Link text and href are updated correctly', () => {
    const link = document.querySelector('a');

    expect(link.textContent).toBe('University of Georgia');
    expect(link.href).toBe('https://www.uga.edu/');
  });
});
