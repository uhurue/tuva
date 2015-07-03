/**
 * 
 */
var location1 = Math.floor(Math.random()*5);
var location2 = location1+1;
var location3 = location1+2;


var guess;
var hits = 0;
var trialCount = 0;

var isSunk = false;
console.log("aaaaaaaaaaaaa");
while (isSunk==false)
{
	console.log("0000000000000000");
	guess = prompt("0-6 좌표를 입력하세요");
	if(guess<0 || guess>6)
	{
		alert('좌표가 영역을 벗어났습니다.');
//		exit(0); // invalid 값은 봐준다.
	}
	else
	{
		trialCount++;
		console.log("111111111111111111111");
		if(guess==location1 || guess==location2 || guess==location3)
		{
			alert('right');
			hits++;
			if(hits==3)
			{
				isSunk = true;
			}
		}
		else
		{
			alert('wrong');
		}
	}
}
var report = "Mission data: trial=" + trialCount + " / hit rate=" + (3/trialCount);
var position = "position: " + location1 + "~" + location3;
alert(position);
alert(report);
