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

    .print_table p {
        padding-left:50px;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">对外发表科技论文审查单</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">标题</td>
            <td style="width:35%;" ng-bind="vm.item.title"></td>
            <td style="width:15%;">作者单位</td>
            <td style="width:35%;" ng-bind="vm.item.deptName"></td>
        </tr>
        <tr>
            <td>作者</td>
            <td ng-bind="vm.item.authorName"></td>
            <td>作者联系方式</td>
            <td ng-bind="vm.item.authorLink"></td>
        </tr>
        <tr>
            <td>单位负责人</td>
            <td ng-bind="vm.item.deptChargeManName"></td>
            <td>技术质量与信息化负责人</td>
            <td ng-bind="vm.item.technologyChargeManName"></td>
        </tr>
        <tr>
            <td style="height:80px;">拟发表刊物或学术交流</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.item.periodical"></td>

        </tr>
        <tr>
            <td rowspan="2">部门/单位审查意见</td>
            <td colspan="3" style="height:60px;text-align:left;padding-left:15px;padding-left:15px">审查要点：<p>1.论文不涉及国际秘密和公司商业秘密;</p><p>2.论文无政治性错误;</p><p>3.论文无技术性、概念性错误，真实准确</p>同意发表</td>
        </tr>
        <tr>
            <td colspan="3" style="text-align: right;padding-right: 12%">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部审查意见</td>
            <td colspan="3" style="height:120px;text-align:left;vertical-align: top;padding-top: 15px;padding-left:15px;padding-left:15px" ng-bind="vm.item.technologyChargeManComment">

            </td>
        </tr>
        <tr>
            <td colspan="3" style="text-align: right;padding-right: 12%">
                <span ng-bind="vm.printData.informationEquipmentMen.userName"></span>
                <span ng-if="vm.printData.informationEquipmentMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationEquipmentMen.userLogin}}"></span>
                <span ng-bind="vm.printData.informationEquipmentMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

    </table>
</div>

