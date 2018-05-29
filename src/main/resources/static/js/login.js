///<reference path="jquery-3.3.1.min.js">
///<reference path="jquery.validate.js">

$.validator.addMethod("lettersDegitsOnly", function (value, element) {
    var regexp = /^[\-а-яА-Яa-zA-Z0-9]+$/i;
    return this.optional(element) || regexp.test(value);
}, "Please enter letters or digits only");

$('#form').validate({
    rules: {
        login: {
            required: true,
            minlength: 6,
            lettersDegitsOnly: true
        },
        password:{
            required: true,
            minlength: 6,
            maxlength: 20
        }
    },
    errorClass: "invalid"
});

$('document').ready(function () {
    var $login = $('#login'),
        $password = $('#password'),
        $passError = $('#password-error'),
        $loginError = $('#login-error');

    $login.on('focus', function (e) {
        $(this).removeClass('invalid');
        $loginError[0].innerHTML = '';
    });

    $password.on('focus', function (e) {
        $(this).removeClass('invalid');
        $passError[0].innerHTML = '';
    });

    $('.send').on('click', function (e) {
        var a = $login.val();
        e.preventDefault();
        //проверка, зареган ли такой пользователь
        $.ajax({
            url: '/loginAlreadyExists',
            data: {login: a},
            success: function (request) {
                if (request) {
                    $.ajax({
                        url: '/authorization',
                        data: {login: $login.val(), password: $password.val()},
                        success: function (request) {
                            console.log(request);
                            if (request) {
                                //костыль для сохранения инфы о авторизированном юзере
                                //чтобы вытащить его при переходе
                                localStorage.setItem("user", request);
                                location.replace("/");
                            } else {
                                $password.addClass('invalid');
                                $passError[0].innerHTML = 'Uncorrect password';
                            }
                        },
                        error: function (a, b, c) {
                            console.log(c);
                        }
                    });
                } else {
                    $login.addClass('invalid');
                    $loginError[0].innerHTML = 'Uncorrect login';
                }
            },
            error: function (a, b, c) {
                console.log(a, b, c);
            }
        });
    });
});