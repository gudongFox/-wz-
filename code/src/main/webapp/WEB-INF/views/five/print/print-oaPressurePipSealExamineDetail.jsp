<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 55px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">压力管道设计资格印章使用审批表 </h3>
    <table class="print_table">
        <tr>
            <td rowspan="2" style="width:15%;">盖章</td>
            <td colspan="3" style="text-align:left;padding-left:50px;padding-left:50px;border-bottom:none;" ng-bind="vm.item.seal"></td>
        </tr>
        <tr>
            <td colspan="3" style="text-align:left;padding-left:15px;border-top:none;">线上盖章：由成品室自动加盖电子印章；</br>线下盖章：到信息化建设与管理部手动加盖实体印章。</td>
        </tr>
        <tr>
            <td style="width:15%;">申请人</td>
            <td style="width:35%;" ng-bind="vm.item.applyManName"></td>
            <td style="width:15%;">申请人电话</td>
            <td style="width:35%;" ng-bind="vm.item.applyManLink"></td>
        </tr>
        <tr>
            <td>用印单位</td>
            <td ng-bind="vm.item.deptName"></td>
            <td>用印时间</td>
            <td ng-bind="vm.item.useTime"></td>
        </tr>
        <tr>
            <td>设计项目名称</td>
            <td colspan="3" style="text-align:left;padding-left:50px;padding-left:50px" ng-bind="vm.item.projectName"></td>
        </tr>
        <tr>
            <td>管道平面布置图名称/图号</td>
            <td ng-bind="vm.item.pipDrawName"></td>
            <td>图纸完成时间</td>
            <td ng-bind="vm.item.drawCompleteTime"></td>
        </tr>
        <tr>
            <td>压力管道类/级别</td>
            <td ng-bind="vm.item.pressurePipType"></td>
            <td>图纸用章份数</td>
            <td ng-bind="vm.item.drawNum"></td>
        </tr>
        <tr>
            <td rowspan="2">用章单位负责人</td>
            <td colspan="3" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.item.deptChargeManComment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;"></td>
        </tr>
        <tr>
            <td rowspan="2">压力管道设计技术负责人</td>
            <td colspan="3" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.item.pressurePipChargeManComment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;"></td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部</td>
            <td colspan="3" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.item.technologyChargeManComment"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top:none;"></td>
        </tr>
        <tr>
            <td>盖章人</td>
            <td ng-bind="vm.item.sealManName"></td>
            <td>监章人</td>
            <td ng-bind="vm.item.superviseManName"></td>
        </tr>
    </table>
</div>