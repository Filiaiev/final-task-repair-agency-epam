function createOrder(){
    var formElements = document.getElementsByClassName("creationForm");
    for (let formElement of formElements) {
        formElement.style.display = "flex";
        formElement.style.flexDirection = "column";
    }
}

function hello(){
    alert('hello');
}

function validatePayment() {
    var client = '<%= Session["Client"]%>';
    alert(client);
}

function hideCreationForm(){
    var formElements = document.getElementsByClassName("creationForm");
    for (let formElement of formElements) {
        formElement.style.display = "none";
    }
}

function sendForm(){
    if(confirm("Do you want to send request?")){
        var xhttp = new XMLHttpRequest();
        var orderText = document.getElementById("formTextArea").value;
        xhttp.open("post", "controller?command=create&orderText="+orderText, true);
        xhttp.send();
        xhttp.onload = function(){
            alert("Запит надіслано!" + orderText);
        };
    }
}

function receiveOrders() {
    var xhttp = new XMLHttpRequest();
    xhttp.open("post", "controller?command=receive_orders", true);
    xhttp.send();
}
