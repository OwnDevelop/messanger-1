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

$('document').ready(function () {
    var $name = $("#name"),
        $surname = $("#last-name"),
        $login = $("#login"),
        $email = $("#email"),
        $password = $("#password"),
        $confirm_pass = $("#confirm-pass"),
        isLoginValide = false,
        isEmailValide = false;

    $('.send')[0].onclick = function (e) {
        var login = $login.val().trim(),
            email = $email.val().trim(),
            password = $password.val().trim(),
            confirmPass = $confirm_pass.val().trim(),
            firstName = $name.val().trim(),
            lastName = $surname.val().trim(),
            sex = $('input[name="sex"]:checked').val();

        e.preventDefault();

        if (checkOnFieldsFullFill() && password === $confirm_pass.val()) {
            checkLoginExist($login.val());
            checkEmailExist($email.val());

            setTimeout(function () {
                var rand = Math.floor(1 + Math.random() * 12),
                    url = 'img/defaults/image0';

                if (isLoginValide && isEmailValide) {
                    if (rand.toString().length === 1) {
                        url += '0' + rand;
                    } else {
                        url += rand;
                    }

                    url += '.jpg';

                    $.ajax({
                        url: '/setUser',
                        data: {
                            login: login,
                            email: email,
                            password: password,
                            first_name: firstName,
                            last_name: lastName,
                            sex: sex,
                            status: 4,
                            avatar: url
                        },
                        success: function (request) {
                            if (request) {
                                location.replace('/signin');
                            } else {
                                console.log('косяк с добавлением');
                            }
                        },
                        error: function (error) {
                            console.log(error);
                            alert('server error');
                        }
                    });
                }
            }, 800);
        }

        function checkOnFieldsFullFill() {
            return login.length > 6 && firstName.length > 6 && lastName.length > 6 && email.length > 6 && password.length > 6 && confirmPass.length > 6;
        }
    }


    function checkLoginExist(login) {
        $.ajax({
            url: '/loginAlreadyExists',
            data: {login: login},
            success: function (request) {
                if (!request) {
                    isLoginValide = true;
                } else {
                    isLoginValide = false;
                    $login.addClass('invalid');
                }
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    }

    function checkEmailExist(email) {
        $.ajax({
            url: '/emailAlreadyExists',
            data: {email: email},
            success: function (request) {
                if (!request) {
                    isEmailValide = true;
                } else {
                    isEmailValide = false;
                    $email.addClass('invalid');
                }
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    }
});