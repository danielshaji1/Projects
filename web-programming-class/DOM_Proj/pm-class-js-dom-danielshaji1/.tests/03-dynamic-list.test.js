const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('Part 3: Create a dynamic list', () => {
  let dom;
  let document;

  beforeEach(() => {
    const html = fs.readFileSync(path.join(__dirname, '../index.html'), 'utf8');
    dom = new JSDOM(html, { runScripts: 'outside-only' });
    document = dom.window.document;

    const js = fs.readFileSync(path.join(__dirname, '../js/dom_js.js'), 'utf8');
    dom.window.eval(js);
  });

  describe('List functionality', () => {

    test('adds item to list when Add button is clicked with valid input', () => {
      const input = document.getElementById('usrInput');
      const addButton = document.getElementById('addBtn');
      const list = document.getElementById('myUL');

      // Initial state
      expect(list.children.length).toBe(0);

      input.value = 'Test Item';
      addButton.click();

      const listItems = list.querySelectorAll('li');
      expect(listItems.length).toBe(1);

      const li = listItems[0];

      // Verify real structure: text node + button
      expect(li.firstChild.textContent).toBe('Test Item');

      const deleteButton = li.querySelector('button');
      expect(deleteButton).toBeTruthy();
      expect(deleteButton.textContent).toBe('Delete');
    });

    test('does not add item to list when input is empty', () => {
      const input = document.getElementById('usrInput');
      const addButton = document.getElementById('addBtn');
      const list = document.getElementById('myUL');

      // Initial state
      expect(list.children.length).toBe(0);

      input.value = '';
      addButton.click();

      expect(list.children.length).toBe(0);
    });

    test('clears input field after adding item', () => {
      const input = document.getElementById('usrInput');
      const addButton = document.getElementById('addBtn');
      const list = document.getElementById('myUL');

      // Initial state
      expect(list.children.length).toBe(0);

      input.value = 'Test Item';
      addButton.click();

      expect(input.value).toBe('');
    });

    test('delete button removes only its own list item', () => {
      const input = document.getElementById('usrInput');
      const addButton = document.getElementById('addBtn');
      const list = document.getElementById('myUL');

      // Initial state
      expect(list.children.length).toBe(0);

      // Add two items
      input.value = 'First Item';
      addButton.click();

      input.value = 'Second Item';
      addButton.click();

      let listItems = list.querySelectorAll('li');
      expect(listItems.length).toBe(2);

      const firstLi = listItems[0];
      const secondLi = listItems[1];

      const firstDeleteButton = firstLi.querySelector('button');
      firstDeleteButton.click();

      listItems = list.querySelectorAll('li');
      expect(listItems.length).toBe(1);

      // Ensure ONLY the first was removed
      expect(listItems[0].firstChild.textContent).toBe('Second Item');
    });

    test('can add multiple items and delete them individually', () => {
      const input = document.getElementById('usrInput');
      const addButton = document.getElementById('addBtn');
      const list = document.getElementById('myUL');

      // Initial state
      expect(list.children.length).toBe(0);

      input.value = 'Item A';
      addButton.click();

      input.value = 'Item B';
      addButton.click();

      input.value = 'Item C';
      addButton.click();

      let listItems = list.querySelectorAll('li');
      expect(listItems.length).toBe(3);

      // Delete middle item
      const middleDeleteButton = listItems[1].querySelector('button');
      middleDeleteButton.click();

      listItems = list.querySelectorAll('li');
      expect(listItems.length).toBe(2);

      expect(listItems[0].firstChild.textContent).toBe('Item A');
      expect(listItems[1].firstChild.textContent).toBe('Item C');
    });

  });
});
