// index-validity-structure(3pts).test.js
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

describe('Index.html → HTML validity', () => {
  test('index.html is valid HTML5 (W3C)', async () => {
    const html = fs.readFileSync(path.resolve(__dirname, '..', 'index.html'), 'utf8');
    const result = await validator({ data: html, format: 'json' });

    const errors = (result.messages || []).filter(m => m.type === 'error');
    if (errors.length) {
      const printable = errors
        .map(e => `• ${e.message}${e.extract ? `\n   ↳ ${e.extract}` : ''}`)
        .join('\n');
      throw new Error(`HTML5 validation errors found in index.html:\n${printable}`);
    }
    expect(errors.length).toBe(0);
  });
});

describe('Index.html → Document setup', () => {
  let document;
  beforeAll(() => { document = openDocument('index.html'); });

  test('Has exactly one <h1> and sane heading hierarchy (no jumps)', () => {
    const h1s = document.querySelectorAll('h1');
    assert(h1s.length === 1, `Expected exactly one <h1>, found ${h1s.length}.`);

    const headings = Array.from(document.querySelectorAll('h1,h2,h3'));
    headings.forEach((el, i) => {
      if (i === 0) return;
      const prevLevel = Number(headings[i - 1].tagName.slice(1));
      const level = Number(el.tagName.slice(1));
      assert(level - prevLevel <= 1,
        `Heading level jumped from <h${prevLevel}> to <h${level}>. Do not skip levels.`);
    });
  });

  test('<title> must equal resume.json "name"', () => {
    const title = document.querySelector('title');
    assert(title, 'Missing <title> in <head>.');

    const resumePath = path.resolve(__dirname, '..', 'resume.json');
    assert(fs.existsSync(resumePath), 'resume.json missing.');
    const resume = JSON.parse(fs.readFileSync(resumePath, 'utf8'));
    assert(resume.name, 'resume.json must include a "name" field.');

    assert(title.textContent.trim() === resume.name,
      `Document title must equal resume.name ("${resume.name}"). Found "${title.textContent.trim()}".`);
  });
});
