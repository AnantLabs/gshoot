function checkInput() {
    if ($("#q").val() == "") {
        $("#q").focus();
        return false;
    }
    return true;
}