class Utils {
	//Utils Singleton instance
	static instance;
	constructor() {
		if (Utils.instance != undefined) {
			return Utils.instance;
		}
		Utils.instance = this;
	}

	// url에 있는 parameter값을 parameter이름으로 불러오는 함수.
	// 참조한 사이트: https://stackoverflow.com/a/53824889
	getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(location.search);
		return results === null ? "" : decodeURI(results[1].replace(
			/\+/g, " "));
	}

	//AJAX 요청을 하는 함수
	//method : 'POST', 'GET'...
	requestAjax(method, url, loadListener, object) {
		var oReq = new XMLHttpRequest();
		oReq.addEventListener("load", loadListener);
		oReq.open(method, url);
		oReq.setRequestHeader("Content-Type", "application/json");
		if (object != null && object != undefined) {
			var data = JSON.stringify(object);
			oReq.send(data);
		}
		else {
			oReq.send();
		}
	}

	// 사이트의 맨 위로 스크롤 하는 함수
	// 참조한 사이트: https://developer.mozilla.org/ko/docs/Web/API/Window/scrollTo
	scrollToTop() {
		window.scrollTo({
			top: 0,
			left: 0,
			behavior: 'smooth'
		});
	}

	// Anchor 태그의 href URL을 바꾸는 함수
	changeAnchorUrl(selector, url) {
		document.querySelector(selector).href = url;
	}

	// 이전 사이트로 이동하는 함수
	tothePreviousSite() {
		history.go(-1);
	}

	//click 리스너를 등록하는 함수
	registerClickListener(object, onclickListener) {
		if (typeof (object) == "string") {
			document.querySelector(object).addEventListener("click", onclickListener);
		}
		else {
			object.addEventListener("click", onclickListener);
		}
	}

	//click 리스너를 등록 해지하는 함수
	unregisterClickListener(object, onclickListener) {
		if (typeof (object) == "string") {
			document.querySelector(object).removeEventListener("click", onclickListener);
		}
		else {
			object.removeEventListener("click", onclickListener);
		}
	}

	// selector string에 해당하는 element가 보여질지 말지 설정하는 함수
	setVisibility(selectors, isShown) {
		if (isShown) {
			document.querySelector(selectors).style.display = "";
		} else {
			document.querySelector(selectors).style.display = "none";
		}
	}

	//html 요소에 클래스이름을 추가하는 함수
	//요소에 이미 추가하려는 클래스 이름이 있어도 정상 동작한다.
	//참조 : 리뷰어 '배돌이'님의 피드백

	addClass(object, classString) {
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

	//html 요소에 있는 클래스 이름을 제거하는 함수
	//요소에 제거하려는 클래스 이름이 있든 없든 정상 동작한다.
	//참조 : 리뷰어 '배돌이'님의 피드백

	removeClass(object, classString) {
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
}