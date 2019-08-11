// 예매자 평가 더보기 페이지를 위한 스크립트

// AJAX 통신을 이용해서 서버로부터 DisplayInfoResponse를 요청하고 
// 받은 JSON 객체를 통해
// 예매자 평가 더보기 페이지에 있는 모든 뷰들의 내용을 채우는 함수
function getDisplayInfoResponse(displayInfoId) {
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function () {
		var jsonObj = JSON.parse(this.responseText);
		var comments = jsonObj.comments;
		var averageScore = jsonObj.averageScore;
		Comment.updateAverageScore(averageScore, comments);
		Comment.updateCommentList(comments, false);
	})
	oReq.open("GET", "/reserv/api/products/" + displayInfoId);
	oReq.send();
}

// 사이트가 처음 로드되었을 경우 호출
window.addEventListener('load', function () {
	getDisplayInfoResponse(Utils.getParameterByName("displayInfoId"));
	Utils.registerClickListener(".top_title.gr > .btn_back", Utils.tothePreviousSite); // 뒤로 가기 버튼을 눌렀을 때
	Utils.registerClickListener(".gototop > .lnk_top", Utils.scrollToTop); // TOP 버튼을 눌렀을 때
});
