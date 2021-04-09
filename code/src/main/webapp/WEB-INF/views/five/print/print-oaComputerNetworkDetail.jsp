<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 45px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">非密信息化设备安全准入审批表</h3>
    <table class="print_table">
        <tr>
            <td style="width: 15%">单位</td>
            <td style="width: 35%" ng-bind="vm.printData.deptName"></td>
            <td style="width: 15%">设备信息化编号</td>
            <td  style="width: 35%" ng-bind="vm.printData.equipmentNo"></td>
        </tr>
        <tr>
            <td style="width: 15%">责任人</td>
            <td style="width: 35%" ng-bind="vm.printData.chargeManName"></td>
            <td style="width: 15%">责任人职工号</td>
            <td  style="width: 35%" ng-bind="vm.printData.chargeMan"></td>
        </tr>
        <tr>
            <td style="width: 15%">使用人</td>
            <td style="width: 35%" ng-bind="vm.printData.userName"></td>
            <td style="width: 15%">使用人职工号</td>
            <td  style="width: 35%" ng-bind="vm.printData.userLogin"></td>
        </tr>
        <tr>
            <td style="width: 15%">设备名称</td>
            <td style="width: 35%" ng-bind="vm.printData.equipmentName"></td>
            <td style="width: 15%">设备类型</td>
            <td  style="width: 35%" ng-bind="vm.printData.equipmentType"></td>
        </tr>
        <tr>
            <td style="width: 15%">联系电话</td>
            <td style="width: 35%" ng-bind="vm.printData.linkPhone"></td>
            <td style="width: 15%"></td>
            <td  style="width: 35%" ></td>
        </tr>
        <tr>
            <td style="width: 15%">接入网络</td>
            <td colspan="3"  >
                <input type="checkbox" ng-checked="vm.printData.networkEach=='1'" />
                <span>互联网</span>
                <input type="checkbox" ng-checked="vm.printData.networkNoSecret=='1'" />
                <span>非密内网</span>
                <input type="checkbox" ng-checked="vm.printData.networkMiddle=='1'" />
                <span>中间机</span>
                <input type="checkbox" ng-checked="vm.printData.networkAlone=='1'" />
                <span>单机</span>
                <input type="checkbox" ng-checked="vm.printData.networkOther=='1'" />
                <span>其他</span>
                <span ng-bind="'&nbsp;&nbsp;'+vm.printData.networkOtherRemark"></span>
            </td>
        </tr>
        <tr>
            <td style="width: 15%">申请开发权限</td>
            <td colspan="3"  >
                <input type="checkbox" ng-checked="vm.printData.modelCd=='1'" />
                <span>光驱</span>
                <input type="checkbox" ng-checked="vm.printData.modelUsb=='1'" />
                <span>USB</span>
                <input type="checkbox" ng-checked="vm.printData.modelOther=='1'" />
                <span>其他</span>
                <span ng-bind="'&nbsp;&nbsp;'+vm.printData.modelOtherRemark"></span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;border-top: none;">部门领导意见：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">信息化建设与管理部主管领导审批意见:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.informationEquipmentMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.informationEquipmentMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentMen.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.informationEquipmentMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">网络运维中心服务：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.computerLeader.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.computerLeader.userName"></span>
                <span ng-if="vm.printData.computerLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.computerLeader.userLogin}}"></span>
            <td>日期：</td>
            <td ng-bind="vm.printData.computerLeader.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>