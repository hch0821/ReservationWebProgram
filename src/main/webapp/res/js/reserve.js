// 예약 페이지를 위한 스크립트

//예약 뷰 클래스
function ReservationView(price, displayInfo, reservationInfo)  
{
	this.price = price; // ajax로 부터 받은 가격 정보를 담은 객체
	this.displayInfo = displayInfo; // ajax로 부터 받은 전시 정보 객체
	this.reservationInfo = reservationInfo;	// ajax로 부터 받은 예약 정보를 가지고 있는 객체
}

// 예약 페이지의 사진 영역을 업데이트하는 함수.
ReservationView.prototype.updateProductImageArea = function(productImage) {
	document.querySelector(".visual_img > .item > .img_thumb").src = "/reserv/image?path="
		+ productImage.saveFileName;

	document.querySelector(".preview_txt_tit").innerText = this.displayInfo.productDescription;
}

// 상품 정보 영역을 업데이트하는 함수
ReservationView.prototype.updateSectionProductDetail = function() {
	var dscs = document.querySelectorAll(".dsc");
	var placeDsc = dscs[0];

	var pricesDsc = dscs[2];

	var resultHTML = "";

	placeDsc.innerText = this.displayInfo.placeStreet;

	Array.prototype.forEach.call(this.price.productPrices, function (v) {
		resultHTML +=
			this.price.lookupPriceType(v.priceTypeName) + ": " +
			this.price.adjustDiscountRate(v.price, v.discountRate) +
			"원" + " (" + v.discountRate + "% 할인가) <br>";

	}.bind(this));


	pricesDsc.innerHTML = resultHTML;
}

// 티켓 정보 영역을 업데이트하는 함수.
ReservationView.prototype.updateSectionBookingTicket = function() {
	var productPrices = this.price.productPrices;
	var ticket_body = document.querySelector(".ticket_body");
	var template = document.querySelector("#ticket_info_template").innerText;
	var utils = Utils.getInstance();
	Handlebars.registerHelper("price_times_discountRate1", function (price, discountRate) {
		var priceStr = "";
		if (discountRate != 0) {
			priceStr = "<del style='color:red;'><span style='color:darkgray'>" +
				utils.getPriceNumberString(price) +
				"</span></del> " +
				Price.WON_STRING;
		}
		else {
			priceStr = utils.getPriceNumberString(price, discountRate) + " " + Price.WON_STRING;
		}
		return priceStr;
	}.bind(this));

	Handlebars.registerHelper("price_times_discountRate2", function (price, discountRate) {
		var priceStr = "";
		if (discountRate != 0) {
			priceStr = this.price.adjustDiscountRate(price, discountRate) + " " + Price.WON_STRING + "(" + discountRate + "% 할인가)";
		}
		else {
			priceStr = utils.getPriceNumberString(price) + " " + Price.WON_STRING;
		}
		return priceStr;
	}.bind(this));

	Handlebars.registerHelper("lookupPriceType", function (priceTypeName) {
		return this.price.lookupPriceType(priceTypeName);
	}.bind(this));

	var bindTemplate = Handlebars.compile(template);
	var resultHTML;
	resultHTML = productPrices.reduce(function (prev, next) {
		return prev + bindTemplate(next);
	}, "");
	ticket_body.innerHTML = resultHTML;
}

// ajax로 서버로부터 공연 일자를 받아오는 함수 (규칙 : 오늘날짜 포함 1~5일 뒤의 날짜를 받아옴.)
ReservationView.prototype.updateReservationDate = function() {
	var utils = Utils.getInstance();
	var reservationInfo = this.reservationInfo;
	utils.requestAjax("GET", "/reserv/api/reservations/reservationDate", function () {
		if (!this.responseText) {
			alert("죄송합니다. 공연 일자 정보를 얻지 못했습니다.");
			utils.tothePreviousSite();
			return;
		}
		var reservationDate = JSON.parse(this.responseText).reservationDate;
		var formattedReservationDate = utils.formatDatetime(reservationDate);
		var dscs = document.querySelectorAll(".dsc");
		var periodDsc = dscs[1];

		reservationInfo.reservationYearMonthDay = reservationDate;
		document.querySelector("#reservation_date").innerText = formattedReservationDate;
		periodDsc.innerText = formattedReservationDate;

	}, null);
}

// 빼기 또는 더하기 버튼을 누를 때 마다 총 구매 티켓 개수를 업데이트하는 함수.
ReservationView.prototype.updateTotalTicketCount = function() {
	var totalCount = 0;
	var ticketCountInputs = document.querySelectorAll(".count_control_input");
	var ticketCountInputs_length = ticketCountInputs.length;
	for (var i = 0; i < ticketCountInputs_length; i++) {
		totalCount += parseInt(ticketCountInputs[i].value);
	}
	document.querySelector("#total_count").innerText = totalCount;
}

// 빼기 또는 더하기 버튼을 누를 때 마다 총 결제 금액을 업데이트하는 함수.
ReservationView.prototype.updateTotalPricePaid = function() {
	var totalPricePaid = 0;
	var ticketCountInputs = document.querySelectorAll(".count_control_input");
	var ticketCountInputs_length = ticketCountInputs.length;
	var productPrices = this.price.productPrices;
	var utils = Utils.getInstance();
	for (var i = 0; i < ticketCountInputs_length; i++) {
		totalPricePaid += parseInt(ticketCountInputs[i].value) * parseInt(productPrices[i].price * 0.01 * (100 - productPrices[i].discountRate));
	}

	document.querySelector("#total_price_paid").innerText = utils.getPriceNumberString(totalPricePaid);
}

// 더하기 또는 빼기 버튼 리스너를 등록하는 함수.
ReservationView.prototype.initPlusMinusButtonListener = function() {
	var utils = Utils.getInstance();

	var productPrices = this.price.productPrices;
	var displayInfo = this.displayInfo;

	var qtys = document.querySelector(".ticket_body").children;
	var qtys_length = qtys.length;

	for (var i = 0; i < qtys_length; i++) {
		(function (idx) {
			var minusButton = qtys[idx].querySelector(".btn_plus_minus.spr_book2.ico_minus3")
			var plusButton = qtys[idx].querySelector(".btn_plus_minus.spr_book2.ico_plus3");
			var ticketCountInput = qtys[idx].querySelector(".count_control_input");
			var totalPriceSpan = qtys[idx].querySelector(".total_price");
			var price = this.price;
			var reservationView = this;
			var productPrice = price.productPrices[idx];
			utils.registerClickListener(minusButton, function () {
				if (ticketCountInput.value > 0) {
					ticketCountInput.value--;
					totalPriceSpan.innerText = price.adjustDiscountRate(productPrice.price * ticketCountInput.value, productPrice.discountRate);
					reservationView.updateTotalTicketCount();
					reservationView.updateTotalPricePaid();
				}
				if (ticketCountInput.value == 0) {
					utils.addClass(ticketCountInput, "disabled");
					utils.addClass(minusButton, "disabled");
				}

				reservationView.reservationInfo.validateRegisterInfo(displayInfo, productPrices);
			})
			utils.registerClickListener(plusButton, function () {
				ticketCountInput.value++;
				totalPriceSpan.innerText = price.adjustDiscountRate(productPrice.price * ticketCountInput.value, productPrice.discountRate);
				utils.removeClass(ticketCountInput, "disabled");
				utils.removeClass(minusButton, "disabled");

				reservationView.updateTotalTicketCount();
				reservationView.updateTotalPricePaid();

				reservationView.reservationInfo.validateRegisterInfo(displayInfo, productPrices);
			})
		}.bind(this))(i);
	}
}

// 예약 폼의 input 값들이 올바른 형식이 아닐 때 에러 다이얼로그를 띄워주는 함수
ReservationView.prototype.showValidateFailedDialog = function(errorMessages) {
	var utils = Utils.getInstance();
	var errorDialog = document.querySelector("#errorDialog");
	var confirmButton = errorDialog.querySelector(".btn");
	var ul = errorDialog.querySelector("ul");
	var resultHTML = "";

	Array.prototype.forEach.call(errorMessages, function (v) {
		resultHTML += "<li>" + v + "</li>";
	});


	ul.innerHTML = resultHTML;
	utils.setVisibility(errorDialog, true);

	try {
		errorDialog.show();
	}
	catch (e) {
		utils.addClass(errorDialog, "open");
	}
	finally{
		errorDialog.scrollIntoView();
		confirmButton.focus();
	}
}

// 예약 페이지의 모든 컨트롤의 리스너를 등록하는 함수
ReservationView.prototype.initViewListeners = function() {
	var utils = Utils.getInstance();
	var reservationInfo = this.reservationInfo;
	var productPrices = this.price.productPrices;
	var displayInfo = this.displayInfo;
	// 맨 위로 가기 버튼 눌렀을 때
	utils.registerClickListener(".lnk_top", function () {
		utils.scrollToTop();
	})

	this.initPlusMinusButtonListener();

	// 예약 버튼을 눌렀을 때
	utils.registerClickListener(".bk_btn", function () {
		var errorMessages = this.reservationInfo.validateRegisterInfo(displayInfo, productPrices);

		if (errorMessages.length == 0) {
			utils.requestAjax("POST", "/reserv/api/reservations", function () {
				if (!this.responseText) {
					alert("죄송합니다. 예약하는 도중 에러가 발생하였습니다.");
					return;
				}
				alert("예약이 완료되었습니다!!");
				window.location.href = "/reserv/checkSession";
			}, this.reservationInfo);
		}
		else {
			this.showValidateFailedDialog(errorMessages);
		}
	}.bind(this));

	// 약관 동의 상세 보기/접기 버튼 눌렀을 때
	var agreementButtons = document.querySelectorAll(".btn_agreement");
	var agreementButtons_length = agreementButtons.length;
	for (var i = 0; i < agreementButtons_length; i++) {
		(function (idx) {
			var agreementButton = agreementButtons[idx];
			var agreement = agreementButtons[idx].parentElement;
			utils.registerClickListener(agreementButton, function () {
				if (agreement.className.indexOf("open") > -1) {
					utils.removeClass(agreement, "open");
					agreement.querySelector(".btn_text").innerText = "보기";
				}
				else {
					utils.addClass(agreement, "open");
					agreement.querySelector(".btn_text").innerText = "접기";
				}
			});

		})(i);
	}

	var agreementCheckbox = document.querySelector(".chk_agree");
	agreementCheckbox.addEventListener('change', function () {
		reservationInfo.validateRegisterInfo(displayInfo, productPrices);
	});

	var emailInput = document.querySelector("#email");
	var nameInput = document.querySelector("#name");
	var telInput = document.querySelector("#tel");

	//입력란에 글자를 입력할 때마다 유효값인지 확인
	emailInput.addEventListener("input", function () {
		reservationInfo.validateRegisterInfo(displayInfo, productPrices);
	});
	nameInput.addEventListener("input", function () {
		reservationInfo.validateRegisterInfo(displayInfo, productPrices);
	});
	telInput.addEventListener("input", function () {
		reservationInfo.validateRegisterInfo(displayInfo, productPrices);
	});

	//에러 다이얼로그의 확인 버튼을 눌렀을 때
	var errorDialog = document.querySelector("#errorDialog");
	var confirmButton = errorDialog.querySelector(".btn");
	utils.registerClickListener(confirmButton, function () {
		try {
			errorDialog.show();
		}
		catch (e) {
			utils.removeClass(errorDialog, "open");
		}

		utils.setVisibility(errorDialog, false);
	});

}


// 사용자의 모든 예약 정보를 담을 클래스
function ReservationInfo() 
{
	this.initReservationInfo();
}
// 에러 메시지를 담고있는 스태틱 변수
ReservationInfo.ERROR_MESSAGE =  {
	NAME_EMPTY: "예매자 항목 입력은 필수입니다.",
	TEL_EMPTY: "연락처 항목 입력은 필수입니다.",
	EMAIL_EMPTY: "이메일 항목 입력은 필수입니다.",
	TEL_FORMAT_NOT_MATCHED: "연락처는 다음과 같은 형식이여합니다: <br>'010-0000-0000'",
	EMAIL_FORMAT_NOT_MATCHED: "이메일은 다음과 같은 형식이여야합니다: <br>'aaaa@aaaa.com'",
	TICKET_NOT_CHOSEN: "티켓을 최소 한 장 이상은 구매하셔야합니다.",
	AGREEMENT_NOT_CHECKED: "이용자 약관에 모두 동의하셔야합니다."
}


// 예약 정보 초기화하는 함수
ReservationInfo.prototype.initReservationInfo = function() {
	this.displayInfoId = 0;
	this.prices = [];
	this.productId = 0;
	this.reservationEmail = "";
	this.reservationName = "";
	this.reservationTelephone = "";
}

// 예약 정보를 ajax로부터 받아온 객체들을 파라미터로 전달받아 저장하는 함수
ReservationInfo.prototype.registerReservationInfo = function(displayInfo, productPrices) {

	this.initReservationInfo();

	var pricesLength = productPrices.length;
	var ticketCountInputs = document.querySelectorAll(".count_control_input");

	this.displayInfoId = displayInfo.displayInfoId;
	this.productId = displayInfo.productId;
	this.reservationEmail = document.querySelector("#email").value;
	this.reservationName = document.querySelector("#name").value;
	this.reservationTelephone = document.querySelector("#tel").value;
	for (var i = 0; i < pricesLength; i++) {
		var count = parseInt(ticketCountInputs[i].value);
		if (count == 0) {
			continue;
		}
		var productPriceId = productPrices[i].productPriceId;
		this.prices.push({ "count": count, "productPriceId": productPriceId });
	}
}

// 예약 정보가 유효한지 확인하는 함수
ReservationInfo.prototype.validateRegisterInfo = function(displayInfo, productPrices) {

	var utils = Utils.getInstance();
	this.registerReservationInfo(displayInfo, productPrices);

	var errorMessages = [];
	var telRegex = /^01[01789]-\d{3,4}-\d{4}$/; // 전화번호 regex
	var emailRegex = /^[\w+_]\w+@\w+\.\w+(\.\w+)?$/; // 이메일 regex
	if (this.reservationName == "") {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.NAME_EMPTY);
	}

	if (this.reservationEmail == "") {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.EMAIL_EMPTY);
	}
	else if (!this.reservationEmail.match(emailRegex)) {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.EMAIL_FORMAT_NOT_MATCHED);
	}

	if (this.reservationTelephone == "") {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.TEL_EMPTY);
	}
	else if (!this.reservationTelephone.match(telRegex)) {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.TEL_FORMAT_NOT_MATCHED);
	}

	if (this.prices.length == 0) {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.TICKET_NOT_CHOSEN);
	}

	var agreementCheckbox = document.querySelector(".chk_agree");
	if (!agreementCheckbox.checked) {
		errorMessages.push(ReservationInfo.ERROR_MESSAGE.AGREEMENT_NOT_CHECKED);
	}


	if (errorMessages.length == 0) {

		utils.removeClass(".bk_btn_wrap", "disable"); // 예약하기 버튼 활성화
	}
	else {
		utils.addClass(".bk_btn_wrap", "disable"); // 예약하기 버튼 비활성화
	}

	return errorMessages;
}


// 가격 정보를 담을 클래스
function Price(productPrices) {
	this.productPrices = productPrices;	// 사용자가 예매한 모든 표의 가격정보들 객체
}
// 돈 단위 글자인 원 html 글자 갖고있는 스태틱 변수
Price.WON_STRING = '<span class="price_type">원</span>';


// priceType에 따라 그에 해당하는 좌석 클래스 이름을 반환하는 함수
Price.prototype.lookupPriceType = function(priceType) {
	var priceTypeName = "";
	priceType = priceType.toUpperCase();
	switch (priceType) {
		case "A":
			priceTypeName = "성인";
			break;
		case "Y":
			priceTypeName = "청소년";
			break;
		case "B":
			var isContainsAorY = false;

			Array.prototype.forEach.call(this.productPrices, function (v) {
				if (v.priceTypeName == 'A' ||
					v.priceTypeName == 'Y') {
					priceTypeName = "유아";
					isContainsAorY = true;
					return;
				}
			});

			if (!isContainsAorY) {
				priceTypeName = "B석";
			}

			break;
		case "S":
			priceTypeName = "세트"
			break;
		case "D":
			priceTypeName = "장애인"
			break;
		case "C":
			priceTypeName = "지역주민"
			break;
		case "E":
			priceTypeName = "어얼리버드"
			break;
		case "V":
			priceTypeName = "VIP"
			break;
		case "R":
			priceTypeName = "R석"
			break;
		case "S":
			priceTypeName = "S석"
			break;
		case "D":
			priceTypeName = "평일"
			break;
	}

	return priceTypeName;
}

// 금액에 할인가를 적용하여 반환하는 함수
Price.prototype.adjustDiscountRate = function(price, discountRate) {
	return Utils.getInstance().getPriceNumberString(parseInt(price * (100 - discountRate) * 0.01));
}



// 사이트가 처음 로드되었을 경우 호출
window.addEventListener('load', function () {
	var utils = Utils.getInstance();
	var displayInfoView;
	var displayInfoId = utils.getParameterByName("displayInfoId");

	if (!displayInfoId || displayInfoId <= 0) {
		this.alert("url 형식이 잘못되었습니다.");
		utils.tothePreviousSite();
	}
	utils.removeClass(".header", "fade")

	utils.requestAjax("GET", "/reserv/api/products/" + displayInfoId, function () {

		if (!this.responseText) {
			alert("죄송합니다. 상품 정보를 얻지 못했습니다.");
			utils.tothePreviousSite();
			return;
		}

		var jsonObj = JSON.parse(this.responseText);
		var productPrices = jsonObj.productPrices;
		var displayInfo = jsonObj.displayInfo;
		var productImages = jsonObj.productImages;

		displayInfoView = new ReservationView(new Price(productPrices), displayInfo, new ReservationInfo());
		displayInfoView.updateProductImageArea(productImages[0]);
		displayInfoView.updateSectionProductDetail();
		displayInfoView.updateSectionBookingTicket();
		displayInfoView.initViewListeners();
		displayInfoView.updateReservationDate();
	}, null);

	utils.registerClickListener(".btn_back", function () {
		utils.tothePreviousSite();
	});


});