<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/12/18
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${fileName}</title>
    <link rel="shortcut icon" href="/m/favicon.ico"/>
</head>
<body>
<div id="wmwrapper" style="width: 100%;height: 100%;text-align: center">
    <iframe style="max-width:800px;width: 100%;height: 100%" src="${src}" frameborder="0"
            referrerpolicy="no-referrer-when-downgrade">
    </iframe>
</div>

<script type="text/javascript" src="/assets/js/canvaswm.js"></script>
<script>
    var sec = '${sec}';
    __canvasWM({
        content: sec
    });
</script>
</body>
</html>









