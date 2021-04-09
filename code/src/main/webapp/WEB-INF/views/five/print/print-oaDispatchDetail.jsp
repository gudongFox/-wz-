<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    #print_area{
        display: none;
    }
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
        color:red;
    }
    .print_table td{
        border: solid red 1px;
        height:80px;
        text-align: center;
        color:red;
        border-left: none;
        border-right: none;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area">
    <h3 style="text-align: center;color:red;">中国五洲工程设计集团有限公司发文稿纸</h3>
    <h4></h4>
    <table class="print_table">
        <tr>
            <td rowspan="3" style="width:15%; height:80px;border-left: none;">签发</td>
            <td style="color: black;text-align: left" rowspan="3" style="width:35%;">
                <p ng-bind="vm.printData.signer.comment"></p>
                <span ng-bind="vm.printData.signer.userName"></span>
                <span ng-if="vm.printData.signer.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.signer.userLogin}}"></span>

                <span ng-bind="vm.printData.signer.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td rowspan="2" style="width:15%;height:60px;border-left: solid 1px;">会签</td>
            <td style="color: black;width:35%;text-align: left;" rowspan="2" colspan="2">
<%--                <p ng-bind="vm.printData.counterSigner.comment"></p>
                <p ng-if="vm.printData.counterSigner.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.counterSigner.userLogin}}"></p>
                <span ng-bind="vm.printData.counterSigner.userName"></span>
                <span ng-bind="vm.printData.counterSigner.end|date:'yyyy-MM-dd'"></span>--%>

                <div ng-repeat="node in vm.printData.counterSigners" ng-if="node.userName!=null">
                    <p ng-bind="node.comment"></p>
                    <span ng-bind="node.userName"></span>
                    <img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+node.userLogin}}">
                    <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
                </div>
            </td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td rowspan="2" style="height:40px;border-left: solid 1px;"> 秘密等级</br ><span ng-if="vm.printData.secretGrade.indexOf('普通')==-1">及期限:</span></td>
            <td style="color: black" rowspan="2">
                <span ng-bind="vm.printData.secretGrade"></span> <br>
            </td>
            <td style="color: black" rowspan="2">
                <span ng-if="vm.printData.secretGrade.indexOf('普通')==-1" ng-bind="vm.printData.allottedTime"></span>
            </td>
        </tr>

        <tr>
            <td rowspan="3" style="height:80px;border-left: none;">公司办核稿</td>
            <td style="color: black;text-align: left" rowspan="3">
                <p ng-bind="vm.printData.audit.comment"></p>
                <span ng-bind="vm.printData.audit.userName"></span>
                <span ng-if="vm.printData.audit.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.audit.userLogin}}"></span>
                <span ng-bind="vm.printData.audit.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
        <tr>
            <td rowspan="2" style="height:60px;border-left: solid 1px;"> 公司保密委</br>员会签字: </td>
            <td style="color: black" rowspan="2" colspan="2" >
               <span ng-if="vm.printData.companySecurity.length>0">
                   <img ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companySecurity}}">
               </span>
                <span ng-bind="vm.printData.companySecurityName"></span>
           </td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td style="border-left: none;">核稿</td>
            <td style="color: black;text-align: left">
                <span ng-bind="vm.printData.audit.userName"></span>
                <span ng-if="vm.printData.audit.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.audit.userLogin}}"></span>

                <span ng-bind="vm.printData.audit.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="border-left: solid 1px;">主办单位</br>和拟稿人:</td>
            <td style="color: black;text-align: left;width: 10%" ng-bind="vm.printData.deptName"></td>
            <td style="color: black;text-align: left;width: 25%">
                <p ng-bind="vm.printData.creatorName"></p>
                <span ng-if="vm.printData.creator.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.creator}}"></span>
                <span ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width: 15%;height:60px;border-top: none;">发文:</td>
            <td colspan="3" style="border-top: none;color: black" ng-bind="vm.printData.dispatch"></td>
        </tr>
        <tr>
            <td>标题:</td>
            <td style="color: black" ng-bind="vm.printData.dispatchTitle"></td>
        </tr>
        <tr>
            <td>主题词:</td>
            <td style="color: black" style="border-bottom: none;" ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td style="height: 40px;border-bottom: none;border-top: none;">主送:</td>
            <td colspan="3" style="height: 40px;color: black;border-bottom: none;" ng-bind="vm.printData.realSendManName"></td>
        </tr>
        <tr>
            <td style="height: 40px;border-top: none;">抄送:</td>
            <td colspan="3" style="height: 40px;color: black;border-top: none;" ng-bind="vm.printData.copySendManName"></td>
        </tr>
        <tr>
            <td>附件:</td>
            <td style="color: black">
                <div  ng-repeat="file in vm.printData.fileList">
                    <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>
        </tr>
    </table>

</div>