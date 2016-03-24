<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ActionView</title>
    <script src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {
            setInterval(function() {
                $.ajax({
                   url: "/sk/viewRegion",
                   type: "GET",
                   success: function(data) {
                       document.getElementById("regionId").innerHTML = data.toString();
                   }
                });
            }, 2500);
        });
    </script>
</head>
<body>
    <h1 id="regionId"></h1>
</body>
</html>
