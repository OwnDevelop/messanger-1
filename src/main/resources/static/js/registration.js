///<reference path="jquery-3.3.1.min.js">
///<reference path="jquery.validate.js">

$.validator.addMethod("lettersDegitsOnly", function (value, element) {
    var regexp = /^[\-а-яА-Яa-zA-Z0-9]+$/i;
    return this.optional(element) || regexp.test(value);
}, "Please enter letters or digits only");

$.validator.addMethod("lettersOnly", function (value, element) {
    var regexp = /^[\-а-яА-Яa-zA-Z]+$/i;
    return this.optional(element) || regexp.test(value);
}, "Please enter letters only");


$('#form').validate({
    rules: {
        email: {
            required: true,
            email: true,
            maxlength: 35
        },
        name: {
            required: true,
            minlength: 3,
            lettersOnly: true
        },
        lastName: {
            required: true,
            minlength: 3,
            lettersOnly: true
        },
        login: {
            required: true,
            minlength: 6,
            lettersDegitsOnly: true
        },
        password: {
            required: true,
            minlength: 6,
            maxlength: 20
        },
        confirmPass: {
            equalTo: '#password',
            required: true
        }
    },
    errorClass: "invalid",
    validClass: "valid"
});

$(function () {
    var $name, $surname, $login, $email, $password, $confirm_pass;
    $name = $("#name");
    $surname = $("#surname");
    $login = $("#login");
    $email = $("#email");
    $password = $("#password");
    $confirm_pass = $("#confirm-pass");

    $('.send')[0].onclick = function (e) {
        console.log($login.val());
        checkLoginExist($login.val());
        checkEmailExist($email.val());
    }


    function checkLoginExist(login) {
        $.ajax({
            data: login,
            success: function (request) {
                if (!request) {
                    //вывести ошибку
                }
            },
            error: function () {

            }
        });
    }

    function checkEmailExist(email) {
        $.ajax({
            data: email,
            success: function (request) {
                if (!request) {
                    //вывести ошибку
                }
            }
        });
    }
});