<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade" id="backFlowModal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">打回任务</h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-7">
                    <form class="form-horizontal" role="form" id="backFlowForm">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">当前节点</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="当前节点"
                                           ng-model="processInstance.myTaskName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">办理人</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="办理人"
                                           ng-model="processInstance.userName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">指派时间</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control"
                                           value="{{processInstance.myTaskTime | date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">打回意见
                                    <span style="color: red;margin-left: 2px;">*</span>
                                </label>
                                <div class="col-md-7">
                                    <textarea class="form-control"  id="backFlowComment" name="flowComment"
                                              placeholder="您的打回意见"
                                              required="true" maxlength="20"></textarea>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5" style="max-height: 300px;">
                    <div id="backFlowTree"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="backFlow();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="sendFlowModal" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">发送任务</h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-7">
                    <form class="form-horizontal" role="form" id="sendFlowForm">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-4 control-label">当前节点</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="当前节点"
                                           ng-model="processInstance.myTaskName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">办理人</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control" placeholder="办理人"
                                           ng-model="processInstance.userName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">指派时间</label>
                                <div class="col-md-7">
                                    <input type="text" class="form-control"
                                           value="{{processInstance.myTaskTime | date:'yyyy-MM-dd HH:mm:ss'}}"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">审核意见<span
                                        style="color: red;margin-left: 2px;">*</span></label>
                                <div class="col-md-7">
                                        <textarea rows="3" class="form-control" placeholder="您的审核意见" id="sendFlowComment"
                                                  ng-model="flowComment" required="true" maxlength="50"
                                                  name="flowComment">
                                        </textarea>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-5">
                    <div id="sendFlowTree"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="sendFlow();">确认</button>
            </div>
        </div>
    </div>
</div>
