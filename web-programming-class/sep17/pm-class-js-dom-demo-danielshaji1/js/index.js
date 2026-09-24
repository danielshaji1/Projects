const heading = document.querySelector('h1');

setTimeout(() => {
  heading.textContent = 'Rise and shine!';
}, 3000);

const image = document.querySelector('#main');

setTimeout(() => {
  image.src = 'images/day.jpg';
}, 5000);

const link = document.querySelector('a');
link.textContent = 'University of Georgia';
link.href = 'https://www.uga.edu/';

const contentDiv = document.querySelector('div');
const paragraph = document.createElement('p');
paragraph.textContent = 'Go Dawgs!';
contentDiv.appendChild(paragraph);

const list = document.querySelector('ul');
const input = document.querySelector('#item');
const button = document.querySelector('button');

button.addEventListener('click', () => {
  const itemText = input.value.trim();

  if (!itemText) {
    return;
  }

  const listItem = document.createElement('li');
  listItem.textContent = itemText;
  list.appendChild(listItem);
  input.value = '';
});
