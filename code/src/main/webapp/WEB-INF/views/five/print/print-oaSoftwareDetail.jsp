<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 35px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>
<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">软件购置、升级、服务申请单 </h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">申请单位：</td>
            <td colspan="2" ng-bind="vm.item.deptName"></td>
            <td style="width:15%;">申请类型：</td>
            <td style="width:18%;" ng-bind="vm.item.applyStyle"></td>
            <td style="width:15%;">申请日期：</td>
            <td style="width:18%;" ng-bind="vm.item.applyTime"></td>
        </tr>
        <tr>
            <td>软件公司名称：</td>
            <td colspan="4" ng-bind="vm.item.softwareCompanyName"></td>
            <td rowspan="2" style="width:15%;">软件授权类型</td>
            <td rowspan="2" style="width:15%;" ng-bind="vm.item.applyStyle"></td>
        </tr>
        <tr>
            <td>软件公司网站：</td>
            <td colspan="4" ng-bind="vm.item.softwareCompanyUrl"></td>
        </tr>
        <tr>
            <td>软件名称</td>
            <td colspan="4" ng-bind="vm.item.softwareName"></td>
            <td style="width:15%;">软件授权点数</td>
            <td style="width:15%;" ng-bind="vm.item.softwareLicenceCount"></td>
        </tr>
        <tr>
            <td>软件报价</td>
            <td ng-bind="vm.item.softwareOffer"></td>
            <td style="width:8%;">(万元)</td>
            <td>软件公司联系人</td>
            <td ng-bind="vm.item.softwareLink"></td>
            <td>电话</td>
            <td ng-bind="vm.item.softwarePhone"></td>
        </tr>
        <tr>
            <td>成交价格</td>
            <td ng-bind="vm.item.softwarePrice"></td>
            <td style="width:8%;">(万元)</td>
            <td>使用该软件的专业</td>
            <td colspan="3" ng-bind="vm.item.softwareUseMajor"></td>

        </tr>
        <tr>
            <td style="height:60px">软件购置途径及购置理由</td>
            <td colspan="6" style="text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.item.softwareUseWay"></td>
        </tr>
        <tr>
            <td colspan="3">软件使用单位名称</td>
            <td colspan="2">费用摊销比例(%)</td>
            <td colspan="2">备注</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td colspan="3" ng-bind="printDetail.softwareUserName"></td>
            <td colspan="2" ng-bind="printDetail.softwareCostRatio"></td>
            <td colspan="2" ng-bind="printDetail.remark"></td>
        </tr>
    </table>
        <table class="print_table">
        <tr>
            <td colspan="8" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">软件安装概要:</td>
        </tr>
        <tr>
            <td colspan="8" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.item.softwareInstall"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">网络运维中心审批意见:</td>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">财务资产部核准申请单位预算:</td>
        </tr>
            <tr>
                <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.computerDept.comment"></td>
                <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.financialDept.comment"></td>
            </tr>
        <tr>
            <td style="height:60px;text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.computerDept.userName"></P>
                <p ng-if="vm.printData.computerDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.computerDept.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px">日期:</td>
            <td ng-bind="vm.printData.computerDept.end|date:'yyyy-MM-dd'"></td>
            <td style="text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.financialDept.userName"></P>
                <p ng-if="vm.printData.financialDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.financialDept.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px">日期:</td>
            <td ng-bind="vm.printData.financialDept.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">信息化建设与管理部审批意见:</td>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">公司主管领导审批意见:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.scienceDept.comment"></td>
            <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.companyLeader.comment"></td>
        </tr>
        <tr>
            <td style="height:60px;text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.scienceDept.userName"></P>
                <p ng-if="vm.printData.scienceDept.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.scienceDept.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px">日期:</td>
            <td  ng-bind="vm.printData.scienceDept.end|date:'yyyy-MM-dd'"></td>
            <td style="text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.companyLeader.userName"></P>
                <p ng-if="vm.printData.companyLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyLeader.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px">日期:</td>
            <td ng-bind="vm.printData.companyLeader.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">公司总会计师审批意见:</td>
            <td colspan="4" style="height:35px;text-align: left;padding-left: 15px;border-top:none;">公司总经理审批意见:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.chiefAccountant.comment"></td>
            <td colspan="4" style="height:60px;text-align: left;vertical-align: top;padding-left: 15px;padding-right: 15px;padding-top: 15px;" ng-bind="vm.printData.chiefManager.comment"></td>
        </tr>
        <tr>
            <td style="height:60px;text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.chiefAccountant.userName"></P>
                <p ng-if="vm.printData.chiefAccountant.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefAccountant.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px">日期:</td>
            <td ng-bind="vm.printData.chiefAccountant.end|date:'yyyy-MM-dd'"></td>
            <td style="text-align: right;padding-left: 5px">签字:</td>
            <td>
                <P ng-bind="vm.printData.chiefManager.userName"></P>
                <p ng-if="vm.printData.chiefManager.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefManager.userLogin}}"></p>
            </td>
            <td style="text-align: right;padding-left: 5px" >日期:</td>
            <td ng-bind="vm.printData.chiefManager.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>