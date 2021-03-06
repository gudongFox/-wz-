<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:50})">科研审批流程</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">内部科研项目申请</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">内部科研项目申请</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
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


                            <div class="form-group" >
                                <label class="col-md-2 control-label required">课题名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName" required="true"  ng-disabled="!processInstance.firstTask" />
                                </div>


                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='deptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">开工日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="startTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.startTime" name="startTime" required="true"  readonly ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>  <label class="col-md-2 control-label ">完工日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="endTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.endTime" name="endTime" required="true"  readonly ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">课题负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeMenName" name="chargeMenName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMen');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">主要参加人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.attenderName" name="attenderName"  required="true" readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('attender');" ng-disabled="!processInstance.firstTask" ><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目类别</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="!processInstance.firstTask">
                                        <option value="公司级">公司级</option>
                                        <option value="自主开发">自主开发</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.projectType=='公司级'">专家委员会常委</label>
                                <div class="col-md-4" ng-if="vm.item.projectType=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.scientificFirstTrialName" name="scientificFirstTrialName"  required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('初审人员')==-1"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('scientificFirstTrial');"  ng-disabled="processInstance.myRunningTaskName.indexOf('初审人员')==-1" ><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                   <%-- <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('初审人员')>-1">请点击右侧<i style="color: black" class="fa fa-user"></i>选择专家委员会常委</span>--%>
                                    <span style="color: red" >由科研与技术质量部填写</span>
                                </div>
                            </div>

                            <div  class="form-group">
                                <label class="col-md-2 control-label required" >是否脱密</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.secret" name="secret" class="form-control"  ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否脱密'}:true"
                                            ng-disabled="!processInstance.firstTask">
                                    </select>
                                    <span style="color: red" >请确保完成保密审查，经过脱密处理，没有涉密信息</span>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">主要研究内容</label>
                                <div class="col-md-10">
                                    <textarea rows="6" class="form-control" ng-model="vm.item.projectContent" required="true" name="projectContent" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">成果形式及预期效益</label>
                                <div class="col-md-10">
                                    <textarea rows="6" class="form-control" ng-model="vm.item.achievement" required="true" name="achievement" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <span style="color: red">投入经费(万元)</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合计</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.totalPrice" name="totalPrice"  required="true" ng-disabled="true" />
                                </div>
                                <label class="col-md-2 control-label required">材料费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.materialsCost" ng-change=" vm.countTotalPrice('materialsCost');" name="materialsCost" ng-blur="vm.burl('materialsCost')" required="true" ng-disabled="!processInstance.firstTask"  />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">专用费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.appropriativeCost" name="appropriativeCost"  ng-change=" vm.countTotalPrice('appropriativeCost');" ng-blur="vm.burl('appropriativeCost')" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">外协费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.outsourceCost" name="outsourceCost" ng-change=" vm.countTotalPrice('outsourceCost');" ng-blur="vm.burl('outsourceCost')" required="true" ng-disabled="!processInstance.firstTask"  />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">事务费-会议费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.meetingCost" name="meetingCost" ng-change=" vm.countTotalPrice('meetingCost');" ng-blur="vm.burl('meetingCost')" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">事务费-差旅费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.travelCost" name="travelCost" ng-change=" vm.countTotalPrice('travelCost');" ng-blur="vm.burl('travelCost')" required="true" ng-disabled="!processInstance.firstTask"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">事务费-专家咨询费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.specialistCost" name="specialistCost" ng-change=" vm.countTotalPrice('specialistCost');" ng-blur="vm.burl('specialistCost')" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">固定资产折旧费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.fixeAssetDepreciationCost" name="fixeAssetDepreciationCost" ng-change=" vm.countTotalPrice('fixeAssetDepreciationCost');" ng-blur="vm.burl('fixeAssetDepreciationCost')" required="true" ng-disabled="!processInstance.firstTask"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">燃料动力费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.fuelPowerCost" name="fuelPowerCost" ng-change=" vm.countTotalPrice('fuelPowerCost');" ng-blur="vm.burl('fuelPowerCost')" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">工资及劳务费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.salaryServiceCost" name="salaryServiceCost" ng-change=" vm.countTotalPrice('salaryServiceCost');" ng-blur="vm.burl('salaryServiceCost')" required="true" ng-disabled="!processInstance.firstTask"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label " >备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" placeholder="" ng-disabled="!processInstance.firstTask"/>
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


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>