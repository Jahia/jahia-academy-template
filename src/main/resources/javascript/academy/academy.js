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

if(window.location.hash) {
    var y = $(window).scrollTop();  //your current y position on the page
    $(window).scrollTop(y-180);
}

$(document).ready(function ($) {
    $('a[href^="http://"]').attr('target','_blank');
    $('a[href^="https://"]').attr('target','_blank');
    $("a[href$='.pdf']").attr("target", "_blank");

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
    /*var stretchCounter = 0;
    $( "table.stack" ).each(function( index ) {
        stretchCounter++;
        var id = 'stack'+stretchCounter;
        var dashId = '#stack'+stretchCounter;
        $( this ).attr('id',id);
        var headertext = [];
        var headers = document.querySelectorAll(dashId+" th");
        var tablebody = document.querySelector(dashId+" tbody");

        for(var i = 0; i < headers.length; i++) {
            var current = headers[i];
            headertext.push(current.textContent.replace(/\r?\n|\r/,""));
        }
        for (var i = 0, row; row = tablebody.rows[i]; i++) {
            for (var j = 0, col; col = row.cells[j]; j++) {
                col.setAttribute("data-th", headertext[j]);
            }
        }
    });*/
    $('table.stack').cardtable();
});

