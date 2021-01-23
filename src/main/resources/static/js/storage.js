

document.getElementById('addItemButton').onclick = onFormSubmit;

loadItems();

function onFormSubmit() {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/item', true);
    let data = new FormData(document.getElementById('newItemForm'));
    data.append('storageId', getStorageId());
    xhr.send(data);
    xhr.onreadystatechange = function() { // (3)
        if (xhr.readyState !== xhr.DONE) return;
        if (xhr.status !== 200) {
            alert('Не удалось добавить');
        } else {
            let item = JSON.parse(xhr.responseText);
            addItem(item);
        }
    }
}

function loadItems() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '/item/all?storageId=' + getStorageId(), true);
    xhr.send();
    xhr.onreadystatechange = function() { // (3)
        if (xhr.readyState !== XMLHttpRequest.DONE) return;
        if (xhr.status !== 200) {
            alert(xhr.status + ': ' + xhr.statusText);
        } else {
            let items = JSON.parse(xhr.responseText);
            for(let i = 0; i < items.length; i++) {
                addItem(items[i]);
            }
        }
    }
}

function getStorageId() {
    return Number(window.location.href.split('/').pop());
}