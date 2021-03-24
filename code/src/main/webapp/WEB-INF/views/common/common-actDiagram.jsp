<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
<img ng-src="{{'/act/processQuery/image/'+vm.item.processInstanceId+'?time='+refeshTime}}"
     style="max-width: 99%;"/>
--%>

<img ng-src="{{'/myAct/download/'+vm.item.processInstanceId+'?'+refreshTime}}" ng-show="vm.item.processInstanceId" style="max-width: 99%;"/>