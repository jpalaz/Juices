var username = "";
var selectedRow = null;
var isEditing = false;
var connected = true;

function run() {
    
	var button = document.getElementsByClassName('btn-add')[0];
	button.addEventListener('click', onAddButtonClick);
    
    var username = document.getElementsByClassName('input-name')[0];
    username.addEventListener('focusout', onNameInput);
    
    var input = document.getElementsByClassName('messageText')[0];
	input.addEventListener('keydown', onTextInput);
    
    var edit = document.getElementsByClassName('icon')[0];
    edit.addEventListener('click', onEditClick);
    
    var remove = document.getElementsByClassName('icon')[1];
    remove.addEventListener('click', onRemoveClick);
    
    var conection = document.getElementById('conection-item');
    conection.addEventListener('click', onConnectionPress);
    
    var table = document.getElementsByClassName('table')[0];
    table.scrollTop = table.scrollHeight;
    
    makeIconsUnvisible();
    updateCounter();
    onResizeDocument();
}

function onResizeDocument() {
    var all = document.getElementsByTagName('html')[0].clientHeight;
    var navbar = document.getElementsByClassName('navbar')[0].clientHeight;
    var input = document.getElementsByClassName('message-input')[0].clientHeight;
    var height = all - navbar - input - 50;
    height = height.toString() + 'px';
    document.getElementsByClassName('table')[0].style.height = height;
}

function onAddButtonClick() {
    var message = document.getElementsByClassName('messageText')[0];
    
    if(isEditing == true) {
        selectedRow.getElementsByClassName('list-group-item-text')[0].innerHTML = message.value;
        message.value = '';
        
        isEditing = false;
        selectedRow = null;
        return;
    }
    
    while(username.length === 0) {
        username = prompt("Enter your username!");
    }
    
    var userInput = document.getElementsByClassName('input-name')[0];
    userInput.value = username;

	addMessage(message.value);
	message.value = '';
	updateCounter();
}

function onNameInput(e) {
    var name = document.getElementsByClassName('input-name')[0];
    username = name.value;
}

function onTextInput(e) {  
    if(isEditing == true) {
        if(e.currentTarget.value.length == 0) {
            isEditing = false;
            selectedRow = null;
        }
    }
    
    var key = e.keyCode;
    if (key === 13) { // 13 is enter
      onAddButtonClick();
    }    
}

function addMessage(value) {
	if(!value) {
		return;
	}
    
	var table = document.getElementsByClassName('table')[0];
    
    var bottomScroll = false;
    var h1 = table.scrollHeight - table.scrollTop;
    var h2 = table.clientHeight + table.firstElementChild.lastChild.scrollHeight;
    if(h1 <= h2)
        bottomScroll = true;
    
    var row = table.insertRow(-1);
    createRowValues(row, value);
    
    if(bottomScroll)
        table.scrollTop = table.scrollHeight;
    
	updateCounter();
}

function createRowValues(row, text) {
	row.classList.add('item');
    row.classList.add(username);
    row.id = "message" + document.getElementsByClassName("items")[0].rows.length;
    row.addEventListener('click', onMessageClick);
    
    var tdTime = document.createElement('td');
	tdTime.classList.add('col-time');
    tdTime.innerHTML = getCurrentTime();
	row.appendChild(tdTime);
    
    var tdMessage = document.createElement('td');
    tdMessage.classList.add('col-message');
    tdMessage.classList.add('list-group');
    
    var divMessage = document.createElement('div');
    divMessage.classList.add('list-group-item');
    
    var user = document.createElement('h4');
    user.classList.add('list-group-item-heading');
    user.innerHTML = username;
    divMessage.appendChild(user);
    
    var message = document.createElement('p');
    message.classList.add('list-group-item-text');
    message.innerHTML = text;
    divMessage.appendChild(message);
    
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
    if(row.classList.contains(username))
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
    
    isEditing = true;
    makeIconsUnvisible();
    
    selectedRow.classList.remove('info');
    var selectedMessage = selectedRow.getElementsByClassName('list-group-item')[0];
    selectedMessage.classList.remove('active');
}

function onRemoveClick() {
    if(selectedRow == null)
        return;
    
    selectedRow.parentNode.removeChild(selectedRow);
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

//window.onresize = function(event) {
//    var all = document.getElementsByTagName('html').height;
//    var navbar = document.getElementsByClassName('navbar')[0].height;
//    var input = document.getElementsByClassName('message-input')[0].height;
//    var height = all - navbar - input;
//    height = height.toString() + 'px';
//    document.getElementsByClassName('table')[0].style.height = height;
//}