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
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">非独立运行中心设立申请表</h3>
    <table class="print_table">
        <tr>
            <td style="width:25%;">中心名称</td>
            <td style="width:25%;" ng-bind="vm.item.centerName"></td>
            <td style="width:25%;">依托单位</td>
            <td style="width:25%;" ng-bind="vm.item.deptName"></td>
        </tr>
        <tr>
            <td>研究方向</td>
            <td  colspan="3" ng-bind="vm.item.researchDirection"></td>
        </tr>
        <tr>
            <td>上传附件(技术中心设立申请书)</td>
            <td  colspan="3">
                <div  ng-repeat="file in vm.printData.fileList"><span ng-bind="file.fileName+'_'+file.creatorName"></span></div>
            </td>
        </tr>
        <tr>
            <td>联系人</td>
            <td ng-bind="vm.item.linkManName"></td>
            <td>电话</td>
            <td ng-bind="vm.item.linkManPhone"></td>
        </tr>
        <tr>
            <td rowspan="2">依托单位负责人意见</td>
            <td colspan="3" style="height:100px;text-align:left;vertical-align:top;padding-right:10px;padding-top: 10px;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">单位负责人</td>
            <td>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部意见</td>
            <td colspan="3" style="text-align:left;height:100px;vertical-align:top;padding-right:10px;padding-top: 10px;" ng-bind="vm.printData.informationEquipmentMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">信息化建设与管理部负责人</td>
            <td>
                <span ng-bind="vm.printData.informationEquipmentMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentMen.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.informationEquipmentMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">专家意见</td>
            <td colspan="3" style="text-align:left;height:200px;vertical-align:top;padding-right:10px;padding-top: 10px;" ng-bind="vm.printData.specialist.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">专家意见</td>
            <td>
                <span ng-bind="vm.printData.specialist.userName"></span>
                <span ng-if="vm.printData.specialist.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.specialist.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.specialist.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>