const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const dom = new JSDOM('<!DOCTYPE html><html><body></body></html>');
global.document = dom.window.document;
global.window = dom.window;

describe('JavaScript DOM Demo Tests - List Items', () => {
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

  test('Clicking button adds new list item', () => {
    const input = document.querySelector('#item');
    const button = document.querySelector('button');
    const list = document.querySelector('ul');

    input.value = 'Test Item';

    button.click();

    const items = list.querySelectorAll('li');

    expect(items.length).toBe(1);
    expect(items[0].textContent).toBe('Test Item');
  });

  test('Input clears after adding item', () => {
    const input = document.querySelector('#item');
    const button = document.querySelector('button');

    input.value = 'Another Item';
    button.click();

    expect(input.value).toBe('');
  });
});
