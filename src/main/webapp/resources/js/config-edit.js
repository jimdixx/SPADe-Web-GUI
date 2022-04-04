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
    if(document.getElementById("configuration-save-button") != null)
        document.getElementById("configuration-save-button").hidden = true;

    if(document.getElementById("configuration-save-button-individual") != null)
        document.getElementById("configuration-save-button-individual").hidden = true;

    if(document.getElementById("configuration-save-as-button") != null)
        document.getElementById("configuration-save-as-button").hidden = false;

    if(document.getElementById("configuration-save-as-input") != null)
        document.getElementById("configuration-save-as-input").hidden = false;
}

/**
 * Show both save buttons
 */
function showBothSaveButtons(){
    if(document.getElementById("configuration-save-button") != null)
    document.getElementById("configuration-save-button").hidden = false;

    if(document.getElementById("configuration-save-button-individual") != null)
        document.getElementById("configuration-save-button-individual").hidden = false;

    if(document.getElementById("configuration-save-as-button") != null)
        document.getElementById("configuration-save-as-button").hidden = false;

    if(document.getElementById("configuration-save-as-input") != null)
        document.getElementById("configuration-save-as-input").hidden = false;
}

/**
 * Hide both save buttons
 */
function hideBothSaveButtons(){
    if(document.getElementById("configuration-save-button") != null)
        document.getElementById("configuration-save-button").hidden = true;

    if(document.getElementById("configuration-save-button-individual") != null)
        document.getElementById("configuration-save-button-individual").hidden = true;

    if(document.getElementById("configuration-save-as-button") != null)
        document.getElementById("configuration-save-as-button").hidden = true;

    if(document.getElementById("configuration-save-as-input") != null)
        document.getElementById("configuration-save-as-input").hidden = true;
}