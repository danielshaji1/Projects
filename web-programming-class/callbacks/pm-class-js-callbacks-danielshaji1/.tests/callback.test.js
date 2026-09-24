// .tests/callback.test.js
const { runRoutine } = require("../callback");

jest.useFakeTimers();

beforeEach(() => {
  jest.clearAllTimers();
  jest.clearAllMocks();
});

test("runRoutine chains tasks sequentially (no parallel execution)", () => {
  const finalCb = jest.fn();
  const timeoutSpy = jest.spyOn(global, "setTimeout");

  runRoutine(finalCb);

  // Immediately: only the first task should schedule a timer
  expect(timeoutSpy).toHaveBeenCalledTimes(1);
  expect(timeoutSpy).toHaveBeenLastCalledWith(expect.any(Function), 1000);
  expect(finalCb).not.toHaveBeenCalled();

  // Advance to homework completion: cleanRoom should now be scheduled
  jest.advanceTimersByTime(1000);

  expect(timeoutSpy).toHaveBeenCalledTimes(2);
  expect(timeoutSpy).toHaveBeenLastCalledWith(expect.any(Function), 800);
  expect(finalCb).not.toHaveBeenCalled();

  // Advance to cleanRoom completion: playGames should now be scheduled
  jest.advanceTimersByTime(800);

  expect(timeoutSpy).toHaveBeenCalledTimes(3);
  expect(timeoutSpy).toHaveBeenLastCalledWith(expect.any(Function), 500);
  expect(finalCb).not.toHaveBeenCalled();

  // Advance to playGames completion: routine should finish
  jest.advanceTimersByTime(500);

  expect(finalCb).toHaveBeenCalledTimes(1);
  expect(finalCb).toHaveBeenCalledWith(null, [
    "Homework done",
    "Room clean",
    "Games played",
  ]);

  timeoutSpy.mockRestore();
});

