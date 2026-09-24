// index-profile-region(2pts).test.js
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

/**
 * Profile region:
 * - Accept either an explicitly titled "Profile" section, OR
 * - The "prelude" content before the first <section> (your sample case).
 */
describe('Index.html → Profile region (top-of-page block OR a section)', () => {
  let document, regionNode;
  beforeAll(() => {
    document = openDocument('index.html');

    const sections = Array.from(document.querySelectorAll('section'));
    const firstSection = sections[0] || null;

    // Try titled "Profile" section first
    const profByHeading = sections.find(sec => {
      const h = sec.querySelector('h1,h2,h3');
      return h && /profile/i.test(h.textContent);
    });

    if (profByHeading) {
      regionNode = profByHeading;
    } else {
      // Build a virtual container from nodes before the first <section>
      const bodyChildren = Array.from(document.body.children);
      const cutIndex = firstSection ? bodyChildren.indexOf(firstSection) : bodyChildren.length;
      const preludeNodes = bodyChildren.slice(0, cutIndex);
      const container = document.createElement('div');
      preludeNodes.forEach(n => container.appendChild(n.cloneNode(true)));
      regionNode = container;
    }
  });

  test('Contains a top-level <h1> with the name', () => {
    const h1 = regionNode.querySelector('h1');
    assert(h1, 'Add a top-level <h1> with your name near the top of the page.');
    assert(h1.textContent.trim().length > 0, '<h1> must not be empty.');
  });

  test('Contains a headshot <img> with relative src and descriptive alt', () => {
    const img = regionNode.querySelector('img');
    assert(img, 'Add a headshot <img> near the top of the page.');
    const src = img.getAttribute('src') || '';
    assert(!/^https?:\/\//.test(src), `Headshot src must be relative (got "${src}").`);
    assert(src.startsWith('images/'), 'Place the headshot under images/ (e.g., images/headshot.jpg).');
    const abs = path.resolve(__dirname, '..', src);
    assert(fs.existsSync(abs), `Headshot file not found: ${src}`);
    const alt = (img.getAttribute('alt') || '').trim();
    assert(alt.length > 0, 'Headshot <img> needs descriptive alt text.');
  });

  test('Includes contact and summary paragraphs (address/phone/email + interests/goals)', () => {
    const ps = Array.from(regionNode.querySelectorAll('p'));
    assert(ps.length >= 2,
      'Include a paragraph with address/phone/email and another with your interests/goals near the top.');
    // Relaxed contact check: look for an email and some digits (phone).
    const contactP = ps.find(p => /@/.test(p.textContent));
    assert(contactP, 'Include an email address in the contact paragraph.');
    assert(/\d/.test(contactP.textContent), 'Include a phone number in the contact paragraph.');
  });
});
