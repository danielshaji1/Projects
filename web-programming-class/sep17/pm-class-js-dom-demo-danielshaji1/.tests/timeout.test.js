const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
global.document = dom.window.document;
global.window = dom.window;

describe('JavaScript DOM Demo Tests - Heading and Image', () => {
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

  test('Heading changes after 3 seconds', () => {
    const heading = document.querySelector('h1');

    expect(heading.textContent).toBe('zzzzz...');

    jest.advanceTimersByTime(3000);

    expect(heading.textContent).toBe('Rise and shine!');
  });

  test('Image src changes after 5 seconds', () => {
    const image = document.querySelector('#main');

    expect(image.getAttribute('src')).toBe('images/night.jpg');

    jest.advanceTimersByTime(5000);

    expect(image.getAttribute('src')).toBe('images/day.jpg');
  });
});
