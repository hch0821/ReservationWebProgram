//예약 확인 사이트를 위한 스크립트

//예약 확인 뷰 클래스
function ReservationConfirmView() 
{
}

ReservationConfirmView.prototype.updateTabFigureSpan = function() 
{
    var figureSpans = document.querySelectorAll(".figure");
    var typeAllSpan = figureSpans[0];
    var typeWillBeUsedSpan = figureSpans[1];
    var typeUsedSpan = figureSpans[2];
    var typeCanceledSpan = figureSpans[3];

    typeAllSpan.innerText = this.reservationInfos.length;
    typeWillBeUsedSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.WILL_BE_USED];
    typeUsedSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.USED];
    typeCanceledSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.CANCELED];
}

    

// 취소/이용완료/이용예정인 티켓들의 개수를 상단에 표시해주는 함수
ReservationConfirmView.prototype.updateTabFigureSpan = function()
{
    var figureSpans = document.querySelectorAll(".figure");
    var typeAllSpan = figureSpans[0];
    var typeWillBeUsedSpan = figureSpans[1];
    var typeUsedSpan = figureSpans[2];
    var typeCanceledSpan = figureSpans[3];

    typeAllSpan.innerText = this.reservationInfos.length;
    typeWillBeUsedSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.WILL_BE_USED];
    typeUsedSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.USED];
    typeCanceledSpan.innerText = this.typeCounts[ReservationInfo.TYPE_LIST.CANCELED];
}

// 카드 머리들을 만들어주는 함수
ReservationConfirmView.prototype.makeCardHeads = function() {
    var listCards = document.querySelector(".list_cards");
    var cardHeadTemplate = document.querySelector("#card_head_template").innerText;
    if (this.typeCounts[ReservationInfo.TYPE_LIST.WILL_BE_USED] > 0) {
        var template = cardHeadTemplate;
        template = template.replace("{cardType}", "confirmed");
        template = template.replace("{bookType}", "ico_clock");
        template = template.replace("{typeName}", "이용 예정인 예약");
        listCards.innerHTML += template;
    }
    if (this.typeCounts[ReservationInfo.TYPE_LIST.USED] > 0) {
        var template = cardHeadTemplate;
        template = template.replace("{cardType}", "used");
        template = template.replace("{bookType}", "ico_check2");
        template = template.replace("{typeName}", "이용 완료/만기된 예약");
        listCards.innerHTML += template;
    }
    if (this.typeCounts[ReservationInfo.TYPE_LIST.CANCELED] > 0) {
        var template = cardHeadTemplate;
        template = template.replace("{cardType}", "cancel used");
        template = template.replace("{bookType}", "ico_cancel");
        template = template.replace("{typeName}", "취소된 예약");
        template = listCards.innerHTML += template;
    }

}

// 모든 탭들의 버튼 리스너들을 등록하는 함수
ReservationConfirmView.prototype.initTabButtonListener = function(){
    var utils = Utils.getInstance();
    var tabButtons = document.querySelectorAll(".link_summary_board");
    var tabButtonsLength = tabButtons.length;
    var reservationConfirmView = this;
    for (var i = 0; i < tabButtonsLength; i++) {
        (function (idx) {
            var button = tabButtons[idx];
            utils.registerClickListener(button, function () {
                Array.prototype.forEach.call(tabButtons, function (v) {
                    utils.removeClass(v, "on");
                });
                utils.addClass(button, "on");

                Array.prototype.forEach.call(document.querySelectorAll(".card"), function (v) {
                    utils.setVisibility(v, false);
                });

                switch (idx) {
                    case 0:
                        var cards = document.querySelectorAll(".card");
                        Array.prototype.forEach.call(cards, function (v) {
                            utils.setVisibility(v, true);
                        });

                        if (!cards || cards.length == 0) {
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(true);
                        }
                        else {
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(false);
                        }

                        break;
                    case 1:
                        var cardConfirmed = document.querySelector(".card.confirmed");
                        if (cardConfirmed) {
                            utils.setVisibility(cardConfirmed, true);
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(false);
                        }
                        else {
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(true);
                        }
                        break;
                    case 2:
                        var cardUsed = document.querySelector(".card.used");
                        if (cardUsed && cardUsed.className == "card used") {
                            utils.setVisibility(cardUsed, true);
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(false);
                        }
                        else {
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(true);
                        }
                        break;
                    case 3:
                        var cardCanceled = document.querySelector(".card.cancel.used");
                        if (cardCanceled && cardCanceled.className == "card cancel used") {
                            utils.setVisibility(cardCanceled, true);
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(false);
                        }
                        else {
                            reservationConfirmView.setVisibilityofReservationIsEmptyView(true);
                        }
                        break;
                }

            });
        })(i);
    }

}

// 탭에 해당하는 예약 목록이 아무것도 없을 때 특정 뷰를 보여주는 함수
ReservationConfirmView.prototype.setVisibilityofReservationIsEmptyView =
function(visibility) {
    var utils = Utils.getInstance();
    utils.setVisibility(".err", visibility);
}

// 카드에 해당하는 데이터를 가져오고 뷰에 뿌려주는 함수
ReservationConfirmView.prototype.initCardData = function() {
    var listHeadTemplate = document.querySelector("#list_card_template").innerHTML;
    var utils = Utils.getInstance();
    var cardCanceld = document.querySelector(".card.used.cancel");
    var cardUsed = document.querySelector(".card.used");
    var cardConfirmed = document.querySelector(".card.confirmed");

    Handlebars.registerHelper("visibility", function (type) {
        var visibility = " ";
        if (type == ReservationInfo.TYPE_LIST.CANCELED) {
            visibility = "none";
        }
        return visibility;
    })
    Handlebars.registerHelper("buttonStr", function (type) {
        var buttonStr = "";
        switch (type) {
            case ReservationInfo.TYPE_LIST.USED:
                buttonStr = "리뷰 남기기";
                break;
            case ReservationInfo.TYPE_LIST.WILL_BE_USED:
                buttonStr = "취소";
                break;
        }
        return buttonStr;
    })
    Handlebars.registerHelper("totalPrice", function (totalPrice) {
        return utils.getPriceNumberString(totalPrice);
    }.bind(this));

    Handlebars.registerHelper("buttonClass", function (type) {
        var buttonClass = "";
        switch (type) {
            case ReservationInfo.TYPE_LIST.USED:
                buttonClass = "booking_comment";
                break;
            case ReservationInfo.TYPE_LIST.WILL_BE_USED:
                buttonClass = "booking_cancel";
                break;
        }
        return buttonClass;
    }.bind(this));

    Handlebars.registerHelper("displayInfoLink", function (displayInfoId) {
        return "/reserv/detail?displayInfoId=" + displayInfoId;
    });

    Handlebars.registerHelper("price_tit", function (type) {
        if (type == ReservationInfo.TYPE_LIST.USED || type == ReservationInfo.TYPE_LIST.WILL_BE_USED) {
            return " 완료된 ";
        }
        else if (type == ReservationInfo.TYPE_LIST.CANCELED) {
            return " 취소된 ";
        }
    });

    Handlebars.registerHelper("homepageUrl", function (homepage) {
        var url = "";
        if (homepage == "") {
            return url
        }
        if (homepage.indexOf("http") == -1) {
            url = "https://" + homepage;
        }
        else {
            url = homepage;
        }

        return url;
    });

    Handlebars.registerHelper("isCanceledReservation", function (type) {
        if (type == ReservationInfo.TYPE_LIST.CANCELED) {
            return "";
        }

        return "style='display:none;'";
    });


    var bindTemplate = Handlebars.compile(listHeadTemplate);
    var resultHTML;

    var canceledReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
        return reservationInfo.type == ReservationInfo.TYPE_LIST.CANCELED;
    });
    var usedReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
        return reservationInfo.type == ReservationInfo.TYPE_LIST.USED;
    });
    var willBeUsedReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
        return reservationInfo.type == ReservationInfo.TYPE_LIST.WILL_BE_USED;
    });


    if (this.typeCounts[ReservationInfo.TYPE_LIST.CANCELED] > 0) {
        resultHTML = canceledReservationInfos.reduce(function (prev, next) {
            return prev + bindTemplate(next);
        }, "");
        cardCanceld.innerHTML += resultHTML;
    }
    if (this.typeCounts[ReservationInfo.TYPE_LIST.WILL_BE_USED] > 0) {
        resultHTML = willBeUsedReservationInfos.reduce(function (prev, next) {
            return prev + bindTemplate(next);
        }, "");
        cardConfirmed.innerHTML += resultHTML;
    }
    if (this.typeCounts[ReservationInfo.TYPE_LIST.USED] > 0) {
        resultHTML = usedReservationInfos.reduce(function (prev, next) {
            return prev + bindTemplate(next);
        }, "");
        cardUsed.innerHTML += resultHTML;
    }
}


// 예약 취소 또는 리뷰 남기기 버튼의 리스너들을 등록하는 함수
ReservationConfirmView.prototype.initCancelAndCommentButton = function() {
    var utils = Utils.getInstance();
    var reservationInfos = this.reservationInfos;
    var reservationConfirmView = this;
    Array.prototype.forEach.call(document.querySelectorAll(".booking_cancel"), function (v) {
        var cancel_button = v.querySelector(".btn");
        var reservationInfoId = cancel_button.value;

        utils.registerClickListener(cancel_button, function () {

            var clickedReservationInfo = reservationInfos.filter(function (reservationInfo) {
                return reservationInfo.id == parseInt(reservationInfoId);
            })
            if (!clickedReservationInfo) {
                alert("죄송합니다. 상품 정보를 얻어오는 데 실패하였습니다.");
                return;
            }

            clickedReservationInfo = clickedReservationInfo[0];
            reservationConfirmView.reservationInfoIdFromClickedCard = reservationInfoId;
            reservationConfirmView.showCancelationPopup(clickedReservationInfo);
        }.bind(this));
    })
    
    Array.prototype.forEach.call(document.querySelectorAll(".booking_comment"), function (v) {
        var comment_button = v.querySelector(".btn");
        var reservationInfoId = comment_button.value;
        utils.registerClickListener(comment_button, function () {
            var clickedReservationInfo = reservationInfos.filter(function (reservationInfo) {
                return reservationInfo.id == parseInt(reservationInfoId);
            })

            if (!clickedReservationInfo) {
                alert("죄송합니다. 상품 정보를 얻어오는 데 실패하였습니다.");
                return;
            }
            clickedReservationInfo = clickedReservationInfo[0];

            window.location.href = "/reserv/reviewWrite?reservationInfoId=" + clickedReservationInfo.id;
        });
    }.bind(this));
}

// 예약 취소 버튼을 눌렀을 때 확인 팝업을 띄우는 함수
ReservationConfirmView.prototype.showCancelationPopup = function(reservationInfo) {
    var utils = Utils.getInstance();
    var popupTitleHead = document.querySelector(".pop_tit");
    popupTitleHead.querySelector("span").innerText = reservationInfo.serviceName + " / " + reservationInfo.productName;
    popupTitleHead.querySelector(".sm").innerText = reservationInfo.reservationDate;
    var popup = document.querySelector(".popup_booking_wrapper");
    var confirmButton = document.querySelector(".btn_green .btn_bottom");
    utils.setVisibility(popup, true);
    popup.scrollIntoView();
	confirmButton.focus();
}


ReservationConfirmView.prototype.resetReservationInfoIdFromClickedCard = function() {
    this.reservationInfoIdFromClickedCard = undefined;
};

// 예약 확인 뷰의 모든 버튼들의 리스너를 등록하는 함수
ReservationConfirmView.prototype.initButtonListener = function() {
    var utils = Utils.getInstance();
    var reservationConfirmView = this;
    this.initTabButtonListener();
    this.initCancelAndCommentButton();

    // TOP 버튼 클릭 시
    utils.registerClickListener(".lnk_top", function () {
        utils.scrollToTop();
    });


    var closeFunc = function () {
        utils.setVisibility(".popup_booking_wrapper", false);
        reservationConfirmView.resetReservationInfoIdFromClickedCard();
    };

    utils.registerClickListener(".popup_btn_close", closeFunc);
    utils.registerClickListener(".btn_gray .btn_bottom", closeFunc);
    utils.registerClickListener(".btn_green .btn_bottom", function () {
        utils.setVisibility(".popup_booking_wrapper", false);
        var reservationInfoId = reservationConfirmView.reservationInfoIdFromClickedCard;
        if (!reservationInfoId) {
            alert("죄송합니다. 예약 취소 중 에러가 발생했습니다.");
            return;
        }
        utils.requestAjax("PUT", "/reserv/api/reservations/" + reservationInfoId, function () {
            reservationConfirmView.resetReservationInfoIdFromClickedCard();
            if (!this.responseText) {
                alert("죄송합니다. 예약 취소 중 에러가 발생했습니다.");
                return;
            }
            alert("해당 예약을 취소하였습니다.");
            history.go(0);

        }, null);
    });

    // 처음 로딩 시 첫번째 탭을 클릭하도록 함
    document.querySelector(".summary_board").children[0].querySelector(".link_summary_board").click();
}


// 카드 하나에 해당하는 예약 정보를 담을 객체
function ReservationInfo() 
{
}

// 예약 상태 목록 스태틱 변수
ReservationInfo.TYPE_LIST =  
{
    CANCELED: 0,
    USED: 1,
    WILL_BE_USED: 2
}

// 사이트가 처음 로드되었을 때 호출
window.addEventListener('load', function () {
    var utils = Utils.getInstance();
    var reservationEmail = document.querySelector(".btn_my").innerText;
    var reservationConfirmView = new ReservationConfirmView();
    if (reservationEmail.trim() == "예약확인") {
        this.alert("로그인이 필요합니다.")
        window.location.href = "/reserv/bookinglogin"
        return;
    }
    utils.requestAjax("GET", "/reserv/api/reservations?reservationEmail=" + reservationEmail, function () {
        if (!this.responseText) {
            alert("죄송합니다. 예약 정보를 얻지 못했습니다.");
            return;
        }
        var jsonObj = JSON.parse(this.responseText);
        var reservations = jsonObj.reservations;
        var reservationInfos = [];
        var typeCounts = [0, 0, 0];
        Array.prototype.forEach.call(reservations, function (v) 
        {
            var reservationInfo = new ReservationInfo();
            reservationInfo.id = v.reservationInfoId;
            reservationInfo.reservationName = v.reservationName;
            reservationInfo.serviceName = v.displayInfo.categoryName;
            reservationInfo.productName = v.displayInfo.productDescription;
            reservationInfo.reservationDate = utils.formatDatetime(v.reservationDate);
            reservationInfo.createDate = utils.formatDatetime(v.createDate);
            reservationInfo.modifyDate = utils.formatDatetime(v.modifyDate);
            reservationInfo.placeName = v.displayInfo.placeName;
            reservationInfo.homepage = v.displayInfo.homepage;
            reservationInfo.totalPrice = v.totalPrice;
            reservationInfo.displayInfoId = v.displayInfoId;
            if (v.cancelYn) {
                reservationInfo.type = ReservationInfo.TYPE_LIST.CANCELED;
                typeCounts[reservationInfo.type]++;
            }
            else {

                var reservationDate = v.reservationDate.split(" ");
                var date = reservationDate[0];
                var time = reservationDate[1].split(".")[0];
                var format = date + "T"+ time; //new Date('1995-12-17T03:24:00'); //IE 호환을 위한 포맷

                reservationDate = new Date(format); 
                var todayDate = new Date();


                if (reservationDate < todayDate) {
                    reservationInfo.type = ReservationInfo.TYPE_LIST.USED;
                    typeCounts[reservationInfo.type]++;
                }
                else {
                    reservationInfo.type = ReservationInfo.TYPE_LIST.WILL_BE_USED;
                    typeCounts[reservationInfo.type]++;
                }
            }
            reservationInfos.push(reservationInfo);
        });

        reservationConfirmView.reservationInfos = reservationInfos;
        reservationConfirmView.typeCounts = typeCounts;
        reservationConfirmView.updateTabFigureSpan();
        reservationConfirmView.makeCardHeads();
        reservationConfirmView.initCardData();
        reservationConfirmView.initButtonListener();
    }, null);
});