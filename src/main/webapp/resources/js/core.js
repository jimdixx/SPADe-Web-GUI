function projectCheckboxesAllIndeterminate(checkbox, selectPattern) {

    var globalCheckboxPattern = "";
    var checkboxPattern = "";

    if (selectPattern == 0) {
        globalCheckboxPattern = '*[id^="select_all_projects"]';
        checkboxPattern = '*[id^="project_"]';
    } else {
        globalCheckboxPattern = '*[id^="select_all_anti_patterns"]';
        checkboxPattern = '*[id^="anti-pattern_"]';
    }

    var projectCheckboxes = document.querySelectorAll(checkboxPattern);
    var checkedCounter = 0;

    for (let i = 0; i < projectCheckboxes.length; i++) {
            if(projectCheckboxes[i].checked) {
                setCookie(selectPattern);
                checkedCounter++;
            }
     }
        var projectSelectAllCheckbox = document.querySelector(globalCheckboxPattern);

        if (checkedCounter <= 0) {
            projectSelectAllCheckbox.indeterminate = false;
            projectSelectAllCheckbox.checked = false;
        } else if (checkedCounter < projectCheckboxes.length) {
            projectSelectAllCheckbox.indeterminate = true;
        } else {
            projectSelectAllCheckbox.indeterminate = false;
            projectSelectAllCheckbox.checked = true;
        }

}

// script to popover
$(document).ready(function () {
    $('[data-toggle="popover"]').popover();
});

// functions used in index page
function checkAllProjects(checkBox) {
    var projects = document.querySelectorAll('*[id^="project_"]');
    var i;
    for (i = 0; i < projects.length; i++) {
        projects[i].checked = !!checkBox.checked;
    }
    setCookie(0);
}

function checkAllAntiPatterns(checkBox) {
    var antiPatterns = document.querySelectorAll('*[id^="anti-pattern"]');
    var i;
    for (i = 0; i < antiPatterns.length; i++) {
        antiPatterns[i].checked = !!checkBox.checked;
    }
    setCookie(1);
}

function showProgressBar() {
    $('#progressBar').css('display', '');
    var analyzeButton = $('#analyzeButton');
    analyzeButton.addClass('disabled');
    analyzeButton.text('Detecting...');
}