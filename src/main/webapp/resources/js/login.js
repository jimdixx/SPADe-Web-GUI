/**
 * Check if user is logged when a page is loaded
 */
window.addEventListener("load",function(event) {
    checkIfUserIsLogged();
},false);

/**
 * Checking if user is logged and then applying the style
 */
function checkIfUserIsLogged() {
    fetch( '/userLogged' )
        .then( response => {
            var url = new URL(response.url);
            var c = url.searchParams.get("user");
            if(c == null)
                applyUnloggedStyle();
            else
                applyLoggedStyle(c);
        });
}

/**
 * Applying style when user is not logged
 */
function applyUnloggedStyle(){
    document.getElementById("login-button").hidden = false;
    document.getElementById("logout-button").hidden = true;
    document.getElementById("current-configuration-select").disabled = true;

    if(document.getElementById("operationalizationEditButton") != null) {
        document.getElementById("operationalizationEditButton").hidden = true;
        document.getElementById("operationalizationEditButton").disabled = true;
    }
}

/**
 * Applying style when user is logged
 */
function applyLoggedStyle(user){
    document.getElementById("login-button").hidden = true;
    document.getElementById("logout-button").hidden = false;
    document.getElementById("user-name-container").innerText = user;
    document.getElementById("user-name-container").hidden = false;
    document.getElementById("current-configuration-select").disabled = false;

    if(document.getElementById("operationalizationEditButton") != null) {
        document.getElementById("operationalizationEditButton").hidden = false;
        document.getElementById("operationalizationEditButton").disabled = false;
    }
}
