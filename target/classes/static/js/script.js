var APP = APP || {};
APP.createNamespace = function (path) {
    var parts = path.split('.'),
        parent = APP,
        i = 0;

    if (parts[i] === 'APP') {
        parts = parts.slice(1);
    }

    for (i = 0; i < parts.length; i += 1) {
        if (typeof parent[parts[i]] === "undefined") {
            parent[parts[i]] = {};
        }

        parent = parent[parts[i]];
    }

    return parent;
};

APP.createNamespace('APP.models.buttons');
APP.createNamespace('APP.models.fields');
APP.createNamespace('APP.models.entities');
APP.createNamespace('APP.models.identeties');
APP.createNamespace('APP.utilities.actions');
APP.createNamespace('APP.utilities.ajax');
APP.createNamespace('APP.utilities.validation');

APP.models.entities = {
    me: {
        id: 9,
        login: "BestGuy",
        url: "img/profiles/my.jpg",
        firstName: "Sergey",
        lastName: "Kovalenko",
        created_at: "6/10/1998",
        status: "Online",
        email: "best@people.math",
        sex: "male"
    },
    dialogs: [{
        userId: 1,
        avatar_url: "img/profiles/my.jpg",
        firstName: "ivan",
        lastName: "ivanov",
        message: "last msg",
        created_at: "9:05PM",
        countUnread: 5
    }],
    profiles: [{
        id: 1,
        login: "boss",
        url: "img/profiles/my.jpg",
        name: "ivan",
        lastName: "ivanov",
        created_at: "5/19/2018",
        status: "Offline",
        email: "mail1@google.com",
        sex: "male"
    }],
    conversations: [{
        userId: 1,
        from_id: 852,
        title: "ivan",
        message: "last msg",
        created_at: "9:05PM",
        countUnread: 5
    }]
};

APP.models.buttons = {
    $btnSettings: $('.settings-btn'),
    $btnSearch: $('.search-btn'),
    $btnCreateConvers: $('.create-conversation'),
    $btnSendMess: $('#send-message'),
    $btnSendPict: $('#add-image'),
    $btnRusLang: $('.dropdown-menu li:nth-child(1)'),
    $btnRusLang: $('.dropdown-menu li:nth-child(2)')
};

APP.models.fields = {
    $sendField: $('.send-field'),
    $searchField: $('.search-field input')
};

APP.utilities.validation = (function () {
    function htmlEscape(text) {
        return text.replace(/[<>"&]/g, function (match, pos, originalText) {
            switch (match) {
                case '<':
                    return "&lt;";
                case '>':
                    return "&gt;";
                case '"':
                    return "&quot;";
                case '&':
                    return "&amp;";
            }
        });
    }

    return {
        htmlEscape: htmlEscape
    };
}());

APP.utilities.actions = (function () {
    var dialogs = APP.models.entities.dialogs,
        conversations = APP.models.entities.conversations,
        profiles = APP.models.entities.profiles,
        validation = APP.models.entities.validation,
        me = APP.models.entities.me,
        buttons = APP.models.buttons,
        fields = APP.models.fields,
        MESSEGE_MAX_LENGHT = 200;

    function showDialogsAndConversations() {
        var $form = $('.jspPane:eq(0)'),
            html = "",
            i = 0,
            elem = {};

        //получение диалогов из ajax
        $.ajax({
            url: "/getDialogs",
            data: {id: me.id},
            success: function (request) {
                dialogs = request;

                for (i = 0; i < dialogs.length; i += 1) {
                    elem = dialogs[i];

                    html += '<div class="dialog"><img class="profile-photo" src="' + elem.avatar_url + '" alt="user">' +
                        '<a class="dial-name">' + elem.firstName + ' ' + elem.lastName + '</a>' +
                        '<span class="last-message-time">' + elem.created_at + '</span>' +
                        '<div class="short-message ellipsis">' + elem.message + '</div>' +
                        '<span class="badge">' + elem.countUnread + '</span></div>';
                }

                $form[0].innerHTML = html;
                setEventsForDialogs();
            },
            error: function (e) {
                console.log(e);
            }
        });

        $.ajax({
            url: "/getConversations",
            data: {id: me.id},
            success: function (request) {
                html = "";
                conversations = request;

                for (i = 0; i < conversations.length; i += 1) {
                    elem = conversations[i];

                    html += '<div class="conversation"><img class="profile-photo" src="img/defaults/conversation.jpg" alt="user">' +
                        '<a class="convers-name">' + elem.title + '</a>' +
                        '<span class="last-message-time">' + elem.created_at + '</span>' +
                        '<div class="short-message ellipsis">' + elem.message + '</div>' +
                        '<span class="badge">' + elem.countUnread + '</span></div>';
                }

                $form[0].innerHTML += html;
                setEventsForDialogs();
            },
            error: function (e) {
                console.log(e);
            }
        });
    }

    function setEventsForDialogs() {
        var $dialNames = $('.dial-name'),
            $dialogs = $('.dialog'),
            $convers = $('.conversation'),
            i = 0;

        for (i = 0; i < $dialogs.length; i += 1) {
            $dialogs[i].current = i;
            $dialogs[i].onclick = function (e) {
                openDialog(dialogs[this.current].from_id);
            };

            $dialNames[i].current = i;
            $dialNames[i].onclick = function (e) {
                e.stopPropagation();
                showModalForUser(dialogs[this.current].from_id);
            };
        }

        for (i = 0; i < $convers.length; i += 1) {
            $convers[i].current = i;
            $convers[i].onclick = function (e) {
                openConversation(conversations[this.current].userId);
            };
        }
    }

    function openDialog(id) {
        var data = {currentUserId: me.id, showId: id}

        $.ajax({
            url: "GetMessage",
            data: data,
            success: function (request) {
                dialogs = res;
                var res = JSON.parse(request);
                //дальнейшая работа
            },
            error: function (a, b, c) {
                console.log(a, b, c);
            }
        });

        $('.dialog').removeClass('activeted');
        $(this).addClass('activeted');

        alert('show dialog for id: ' + id);
    }

    function openConversation(id) {
        var data = {currentUserId: me.id, showId: id}

        $.ajax({
            url: "GetMessage",
            data: data,
            success: function (request) {
                dialogs = res;
                var res = JSON.parse(request);
                //дальнейшая работа
            },
            error: function (a, b, c) {
                console.log(a, b, c);
            }
        });

        $('.conversation').removeClass('activeted');
        $(this).addClass('activeted');

        alert('show conversation for id: ' + id);
    }

    function showModalForUser(id) {
        var html = "",
            $modalBody = $('.modal-body'),
            $modalFooter = $('.modal-footer');

        $.ajax({
            url: "/getUser",
            data: {id: id},
            method: "GET",
            success: function (request) {
                var user = request;

                if (user) {
                    $('#myModalLabel').html('Profile Information');

                    html = '<div class="row"><div class="col-xs-5"><img class="profile-img" src="' + user.avatar_url + '" alt="user photo"></div>' +
                        '<div class="col-xs-7"><div class="name text-center">' + user.firstName + ' ' + user.lastName + '</div>' +
                        '<div class="status text-center" >' + user.status + '</div>' +
                        '<button type="button" class="btn btn-default btn-write" data-dismiss="modal">Write</button></div></div></div>';

                    $modalBody.html(html);

                    html = '<div class="row"><div class="col-xs-5"><h4 class="profile-info text-right">E-mail:</h4>' +
                        '<h4 class="profile-info text-right">Login:</h4><h4 class="profile-info text-right">Sex:</h4><h4 class="profile-info text-right">Registration:</h4></div>' +
                        '<div class="col-xs-7"><h4 class="profile-info text-left">' + user.email + '</h4>' +
                        '<h4 class="profile-info text-left">' + user.login + '</h4>' +
                        '<h4 class="profile-info text-left">' + user.sex + '</h4>' +
                        '<h4 class="profile-info text-left">' + user.created_at + '</h4 ></div ></div >';

                    $modalFooter.html(html);

                    $('.btn-write').on('click', openDialog);
                    $('#Modal').modal('show');
                }
            },
            dateType: "json",
            error: function (a, b, c) {
                console.log(a, b, c);
            }
        });
    }

    function showModalForConversation() {
        var html = "", i = 0,
            $modalBody = $('.modal-body'),
            $modalFooter = $('.modal-footer'),
            $participant = {};

        for (i = 0; i < dialogs.length; i += 1) {
            elem = dialogs[i];

            html += '<div class="dialog participant"><img class="profile-photo" src="' + elem.avatar_url + '" alt="user">' +
                '<a class="dial-name">' + elem.firstName + ' ' + elem.lastName + '</a>' +
                '<span class="last-message-time"></span>' +
                '<div class="short-message ellipsis">' + elem.status + '</div></div>';
        }

        $('#myModalLabel').html("Select participants");
        $modalBody.html(html);
        $modalFooter.html('<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>' +
            '<button type="button" class="btn btn-primary start-convers">Start Conversation</button>');

        $participant = $('.participant');

        for (i = 0; i < $participant.length; i += 1) {
            $participant[i].current = dialogs[i].id;
            $participant[i].onclick = function () {
                $(this).toggleClass('selected');
            };
        }

        $('.start-convers').on('click', function () {
            var $people = $('.selected'),
                participantsId = [];

            if ($people.length == 0) {
                return;
            }

            for (i = 0; i < $people.length; i += 1) {
                participantsId.push($people[i].current);
            }

            $("#Modal").modal('hide');
            $modalBody.html('');
            $modalFooter.html('');
        });

        $("#Modal").modal('show');
        initializeScroll();
    }

    function initializeScroll() {
        var scrollPane = $('.scroll-pane');
        scrollPane.jScrollPane({
            maintainPosition: true,
            stickToBottom: true,
            contentWidth: 200
        });

        scrollPane.data("jsp").scrollTo(0, 1000);

        window.onresize = function () {
            $('.scroll-pane').jScrollPane({
                maintainPosition: true,
                stickToBottom: true
            });
        };
    }

    function initializeSearch() {
        fields.$searchField.on('blur', function () {
            var value = this.value;
            if (value) {
                $.ajax({
                    url: "/searchUsers",
                    data: {searchQuery: value},
                    success: function (request) {
                        console.log(request);
                        //дальнейшая работа
                        if (!request) {
                            return;
                        }
                        profiles = request;
                        showSearchResults(request);
                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            } else {
                showDialogsAndConversations();
            }
        });
    }

    function showSearchResults(arr) {
        var $form = $('.jspPane:eq(0)'),
            html = "", elem = {}, i = 0,
            $dialogs = {};

        for (i = 0; i < arr.length; i += 1) {
            elem = arr[i];

            html += '<div class="dialog"><img class="profile-photo" src="' + elem.avatar_url + '" alt="user">' +
                '<a class="dial-name">' + elem.firstName + ' ' + elem.lastName + '</a>' +
                '<span class="last-message-time"></span>' +
                '<div class="short-message ellipsis">' + elem.status + '</div></div>';
        }

        $form.html(html);

        $dialogs = $('.dialog');

        for (i = 0; i < $dialogs.length; i += 1) {
            $dialogs[i].current = i;
            $dialogs[i].onclick = function (e) {
                showModalForUser(profiles[this.current].id);
            };
        }
    }

    function initialization() {
        buttons.$btnSearch.on('click', function () {
            fields.$searchField.focus();
            fields.$searchField.val("");
        });

        buttons.$btnSettings.on('click', function () {
            showModalForUser(me.id);
        });

        buttons.$btnCreateConvers.on('click', function () {
            showModalForConversation();
        });

        initializeScroll();
        initializeSearch();

        initializeMesseges();
    }

    function initializeMesseges() {
        fields.$sendField.on("keyup", function (e) {
            var $this = $(this),
                key = e.key;

            switch (key) {
                case "Enter":
                    fixLength();
                    buttons.$btnSendMess.click();
                    return;
                case "Backspace":
                    return;
            }

            function fixLength() {
                if ($this.val().length > MESSEGE_MAX_LENGHT) {
                    $this.val($this.val().substr(0, MESSEGE_MAX_LENGHT));
                }
            }
        });

        buttons.$btnSendMess.on("click", function (e) {
            var text = fields.$sendField.val().trim();

            if (text) {
                text = validation.htmlEscape(text);
                console.log(text);

                $.ajax({
                    url: "/setMessage",
                    method: "GET",
                    data: {from_id: me.id, conversation_id: 5, message: text, attachment_urlMISTAKE: ""},
                    success: function (request) {
                        //
                        //добавление текста как нового сообщения
                        //
                    },
                    error: function (error) {
                        console.log(error);
                    }
                });
            }
            else {
                fields.$sendField.focus();
            }
        });
    }

    return {
        showDialogs: showDialogsAndConversations,
        initialization: initialization
    };
})();


$("document").ready(function () {
    var actions = APP.utilities.actions;
    APP.models.entities.me = localStorage.getItem("me");
    localStorage.clear();

    actions.initialization();
    actions.showDialogs();
});