<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" hidden>
    <h2 style="text-align: center;">公司级设计评审申报表</h2>
    <table class="print_table">
        <tr>
            <td style="width:20%;height: 30px;" colspan="1">用户单位</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.departmentName"></td>
            <td style="width: 20%;" colspan="1">登记日期</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.registerTime"></td>
        </tr>
        <tr>
            <td style="width:10%;height: 30px;">项目名称</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td style="width:15%;height: 30px;">类别</td>
            <td style="width: 35%;" ng-bind="vm.printData.category"></td>
            <td style="width: 15%;">登记编号</td>
            <td style="width: 35%;" ng-bind="vm.printData.registerNo"></td>
        </tr>
        </table>
    <table class="print_table">

        <tr>
            <td style="width:10%;height: 170px;border-top: none">逐条登记问题内容</td>
            <td style="text-align: left;width:90%;padding-right: 30px;padding-left: 30px;border-top: none" ng-bind="vm.printData.registerContent"></td>
        </tr>
        <tr>
            <td style="height: 170px;">主管领导总设计师签署意见</td>
            <td style="text-align: left;padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.departmentReview"></td>
        </tr>
        <tr>
            <td style="height: 170px;">专业人员处理意见</td>
            <td style="text-align: left;padding-right: 30px;padding-left: 30px;" ng-bind="vm.printData.specialistReview">
            </td>
        </tr>
        <tr>
            <td style="height: 60px;">备注</td>
            <td style="text-align: left">（1） 类别栏，可填电报、传真、电话、信函、来访、图纸会审、设计交底、设计回访。</br>
                （2） 问题内容栏，可附原件，也要直接填写，内容较多时可另附页。</td>
        </tr>
    </table>


</div>