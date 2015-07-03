/**
 * 
 */

function makeTimer()
{
	var startTime;
	var curTime;;
	return function()
	{
		curTime = new Date();
		if(startTime) //이미 start time이 정의돼 있다.
		{
			alert('end time:'+curTime.getTime());
			return (curTime.getTime()-startTime); //milisec since 1970
		}
		else
		{
			startTime = curTime.getTime(); //milisec since 1970
			alert('begin time:'+startTime);
			return null;
		}
	}
}
