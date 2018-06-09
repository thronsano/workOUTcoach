function editGoal() {
    var id = document.getElementById("hiddenGoalID").value;
    var goal = document.getElementById("goal").innerText;
    document.getElementById("goalSection").innerHTML =
        '<form action="/clientProfile/goal" method="get">' +
        '<input type="hidden" name="clientID" id="hiddenID" value="' + id + '">' +
        '<input type="text" name="goal" id="goal" placeholder="' + goal + '">' +
        '<button type="submit" th:href="@{/clientProfile/goal}" id="goalButton">OK</button></form>';
}

function editHealthCondition() {
    var id = document.getElementById("hiddenHealthID").value;
    var health = document.getElementById("health").innerText;
    document.getElementById("healthSection").innerHTML =
        '<form action="/clientProfile/health" method="get">' +
        '<input type="hidden" name="clientID" id="hiddenID" value="' + id + '">' +
        '<input type="text" name="health" id="health" placeholder="' + health + '">' +
        '<button type="submit" th:href="@{/clientProfile/health}" id="healthButton">OK</button></form>';
}

function editPhoneNumber() {
    var id = document.getElementById("hiddenPhoneID").value;
    var phone = document.getElementById("phone").innerText;
    document.getElementById("phoneSection").innerHTML =
        '<form action="/clientProfile/phone" method="get">' +
        '<input type="hidden" name="clientID" id="hiddenID" value="' + id + '">' +
        '<input type="text" name="phone" id="phone" placeholder="' + phone + '">' +
        '<button type="submit" th:href="@{/clientProfile/phone}" id="phoneButton">OK</button></form>';
}

function editGymName() {
    var id = document.getElementById("hiddenGymID").value;
    var gym = document.getElementById("gym").innerText;
    document.getElementById("gymSection").innerHTML =
        '<form action="/clientProfile/gym" method="get">' +
        '<input type="hidden" name="clientID" id="hiddenID" value="' + id + '">' +
        '<input type="text" name="gym" id="gym" placeholder="' + gym + '">' +
        '<button type="submit" th:href="@{/clientProfile/gym}" id="gymButton">OK</button></form>';
}

function GetURLParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
            return sParameterName[1];
        }
    }
}