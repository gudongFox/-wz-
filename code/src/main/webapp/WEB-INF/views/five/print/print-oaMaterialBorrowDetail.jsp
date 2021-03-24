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
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">档案资料借阅/电子复制申请表</h3>
    <P style="float: right;padding-right: 15px;"></P>
    <table class="print_table">
        <tr>
            <td style="width:10%">申请人:</td>
            <td style="width:10%" ng-bind="vm.printData.applicantName"></td>
            <td style="width:10%">职工号:</td>
            <td style="width:10%" ng-bind="vm.printData.applicantNo"></td>
            <td style="width:10%">所在单位</td>
            <td style="width:10%" ng-bind="vm.printData.deptName"></td>
            <td style="width:10%">所属专业</td>
            <td style="width:10%" ng-bind="vm.printData.applicantMajor"></td>
            <td style="width:10%">电话</td>
            <td style="width:10%" ng-bind="vm.printData.applicantTel"></td>
        </tr>
        <tr>
            <td style="text-align:right;border-right:none">借出</td>
            <td style="border-right:none;border-left:none;" ng-bind="vm.printData.borrow"></td>
            <td style="text-align:right;border-right:none;border-left:none;">查阅</td>
            <td style="border-right:none;border-left:none;" ng-bind="vm.printData.consult"></td>
            <td style="text-align:right;border-right:none;border-left:none;">复制</td>
            <td style="border-right:none;border-left:none;" ng-bind="vm.printData.copy"></td>
            <td style="text-align:right;border-right:none;border-left:none;">共计</td>
            <td style="border-right:none;border-left:none;" ng-bind="vm.printData.count"></td>
            <td style="border-right:none;border-left:none;">件</td>
            <td style="border-left:none;"></td>
        </tr>
        </table>
        <table class="print_table">
            <tr>
                <td>序号</td>
                <td>文件资料名称</td>
                <td>档号</td>
                <td>档案类型</td>
                <td>专业</td>
                <td>归档单位</td>
                <td>密级</td>
                <td>底稿(硫酸纸)</td>
                <td>蓝图</td>
                <td>文书</td>
                <td>DWG</td>
                <td>PDF</td>
                <td>件数</td>
                <td>备注</td>
            </tr>
            <tr ng-repeat="printDetail in vm.printDetails">
                <td ng-bind="$index+1"></td>
                <td ng-bind="printDetail.fileName"></td>
                <td ng-bind="printDetail.fileNo"></td>
                <td ng-bind="printDetail.fileType"></td>
                <td ng-bind="printDetail.major"></td>
                <td ng-bind="printDetail.deptName"></td>
                <td>{{printDetail.fileLevel?'√':''}}</td>
                <td>{{printDetail.draft?'√':''}}</td>
                <td >{{printDetail.blueprint?'√':''}}</td>
                <td >{{printDetail.word?'√':''}}</td>
                <td >{{printDetail.dwg?'√':''}}</td>
                <td >{{printDetail.pdf?'√':''}}</td>
                <td ng-bind="printDetail.count"></td>
                <td ng-bind="printDetail.remark"></td>
            </tr>
        </table>
        <table class="print_table">
            <tr>
                <td colspan="14" style="height:35px;text-align:left;border-top:none;background: #dcdcdc;">借用审批</td>
            </tr>
            <tr>
                <td colspan="7" style="height:35px;text-align:left;">申请单位负责人审批:</td>
                <td colspan="7" style="height:35px;text-align:left;">借用人接收:</td>
            </tr>
            <tr>
                <td colspan="7" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.deptChargeMen.comment"></td>
                <td colspan="7" style="text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.startMan.comment"></td>
            </tr>
            <tr>
                <td style="width:8%">签字：</td>
                <td colspan="2" style="width:14%">
                    <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
                    <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                </td>
                <td colspan="2" style="width:14%">日期：</td>
                <td colspan="2" style="width:14%" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
                <td style="width:8%">签字:</td>
                <td colspan="2" style="width:14%">
                    <span ng-if="vm.printData.startMan.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.startMan.userLogin}}"></span>
                    <span ng-bind="vm.printData.startMan.userName"></span>
                </td>
                <td colspan="2" style="width:14%">日期：</td>
                <td colspan="2" style="width:14%" ng-bind="vm.printData.startMan.end|date:'yyyy-MM-dd'"></td>
            </tr>
            <tr>
                <td colspan="7" style="height:35px;text-align:left;">档案管理人员移交:</td>
                <td colspan="7" rowspan="3">
                </td>
            </tr>
            <tr>
                <td colspan="7" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px">
                    <p ng-if="vm.printData.blueprint.userName.length>0" ng-bind="vm.printData.blueprint.comment+'-'+vm.printData.blueprint.userName"></p>
                    <p ng-if="vm.printData.baseMap.userName.length>0" ng-bind="vm.printData.baseMap.comment+'-'+vm.printData.baseMap.userName"></p>
                    <p ng-if="vm.printData.officeCopy.userName.length>0" ng-bind="vm.printData.officeCopy.comment+'-'+vm.printData.officeCopy.userName"></p>
                    <p ng-if="vm.printData.electronicRecord.userName.length>0" ng-bind="vm.printData.electronicRecord.comment+'-'+vm.printData.electronicRecord.userName"></p>
                </td>
            </tr>
            <tr>
                <td style="width:8%">签字：</td>
                <td colspan="4">
                    <span ng-if="vm.printData.blueprint.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.blueprint.userLogin}}"></span>
                    <span ng-if="vm.printData.baseMap.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.baseMap.userLogin}}"></span>
                    <span ng-if="vm.printData.officeCopy.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.officeCopy.userLogin}}"></span>
                    <span ng-if="vm.printData.electronicRecord.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.electronicRecord.userLogin}}"></span>
                </td>
                <td style="width:7%">日期：</td>
                <td style="width:7%" ng-bind="vm.printData.startMan.end|date:'yyyy-MM-dd'"></td>
            </tr>
            <tr>
                <td colspan="14" style="height:35px;text-align:left;border-top:none;background: #dcdcdc;">归还审批</td>
            </tr>
            <tr>
                <td colspan="7" style="height:35px;text-align:left;">借用人归还:</td>
                <td colspan="7" style="height:35px;text-align:left;">档案管理人员接收:</td>
            </tr>
            <tr>
                <td colspan="7" style="text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.returnMan.comment"></td>
                <td colspan="7" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px">
                    <p ng-if="vm.printData.returnBlueprint.userName.length>0" ng-bind="vm.printData.returnBlueprint.comment+'-'+vm.printData.returnBlueprint.userName"></p>
                    <p ng-if="vm.printData.returnBaseMap.userName.length>0" ng-bind="vm.printData.returnBaseMap.comment+'-'+vm.printData.returnBaseMap.userName"></p>
                    <p ng-if="vm.printData.returnOfficeCopy.userName.length>0" ng-bind="vm.printData.returnOfficeCopy.comment+'-'+vm.printData.returnOfficeCopy.userName"></p>
                    <p ng-if="vm.printData.returnElectronicRecord.userName.length>0" ng-bind="vm.printData.returnElectronicRecord.comment+'-'+vm.printData.returnElectronicRecord.userName"></p>
                </td>

            </tr>
            <tr>
                <td>签字：</td>
                <td colspan="2">
                    <span ng-if="vm.printData.returnMan.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.returnMan.userLogin}}"></span>
                    <span ng-bind="vm.printData.returnMan.userName"></span>
                </td>
                <td colspan="2">日期：</td>
                <td colspan="2" ng-bind="vm.printData.returnMan.end|date:'yyyy-MM-dd'"></td>
                <td style="width:8%">签字：</td>
                <td colspan="4">
                    <span ng-if="vm.printData.returnBlueprint.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.returnBlueprint.userLogin}}"></span>
                    <span ng-if="vm.printData.returnBaseMap.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.returnBaseMap.userLogin}}"></span>
                    <span ng-if="vm.printData.returnOfficeCopy.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.returnOfficeCopy.userLogin}}"></span>
                    <span ng-if="vm.printData.returnElectronicRecord.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.returnElectronicRecord.userLogin}}"></span>
                </td>
                <td style="width:7%">日期：</td>
                <td style="width:7%" ng-bind="vm.printData.returnMan.end|date:'yyyy-MM-dd'"></td>
            </tr>
        </table>
</div>

