<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area">
    <h3 style="text-align: center;">参保证明申请书</h3>
    <p style="text-align: left">人力资源和社会保障局：</p>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;兹有我公司员工 &nbsp;<span ng-bind="vm.printData.userName">黄南州</span >，身份证号码：<span ng-bind="vm.printData.idCardNo">500231199601161454</span>,社保编码为：<span ng-bind="vm.printData.socialNo">asdasl</span>，现需证明为我单位员工并打印社保局证明，请贵局给予办理。</p>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;特此申请。致谢！</p>
    <p style="margin-top: 500px;text-align: right" ng-bind="vm.printData.companyName">中级中联工程有限公司</p>
    <p style="text-align: right;margin-right: 30px" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'">2019-8-8</p>
</div>