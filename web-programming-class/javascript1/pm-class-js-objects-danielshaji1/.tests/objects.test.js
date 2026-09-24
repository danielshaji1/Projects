const fs = require("fs");
const path = require("path");

const filePath = path.join(__dirname, "../backpack.js");
const source = fs.readFileSync(filePath, "utf8");

const {
  backpack,
  morning,
  afternoon,
  evening,
  night
} = require("../backpack");

describe("Javascript Object Assignment", () => {

  /* ===========================
     RUNTIME BEHAVIOR TESTS
     =========================== */

  test("morning adds charger to backpack", () => {
    morning();
    expect(backpack.charger).toBe("USB-C");
  });

  test("afternoon removes snack from backpack", () => {
    afternoon();
    expect(backpack.snack).toBeUndefined();
  });

  test("evening returns writingUtensil and laptop", () => {
    const result = evening();

    expect(result).toEqual({
      linus: "Pen",
      laptop: "MacBook"
    });
  });

  test("night returns bag without laptop", () => {
    const bag = night();

    expect(bag).toEqual(
      expect.objectContaining({
        writingUtensil: "Pen",
        notebook: "Moleskine",
        waterBottle: "Owala"
      })
    );

    expect(bag.laptop).toBeUndefined();
  });

  /* ===========================
     SOURCE CODE SYNTAX TESTS
     =========================== */

  test("uses dot notation to add charger", () => {
    expect(source).toMatch(/backpack\.charger\s*=\s*["']USB-C["']/);
  });

  test("uses delete keyword to remove snack", () => {
    expect(source).toMatch(/delete\s+backpack\.snack/);
  });

  test("uses direct property access for writingUtensil", () => {
    expect(source).toMatch(
      /linus\s*=\s*backpack\.writingUtensil/
    );
  });

  // test("uses object destructuring to extract laptop", () => {
  //   expect(source).toMatch(
  //     /const\s*\{\s*laptop\s*\}\s*=\s*backpack/
  //   );
  // });

  test("uses rest destructuring to create bag", () => {
    expect(source).toMatch(
      /const\s*\{\s*laptop\s*,\s*\.\.\.bag\s*\}\s*=\s*backpack/
    );
  });

});
