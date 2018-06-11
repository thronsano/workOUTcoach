function changePaid(id) {
    document.getElementById("paymentID").value = id;
    document.getElementById("myForm").submit();
}

function rowClicked(value) {
    location.href = "/appointmentPage?id=" + value;
}

