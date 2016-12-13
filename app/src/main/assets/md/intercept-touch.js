window.onload = function(){
    addTouchEvents(document.getElementsByTagName("pre"));
    addTouchEvents(document.getElementsByTagName("table"));
};

function addTouchEvents(elements){
    for(var i = 0; i < elements.length; i++){
        elements[i].addEventListener("touchstart", touchStart, false);
        elements[i].addEventListener("touchend", touchEnd, false);
    }
}

function touchStart(event){
    Android.startIntercept();
}

function touchEnd(event){
   Android.stopIntercept();
}