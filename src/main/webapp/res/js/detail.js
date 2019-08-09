//상세 페이지를 위한 스크립트

//AJAX 통신을 이용해서 서버로부터 DisplayInfoResponse를 요청하고 
//받은 JSON 객체를 통해
//상세 페이지에 있는 모든 뷰들의 내용을 채우는 함수
function getDisplayInfoResponse(displayInfoId) {
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function () {
		var jsonObj = JSON.parse(this.responseText);
		var productImages = jsonObj.productImages;
		var comments = jsonObj.comments;
		var displayInfoImage = jsonObj.displayInfoImage;
		// var productPrices = jsonObj.productPrices;
		var displayInfo = jsonObj.displayInfo;
		var averageScore = jsonObj.averageScore;

		ProductImage.addProductImages(productImages, displayInfo);
		Content.addProductContent(displayInfo);
		Event.addEventInfo(displayInfo);
		Comment.addAverageScore(averageScore, comments);
		Comment.addComments(comments, true);
		Comment.updateMoreCommentButton(comments);
		Utils.changeAnchorUrl(".btn_review_more",
			"/reserv/review?displayInfoId=" + displayInfoId);
		Location.addLocationInfoArea(displayInfo, displayInfoImage);
		ProductImage.loadAnimation();

	})
	oReq.open("GET", "/reserv/api/products/" + displayInfoId);
	oReq.send();
}

//click 리스너를 등록하는 함수
function registerClickListener(selector, onclickListener) {
	document.querySelector(selector).addEventListener("click", onclickListener);
}

window.addEventListener('load', function () {
	//사이트가 처음 로드되었을 경우 호출
	getDisplayInfoResponse(Utils.getParameterByName("displayInfoId"));
	registerClickListener(".btn_prev", ProductImage.animateToLeft);//슬라이드 이미지에 있는 왼쪽 화살표 버튼을 눌렀을때
	registerClickListener(".btn_nxt", ProductImage.animateToRight);//슬라이드 이미지에 있는 오른쪽 화살표 버튼을 눌렀을때
	registerClickListener(".bk_more._open", Content.getMoreText); //펼쳐보기 버튼 클릭
	registerClickListener(".bk_more._close", Content.minifyText); //접기 버튼 클릭
	registerClickListener(".item._detail > a", Content.showContent);//'상세정보' 탭을 눌렀을때
	registerClickListener(".item._path > a", Location.showLocationInfo);//'오시는 길' 탭을 눌렀을때
	registerClickListener(".lnk_top", Utils.scrollToTop);// TOP 버튼을 눌렀을 때
});