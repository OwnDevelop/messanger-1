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
    me: (function () {
        var user = JSON.parse(localStorage.getItem("user"));
        // localStorage.clear();
        return user;
    }()),
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
    foundConversations: [{
        id: 1,
        admin_id: 1,
        title: "ivan",
        created_at: "5/19/2018"
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
    $btnRusLang: $('.dropdown-menu li:nth-child(2)'),
    $btnLogout: $('.logout-btn')
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
        foundConversations = APP.models.entities.foundConversations,
        profiles = APP.models.entities.profiles,
        validation = APP.utilities.validation,
        entities = APP.models.entities,
        buttons = APP.models.buttons,
        fields = APP.models.fields,
        files = {},
        MESSEGE_MAX_LENGHT = 200;

    function showDialogsAndConversations() {
        var $form = $('.jspPane:eq(0)'),
            html = "",
            i = 0,
            elem = {};

        $.ajax({
            url: "/getDialogs",
            data: {id: entities.me.id},
            method: 'POST',
            success: function (request) {
                dialogs = request;
                console.log(request);

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
            data: {id: entities.me.id},
            method: 'POST',
            success: function (request) {
                html = "";
                conversations = request;

                for (i = 0; i < conversations.length; i += 1) {
                    elem = conversations[i];

                    html += '<div class="conversation"><img class="profile-photo" src="img/defaults/conversation.jpg" alt="user">' +
                        '<a class="convers-name">' + elem.title + '</a>' +
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
            $dialogs[i].lastMessId = dialogs[i].id;
            $dialogs[i].conversId = dialogs[i].conversation_id;
            $dialogs[i].countUnread = dialogs[i].countUnread;
            $dialogs[i].url = dialogs[i].avatar_url;
            $dialogs[i].name = dialogs[i].firstName + ' ' + dialogs[i].lastName;
            $dialogs[i].onclick = openDialog;

            $dialNames[i].current = i;
            $dialNames[i].onclick = function (e) {
                e.stopPropagation();
                showModalForUser(dialogs[this.current].from_id);
            };
        }

        for (i = 0; i < $convers.length; i += 1) {
            $convers[i].current = i;
            $convers[i].lastMessId = conversations[i].id;
            $convers[i].conversId = conversations[i].conversation_id;
            $convers[i].countUnread = conversations[i].countUnread;
            $convers[i].name = conversations[i].title;
            $convers[i].url = conversations[i].avatar_url;
            $convers[i].onclick = openDialog;
        }

        initializeScroll();
    }

    function openDialog() {
        var url = this.url,
            name = this.name,
            conversationId = this.conversId,
            lastMessageId = this.lastMessId,
            unreadedMessages = this.countUnread;

        $('.profile-photo.img-responsive').attr('src', entities.me.avatar_url);

        $.ajax({
            url: "/getMessages",
            method: 'POST',
            data: {id: entities.me.id, message_id: lastMessageId, conversation_id: conversationId},
            success: function (request) {
                var html = '', i = 0,
                    $messages = $('.messages > .jspContainer > .jspPane'),
                    $names = {},
                    elem = {};

                html = '<img src="' + url + '" class="conversation-img profile-photo">' +
                    '<div class="conversation-name">' + name + '</div>' +
                    '<a class="search-messege btn"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a>' +
                    '<a class="leave btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>';

                $('.dialog-actions').html(html);
                html = '';

                $('.leave').on('click', function () {
                    var answer = confirm('Leave conversation?');

                    if (answer) {
                        $.ajax({
                            url: '/leaveTheConversation',
                            method: 'GET',
                            data: {
                                id: entities.me.id,
                                conversation_id: conversationId
                            },
                            success: function (res) {
                                console.log(res);
                                fields.$searchField.val('');
                                fields.$sendField.val('');
                                showDialogsAndConversations();
                            },
                            error: function (error) {
                                console.log(error);
                                alert('message error');
                            }
                        });
                    }
                });

                if (unreadedMessages > 0) {
                    $.ajax({
                        url: '/setUnreadMessages',
                        method: 'POST',
                        data: {
                            id: entities.me.id,
                            conversation_id: conversationId,
                            count: 0
                        },
                        success: function (res) {
                            console.log('all mess were read');
                            $('.activeted .badge').html('0');
                        },
                        error: function (error) {
                            console.log(error);
                            alert('message error');
                        }
                    });
                }

                for (i = request.length - 1; i >= 0; i -= 1) {
                    elem = request[i];

                    html += '<div class="message">' +
                        '<img src="' + elem.avatar_url + '" alt="user" class="profile-photo">\n' +
                        '<a href="#" class="name">' + elem.firstName + ' ' + elem.lastName + '</a>' +
                        '<div class="last-message-time">' + timeConverter(elem.created_at) + '</div>' +
                        '<div class="full-message">' + elem.message + '</div></div>';
                }

                $messages.html(html);

                $names = $('.name');

                for (i = 0; i < $names.length; i += 1) {
                    $names[i].current = i;
                    $names[i].onclick = function () {
                        showModalForUser(request[this.current].from_id, "closeModal");
                    };
                }

                initializeScroll();

                fields.$sendField.focus();
                fields.$sendField.val('');
            },
            error: function (error) {
                console.log(error);
            }
        });

        $('.dialog').removeClass('activeted');
        $('.conversation').removeClass('activeted');
        $(this).addClass('activeted');
    }



    function timeConverter(UNIX_timestamp){
        var a = new Date(UNIX_timestamp.seconds * 1000);
        var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
        var year = a.getFullYear();
        var month = months[a.getMonth()];
        var date = a.getDate();
        var hour = a.getHours();
        var min = a.getMinutes();
        var sec = a.getSeconds();
        var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec ;
        return time;
    }



    function showModalForUser(id, behavior) {
        var html = "",
            $modalBody = $('.modal-body'),
            $modalFooter = $('.modal-footer');

        behavior = behavior || "open";

        $.ajax({
            url: "/getUser",
            data: {id: id},
            method: 'POST',
            success: function (request) {
                var user = request,
                    $btn = {};

                if (user) {
                    $('#myModalLabel').html('Profile Information');

                    html = '<div class="row"><div class="col-xs-5"><img class="profile-img" src="' + user.avatar_url + '" alt="user photo"></div>' +
                        '<div class="col-xs-7"><div class="name text-center">' + user.firstName + ' ' + user.lastName + '</div>' +
                        '<div class="status text-center" >' + user.status + '</div>' +
                        '<button type="button" class="btn btn-default btn-write">Open dialog</button></div></div></div>';

                    $modalBody.html(html);

                    html = '<div class="row"><div class="col-xs-5"><h4 class="profile-info text-right">E-mail:</h4>' +
                        '<h4 class="profile-info text-right">Login:</h4><h4 class="profile-info text-right">Sex:</h4><h4 class="profile-info text-right">Registration:</h4></div>' +
                        '<div class="col-xs-7"><h4 class="profile-info text-left">' + user.email + '</h4>' +
                        '<h4 class="profile-info text-left">' + user.login + '</h4>' +
                        '<h4 class="profile-info text-left">' + user.sex + '</h4>' +
                        '<h4 class="profile-info text-left">' + timeConverter(user.created_at) + '</h4 ></div ></div >';

                    $modalFooter.html(html);

                    $btn = $('.btn-write');

                    if (user.id == entities.me.id) {
                        behavior = "changeStatus";
                    }

                    switch (behavior) {
                        case "open":
                            $btn.html('Start conversation');

                            $btn.on('click', function () {
                                var participants = [entities.me.id, user.id];
                                index = dialogs.find(function (element) {
                                    return element.id === user.id;
                                });

                                console.log(index);
                                console.log(user.id);

                                if (!index) {
                                    $.ajax({
                                        url: '/setConversation',
                                        method: 'POST',
                                        data: {users: participants.join(), admin_id: entities.me.id, title: null},
                                        success: function (request) {
                                            console.log('joined');
                                            //firstMessegeAJAX(user.id); это
                                            firstMessegeAJAX(request);
                                        },
                                        error: function (error) {
                                            console.log(error);
                                        }
                                    });
                                }

                                $('#Modal').modal('hide');
                            });
                            break;
                        case "closeModal":
                            $btn.html('Continue writing');
                            $btn.on('click', function () {
                                $('#Modal').modal('hide');
                                fields.$sendField.focus();
                            });
                            break;
                        case "changeStatus":
                            $btn.html('Change status');
                            $btn.on('click', function () {
                                var $status = $('.status'),
                                    value = $status.html();

                                switch (value) {
                                    case 'Online':
                                        $status.html('Idle');
                                        break;
                                    case 'Idle':
                                        $status.html('Do Not Disturb');
                                        break;
                                    case 'Do Not Disturb':
                                        $status.html('Online');
                                        break;
                                }
                            });

                            $('.close')[0].addEventListener('click', listener);
                            break;
                    }

                    $('#Modal').modal({
                        backdrop: 'static',
                        show: true
                    });

                    function listener() {
                        var value = $('.status').html(),
                            status = 0;

                        switch (value) {
                            case 'Online':
                                status = 1;
                                break;
                            case 'Idle':
                                status = 2;
                                break;
                            case 'Do Not Disturb':
                                status = 3;
                                break;
                            default:
                                status = 1;
                        }

                        $.ajax({
                            url: "/changeStatus",
                            data: {id: entities.me.id, status: status},
                            method: 'POST',
                            success: function (request) {
                                console.log('status was changed');
                            },
                            error: function (error) {
                                console.log(error);
                                alert('server error');
                            }
                        });

                        $('.close')[0].removeEventListener('click', listener);
                    }
                }
            }
            ,
            error: function (error) {
                console.log(error);
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

        $('#myModalLabel').html('Select participants<div class="input-group">\n' +
            '  <span class="input-group-addon" id="sizing-addon2">Title</span>\n' +
            '  <input type="text" class="form-control" placeholder="Conversation name" aria-describedby="sizing-addon2">\n' +
            '</div>');
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
                $title = $('#myModalLabel input'),
                title = $title.val().trim(),
                participantsId = [entities.me.id];

            if ($people.length == 0) {
                return;
            }

            if (!title || title.length < 5) {
                $title.addClass('invalid')
                $title.focus();
                return;
            }

            for (i = 0; i < $people.length; i += 1) {
                participantsId.push($people[i].current);
            }

            $.ajax({
                url: '/setConversation',
                data: {admin_id: entities.me.id, title: title, users: participantsId.join()},
                method: 'POST',
                success: function (request) {
                    if (request) {
                        firstMessegeAJAX(request);
                    }
                },
                error: function (err) {
                    console.log(err);
                    alert('creation error');
                }
            });

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
        foundConversations = [];

        fields.$searchField.on('blur', function () {
            var value = this.value;

            $('.jspPane:eq(0)').html('');

            if (value) {
                $.ajax({
                    url: "/searchUsers",
                    data: {searchQuery: value},
                    method: 'POST',
                    success: function (request) {
                        console.log(request);

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

                $.ajax({
                    url: "/searchConversations",
                    data: {searchQuery: value},
                    method: 'POST',
                    success: function (request) {
                        console.log(request);

                        if (!request) {
                            return;
                        }
                        foundConversations = request;
                        showSearchResults(request);
                    },
                    error: function (error) {
                        console.log(error);
                    }
                });
            } else {
                showDialogsAndConversations();
            }
        });
    }

    function showSearchResults(arr) {
        var messeges = $('.jspPane:eq(0)')[0],
            html = "", elem = {}, i = 0,
            className = '', dialogName = '',
            $dialogs = {},
            $conversations = {};

        for (i = 0; i < arr.length; i += 1) {
            elem = arr[i];

            if (elem.hasOwnProperty("firstName")) {
                html += '<div class="dialog"><img class="profile-photo" src="' + elem.avatar_url + '" alt="user">' +
                    '<a class="dial-name">' + elem.firstName + ' ' + elem.lastName + '</a>' +
                    '<span class="last-message-time"></span>' +
                    '<div class="short-message ellipsis">' + elem.status + '</div></div>';
            } else {
                html += '<div class="conversation"><img class="profile-photo" src="img/defaults/conversation.jpg" alt="user">' +
                    '<a class="dial-name">' + elem.title + '</a>' +
                    '<span class="last-message-time"></span>' +
                    '<div class="short-message ellipsis">Click to join!</div></div>';
            }
        }

        messeges.innerHTML += html;

        $dialogs = $('.dialog');
        $conversations = $('.conversation');

        for (i = 0; i < $dialogs.length; i += 1) {
            $dialogs[i].current = i;
            $dialogs[i].onclick = function (e) {
                showModalForUser(profiles[this.current].id, "open");
            };
        }

        for (i = 0; i < $conversations.length; i += 1) {
            $conversations[i].current = i;
            $conversations[i].conversId = foundConversations[i].id;
            $conversations[i].onclick = function () {
                var answer = false,
                    conversId = this.conversId,
                    index = 0;

                index = conversations.find(function (element) {
                    return element.conversation_id === conversId;
                });

                if (!index) {
                    answer = confirm('Join conversation?');

                    if (answer) {
                        $.ajax({
                            url: '/joinTheConversation',
                            data: {conversation_id: conversId, id: entities.me.id},
                            method: 'POST',
                            success: function (request) {
                                console.log('joined');
                                showDialogsAndConversations();
                            },
                            error: function (error) {
                                console.log(error);
                                alert('server error');
                            }
                        });
                    }
                }
            };
        }

        initializeScroll();
    }

    function firstMessegeAJAX(conversationId) {
        $.ajax({
            url: '/setMessage',
            method: 'POST',
            data: {
                from_id: entities.me.id,
                conversation_id: conversationId,
                message: "Conversation has started",
                attachment_url: ""
            },
            success: function (res) {
                console.log(res);
                fields.$searchField.val('');
                showDialogsAndConversations();
            },
            error: function (error) {
                console.log(error);
                alert('server error');
            }
        });
    }

    function initialization() {
        buttons.$btnSearch.on('click', function () {
            fields.$searchField.focus();
            fields.$searchField.val("");
        });

        buttons.$btnSettings.on('click', function () {
            showModalForUser(entities.me.id, true);
        });

        buttons.$btnCreateConvers.on('click', function () {
            showModalForConversation();
        });

        buttons.$btnLogout.on('click', function (e) {
            $.ajax({
                url: '/logout',
                data: {token: entities.me.token},
                method: 'POST',
                success: function (request) {
                    localStorage.removeItem('user');
                    location.replace('/signin');
                },
                error: function (error) {
                    console.log(error);
                    alert('server error');
                }
            });
        });

        $('input[type=file]').on('change', function () {
            if (this.files[0].size > 1048576 ){
                this.value = "";
                alert('File must be less than 2MB');
                return;
            }

            if(this.files[0].size < 10000){
                this.value = "";
                alert('Low size');
                return;
            }
            files = this.files;
            console.log(files);
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

        $('#sender').on('submit', function (e) {
            var text = fields.$sendField.val().trim(),
                $that = $(this),
                a = $('.activeted'),
                data = new FormData($that.get(0)),
                conversId = a[0].conversId;

            e.preventDefault();

            data.append('from_id', entities.me.id);
            data.append('conversation_id', conversId);
            data.append('attachment_url', "");

            if (text || files) {
                text = validation.htmlEscape(text);
                data.set('message', text);

                console.log(files);
                console.log(data);

                $.ajax({
                    url: '/setMessage',
                    method: 'POST',
                    data: data,
                        // {from_id: entities.me.id,conversation_id: conversId,message: text.toString(),attachment_url: "" },
                    cache: false,
                    dataType: 'json',
                    processData : false,
                    contentType : false,
                    success: function (res) {
                        fields.$sendField.val('');

                        a[0].lastMessId = res;
                        openDialog.apply(a[0], arguments);

                        showDialogsAndConversations();
                    },
                    error: function (error) {
                        console.log(error);
                        alert('server error');
                    }
                });
            }
            else {
                fields.$sendField.focus();
            }
        });
        // buttons.$btnSendMess.on("click", function (e) {
        //
        // });
    }

    return {
        showDialogs: showDialogsAndConversations,
        initialization: initialization
    };
})();

$("document").ready(function () {
    var actions = {},
        entities = APP.models.entities;

    if (!entities.me) {
        location.replace('/signin');
    } else {
        $.ajaxSetup({
            headers: {
                'token':entities.me.token,
                'Content-Type':'application/json' //TODO: ТЫ УВЕРЕН?
            },
            contentType: "application/json" //todo;
        });
        actions = APP.utilities.actions;
        actions.initialization();
        actions.showDialogs();
    }
});