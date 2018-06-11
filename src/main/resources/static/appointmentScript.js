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

function buttonClicked(value) {
    var offset = getURLParameter('edit');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset) + parseInt(value);

    document.location.href = updateURLparameters("appointmentPage", "edit", true);
}

function updateURLparameters(page, key, value) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.length === 0 ? "" : sPageURL.split('&&');
    var updatedVariables = "/" + page + "?";
    var variableAlreadyExists = false;

    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] == key) {
            sParameterName[1] = value;
            variableAlreadyExists = true;
        }
        updatedVariables += sParameterName[0] + "=" + sParameterName[1];
        if (i + 1 < sURLVariables.length)
            updatedVariables += "&&"
    }

    if (!variableAlreadyExists) {
        if(sURLVariables.length > 0)
            updatedVariables += "&&"
        updatedVariables += key + "=" + value;
    }

    return updatedVariables;
}


