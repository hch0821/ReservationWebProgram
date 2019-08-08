// 예매자 평가 더보기 페이지를 위한 스크립트

function getDisplayInfoResponse(displayInfoId){
	oReq = new XMLHttpRequest();
	oReq.addEventListener("load", function() {
		var jsonObj = JSON.parse(this.responseText);
		var comments = jsonObj.comments;
		var displayInfo = jsonObj.displayInfo;
		var averageScore = jsonObj.averageScore;

		addAverageScore(averageScore, comments);
		addComments(comments, false);
	})
	oReq.open("GET", "/reserv/api/products/"+displayInfoId);
	oReq.send();
}



function addAverageScore(averageScore, comments){
	var graph_value = document.querySelector(".grade_area>.graph_mask>.graph_value");
	graph_value.style.width = ((averageScore / 5.0) * 100) + "%";

	var avgSpan = document.querySelector(".grade_area>.text_value>span");
	avgSpan.innerText = averageScore;
	var joinCount = document.querySelector(".join_count>.green");
	joinCount.innerText = comments.length + "건";
}

function addComments(comments, showLess){
	Handlebars.registerHelper("thumb_area_image", function(commentImages){
		if(commentImages.length < 1)
		{
			return "/img/bg_card_bl.png"; //아무 의미 없는 이미지 경로
		}
		else{
			return commentImages[0].saveFileName;
		}
	});
	Handlebars.registerHelper("thumb_area_image_count", function(commentImages){	
		return commentImages.length;
	});
	Handlebars.registerHelper("thumb_area_style", function(commentImages){
		if(commentImages.length < 1){
			return "display:none;"
		}
		else{
			return ""
		}
	});

	Handlebars.registerHelper("thumb_area_score", function(score){
		return score.toFixed(1)
	});
	Handlebars.registerHelper("thumb_area_name", function(reservationEmail){
		var id = reservationEmail.split("@")[0];
		id = id.slice(0, 4);
		id+="****";
		return id;
	});

	Handlebars.registerHelper("thumb_area_date", function(reservationDate){
		var date = new Date(reservationDate);
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		
		return year+"."+month+"."+day;
	});

	Handlebars.registerHelper("thumb_area_image_exist", function(commentImages){
		if(commentImages.length < 1){
			return "review_area no_img";
		}
		else{
			return "review_area";
		}
	});

	var template = document.querySelector("#comment_template").innerText;
	var ul = document.querySelector(".list_short_review");
	var bindTemplate = Handlebars.compile(template);
	var resultHTML;

	if(showLess){
		resultHTML = comments.slice(0, 3).reduce(function(prev, next){
			return prev + bindTemplate(next);
		}, "");
	}
	else{
		resultHTML = comments.reduce(function(prev, next){
			return prev + bindTemplate(next);
		}, "");
	}
	

	ul.innerHTML = resultHTML;
}

//url에 있는 parameter값을 parameter이름으로 불러오는 함수.
//https://stackoverflow.com/a/53824889
function getParameterByName(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
      results = regex.exec(location.search);
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function onBackButtonClicked(){
    history.go(-1);
}

// TOP 버튼을 눌렀을 때 사이트의 맨 위로 스크롤 하는 함수
// 참조한 사이트: https://developer.mozilla.org/ko/docs/Web/API/Window/scrollTo
function gototopClicked(){

	window.scrollTo({
		  top: 0,
		  left: 0,
		  behavior: 'smooth'
		});
}

window.addEventListener('load', function() {
	
	//사이트가 처음 로드되었을 경우 호출
	getDisplayInfoResponse(getParameterByName("displayInfoId"));
});

