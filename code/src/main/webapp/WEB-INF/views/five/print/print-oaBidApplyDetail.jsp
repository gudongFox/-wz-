<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table1 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table1 td {
        border: solid #000 1px;
        height: 40px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
    .print_table2 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table2 td {
        border: solid #000 1px;
        height: 35px;
        text-align: left;
        padding-left:5px;
        padding-right:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">投标申请表</h3>
    <p style="float:right" ng-bind="'备案号：'+vm.item.recordNo"></p>
    <table class="print_table1">
        <tr>
            <td style="width:25%;">投标申请人</td>
            <td style="width:25%;" ng-bind="vm.item.bidManName"></td>
            <td style="width:25%;">日期</td>
            <td style="width:25%;" ng-bind="vm.item.bidTime"></td>
        </tr>
        <tr>
            <td>项目名称</td>
            <td ng-bind="vm.item.projectName"></td>
            <td>工程类型</td>
            <td ng-bind="vm.item.projectType"></td>
        </tr>
        <tr>
            <td>项目所属行业</td>
            <td ng-bind="vm.item.projectIndustry"></td>
            <td>招标方名称</td>
            <td ng-bind="vm.item.bidder"></td>
        </tr>
        <tr>
            <td>信息来源</td>
            <td ng-bind="vm.item.informationSource"></td>
            <td>预计合同额</td>
            <td ng-bind="vm.item.contractMoney"></td>
        </tr>
        <tr>
            <td>联系人</td>
            <td ng-bind="vm.item.bidderLinkMan"></td>
            <td>联系电话</td>
            <td ng-bind="vm.item.bidderLinkTel"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:30px;text-align:left;padding-left:5px;border-bottom:none;"><b>项目工程信息</b></td>
        </tr>
    </table>
    <table class="print_table2">
        <tr>
            <td style="width:25%;">◆建设地点:</td>
            <td colspan="3" ng-bind="vm.item.projectAddress"></td>
        </tr>
        <tr>
            <td>◆建设规模:</td>
            <td colspan="3" ng-bind="vm.item.projectScale"></td>
        </tr>
        <tr>
            <td>◆资金来源:</td>
            <td colspan="2" ng-bind="vm.item.moneySource"></td>
            <td style="width:25%;">其他：<span ng-bind="vm.item.moneySourceOther"></span></td>
        </tr>
        <tr>
            <td rowspan="2">◆合格条件与资格要求:</td>
            <td colspan="3" ng-bind="vm.item.qualification"></td>
        </tr>
        <tr>
            <td colspan="3">其他：<span ng-bind="vm.item.qualificationOther"></span></td>
        </tr>
        <tr>
            <td rowspan="2">◆投标方式:</td>
            <td colspan="3" ng-bind="vm.item.bidType"></td>
        </tr>
        <tr>
            <td colspan="3">其他：<span ng-bind="vm.item.bidTypeOther"></span></td>
        </tr>
        <tr>
            <td>◆招标范围:</td>
            <td colspan="3" ng-bind="vm.item.bidScope"></td>
        </tr>
        <tr>
            <td>◆合同方式:</td>
            <td colspan="2" ng-bind="vm.item.contractType"></td>
            <td style="width:25%;">其他：<span ng-bind="vm.item.contractTypeOther"></span></td>
        </tr>
        <tr>
            <td>◆工期及进度要求:</td>
            <td colspan="3"><p ng-bind="vm.item.projectTime"></p>&emsp; &emsp; <p ng-bind="vm.item.scheduleTarget"></p></td>
        </tr>
        <tr>
            <td style="border-bottom:none;">◆招标文件附件:</td>
            <td style="border-bottom:none;" colspan="3">
                <div  ng-repeat="file in vm.printData.fileList">
                    <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>
        </tr>
    </table>
    <table class="print_table2">
        <tr>
            <td style="width:25%;">◆投标保证金金额: (元):</td>
            <td style="width:15%;" ng-bind="vm.item.depositMoney"></td>
            <td style="width:14%;">保证金期限(天)：</td>
            <td style="width:13%;" ng-bind="vm.item.depositTime"></td>
            <td style="width:18%;">◆招标文件及资料费: (元):</td>
            <td style="width:15%;" ng-bind="vm.item.fileDataCost"></td>
        </tr>
        <tr>
            <td style="height:60px;">◆工程款支付方式、节点与支付条件:</td>
            <td colspan="5" ng-bind="vm.item.paymentRule"></td>
        </tr>
        <tr>
            <td style="height:60px;">◆与招标方合作经历:</td>
            <td colspan="5" ng-bind="vm.item.cooperationExperience"></td>
        </tr>
        <tr>
            <td style="height:60px;">◆潜在竞争对手:</td>
            <td colspan="5" ng-bind="vm.item.opponent"></td>
        </tr>
    </table>

<div  style="page-break-before:always" >
    <h3 style="text-align: center;">投标申请表</h3>
    <p style="float:right" ng-bind="'备案号：'+vm.item.recordNo"></p>
    <table class="print_table2">
        <tr>
            <td colspan="4" style="height:30px;text-align:left;padding-left:5px;"><b>投标可行性评价: (技术、经济可行性及其他)</b></td>
        </tr>
        <tr>
            <td style="width:25%;">◆技术可行性:</td>
            <td colspan="2" ng-bind="vm.item.technologyFeasibility"></td>
            <td style="width:25%;">其他：<span ng-bind="vm.item.technologyFeasibilityOther"></span></td>
        </tr>
        <tr>
            <td style="width:25%;">◆生成能力可行性:</td>
            <td colspan="2" ng-bind="vm.item.productFeasibility"></td>
            <td style="width:25%;">其他：<span ng-bind="vm.item.productFeasibilityOther"></span></td>
        </tr>
        <tr>
            <td style="height:60px;">◆其他可行性因素:</td>
            <td colspan="3" ng-bind="vm.item.otherFeasibility"></td>
        </tr>
        <tr>
            <td style="height:60px;">◆合同条款风险:</td>
            <td colspan="3" ng-bind="vm.item.contractRisk"></td>
        </tr>
        <tr>
            <td colspan="4" style="border-bottom:none;"><b>审核意见</b></td>
        </tr>
    </table>
    <table class="print_table2">
    <tr>
        <td colspan="4" style="width:50%;">申请单位领导：</td>
        <td colspan="4" >经验发展部领导：</td>
    </tr>
    <tr>
        <td colspan="4" style="height:60px;vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        <td colspan="4" style="vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.manageDevelopMen.comment"></td>
    </tr>
    <tr>
        <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
        <td style="width:19%;border-right:none;border-left:none;border-top:none;">
            <span ng-bind="vm.printData.deptChargeMen.userName"></span>
            <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
        </td>
        <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
        <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
        <td style="width:19%;border-right:none;border-left:none;border-top:none;">
            <span ng-bind="vm.printData.manageDevelopMen.userName"></span>
            <span ng-if="vm.printData.manageDevelopMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.manageDevelopMen.userLogin}}"></span>
        </td>
        <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
        <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.manageDevelopMen.end|date:'yyyy-MM-dd'"></td>
    </tr>
    <tr>
        <td colspan="8" >主管公司领导审批意见：</td>
    </tr>
    <tr>
        <td colspan="8" style="height:60px;vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.companyLeader.comment"></td>
    </tr>
    <tr>
        <td colspan="2" style="border-right:none;border-top:none;text-align: right;">签字：</td>
        <td colspan="2" style="border-right:none;border-left:none;border-top:none;">
            <span ng-bind="vm.printData.companyLeader.userName"></span>
            <span ng-if="vm.printData.companyLeader.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyLeader.userLogin}}"></span>
        </td>
        <td colspan="2" style="border-right:none;border-left:none;border-top:none;text-align: right;">日期：</td>
        <td colspan="2" style="border-left:none;border-top:none;" ng-bind="vm.printData.companyLeader.end|date:'yyyy-MM-dd'"></td>
    </tr>
    <tr>
        <td colspan="2" style="height:50px;text-align: right;">是否中标:</td>
        <td colspan="6" ng-bind="vm.item.win"></td>
    </tr>
    <tr>
        <td colspan="2" style="height:50px;text-align: right;">未中标原因:</td>
        <td colspan="6" ng-bind="vm.item.failReason"></td>
    </tr>
</table>
</div>
</div>