function setStatusColor(id, status_id){
    /*
         0 - created, 1 - waiting for payment, 2 - paid
         3 - canceled, 4 - in work, 5 - completed
    */
    status_id = parseInt(status_id);
    var style_holder = document.getElementById(id);
    switch (status_id){
        case 1:
            style_holder.style.backgroundColor = "orange";
            break;
        case 2:
            style_holder.style.backgroundColor = "khaki";
            break;
        case 3:
            style_holder.style.backgroundColor = "lightcoral";
            style_holder.style.color = "white";
            break;
        case 4:
            style_holder.style.backgroundColor = "yellow";
            break;
        case 5:
            style_holder.style.backgroundColor = "green";
            style_holder.style.color = "white";
            break;
    }
}

function managerGetOrders(){
    var xhttp = new XMLHttpRequest();
    xhttp.open("post", "controller?command=receive_orders");
    xhttp.send()

    xhttp.onload = function (){
        var filterDiv = document.getElementById("com.filiaiev.agency.filter");
        filterDiv.style.display = "block";
    }
}

function showAll(){
    var elements = document.getElementsByClassName("show-element");
    var disp;
    for (let i = 0; i < elements.length; i++) {
        disp = elements[i].id == "orders-table" ? "block" : "inline-block";

        if(elements[i].style.display == "none"){
            elements[i].style.display = disp;
        }else{
            elements[i].style.display = "none";

        }
    }
}

function printJSON(){
    var xhttp = new XMLHttpRequest();
    xhttp.open("get", "controller?command=receive_orders", false);
    xhttp.send();
    alert(xhttp.responseURL);
}

function setMaxDate(){
    let currentDate = new Date();
    let minDate = new Date(currentDate.getFullYear()-18, currentDate.getMonth(), currentDate.getUTCDate())
        .toISOString().slice(0, 10);
    let dateInput = document.querySelector("input[type='date']");
    dateInput.setAttribute("max", minDate);
}

function showHideCreationForm(){
    var form = document.getElementById("creation-form");
    if(form.className === "d-flex"){
        form.className = null;
        form.style.display = "none";
    }else{
        form.className = "d-flex";
        form.style.display = null;
    }
}