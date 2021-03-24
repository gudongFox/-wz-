<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:64})">行政事务部</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">职工卡充值变动</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">职工卡充值变动</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">变动说明</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" rows="3"
                                              ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"  >
                                    </textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 职工卡修改列表
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0,'总包');"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 135px">姓名</th>
                    <th style="width: 110px">职工号</th>
                    <th style="width: 80px">员工类别</th>
                    <th>充值卡当前类型</th>
                    <th>充值卡调整类型</th>

                    <th>备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.userName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.userLogin"></td>
                    <td ng-bind="detail.userType"></td>
                    <td ng-bind="detail.cardTypeNow"></td>
                    <td ng-bind="detail.cardTypeChange"></td>
                    <td ng-bind="detail.remark"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-disabled="!processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>



<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">职工卡修改详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="productDetailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">姓名</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.userName" name="type" required="true" >
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">职工号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.userLogin" name="type" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">员工类别</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.userType" name="type" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">充值卡当前类型</label>
                        <div class="col-md-7">
                           <span ng-repeat="cardType in vm.cardTypeList">
                            <input type="checkbox" style="width: 15px;height: 15px;" class="cb_cardTypeNow"
                                                   ng-checked="vm.detail.cardTypeNow.indexOf(cardType.name)>=0" ng-click="vm.chooseCardType('cardNow')"
                                                   data-name="{{cardType.name}}"/> <span ng-bind="cardType.name"
                                                                                      class="margin-right-10"
                                                                                      style="font-size: 15px;"></span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">充值卡调整类型</label>
                        <div class="col-md-7">
                          <span ng-repeat="cardType in vm.cardTypeList">
                            <input type="checkbox" style="width: 15px;height: 15px;" class="cb_cardTypeChange"
                                   ng-checked="vm.detail.cardTypeChange.indexOf(cardType.name)>=0" ng-click="vm.chooseCardType('cardChange')"
                                   data-name="{{cardType.name}}"/> <span ng-bind="cardType.name"
                                                                         class="margin-right-10"
                                                                         style="font-size: 15px;"></span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-disabled="!processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>


<div class="modal fade draggable-modal" id="selectLibraryModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
        <div class="portlet-title">
            <div class="caption">
                <i class="icon-clock"></i>选择合同库中的合同
                <small >当前已选择合同：<span ng-bind="vm.item.projectName"></span></small>
            </div>
        </div>
        <div class="portlet-body" >
            <div class="tabbable-custom">
                <ul class="nav nav-tabs ">
                    <li class="active">
                        <a href="#tab_15_review" data-toggle="tab" aria-expanded="true">
                            合同库
                        </a>
                    </li>

                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_15_review"
                         style="min-height: 400px;">
                        <div class="modal-body" >
                            <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th>项目名称</th>
                                        <th class="hidden-md hidden-sm">项目号</th>
                                        <th>合同名称</th>
                                        <th>合同类型</th>
                                        <th class="hidden-md hidden-sm">合同号</th>
                                        <th>合同额（万元）</th>
                                        <th style="width: 90px">签约日期</th>
                                        <th style="width: 100px;">创建时间</th>
                                        <th style="width: 100px;">合同来源</th>
                                        <th style="width: 30px;">补充合同标识</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.librarys">
                                        <td>
                                            <input type="checkbox" ng-checked="vm.item.mainContractLibraryId==item.id" class="cb_library"
                                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                        </td>
                                        <td ng-bind="item.projectName"  class="data_title" ><strong ></strong></td>
                                        <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                        <td ng-bind="item.contractName"  class="data_title" ><strong ></strong></td>
                                        <td ng-bind="item.contractType"   ><strong ></strong></td>
                                        <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>

                                        <td ng-bind="item.contractMoney " class="hidden-md hidden-sm"></td>
                                        <td ng-bind="item.signTime"></td>
                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                        <td>
                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId==0"><span style="color: red" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单</span></strong>
                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId!=0"><span style="color: green" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单(已补录)</span></strong>
                                            <strong   ng-if="item.contractReviewId!=0&&item.contractPreId==0"><span style="color: blue" ng-click="vm.opt=0;vm.showNew(item.contractReviewId)">合同评审</span></strong>
                                            <strong  ng-if="item.contractReviewId==0&&item.preContractId==0" ><span style="color: grey">其他</span></strong>
                                        </td>
                                        <td class="text-center">
                                            <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                            <span class="label label-sm label-default" ng-if="!item.main">否</span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn blue" ng-click="vm.saveSelectModel();">确认</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<%-- <div class="form-group">
                               <label class="col-md-2 control-label required">总经理</label>
                               <div class="col-md-4">
                                   <div class="input-group">
                                       <input type="text" class="form-control" ng-model="vm.item.totalMangerName" name="totalMangerName"  required="true" ng-disabled="!processInstance.firstTask" />
                                       <span class="input-group-btn" >
                                           <button class="btn default" type="button" ng-click="vm.showUserModel('totalManger');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                   </div>
                               </div>
                               <label class="col-md-2 control-label required">总会计师</label>
                               <div class="col-md-4">
                                   <div class="input-group">
                                       <input type="text" class="form-control" ng-model="vm.item.totalAccountantName" name="totalAccountantName"  required="true" ng-disabled="!processInstance.firstTask" />
                                       <span class="input-group-btn" >
                                           <button class="btn default" type="button" ng-click="vm.showUserModel('totalAccountant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                   </div>
                               </div>
                           </div>--%>
<%-- <div class="form-group">
     <label class="col-md-2 control-label required">分管副总</label>
     <div class="col-md-4">
         <div class="input-group">
             <input type="text" class="form-control" ng-model="vm.item.chargeLeaderManName" name="chargeLeaderManName"  required="true" ng-disabled="!processInstance.firstTask" />
             <span class="input-group-btn" >
                 <button class="btn default" type="button" ng-click="vm.showUserModel('chargeLeaderMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
              </span>
         </div>
     </div>
     <label class="col-md-2 control-label required">财务金融部</label>
     <div class="col-md-4">
         <div class="input-group">
             <input type="text" class="form-control" ng-model="vm.item.financeDeptMenName" name="financeDeptMenName"  required="true" ng-disabled="!processInstance.firstTask" />
             <span class="input-group-btn" >
                 <button class="btn default" type="button" ng-click="vm.showUserModel('financeDeptMen');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
              </span>
         </div>
     </div>
 </div>--%>
<%--<div class="form-group">
    <label class="col-md-2 control-label required">工程管理部</label>
    <div class="col-md-4">
        <div class="input-group">
            <input type="text" class="form-control" ng-model="vm.item.projectManagementMenName" name="projectManagementMenName"  required="true" ng-disabled="!processInstance.firstTask" />
            <span class="input-group-btn" >
                <button class="btn default" type="button" ng-click="vm.showUserModel('projectManagementMen');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
             </span>
        </div>
    </div>

</div>--%>