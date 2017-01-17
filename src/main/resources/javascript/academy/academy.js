//TAKE THE TALLEST DOC HEIGHT
function setdocheight() {

    if ($(window).width() > 998) {

        var max = -1;

        $(".doc-height").css("height", "auto");

        $(".doc-height").each(function () {
            var h = $(this).height();
            max = h > max ? h : max;
        });

        $(".doc-height").css("height", max + 50, "px");

    } else {
        $(".doc-height").css("height", "auto");
    }
    ;

};


// FUNCTION CALLS

// on page load
setdocheight();


// on resize
window.onresize = function (event) {
    setdocheight();
};


$(document).on('click', 'a[data-scrollto]', function (event) {
    event.preventDefault();
    scrollto = $.attr(this, 'data-scrollto');
    offset = scrollto === "#top" ? 0 : 170;
    $('html, body').animate({
        scrollTop: $(scrollto).offset().top - offset
    }, 200, function () {
        window.location.hash = scrollto;
    });
    scrollBy(0, -20);
    return false;
});



$(document).ready(function ($) {
    $('a[href^="http://"]').attr('target','_blank');
    $('a[href^="https://"]').attr('target','_blank');

    // delegate calls to data-toggle="lightbox"
    $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
        event.preventDefault();
        $(this).ekkoLightbox({
            always_show_close: false
        });
    });
    $(".navbar-fixed-top").headroom({
        "offset": 180,
        "tolerance": 5
    });
});

