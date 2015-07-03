scoops = 5;
while (scoops > 0) {
	document.write("한 숟가락 더!<br>");
	if (scoops < 3) {
		alert("아이스크림이 얼마 남지 않았어요!");
	} else if (scoops >= 5) {
		alert("어서 드세요. 아이스크림 녹겠어요!");
	}
	scoops = scoops - 1;
}
document.write("아이스크림 없는 인생은 인생이 아닙니다.");
