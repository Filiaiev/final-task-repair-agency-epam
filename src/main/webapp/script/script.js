const orderPattern = "^[A-zÀ-ÿ³¿º¸²¯ª¨,.!?:'\";/@#¹$%^&*()-_=\\d\\s]{10,200}$";

function createOrder(areaId, localizedName, errorMessage, successMessage, allowedMessage){
    const orderRe = new RegExp(orderPattern);
    var textArea = document.getElementById(areaId);
    var text = textArea.value.replace(/\r\n|\n|\r/, " ");
    var p = document.getElementById("message-holder");
    if(!orderRe.test(text)){
        setErrorBorder(textArea);
        p.innerHTML = localizedName + " " + errorMessage + "<br/>" + allowedMessage +
            ": " + orderPattern;
        p.className = "text-danger text-center w-25";
    }else{
        setSuccessBorder(textArea);
        var xhttp = new XMLHttpRequest();
       /*
        *  Replacing line separator to heximal code of it
        *  So the it`s parsed correctly and setted to URL
       */
        let url = "controller?command=createOrder" + "&" + "orderText=" +
        textArea.value;
            // .replace(/\r\n|\n|\r/, "%0A");
        xhttp.open("post", encodeURI(url));
        xhttp.send();
        xhttp.onload = function (){
            p.innerHTML = localizedName + " " + successMessage;
            p.className = "text-success text-center w-25";
        }
    }
}

const commentPattern = "^[A-zÀ-ÿ³¿º¸²¯ª¨,.!?:'\";/@#¹$%^&*()-_=\\d\\s]{10,50}$";

function validateCommentCreation(areaId, localizedName, errorMessage, allowedMessage){
    const commentRe = new RegExp(commentPattern);
    var textArea = document.getElementById(areaId);
    var text = textArea.value.replace(/\r\n|\n|\r/, " ");
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
