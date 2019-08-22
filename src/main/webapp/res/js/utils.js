// 자주 쓰는 함수들을 모아놓은 클래스(싱글톤 객체 방식)
function Utils() 
{
}

Utils.getInstance = function()
{
	if (!this.instance) 
	{
		this.instance = new Utils();
	}
	return this.instance;
}

// url에 있는 parameter값을 parameter이름으로 불러오는 함수.
// 참조한 사이트: https://stackoverflow.com/a/53824889
Utils.prototype.getParameterByName = function(name) 
{
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
		.exec(location.search);
	return results === null ? "" : decodeURI(results[1].replace(
		/\+/g, " "));
}

// AJAX 요청을 하는 함수
// method : 'POST', 'GET'...
Utils.prototype.requestAjax = function(method, url, loadListener, object) {
	var oReq = new XMLHttpRequest();
	oReq.addEventListener("load", loadListener);
	oReq.open(method, url);
	if (object != null && object != undefined) {
		oReq.setRequestHeader("Content-Type", "application/json");
		var data = JSON.stringify(object);
		oReq.send(data);
	}
	else {
		oReq.send();
	}
}

// 사이트의 맨 위로 스크롤 하는 함수
// 참조한 사이트: https://developer.mozilla.org/ko/docs/Web/API/Window/scrollTo
Utils.prototype.scrollToTop = function(){
	window.scrollTo({
		top: 0,
		left: 0,
		behavior: 'smooth'
	});
}

// 이전 사이트로 이동하는 함수
Utils.prototype.tothePreviousSite = function() {
	history.go(-1);
}

// click 리스너를 등록하는 함수
Utils.prototype.registerClickListener = function(object, onclickListener) {
	if (typeof (object) == "string") {
		document.querySelector(object).addEventListener("click", onclickListener);
	}
	else {
		object.addEventListener("click", onclickListener);
	}
}

// element가 보여질지 말지 설정하는 함수
Utils.prototype.setVisibility = function(object, isShown) {
	var element;
	if (typeof (object) == "string") {
		element = document.querySelector(object);
	}
	else {
		element = object;
	}

	if (isShown) {
		element.style.display = "";
	} else {
		element.style.display = "none";
	}
}

// html 요소에 클래스이름을 추가하는 함수
// 요소에 이미 추가하려는 클래스 이름이 있어도 정상 동작한다.
// 참조 : 리뷰어 '배돌이'님의 피드백
Utils.prototype.addClass = function(object, classString) {
	var element;
	if (typeof (object) == "string") {
		element = document.querySelector(object);
	}
	else {
		element = object;
	}
	element.className = element
		.className
		.split(' ')


		.filter(function (name) { return name !== classString; })
		.concat(classString)
		.join(' ');
}

// html 요소에 있는 클래스 이름을 제거하는 함수
// 요소에 제거하려는 클래스 이름이 있든 없든 정상 동작한다.
// 참조 : 리뷰어 '배돌이'님의 피드백
Utils.prototype.removeClass = function(object, classString) {
	var element;
	if (typeof (object) == "string") {
		element = document.querySelector(object);
	}
	else {
		element = object;
	}
	element.className = element
		.className
		.split(' ')
		.filter(function (name) { return name !== classString; })
		.join(' ');
}

//yyyy-MM-dd HH:mm 형식을 yyyy년 MM월 dd일 aa HH시 mm으로 바꾸는 함수
Utils.prototype.formatDatetime = function(datetime) {

	var resultStr = "";
	var i; 
	var datetime = datetime.split(" ");
	var date = datetime[0];
	var dates = date.split("-");
	var dates_length = dates.length;
	
	for (i = 0; i < dates_length; i++) {
		switch (i) {
			case 0:
				resultStr += dates[i] + "년 ";
				break;
			case 1:
				resultStr += dates[i] + "월 ";
				break;
			case 2:
				resultStr += dates[i] + "일 ";
				break;
		}
	}

	if(datetime.length < 2 || datetime[1] == "00:00:00.0"){
		return resultStr;
	}

	var time = datetime[1]; 
	var times = time.split(":");
	var times_length = times.length;

	for (i = 0; i < times_length-1; i++) {
		switch (i) {
			case 0:
				if(times[i] < 12){
					resultStr += "오전 ";
				}
				else{
					resultStr += "오후 ";
					if(times[i] > 12){
						times[i] -= 12;
					}
				}
				resultStr += times[i] + "시 ";
				break;
			case 1:
				resultStr += times[i] + "분";
				break;
		}
	}

	return resultStr;
}


// 금액 값을 뒤자리에서부터 숫자 세 개당 , 를 반복해서 찍어주는 함수
// ex 100000 ==> 100,000
Utils.prototype.getPriceNumberString = function(number) {
	number = number + "";
	var count = 0;
	var numberArray = number.split('');
	for (var i = numberArray.length - 1; i > 0; i--) {
		count++;
		if (count == 3) {
			numberArray.splice(i, 0, ',');
			count = 0;
		}
	}
	return numberArray.join('');
}
