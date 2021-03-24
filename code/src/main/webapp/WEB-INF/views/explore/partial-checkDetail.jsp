<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 岩土工程勘察野外检查记录表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/check/exportData.json?id='+vm.checkId}}" target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group"> <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.stepName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">测绘放孔-钻孔位置</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.holeLocation" name="holeLocation" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="准确" >准确</option>
                                        <option value="不准确" >不准确</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">测绘放孔-孔口高程</label>
                                <div class="col-md-4">
                                <select ng-model="vm.item.holeHeight" name="holeHeight" class="form-control" ng-disabled="!processInstance.firstTask">
                                    <option value="准确" >准确</option>
                                    <option value="不准确" >不准确</option>
                                </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">测绘放孔-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.holeResult" name="holeResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="重新测绘" >重新测绘</option>
                                        <option value="校核 " >校核 </option>
                                        <option value="合格" >合格</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label required">测绘放孔-检查时间</label>
                                <div class="col-md-4">
                                <div class="input-group date date-picker" id="holeTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.holeTime" name="holeTime"  required="true" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">机具设备-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.equipmentState" name="equipmentState" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="运转正常" >运转正常</option>
                                        <option value="基本正常" >基本正常</option>
                                        <option value="不正常" >不正常</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">机具设备-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.equipmentResult" name="equipmentResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="更换" >更换</option>
                                        <option value="现场准确" >现场准确</option>
                                        <option value="设备正常" >设备正常</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">机具设备-检查时间</label>
                                <div class="col-md-4">
                                <div class="input-group date date-picker" id="equipmentTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.equipmentTime" name="equipmentTime" required="true"  ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">孔位偏移-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.holeOffset" name="holeOffset" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="无偏移" >无偏移</option>
                                        <option value="偏移较小" >偏移较小</option>
                                        <option value="偏移较大" >偏移较大</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">孔位偏移-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.holeOffsetResult" name="holeOffsetResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="满足" >满足</option>
                                        <option value="不满足勘察要求，重新钻孔">不满足勘察要求，重新钻孔</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">孔位偏移-检查时间</label>
                                <div class="col-md-4">
                                <div class="input-group date date-picker" id="holeOffsetTime">
                                    <input type="text" class="form-control" name="holeOffsetTime" required="true"
                                           ng-model="vm.item.holeOffsetTime" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">钻孔、取样-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.sampleState" name="sampleState" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="取样位置正常" >取样位置正常</option>
                                        <option value="取样位置偏浅" >取样位置偏浅</option>
                                        <option value="取样位置偏深" >取样位置偏深</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">钻孔、取样-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.sampleResult" name="sampleResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="满足" >满足</option>
                                        <option value="不满足勘察要求，重新钻孔" >不满足勘察要求，重新钻孔</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">钻孔、取样-检查时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="sampleTime">
                                        <input type="text" class="form-control" name="sampleTime" required="true"
                                               ng-model="vm.item.sampleTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">原位测试-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.orginalState" name="orginalState" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="满足勘察要求" >满足勘察要求</option>
                                        <option value="基本满足勘察要求" >基本满足勘察要求</option>
                                        <option value="不满足勘察要求" >不满足勘察要求</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">原位测试-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.orginalResult" name="orginalResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="合格" >合格</option>
                                        <option value="基本合格" >基本合格</option>
                                        <option value="不合格，重新补做" >不合格，重新补做</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">原位测试-检查时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="orginalTime">
                                        <input type="text" class="form-control" name="orginalTime" required="true"
                                               ng-model="vm.item.orginalTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">岩土样、封存、储存-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.rockState" name="rockState" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="样品制作良好" >样品制作良好</option>
                                        <option value="样品制作合格" >样品制作合格</option>
                                        <option value="样品制作不合格 " >样品制作不合格 </option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">岩土样、封存、储存-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.rockResult" name="rockResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="不处理" >不处理</option>
                                        <option value="重新制作，封装" >重新制作，封装</option>
                                    </select>
                                 </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">岩土样、封存、储存-检查时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="rockTime">
                                        <input type="text" class="form-control" name="rockTime" required="true"
                                               ng-model="vm.item.rockTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">野外编录-状态</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.outState" name="outState" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="质量良好" >质量良好</option>
                                        <option value="质量一般" >质量一般</option>
                                        <option value="质量差" >质量差</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">野外编录-处理意见</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.outResult" name="outResult" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="编录内容合格，不处理" >编录内容合格，不处理</option>
                                        <option value="不合格，重新整理" >不合格，重新整理</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">野外编录-检查时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="outTime">
                                        <input type="text" class="form-control" name="outTime" required="true"
                                               ng-model="vm.item.outTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">检验要求</label>
                                <div class="col-md-10">
                                   <textarea id="maxlength_textarea" ng-model="vm.item.checkRule" name="checkRule"  class="form-control" maxlength="250" rows="8" placeholder="" ng-disabled="!processInstance.firstTask">
                                   </textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">对纠正、改进<br/>跟踪验证结果</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" rows="3"  ng-model="vm.item.correctDesc" name="correctDesc" required="true" maxlength="200" ng-disabled="!processInstance.firstTask">
                                    </textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">作业班组</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.operationTeam" name="operationTeam" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile2.jsp"/>
<jsp:include page="./print/print-cheakDetail.jsp"/>

