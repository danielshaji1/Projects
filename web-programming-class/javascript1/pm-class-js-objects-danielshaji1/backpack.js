const backpack = {
  laptop: "MacBook",
  writingUtensil: "Pen",
  notebook: "Moleskine",
  waterBottle: "Owala",
  snack: "Granola bar"
};

function morning() {
  // Fill in code here
  backpack.charger = "USB-C";
}

function afternoon() {
  // Fill in code here
  delete backpack.snack;
}

function evening() {
  // Fill in code here
  let linus = backpack.writingUtensil;
  let laptop = backpack.laptop;
  return {linus, laptop};
}

function night() {
  // Fill in code here
  const { laptop, ...bag } = backpack;
  return bag;
}

module.exports = {
  backpack,
  morning,
  afternoon,
  evening,
  night
};
