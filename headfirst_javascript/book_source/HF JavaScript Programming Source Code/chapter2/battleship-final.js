var randomLoc = Math.floor(Math.random() * 5);
var location1 = randomLoc;
var location2 = location1 + 1;
var location3 = location1 + 2;
var guess;
var hits = 0;
var guesses = 0;
var isSunk = false;

while (isSunk == false) {
	guess = prompt("준비, 조준, 발사! (0에서 6까지 중 추측한 숫자를 입력하세요):");
	if (guess < 0 || guess > 6) {
		alert("셀 번호가 올바르지 않습니다!");
	} else {
		guesses = guesses + 1;
		if (guess == location1 || guess == location2 || guess == location3) {
			alert("명중!");
			hits = hits + 1;
			if (hits == 3) {
				isSunk = true;
				alert("전함 침몰!");
			}
		} else {
			alert("실패");
		}
	}
}
var stats = "여러분은 전함을 격침시키기 위해 " + guesses + "번 발사했습니다." +
            "따라서 명중률은 " + (3/guesses) + "입니다";
alert(stats);
