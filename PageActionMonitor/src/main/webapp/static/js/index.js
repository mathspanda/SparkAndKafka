/**
 * Created by MP on 23/3/16.
 */
$(document).ready(function() {
    $("#collectorDiv").mousemove(function(event) {
        var postData = {"x": event.pageX, "y": event.pageY};
        $.ajax({
            url: "/sk/coordinate",
            type: "POST",
            data: postData,
            success: function() {

            },
            error: function() {

            }
        });
    });
});
