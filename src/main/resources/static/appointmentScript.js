function showRepeatAmountField() {
    var repeatElement = document.getElementById("hiddenRepeatAmount");

    if (repeatElement.innerHTML.length === 0)
        repeatElement.innerHTML = 'Repeat for x times:<input type="number" class="input-field" name="repeatAmount" required/>';
    else
        repeatElement.innerHTML = "";
}
