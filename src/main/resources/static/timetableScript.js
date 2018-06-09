function buttonClicked(value) {
    var offset = getURLParameter('offset');
    if (offset == null)
        offset = value;
    else
        offset = parseInt(offset)+ parseInt(value);

    document.location.href = "/home?offset=" + offset;
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
    return null;
}