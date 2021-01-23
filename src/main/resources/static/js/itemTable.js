let table = document.getElementById('items-table');

// DOM manipulation

function addItem(item) {
    let itemDOM = document.createElement('tr');
    itemDOM.setAttribute('itemId', item.id);
    appendColumnToRow(itemDOM, item.type);
    appendColumnToRow(itemDOM, item.marking);
    appendColumnToRow(itemDOM, item.current);
    appendColumnToRow(itemDOM, item.section);
    appendColumnWithButtonsToRow(itemDOM, item.count);
    itemDOM.onclick = onItemClick;
    table.appendChild(itemDOM);
}

function setValue(itemDOM, index, value) {
    itemDOM.children[index].innerHTML = convertField(value);
}

function setType(itemDOM, value) {
    setValue(itemDOM, 0, value)
}

function setMarking(itemDOM, value) {
    setValue(itemDOM, 1, value)
}

function setCurrent(itemDOM, value) {
    setValue(itemDOM, 2, value)
}

function setSection(itemDOM, value) {
    setValue(itemDOM, 3, value)
}

function setCount(itemDOM, value) {
    itemDOM.children[4].children[0].innerHTML = value;
}

function getCount(itemDOM) {
    return Number(itemDOM.children[4].children[0].innerHTML);
}

function getItemId(itemDOM) {
    return Number(itemDOM.getAttribute('itemid'));
}

function updateItem(item) {
    for (let i = 0; i < table.children.length; i++) {
        if (getItemId(table.children[i]) === item.id) {
            let itemDOM;
            itemDOM = table.children[i];
            setType(itemDOM, item.type);
            setMarking(itemDOM, item.marking);
            setCurrent(itemDOM, item.current);
            setSection(itemDOM, item.section);
            setCount(itemDOM, item.count);
        }
    }
}

function appendColumnToRow(row, columnValue) {
    let column = document.createElement('td');
    column.innerHTML = columnValue ? columnValue : '-';
    row.appendChild(column);
}

function appendColumnWithButtonsToRow(row, columnValue) {
    let column = document.createElement('td');
    let div = document.createElement('span');
    div.innerHTML = columnValue ? columnValue : '-';
    column.appendChild(div);
    appendButton(column, '+', increment);
    appendButton(column, '-', decrement);
    row.appendChild(column);
}

function appendButton(column, content, onclick) {
    let button = document.createElement('button');
    button.innerHTML = content;
    button.onclick = onclick;
    button.classList.add('btn', 'right');
    column.appendChild(button);
}

function getFormDataFromForm(form) {
    let data = new FormData(form);
    data.set('count', data.get('count') ? data.get('count') : 0);
    data.set('current', data.get('current') ? data.get('current') : 0);
    return data;
}

function getFormDataFromTable(row) {
    let data = new FormData();
    data.set('id', row.getAttribute('itemId'));
    data.set('type', convertField(row.children[0].innerHTML));
    data.set('marking', convertField(row.children[1].innerHTML));
    data.set('current', convertField(row.children[2].innerHTML));
    data.set('section', convertField(row.children[3].innerHTML));
    data.set('count', convertField(row.children[4].children[0].innerHTML));
    return data;
}

function convertField(field) {
    return field !== '-' ? field : "";
}

// Click

function onItemClick(e) {
    if (e.path[0].tagName === 'BUTTON')
        return;
    openPopup(findRow(e));
}

function findRow(e) {
    for (let i = 0; i < e.path.length; i++) {
        if (e.path[i].tagName === 'TR')
            return e.path[i];
    }

}

// Modal

let targetItemId;
let editForm = document.getElementById('editItemForm');
let modalType = document.getElementById('modal-type');
let modalMarking = document.getElementById('modal-marking');
let modalCurrent = document.getElementById('modal-current');
let modalSection = document.getElementById('modal-section');
let modalCount = document.getElementById('modal-count');

function putItem(form) {
    const xhr = new XMLHttpRequest();
    xhr.open('PUT', '/item', true);
    xhr.send(form);
    xhr.onreadystatechange = function () { // (3)
        if (xhr.readyState !== xhr.DONE) return;
        if (xhr.status !== 200) {
            alert('Не удалось изменить');
        } else {
            let item = JSON.parse(xhr.responseText);
            updateItem(item);
            closePopup();
        }
    }
}

document.getElementById('editItemButton').onclick = function () {
    let data = getFormDataFromForm(editForm);
    data.set('id', targetItemId);
    putItem(data);
}

function openPopup(itemDOM) {
    let data = getFormDataFromTable(itemDOM);
    modalType.value = data.get('type');
    modalMarking.value = data.get('marking');
    modalCurrent.value = data.get('current');
    modalSection.value = data.get('section');
    modalCount.value = data.get('count');
    targetItemId = data.get('id');
    modal.style.display = "block";
}

function closePopup() {
    modal.style.display = "none";
}

let modal = document.getElementById("editItemModal");

let span = document.getElementsByClassName("close")[0];

span.onclick = closePopup;

window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


// increment / decrement

//  itemState : { itemId , wasChanged, row, sent }
let itemStates = [];

let waitTime = 1500;

function findInArray(id) {
    return itemStates.find((item, arrayId, array) => item.itemId === id);
}

function findOrCreate(id) {
    let state = findInArray(id);
    if (state !== undefined) {
        return state;
    }
    state = {itemId: id, wasChanged: true, sent: false};
    itemStates.push(state);
    return state;
}

function increment(e) {
    startTimer(e, 1);
}

function decrement(e) {
    startTimer(e, -1);
}

function startTimer(e, delta) {
    let row = findRow(e);
    let state = findOrCreate(getItemId(row));
    state.row = row;
    state.sent = false;
    state.wasChanged = true;
    setCount(row, getCount(row) + delta);
    setTimeout(() => waitBeforeSend(getItemId(row)), waitTime);
}

function waitBeforeSend(id) {
    let state = findOrCreate(id);
    if (state.wasChanged) {
        state.wasChanged = false;
        setTimeout(() => waitBeforeSend(id), waitTime);
    } else {
        if (!state.sent) {
            state.sent = true;
            putItem(getFormDataFromTable(state.row));
        }
    }
}

