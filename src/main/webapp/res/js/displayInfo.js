//AJAX를 통해 가져온 DisplayInfo JSON 객체를 이용해 뷰의 내용을 컨트롤 하기 위한 스크립트
//detail.html, review.html에 각각 있는
//detail.js, review.js가 사용하고 있다.

//상품 이미지, 이미지 애니메이션에 대한 객체
var ProductImage = {
	translated: -100, // 슬라이드 이미지가 왼쪽 또는 오른쪽으로 이동한 정도 (단위 : %)
	productImageIndex: 0, // 슬라이드 이미지의 인덱스

	// 슬라이드 이미지에 이미지를 추가하는 함수
	addImages: function (productImages, displayInfo) {
		var imageLength = productImages.length;
		if (imageLength > 2) {
			productImages = productImages.slice(0, 2);
			imageLength = productImages.length;
		}
		Handlebars.registerHelper("title", function () {
			return displayInfo.productDescription;
		});

		var ul = document.querySelector(".visual_img.detail_swipe");
		var template = document.querySelector("#visual_img_list").innerText;
		var bindTemplate = Handlebars.compile(template);
		var resultHTML;
		resultHTML = productImages.reduce(function (prev, next) {
			return prev + bindTemplate(next);
		}, "");
		ul.innerHTML = resultHTML;

		var figurePagination = document.querySelector(".figure_pagination");

		figurePagination.querySelector(".num.off span").innerText = imageLength;
		
		if (imageLength < 2) {
			Utils.setVisibility(".prev_inn > .btn_prev", false);
			Utils.setVisibility(".nxt_inn > .btn_nxt", false)
		}

		productImageIndex = 0;
	},

	// 슬라이드 이미지를 위한 애니메이션 준비
	loadAnimation: function () {
		var parentElement = document.querySelector(".visual_img.detail_swipe")
		var children = parentElement.children;
		var childrenLength = children.length;
		parentElement.innerHTML += children[0].outerHTML;
		parentElement.innerHTML = children[childrenLength - 1].outerHTML
			+ parentElement.innerHTML;
		children = parentElement.children;
		this.setTransition(children, true);
	},

	// 슬라이드 이미지를 왼쪽 또는 오른쪽으로 넘기는 효과를 주는 함수
	animateProductImage: function (children, childrenLength, direction) {
		var idx = 0;

		this.setEnableArrowButton(".btn_prev", this.animateToLeft,
			false);
		this.setEnableArrowButton(".btn_nxt", this.animateToRight,
			false);

		this.setTransition(children, true);
		if (direction == "right") {
			this.translated -= 100;
		} else if (direction == "left") {
			this.translated += 100;
		}
		for (idx = 0; idx < childrenLength; idx++) {
			children[idx].style.transform = "translateX(" + this.translated
				+ "%)";
		}
		if (productImageIndex == 0 || productImageIndex == childrenLength - 3) {
			if (productImageIndex == 0) {
				this.translated = -200;
			} else if (productImageIndex == childrenLength - 3) {
				this.translated = -100;
			}
			setTimeout(function () {
				this.setTransition(children, false);
				for (idx = 0; idx < childrenLength; idx++) {
					children[idx].style.transform = "translateX("
						+ this.translated + "%)";
				}
			}.bind(this), 1000);
		}
		setTimeout(function () {
			this.setEnableArrowButton(".btn_prev",
				this.animateToLeft, true);
			this.setEnableArrowButton(".btn_nxt",
				this.animateToRight, true);
		}.bind(this), 1000);

	},

	// 이미지 슬라이드 영역에 있는 화살표 버튼을 비활성화 또는 활성화하는 함수
	setEnableArrowButton: function (anchorSelector, onclickListener, isEnabled) {
		var anchor = document.querySelector(anchorSelector);
		if (isEnabled) {
			anchor.addEventListener("click", onclickListener);
			Utils.removeClass(anchor.children[0], "off", false);
		} else {
			anchor.removeEventListener("click", onclickListener);
			Utils.addClass(anchor.children[0], "off", false);
		}
	},

	// 파라미터로 들어온 element list들의 애니메이션 효과를 켜고 끌 수 있도록 구현한 함수
	setTransition: function (elemList, on) {
		if (on) {
			for (idx = 0; idx < elemList.length; idx++) {
				elemList[idx].style.transition = "transform 1s ease-in-out";
			}
		} else {
			for (idx = 0; idx < elemList.length; idx++) {
				elemList[idx].style.transition = "none";
			}
		}
	},

	// 이미지를 왼쪽으로 슬라이드 하는 함수
	animateToLeft: function () {
		var swipeUlChildren = document
			.querySelector(".visual_img.detail_swipe").children;
		var swipeUlChildlrenLength = swipeUlChildren.length;
		ProductImage.animateProductImage(swipeUlChildren, swipeUlChildlrenLength, "left");
		productImageIndex--;
		if (productImageIndex < 0) {
			productImageIndex = swipeUlChildlrenLength - 3;
		}
		document.querySelector(".figure_pagination > .num").innerText = productImageIndex + 1;
	},

	// 이미지를 오른쪽으로 슬라이드 하는 함수
	animateToRight: function () {
		var lengthInfo = document.querySelector(".figure_pagination");
		var imageLength = parseInt(lengthInfo.querySelector(".num.off span").innerText);
		var swipeUlChildren = document
			.querySelector(".visual_img.detail_swipe").children;
		var swipeUlChildlrenLength = swipeUlChildren.length;
		ProductImage.animateProductImage(swipeUlChildren, swipeUlChildlrenLength, "right");
		productImageIndex = (productImageIndex + 1) % imageLength;

		lengthInfo.querySelector(".num").innerText = productImageIndex + 1;
	},

}

// 공통적으로 사용하는 유용한 함수들을 모아놓은 객체
var Utils = {

	// url에 있는 parameter값을 parameter이름으로 불러오는 함수.
	// 참조한 사이트: https://stackoverflow.com/a/53824889
	getParameterByName: function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(location.search);
		return results === null ? "" : decodeURI(results[1].replace(
			/\+/g, " "));
	},

	// 사이트의 맨 위로 스크롤 하는 함수
	// 참조한 사이트: https://developer.mozilla.org/ko/docs/Web/API/Window/scrollTo
	scrollToTop: function () {
		window.scrollTo({
			top: 0,
			left: 0,
			behavior: 'smooth'
		});
	},

	// Anchor 태그의 href URL을 바꾸는 함수
	changeAnchorUrl: function (selector, url) {
		document.querySelector(selector).href = url;
	},

	// 이전 사이트로 이동하는 함수
	tothePreviousSite: function () {
		history.go(-1);
	},

	//click 리스너를 등록하는 함수
	registerClickListener: function (selector, onclickListener) {
		document.querySelector(selector).addEventListener("click", onclickListener);
	},
	//click 리스너를 등록 해지하는 함수
	unregisterClickListener: function (selector, onclickListener) {
		document.querySelector(selector).removeEventListener("click", onclickListener);
	},
	
	// selector string에 해당하는 element가 보여질지 말지 설정하는 함수
	setVisibility: function (selectors, isShown) {
		if (isShown) {
			document.querySelector(selectors).style.display = "";
		} else {
			document.querySelector(selectors).style.display = "none";
		}
	},
	
	//html 요소에 클래스이름을 추가하는 함수
	//요소에 이미 추가하려는 클래스 이름이 있어도 정상 동작한다.
	//참조 : 리뷰어 '배돌이'님의 피드백
	
	//예시
	//<h1 class="title orange">제목</h1> 이라는 요소에 hide라는 클래스를 추가하고 싶다면
	//var h1 = Utils.addClass(".title.orange", "hide", true); 로 실행하면 된다.
	//결과는 <h1 class="title orange hide">제목</h1> 가 된다.
	addClass: function(object, classString, isObjectSelector){
		var element;
		if(isObjectSelector)
		{
			element = document.querySelector(object);
		}
		else{
			element = object;
		}
		element.className = element
			   .className // return: "title orange"
			   .split(' ') // return: ["title", "orange"]
			   
			   // return: ["title", "orange"]
			   // 만약 classString이 "orange"일 경우 classString이 아닌 배열의 값만 리턴하므로 ["title"]이 리턴 된다. 
			   .filter(function(name){return name !== classString;}) 
			   .concat(classString) // return: ["title", "orange", "hide"]
			   .join(' '); // return "title orange hide"
	},
	
	//html 요소에 있는 클래스 이름을 제거하는 함수
	//요소에 제거하려는 클래스 이름이 있든 없든 정상 동작한다.
	//참조 : 리뷰어 '배돌이'님의 피드백
	
	removeClass: function(object, classString, isObjectSelector){
		var element;
		if(isObjectSelector)
		{
			element = document.querySelector(object);
		}
		else{
			element = object;
		}
		element.className = element
			   .className
			   .split(' ')
			   .filter(function(name){return name!==classString;})
			   .join(' ');
	}
}

// 댓글 관련 객체
var Comment = {
	// 평점과 댓글의 갯수를 뿌리는 함수
	updateAverageScore: function (averageScore, comments) {
		var graph_value = document
			.querySelector(".grade_area>.graph_mask>.graph_value");
		graph_value.style.width = ((averageScore / 5.0) * 100) + "%";

		var avgSpan = document.querySelector(".grade_area>.text_value>span");
		avgSpan.innerText = averageScore;
		var joinCount = document.querySelector(".join_count>.green");
		joinCount.innerText = comments.length + "건";
	},

	// comments 객체로부터 템플릿 html을 이용하여 댓글 목록을 형성하는 함수
	updateCommentList: function (comments, showLess) {

		// 사용자가 올린 이미지 추가
		Handlebars.registerHelper("thumb_area_image", function (commentImages) {
			if (commentImages.length < 1) {
				return ""; 
			} else
			{
				return "/reserv/commentimage?id="+commentImages[0].imageId;
			}
		});

		// 이미지 개수를 썸네일 오른쪽 하단에 추가
		Handlebars.registerHelper("thumb_area_image_count", function (
			commentImages) {
			return commentImages.length;
		});

		// 올린 이미지가 없을 경우 이미지 개수를 표시하지 않음
		Handlebars.registerHelper("thumb_area_style", function (commentImages) {
			if (commentImages.length < 1) {
				return "display:none;";
			} else {
				return "";
			}
		});

		// 평점을 소수점 한자리까지 표시
		Handlebars.registerHelper("thumb_area_score", function (score) {
			return score.toFixed(1)
		});

		// 사용자 이메일중 아이디만 표시. 단 모두 표시하지 않고 ****로 감춤.
		Handlebars.registerHelper("thumb_area_name",
			function (reservationEmail) {
				var id = reservationEmail.split("@")[0];
				id = id.slice(0, 4);
				id += "****";
				return id;
			});

		// 날짜 형식을 YYYY.MM.DD형태로 바꿈.
		Handlebars.registerHelper("thumb_area_date", function (reservationDate) {

			// Internet Explorer 호환을 위해 날짜형식을 '2019-07-28 13:58:16.0'에서
			// '2019-07-28'로 변경
			reservationDate = reservationDate.split(" ")[0];
			var date = new Date(reservationDate);
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			var day = date.getDate();

			return year + "." + month + "." + day;
		});

		// 사용자가 이미지를 올렸을 경우와 올리지 않았을 경우를 구분하여 className을 변경.
		Handlebars.registerHelper("thumb_area_image_exist", function (
			commentImages) {
			if (commentImages.length < 1) {
				return "review_area no_img";
			} else {
				return "review_area";
			}
		});

		var template = document.querySelector("#comment_template").innerText;
		var ul = document.querySelector(".list_short_review");
		var bindTemplate = Handlebars.compile(template);
		var resultHTML;

		if (showLess) {
			resultHTML = comments.slice(0, 3).reduce(function (prev, next) {
				return prev + bindTemplate(next);
			}, "");
		} else {
			resultHTML = comments.reduce(function (prev, next) {
				return prev + bindTemplate(next);
			}, "");
		}

		ul.innerHTML = resultHTML;
	},

	// 댓글의 개수에 따라 더보기 버튼이 보여질지 말지 결정하는 함수
	updateMoreCommentButton: function (comments) {
		if (comments.length == 0) {
			Utils.setVisibility(".btn_review_more", false);
		} else {
			Utils.setVisibility(".btn_review_more", true);
		}
	}
};

// 상영관 또는 콘서트장 위치 관련 객체
var Location = {
		
	// 위치 정보 탭에 해당하는 뷰의 내용들을 displayInfo 객체로부터 뿌리는 함수
	updateLocationInfoArea: function (displayInfo, displayInfoImage) {
		document.querySelector(".store_addr.store_addr_bold").innerText = displayInfo.placeStreet;
		document.querySelector(".store_addr > .addr_old_detail").innerText = displayInfo.placeLot;
		document.querySelector(".store_addr.addr_detail").innerText = displayInfo.placeName;
		document
			.querySelector(".box_store_info.no_topline > .store_location > .store_map.img_thumb").src = "/reserv/image?path="
			+ displayInfoImage.saveFileName;
		document.querySelector(".store_name").innerText = displayInfo.productDescription;
		var telAnchor = document.querySelector(".lst_store_info .store_tel");
		var tel = displayInfo.telephone;
		if (tel == "") {
			tel = "등록된 연락처가 없습니다."
		}
		telAnchor.innerText = tel;
	},

	// 찾아오시는 길 탭을 눌렀을때 상세정보 탭의 내용을 숨기고 해당 내용을 보여주는 함수
	showLocationTab: function () {
		var sectionInfoTab = document.querySelector(".section_info_tab");
		Utils.addClass(sectionInfoTab.children[1], "detail_area_wrap hide", false);
		Utils.removeClass(sectionInfoTab.children[2], "hide", false);	
		
		Utils.removeClass(".info_tab_lst> .item._detail > a", "active", true);
		Utils.addClass(".info_tab_lst> .item._path > a", "active", true);
		
	}
};

// 상품 상세 내용 관련 객체
var Content = {
	// 상품 내용을 뷰에 뿌리는 함수
	updateProductContent: function (displayInfo) {
		document.querySelector(".dsc").innerHTML = displayInfo.productContent;
		document.querySelector(".detail_info_group .in_dsc").innerHTML = displayInfo.productContent;
	},

	// 상품 내용을 펼쳐서 보여주는 함수
	getMoreText: function () {
		Utils.removeClass(".store_details.close3", "close3", true);
		Utils.setVisibility(".bk_more._open", false);
		Utils.setVisibility(".bk_more._close", true);
	},

	// 상품 내용을 축약해주는 함수
	minifyText: function () {
		Utils.addClass(".store_details", "close3", true);
		Utils.setVisibility(".bk_more._open", true);
		Utils.setVisibility(".bk_more._close", false);
	},

	// 상세정보 탭을 눌렀을 때 찾아오시는길 탭의 내용을 숨기고 해당 내용을 보여주는 함수
	showContentTab: function () {
		var sectionInfoTab = document.querySelector(".section_info_tab");
		Utils.removeClass(sectionInfoTab.children[1], "hide", false);
		Utils.addClass(sectionInfoTab.children[2], "hide", false);
		
		Utils.addClass(".info_tab_lst> .item._detail > a", "active", true);
		Utils.removeClass(".info_tab_lst> .item._path > a", "active", true);
	}

};

// 이벤트 관련 객체
var Event = {
	// 이벤트 정보를 뿌리는 함수
	updateEventInfo: function (displayInfo) {
		var productEvent;
		if (displayInfo.productEvent == "") {
			productEvent = "이벤트 정보가 없습니다.";
		}
		document.querySelector(".event_info .in_dsc").innerText = productEvent;
	}
};
