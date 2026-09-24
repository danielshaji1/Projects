const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

describe('Sections: Skills and Experience', () => {
  let document;

  beforeAll(() => {
    const filePath = path.join(__dirname, '../index.html');
    const html = fs.readFileSync(filePath, 'utf-8');
    const dom = new JSDOM(html);
    document = dom.window.document;
  });

  test('properly tagged Skills and Experience sections', () => {
    const h2s = Array.from(document.querySelectorAll('h2'));

    // Skills
    const skillsHeading = h2s.find(h => h.textContent.trim().toLowerCase() === 'skills');
    expect(skillsHeading).toBeDefined();
    const ul = skillsHeading.nextElementSibling;
    expect(ul).not.toBeNull();
    expect(ul.tagName.toLowerCase()).toBe('ul');
    const lis = ul.querySelectorAll('li');
    expect(lis.length).toBeGreaterThanOrEqual(3);

    // Experience
    const experienceHeading = h2s.find(h => h.textContent.trim().toLowerCase() === 'experience');
    expect(experienceHeading).toBeDefined();
    const dl = experienceHeading.nextElementSibling;
    expect(dl).not.toBeNull();
    expect(dl.tagName.toLowerCase()).toBe('dl');
    const dts = dl.querySelectorAll('dt');
    const dds = dl.querySelectorAll('dd');
    expect(dts.length).toBeGreaterThanOrEqual(2);
    expect(dds.length).toBeGreaterThanOrEqual(2);
    expect(dts.length).toBe(dds.length);
  });
});
