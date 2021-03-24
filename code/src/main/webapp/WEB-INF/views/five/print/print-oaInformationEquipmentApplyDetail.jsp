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


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">信息化设备购置申请单</h3>
    <table class="print_table">
        <tr>
            <td style="width:12%">申请单位</td>
            <td >是否预算内</td>
            <td style="width:12%">申请日期</td>
            <td style="width:12%">联系人</td>
            <td style="width:12%">电话</td>
            <td>总价</td>
            <td >设备用途及购置理由</td>
        </tr>
        <tr>
            <td  ng-bind="vm.printData.deptName"></td>
            <td ng-bind="vm.printData.plan"></td>
            <td style="width:16%" ng-bind="vm.printData.applyTime"></td>
            <td style="width:16%" ng-bind="vm.printData.linkManName"></td>
            <td style="width:16%" ng-bind="vm.printData.linkManPhone"></td>
            <td  style="height:100px" ng-bind="vm.printData.totalMoney"></td>
            <td  style="height:100px" ng-bind="vm.printData.equipmentUse"></td>

        </tr>
        <%--<tr>
            <td >上传附件：</td>
            <td colspan="3">
                <div  ng-repeat="file in vm.printData.fileList">
                    <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>

        </tr>--%>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:12%;border-top: none;">设备名称</td>
            <td style="border-top: none;">设备所在单位</td>
            <td style="border-top: none;">品牌</td>
            <td style="border-top: none;">设备型号</td>
            <td style="border-top: none;">数量</td>
            <td style="border-top: none;">单价（元）</td>
            <td style="border-top: none;">小计(元)</td>
            <td style="border-top: none;">特殊需求</td>
            <td style="border-top: none;">备注</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="printDetail.equipmentName"></td>
            <td ng-bind="printDetail.deptName"></td>
            <td ng-bind="printDetail.brand"></td>
            <td ng-bind="printDetail.equipmentType"></td>
            <td ng-bind="printDetail.number"></td>
            <td ng-bind="printDetail.price"></td>
            <td ng-bind="printDetail.totalPrice"></td>
            <td ng-bind="printDetail.otherRequirement"></td>
            <td ng-bind="printDetail.remark"></td>
        </tr>
    </table>
    <table class="print_table" >
        <tr>
            <td colspan="8" style="height:35px;text-align:left;padding-left:15px;border-top:none;">单位负责人意见：</td>
        </tr>
        <tr>
            <td colspan="8" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.deptCharge.comment"></td>
        </tr>
        <tr>
            <td colspan="5" style="text-align: right;">签字：</td>
            <td>
                <span ng-bind="vm.printData.deptCharge.userName"></span>
                <span ng-if="vm.printData.deptCharge.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptCharge.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:13%;">日期：</td>
            <td ng-bind="vm.printData.deptCharge.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;">财务金融部审批意见:</td>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;">科技质里部审批意见:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.financeDept.comment"></td>
            <td colspan="4" style="text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.technologyDept.comment"></td>
        </tr>
        <tr>
            <td style="width:12%">签字：</td>
            <td style="width:13%">
                <span ng-bind="vm.printData.financeDept.userName"></span>
                <span ng-if="vm.printData.financeDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.financeDept.userLogin}}"></span>
            </td>
            <td style="width:12%">日期：</td>
            <td style="width:13%" ng-bind="vm.printData.financeDept.end|date:'yyyy-MM-dd'"></td>
            <td style="width:12%">签字</td>
            <td style="width:13%">
                <span ng-bind="vm.printData.technologyDept.userName"></span>
                <span ng-if="vm.printData.technologyDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.technologyDept.userLogin}}"></span>
            </td>
            <td style="width:12%">日期：</td>
            <td style="width:13%" ng-bind="vm.printData.technologyDept.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;">行政事务部审批意见:</td>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;">设备购置部门分管公司领导审批意见:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:100px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.affairs.comment"></td>
            <td colspan="4" style="text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.equipmentDept.comment"></td>
        </tr>
        <tr>
            <td>签字：</td>
            <td>
                <span ng-bind="vm.printData.affairs.userName"></span>
                <span ng-if="vm.printData.affairs.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.affairs.userLogin}}"></span>
            </td>
            <td>日期：</td>
            <td ng-bind="vm.printData.affairs.end|date:'yyyy-MM-dd'"></td>
            <td>签字</td>
            <td>
                <span ng-bind="vm.printData.equipmentDept.userName"></span>
                <span ng-if="vm.printData.equipmentDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.equipmentDept.userLogin}}"></span>
            </td>
            <td>日期：</td>
            <td ng-bind="vm.printData.equipmentDept.end|date:'yyyy-MM-dd'"></td>
        </tr>

    </table>
</div>

