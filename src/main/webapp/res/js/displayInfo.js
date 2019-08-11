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
		})

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

		var prevProductImageButton = document
			.querySelector(".prev_inn > .btn_prev");
		var nextProductImageButton = document
			.querySelector(".nxt_inn > .btn_nxt");

		if (imageLength < 2) {
			prevProductImageButton.style.display = "none";
			nextProductImageButton.style.display = "none";
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
	animate: function (children, childrenLength, direction) {
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
			anchor.children[0].className = anchor.children[0].className
				.replace(" off", "");
		} else {
			anchor.removeEventListener("click", onclickListener);
			anchor.children[0].className = anchor.children[0].className += " off";
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
		ProductImage.animate(swipeUlChildren, swipeUlChildlrenLength, "left");
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
		ProductImage.animate(swipeUlChildren, swipeUlChildlrenLength, "right");
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
		return results === null ? "" : decodeURIComponent(results[1].replace(
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
				return "/img/bg_card_bl.png"; // 아무 의미 없는 이미지 경로
			} else {
				return commentImages[0].saveFileName;
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
				return "display:none;"
			} else {
				return ""
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
		var buttonReviewMore = document.querySelector(".btn_review_more");
		if (comments.length == 0) {
			buttonReviewMore.style.display = "none";
		} else {
			buttonReviewMore.style.display = "";
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
			.querySelector(".box_store_info.no_topline > .store_location > .store_map.img_thumb").src = "/reserv/res/"
			+ displayInfoImage.saveFileName;
		document.querySelector(".box_store_info.no_topline > .store_location").href = "https://m.map.naver.com/search2/search.nhn?sm=hty&style=v4&query="
			+ displayInfo.placeStreet;
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
		sectionInfoTab.children[1].className = "detail_area_wrap hide";
		sectionInfoTab.children[2].className = "detail_location";

		document.querySelector(".info_tab_lst> .item._detail > a").className = "anchor";
		document.querySelector(".info_tab_lst> .item._path > a").className = "anchor active";
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
		document.querySelector(".store_details.close3").className = "store_details";
		document.querySelector(".bk_more._open").style.display = "none";
		document.querySelector(".bk_more._close").style.display = "";
	},

	// 상품 내용을 축약해주는 함수
	minifyText: function () {
		document.querySelector(".store_details").className = "store_details close3";
		document.querySelector(".bk_more._open").style.display = "";
		document.querySelector(".bk_more._close").style.display = "none";
	},

	// 상세정보 탭을 눌렀을 때 찾아오시는길 탭의 내용을 숨기고 해당 내용을 보여주는 함수
	showContentTab: function () {
		var sectionInfoTab = document.querySelector(".section_info_tab");
		sectionInfoTab.children[1].className = "detail_area_wrap";
		sectionInfoTab.children[2].className = "detail_location hide";

		document.querySelector(".info_tab_lst> .item._detail > a").className = "anchor active";
		document.querySelector(".info_tab_lst> .item._path > a").className = "anchor";
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
