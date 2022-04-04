/**
 * Copying inner HTML from content editable div to textarea
 */
$('#operationalization-form').submit(function() {
    document.getElementById("contentTextArea").innerHTML = $("#content-area").html();
    return true;
});

/**
 * Make editor not editable when page is loaded
 */
window.addEventListener("load",function(event) {
    makeEditorNotEditable();
},false);

/**
 * Set operationalization editor not editable
 */
function makeEditorNotEditable(){
    let header = document.getElementById('editorHeader');
    let save_button = document.getElementById('op_save_button');
    let content_area = document.getElementById('content-area');

    header.hidden = true;
    save_button.hidden = true;
    content_area.contentEditable = 'false';
    content_area.style.backgroundColor = '#e9ecef';
}

/**
 * Enable operationalization edit
 */
function enableEdit(){
    let header = document.getElementById('editorHeader');
    let save_button = document.getElementById('op_save_button');
    let content_area = document.getElementById('content-area');

    if(header.hidden === true){
        header.hidden = false;
        save_button.hidden = false;
        content_area.contentEditable = 'true';
        content_area.style = 'border-radius: 0rem 0rem 0.25rem 0.25rem; background-color: #FFFFFF';
    }
    else{
        header.hidden = true;
        save_button.hidden = true;
        content_area.contentEditable = 'false';
        content_area.style = 'border-radius: 0.25rem; background-color: #e9ecef';
    }

}

/**
 * Event listener to the editor buttons
 */
const elements = document.querySelectorAll('.btn');
elements.forEach(element => {
    element.addEventListener('click', () => {
        let command = element.dataset['element'];

        if(command == 'createLink'){
            let url = prompt('Enter the link: ', '');
            document.execCommand(command, false, url);
        }
        else if(command == 'insertImage'){
            let input = document.getElementById('imageInput');
            input.click();
        }
        else if(command == 'header1'){
            headerH1();
        }
        else if(command == 'header2'){
            headerH2();
        }
        else if(command == 'header3'){
            headerH3();
        }
        else if(command == 'codeFormat'){
            codeFormat();
        }
        else{
            document.execCommand(command, false, null);
        }

    });
});

/**
 * Event listener for input of the image in the editor
 * - clicking submit button (to save image on the server)
 * - inserting img tag with src to the content
 */
document.getElementById('imageInput').addEventListener('change', (event) => {
    if(document.getElementById('imageInput').files[0] == null)
        return;

    document.getElementById('upload-button').click();

    setTimeout(() => {
        let imageName = document.getElementById('imageInput').files[0].name;
        let imageString = "<img src=\"/operationalizations/images/" + imageName + "\">";
        document.execCommand('insertHTML', false, imageString.toString());
    }, 1000);
});

/**
 * Uploads file to the server by sending it in the POST
 */
async function uploadFile() {
    let formData = new FormData();
    formData.append("file", document.getElementById('imageInput').files[0]);
    let response = await fetch('/uploadImage', {
        method: "POST",
        body: formData
    });

    if(response.status == 200)
        return;
    else{
        alert("Image upload error.");
        document.getElementById('imageInput').files[0] = null;
        return;
    }
}

/**
 * Wraping selected text by h1 tag
 */
function headerH1() {
    let S=window.getSelection().toString();
    if(S.length == 0)
        return;
    if(window.getSelection().anchorNode.parentNode.nodeName.toLowerCase() ===  'h1'){
        document.execCommand('formatBlock', false, 'div');
        document.execCommand('insertHTML',false, S);
    }
    else{
        document.execCommand('delete',false,'');
        document.execCommand('insertHTML',false,'<h1>'+S+'</h1>');
    }
}

/**
 * Wraping selected text by h2 tag
 */
function headerH2() {
    let S=window.getSelection().toString();
    if(S.length == 0)
        return;
    if(window.getSelection().anchorNode.parentNode.nodeName.toLowerCase() ===  'h2'){
        document.execCommand('formatBlock', false, 'div');
        document.execCommand('insertHTML',false, S);
    }
    else{
        document.execCommand('delete',false,'');
        document.execCommand('insertHTML',false,'<h2>'+S+'</h2>');
    }
}

/**
 * Wraping selected text by h3 tag
 */
function headerH3() {
    let S=window.getSelection().toString();
    if(S.length == 0)
        return;
    if(window.getSelection().anchorNode.parentNode.nodeName.toLowerCase() ===  'h3'){
        document.execCommand('formatBlock', false, 'div');
        document.execCommand('insertHTML',false, S);
    }
    else{
        document.execCommand('delete',false,'');
        document.execCommand('insertHTML',false,'<h3>'+S+'</h3>');
    }
}

function codeFormat() {
    let S=window.getSelection().toString();
    if(S.length == 0)
        return;
    if(window.getSelection().anchorNode.parentNode.nodeName.toLowerCase() ===  'pre'){
        document.execCommand('formatBlock', false, 'div');
        document.execCommand('insertHTML',false, S);
    }
    else{
        document.execCommand('delete',false,'');
        document.execCommand('insertText',false,'<pre>'+S+'</pre>');
    }
}



