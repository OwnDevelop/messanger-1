///<reference path="jquery-3.3.1.min.js">
$('document').ready(function () {
    var $login = $('#login'),
        $password = $('#password'),
        $passError = $('.pass-error'),
        $loginError = $('.login-error');

    $login.on('focus', function (e) {
        $(this).removeClass('invalid');
        $loginError.val('');
    });

    $password.on('focus', function (e) {
        $(this).removeClass('invalid');
        $passError.val('');
    });

    $('.send').on('click', function (e) {
        var a = $login.val();
        e.preventDefault();
        //проверка, зареган ли такой пользователь
        console.log(a);
        $.ajax({
            url: '/loginAlreadyExists',
            date: {login: a},
            success: function (request) {
                if (request) {
                    $.ajax({
                        url: '/authorization',
                        date: {login: $login.val(), password: $password.val()},
                        success: function (request) {
                            if (!request) {
                                $password.addClass('invalid');
                                $passError.val('Uncorrect password');
                            }
                        },
                        error: function (a, b, c) {
                            console.log(c);
                        }
                    });
                } else {
                    $login.addClass('invalid');
                    $loginError.val('Uncorrect login');
                }
            },
            error: function (a, b, c) {
                console.log(a, b, c);
            }
        });
    });
});