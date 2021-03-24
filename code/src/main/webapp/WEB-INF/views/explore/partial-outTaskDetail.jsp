<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘察野外作业钻探任务书
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/outTask/exportData.json?id='+vm.outTaskId}}" target="_blank">
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
                     style="min-height: 600px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工程地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.constructionAddress" name="constructionAddress"
                                           required="true" maxlength="50" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="outDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.outDate"  name="outDate"  required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>建筑物性质</strong>&nbsp;结构类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.buildStructure" name="buildStructure" required="true"  maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">层数（高度）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.buildHeight" name="buildHeight"  required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟采用基础形式</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.buildBase" name="buildBase" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">基底荷载</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.buildUnderLoad" name="buildUnderLoad" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>一、放孔测量</strong>&nbsp;放孔（点）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.holePoint" name="holePoint" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">地形测量（Km2）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.holeLand" name="holeLand" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">断面测量（Km2）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.holeFracture" name="holeFracture" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">完成日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="holeMeasureTime">
                                        <input type="text" class="form-control" name="holeMeasureTime" required="true"
                                               ng-model="vm.item.holeMeasureTime"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.holeMeasureUser" name="holeMeasureUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>


                            <div class="form-group">
                            <label class="col-md-2 control-label required"><strong>二、控制</strong>&nbsp;取样孔</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control"  ng-model="vm.item.controlSample " required="true" name="controlSample"  maxlength="20" ng-disabled="!processInstance.firstTask"/>
                            </div>
                            <label class="col-md-2 control-label required">鉴别孔</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control"  ng-model="vm.item.controlIdentify"  required="true" name="controlIdentify"  maxlength="20" ng-disabled="!processInstance.firstTask"/>
                            </div>
                        </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">取岩样孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.controlRock" name="controlRock" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">取土样孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.controlSoil" name="controlSoil" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">取水样孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.controlWater" name="controlWater" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">水文观测孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.controlWaterLook" name="controlWaterLook" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">探槽、探井、探坑</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.controlDig" name="controlDig" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">完成日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="controlTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.controlTime" name="controlTime"   required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.controlUser" name="controlUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>三、野外测试</strong>&nbsp;静探孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outSilence " name="outSilence" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">动探孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outAction" name="outAction" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">标贯孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outPass" name="outPass" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">波速测试孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outWaveSpeed" name="outWaveSpeed" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">水文测试孔</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.outWater" name="outWater" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">其他</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outOther" name="outOther" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">完成日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="outTime">
                                        <input type="text" class="form-control" name="outTime" required="true"
                                               ng-model="vm.item.outTime"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.outUser" name="outUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>四、取样</strong>&nbsp;原状土样</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="件"   ng-model="vm.item.sampleOriginalSoil" name="sampleOriginalSoil" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">扰动土样</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="件"   ng-model="vm.item.sampleActionSoil" name="sampleActionSoil" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">岩芯样</label>
                                <div class="col-md-4">
                                     <input type="text" class="form-control" placeholder="件"   ng-model="vm.item.sampleRock" name="sampleRock" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                 </div>
                                <label class="col-md-2 control-label required">水样</label>
                                 <div class="col-md-4">
                                     <input type="text" class="form-control" placeholder="件"  ng-model="vm.item.sampleWater" name="sampleWater" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">


                                <label class="col-md-2 control-label required">完成日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="sampleTime">
                                        <input type="text" class="form-control" name="sampleTime" required="true"
                                               ng-model="vm.item.sampleTime"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.sampleUser" name="sampleUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">技术安全要求</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" required="true" ng-model="vm.item.techSec" rows="5" required="true"  ng-disabled="!processInstance.firstTask"></textarea>
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
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
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
<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-outTaskDetail.jsp"/>


