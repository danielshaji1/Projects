// index-content-sections(5pts).test.js
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

describe('Index.html → Sections overview', () => {
  let document;
  beforeAll(() => { document = openDocument('index.html'); });

  test('At least five <h2> headings including education, honors, skills, experience, projects', () => {
    const h2s = Array.from(document.querySelectorAll('h2'));
    assert(h2s.length >= 5, `Expected at least 5 <h2> headings, found ${h2s.length}.`);

    const required = ['education', 'honors', 'skills', 'experience', 'projects'];
    required.forEach(key => {
      const match = h2s.some(h2 => h2.textContent.toLowerCase().includes(key));
      assert(match, `Missing an <h2> that includes the word "${key}".`);
    });
  });
});

describe('Index.html → Education', () => {
  let document, section;
  beforeAll(() => { document = openDocument('index.html'); section = getSectionByHeading(document, 'education'); });

  test('<dl> has matching <dt>/<dd> pairs', () => {
    const dl = section.querySelector('dl');
    assert(dl, 'Education section must contain a <dl>.');
    const dt = dl.querySelectorAll('dt');
    const dd = dl.querySelectorAll('dd');
    assert(dt.length > 0 && dd.length > 0, 'Education <dl> must include <dt> and <dd>.');
    assert(dt.length === dd.length, `Mismatched <dt>/<dd> count in Education: ${dt.length} vs ${dd.length}.`);
  });
});

describe('Index.html → Honors & Awards', () => {
  let document, section;
  beforeAll(() => { document = openDocument('index.html'); section = getSectionByHeading(document, 'honors'); });

  test('<dl> has matching <dt>/<dd> pairs', () => {
    const dl = section.querySelector('dl');
    assert(dl, 'Honors & Awards section must contain a <dl>.');
    const dt = dl.querySelectorAll('dt');
    const dd = dl.querySelectorAll('dd');
    assert(dt.length > 0 && dd.length > 0, 'Honors <dl> must include <dt> and <dd>.');
    assert(dt.length === dd.length, `Mismatched <dt>/<dd> count in Honors: ${dt.length} vs ${dd.length}.`);
  });
});

describe('Index.html → Skills', () => {
  let document, section;
  beforeAll(() => { document = openDocument('index.html'); section = getSectionByHeading(document, 'skills'); });

  test('<ul> with at least one <li>', () => {
    const ul = section.querySelector('ul');
    assert(ul, 'Skills must include a <ul>.');
    const lis = ul.querySelectorAll('li');
    assert(lis.length > 0, 'Add at least one <li> inside the Skills <ul>.');
  });
});

describe('Index.html → Leadership & Experience', () => {
  let document, section;
  beforeAll(() => { document = openDocument('index.html'); section = getSectionByHeading(document, 'experience'); });

  test('At least one absolute company URL in Experience', () => {
    const hasAbsolute = Array.from(section.querySelectorAll('a'))
      .some(a => /^https?:\/\//.test(a.getAttribute('href') || ''));
    assert(hasAbsolute, 'Add at least one absolute company link (https://...) in Experience.');
  });
});
