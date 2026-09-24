# JavaScript DOM Demo

In this assignment, you will practice using using JavaScript to create and alter elements in the DOM

## Learning outcomes

 - Implement `setTimeout()` to change elements after a selected amount of time
 - Use `innerHTML` and `textContent` to change element text
 - Create elements with `createElement()` and append them to the DOM with `appendChild()`
 - Select elements from the DOM with `querySelector()`
 - Add event listeners with `addEventListener()` to add functionality

## Project Structure
.  
├── images/  
│   └── day.jpg  
│   └── night.jpg  
├── js/  
│   └── index.js  
├── index.html  
└── README.md  

## Instructions
1. **Set up your environment**
    - Run `npm install` to install necessary packages
    - Disregard fund/audit requests from npm if applicable
    - View the webpage with the Live Server extension

2. **Implement the following functions in index.js**
    1) Switch elements on the page on timeout
        - Switch the heading to `Rise and shine!` after 3 seconds
        - Select the image by its id and switch after timeout event of 5 seconds to `images/day.jpg`
    2) Update the link text and href
        - Select the link and change the text to `University of Georgia`
        - Make the link href point to https://www.uga.edu/
    3) Create and append a new element
        - Create a new paragraph element and make its text content `Go Dawgs!`
        - Append the paragraph to the div
    4) Build a list dynamically with javascript
        - Create nodes to hold the list, input and button elements
        - Add an event listener to the button to add new list items, clearing the input field after each item is added

3. **Run the tests locally**
    - Run `npm test` to test your functions for correct functionality
    - If needed, you can run the tests individually using `npm test timeout`, `npm test link`, `npm test paragraph`, and `npm test list`

3. **Submit your work**
    - Once the tests pass locally, push your working code to GitHub for submission and grading

## **Have fun :)**