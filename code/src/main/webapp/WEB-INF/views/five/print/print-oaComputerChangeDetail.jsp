<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
        margin-top: -1px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 35px;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
    .prevtitle{
        text-align:center;
        background-color:#dcdcdc;
    }
    .print_table .topline{
        border-top:none;
    }
    .print_table .bottomline{
        border-bottom:none;
    }
    .print_table .leftline{
        border-left:none;
    }
    .print_table .rightline{
        border-right:none;
    }
    .text_right{
        text-align:right;
    }


</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">非密计算机策略变更审批表</h3>
    <table class="print_table">
        <tr>
            <td style="width:12%;" class="prevtitle">申请人</td>
            <td colspan="2" style="width:22%;">姓名</td>
            <td style="width:22%;" ng-bind="vm.printData.applyUserName"></td>
            <td style="width:22%;">联系电话:</td>
            <td style="width:22%;" ng-bind="vm.printData.applyPhone"></td>
        </tr>
        <tr>
            <td rowspan="2" class="prevtitle">设备基础信息</td>
            <td colspan="2" >设备信息化编号:</td>
            <td ng-bind="vm.printData.computerNo"></td>
            <td>设备名称:</td>
            <td ng-bind="vm.printData.computerName"></td>
        </tr>
        <tr>
            <td colspan="2" >资产编号</td>
            <td ng-bind="vm.printData.assetNo"></td>
            <td>MAC地址</td>
            <td ng-bind="vm.printData.macAddress"></td>
        </tr>
        <tr>
            <td rowspan="17" style="width:12%;" class="prevtitle">变更内容</td>
            <td colspan="2" class="prevtitle">变更项</td>
            <td class="prevtitle">选择</td>
            <td class="prevtitle">变更前</td>
            <td class="prevtitle">变更后</td>
        </tr>
        <tr>
            <td rowspan="4" style="width:8%">责任人</td>
            <td>姓名</td>
            <td rowspan="4"><input type="checkbox" ng-checked="vm.item.dutyName!=vm.item.newDutyName&&vm.item.newDutyName.length!=0" /></td>
            <td ng-bind="vm.printData.dutyName"></td>
            <td ng-bind="vm.printData.newDutyName"></td>
        </tr>
        <tr>
            <td>职工号</td>
            <td ng-bind="vm.printData.dutyLogin"></td>
            <td ng-bind="vm.printData.newDutyLogin"></td>
        </tr>
        <tr>
            <td>密级</td>
            <td ng-bind="vm.printData.dutySecurity"></td>
            <td ng-bind="vm.printData.newDutySecurity"></td>
        </tr>
        <tr>
            <td>所属单位</td>
            <td ng-bind="vm.printData.dutyDeptName"></td>
            <td ng-bind="vm.printData.newDutyDeptName"></td>
        </tr>
        <tr>
            <td rowspan="4">使用人</td>
            <td>姓名</td>
            <td rowspan="4"><input type="checkbox" ng-checked="vm.item.usersName!=vm.item.newUsersName&&vm.item.newUsersName.length!=0" /></td>
            <td ng-bind="vm.printData.usersName"></td>
            <td ng-bind="vm.printData.newUsersName"></td>
        </tr>
        <tr>
            <td>职工号</td>
            <td ng-bind="vm.printData.usersLogin"></td>
            <td ng-bind="vm.printData.newUsersLogin"></td>
        </tr>
        <tr>
            <td>密级</td>
            <td ng-bind="vm.printData.usersSecurty"></td>
            <td ng-bind="vm.printData.newUsersSecurty"></td>
        </tr>
        <tr>
            <td>所属单位</td>
            <td ng-bind="vm.printData.usersDeptName"></td>
            <td ng-bind="vm.printData.newUsersDeptName"></td>
        </tr>
        <tr>
            <td colspan="2">设备所属单位</td>
            <td><input type="checkbox" ng-checked="vm.item.deptName!=vm.item.newDeptName&&vm.item.newDeptName.length!=0" /></td>
            <td ng-bind="vm.printData.deptName"></td>
            <td ng-bind="vm.printData.newDeptName"></td>
        </tr>
        <tr>
            <td colspan="2">使用情况</td>
            <td><input type="checkbox" ng-checked="vm.item.useSituation!=vm.item.newUseSituation&&vm.item.newUseSituation.length!=0" /></td>
            <td ng-bind="vm.printData.useSituation"></td>
            <td ng-bind="vm.printData.newUseSituation"></td>
        </tr>
        <tr>
            <td colspan="2">用途</td>
            <td><input type="checkbox" ng-checked="vm.item.useWay!=vm.item.newUseWay&&vm.item.newUseWay.length!=0" /></td>
            <td ng-bind="vm.printData.useWay"></td>
            <td ng-bind="vm.printData.newUseWay"></td>
        </tr>
        <tr>
            <td colspan="2">使用类别</td>
            <td><input type="checkbox" ng-checked="vm.item.useType!=vm.item.newUseType&&vm.item.newUseType.length!=0" /></td>
            <td ng-bind="vm.printData.useType"></td>
            <td ng-bind="vm.printData.newUseType"></td>
        </tr>
        <tr>
            <td colspan="2">联网类型</td>
            <td><input type="checkbox" ng-checked="vm.item.network!=vm.item.newNetwork&&vm.item.newNetwork.length!=0" /></td>
            <td ng-bind="vm.printData.network"></td>
            <td ng-bind="vm.printData.newNetwork"></td>
        </tr>
        <tr>
            <td colspan="2">放置地点</td>
            <td><input type="checkbox" ng-checked="vm.item.room!=vm.item.newRoom&&vm.item.newRoom.length!=0" /></td>
            <td ng-bind="vm.printData.room"></td>
            <td ng-bind="vm.printData.newRoom"></td>
        </tr>
        <tr>
            <td colspan="2">光驱状态</td>
            <td><input type="checkbox" ng-checked="vm.item.hardDisk!=vm.item.newHardDisk&&vm.item.newHardDisk.length!=0" /></td>
            <td ng-bind="vm.printData.hardDisk"></td>
            <td ng-bind="vm.printData.newHardDisk"></td>
        </tr>
        <tr>
            <td colspan="2">USB状态</td>
            <td><input type="checkbox" ng-checked="vm.item.usersLogin!=vm.item.newUsersLogin&&vm.item.newUsersLogin.length!=0" /></td>
            <td ng-bind="vm.printData.usb"></td>
            <td ng-bind="vm.printData.newUsb"></td>
        </tr>
        <tr>
            <td rowspan="16" class="prevtitle">审批信息</td>
            <td style="height:70px" colspan="2">申请原因</td>
            <td colspan="3" ng-bind="vm.printData.reason"></td>
        </tr>
        <tr>
            <td rowspan="2" colspan="2">责任人确认</td>
            <td colspan="3" class="bottomline"  ng-bind="vm.printData.beforeDutyMan.comment"></td>
        </tr>
        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.beforeDutyMan.userName"></span>
                <span ng-if="vm.printData.beforeDutyMan.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.beforeDutyMan.userLogin}}"></span></td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.beforeDutyMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="3" colspan="2">变更后责任人确认</td>
            <td colspan="3" class="bottomline" style="height:20px;text-align:right;">（涉及设备责任人变更需填此项）</td>
        </tr>
        <tr>
            <td colspan="3" class="topline bottomline" ng-bind="vm.printData.endDutyMan.comment"></td>
        </tr>

        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.endDutyMan.userName"></span>
                <span ng-if="vm.printData.endDutyMan.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.endDutyMan.userLogin}}"></span></td>
            </td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.endDutyMan.end|date:'yyyy-MM-dd'">日期：</td>
        </tr>
        <tr>
            <td rowspan="2" colspan="2">设备所属单位领导意见</td>
            <td colspan="3" class="bottomline" ng-bind="vm.printData.deviceChargeMen.comment"></td>
        </tr>
        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.deviceChargeMen.userName"></span>
                <span ng-if="vm.printData.deviceChargeMen.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deviceChargeMen.userLogin}}"></span></td>
            </td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.deviceChargeMen.end|date:'yyyy-MM-dd'">日期：</td>
        </tr>
        <tr>
            <td rowspan="3" colspan="2">行政事务部意见</td>
            <td colspan="3" class="bottomline" style="height:20px;text-align:right;">（涉及设备责任人变更需填此项）</td>
        </tr>
        <tr>
            <td colspan="3" class="topline bottomline" ng-bind="vm.printData.administrative.comment"></td>
        </tr>

        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.administrative.userName"></span>
                <span ng-if="vm.printData.administrative.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.administrative.userLogin}}"></span></td>
            </td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.administrative.end|date:'yyyy-MM-dd'">日期：</td>
        </tr>
        <tr>
            <td rowspan="2" colspan="2">信息化建设于管理部意见</td>
            <td colspan="3" class="bottomline" ng-bind="vm.printData.informationEquipmentMen.comment"></td>
        </tr>
        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.informationEquipmentMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentMen.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentMen.userLogin}}"></span></td>
            </td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.informationEquipmentMen.end|date:'yyyy-MM-dd'">日期：</td>
        </tr>
        <tr>
            <td rowspan="2" colspan="2">网络运维中心服务工程师</td>
            <td colspan="3" class="bottomline" ng-bind="vm.printData.computerLeader.comment"></td>
        </tr>
        <tr>
            <td class="topline leftline rightline text_right">签字：</td>
            <td class="topline leftline rightline">
                <span ng-bind="vm.printData.computerLeader.userName"></span>
                <span ng-if="vm.printData.computerLeader.userName.length>0">
				<img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.computerLeader.userLogin}}"></span></td>
            </td>
            <td class="topline leftline" ng-bind="'日期：'+vm.printData.computerLeader.end|date:'yyyy-MM-dd'">日期：</td>
        </tr>
        <tr>
            <td style="height:50px" colspan="2">备注</td>
            <td colspan="3"></td>
        </tr>
    </table>
</div>