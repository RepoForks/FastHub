document.addEventListener("DOMContentLoaded", function(event) {
   document.querySelectorAll('img').forEach(function(img){
  	img.onerror = function(){this.style.display='none';};
   });
});

window.onload = function() {
    addTouchEvents(document.getElementsByTagName("pre"));
    addTouchEvents(document.getElementsByTagName("table"));
    addEventListener(document.getElementsByClassName("highlight"));
//    addTouchEvents(document.getElementsByTagName("div"));
};
function addTouchEvents(elements) {
    for (var i = 0; i < elements.length; i++) {
        elements[i].addEventListener("touchstart", touchStart, false);
        elements[i].addEventListener("touchend", touchEnd, false);
//        elements[i].addEventListener("scroll", onScroll, false);
    }
}

function touchStart(event) {
    Android.startIntercept();
}

function touchEnd(event) {
    Android.stopIntercept();
}

function onScroll(event) {
    var width = (event.target.scrollWidth - event.target.clientWidth);
    var scroll = event.target.scrollLeft;
    if (scroll > 0 && scroll < width) {
        touchStart(event);
    } else {
        touchEnd(event);
    }
}
