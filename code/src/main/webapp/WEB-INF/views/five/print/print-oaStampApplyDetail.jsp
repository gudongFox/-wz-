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
    <h3 style="text-align: center;">用印审批</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">用印事由</td>
            <td colspan="3" ng-bind="vm.printData.remark"></td>
        </tr>
        <tr>
            <td style="width:15%;">文件/图纸名称</td>
            <td colspan="3" ng-bind="vm.printData.fileName"></td>
        </tr>
        <tr>
            <td style="height:100px;width:15%;">印章类型</td>
            <td style="width:40%;" ng-bind="vm.printData.stampName"></td>
            <td style="width:15%;">是否需要法律审核</td>
            <td style="width:30%;" ng-bind="vm.printData.legalReview"></td>
        </tr>
         <tr ng-if="vm.printData.stampName.indexOf('合同章')>-1">
            <td>评审级别</td>
            <td ng-bind="vm.printData.companyLevel"></td>
            <td>副总审批</td>
            <td ng-bind="vm.printData.viceLeaderName"></td>
        </tr>
          <tr ng-if="vm.printData.companyLevel.indexOf('公司级')>-1&&vm.printData.stampName.indexOf('合同章')>-1">
            <td>合同评审人员</td>
            <td colspan="3" ng-bind="vm.printData.contractSealManName"></td>
          </tr>

          <tr ng-if="vm.printData.stampName.indexOf('公司章')>-1||vm.printData.stampName.indexOf('法人')>-1">
            <td style="height: 100px;">归口管理部门</td>
            <td colspan="3" ng-bind="vm.printData.functionDeptName"></td>
          </tr>
         <tr ng-if="vm.printData.stampName.indexOf('压力')>-1">
            <td>压力管道技术员</td>
            <td ng-bind="vm.printData.qualityCompanyManName"></td>
            <td>评审方式</td>
            <td ng-bind="vm.printData.online"></td>
        </tr>
    </table>
</div>