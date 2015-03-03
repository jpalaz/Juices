function run() {
	var button = document.getElementsByClassName('btn-add')[0];

	button.addEventListener('click', onAddButtonClick);
    updateCounter();
}

function onAddButtonClick() {
	var message = document.getElementsByClassName('messageText')[0];

	addMessage(message.value);
	message.value = '';
	updateCounter();
}

function addMessage(value) {
	if(!value){
		return;
	}
    
	var table = document.getElementsByClassName('table')[0];
    var row = table.insertRow(-1);
    createRowValues(row, value);
    
	updateCounter();
}

/*<tr class="item">
<td class="col-time"><time>2015-02-26</time></td>
<td class="col-message">Person 1: Message 1.</td>
<td class="col-edit"><a class="btn-default" href="#"><i class="glyphicon glyphicon-edit"></i></a></td>
<td class="col-delete"><a class=&quot;btn-default&quot; href=&quot;#&quot;><i class=&quot;glyphicon glyphicon-remove&quot;></i></a></td>
</tr>*/

function createRowValues(row, text){
	var tdTime = document.createElement('td');
	var tdMessage = document.createElement('td');
	var tdEdit = document.createElement('td');
	var tdRemove = document.createElement('td');

	row.classList.add('item');
    tdTime.classList.add('col-time');
    tdMessage.classList.add('col-message');
    tdEdit.classList.add('col-edit');
    tdRemove.classList.add('col-delete');
    
    tdTime.appendChild(document.createTextNode("2015-03-03"));
    tdMessage.appendChild(document.createTextNode(text));
    tdEdit.innerHTML = "<a class=&quot;btn-default&quot; href=&quot;#&quot;><i class=&quot;glyphicon glyphicon-edit&quot;></i></a>";
    tdRemove.innerHTML = "<a class=&quot;btn-default&quot; href=&quot;#&quot;><i class=&quot;glyphicon glyphicon-remove&quot;></i></a>";
    
	row.appendChild(tdTime);
	row.appendChild(tdMessage);
	row.appendChild(tdEdit);
	row.appendChild(tdRemove);

	//return row;
}

function updateCounter(){
	var counter = document.getElementsByClassName('counter-holder')[0];
    var count = document.getElementsByClassName("items")[0].rows.length;
    
    counter.innerText = count.toString();
}
