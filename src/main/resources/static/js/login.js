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
        password: {
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
                        success: function (answer) {
                            var $input = $('#token');
                            console.log(answer);

                            if (answer) {
                                localStorage.setItem("user", JSON.stringify(answer));
                                $input.val(answer.token);
                                $('#form').submit();
                            } else {
                                $password.addClass('invalid');
                                $('#password').addClass('invalid');
                                $passError.html('Incorrect password');
                                $passError.css('display', 'block');
                            }
                        },
                        error: function (error) {
                            console.log(error);
                            alert('server error');
                        }
                    });
                } else {
                    $login.addClass('invalid');
                    $loginError[0].innerHTML = 'Uncorrect login';
                }
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    });
});