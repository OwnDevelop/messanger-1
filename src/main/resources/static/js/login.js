///<reference path="jquery-3.3.1.min.js">
///<reference path="jquery.validate.js">

$.validator.addMethod("lettersDegitsOnly", function (value, element) {
    var regexp = /^[\-а-яА-Яa-zA-Z0-9@.]+$/i;
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
        var value = $login.val();
        e.preventDefault();
        //проверка, зареган ли такой пользователь
        $.ajax({
            url: '/loginAlreadyExists',
            data: {login: value},
            method: 'POST',
            success: function (request) {
                if (request) {
                    authorize(value);
                } else {
                    $.ajax({
                        url: '/emailAlreadyExists',
                        data: {email: value},
                        method: 'POST',
                        success: function (answer) {
                            if (answer){
                                authorize(value);
                            } else {
                                $login.addClass('invalid');
                                $loginError.css('display', 'block');
                                $loginError.html('Incorrect login or email');
                            }
                        },
                        error: function (error) {
                            console.log(error);
                            alert('server error');
                        }
                    });
                }
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    });

    function authorize(value) {
        $.ajax({
            url: '/authorization',
            data: {login: value, password: $password.val()},
            method: 'POST',
            success: function (answer) {
                var $input = $('#token');

                switch (answer) {
                    case "User is not activated yet":
                        $login.addClass('invalid');
                        $loginError.css('display', 'block');
                        $loginError.html(answer);
                        break;
                    case "No Such User":
                    case "Incorrect password":
                        $password.addClass('invalid');
                        $passError.css('display', 'block');
                        $passError.html(answer);
                        break;
                    default:
                        localStorage.setItem("user", JSON.stringify(answer));
                        $input.val(answer.token);
                        $('#form').submit();
                        break;
                }
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    }
});