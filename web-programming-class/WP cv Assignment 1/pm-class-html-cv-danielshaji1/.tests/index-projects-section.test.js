// index-projects-section(1pt).test.js
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

function getSectionByHeading(document, headingText) {
  const h2 = Array.from(document.querySelectorAll('h2'))
    .find(h => h.textContent.toLowerCase().includes(headingText));
  assert(h2, `Missing <h2> for section "${headingText}".`);
  const section = h2.closest('section');
  assert(section, `The "${headingText}" heading must be inside a <section>.`);
  return section;
}

describe('Index.html → Projects', () => {
  let document, section;
  beforeAll(() => { document = openDocument('index.html'); section = getSectionByHeading(document, 'projects'); });

  test('At least 3 project cards with <h3>, <img> (relative + alt), and <ul>', () => {
    const cards = Array.from(section.querySelectorAll('div'));
    assert(cards.length >= 3, `Expected at least 3 project <div> cards, found ${cards.length}.`);

    cards.forEach((div, i) => {
      assert(div.querySelector('h3'), `Project card #${i + 1} is missing <h3>.`);

      const img = div.querySelector('img');
      assert(img, `Project card #${i + 1} is missing <img>.`);
      const src = img.getAttribute('src') || '';
      assert(!/^https?:\/\//.test(src), `Project image must be a relative path (got "${src}").`);
      const alt = (img.getAttribute('alt') || '').trim();
      assert(alt.length > 0, `Project card #${i + 1} image needs meaningful alt.`);

      const ul = div.querySelector('ul');
      assert(ul, `Project card #${i + 1} is missing <ul>.`);
      assert(ul.querySelectorAll('li').length > 0, `Project card #${i + 1} needs at least one <li> bullet.`);
    });
  });
});
