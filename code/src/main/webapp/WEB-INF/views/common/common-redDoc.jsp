<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div class="portlet light" ng-controller="RedHeaderController as rh" >
    <div class="portlet-body ">
        <form class="form-horizontal" role="form">
            <div class="form-body">

                <div class="form-group">
                    <label class="col-md-2 control-label ">发文word</label>
                    <div class="col-md-10">
                        <a href="#" ng-click="rh.readDocOnly(redheadFile.id)" ng-bind="redheadFile.fileName">红头文件.doc</a>&nbsp;&nbsp;
                        <%--ng-show="processInstance.firstTask"--%>

                        <span id="btnUpload0" class="btn btn-sm default fileinput-button" ng-show="processInstance.firstTask">
                        <i class="fa fa-cloud-upload" ></i><span>上传</span>
                        <input id="redHead" type="file" name="singleUpload" accept=".doc,.docx.pdf"></span>
                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.editRedHead(vm.item)" ng-show="redheadFile!=undefined&&redheadFile.fileName.indexOf('.pdf')==-1&&(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('文号')>=0)"><i class='fa fa-edit'></i><span>编辑/套红</span></span>
                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.firstTask"><i class='fa fa-trash'></i><span>删除</span></span>

                    </div>
                </div>
                <div ng-controller="RedHeaderController">
                    <input id="hideMessage" hidden value="edit" >
                </div>
            </div>
        </form>
    </div>
    <script >
        function ntkoCloseEvent(){

           var input= document.getElementById("hideMessage");
            if (input.value=="edit"){
                //关闭时间回调函数
                var appElement = document.querySelector('[ng-controller=RedHeaderController]');
                var $scope  = angular.element(appElement).scope();
                $scope.$apply();
                $scope.ntkoCloseEvent()
            }else {
                input.value="edit";
            }
        }
    </script>
</div>


<div class="modal fade draggable-modal" id="selectRedHeadModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">历史记录</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>文件名称</th>
                            <th style="width: 90px;">文件类型</th>
                            <th style="width: 90px;">文件大小</th>
                            <th style="width: 60px;">创建人</th>
                            <th style="width: 130px;">创建时间</th>
                            <th style="width: 130px;">修改时间</th>
                            <th style="width: 180px;">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat=" contentFile in contentFiles">
                            <td>
                                <span ng-bind="$index+1"></span>
                            </td>
                            <td>
                                <img class="cloud-file-type" ng-src="/m/img/filetype/docx.png" />
                                <span ng-bind="contentFile.fileName" ng-show="browserVersion!='ie9'" class="data_title"
                                      ng-click=""></span>
                                <a ng-href="" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" ></a>
                                <span class="label label-sm label-success" ng-show="processInstance.preHandleTime<contentFile.gmtModified">新</span>
                            </td>
                            <td >
                                <span ng-if="contentFile.fileType==1">正文</span>
                                <span ng-if="contentFile.fileType==0">红头文件</span>
                            </td>
                            <td ng-bind="contentFile.size"></td>
                            <td ng-bind="contentFile.creatorName"></td>
                            <td ng-bind="contentFile.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                            <td ng-bind="contentFile.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                            <td >

                                <i class="fa  fa-cloud margin-right-5" ng-click="editDocFile(contentFile.businessKey,1);"
                                   title="编辑">编辑</i>
                                <i class="fa  fa-cloud margin-right-5" ng-click="showVersion(file.id);"
                                   title="删除">删除</i>
                            </td>

                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div></div>


