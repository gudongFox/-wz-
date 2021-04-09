<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 16px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 70px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }

</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h2 style="text-align: center;">职工离职通知单</h2>
    <p style="text-align: center;font-size: 18px;"><u ng-bind="vm.item.name"></u>同志（职工号：<span ng-bind="vm.item.login"></span>)自<u ng-bind="vm.item.entryTime|date:'yyyy年MM月dd日'"></u>由</p>
    <p style="text-align: center;font-size: 18px;"><u ng-bind="vm.item.deptName"></u>调入<u ng-bind="vm.item.entryDeptName"/>。</p>
    <p style="padding-left:70%;font-size: 18px;" ng-bind="vm.item.gmtCreate|date:'yyyy年MM月dd日'"></p>
    <table class="print_table">
        <tr>
            <td colspan="2" style="width:40%;">单位</td>
            <td style="width:20%;">经办人</td>
            <td style="width:20%;">单位</td>
            <td style="width:20%;">经办人</td>
        </tr>
        <tr>
            <td rowspan="3" style="width:15%;">党群工作部</td>
            <td>组织</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2704}}"></td>
            <td>科技管理部</td>
            <td></td>
        </tr>
        <tr>
            <td>团委</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2636}}"></td>
            <td>信息化建设与管理部</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2887}}"></td>
        </tr>
        <tr>
            <td>工会</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2560}}"></td>
            <td>经营发展部</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2169}}"></td>
        </tr>
        <tr>
            <td rowspan="5" style="width:15%;">行政事务部</td>
            <td>房产管理</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2684}}"></td>
            <td>财务金融部</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.financeDepartmentMen.userLogin}}"></td>
        </tr>
        <tr>
            <td>设备管理</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2411}}"></td>
            <td>保密办公室</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2786}}"></td>
        </tr>
        <tr>
            <td>宿舍管理</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+0784}}"></td>
            <td>网络运维中心</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+2589}}"></td>
        </tr>
        <tr>
            <td>食堂管理</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+0784}}"></td>
            <td>物业服务中心供应管理</td>
            <td></td>
        </tr>
        <tr>
            <td>保卫办</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+0784}}"></td>
            <td>原单位</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyPrincipal.userLogin}}"></td>
        </tr>
        <tr>
            <td rowspan="2">档案图书室</td>
            <td>档案资料</td>
            <td><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+0756}}"></td>
            <td rowspan="2" colspan="2" style="text-align:left;">注：此通知单请一周内办完，由本人交回人力资源部，并办理人事关系及档案转移手续</td>
        </tr>
        <tr>
            <td>文书档案</td>
            <td></td>
        </tr>
    </table>
</div>