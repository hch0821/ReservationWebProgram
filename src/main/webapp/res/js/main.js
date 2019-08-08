//메인 페이지를 위한 스크립트

var curStart = -1; // 더보기를 누를때마다 0부터 시작하여 NUM_ITEM만큼 증가
var curCategoryId = -1; // 현재 선택되어있는 카테고리의 아이디
var flag = true; // 왼쪽문단과 오른쪽 문단에 번갈아가며 상품을 추가하기 위한 플래그
var oReq; // ajax 요청을 위한 객체
const NUM_ITEM = 4; // ajax 요청을 통해 (추가로) 읽어들일 상품의 개수

// ajax 요청을 통해 프로모션 영역에 뿌릴 정보 가져오는 함수
function getPromotions() {
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function() {
		var jsonObj = JSON.parse(this.responseText);
		var items = jsonObj.items;

		var html = document.querySelector("#promotionItem").innerHTML;
		var resultHTML = "";
		var visualImg = document.querySelector(".visual_img");
		Array.prototype.forEach.call(items, function(v) {
			resultHTML += html.replace("{productImageUrl}", v.productImageUrl)
			visualImg.innerHTML = resultHTML
		});

		animatePromotions(visualImg);
	})
	oReq.open("GET", "/reserv/api/promotions");
	oReq.send();
}

// 프로모션 상품 사진들을 오른쪽에서 왼쪽으로 반복적으로 이동하도록 하는 함수
function animatePromotions(visualImg) {
	var promotionLength;
	var children = visualImg.children;
	var idx = 0;
	var cnt = 0;
	var translated = 0;

	visualImg.innerHTML += children[0].outerHTML;
	children = visualImg.children;
	promotionLength = visualImg.childElementCount;

	setTransition(children, true);

	function animate() {
		for (idx = 0; idx < promotionLength; idx++) {
			children[idx].style.transform = "translateX(" + translated + "%)";
		}
		cnt++;
		translated -= 100;
		if (cnt >= promotionLength) {
			translated = 0;
		}
		if (cnt >= promotionLength + 1) {
			setTransition(children, false);
			setTimeout(function() {
				setTransition(children, true);
			}, 1000)
			cnt = 0;
		}
		setTimeout(function() {
			animate();
		}, 2000)
	}
	animate();
}

// 카테고리 목록을 ajax 요청을 통해 받고 화면에 추가하는 함수
function getCategories() {
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function() {
		var jsonObj = JSON.parse(this.responseText);
		var items = jsonObj.items;

		var html = document.querySelector("#tabitem").innerHTML;
		var resultHTML = "";
		var tabList = document.querySelector(".event_tab_lst.tab_lst_min");
		Array.prototype.forEach.call(items,
				function(v) {
					resultHTML += html.replace("{name}", v.name).replace(
							/{id}/g, v.id);
					tabList.innerHTML = resultHTML;
				})
		getProducts(true, 0, 0);
	})

	oReq.open("GET", "/reserv/api/categories");
	oReq.send();
}

// 파라미터로 들어온 element list들의 애니메이션 효과를 켜고 끌 수 있도록 구현한 함수
function setTransition(elemList, on) {
	if (on) {
		for (idx = 0; idx < elemList.length; idx++) {
			elemList[idx].style.transition = "transform 1s ease-in-out";
		}
	} else {
		for (idx = 0; idx < elemList.length; idx++) {
			elemList[idx].style.transition = "none";
		}
	}
}

// 카테고리(탭)를 툴렀을 때 동작하는 함수
// ajax 통신을 통해 해당 카테고리에 속한 상품 목록들을 불러들인다.
function getProducts(isTabClicked, categoryId, start) {
	if (categoryId == curCategoryId && isTabClicked)
		return;
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function() {
		var jsonObj = JSON.parse(this.responseText)
		getProductInfo(isTabClicked, categoryId, jsonObj)
	});

	oReq.open("GET", "/reserv/api/products?categoryId=" + categoryId
			+ "&start=" + start);
	oReq.send();
	curStart = start;
	curCategoryId = categoryId;
}

// 상품 목록 정보를 파라미터로 넘겨받아서 상품 설명, 상품 내용, 상품 Id 정보를 가져오는 함수
// 상품의 이미지는 서버에게 상품 id를 주어서 GET 방식으로 받도록 한다.
function getProductInfo(isTabClicked, categoryId, jsonObj) {
	var html;
	var eventBoxes;
	var length;
	var items = jsonObj.items;
	if (isTabClicked) {
		emptyProducts();
		document.querySelector(".event_lst_txt>.pink").innerHTML = jsonObj.totalCount
				+ "개";
		var children = document.querySelector(".event_tab_lst.tab_lst_min").children;
		for (var i = 0; i < children.length; i++) {
			children[i].firstElementChild.className = "anchor";
		}
		children[categoryId].firstElementChild.className = "anchor active";

	}

	html = document.querySelector("#itemList").innerHTML;
	eventBoxes = document.querySelector(".wrap_event_box").children;
	length = 0;

	Array.prototype.forEach.call(jsonObj.items, function(v) {
		var resultHTML = "";
		resultHTML = html.replace("{productId}", v.productId).replace(
				/{productDescription}/g, v.productDescription).replace(
				"{productContent}", v.productContent).replace("{placeName}",
				v.placeName).replace("{productId}", v.productId).replace(
				"{displayInfoId}", v.displayInfoId);
		if (flag) {
			eventBoxes[0].innerHTML += resultHTML;
		} else {
			eventBoxes[1].innerHTML += resultHTML;
		}
		flag = !flag;
		length++;
	})
	if (length < NUM_ITEM) {
		setVisibility(".more", false);
	} else {
		setVisibility(".more", true);
	}
}

// 카테고리를 누를때마다 해당 상품목록을 갱신하기위해 상품 목록을 html에서 제거하는 함수
function emptyProducts() {
	var eventBoxes = document.querySelector(".wrap_event_box").children;
	eventBoxes[0].innerHTML = "";
	eventBoxes[1].innerHTML = "";
	curStart = 0;
}

// 더보기 버튼을 눌렀을 경우 현재 start에 NUM_ITEM만큼 추가하여 상품 목록을 추가로 NUM_ITEM만큼 가져오는 함수
function moreClicked() {
	getProducts(false, curCategoryId, curStart + NUM_ITEM);
}

// TOP 버튼을 눌렀을 때 사이트의 맨 위로 스크롤 하는 함수
// 참조한 사이트: https://developer.mozilla.org/ko/docs/Web/API/Window/scrollTo
function gototopClicked() {

	window.scrollTo({
		top : 0,
		left : 0,
		behavior : 'smooth'
	});
}
// selector string에 해당하는 element가 보여질지 말지 설정하는 함수
function setVisibility(selectors, isShown) {
	if (isShown) {
		document.querySelector(selectors).style.display = "";
	} else {
		document.querySelector(selectors).style.display = "none";
	}
}

window.addEventListener('load', function() {

	//사이트가 처음 로드되었을 경우 호출
	getCategories();
	getPromotions();
})
