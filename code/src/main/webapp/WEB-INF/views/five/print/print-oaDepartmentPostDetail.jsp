<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
   
    .print_table table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
        color:red;
    }

    
    .print_table td {
        border: solid red 1px;
        height:80px;
        text-align: center;
        color:red;
        border-left: none;
        border-right: none;
        border-bottom: none;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;color:red;">部门发文稿纸</h3>
    <table class="print_table">
        <tr>
            <td style="width:13%;">核稿:</td>
            <td style="width:37%;color: black;">
                <p ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></p>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width:13%;border-left: solid 1px;">主办单位</br>和拟稿人:</td>
            <td style="width:17%;color: black;" ng-bind="vm.printData.deptName"></td>
            <td style="width:20%;color: black;">
                <p ng-if="vm.printData.drafter.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.drafter}}"></p>
                <span ng-bind="vm.printData.drafterName"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
        <tr>
            <td>发文:</td>
            <td colspan="4" style="color: black;" ng-bind="vm.printData.dispatch"></td>
        </tr>
        <tr>
            <td>标题:</td>
            <td colspan="4" style="color: black;" ng-bind="vm.printData.title"></td>
        </tr>
        <tr>
            <td style="height: 40px">主送:</td>
            <td colspan="4" style="height: 40px;color: black;" ng-bind="vm.printData.realSendManName"></td>
        </tr>
        <tr>
            <td style="height: 40px;border-top: none;">抄送:</td>
            <td colspan="4" style="height: 40px;border-top: none;color: black;" ng-bind="vm.printData.copySendManName"></td>
        </tr>
        <tr>
            <td>主题词:</td>
            <td colspan="4" style="color: black;" ng-bind="vm.printData.subjectTerms"></td>
        </tr>
        <tr>
            <td style="border-bottom:solid red 1px;">附件:</td>
            <td style="border-bottom:solid red 1px;" colspan="4">
                <div  ng-repeat="file in vm.printData.fileList">
                <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>
        </tr>
    </table>
</div>