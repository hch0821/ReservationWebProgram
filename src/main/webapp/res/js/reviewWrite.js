//리뷰 쓰기 페이지를 위한 스크립트

var utils = undefined;
//리뷰 쓰기 뷰 클래스
function ReviewWriteView(formData) {
    this.formData = formData;
}

//별 버튼을 누를 때 동작하는 리스너를 등록하는 함수
ReviewWriteView.prototype.registerRatingStarsClickListener = function () {

    var starInputs = document.querySelector(".rating").querySelectorAll("input");
    var starInputsLength = starInputs.length;
    var scoreText = document.querySelector(".star_rank");
    Array.prototype.forEach.call(starInputs, function (starInput) {
        utils.registerClickListener(starInput, function () {
            var idx;

            for (idx = 0; idx < starInputsLength; idx++) {
                utils.removeClass(starInputs[idx], "checked");
                starInputs[idx].checked = false;
            }

            for (idx = 0; idx < starInput.value; idx++) {
                utils.addClass(starInputs[idx], "checked");
                starInputs[idx].checked = true;
            }

            scoreText.innerText = starInput.value;

            utils.removeClass(".star_rank", "gray_star");
        });
    });
}

//댓글 텍스트 영역 관련 리스너 등록 함수
ReviewWriteView.prototype.registerTextAreaListener = function (commentObj) {
    var reviewCommentBlockerAnchor = document.querySelector(".review_write_info");
    var reviewCommentTextArea = document.querySelector(".review_textarea");
    var textLengthSpan = document.querySelector(".cur_text_length");
    utils.registerClickListener(reviewCommentBlockerAnchor, function () {
        utils.setVisibility(reviewCommentBlockerAnchor, false);
        reviewCommentTextArea.focus();
    });

    reviewCommentTextArea.addEventListener("focusout", function () {
        if (reviewCommentBlockerAnchor.innerText.trim() == reviewCommentTextArea.value.trim()) {
            utils.setVisibility(reviewCommentBlockerAnchor, true);
        }
    });

    reviewCommentTextArea.addEventListener("input", function () {
        textLengthSpan.innerText = reviewCommentTextArea.value.length;
    });
}

// 리뷰 사진 데이터, 썸네일을 모두 지우는 함수
ReviewWriteView.prototype.resetReviewImageFileOpenInput = function (formData) {
    var reviewImageFileOpenInput = document.querySelector("#reviewImageFileOpenInput");
    var imageThumbnail = document.querySelector(".item_preview_thumbs .item_thumb")
    reviewImageFileOpenInput.value = "";
    utils.setVisibility(".item_preview_thumbs .item", false);
    imageThumbnail.src = "";
    formData.set("attachedImage", []);
}

//리뷰 사진 input, 사진 삭제 버튼 관련 리스너를 등록하는 함수
ReviewWriteView.prototype.registerReviewPhotoWriteListener = function () {
    var reviewImageFileOpenInput = document.querySelector("#reviewImageFileOpenInput");
    var imageThumbnail = document.querySelector(".item_preview_thumbs .item_thumb")
    var deleteImageButton = document.querySelector(".item_preview_thumbs .anchor");
    var validation = Validation.getInstance();
    var reviewWriteView = this;
    reviewImageFileOpenInput.addEventListener("change", function (evt) {
        var image = evt.target.files[0];
        if (!image) {
            reviewWriteView.resetReviewImageFileOpenInput(reviewWriteView.formData);
            return;
        }

        if (!validation.validImageType(image)) {
            alert("이미지 파일형식은 JPG 또는 PNG이여야합니다.");
            reviewWriteView.resetReviewImageFileOpenInput(reviewWriteView.formData);
            return;
        }

        if (!validation.checkImageSize(image)) {
            alert("이미지 크기가 최대 크기인 10MB를 초과했습니다.");
            reviewImageFileOpenInput.value = "";
            utils.setVisibility(".item_preview_thumbs .item", false);
            reviewWriteView.resetReviewImageFileOpenInput(reviewWriteView.formData);
            return;
        }

        utils.setVisibility(".item_preview_thumbs .item", true);
        imageThumbnail.src = window.URL.createObjectURL(image);
    });

    utils.registerClickListener(deleteImageButton, function () {
        reviewWriteView.resetReviewImageFileOpenInput(reviewWriteView.formData);
    });
}

//댓글 이미지 파일의 다운로드 url을 ajax로 요청하고, 결과 데이터를 File 객체로 받는 작업
//참고 링크 : https://stackoverflow.com/a/41752161
ReviewWriteView.prototype.getImageFileFromServer = function (fileUrl, fileName, callback) {
    var oReq = new XMLHttpRequest();
    oReq.open('GET', fileUrl, true);
    oReq.responseType = 'blob';
    oReq.onload = function () {
        if (this.status == 200) {
            var imageFile = new File([this.response], fileName);
            callback(imageFile);
        }
    };
    oReq.send();
}

//이미 쓴 리뷰가 있다면 불러오는 함수
ReviewWriteView.prototype.loadPreviousReview = function (displayInfoId, reservationInfoId) {
    var reviewWriteView = this;
    var titleSpan = document.querySelector(".title");
    var registerButtonText = document.querySelector(".box_bk_btn .btn_txt");
    var starInputs = document.querySelector(".rating").querySelectorAll("input");
    var reviewCommentBlockerAnchor = document.querySelector(".review_write_info");
    var reviewCommentTextArea = document.querySelector(".review_textarea");
    var imageThumbnail = document.querySelector(".item_preview_thumbs .item_thumb")
    utils.requestAjax("GET", "/reserv/api/products/" + displayInfoId, function () {
        if (!this.responseText || this.status != 200) {
            utils.showErrorMessage("상품 정보를 불러오던 중에 에러가 발생했습니다.", utils.tothePreviousSite);
            return;
        }
        var jsonObj = JSON.parse(this.responseText);
        var productId = jsonObj.displayInfo.productId;

        reviewWriteView.registerAddReviewButton(productId, reservationInfoId);

        titleSpan.innerText = jsonObj.displayInfo.productDescription;
        var comments = jsonObj.comments;
        if (!comments) {
            registerButtonText.innerText = "리뷰 등록";
            return;
        }
        comments = comments.filter(function (comment) {
            return comment.reservationInfoId == reservationInfoId;
        })
        if (comments.length < 1) {
            registerButtonText.innerText = "리뷰 등록";
            return;
        }
        var commentObj = comments[0];
        var commentImage = undefined;
        starInputs[commentObj.score - 1].click();
        reviewCommentBlockerAnchor.innerText = commentObj.comment;
        reviewCommentTextArea.value = commentObj.comment;
        document.querySelector(".cur_text_length").innerText = reviewCommentTextArea.value.length;

        commentImage = commentObj.commentImages.filter(function (image) {
            return !image.deleteFlag;
        });
        if (commentImage && commentImage.length > 0) {
            commentImage = commentImage[0];
            utils.setVisibility(".item_preview_thumbs .item", true);
            const fileUrl = "/reserv/commentimage?id=" + commentImage.imageId;

            reviewWriteView.getImageFileFromServer(fileUrl, commentImage.fileName, function (imageFile) {
                reviewWriteView.formData.set("attachedImage", imageFile);
            })

            imageThumbnail.src = fileUrl;
        }
        registerButtonText.innerText = "리뷰 수정";
    }, null);

}

//리뷰 등록 버튼 클릭 리스너를 등록하는 함수
ReviewWriteView.prototype.registerAddReviewButton = function (productId, reservationInfoId) {
    var reviewWriteView = this;

    var scoreText = document.querySelector(".star_rank");
    var reviewCommentTextArea = document.querySelector(".review_textarea");
    var reviewImageFileOpenInput = document.querySelector("#reviewImageFileOpenInput");
    var validation = Validation.getInstance();
    var formData = reviewWriteView.formData;

    utils.registerClickListener(".bk_btn", function () {
        var registerOrModifyStr = document.querySelector(".box_bk_btn .btn_txt").innerText.split(" ")[1];

        if (!formData.get("attachedImage")) {
            var files = reviewImageFileOpenInput.files;
            if (files && files.length > 0) {
                formData.set("attachedImage", files[0]);
            }
            else {
                formData.set("attachedImage", null);
            }
        }

        formData.set("comment", reviewCommentTextArea.value.trim());
        formData.set("productId", productId);
        formData.set("score", scoreText.innerText);

        var errorMessages = validation.validateForm(formData, reservationInfoId)
        if (errorMessages.length > 0) {
            alert("죄송합니다. 다음과 같은 이유로 리뷰 " + registerOrModifyStr + "에 실패하였습니다.\n" + errorMessages.join("\n"));
            return;
        }

        //서버에 리뷰 등록/수정 요청
        var oReq = new XMLHttpRequest();
        oReq.open("POST", "/reserv/api/reservations/" + reservationInfoId + "/comments", true);
        oReq.onload = function () {
            if (oReq.status == 200) {
                alert("리뷰가 성공적으로 " + registerOrModifyStr + "되었습니다.");
                window.location.href = "/reserv/checkSession";
            } else {
                alert("알 수 없는 이유로 리뷰 " + registerOrModifyStr + "에 실패하였습니다.");
            }
        };

        oReq.send(reviewWriteView.formData);

    });
}

//모든 리스너를 등록하는 함수
ReviewWriteView.prototype.registerAllListeners = function () {
    utils.registerClickListener(".btn_back", utils.tothePreviousSite);
    utils.registerClickListener(".gototop", utils.scrollToTop);
    this.registerRatingStarsClickListener();
    this.registerTextAreaListener();
    this.registerReviewPhotoWriteListener();
}

// 유효성 검사 관련 클래스
function Validation() {

}

Validation.getInstance = function () {
    if (!this.instance) {
        this.instance = new Validation();
    }
    return this.instance;
}

//이미지 형식이 png, jpg인지 확인하는 함수
Validation.prototype.validImageType = function (image) {
    const imageTypes = ['image/jpeg', 'image/png', 'image/jpg'];
    return imageTypes.indexOf(image.type) > -1;
}

//이미지의 크기가 최대 크기를 넘는지 확인하는 함수
Validation.prototype.checkImageSize = function (image) {
    const maxImageSize = 10 * 1024 * 1024; // 10MB
    return image.size <= maxImageSize;
}

//이미지 파일의 유효성을 제외한 폼의 유효성을 검사하는 함수
Validation.prototype.validateForm = function (formData, reservationInfoId) {

    var ERROR_MESSAGE =
    {
        COMMENT_NOT_VALID: "한줄 평은 최소 5자 이상 400자 이하로 써주세요!",
        PRODUCTINFO_NOT_VALID: "상품 정보가 올바르지 않습니다.",
        SCORE_NOT_VALID: "평점 정보가 올바르지 않습니다.",
        RESERVATION_INFO_NOT_VALID: "예약 정보가 올바르지 않습니다."
    };
    var errorMessages = [];
    var comment = formData.get("comment");
    var productId = formData.get("productId");
    var score = formData.get("score");
    var textLimitRegex = /^[\w|\W]{5,400}$/;  //글자수 : 5~ 400글자 제한
    var positiveNumberRegex = /^\d+$/; //양의 정수인지 확인
    var scoreRegex = /^[1-5]$/; //점수 : 1~5점 제한 (별점에 소수자리를 구현하지 않았으므로 정수만 받도록 함)

    if (!comment || !comment.match(textLimitRegex)) {
        errorMessages.push(ERROR_MESSAGE.COMMENT_NOT_VALID);
    }

    if (!score || !score.match(scoreRegex)) {
        errorMessages.push(ERROR_MESSAGE.SCORE_NOT_VALID);
    }

    if (!productId || !productId.match(positiveNumberRegex)) {
        errorMessages.push(ERROR_MESSAGE.PRODUCTINFO_NOT_VALID);
    }

    if (!reservationInfoId || !reservationInfoId.match(positiveNumberRegex)) {
        errorMessages.push(ERROR_MESSAGE.RESERVATION_INFO_NOT_VALID);
    }
    return errorMessages;
}


// 사이트가 처음 로드되었을 때 호출
window.addEventListener('load', function () {
    utils = Utils.getInstance();

    var emailText = document.querySelector(".btn_my").innerText;
    if (emailText.trim() == "예약확인") {
        this.alert("로그인이 필요합니다.")
        window.location.href = "/reserv/bookinglogin"
        return;
    }

    var reviewWriteView = new ReviewWriteView(new FormData());

    var reservationInfoId = utils.getParameterByName("reservationInfoId");
    var displayInfoId = utils.getParameterByName("displayInfoId");
    if (!reservationInfoId || reservationInfoId == "" || !displayInfoId || displayInfoId == "") {
        utils.showErrorMessage("유효하지 않은 요청입니다.", utils.tothePreviousSite);
        return;
    }

    var previousSiteUrl = utils.getPreviousSiteUrl();
    if (!previousSiteUrl || previousSiteUrl.indexOf("myreservation") == -1) {
        utils.showErrorMessage("잘못된 접근입니다.", function () {
            window.location.href = "/reserv/";
        });
    }

    reviewWriteView.loadPreviousReview(displayInfoId, reservationInfoId);
    reviewWriteView.registerAllListeners();
});