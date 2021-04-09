
<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 40px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden >
    <h2 style="text-align: center;">中国五洲工程设计集团有限公司</h2>
    <h3 style="text-align: center;" ng-bind="vm.printData.tableName"></h3>
<%--表单信息--%>
    <table class="print_table" ng-repeat="model in vm.printData.groupList" ng-if="model.groupName!=''">
        <thead>
            <tr>
                <td colspan="4" style="width: 100%;font-size: 16px;color: black;font-weight: bold" ng-bind="model.groupName"></td>
            </tr>
        </thead>
       <tbody>
           <tr ng-repeat="item in model.items" ng-if="($index%2)===0" >
               <td style="width: 15%;" ng-bind="item.commonFormDetail.inputText"></td>
               <td style="width: 35%;" ng-bind="item.inputValue"></td>
               <td style="width: 15%;" ng-bind="model.items[$index+1].commonFormDetail.inputText"></td>
               <td style="width: 35%;" ng-bind="model.items[$index+1].inputValue"></td>
           </tr>
       </tbody>
    </table>
    <%--子表 表格打印形式--%>
    <div ng-repeat="group in vm.printData.groupDetailList" ng-if="vm.printData.childTableType==1">
        <table  class="print_table" >
            <tr>
                <td  style="width: 100%;font-size: 16px;color: black;font-weight: bold;border-bottom: 0px;border-top: 0px" ng-bind="group.name">子项信息</td>
            </tr>
        </table>
        <table  class="print_table" >
            <thead>
                <tr>
                    <td style="width: 35px;" >#</td>
                    <td ng-repeat="item in group.list[0]" ng-bind="item.name"></td>
                </tr>
            </thead>
                <tbody ng-repeat="kv in group.list">
                   <tr>
                       <td style="width: 35px;" ng-bind="$index +1"></td>
                       <td ng-repeat="sk in kv" ng-bind="sk.code"></td>
                   </tr>
                </tbody>
        </table>
    </div>

    <%--通用子表默认打印方式--%>
    <div ng-repeat="group in vm.printData.groupDetailList" ng-if="vm.printData.childTableType==2">
        <table  class="print_table" ng-repeat="item in group.list">
            <thead>
                <tr>
                    <td colspan="4"  style="width: 100%;font-size: 16px;color: black;font-weight: bold;border-bottom: 0px;border-top: 0px" ng-bind="group.name+($index+1)"></td>
                </tr>
            </thead>
            <tbody>
               <tr ng-repeat="kv in item"  ng-if="($index%2)===0 &&kv.seq===6" >
                   <td style="width: 15%;" ng-bind="kv.name"></td>
                   <td style="width: 35%;" ng-bind="kv.code"></td>
                   <td style="width: 15%;" ng-bind="item[$index+1].name"></td>
                   <td style="width: 35%;"  ng-bind="item[$index+1].code"></td>
               </tr>
               <tr ng-repeat="kv in item"  ng-if=" kv.seq===12" >
                   <td style="width: 15%;" ng-bind="kv.name"></td>
                   <td colspan="3" style="width: 75%;" ng-bind="kv.code"></td>
               </tr>
            </tbody>

        </table>

    </div>

    <%--流程信息--%>
    <table class="print_table" ><%--ng-if="vm.printData.nodes>0"--%>
        <thead>
            <tr>
                <td colspan="2" style="width: 100%;font-size: 16px;color: black;font-weight: bold;border-top: 0px;" >流程信息</td>
            </tr>
        </thead>
        <tbody>
            <tr  ng-repeat="node in vm.printData.nodes" >
                <td style="width: 20%;border-top: none" ng-bind="node.name"></td>
                <td style="width: 80%;border-top: none">
                    <div ng-repeat=" comment in node.comments.reverse()">
                        <img ng-if="comment.userId.length>0"style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+comment.userId}}">
                        <span ng-bind="comment.cnName" style="margin-right: 10px;"></span>
                        <span ng-bind="comment.message" style="margin-right: 10px;"></span>
                        <span ng-bind="comment.gmtCreate|date:'yyyy-MM-dd'"></span>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
