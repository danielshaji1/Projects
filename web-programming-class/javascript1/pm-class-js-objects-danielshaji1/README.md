# JavaScript Objects

This assignment serves as practice for JavaScript object usage. Students will learn to add, delete, and destructure objects.

## Learning outcomes

 - Implement JS object modifiers such as add/delete/destructure
 - Experiment with different object destructure syntaxes

## Project Structure
.
├── backpack.js

## Goal

Given the backpack object and function outlines, fill in the functions as specified to use and alter the object properties.

## Instructions (Part 1)
1. **Set up your environment**
    - Run `npm install` to install necessary packages
    - Disregard fund/audit requests from npm if applicable

2. **Implement the following functions in backpack.js**
    1. Morning
        - Don't forget your charger! Add a `charger` property to backpack with value `"USB-C"`
    2. Afternoon
        - Getting hungry? Delete the `snack` property from backpack
    3. Evening
        - Your friend Linus needs something to write notes with, create a variable `linus` and assign it the value of `writingUtensil` using dot notation
        - Get your laptop out for your own notes, create a `laptop` variable with the value of the laptop property in the backpack object using bracket notation
    4. Night
        - Your new backpack arrived! Add all properties besides `laptop` in a new object `bag` using rest (...) syntax

3. **Run the tests locally**
    - Run `npm test` to test for correct functionality

4. **Submit your work**
    - Once complete, push your working code to github for grading