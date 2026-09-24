// contact-footer(1pt).test.js
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

describe('Contact.html → Footer', () => {
  let document, footer;
  beforeAll(() => {
    document = openDocument('contact.html');
    footer = document.querySelector('footer');
    assert(footer, 'Missing <footer>.');
  });

  test('Footer includes link back to index.html (relative)', () => {
    const nameLink = footer.querySelector('a[href="index.html"]');
    assert(nameLink, 'Footer should include a link back to index.html.');
    assert(nameLink.textContent.trim().length > 0, 'Footer name link text cannot be empty.');
  });

  test('Footer includes a LinkedIn link (no target/rel required)', () => {
    const ln = footer.querySelector('a[href*="linkedin.com"]');
    assert(ln, 'Footer must include a LinkedIn link.');
  });
});
