// contact-navigation(1pt).test.js
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

describe('Contact.html → Navigation', () => {
  let document;
  beforeAll(() => { document = openDocument('contact.html'); });

  test('nav contains name <p> and a relative link to index.html labeled "About Your Name"', () => {
    const nav = document.querySelector('nav');
    assert(nav, 'Missing <nav>.');

    const namePara = nav.querySelector('p');
    assert(namePara && namePara.textContent.trim().length > 0, 'In <nav>, include a <p> with your name.');

    const link = nav.querySelector('a[href="index.html"]');
    assert(link, 'Add a link in <nav> to index.html.');
    assert(/about/i.test(link.textContent),
      `The nav link text should include "About Your Name" (found "${link.textContent}").`);
  });
});
