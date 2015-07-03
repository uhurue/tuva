/**
 * 
 */

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
}

var model = {
	boardSize: 7,
	numShips: 3,
	shipLength: 3,
	shipSunk: 0,
//	ships: [
//	    {locations: ["06", "16", "26"],	hits:["", "", ""]},
//	    {locations: ["24", "34", "44"],	hits:["", "", ""]},
//	    {locations: ["10", "11", "12"],	hits:["", "", ""]}
//	],
	ships: [
	    {locations: ["0", "0", "0"],	hits:["", "", ""]},
	    {locations: ["0", "0", "0"],	hits:["", "", ""]},
	    {locations: ["0", "0", "0"],	hits:["", "", ""]}
	],
	fire: function(guess) {
		for(var i=0; i<this.numShips; i++) {
			var ship = this.ships[i];
			var index = ship.locations.indexOf(guess);
			if(index>=0)
			{
				ship.hits[index] = "hit";
				view.displayHit(guess);
				view.displayMessage("명중");
				if(this.isSunk(ship)) {
					this.shipSunk++;
					view.displayMessage("전함이 침몰했습니다. (누적 " + this.shipSunk + "개)");
				}
				return true;
			}
		}
		view.displayMiss(guess);
		view.displayMessage("공격 실패");
		return false;
	},
	isSunk: function(ship) {
		for(var i=0; i<this.shipLength; i++) {
			if(ship.hits[i]!=="hit")
			{
				return false;
			}
		}
		return true;
	},
	generateShip: function() {
		var locations;
		for(var i=0; i<this.numShips; i++) {
			do {
				locations = this.generateShipLocation();
			} while(this.collision(locations)); //collision이 없는 ship 하나가 구해지면 loop 탈출
			this.ships[i].locations = locations;
		}
		console.log("ship info = " + this.ships);
	},
	generateShipLocation: function() {
		var direction = Math.floor(Math.random()*2);
		var row, col;
		if(direction===1) { //가로 전함
			row = Math.floor(Math.random()*model.boardSize);
			col = Math.floor(Math.random()*(model.boardSize-2));
		}
		else { //세로 전함
			row = Math.floor(Math.random()*(model.boardSize-2));
			col = Math.floor(Math.random()*model.boardSize);
		}
		var shipLocations = [];
		for(var i=0; i<this.shipLength; i++)
		{
			if(direction===1) //가로
			{
				shipLocations.push(row + "" + (col+i));
			}
			else //세로
			{
				shipLocations.push((row+i) + "" + col);
			}
		}
		return shipLocations;
	},
	collision: function(locations) {
		console.log("ground =" + locations);
		for(var i=0; i<model.numShips; i++) {
			var ship = model.ships[i];
			for(var j=0; j<model.shipLength; j++) {
				console.log("niddle =" + ship.locations[j]);
				if(locations.indexOf(ship.locations[j])>=0) {
					console.log("niddle found");
					return true; //충돌
				}
			}
		}
		return false;
	}
}

var controller = {
	guessesDone: 0,
	parseGuess: function(guess) {
		var charNumber = ["A", "B", "C", "D", "E", "F", "G"];
		if(guess === null || guess.length !==2) {
			alert('Error: ship position input invalid');
		}
		else {
			var row = charNumber.indexOf(guess.charAt(0));
			var col = guess.charAt(1);
			if(isNaN(row) || isNaN(col)) {
				alert('Error: ship position value invalid');
			}
			else if(row<0 || row>=model.boardSize || col<0 || col>=model.boardSize)
			{
				alert('Error: ship position out of range');
			}
			else {
				return row+col;
			}
		}
		return null;
	},
	processGuess: function(guess) {
		var location = this.parseGuess(guess);
		if(location) {
			this.guessesDone++;
			var hit = model.fire(location);
			if(hit && model.shipSunk === model.numShips) {
				view.displayMessage("trial count = " + this.guessesDone + " / All ships are fired.");
				alert('trial count = ' + this.guessesDone + ' / All ships are fired.');
			}
		}
	}
};

function init() { //핸들러를 모은 함수...
	var fireButton = document.getElementById("fireButton");
	fireButton.onclick = handleFireButton;
	var guessInput = document.getElementById("guessInput");
	guessInput.onkeypress = handleLocationKeyPress;
	
	model.generateShip(); //무작위로 전함 위치 만듦
}

function handleFireButton() {
	var guessInput = document.getElementById("guessInput");
	var guess = guessInput.value;
	controller.processGuess(guess);
	guessInput.value = "";
}
function handleLocationKeyPress(e) { //이벤트 객체를 받음
	var fireButton = document.getElementById("fireButton");
	if(e.keyCode === 13) {
		fireButton.click();
		return false; //return false를 반환해야 form이 submit하지 않는다.
	}
}

window.onload = init; //onload 콜백 함수 등록

//model.fire("53");
//model.fire("06");
//model.fire("16");
//model.fire("26");
//model.fire("34");
//model.fire("24");
//model.fire("44");
//model.fire("12");
//model.fire("11");
//model.fire("10");
//controller.processGuess("A0");
//controller.processGuess("A6");
//controller.processGuess("B6");
//controller.processGuess("C6");
//controller.processGuess("C4");
//controller.processGuess("D4");
//controller.processGuess("E4");
//controller.processGuess("B0");
//controller.processGuess("B1");
//controller.processGuess("B2");


