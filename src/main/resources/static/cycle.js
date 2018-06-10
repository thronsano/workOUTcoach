function showSchemes() {
    var amount = document.getElementById("numberOfSchemes").value;
    for (var i = 1; i <=amount; i++) {
        document.getElementById("schemeList").innerHTML =
            '<div>Scheme: <select name="schemeId">' +
            ' <option th:each="scheme : ${schemeList}" th:value="${scheme.getId()}"th:text="${scheme.getTitle()}">' +
            '</option></select></div>';
    }
}