var Login = function () {

	var handleLogin = function() {
	    var  checkCode=false;
		if ($.cookie("SMS_CHECK_SHOW")!=undefined&&$.cookie("SMS_CHECK_SHOW")!='NaN'&&$.cookie("SMS_CHECK_SHOW")!='null') {
			checkCode=true;
		}

		$('.login-form').validate({
			errorElement: 'span', //default input error message container
			errorClass: 'help-block', // default input error message class
			focusInvalid: false, // do not focus the last invalid input
			rules: {
				enLogin: {
					required: true
				},
				password: {
					required: true
				},
				remember: {
					required: false
				},
				checkCode: {
					required:checkCode
				}

			},

			messages: {
				enLogin: {
					required: "用户名不能为空."
				},
				password: {
					required: "密码不能为空."
				},
				checkCode: {
					required: "验证码不能为空."
				}
			},

			invalidHandler: function (event, validator) { //display error alert on form submit
				$('.alert-danger', $('.login-form')).show();
			},

			highlight: function (element) { // hightlight error inputs
				$(element)
					.closest('.form-group').addClass('has-error'); // set error class to the control group
			},

			success: function (label) {
				label.closest('.form-group').removeClass('has-error');
				label.remove();
			},

			errorPlacement: function (error, element) {
				error.insertAfter(element.closest('.input-icon'));
			},

			submitHandler: function (form) {
				var enLogin_ = $("#txt_login").val().replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("\r\n", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ","");
				$("#txt_login").val(enLogin_);
				form.submit();
			}
		});

		$('.login-form input').keypress(function (e) {
			if (e.which == 13) {
				if ($('.login-form').validate().form()) {
					var enLogin_ = $("#txt_login").val().replaceAll("<", "").replaceAll(">", "").replaceAll("\"", "").replaceAll("\r\n", "").replaceAll("\t", "").replaceAll("\n", "").replaceAll(" ","");
					$("#txt_login").val(enLogin_);

					$('.login-form').submit();
				}
				return false;
			}
		});
	}


    return {
        //main function to initiate the module
        init: function () {
            handleLogin();
        }

    };

}();

var time = 60;
var flag = true;
var id;

function sendCode() {
	if (flag) {
		var enLogin = document.getElementById("txt_login").value;
		// 获取验证码
		$.ajax({
			method: 'POST',
			url: '/sendCheckCode',
			data: {
				enLogin:enLogin,
			}
		}).then(function (response) {
			time=60;
			$("#checkCodeBtn").text("请稍后");
			//开启定时器
			id = setInterval(showTime, 1000);
			flag = false;
			$("#checkCodeBtn").attr("onclick", "return false");
			$("#checkCodeBtn").attr("style", "cursor: default;opacity: 0.2");
		});
	}
}

function showTime() {
	if (time > 0) {
		$("#checkCodeBtn").text("已发送" + time + "s");
		time--;
		$.cookie("total",time);
	} else {
		$("#checkCodeBtn").text("发送验证码");
		flag = true;
		time = 60;
		// 清除定时器
		clearInterval(id);
		total=$.cookie("total",time, { expires: -1 });
		$("#checkCodeBtn").attr("onclick", "sendCode()");
		$("#checkCodeBtn").attr("style","color: #0b94ea");
	}
}

function showCheckButton() {
	if ($.cookie("SMS_CHECK_SHOW")!=undefined&&$.cookie("SMS_CHECK_SHOW")!='NaN'&&$.cookie("SMS_CHECK_SHOW")!='null') {
		document.getElementById("check").style.display = "";
	} else {
		document.getElementById("check").style.display = "none";
		console.log("none")
	}
	if($.cookie("total")!=undefined&&$.cookie("total")!='NaN'&&$.cookie("total")!='null'){
		//cookie存在倒计时}
		time= $.cookie("total");
		id = setInterval(showTime, 1000);
		flag = false;
		$("#checkCodeBtn").attr("onclick", "return false");
		$("#checkCodeBtn").attr("style", "cursor: default;opacity: 0.2");
	}else {
		$('#checkCodeBtn').attr("disabled", false);
	}
}

window.onload=showCheckButton;