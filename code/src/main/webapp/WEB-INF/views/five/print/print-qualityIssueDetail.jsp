<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area ">
    <h2 style="text-align: center;">设计质量问题登记</h2>
    <table class="print_table">
        <tr>
            <td style="width: 20%;height: 30px;" colspan="1">项目名称</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.projectName"></td>
            <td style="width: 20%;" colspan="1">审核级别</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.examineLeve"></td>
        </tr>
        <tr>
            <td>设计人</td>
            <td ng-bind="vm.printData.designMan"></td>
            <td>原审核人</td>
            <td ng-bind="vm.printData.checkMan"></td>
        </tr>
        <tr>
            <td>信息来源</td>
            <td colspan="3" ng-bind="vm.printData.informationSource"></td>
        </tr>
        </table>
    <table class="print_table">

        <tr>
            <td style="width:10%;height: 230px;border-top: none">设计质量问题内容</td>
            <td style="text-align: left;width:90%;padding-right: 30px;padding-left: 30px;border-top: none" ng-bind="vm.printData.designContent"></td>
        </tr>
        <tr>
            <td style="height: 230px;">处理措施</td>
            <td style="text-align: left;padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.dealContent"></td>
        </tr>
        <tr>
            <td style="height: 170px;">备注</td>
            <td style="text-align: left;padding-right: 30px;padding-left: 30px;" ng-bind="vm.printData.remark">
            </td>
        </tr>

    </table>


</div>