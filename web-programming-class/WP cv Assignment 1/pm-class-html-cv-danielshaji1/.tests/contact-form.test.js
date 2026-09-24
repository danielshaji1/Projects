// contact-form(4pts).test.js
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

describe('Contact.html → Form', () => {
  let document, form;
  beforeAll(() => {
    document = openDocument('contact.html');
    form = document.querySelector('form');
    assert(form, 'Missing <form>.');
  });

  test('method=POST and action=https://formsubmit.co/<id>', () => {
    assert((form.getAttribute('method') || '').toUpperCase() === 'POST', 'Form method must be POST.');
    const action = form.getAttribute('action') || '';
    assert(/^https:\/\/formsubmit\.co\/.+/.test(action),
      'Form action must be https://formsubmit.co/<your_unique_form_id>');
  });

  // Accessibility labels not required per your preference; only presence and types.
  test('Required inputs present with correct types', () => {
    const nameInput = form.querySelector('input[name="name"]');
    assert(nameInput, 'Missing <input name="name">.');
    assert((nameInput.getAttribute('type') || '').toLowerCase() === 'text',
      'Input "name" should be type="text".');
    assert(nameInput.hasAttribute('required'), 'Input "name" must be required.');

    const emailInput = form.querySelector('input[name="email"]');
    assert(emailInput, 'Missing <input name="email">.');
    assert((emailInput.getAttribute('type') || '').toLowerCase() === 'email',
      'Input "email" should be type="email".');
    assert(emailInput.hasAttribute('required'), 'Input "email" must be required.');

    const message = form.querySelector('textarea[name="message"]');
    assert(message, 'Missing message <textarea name="message">.');
  });

  test('Submit button exists', () => {
    const submit = form.querySelector('button[type="submit"], input[type="submit"]');
    assert(submit, 'Add a submit button (<button type="submit"> or <input type="submit">).');
  });
});
