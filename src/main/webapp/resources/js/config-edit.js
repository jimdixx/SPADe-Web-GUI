/**
 * Check if current configuration is editable by user
 */
window.addEventListener("load",function(event) {
    var currentConfigurationValue = document.getElementById("current-configuration-select").value;
    checkIfConfigIsDefault(currentConfigurationValue);
},false);

/**
 * Sending request, checking and applying style
 */
function checkIfConfigIsDefault(currentConfigurationValue) {
    fetch( '/isConfigEditable/' + currentConfigurationValue)
        .then( response => {
            var url = new URL(response.url);
            var user = url.searchParams.get("user");
            var configurationDefault = url.searchParams.get("default")
            if(user != null){
                if(configurationDefault == "true")
                    showSaveAsButton();
                else
                    showBothSaveButtons();
            }
            else{
                hideBothSaveButtons();
            }
        });
}

/**
 * Show only 'Save as' button
 */
function showSaveAsButton(){
    document.getElementById("configuration-save-button").hidden = true;
    document.getElementById("configuration-save-as-button").hidden = false;
}

/**
 * Show both save buttons
 */
function showBothSaveButtons(){
    document.getElementById("configuration-save-button").hidden = false;
    document.getElementById("configuration-save-as-button").hidden = false;
}

/**
 * Hide both save buttons
 */
function hideBothSaveButtons(){
    document.getElementById("configuration-save-button").hidden = true;
    document.getElementById("configuration-save-as-button").hidden = true;
}