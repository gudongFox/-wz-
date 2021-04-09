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

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">外部专家申请表</h3>
    <p style="text-align:right;padding-right:25px;" ng-bind="'日期:'+vm.printData.submitTime"></p>
    <table class="print_table">
        <tr>
            <td style="width:25%;">姓名</td>
            <td style="width:25%;" ng-bind="vm.printData.name"></td>
            <td style="width:25%;">身份证号</td>
            <td style="width:25%;" ng-bind="vm.printData.identityCardNo"></td>
        </tr>
        <tr>
            <td>送审单位（部门）</td>
            <td ng-bind="vm.printData.deptName"></td>
            <td>地址（房间号）</td>
            <td ng-bind="vm.printData.address"></td>
        </tr>
        <tr>
            <td rowspan="2" >职务</td>
            <td rowspan="2" ng-bind="vm.printData.position"></td>
            <td rowspan="2">职称及证书编号</td>
            <td style="height: 30px;border-bottom: none;" ng-bind="vm.printData.rank"></td>
        </tr>
        <tr>
            <td style="height: 30px;border-top: none;" ng-bind="vm.printData.rankCode"></td>
        </tr>
        <tr>
            <td>政治面貌</td>
            <td ng-bind="vm.printData.politicsStatus"></td>
            <td>称号</td>
            <td ng-bind="vm.printData.label"></td>
        </tr>
        <tr>
            <td>毕业院校</td>
            <td ng-bind="vm.printData.graduateCollege"></td>
            <td>最高学历</td>
            <td ng-bind="vm.printData.highestEducation"></td>
        </tr>
        <tr>
            <td>主要擅长</td>
            <td ng-bind="vm.printData.good"></td>
            <td>专业</td>
            <td ng-bind="vm.printData.major"></td>
        </tr>
        <tr>
            <td rowspan="2">手机/座机</td>
            <td style="height:30px;border-bottom: none;" ng-bind="vm.printData.phone"></td>
            <td rowspan="2">专家所属组</td>
            <td rowspan="2" ng-bind="vm.printData.specialistGroup"></td>
        </tr>
        <tr>
            <td style="height:30px;border-top: none;" ng-bind="vm.printData.telephone"></td>
        </tr>
        <tr>
            <td>申请人签字</td>
            <td>本人签字</td>
            <td>
                <span ng-bind="vm.printData.startMen.userName"></span>
                <span ng-if="vm.printData.startMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.startMen.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.startMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">单位意见</td>
            <td colspan="3" style="height:80px;text-align: left;vertical-align:top;padding-top:5px;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.deptChargeMen.comment""></td>
        </tr>
        <tr>
            <td>签字</td>
            <td>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">专家委意见</td>
            <td colspan="3" style="height:80px;text-align: left;vertical-align:top;padding-top:5px;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.specialistMen.comment"></td>
        </tr>
        <tr>
            <td>签字</td>
            <td ng-bind="vm.printData.specialistMen.end|date:'yyyy-MM-dd'">
                <span ng-bind="vm.printData.specialistMen.userName"></span>
                <span ng-if="vm.printData.specialistMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.specialistMen.userLogin}}"></span>
            </td>
            <td></td>
        </tr>
        <tr>
            <td rowspan="2">公司总工程师意见</td>
            <td colspan="3" style="height:80px;text-align: left;vertical-align:top;padding-top:5px;padding-left: 15px;padding-right: 15px" ng-bind="vm.printData.scienceLeader.comment"></td>
        </tr>
        <tr>
            <td>签字</td>
            <td>
                <span ng-bind="vm.printData.scienceLeader.userName"></span>
                <span ng-if="vm.printData.scienceLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.scienceLeader.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.scienceLeader.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>