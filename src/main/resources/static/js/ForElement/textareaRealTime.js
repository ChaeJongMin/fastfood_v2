function resizeTextArea(ele) {
    if(ele.keyCode !== 13){
        console.log("resizeTextArea");
        ele.style.height='auto';
        let height = ele.scrollHeight;
        ele.style.height= (height + 8)+'px' ;
    }
};
function resetCursor(txtElement){
    txtElement.setSelectionRange(txtElement.value.length,txtElement.value.length);
}