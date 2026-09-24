// index-footer(1pt).test.js
const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

function assert(cond, msg) {
  if (!cond) throw new Error(msg);
}

function openDocument(file) {
  const html = fs.readFileSync(path.resolve(__dirname, '..', file), 'utf8');
  return new JSDOM(html).window.document;
}

describe('Index.html → Footer', () => {
  let document, footer;
  beforeAll(() => {
    document = openDocument('index.html');
    footer = document.querySelector('footer');
    assert(footer, 'Missing <footer>.');
  });

  test('Footer contains your name linking to contact.html (relative)', () => {
    const contactLink = footer.querySelector('a[href="contact.html"]');
    assert(contactLink, 'Footer should include a link to contact.html.');
    const text = (contactLink.textContent || '').trim();
    assert(text.length > 0, 'Footer contact link text cannot be empty (use your name).');
  });

  test('Footer includes a LinkedIn link (no target/rel required)', () => {
    const ln = footer.querySelector('a[href*="linkedin.com"]');
    assert(ln, 'Footer must include a LinkedIn link.');
  });
});
