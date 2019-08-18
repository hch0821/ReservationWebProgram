// 사이트가 처음 로드되었을 경우 호출


class ReservationConfirmView{

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
}

window.addEventListener('load', function () {
    var utils = new Utils();
    var reservationEmail = document.querySelector(".btn_my").innerText;
    if(reservationEmail == "예약확인"){
        this.alert("로그인이 필요합니다.")
        window.location.href="/reserv/bookinglogin"
    }
    utils.requestAjax("GET", "/reserv/api/reservations?reservationEmail="+reservationEmail, function () 
    {
		var jsonObj = JSON.parse(this.responseText);
        console.log(jsonObj);
	}, null);


});