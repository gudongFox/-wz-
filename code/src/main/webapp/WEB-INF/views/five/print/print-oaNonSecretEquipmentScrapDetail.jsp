<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>



<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 40px;
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">计算机及信息化设备报废申请</h3>
        <table class="print_table">
        <tr>
            <td style="width:10%">设备所属单位</td>
            <td style="width:40%" ng-bind="vm.printData.deptName"></td>
            <td style="width:10%">申请人</td>
            <td style="width:40%" ng-bind="vm.printData.applyManName"></td>
        </tr>
        <tr>
            <td style="width:10%">设备名称</td>
            <td style="width:40%" ng-bind="vm.printData.equipmentName"></td>

            <td style="width:10%">设备编号</td>
            <td style="width:40%" ng-bind="vm.printData.equipmentNo"></td>
        </tr>
        <tr>
            <td style="width:10%">设备序列号</td>
            <td style="width:40%" ng-bind="vm.printData.equipmentSerial"></td>

            <td style="width:8%">硬盘序列号</td>
            <td style="width:40%" ng-bind="vm.printData.hardNo"></td>
        </tr>
        </table>

        <table class="print_table">
            <tr>
                <td style="width:10%;border-bottom:none;border-top:none;">资产编号</td>
                <td style="width:90%;border-bottom:none;border-top:none;" ng-bind="vm.printData.assetsNo"></td>
            </tr>
            <tr style="height: 90px">
                <td style="width:10%;" rowspan="2">报废原因</td>
                <td style="width:90%" rowspan="2"  ng-bind="vm.printData.scrapReason"></td>
            </tr>

        </table >

        <table class="print_table">
            <tr>
                <td style="width:10%;border-top:none;" rowspan="3" >安全处理记录</td>
                <td style="width:10%;border-bottom:none;border-top:none;"  >
                    <input type="checkbox" ng-checked="vm.printData.secretHard=='1'"  />
                    <span>硬盘</span>
                </td>
                <td style="width:10%;border-bottom:none;border-top:none;">硬盘序列号</td>
                <td style="width:70%;border-bottom:none;border-top:none;" ng-bind="vm.printData.secretHardNo"></td>
            </tr>
        <tr>
            <td style="width:10%"  >
                <input type="checkbox" ng-checked="vm.printData.secretMemory=='1'"  />
                <span>内存</span>
            </td>
            <td style="width:10%">内存列号</td>
            <td style="width:70%" ng-bind="vm.printData.secretMemoryNo"></td>
        </tr>
        <tr>
            <td style="width:10%"  >
                <input type="checkbox" ng-checked="vm.printData.secretOther=='1'"  />
                <span>其他</span>
            </td>
            <td style="width:10%">其他序列号</td>
            <td style="width:70%" ng-bind="vm.printData.secretOtherNo"></td>
        </tr>
        </table>
        <table class="print_table" ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
        <tr>
            <td  rowspan="2"  style="width: 10%;border-top: none;height: 35px;" ng-bind="node.activityName"></td>
            <td  colspan="4" style="width: 40%;border-top: none;height: 35px;" style="vertical-align:top;padding-right: 5px;padding-left:5px;padding-top: 5px;border-top:none;">
                <span ng-bind="node.comment" style="margin-right: 10px;"></span>
            </td>

            <td rowspan="2" style="width: 10%;border-top: none" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td colspan="4"  style="width: 40%;border-top: none;height: 35px;" style="vertical-align:top;padding-right: 5px;padding-left:5px;padding-top: 5px;border-top:none;" >
                <span ng-bind="vm.printData.nodes[$index+1].comment" style="margin-right: 10px;"></span>
            </td>
        </tr>
        <tr>
            <td style="width:6%">签字:</td>
            <td style="width:15%">
                <span ng-if="node.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+node.userLogin}}"></span>
                <span ng-bind="node.userName"></span>
            </td>
            <td style="width:6%">日期:</td>
            <td style="width:15%" ng-bind="node.end|date:'yyyy-MM-dd'"></td>

            <td style="width:6%">签字:</td>
            <td style="width:15%">
                <span ng-if="vm.printData.nodes[$index+1].userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.nodes[$index+1].userLogin}}"></span>
                <span ng-bind="vm.printData.nodes[$index+1].userName"></span>
            </td>
            <td style="width:6%">日期:</td>
            <td style="width:15%" ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>

</div>