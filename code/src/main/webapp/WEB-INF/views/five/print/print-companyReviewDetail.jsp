<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;">公司级设计评审申报表</h2>
    <table class="print_table">
        <tr>
            <td style="width:20%;height: 30px;" colspan="1">申请单位</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.companyName"></td>
            <td style="width: 20%;" colspan="1">申请日期</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.applyTime"></td>
        </tr>
        <tr>
            <td style="width:10%;height: 30px;">项目名称</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td style="width:10%;height: 30px;">项目性质</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.projectCharacter"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">设计阶段</td>
            <td style="width: 35%;" ng-bind="vm.printData.designStage"></td>
            <td style="width: 15%;">规模</td>
            <td style="width: 35%;" ng-bind="vm.printData.designScale"></td>
        </tr>
        <tr>
            <td style="height: 30px;">项目总师</td>
            <td ng-bind="vm.printData.projectDesigner"></td>
            <td>联系电话</td>
            <td ng-bind="vm.printData.phoneNumber"></td>
        </tr>
        <tr>
            <td style="height: 90px;">涉及专业</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.specialty"></td>
        </tr>
        <tr>
            <td style="height: 60px;">希望评审时间</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.reviewTime"></td>
        </tr>
        <tr>
            <td style="height: 90px;">申请单位意见</td>
            <td colspan="3" style="text-align: left;padding-right: 30px;padding-left: 30px;" ng-bind="vm.printData.companyComment">
               <%-- <p style="text-align: right;padding-top: 50px;padding-right: 30px" ng-bind="'单位负责人签字：'+vm.printData.chargeMan+' '+vm.printData.chargeTime"></p>--%>
            </td>
        </tr>
    </table>
    <table >
        <tr>
            <td style="height: 150px;text-align: left;padding-left: 10px;padding-right: 10px;padding-top: 15px;padding-bottom: 100px">1.项目概况:</br><span ng-bind="vm.printData.projectState" ></span></td>

        </tr>
        <tr>
            <td style="height: 150px;text-align: left;padding-left: 10px;padding-right: 10px">2.提请评审内容/要点或申请理由: </br><span ng-bind="vm.printData.content" ></span></td>

        </tr>

    </table>

</div>