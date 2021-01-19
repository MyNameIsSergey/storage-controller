
let table = document.getElementById('items-table');

function addItem(item) {
    let itemDOM = document.createElement('tr');
    appendColumnToRow(itemDOM, item.type);
    appendColumnToRow(itemDOM, item.marking);
    appendColumnToRow(itemDOM, item.current);
    appendColumnToRow(itemDOM, item.section);
    appendColumnToRow(itemDOM, item.count);
    table.appendChild(itemDOM);
}

function appendColumnToRow(row, columnValue) {
    let column = document.createElement('td');
    column.innerHTML = columnValue;
    row.appendChild(column);
}