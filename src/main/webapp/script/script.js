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

const orderPattern = "^[A-zÀ-ÿ³¿º¸²¯ª¨,.!?:'\";/@#¹$%^&*()-_=\\d\\s]{10,200}$";
const orderRe = new RegExp(orderPattern, "mg");
const newLineRe = new RegExp("\r\n|\n|\r", "g");

function createOrder(areaId, localizedName, errorMessage, successMessage, allowedMessage){
    var textArea = document.getElementById(areaId);
    var text = textArea.value.replaceAll(newLineRe, " ");
    var p = document.getElementById("message-holder");
    if(!orderRe.test(text)){
        setErrorBorder(textArea);
        p.innerHTML = localizedName + " " + errorMessage + "<br/>" + allowedMessage +
            ": " + orderPattern;
        p.className = "text-danger text-center w-25";
        // console.log(text);
    }else{
        setSuccessBorder(textArea);
        var xhttp = new XMLHttpRequest();
       /* Replacing line separator to heximal code of it
          So the it`s parsed correctly and setted to URL
       */
        var url = "controller?command=createOrder" + "&" + "orderText=" +
            textArea.value.replaceAll(newLineRe, "%0A");
        xhttp.open("post", url);
        xhttp.send();
        xhttp.onload = function (){
            p.innerHTML = localizedName + " " + successMessage;
            p.className = "text-success text-center w-25";
        }
    }
}

const commentPattern = "^[A-zÀ-ÿ³¿º¸²¯ª¨,.!?:'\";/@#¹$%^&*()-_=\\d\\s]{10,50}$";
const commentRe = new RegExp(commentPattern, "mg");

function validateCommentCreation(areaId, localizedName, errorMessage, allowedMessage){
    var textArea = document.getElementById(areaId);
    var text = textArea.value.replaceAll(newLineRe, " ");
    var p = document.getElementById("message-holder");
    console.log(textArea.value);
    if(!commentRe.test(text)) {
        setErrorBorder(textArea);
        p.innerHTML = localizedName + " " + errorMessage + "<br/>" + allowedMessage +
            ": " + commentPattern;
        p.className = "text-danger text-center w-25";
        return false;
    }
    return true;
}

function setMaxDate(){
    let currentDate = new Date();
    let minDate = new Date(currentDate.getFullYear()-18, currentDate.getMonth(), currentDate.getUTCDate())
        .toISOString().slice(0, 10);
    let dateInput = document.querySelector("input[type='date']");
    dateInput.setAttribute("max", minDate);
}

function showHideCreationForm(){
    var location = document.URL;
    var pos = location.indexOf("agency/", 0);
    if(location.slice(pos+"agency".length, location.length) !== "/home"){
        window.location.href = location.slice(0, pos+"agency".length) + "/home";
        var f = document.getElementById("creation-form");
        f.className = "d-flex";
        f.style.display = null;
    }else{
        var form = document.getElementById("creation-form");
        if(form.className === "d-flex"){
            form.className = null;
            form.style.display = "none";
        }else{
            form.className = "d-flex";
            form.style.display = null;
        }
    }
}

const loginPattern = /[a-z][a-z0-9]{5,15}/;
const passPattern = /[A-z0-9]{4,20}/;
const namePattern = /[A-ZÀ-ß²¯ª][a-zà-ÿ³¿º]{2,13}/;
const patterns = [loginPattern, passPattern, namePattern];

function validateRegisterForm(){
    var login = document.getElementById("login");
    var pass = document.getElementById("password");
    var lname = document.getElementById("last-name");
    var mname = document.getElementById("middle-name");
    var fname = document.getElementById("first-name");
    const inputs = [login, pass, lname, mname, fname];
    var successCounter = 0;

    for (let i = 0; i < patterns.length-1; i++) {
        if(patterns[i].test(inputs[i].value)){
            setSuccessBorder(inputs[i]);
            successCounter++;
        }else{
            setErrorBorder(inputs[i]);
        }
    }

    for (let i = patterns.length-1; i < inputs.length; i++) {
        if(patterns[patterns.length-1].test(inputs[i].value)){
            setSuccessBorder(inputs[i]);
            successCounter++;
        }else{
            setErrorBorder(inputs[i]);
        }
    }

    var dateInput = document.getElementById("birth-date");
    if(dateInput.value !== ""){
        setSuccessBorder(dateInput);
        successCounter++;
    }else{
        setErrorBorder(dateInput);
    }
    return successCounter == inputs.length+1;
}

function validateLoginForm(){
    var login = document.getElementById("login");
    var pass = document.getElementById("password");
    const inputs = [login, pass];
    var successCounter = 0;

    for (let i = 0; i < inputs.length; i++) {
        if(patterns[i].test(inputs[i].value)){
            setSuccessBorder(inputs[i]);
            successCounter++;
        }else{
            setErrorBorder(inputs[i]);
        }
    }
    return successCounter == inputs.length;
}

function setErrorBorder(input){
    input.className = "form-control border border-danger";
}

function setSuccessBorder(input){
    input.className = "form-control border border-success";
}
