function one(numbers) {
  return numbers.filter((number) => number % 2 === 0);
}

function two(numbers) {
  return numbers.filter((number) => number % 2 !== 0);
}

function three(users) {
  return users.filter((user) => user.age >= 18);
}

function four(users, type) {
  return users.filter((user) => user.type === type);
}

function five(numbers) {
  return numbers.map((number) => number * 2);
}

function six(numbers) {
  return numbers.map((number) => number * number);
}

function seven(users) {
  return users.map((user) => user.name.toUpperCase());
}

module.exports = {
  one,
  two,
  three,
  four,
  five,
  six,
  seven
};