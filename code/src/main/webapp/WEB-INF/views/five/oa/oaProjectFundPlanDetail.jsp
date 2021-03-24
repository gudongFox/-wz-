<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:45})">工程管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">项目资金使用计划</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">项目资金使用计划</span>
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
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.projectName"
                                                   name="projectName" required="true"  ng-disabled="!processInstance.firstTask"/>
                                            <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showContractLibraryModal()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true"   ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true"   ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">计划时间</label>
                                <div class="col-md-4">
                                    <div class="input-group  date date-picker" id="计划时间"  data-date-format="yyyy-mm" data-date-viewmode="years" data-date-minviewmode="months">
                                        <input type="text"  class="form-control" required="true" name="planTime"
                                               ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.planTime" placeholder="报送日期">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeLeaderManName" name="chargeLeaderManName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeLeaderMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">编制单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">部门负责人</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.deptChargeMenName" name="deptChargeMenName"  required="true" readonly />
                                            <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeMen');" ng-disabled="!processInstance.firstTask" ><i class="fa fa-user"></i></button>
                                             </span>
                                        </div>
                                        <span ng-show="processInstance.firstTask"style="color: red">注：请选择部门负责人会签</span>
                                    </div>
                                <label class="col-md-2 control-label ">备注</label>
                                
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"  >
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
<div class="actions" style="margin-bottom: 10px;margin-left: 20px;">
    <a href="javascript:;" class="btn btn-sm btn-default yellow-stripe" ng-click="vm.upLoadMonth();"
       ng-show="processInstance.firstTask">
        <i class="fa fa-cog"></i> 匹配前置数据 </a>
    <span id="btnUpload" class="btn btn-sm btn-default fileinput-button yellow-stripe">
                            <i class="fa fa-upload"></i>
                            <span>上传</span>
                            <input type="file" name="file" id="uploadFile1"
                                   multiple accept="application/vnd.ms-excel" >
                </span>
    <a href="/assets/doc/five/项目资金使用计划.xls" class="btn btn-sm btn-default yellow-stripe"
       ng-show="processInstance.firstTask">
        <i class="fa fa-download"></i> 模板下载 </a>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 总包合同（收）<span style="margin-left: 5px;font-size: 12px">单位：万元</span>
            <span style="margin-left: 30px;color: red;font-size: 12px">收款合计:</span>&nbsp;&nbsp;&nbsp;合同额:&nbsp;<span ng-bind="vm.item.totalContractPrice"/>&nbsp;&nbsp;&nbsp;本月前累计收款:&nbsp;<span ng-bind="vm.item.totalAccumulativePrice"/> &nbsp;&nbsp;&nbsp;本月应该收款:&nbsp;<span ng-bind="vm.item.totalReceivablePrice"/>&nbsp;&nbsp;&nbsp;合同尾款:&nbsp;<span ng-bind="vm.item.totalFinalPrice"/>
    </div>
        <div class="actions">

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0,'总包');"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 135px">合同号</th>
                    <th >采购平台</th>
                    <th style="width: 80px">采购方式</th>
                    <th>对方单位名称</th>
                    <th>项目名称</th>
                    <th style="width: 110px">合同额</th>
                    <th style="width: 110px">本月前累计收款</th>
                    <th style="width: 110px">本月应收</th>
                    <th style="width: 110px">合同尾款</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details|filter:{type:'总包'}:true">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.contractNo" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.purchasePlatform"></td>
                    <td ng-bind="detail.purchaseType"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.projectName"></td>
                    <td ng-bind="detail.contractPrice"></td>
                    <td ng-bind="detail.accumulativePrice"></td>
                    <td ng-bind="detail.receivablePrice"></td>
                    <td ng-bind="detail.finalPrice"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 分包合同（付）  <span style="margin-left: 5px;font-size: 12px">单位：万元</span>
            <span style="margin-left: 30px;color: red;font-size: 12px">付款合计:</span>&nbsp;&nbsp;&nbsp;合同额:&nbsp;<span ng-bind="vm.item.totalContractPrice2"/>&nbsp;&nbsp;&nbsp;本月前累计付款:&nbsp;<span ng-bind="vm.item.totalAccumulativePrice2"/> &nbsp;&nbsp;&nbsp;本月应该付款:&nbsp;<span ng-bind="vm.item.totalReceivablePrice2"/>&nbsp;&nbsp;&nbsp;合同尾款:&nbsp;<span ng-bind="vm.item.totalFinalPrice2"/>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0,'分包');"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 135px">合同号</th>
                    <th>采购平台</th>
                    <th style="width: 80px">采购方式</th>
                    <th>对方单位名称</th>
                    <th>项目名称</th>
                    <th style="width: 110px">合同额</th>
                    <th style="width: 110px">本月前累计付款</th>
                    <th style="width: 110px">本月应付款</th>
                    <th style="width: 110px">合同尾款</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details|filter:{type:'分包'}:true">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.contractNo" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.purchasePlatform"></td>
                    <td ng-bind="detail.purchaseType"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.projectName"></td>
                    <td ng-bind="detail.contractPrice"></td>
                    <td ng-bind="detail.accumulativePrice"></td>
                    <td ng-bind="detail.receivablePrice"></td>
                    <td ng-bind="detail.finalPrice"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="portlet light">
    <div class="caption">
        <i class="fa fa-file"></i> 总计 <span style="margin-left: 5px;font-size: 12px">单位：万元</span>
        <span style="margin-left: 40px;font-size: 16px">项目结余:</span>&nbsp;&nbsp<span ng-bind="vm.item.allProjectMoney" style="color: red" ng-if="vm.item.allProjectMoney<0"/>&nbsp;<span ng-bind="vm.item.allProjectMoney" style="color: green" ng-if="vm.item.allProjectMoney>0"/>&nbsp;
    </div>
</div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">资金使用计划</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="productDetailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">合同类型</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.type" name="type" required="true" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">采购平台</label>
                        <div class="col-md-7">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'采购平台'}:true"
                                    ng-model="vm.detail.purchasePlatform" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">采购方式</label>
                        <div class="col-md-7">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'采购方式'}:true"
                                    ng-model="vm.detail.purchaseType" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">合同号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.contractNo" name="contractNo" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">对方单位名称</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">项目名称</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.projectName" name="projectName" required="true">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label">合同额</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.contractPrice" ng-change=" vm.coutFinalPrice();" name="contractPrice" required="true">
                        </div>
                    </div>
                    <div class="form-group">

                        <label class="col-md-4 control-label" ng-if="vm.detail.type=='总包'">本月前累计收款</label>
                        <label class="col-md-4 control-label" ng-if="vm.detail.type=='分包'">本月前累计付款</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.accumulativePrice" ng-change=" vm.coutFinalPrice();" name="accumulativePrice" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label" ng-if="vm.detail.type=='总包'">本月应收</label>
                        <label class="col-md-4 control-label" ng-if="vm.detail.type=='分包'">本月应付</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.receivablePrice" name="receivablePrice" ng-change=" vm.coutFinalPrice();" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">尾款</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.finalPrice" name="finalPrice" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">排序</label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" ng-model="vm.detail.seq" name="seq" required="true">
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

<jsp:include page="../print/print-oaProjectFundPlanDetail.jsp"/>


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