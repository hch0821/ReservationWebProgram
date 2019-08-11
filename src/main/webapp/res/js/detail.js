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
		var displayInfo = jsonObj.displayInfo;
		var averageScore = jsonObj.averageScore;

		ProductImage.addImages(productImages, displayInfo);
		Content.updateProductContent(displayInfo);
		Event.updateEventInfo(displayInfo);
		Comment.updateAverageScore(averageScore, comments);
		Comment.updateCommentList(comments, true);
		Comment.updateMoreCommentButton(comments);
		Utils.changeAnchorUrl(".btn_review_more",
			"/reserv/review?displayInfoId=" + displayInfoId);
		Location.updateLocationInfoArea(displayInfo, displayInfoImage);
		ProductImage.loadAnimation();

	})
	oReq.open("GET", "/reserv/api/products/" + displayInfoId);
	oReq.send();
}

window.addEventListener('load', function () {
	//사이트가 처음 로드되었을 경우 호출
	getDisplayInfoResponse(Utils.getParameterByName("displayInfoId"));
	Utils.registerClickListener(".btn_prev", ProductImage.animateToLeft);//슬라이드 이미지에 있는 왼쪽 화살표 버튼을 눌렀을때
	Utils.registerClickListener(".btn_nxt", ProductImage.animateToRight);//슬라이드 이미지에 있는 오른쪽 화살표 버튼을 눌렀을때
	Utils.registerClickListener(".bk_more._open", Content.getMoreText); //펼쳐보기 버튼 클릭
	Utils.registerClickListener(".bk_more._close", Content.minifyText); //접기 버튼 클릭
	Utils.registerClickListener(".item._detail > a", Content.showContentTab);//'상세정보' 탭을 눌렀을때
	Utils.registerClickListener(".item._path > a", Location.showLocationTab);//'오시는 길' 탭을 눌렀을때
	Utils.registerClickListener(".lnk_top", Utils.scrollToTop);// TOP 버튼을 눌렀을 때
	Utils.registerClickListener(".section_btn > .bk_btn", function(){ // 예매하기 버튼 눌렀을 때
		
		// 참조 사이트: https://www.w3schools.com/howto/howto_js_redirect_webpage.asp
		window.location.href="/reserv/res/htmls/reserve.html";
	});
});