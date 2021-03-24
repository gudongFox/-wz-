<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style3">
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

</style>

<div id="print_area3" hidden>
    <h2 style="text-align: center; color: red;font-size: 50px">中共中国五洲工程设计集团</h2>
    <h1 style="text-align: center; color: red;font-size: 50px"><span>委员会会议纪要</span></h1>

    <table class="print_table">
        <tr style="border-bottom: 2px solid red; ">
            <td style="width:50%;border-bottom: 2px solid red;font-size: 16px;text-align: left" >五洲集团党委办公室</td>
            <td style="width:50%;border-bottom: 2px solid red;font-size: 16px;text-align: right" ng-bind="vm.printData.meetingTimePlan2">2020年7月27日</td>
        </tr>
        <tr>
            <td colspan="2" style="font-size: 26px;text-align: center" ><strong ng-bind="vm.printData.item">董事会2020年度第十五次会议纪要</strong></td>
        </tr>
        <tr>
            <td style="height: 40px" ></td>
            <td ></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:100%;padding-left:40px;font-size: 24px;">
                <p style="text-indent: 2em;" >
                   <span  ng-bind="vm.printData.meetingTimePlan">7月27日</span>，公司召开<span ng-bind="vm.printData.year">2020</span >年第<span ng-bind="vm.printData.stages">30</span>次党委会，公司党委书记<span ng-bind="vm.printData.compereName">刘志刚</span>主持会议，
                    公司党委成员参加会议，公司领导和相关部门主要负责人列席相关议题。会议议定事项纪要如下：
                </p>
            </td>
        </tr>
    </table>
    <%--审议事项--%>
    <table class="print_table" ng-repeat="detail in vm.printData.detailList">
        <tr >
            <td style="width:10%;padding-left:20px;font-size: 24px;text-align: right" ng-bind="detail.array" valign="top"></td>
            <td style="font-size: 24px;width: 80%" valign="top"><p  ><strong ng-bind="detail.title"></strong></p></td>
        </tr>
        <tr >
            <td style="width:10%;padding-left:20px;font-size: 24px;text-align: right"  valign="top"></td>
            <td style="font-size: 24px;width: 80%" valign="top"><p  ng-bind="detail.conclusion"></p></td>
        </tr>
    </table>



    <table  class="print_table">
        <tr >
            <td style="border-top: 100px;border-bottom: 2px solid #2c2830;"></td>
            <td style="border-top: 100px;border-bottom: 2px solid #2c2830;"></td>
        </tr>
        <tr >
            <td  style="width:15%; border-top: 2px solid #2c2830; font-size: 16px">出席：</td>
            <td style="border-top: 2px solid #2c2830;  font-size: 16px" ng-bind="vm.printData.companyLeaderName">刘志刚  湛  峰  李宝林</td>
        </tr>
        <tr ng-if="vm.printData.absentList.length>0">
            <td  style="width:15%;  font-size: 16px ">缺席：</td>
            <td  style=" font-size: 16px " ng-bind="vm.printData.absentList[0].name"></td>
        </tr>
        <tr ng-repeat="absent in vm.printData.absentList" ng-if="$index>0">
            <td style="padding-left:40px;font-size: 16px;"></td>
            <td style="font-size: 16px;" ng-bind="absent.name"></td>
        </tr>
        <tr>
            <td style="width:15%; font-size: 16px">列席：</td>
            <td ng-bind="vm.printData.attenderName2[0].name"></td>
        </tr>
        <tr ng-repeat="attender in vm.printData.attenderName2" ng-if="$index>0">
            <td style="padding-left:40px;font-size: 16px;"></td>
            <td style="font-size: 16px;" ng-bind="attender.name"></td>
        </tr>

    </table>
    <table class="print_table">
        <tr>
            <td style=" width:15%;  font-size: 16px; border-top: 2px solid #2c2830;">主送:</td>
            <td style=" font-size: 16px; border-top: 2px solid #2c2830;">公司党委成员</td>
        </tr>
        <tr>
            <td style=" width:10%; font-size: 16px">抄送：</td>
            <td style=" font-size: 16px">公司办公室（董事会办公室）、人力资源部、党群工作部、纪检工作部</td>
        </tr>
        <tr>
            <td style=" font-size: 16px;border-bottom: 2px solid #2c2830;">记录：</td>
            <td style=" font-size: 16px;border-bottom: 2px solid #2c2830;" ng-bind="vm.printData.recordManName">林  芳</td>
        </tr>
    </table>

</div>