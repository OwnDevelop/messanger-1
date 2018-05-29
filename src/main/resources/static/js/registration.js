///<reference path="jquery-3.3.1.min.js">
///<reference path="jquery.validate.js">

$.validator.addMethod("lettersDegitsOnly", function(value, element) {
    var regexp = /^[\-а-яА-Яa-zA-Z0-9]+$/i;
    return this.optional(element) || regexp.test(value);
}, "Please enter letters or digits only");

$.validator.addMethod("lettersOnly", function(value, element) {
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