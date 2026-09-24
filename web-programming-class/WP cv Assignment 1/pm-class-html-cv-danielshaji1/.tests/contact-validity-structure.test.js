// contact-validity-structure(2pts).test.js
const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');
const validator = require('html-validator');

function assert(cond, msg) {
  if (!cond) throw new Error(msg);
}

function openDocument(file) {
  const html = fs.readFileSync(path.resolve(__dirname, '..', file), 'utf8');
  return new JSDOM(html).window.document;
}

describe('Contact.html → HTML validity', () => {
  test('contact.html is valid HTML5 (W3C)', async () => {
    const html = fs.readFileSync(path.resolve(__dirname, '..', 'contact.html'), 'utf8');
    const result = await validator({ data: html, format: 'json' });
    const errors = (result.messages || []).filter(m => m.type === 'error');
    if (errors.length) {
      const printable = errors
        .map(e => `• ${e.message}${e.extract ? `\n   ↳ ${e.extract}` : ''}`)
        .join('\n');
      throw new Error(`HTML5 validation errors found in contact.html:\n${printable}`);
    }
    expect(errors.length).toBe(0);
  });
});

/**
 * Profile area on contact page:
 * Accept the "prelude" (content BEFORE the first <section>).
 * We look for an <h1> and for a paragraph that actually contains contact info
 * (must include an email and some digits for phone).
 */
describe('Contact.html → Profile area (top-of-page prelude)', () => {
  let document, regionNode;

  beforeAll(() => {
    document = openDocument('contact.html');

    // Build a virtual container from nodes BEFORE the first <section>
    const bodyChildren = Array.from(document.body.children);
    const firstSection = document.querySelector('section');
    const cutIndex = firstSection ? bodyChildren.indexOf(firstSection) : bodyChildren.length;
    const preludeNodes = bodyChildren.slice(0, cutIndex);

    const container = document.createElement('div');
    preludeNodes.forEach(n => container.appendChild(n.cloneNode(true)));
    regionNode = container;
  });

  test('Has an <h1> (name) and a contact paragraph (address/phone/email)', () => {
    const h1 = regionNode.querySelector('h1');
    assert(h1, 'Add a top-of-page <h1> with your name on the contact page.');
    assert(h1.textContent.trim().length > 0, '<h1> must not be empty.');

    // Find the paragraph that actually looks like contact info
    const ps = Array.from(regionNode.querySelectorAll('p'));
    const contactP = ps.find(p => /@/.test(p.textContent) && /\d/.test(p.textContent));
    assert(contactP,
      'Add a paragraph near the top with address, phone number, and email (we look for "@" and digits).');
  });
});
