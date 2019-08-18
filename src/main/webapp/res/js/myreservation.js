// 사이트가 처음 로드되었을 경우 호출


class ReservationConfirmView {
    constructor(reservationInfos, typeCounts) {
        this.reservationInfos = reservationInfos;
        this.typeCounts = typeCounts;
    }

    updateTabFigureSpan() {
        var figureSpans = document.querySelectorAll(".figure");
        var typeAllSpan = figureSpans[0];
        var typeWillBeUsedSpan = figureSpans[1];
        var typeUsedSpan = figureSpans[2];
        var typeCanceledSpan = figureSpans[3];

        typeAllSpan.innerText = this.reservationInfos.length;
        typeWillBeUsedSpan.innerText = this.typeCounts[ReservationInfo.type.TYPE_WILL_BE_USED];
        typeUsedSpan.innerText = this.typeCounts[ReservationInfo.type.TYPE_USED];
        typeCanceledSpan.innerText = this.typeCounts[ReservationInfo.type.TYPE_CANCELED];
    }


    makeCardHeads() {
        var listCards = document.querySelector(".list_cards");
        var cardHeadTemplate = document.querySelector("#card_head_template").innerText;
        if (this.typeCounts[ReservationInfo.type.TYPE_WILL_BE_USED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "confirmed");
            template = template.replace("{bookType}", "ico_clock");
            template = template.replace("{typeName}", "이용 예정인 예약");
            listCards.innerHTML += template;
        }
        if (this.typeCounts[ReservationInfo.type.TYPE_USED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "used");
            template = template.replace("{bookType}", "ico_check2");
            template = template.replace("{typeName}", "이용 완료/만기된 예약");
            listCards.innerHTML += template;
        }
        if (this.typeCounts[ReservationInfo.type.TYPE_CANCELED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "used cancel");
            template = template.replace("{bookType}", "ico_cancel");
            template = template.replace("{typeName}", "취소된 예약");
            template = listCards.innerHTML += template;
        }

    }

    initTabButtonListener() 
    {
        var utils = new Utils();
        var tabButtons = document.querySelectorAll(".link_summary_board");
        var tabButtonsLength = tabButtons.length;
        for (var i = 0; i < tabButtonsLength; i++) {
            (function (idx) 
            {
                var button = tabButtons[idx];
                utils.registerClickListener(button, function () {
                    tabButtons.forEach(function(v){
                        utils.removeClass(v, "on");
                    });
                    utils.addClass(button, "on");
                    document.querySelectorAll(".card").forEach(function (v) {
                        utils.setVisibility(v, false);
                    });
                    switch (idx) 
                    {
                    case 0:
                        document.querySelectorAll(".card").forEach(function (v) {
                            utils.setVisibility(v, true);
                        });
                        break;
                    case 1:
                        var cardConfirmed = document.querySelector(".card.confirmed");
                        if (cardConfirmed) {
                            utils.setVisibility(cardConfirmed, true);
                        }
                        break;
                    case 2:
                        var cardUsed = document.querySelector(".card.used");
                        if (cardUsed) {
                            utils.setVisibility(cardUsed, true);
                        }
                        break;
                    case 3:
                        var cardCanceld= document.querySelector(".card.used.cancel");
                        if (cardCanceld) {
                            utils.setVisibility(cardCanceld, true);
                        }
                        break;
                    }

                });
            })(i);
        }

    }

    initCardData() {
        var listHeadTemplate = document.querySelector("#list_card_template").innerHTML;
       
        var cardCanceld= document.querySelector(".card.used.cancel");
        var cardUsed = document.querySelector(".card.used");
        var cardConfirmed = document.querySelector(".card.confirmed");

        Handlebars.registerHelper("visibility", function(type){
            var visibility = " ";
            if(type == ReservationInfo.type.TYPE_CANCELED){
                visibility = "none";
            }
            return visibility;
        })
        Handlebars.registerHelper("buttonStr", function(type){
            var buttonStr = "";
            switch(type){
                case ReservationInfo.type.TYPE_USED:
                    buttonStr = "리뷰 남기기";
                    break;
                case ReservationInfo.type.TYPE_WILL_BE_USED:
                    buttonStr = "취소";
                    break;
            }
            return buttonStr;
        })
        Handlebars.registerHelper("totalPrice", function(totalPrice){
            return this.getPriceNumberString(totalPrice);
        }.bind(this));

        Handlebars.registerHelper("buttonClass", function(type){
            var buttonClass = "";
            switch(type){
                case ReservationInfo.type.TYPE_USED:
                    buttonClass = "booking_comment";
                    break;
                case ReservationInfo.type.TYPE_WILL_BE_USED:
                    buttonClass = "booking_cancel";
                    break;
            }
            return buttonClass;
        }.bind(this));

        var bindTemplate = Handlebars.compile(listHeadTemplate);
        var resultHTML;

        var canceledReservationInfos = this.reservationInfos.filter(function(reservationInfo){
            return reservationInfo.type == ReservationInfo.type.TYPE_CANCELED;
        });
        var usedReservationInfos = this.reservationInfos.filter(function(reservationInfo){
            return reservationInfo.type == ReservationInfo.type.TYPE_USED;
        });
        var willBeUsedReservationInfos = this.reservationInfos.filter(function(reservationInfo){
            return reservationInfo.type == ReservationInfo.type.TYPE_WILL_BE_USED;
        });

        
        if (this.typeCounts[ReservationInfo.type.TYPE_CANCELED] > 0) {
            resultHTML = canceledReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardCanceld.innerHTML += resultHTML;
        }
        if (this.typeCounts[ReservationInfo.type.TYPE_WILL_BE_USED] > 0) {
            resultHTML = willBeUsedReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardConfirmed.innerHTML += resultHTML;
        }
        if (this.typeCounts[ReservationInfo.type.TYPE_USED] > 0) {
            resultHTML = usedReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardUsed.innerHTML += resultHTML;
        }
    }



    initCancelAndCommentButton(){
        var utils = new Utils();
        var reservationInfos = this.reservationInfos;
        document.querySelectorAll(".booking_cancel").forEach(function(v){
            var cancel_button = v.querySelector(".btn");
            var reservationInfoId = cancel_button.value;
            utils.registerClickListener(cancel_button, function(){
                var clickedReservationInfo = reservationInfos.filter(function(reservationInfo){
                    return reservationInfo.id == parseInt(reservationInfoId);
                })
                console.log(clickedReservationInfo);
            });
        });
        document.querySelectorAll(".booking_comment").forEach(function(v){
            var comment_button = v.querySelector(".btn");
            var reservationInfoId = comment_button.value;
            utils.registerClickListener(comment_button, function(){
                var clickedReservationInfo = reservationInfos.filter(function(reservationInfo){
                    return reservationInfo.id == parseInt(reservationInfoId);
                })
                console.log(clickedReservationInfo);
            });
        });
    }


    showPopup(reservationInfo){
        var utils = new Utils();
        utils.setVisibility(".popup_booking_wrapper", true);
        document.querySelector(".pop_tit").innerText = reservationInfo.serviceName + " / " +reservationInfo.productName;
        document.querySelector(".sm").innerText = reservationInfo.reservationDate;
        var closeFunc = function(){
            utils.setVisibility(".popup_booking_wrapper", false);
        };
        utils.registerClickListener(".popup_btn_close", closeFunc);
        utils.registerClickListener(".btn_gray .btn_bottom", closeFunc);
        utils.registerClickListener(".btn_green .btn_bottom", function(){
            utils.requestAjax("PUT", "/reserv/api/reservations/"+reservationInfo.id, function(){
                
            }, null);
        });
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
    


    initButtonListener(){
        var utils = new Utils();
        this.initTabButtonListener();
        this.initCancelAndCommentButton();
        utils.registerClickListener(".lnk_top", function(){
            utils.scrollToTop();
        });
    }

}

class ReservationInfo {

    static type = {
        TYPE_CANCELED: 0,
        TYPE_USED: 1,
        TYPE_WILL_BE_USED: 2
    }

    constructor(id, type, serviceName, productName, reservationDate, placeName, homepage, totalPrice) {
        this.id = id;
        this.type = type;
        this.serviceName = serviceName;
        this.productName = productName;
        this.reservationDate = reservationDate;
        this.placeName = placeName;
        this.homepage = homepage;
        this.totalPrice = totalPrice;
    }
}


window.addEventListener('load', function () {
    var utils = new Utils();
    var reservationEmail = document.querySelector(".btn_my").innerText;
    if (reservationEmail == "예약확인") {
        this.alert("로그인이 필요합니다.")
        window.location.href = "/reserv/bookinglogin"
    }
    utils.requestAjax("GET", "/reserv/api/reservations?reservationEmail=" + reservationEmail, function () {
        var jsonObj = JSON.parse(this.responseText);
        console.log(jsonObj);
        var reservations = jsonObj.reservations;
        var reservationInfos = [];
        var typeCounts = [0, 0, 0];
        reservations.forEach(function (v) {
            var reservationInfo = new ReservationInfo();
            reservationInfo.id = v.reservationInfoId;
            reservationInfo.serviceName = v.displayInfo.categoryName;
            reservationInfo.productName = v.displayInfo.productDescription;
            reservationInfo.reservationDate = v.reservationDate.split(" ")[0].split("-").join(". ");
            reservationInfo.placeName = v.displayInfo.placeName;
            reservationInfo.homepage = v.displayInfo.homepage;
            reservationInfo.totalPrice = v.totalPrice;
            if (v.cancelYn) {
                reservationInfo.type = ReservationInfo.type.TYPE_CANCELED;
                typeCounts[reservationInfo.type]++;
            }
            else {
                var reservationDate = new Date(v.reservationDate);
                var todayDate = new Date();

                if (reservationDate < todayDate) {
                    reservationInfo.type = ReservationInfo.type.TYPE_USED;
                    typeCounts[reservationInfo.type]++;
                }
                else {
                    reservationInfo.type = ReservationInfo.type.TYPE_WILL_BE_USED;
                    typeCounts[reservationInfo.type]++;
                }
            }
            reservationInfos.push(reservationInfo);
        });

        console.log(reservationInfos);
        var reservationConfirmView = new ReservationConfirmView(reservationInfos, typeCounts);
        console.log(reservationConfirmView);

        reservationConfirmView.updateTabFigureSpan();
        reservationConfirmView.makeCardHeads();
        reservationConfirmView.initCardData();
        reservationConfirmView.initButtonListener();
    }, null);


});