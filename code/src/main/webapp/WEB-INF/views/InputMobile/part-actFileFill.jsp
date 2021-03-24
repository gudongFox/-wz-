
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="weui-panel">

    <div class="weui-cell">
        <div class="weui-cell__bd">业务附件</div>
        <div class="weui-cell__ft">
            <a class="weui-btn weui-btn_mini weui-btn_primary" href="/InputMobile/uploadFile">上传</a>
        </div>
    </div>
    <div class="weui-panel__bd">
        <div class="weui-media-box weui-media-box_small-appmsg">
            <div class="weui-cells">
                <c:forEach var="file" items="${files}">
                    <a class="weui-cell weui-cell_access" href="/mobile/file">
                        <div class="weui-cell__hd"><img src="/m/img/filetype/${file.extensionName}.png" alt=""
                                                        style="width:20px;margin-right:5px;display:block"></div>
                        <div class="weui-cell__bd weui-cell_primary">
                            <p>${file.fileName}</p>
                        </div>
                        <span class="weui-cell__ft"></span>
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
</div>