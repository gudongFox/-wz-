<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style1">
    table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }
    .print_table td {
        border: solid red 1px;
        height: 45px;
        text-align: left;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
    }
    #div1 {
        width: 800px;
        border-top: 2px solid red;
        text-align: center;
    }
    .pStyle{
        font-family:'仿宋_GB2312','仿宋','宋体',sans-serif;
        letter-spacing: 1px;//字间距
        line-height:100%;//行间距
    }
    .pStyle2{
        font-family:'宋体','仿宋',sans-serif;
        letter-spacing: 1px;//字间距
    line-height:100%;//行间距
    }

</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area1" hidden >
    <h1 style="text-align: center; color: red;font-size: 60px" class="pStyle2"><span ng-bind="vm.printData.meetingType">总经理办公会</span>纪要</h1>

    <table class="print_table">
        <tr style="border-bottom: 2px solid red; " class="pStyle">
            <td style="width:35%;border-bottom: 2px solid red;font-size: 20px;text-align: left" ng-bind="vm.printData.deptName2">五洲集团办公室</td>
            <td style="width:30%;border-bottom: 2px solid red;font-size: 20px ;text-align: center" ng-bind="'第'+vm.printData.stages+'次'"> </td>
            <td style="width:35%;border-bottom: 2px solid red;font-size: 20px;text-align: right" ng-bind="vm.printData.meetingTimePlan2">2020年7月27日</td>
        </tr>
        <tr>
            <td style="height: 40px" ></td>
            <td ></td>
            <td ></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:100%;padding-left:40px;font-size: 24px;">
                <p class="pStyle" style="text-indent: 2em;"><span ng-bind="vm.printData.meetingTimePlan3">7月27日</span>，公司领导<span ng-bind="vm.printData.compereName">湛峰</span>在<span ng-bind="vm.printData.meetingTime">201会议室</span>主持召开<span ng-bind="vm.printData.meetingType">总经理办公会</span>。
                    公司领导<span ng-bind="vm.printData.companyLeaderName2">刘志刚、湛峰、连文平、</span><span ng-bind="vm.printData.attenderName3">于万河、王建明、谢力平、孟云、刘颖、宁俊栋</span>,及议题相关负责人<span ng-bind="vm.printData.attenderName4">孙婧毅</span>等出席会议。</p>
            </td>
        </tr>
        <tr>
            <td  style="width:20%;padding-left:40px;vertical-align:top;padding-top:20px;padding-bottom:20px;font-size: 24px;text-indent: 2em;"><p class="pStyle">会议审议以下事项，现纪要如下：</p></td>
        </tr>
    </table>
    <%--审议事项--%>
    <table class="print_table" ng-repeat="detail in vm.printData.detailList">
        <tr >
            <td style="font-size: 24px;text-align: left;padding-left:40px;" valign="top">
                <p class="pStyle" style="text-indent: 2em;" ng-bind="detail.array +detail.title"></p>
            </td>
        </tr>
        <tr>
            <td style="font-size: 24px;text-align: left;padding-left:40px;" valign="top">
                <p class="pStyle" style="text-indent: 2em;" ng-bind="detail.conclusion"></p>
            </td>
        </tr>
    </table>

    <%--通报事项--%>
    <table ng-if="vm.printData.trafficList.length>0">
        <tr>
            <td class="pStyle" style="padding-left:40px;vertical-align:top;padding-top:40px;font-size: 24px;text-indent: 2em;">会议听取以下事项，现纪要如下：</td>
        </tr>
    </table>
    <table ng-repeat="traffic in vm.printData.trafficList" ng-if="vm.printData.trafficList.length>0">
        <tr >
            <td style="font-size: 24px;text-align: left;padding-left:40px;"  valign="top">
                <p class="pStyle" style="text-indent: 2em;" ng-bind="traffic.array +traffic.title"></p>
            </td>
        </tr>
    </table>
</div>