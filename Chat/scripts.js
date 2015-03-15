(
function() {
    var username;
    var selectedRow = null;
    var isEditing = false;
    var connected = true;
    
    window.addEventListener('DOMContentLoaded', run);
    window.addEventListener('resize', onResizeDocument);

    var uniqueId = function() {
        var date = Date.now();
        var random = Math.random() * Math.random();
    
	   return Math.floor(date * random).toString();
    };

var newMessage = function(input, time) {
	return {
        name: username,
        text: input,
        time: time,
        isMyMessage: true,
		edited: false,
		deleted: false,
		id: uniqueId()
	};
};

var messageList = [];
    
function run(e) {
    username = restoreUsername() || "";
    var nameInput = document.getElementById('input-name');
    nameInput.addEventListener('focusout', onNameInput);
    nameInput.value = username;
    
    document.getElementById('add-button').addEventListener('click', onAddButtonClick);
    document.getElementsByClassName('messageText')[0].addEventListener('keydown', onTextInput);
    document.getElementById('conection-item').addEventListener('click', onConnectionPress);
    document.getElementsByClassName('icon')[0].addEventListener('click', onEditClick);
    document.getElementsByClassName('icon')[1].addEventListener('click', onRemoveClick);
    
    updateCounter();
    onResizeDocument();
    makeIconsUnvisible();
    var table = document.getElementsByClassName('table')[0];
    table.scrollTop = table.scrollHeight;
    
    $('#icon-edit').tooltip();
    $('#icon-remove').tooltip();
    $('#name').tooltip();
    $('#messages-number').tooltip();
    $('#message-input').popover({ delay: { "show": 1500 } });
    $('#input-name').popover();
    
    messageList = restoreMessages() || [];
    updateMessages();
}

function onResizeDocument(e) {
    var all = document.getElementsByTagName('html')[0].clientHeight;
    var navbar = document.getElementsByClassName('navbar')[0].clientHeight;
    var input = document.getElementById('message-input').clientHeight;
    var height = all - navbar - input - 50;
    height = height.toString() + 'px';
    document.getElementsByClassName('table')[0].style.height = height;
}

function onAddButtonClick(e) {
    var message = document.getElementsByClassName('messageText')[0];
    
    if (isEditing == true) {  
        var table = document.getElementsByClassName('table')[0];
        var bottomScroll = isScrollBottom(table);
        
        selectedRow.getElementsByClassName('list-group-item-text')[0].innerText = message.value;
        var messageId = selectedRow.getAttribute('id');
            for (var i = 0; i < messageList.length; i++)
                if (messageId == messageList[i].id) {
                    messageList[i].message = message.value;
                    if(!messageList[i].edited) {
                        messageList[i].edited = true;
                        selectedRow.childNodes[0].innerHTML += "<br>" + '<i class="glyphicon glyphicon-pencil"></i>';
                    }
                    break;
                }
        
        message.value = '';
        if(bottomScroll)
            table.scrollTop = table.scrollHeight;
        
        isEditing = false;
        selectedRow = null;
        storeMessages();
        return;
    }
    
    if (username.length === 0) {
        $('#input-name').popover('show');
        nameInput = document.getElementById('input-name').focus();
        return;
    }
    
    if(!/\S/.test(message.value)) {
        message.value = '';
        return;
    }
    
    var createdMessage = newMessage(message.value, getCurrentTime());
	addMessage(createdMessage);
    messageList.push(createdMessage);
    message.value = '';
    storeMessages();
	updateCounter();
}

function onNameInput(e) {
    var name = document.getElementById('input-name');
    if(!/\S/.test(name.value)) {
        name.value = '';
        username = '';
        storeUsername();
        $('#input-name').popover('show');
        return;
    }
    
    username = name.value;
    storeUsername();
    $('#input-name').popover('hide');
}

function onTextInput(e) {  
    if(isEditing == true) {
        if(e.currentTarget.value.length == 0) {
            isEditing = false;
            selectedRow = null;
        }
    }
    
    var key = e.keyCode;
    if (key == 13) { // 13 is enter        
        e.preventDefault();
        if(e.shiftKey)
        {
            var message = document.getElementsByClassName('messageText')[0];
            var caretPos = getCaretPosition(message);
            var text = message.value;
            var br = '\n';
            message.value = text.slice(0, caretPos) + br + text.slice(caretPos);
            setCaretPosition(message, caretPos + 1);
        }
        else {
            onAddButtonClick();
        }
        
        return false;
    }    
}

function getCaretPosition (textarea) {
	var caretPos = 0;
	if (document.selection) {
	   textarea.focus ();
		var select = document.selection.createRange ();
		select.moveStart ('character', -textarea.value.length);
		caretPos = select.text.length;
	}
	else if (textarea.selectionStart || textarea.selectionStart == '0')
		caretPos = textarea.selectionStart;
	return caretPos;
}

function setCaretPosition(textarea, pos) {
	if (textarea.setSelectionRange)	{
		textarea.focus();
		textarea.setSelectionRange(pos, pos);
	}
	else if (textarea.createTextRange) {
		var range = textarea.createTextRange();
        range.collapse(true);
		range.moveEnd('character', pos);
		range.moveStart('character', pos);
		range.select();
	}
}

function addMessage(message) {          
	var table = document.getElementsByClassName('table')[0];
    var bottomScroll = isScrollBottom(table);
    
    var row = table.insertRow(-1);
    createRowValues(row, message);
    
    if(bottomScroll)
        table.scrollTop = table.scrollHeight;
    
	updateCounter();
}

function isScrollBottom(table) {    
    var bottomScroll = false;
    var h1 = table.scrollHeight - table.scrollTop;
    
    var h2 = table.clientHeight;
    if(table.firstElementChild.lastChild.scrollHeight != undefined)
        h2 += table.firstElementChild.lastChild.scrollHeight;
    
    if(h1 <= h2)
        bottomScroll = true;
    
    return bottomScroll;
}
    
function createRowValues(row, message) {
	row.classList.add('item');
    if(message.isMyMessage)
        row.classList.add('my-message');    
    row.addEventListener('click', onMessageClick);
    row.setAttribute('id', message.id);
    
    var tdTime = document.createElement('td');
    var tdMessage = document.createElement('td');
    var divMessage = document.createElement('div');
    var user = document.createElement('h4');
    var wrap = document.createElement('div');
    var text = document.createElement('p');
    
	tdTime.classList.add('col-time');
    tdMessage.classList.add('col-message');
    divMessage.classList.add('list-group-item');
    user.classList.add('list-group-item-heading');
    wrap.classList.add('wrap');
    text.classList.add('list-group-item-text');
    
    user.innerText = message.name;
    text.innerText = message.text;
    tdTime.innerHTML = message.time;
    
    if(message.deleted) {
        tdTime.innerHTML += "<br>" + '<i class="glyphicon glyphicon-trash"></i>';
        row.classList.add('deleted-message');
    } else if(message.edited) {
        tdTime.innerHTML += "<br>" + '<i class="glyphicon glyphicon-pencil"></i>';
    }
    
	row.appendChild(tdTime);
    divMessage.appendChild(user);
    wrap.appendChild(text);
    divMessage.appendChild(wrap);
    tdMessage.appendChild(divMessage);
	row.appendChild(tdMessage);
}

function getCurrentTime() {
    var date = new Date();
    var time = ('0' + date.getDate()).slice(-2) + '.' + ('0' + (date.getMonth()+1)).slice(-2) + "<br>";
    time += ('0' + date.getHours()).slice(-2) + ':' + ('0' + date.getMinutes()).slice(-2);
    time += ':' + ('0' + date.getSeconds()).slice(-2);
    return time;
}

function makeIconsVisible() {
    document.getElementsByClassName('icon')[0].style.display = "block";
    document.getElementsByClassName('icon')[1].style.display = "block";
}

function makeIconsUnvisible() {
    document.getElementsByClassName('icon')[0].style.display = "none";
    document.getElementsByClassName('icon')[1].style.display = "none";
}

function onMessageClick(e) {
    var row = document.getElementById(e.currentTarget.id);
    if(row.classList.contains('my-message'))
    {
        var message = row.getElementsByClassName('list-group-item')[0];
        
        if(row.classList.contains('info'))
        {
            row.classList.remove('info');
            message.classList.remove('active');
            selectedRow = null;
            
            makeIconsUnvisible();
        }
        else
        {
            if(selectedRow != null)
            {
                selectedRow.classList.remove('info');
                var selectedMessage = selectedRow.getElementsByClassName('list-group-item')[0];
                selectedMessage.classList.remove('active');
            }
            
            row.classList.add('info');
            message.classList.add('active');
            selectedRow = row;
            
            makeIconsVisible();
        }
    }
}

function updateCounter() {
	var counter = document.getElementsByClassName('counter-holder')[0];
    var count = document.getElementsByClassName("items")[0].rows.length;
    
    counter.innerText = count.toString();
}

function onEditClick() {
    if(selectedRow == null)
        return;
    
    var text = selectedRow.getElementsByClassName('list-group-item-text')[0];
    var input = document.getElementsByClassName('messageText')[0];    
	input.value = text.innerText;
    input.focus();
    
    isEditing = true;
    makeIconsUnvisible();
    
    selectedRow.classList.remove('info');
    var selectedMessage = selectedRow.getElementsByClassName('list-group-item')[0];
    selectedMessage.classList.remove('active');
}

function onRemoveClick() {
    if(selectedRow == null)
        return;
    
    selectedRow.classList.remove('my-message');
    var messageId = selectedRow.getAttribute('id');
    var time;
    for(var i = 0; i < messageList.length; ++i) {
        if(messageId == messageList[i].id) {
            messageList[i].deleted = true;
            messageList[i].text = "This message has been deleted.";
            messageList[i].isMyMessage = false;
            time = messageList[i].time;
            storeMessages();
            break;
        }
    }
    
    selectedRow.childNodes[1].childNodes[0].childNodes[1].innerText = 'This message has been deleted.';
    selectedRow.childNodes[0].innerHTML = time + "<br>" + '<i class="glyphicon glyphicon-trash"></i>';
    selectedRow.getElementsByClassName('list-group-item')[0].classList.remove('active');
    selectedRow.classList.remove('info');
    selectedRow.classList.add('deleted-message');
    
    selectedRow = null;
    makeIconsUnvisible();
    updateCounter();
}

// !!! TEMPORARY METHOD WHILE SERVER IS UNAVAILABLE !!!
function onConnectionPress() { 
    if(connected) {
        onConnectionLost();
    }
    else {
        onConnectionSeted();
    }
    connected = !connected;
}

function onConnectionLost() {
    var conection = document.getElementById('conection');
    conection.classList.remove('label-success');
    conection.classList.add('label-danger');
    conection.textContent = "Disconnected";
}

function onConnectionSeted() {
    var conection = document.getElementById('conection');
    conection.classList.remove('label-danger');
    conection.classList.add('label-success');
    conection.textContent = "Connected";
}
    
    function storeUsername() {
        if(typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
	   }
        
        localStorage.setItem("Chat Username", JSON.stringify(username));
    }
    
    function storeMessages() {
        if(typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
	   }
        
        localStorage.setItem("Chat messageList", JSON.stringify(messageList));
    }
    
    function restoreUsername() {
        if(typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        
        var name = localStorage.getItem("Chat Username");
        return name && JSON.parse(name);
    }
    
    function restoreMessages() {
        if(typeof(Storage) == "undefined") {
            alert('localStorage is not accessible');
            return;
        }
        
        var item = localStorage.getItem("Chat messageList");
        return item && JSON.parse(item);
    }
    
    function updateMessages() {
        for (var i = 0; i < messageList.length; i++)
            addMessage(messageList[i]);
    }
}())