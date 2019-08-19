class LoginValidation{
    validateEmail(email){
        var errorMessage = "";
        var emailRegex = /^[\w+_]\w+@\w+\.\w+(\.\w+)?$/;

        if(email == "" || email == undefined){
            errorMessage="이메일을 입력해주세요.";
        }
        else if(!email.match(emailRegex)){
            errorMessage="이메일은 다음과 같은 형식이여야합니다: 'aaaa@aaaa.com'";
        }

        return errorMessage;
    }
}


window.addEventListener('load', function () {
    var loginValidation = new LoginValidation();
    var loginConfirmButton = document.querySelector(".login_btn.confirm");
    loginConfirmButton.addEventListener("click", function(event){
        event.preventDefault();
        var errorMessage = loginValidation.validateEmail(document.querySelector("#resrv_id").value);
        if(errorMessage != ""){
            alert(errorMessage);
        }
        else{
            document.querySelector(".ng-pristine.ng-valid").submit();
        }
    });

    //TOP 버튼이 눌렸을 때 호출
    document.querySelector(".lnk_top").addEventListener("click", function(){
        window.scrollTo({
			top: 0,
			left: 0,
			behavior: 'smooth'
		});
    });
});