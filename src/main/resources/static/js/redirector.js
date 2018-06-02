;(function () {
    if (!localStorage.hasOwnProperty("user")) {
        location.replace("/signin");
    }
}());