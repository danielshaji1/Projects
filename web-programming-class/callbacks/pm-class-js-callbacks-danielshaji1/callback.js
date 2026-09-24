// callback.js

function doHomework(callback) {
  setTimeout(() => {
    callback(null, "Homework done");
  }, 1000);
}

function cleanRoom(callback) {
  setTimeout(() => {
    callback(null, "Room clean");
  }, 800);
}

function playGames(callback) {
  setTimeout(() => {
    callback(null, "Games played");
  }, 500);
}

function runRoutine(callback) {
  doHomework(function (err, homeworkResult) {
    if (err) {
      return callback(err,null);
    }
  cleanRoom(function (err, cleanRoomResult) {
    if (err) {
      return callback(err,null);
    }
  playGames(function (err, playGamesResult) {
    if (err) {
      return callback(err,null);
    }
    return callback(null, ["Homework done", "Room clean", "Games played"]);
  }); 
  });
  });
}

module.exports = {
  doHomework,
  cleanRoom,
  playGames,
  runRoutine,
};

// Demo when run directly
if (require.main === module) {
  console.log("\n--- Running Routine Demo ---");
  runRoutine(function (err, results) {
    if (err) {
      console.log("Routine failed:", err);
    } else {
      console.log("Routine finished:", results);
    }
  });
}
