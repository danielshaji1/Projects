const fs = require('fs');
const validator = require('html-validator');

test('index.html is valid HTML5', async () => {
  const html = fs.readFileSync('index.html', 'utf8');
  const result = await validator({ data: html, format: 'json' }); // use JSON for parsing

  if (result.messages.length > 0) {
    console.warn('HTML validation messages:', result.messages);
  }

  // Fail only if there are errors (not warnings)
  const errors = result.messages.filter(msg => msg.type === 'error');
  expect(errors).toHaveLength(0);
});

