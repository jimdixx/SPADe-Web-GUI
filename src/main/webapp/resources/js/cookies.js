function setCookie(typeOfCookie) {

     let type;
     let checkBoxes;

     if (typeOfCookie == 0) {
       type = "projects";
       checkBoxes = [...document.querySelectorAll('*[id^="project_"]')].filter(x => x.checked).map(x => x.value)
     } else {
       type = "antipatterns";
       checkBoxes = [...document.querySelectorAll('*[id^="anti-pattern_"]')].filter(x => x.checked).map(x => x.value)
     }

    let jsonString = JSON.stringify(checkBoxes);

    const expirationDate = 60*60*24*7*1000 //expires in one week
    const date = new Date();
    date.setTime(date.getTime() + expirationDate);

    let expires = "expires="+date.toUTCString();

    document.cookie = type + "=" + jsonString + ";" + expires + ";path=/";
}

function setCheckboxes() {
    var projects = readCookie("projects");
    if (projects != "") {
        projects = JSON.parse(projects);
        setCheckboxes2(0, projects);
    }

    var antipatterns = readCookie("antipatterns");
    if (antipatterns != "") {
        antipatterns = JSON.parse(antipatterns);
        setCheckboxes2(1, antipatterns);
    }

    projectCheckboxesAllIndeterminate(this, 0);
    projectCheckboxesAllIndeterminate(this, 1);

}

function setCheckboxes2(selectPattern, array) {

   var globalCheckboxPattern = "";
   var checkboxPattern = "";

   if (selectPattern == 0) {
       checkboxPattern = '*[id^="project_"]';
   } else {
       checkboxPattern = '*[id^="anti-pattern_"]';
   }

    var projectCheckboxes = document.querySelectorAll(checkboxPattern);



    for (let i = 0; i < array.length; i++) {
        for (let j = 0; j < projectCheckboxes.length; j++) {
            if (array[i] == projectCheckboxes[j].value) {
                projectCheckboxes[j].checked = true;
                break;
            }
        }
    }

}

function readCookie(name) {
    name += '=';
    for (var ca = document.cookie.split(/;\s*/), i = ca.length - 1; i >= 0; i--)
        if (!ca[i].indexOf(name))
            return ca[i].replace(name, '');

    return "";
}