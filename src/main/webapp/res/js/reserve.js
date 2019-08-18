// 예약 페이지를 위한 스크립트

class ReservationView {
	constructor(price, displayInfo, reservationInfo, isReservationFinished) {
		this.price = price;
		this.displayInfo = displayInfo;
		this.reservationInfo = reservationInfo;
		this.isReservationFinished = false;
	}

	updateProductImageArea(productImage) {
		document.querySelector(".visual_img > .item > .img_thumb").src = "/reserv/res/"
			+ productImage.saveFileName;

		document.querySelector(".preview_txt_tit").innerText = this.displayInfo.productDescription;
	}

	updateSectionProductDetail() {
		var dscs = document.querySelectorAll(".dsc");
		var placeDsc = dscs[0];
		var periodDsc = dscs[1];
		var pricesDsc = dscs[2];

		var resultHTML = "";

		placeDsc.innerText = this.displayInfo.placeStreet;
		periodDsc.innerText = this.displayInfo.openingHours;



		this.price.productPrices.forEach(function (v) {
			resultHTML +=
				this.price.lookupPriceType(v.priceTypeName) + ": " +
				this.price.adjustDiscountRate(v.price, v.discountRate) +
				"원" + " (" + v.discountRate + "% 할인가) <br>";

		}.bind(this));

		pricesDsc.innerHTML = resultHTML;
	}

	updateSectionBookingTicket() {
		var productPrices = this.price.productPrices;
		var ticket_body = document.querySelector(".ticket_body");
		var template = document.querySelector("#ticket_info_template").innerText;

		Handlebars.registerHelper("price_times_discountRate1", function (price, discountRate) {
			var priceStr = "";
			if (discountRate != 0) {
				priceStr = "<span style='text-decoration:line-through solid red;'>" +
					this.price.getPriceNumberString(price) +
					"</span> " +
					Price.WON_STRING;
			}
			else {
				priceStr = this.price.getPriceNumberString(price, discountRate) + " " + Price.WON_STRING;
			}
			return priceStr;
		}.bind(this));

		Handlebars.registerHelper("price_times_discountRate2", function (price, discountRate) {
			var priceStr = "";
			if (discountRate != 0) {
				priceStr = this.price.adjustDiscountRate(price, discountRate) + " " + Price.WON_STRING + "(" + discountRate + "% 할인가)";
			}
			else {
				priceStr = this.price.getPriceNumberString(price) + " " + Price.WON_STRING;
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

	updateReservationDate() {
		var utils = new Utils();
		utils.requestAjax("GET", "/reserv/api/reservations/reservationDate", function () {
			var reservationDate = JSON.parse(this.responseText).reservationDate;
			document.querySelector("#reservation_date").innerText = reservationDate;
		}, null);
	}

	updateTotalTicketCount() {
		var totalCount = 0;
		var ticketCountInputs = document.querySelectorAll(".count_control_input");
		var ticketCountInputs_length = ticketCountInputs.length;
		for (var i = 0; i < ticketCountInputs_length; i++) {
			totalCount += parseInt(ticketCountInputs[i].value);
		}

		document.querySelector("#total_count").innerText = totalCount;
	}

	updateTotalPricePaid() {
		var totalPricePaid = 0;
		var ticketCountInputs = document.querySelectorAll(".count_control_input");
		var ticketCountInputs_length = ticketCountInputs.length;
		var productPrices = this.price.productPrices;
		for (var i = 0; i < ticketCountInputs_length; i++) {
			totalPricePaid += parseInt(ticketCountInputs[i].value) * parseInt(productPrices[i].price * 0.01 * (100 - productPrices[i].discountRate));
		}

		document.querySelector("#total_price_paid").innerText = this.price.getPriceNumberString(totalPricePaid);
	}

	initPlusMinusButtonListener() {
		var utils = new Utils();
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

				})
				utils.registerClickListener(plusButton, function () {
					ticketCountInput.value++;
					totalPriceSpan.innerText = price.adjustDiscountRate(productPrice.price * ticketCountInput.value, productPrice.discountRate);
					utils.removeClass(ticketCountInput, "disabled");
					utils.removeClass(minusButton, "disabled");

					reservationView.updateTotalTicketCount();
					reservationView.updateTotalPricePaid();
				})
			}.bind(this))(i);
		}
	}

	initButtonListeners() {
		var utils = new Utils();
		var isReservationFinished = this.isReservationFinished;
		//맨 위로 가기 버튼 눌렀을 때
		utils.registerClickListener(".lnk_top", function () {
			utils.scrollToTop();
		})

		this.initPlusMinusButtonListener();

		//예약 버튼을 눌렀을 때
		utils.registerClickListener(".bk_btn", function () {
			if (document.querySelector(".bk_btn_wrap").className.includes("disable")) {
				return;
			}

			if(isReservationFinished)
			{
				if (window.confirm("이미 예약이 된 상태입니다. 예약 확인 페이지로 이동하시겠습니까?"))
				{
					window.location.href = "/reserv/bookinglogin";
				}
				return;
			}

			this.reservationInfo.registerReservationInfo(this.displayInfo, this.price.productPrices);
			var errorMessages = this.reservationInfo.validateRegisterInfo();

			if (errorMessages.length == 0) {
				utils.requestAjax("POST", "/reserv/api/reservations", function () {
					alert("예약이 완료되었습니다!!");
					utils.removeClass(".header", "fade")
					isReservationFinished = true;
				}, this.reservationInfo);
			}
			else {
				var errorDialog = document.querySelector("#errorDialog");
				var resultHTML = "다음과 같은 이유로 예약에 <span style='color:red'>실패</span>하였습니다.<br><ol>";
				errorMessages.forEach(function (v) {
					resultHTML += "<li>" + v + "</li>";
				});

				resultHTML += "</ol><div style='color:darkgray;'>이 메시지를 종료하려면 <strong>ESC</strong>키를 눌러주세요.</div>"

				errorDialog.innerHTML = resultHTML;

				errorDialog.showModal();
			}
		}.bind(this));

		var agreementCheckbox = document.querySelector(".chk_agree");
		agreementCheckbox.addEventListener('change', function () {
			if (this.checked) {
				utils.removeClass(".bk_btn_wrap", "disable");
			} else {
				utils.addClass(".bk_btn_wrap", "disable");
			}
		});

		var agreementButtons = document.querySelectorAll(".btn_agreement");
		var agreementButtons_length = agreementButtons.length;
		for(var i = 0 ; i < agreementButtons_length; i++)
		{
			(function(idx){
				var agreementButton = agreementButtons[idx];
				var agreement = agreementButtons[idx].parentElement;
				utils.registerClickListener(agreementButton, function(){
					if(agreement.className.includes("open"))
					{
						utils.removeClass(agreement, "open");
						agreement.querySelector(".btn_text").innerText = "보기";
					}
					else{
						utils.addClass(agreement, "open");
						agreement.querySelector(".btn_text").innerText = "접기";
					}
				});

			})(i);
		}
	}
}

class ReservationInfo {
	static VALIDATE_ERR_MESSAGES = {
		NAME_EMPTY: "예매자 항목 입력은 필수입니다.",
		TEL_EMPTY: "연락처 항목 입력은 필수입니다.",
		EMAIL_EMPTY: "이메일 항목 입력은 필수입니다.",
		TEL_FORMAT_NOT_MATCHED: "연락처는 다음과 같은 형식이여합니다: '010-0000-0000'",
		EMAIL_FORMAT_NOT_MATCHED: "이메일은 다음과 같은 형식이여야합니다: 'aaaa@aaaa.com'",
		TICKET_NOT_CHOSEN: "티켓을 최소 한 장 이상은 구매하셔야합니다."
	}

	constructor() {
		this.initReservationInfo();
	}

	initReservationInfo() {
		this.displayInfoId = 0;
		this.prices = [];
		this.productId = 0;
		this.reservationEmail = "";
		this.reservationName = "";
		this.reservationTelephone = "";
		this.reservationYearMonthDay = "";
	}

	registerReservationInfo(displayInfo, productPrices) {

		this.initReservationInfo();

		var pricesLength = productPrices.length;
		var ticketCountInputs = document.querySelectorAll(".count_control_input");

		this.displayInfoId = displayInfo.displayInfoId;
		this.productId = displayInfo.productId;
		this.reservationEmail = document.querySelector("#email").value;
		this.reservationName = document.querySelector("#name").value;
		this.reservationTelephone = document.querySelector("#tel").value;
		this.reservationYearMonthDay = document.querySelector("#reservation_date").innerText;
		for (var i = 0; i < pricesLength; i++) {
			var count = parseInt(ticketCountInputs[i].value);
			if (count == 0) {
				continue;
			}
			var productPriceId = productPrices[i].productPriceId;
			this.prices.push({ "count": count, "productPriceId": productPriceId });
		}
	}

	validateRegisterInfo() {
		var errorMessages = [];
		var telRegex = /01[01789]-\d{3,4}-\d{4}/;
		var emailRegex = /^[\w+_]\w+@\w+\.\w+(\.\w+)?$/;
		if (this.reservationName == "") {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.NAME_EMPTY);
		}

		if (this.reservationEmail == "") {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.EMAIL_EMPTY);
		}
		else if (!this.reservationEmail.match(emailRegex)) {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.EMAIL_FORMAT_NOT_MATCHED);
		}

		if (this.reservationTelephone == "") {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.TEL_EMPTY);
		}
		else if (!this.reservationTelephone.match(telRegex)) {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.TEL_FORMAT_NOT_MATCHED);
		}

		if (this.prices.length == 0) {
			errorMessages.push(ReservationInfo.VALIDATE_ERR_MESSAGES.TICKET_NOT_CHOSEN);
		}


		return errorMessages;
	}
}

class Price {
	static WON_STRING = '<span class="price_type">원</span>';

	constructor(productPrices) {
		this.productPrices = productPrices;
	}
	getPriceNumberString(number) {
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


	lookupPriceType(priceType) {
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

				this.productPrices.forEach(function (v) {
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

	adjustDiscountRate(price, discountRate) {
		return this.getPriceNumberString(parseInt(price * (100 - discountRate) * 0.01));
	}
}


// 사이트가 처음 로드되었을 경우 호출
window.addEventListener('load', function () {
	var utils = new Utils();
	var displayInfoView;
	var displayInfoId = utils.getParameterByName("displayInfoId");


	utils.requestAjax("GET", "/reserv/api/products/" + displayInfoId, function () {
		var jsonObj = JSON.parse(this.responseText);

		var productPrices = jsonObj.productPrices;
		var displayInfo = jsonObj.displayInfo;
		var productImages = jsonObj.productImages;

		displayInfoView = new ReservationView(new Price(productPrices), displayInfo, new ReservationInfo(), false);
		displayInfoView.updateProductImageArea(productImages[0]);
		displayInfoView.updateSectionProductDetail();
		displayInfoView.updateSectionBookingTicket();
		displayInfoView.initButtonListeners();
		displayInfoView.updateReservationDate();
	}, null);

	utils.registerClickListener(".btn_back", function () {
		utils.tothePreviousSite();
	});


});