//예약 확인 사이트를 위한 스크립트

//예약 확인 뷰 클래스
class ReservationConfirmView {
    constructor(reservationInfos, typeCounts) {
        this.reservationInfos = reservationInfos; //사용자가 예약했던 모든 예약 정보를 가지고 있는 객체
        this.typeCounts = typeCounts;  //취소/이용완료/이용예정 인 티켓들의 각 갯수를 가지고 있는 배열
    }

    //취소/이용완료/이용예정인 티켓들의 개수를 상단에 표시해주는 함수
    updateTabFigureSpan() {
        var figureSpans = document.querySelectorAll(".figure");
        var typeAllSpan = figureSpans[0];
        var typeWillBeUsedSpan = figureSpans[1];
        var typeUsedSpan = figureSpans[2];
        var typeCanceledSpan = figureSpans[3];

        typeAllSpan.innerText = this.reservationInfos.length;
        typeWillBeUsedSpan.innerText = this.typeCounts[ReservationInfo.TypeList.WILL_BE_USED];
        typeUsedSpan.innerText = this.typeCounts[ReservationInfo.TypeList.USED];
        typeCanceledSpan.innerText = this.typeCounts[ReservationInfo.TypeList.CANCELED];
    }

    //카드 머리들을 만들어주는 함수
    makeCardHeads() {
        var listCards = document.querySelector(".list_cards");
        var cardHeadTemplate = document.querySelector("#card_head_template").innerText;
        if (this.typeCounts[ReservationInfo.TypeList.WILL_BE_USED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "confirmed");
            template = template.replace("{bookType}", "ico_clock");
            template = template.replace("{typeName}", "이용 예정인 예약");
            listCards.innerHTML += template;
        }
        if (this.typeCounts[ReservationInfo.TypeList.USED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "used");
            template = template.replace("{bookType}", "ico_check2");
            template = template.replace("{typeName}", "이용 완료/만기된 예약");
            listCards.innerHTML += template;
        }
        if (this.typeCounts[ReservationInfo.TypeList.CANCELED] > 0) {
            var template = cardHeadTemplate;
            template = template.replace("{cardType}", "used cancel");
            template = template.replace("{bookType}", "ico_cancel");
            template = template.replace("{typeName}", "취소된 예약");
            template = listCards.innerHTML += template;
        }

    }

    //모든 탭들의 버튼 리스너들을 등록하는 함수
    initTabButtonListener() {
        var utils = Utils.getInstance();
        var tabButtons = document.querySelectorAll(".link_summary_board");
        var tabButtonsLength = tabButtons.length;
        var reservationConfirmView = this;
        for (var i = 0; i < tabButtonsLength; i++) {
            (function (idx) {
                var button = tabButtons[idx];
                utils.registerClickListener(button, function () {
                    tabButtons.forEach(function (v) {
                        utils.removeClass(v, "on");
                    });
                    utils.addClass(button, "on");
                    document.querySelectorAll(".card").forEach(function (v) {
                        utils.setVisibility(v, false);
                    });
                    switch (idx) {
                        case 0:
                            var cards = document.querySelectorAll(".card");
                            cards.forEach(function (v) {
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
                            if (cardUsed) {
                                utils.setVisibility(cardUsed, true);
                                reservationConfirmView.setVisibilityofReservationIsEmptyView(false);
                            }
                            else {
                                reservationConfirmView.setVisibilityofReservationIsEmptyView(true);
                            }
                            break;
                        case 3:
                            var cardCanceld = document.querySelector(".card.used.cancel");
                            if (cardCanceld) {
                                utils.setVisibility(cardCanceld, true);
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

    //탭에 해당하는 예약 목록이 아무것도 없을 때 특정 뷰를 보여주는 함수
    setVisibilityofReservationIsEmptyView(visibility) {
        var utils = Utils.getInstance();
        utils.setVisibility(".err", visibility);
    }

    //카드에 해당하는 데이터를 가져오고 뷰에 뿌려주는 함수
    initCardData() {
        var listHeadTemplate = document.querySelector("#list_card_template").innerHTML;

        var cardCanceld = document.querySelector(".card.used.cancel");
        var cardUsed = document.querySelector(".card.used");
        var cardConfirmed = document.querySelector(".card.confirmed");

        Handlebars.registerHelper("visibility", function (type) {
            var visibility = " ";
            if (type == ReservationInfo.TypeList.CANCELED) {
                visibility = "none";
            }
            return visibility;
        })
        Handlebars.registerHelper("buttonStr", function (type) {
            var buttonStr = "";
            switch (type) {
                case ReservationInfo.TypeList.USED:
                    buttonStr = "리뷰 남기기";
                    break;
                case ReservationInfo.TypeList.WILL_BE_USED:
                    buttonStr = "취소";
                    break;
            }
            return buttonStr;
        })
        Handlebars.registerHelper("totalPrice", function (totalPrice) {
            return this.getPriceNumberString(totalPrice);
        }.bind(this));

        Handlebars.registerHelper("buttonClass", function (type) {
            var buttonClass = "";
            switch (type) {
                case ReservationInfo.TypeList.USED:
                    buttonClass = "booking_comment";
                    break;
                case ReservationInfo.TypeList.WILL_BE_USED:
                    buttonClass = "booking_cancel";
                    break;
            }
            return buttonClass;
        }.bind(this));

        Handlebars.registerHelper("displayInfoLink", function (displayInfoId) {
            return "/reserv/detail?displayInfoId=" + displayInfoId;
        });

        Handlebars.registerHelper("price_tit", function (type) {
            if (type == ReservationInfo.TypeList.USED || type == ReservationInfo.TypeList.WILL_BE_USED) {
                return " 완료된 ";
            }
            else if (type == ReservationInfo.TypeList.CANCELED) {
                return " 취소된 ";
            }
        });

        Handlebars.registerHelper("homepageUrl", function (homepage) {
            var url = "";
            if(homepage != "" && !homepage.includes("http")){
                url = "https://" + homepage;
            }
            return url;
        });

        var bindTemplate = Handlebars.compile(listHeadTemplate);
        var resultHTML;

        var canceledReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
            return reservationInfo.type == ReservationInfo.TypeList.CANCELED;
        });
        var usedReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
            return reservationInfo.type == ReservationInfo.TypeList.USED;
        });
        var willBeUsedReservationInfos = this.reservationInfos.filter(function (reservationInfo) {
            return reservationInfo.type == ReservationInfo.TypeList.WILL_BE_USED;
        });


        if (this.typeCounts[ReservationInfo.TypeList.CANCELED] > 0) {
            resultHTML = canceledReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardCanceld.innerHTML += resultHTML;
        }
        if (this.typeCounts[ReservationInfo.TypeList.WILL_BE_USED] > 0) {
            resultHTML = willBeUsedReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardConfirmed.innerHTML += resultHTML;
        }
        if (this.typeCounts[ReservationInfo.TypeList.USED] > 0) {
            resultHTML = usedReservationInfos.reduce(function (prev, next) {
                return prev + bindTemplate(next);
            }, "");
            cardUsed.innerHTML += resultHTML;
        }
    }


    //예약 취소 또는 리뷰 남기기 버튼의 리스너들을 등록하는 함수
    initCancelAndCommentButton() {
        var utils = Utils.getInstance();
        var reservationInfos = this.reservationInfos;
        var reservationConfirmView = this;
        document.querySelectorAll(".booking_cancel").forEach(function (v) {
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

                reservationConfirmView.showCancelationPopup(clickedReservationInfo);
            }.bind(this));
        });
        document.querySelectorAll(".booking_comment").forEach(function (v) {
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

    //예약 취소 버튼을 눌렀을 때 확인 팝업을 띄우는 함수
    showCancelationPopup(reservationInfo) {
        var utils = Utils.getInstance();
        utils.setVisibility(".popup_booking_wrapper", true);
        var popupTitleHead = document.querySelector(".pop_tit");
        popupTitleHead.querySelector("span").innerText = reservationInfo.serviceName + " / " + reservationInfo.productName;
        popupTitleHead.querySelector(".sm").innerText = reservationInfo.reservationDate;
        var closeFunc = function () {
            utils.setVisibility(".popup_booking_wrapper", false);
        };
        utils.registerClickListener(".popup_btn_close", closeFunc);
        utils.registerClickListener(".btn_gray .btn_bottom", closeFunc);
        utils.registerClickListener(".btn_green .btn_bottom", function () {
            utils.requestAjax("PUT", "/reserv/api/reservations/" + reservationInfo.id, function () {
                if (!this.responseText) {
                    alert("죄송합니다. 예약 취소 중 에러가 발생했습니다.");
                    return;
                }
                alert("해당 예약을 취소하였습니다.");
                history.go(0);

            }, null);
        });
    }

     //금액 값을 뒤자리에서부터 숫자 세 개당 , 를 반복해서 찍어주는 함수
	//ex 100000 ==> 100,000
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


    //예약 확인 뷰의 모든 버튼들의 리스너를 등록하는 함수
    initButtonListener() {
        var utils = Utils.getInstance();
        this.initTabButtonListener();
        this.initCancelAndCommentButton();

        //TOP 버튼 클릭 시
        utils.registerClickListener(".lnk_top", function () {
            utils.scrollToTop();
        });

        //처음 로딩 시 첫번째 탭을 클릭하도록 함
        document.querySelector(".summary_board").children[0].querySelector(".link_summary_board").click();
    }

}

//카드 하나에 해당하는 예약 정보를 담을 객체
class ReservationInfo {

    //예약 상태 목록을 반환하는 프로퍼티
    static get TypeList() {
        const typelist = {
            CANCELED: 0,
            USED: 1,
            WILL_BE_USED: 2
        };
        return typelist;

    }

    constructor(id, reservationName, type, serviceName, productName, reservationDate, placeName, homepage, totalPrice, displayInfoId) {
        this.id = id;
        this.reservationName = reservationName;
        this.type = type;
        this.serviceName = serviceName;
        this.productName = productName;
        this.reservationDate = reservationDate;
        this.placeName = placeName;
        this.homepage = homepage;
        this.totalPrice = totalPrice;
        this.displayInfoId = displayInfoId;
    }
}


//사이트가 처음 로드되었을 때 호출
window.addEventListener('load', function () {
    var utils = Utils.getInstance();
    var reservationEmail = document.querySelector(".btn_my").innerText;
    if (reservationEmail == "예약확인") {
        this.alert("로그인이 필요합니다.")
        window.location.href = "/reserv/bookinglogin"
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
        reservations.forEach(function (v) {
            var reservationInfo = new ReservationInfo();
            reservationInfo.id = v.reservationInfoId;
            reservationInfo.reservationName = v.reservationName;
            reservationInfo.serviceName = v.displayInfo.categoryName;
            reservationInfo.productName = v.displayInfo.productDescription;
            reservationInfo.reservationDate = v.reservationDate.split(" ")[0].split("-").join(". ");
            reservationInfo.placeName = v.displayInfo.placeName;
            reservationInfo.homepage = v.displayInfo.homepage;
            reservationInfo.totalPrice = v.totalPrice;
            reservationInfo.displayInfoId = v.displayInfoId;


            if (v.cancelYn) {
                reservationInfo.type = ReservationInfo.TypeList.CANCELED;
                typeCounts[reservationInfo.type]++;
            }
            else {
                var reservationDate = new Date(v.reservationDate);
                var todayDate = new Date();

                if (reservationDate < todayDate) {
                    reservationInfo.type = ReservationInfo.TypeList.USED;
                    typeCounts[reservationInfo.type]++;
                }
                else {
                    reservationInfo.type = ReservationInfo.TypeList.WILL_BE_USED;
                    typeCounts[reservationInfo.type]++;
                }
            }
            reservationInfos.push(reservationInfo);
        });
        var reservationConfirmView = new ReservationConfirmView(reservationInfos, typeCounts);
        reservationConfirmView.updateTabFigureSpan();
        reservationConfirmView.makeCardHeads();
        reservationConfirmView.initCardData();
        reservationConfirmView.initButtonListener();
    }, null);
});