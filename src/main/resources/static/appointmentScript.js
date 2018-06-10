function toggleRepeatAmountField() {
    var repeatElement = document.getElementById("hiddenRepeatAmount");

    if (repeatElement.innerHTML.length === 0)
        repeatElement.innerHTML = 'Repeat for x times:<input type="number" min="0" class="input-field" name="repeatAmount" required />';
    else
        repeatElement.innerHTML = "";
}

function getURLParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
            return sParameterName[1];
        }
    }
}
