// To use this tester file, you'll need to leave the view, model, controller
// objects in place, but comment out all the testing code except the parts
// you're using.  Remember you can use 
/* 
   code here 
*/
// to comment out large chunks of code.

// 이 테스트 파일을 사용하려면 아래의 뷰, 모델, 컨트롤러 객체는 모두 그대로 두세요.
// 그리고 나머지 테스트 코드만 주석으로 만들면 됩니다.
// 다음과 같이 /*와 */를 사용하면 여러 줄의 코드도 모두 주석으로 만들 수 있습니다.
/*
  주석처리되어 실행되지 않는 코드...
*/

// 뷰 객체
var view = {
	displayMessage: function(msg) {
		var messageArea = document.getElementById("messageArea");
		messageArea.innerHTML = msg;
	},

	displayHit: function(location) {
		var cell = document.getElementById(location);
		cell.setAttribute("class", "hit");
	},

	displayMiss: function(location) {
		var cell = document.getElementById(location);
		cell.setAttribute("class", "miss");
	}

}; 

/*
// 뷰 테스트 코드
view.displayMiss("00");
view.displayHit("34");
view.displayMiss("55");
view.displayHit("12");
view.displayMiss("25");
view.displayHit("26");

view.displayMessage("똑똑, 이 메시지가 보이나요?");
*/

// 모델 객체
var model = {
	boardSize: 7,
	numShips: 3,
	shipLength: 3,
	shipsSunk: 0,
	
	ships: [
		{ locations: ["06", "16", "26"], hits: ["", "", ""] },
		{ locations: ["24", "34", "44"], hits: ["", "", ""] },
		{ locations: ["10", "11", "12"], hits: ["", "", ""] }
	],

	fire: function(guess) {
		for (var i = 0; i < this.numShips; i++) {
			var ship = this.ships[i];
			var index = ship.locations.indexOf(guess);

			if (index >= 0) {
				ship.hits[index] = "hit";
				view.displayHit(guess);
				view.displayMessage("HIT!");

				if (this.isSunk(ship)) {
					view.displayMessage("전함이 격침되었습니다!");
					this.shipsSunk++;
				}
				return true;
			}
		}
		view.displayMiss(guess);
		view.displayMessage("실패했습니다.");
		return false;
	},

	isSunk: function(ship) {
		for (var i = 0; i < this.shipLength; i++)  {
			if (ship.hits[i] !== "hit") {
				return false;
			}
		}
	    return true;
	},

	generateShipLocations: function() {
		var locations;
		for (var i = 0; i < this.numShips; i++) {
			do {
				locations = this.generateShip();
			} while (this.collision(locations));
			this.ships[i].locations = locations;
		}
		console.log("전함 배열: ");
		console.log(this.ships);
	},

	generateShip: function() {
		var direction = Math.floor(Math.random() * 2);
		var row, col;

		if (direction === 1) { // 수평
			row = Math.floor(Math.random() * this.boardSize);
			col = Math.floor(Math.random() * (this.boardSize - this.shipLength));
		} else { // 수직
			row = Math.floor(Math.random() * (this.boardSize - this.shipLength));
			col = Math.floor(Math.random() * this.boardSize);
		}

		var newShipLocations = [];
		for (var i = 0; i < this.shipLength; i++) {
			if (direction === 1) {
				newShipLocations.push(row + "" + (col + i));
			} else {
				newShipLocations.push((row + i) + "" + col);
			}
		}
		return newShipLocations;
	},

	collision: function(locations) {
		for (var i = 0; i < this.numShips; i++) {
			var ship = this.ships[i];
			for (var j = 0; j < locations.length; j++) {
				if (ship.locations.indexOf(locations[j]) >= 0) {
					return true;
				}
			}
		}
		return false;
	}
	
}; 


// 모델 테스트 코드
model.fire("53"); // 실패

model.fire("06"); // 적중
model.fire("16"); // 적중
model.fire("26"); // 적중

model.fire("34"); // 적중
model.fire("24"); // 적중
model.fire("44"); // 적중

model.fire("12"); // 적중
model.fire("11"); // 적중
model.fire("10"); // 적중

// parseGuess() 함수
function parseGuess(guess) {
	var alphabet = ["A", "B", "C", "D", "E", "F", "G"];

	if (guess === null || guess.length !== 2) {
		alert("게임판의 문자와 숫자를 이용해 위치를 입력하십시오.");
	} else {
		var row = alphabet.indexOf(guess.charAt(0));
		var column = guess.charAt(1);
		
		if (isNaN(row) || isNaN(column)) {
			alert("입력값이 올바르지 않습니다.");
		} else if (row < 0 || row >= model.boardSize ||
		           column < 0 || column >= model.boardSize) {
			alert("입력값이 게임판을 벗어났습니다!");
		} else {
			return row + column;
		}
	}
	return null;
}

/*
// parseGuess() 함수 테스트 코드
console.log("parseGuess() 테스트");
console.log(parseGuess("A0"));
console.log(parseGuess("B6"));
console.log(parseGuess("G3"));
console.log(parseGuess("H0")); // 잘못된 입력값
console.log(parseGuess("A7")); // 잘못된 입력값
*/


// 컨트롤러 객체
var controller = {
	guesses: 0,

	processGuess: function(guess) {
		var location = parseGuess(guess);
		if (location) {
			this.guesses++;
			var hit = model.fire(location);
			if (hit && model.shipsSunk === model.numShips) {
					view.displayMessage("여러분은 " + this.guesses + "번 추측해 전함을 모두 격침시켰습니다.");
			}
		}
	}
}
\
/*
// 컨트롤러 테스트 코드
// 게임판에서 세 척의 전함과 하나의 실패가 나타나야 합니다.
// 그리고 "10번 추측해 전함을 모두 격침시켰습니다!"라는 메시지를 볼 수 있어야 합니다.
controller.processGuess("A0"); // 실패

controller.processGuess("A6"); // 적중
controller.processGuess("B6"); // 적중
controller.processGuess("C6"); // 적중

controller.processGuess("C4"); // 적중
controller.processGuess("D4"); // 적중
controller.processGuess("E4"); // 적중

controller.processGuess("B0"); // 적중
controller.processGuess("B1"); // 적중
controller.processGuess("B2"); // 적중
*/