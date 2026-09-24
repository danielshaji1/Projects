const am_button = document.querySelector('#am');
const pm_button = document.querySelector('#pm');
const greeting = document.querySelector('#greeting');

const red_button = document.querySelector('#red');
const yellow_button = document.querySelector('#yellow');
const green_button = document.querySelector('#green');
const blue_button = document.querySelector('#blue');
const div_color = document.querySelector('#div_color');

const add_button = document.querySelector('#addBtn');
const user_input = document.querySelector('#usrInput');
const list = document.querySelector('#myUL');

am_button.addEventListener('click', function() {
    greeting.textContent = 'Good Morning!';
});

pm_button.addEventListener('click', function() {
    greeting.textContent = 'Good Night!';
});

red_button.addEventListener('click', function() {
    div_color.setAttribute('class', 'bg_red');
});

yellow_button.addEventListener('click', function() {
    div_color.setAttribute('class', 'bg_yellow');
});

green_button.addEventListener('click', function() {
    div_color.setAttribute('class', 'bg_green');
});

blue_button.addEventListener('click', function() {
    div_color.setAttribute('class', 'bg_blue');
});


add_button.addEventListener('click', function() {
    const itemText = user_input.value;

    if (itemText.trim() === '') {
        return;
    }

    const listItem = document.createElement('li');
    listItem.textContent = itemText;

    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', function() {
        listItem.remove();
    });

    listItem.appendChild(deleteButton);
    list.appendChild(listItem);
    user_input.value = '';
});

