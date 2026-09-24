// __tests__/box-model.test.js
const fs = require("fs");
const path = require("path");

// ---- Helpers ---------------------------------------------------------------

// Parse a CSS rule block like ".middle { ... }"
function getRuleBlock(css, selector) {
  const re = new RegExp(`${selector}\\s*{[^}]*}`, "m");
  const match = css.match(re);
  return match ? match[0] : null;
}

// Extract the value (string) of a property inside a rule block
function getPropValue(ruleBlock, prop) {
  if (!ruleBlock) return null;
  // allow optional trailing semicolon; tolerate whitespace
  const re = new RegExp(`${prop}\\s*:\\s*([^;]+);?`, "i");
  const m = ruleBlock.match(re);
  return m ? m[1].trim() : null;
}

// Normalize zero values like "0", "0px", "0rem", "0%"
function isZero(v) {
  return v != null && /^0(\s*(px|em|rem|vh|vw|vmin|vmax|%)?)?$/i.test(v.trim());
}

// Validate that there is a horizontal left offset of `px` either via
// explicit margin-left OR via a margin shorthand where top=0, bottom=0, left=px.
// Accepts 2/3/4-value shorthands, rejects 1-value unless it is zero.
function hasValidLeftMargin(ruleBlock, px) {
  const want = `${px}px`;

  // Case 1: explicit margin-left
  const ml = getPropValue(ruleBlock, "margin-left");
  if (ml && ml.replace(/\s+/g, "") === want) return true;

  // Case 2: margin shorthand
  const margin = getPropValue(ruleBlock, "margin");
  if (!margin) return false;

  const parts = margin.trim().replace(/\s+/g, " ").split(" ");

  if (parts.length === 1) {
    // margin: A; -> all sides = A (must not accept unless zero)
    return false;
  }

  if (parts.length === 2) {
    // margin: V H; -> top=bottom=V, left=right=H
    const [V, H] = parts;
    return isZero(V) && H === want;
  }

  if (parts.length === 3) {
    // margin: T H B; -> top=T, left=right=H, bottom=B
    const [T, H, B] = parts;
    return isZero(T) && isZero(B) && H === want;
  }

  if (parts.length >= 4) {
    // margin: T R B L;
    const [T, R, B, L] = parts;
    return isZero(T) && isZero(B) && L === want;
  }

  return false;
}

// Ensure there is NO vertical shift (top/bottom must be zero if present).
// Also validates shorthand margins to ensure top/bottom are zero.
function hasNoVerticalShift(ruleBlock) {
  if (!ruleBlock) return false;

  // Direct longhand checks
  const mt = getPropValue(ruleBlock, "margin-top");
  if (mt && !isZero(mt)) return false;

  const mb = getPropValue(ruleBlock, "margin-bottom");
  if (mb && !isZero(mb)) return false;

  // Shorthand check
  const margin = getPropValue(ruleBlock, "margin");
  if (margin) {
    const parts = margin.trim().replace(/\s+/g, " ").split(" ");
    if (parts.length === 1) {
      // margin: A; -> top/bottom=A
      return isZero(parts[0]);
    }
    if (parts.length === 2) {
      // V H
      const [V] = parts;
      return isZero(V);
    }
    if (parts.length === 3) {
      // T H B
      const [T, , B] = parts;
      return isZero(T) && isZero(B);
    }
    if (parts.length >= 4) {
      // T R B L
      const [T, , B] = parts;
      return isZero(T) && isZero(B);
    }
  }

  return true;
}

// ---- Tests ----------------------------------------------------------------

describe("CSS Box Model Tests", () => {
  let css;

  beforeAll(() => {
    const cssPath = path.join(__dirname, "../css/style_boxes.css");
    css = fs.readFileSync(cssPath, "utf8");
  });

  test("has .middle and .bottom rules", () => {
    expect(getRuleBlock(css, "\\.middle")).toBeTruthy();
    expect(getRuleBlock(css, "\\.bottom")).toBeTruthy();
  });

  test(".middle uses left offset of 160px via margin-left or valid shorthand", () => {
    const middle = getRuleBlock(css, "\\.middle");
    expect(hasValidLeftMargin(middle, 160)).toBe(true);
  });

  test(".bottom uses left offset of 300px via margin-left or valid shorthand", () => {
    const bottom = getRuleBlock(css, "\\.bottom");
    expect(hasValidLeftMargin(bottom, 300)).toBe(true);
  });

  test(".middle has no vertical shift (top/bottom must be zero)", () => {
    const middle = getRuleBlock(css, "\\.middle");
    expect(hasNoVerticalShift(middle)).toBe(true);
  });

  test(".bottom has no vertical shift (top/bottom must be zero)", () => {
    const bottom = getRuleBlock(css, "\\.bottom");
    expect(hasNoVerticalShift(bottom)).toBe(true);
  });

  // Explicitly forbid single-value margins like "margin: 160px;" / "margin: 300px;"
  test("disallow single-value margins that move all sides equally", () => {
    const middle = getRuleBlock(css, "\\.middle") || "";
    const bottom = getRuleBlock(css, "\\.bottom") || "";
    const badSingleValue160 = /margin\s*:\s*160px\s*;?/i;
    const badSingleValue300 = /margin\s*:\s*300px\s*;?/i;
    expect(badSingleValue160.test(middle)).toBe(false);
    expect(badSingleValue300.test(bottom)).toBe(false);
  });
});