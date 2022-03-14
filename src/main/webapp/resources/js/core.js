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
}

function checkAllAntiPatterns(checkBox) {
    var antiPatterns = document.querySelectorAll('*[id^="anti-pattern"]');
    var i;
    for (i = 0; i < antiPatterns.length; i++) {
        antiPatterns[i].checked = !!checkBox.checked;
    }
}

function showProgressBar() {
    $('#progressBar').css('display', '');
    var analyzeButton = $('#analyzeButton');
    analyzeButton.addClass('disabled');
    analyzeButton.text('Analyzing...');
}