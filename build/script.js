//<script type="text/javascript">
tmpTargetPage = "" + window.location.search;
if (tmpTargetPage != "" && tmpTargetPage != "undefined")
    tmpTargetPage = tmpTargetPage.substring(1);
if (tmpTargetPage.indexOf(":") != -1 || (tmpTargetPage != "" && !validURL(tmpTargetPage)))
    tmpTargetPage = "undefined";
targetPage = tmpTargetPage;
function validURL(url) {
    try {
        url = decodeURIComponent(url);
    }
    catch (error) {
        return false;
    }
    var pos = url.indexOf(".html");
    if (pos == -1 || pos != url.length - 5)
        return false;
    var allowNumber = false;
    var allowSep = false;
    var seenDot = false;
    for (var i = 0; i < url.length - 5; i++) {
        var ch = url.charAt(i);
        if ('a' <= ch && ch <= 'z' ||
            'A' <= ch && ch <= 'Z' ||
            ch == '$' ||
            ch == '_' ||
            ch.charCodeAt(0) > 127) {
            allowNumber = true;
            allowSep = true;
        } else if ('0' <= ch && ch <= '9'
            || ch == '-') {
            if (!allowNumber)
                return false;
        } else if (ch == '/' || ch == '.') {
            if (!allowSep)
                return false;
            allowNumber = false;
            allowSep = false;
            if (ch == '.')
                seenDot = true;
            if (ch == '/' && seenDot)
                return false;
        } else {
            return false;
        }
    }
    return true;
}
function loadFrames() {
    if (targetPage != "" && targetPage != "undefined")
        top.classFrame.location = top.targetPage;
}
//</script>