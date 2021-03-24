<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h3 style="text-align: center;">在职证明</h3>
    <br>
    <br>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;兹证明 &nbsp;<span ng-bind="vm.printData.userName">黄南州</span>（身份证号码：
        <span ng-bind="vm.printData.idCardNo">500231199601161454</span>）,系我单位在职员工，从
        <span  ng-bind="vm.printData.joinCompanyTime">2019-7-2</span>起在我单位工作至今。</p>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;特此证明</p>
    <p style="margin-top: 500px;text-align: right" ng-bind="vm.printData.companyName">中级中联工程有限公司</p>
    <p style="text-align: right;margin-right: 30px" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'">2019-8-8</p>
</div>