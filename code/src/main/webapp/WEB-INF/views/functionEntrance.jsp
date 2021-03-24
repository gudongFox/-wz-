<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row justify-content-center" >

    <div class="col-md-12"  ng-repeat="menu in vm.entranceMenus">
        <div class="portlet light cssShadow">
            <div class="portlet-title">
                <div class="caption">
                    <span style="font-size: 22px" class="caption-subject font-blue-steel bold uppercase">{{menu.name}}</span>
                </div>
            </div>
            <div class="portlet-body">
                <div ng-if="menu.menuList.length==0">
                    <div style="display: flex; flex-flow: wrap">

                        <a  ng-repeat="acl in menu.aclList" ui-sref="{{acl.code}}" style=" text-align:center;vertical-align:bottom;border-radius:5px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1) !important;width: 130px;height: 130px;background-color: #f7f8f9;color:#369; margin: 2px 5px 5px 2px;" >
                            <div style="height: 118px;">
                                <%-- <img src="/m/wuzhoulogo80x80.png" style="width: 50px;height: 50px;margin-top: 10px">--%>
                                <i class="{{acl.icon}}" style="font-size: 35px;margin-top: 30px;"></i>
                                <p style="margin-top: 15px;font-size: 20px;color: #369">{{acl.name}}</p>
                            </div>
                        </a>
                    </div>
                </div>
                <div ng-if="menu.menuList.length>0">
                    <div class="row">
                        <a  ng-repeat="acl in menu.aclList" ui-sref="{{acl.code}}" style=" text-align:center;vertical-align:bottom;border-radius:5px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1) !important;width: 110px;height: 110px;background-color: #f7f8f9;color:#369;float: left;  margin: 2px 5px 5px 2px;" >
                            <div style="height: 118px;">
                                <i class="fa {{acl.icon}} " style="font-size: 35px;margin-top: 30px; "></i>
                                <p style="margin-top: 15px;font-size: 20px;color: #369">{{acl.name}}</p>
                            </div>
                        </a>
                    </div>
                    <div class="row">
                        <div class="col-md-12" ng-repeat="menu1 in menu.menuList">
                            <div class="portlet light">
                                <div class="portlet-title" style="border-bottom: 0px">
                                    <div class="caption">
                                        <span class="caption-subject font-blue-steel bold uppercase">{{menu1.name}}</span>
                                    </div>
                                </div>
                                <div class="portlet-body" >
                                    <div style="display: flex; flex-flow: wrap; margin-top: 10px">
                                        <a  ng-repeat="acl1 in menu1.aclList" ui-sref="{{acl1.code}}" style=" text-align:center;vertical-align:bottom;border-radius:5px !important;box-shadow:1px 1px 1px 1px rgba(0,0,0,0.1);width: 110px;height: 110px;background-color: #f7f8f9;color:#369; margin: 2px 5px 5px 2px;" >
                                            <div style="height: 118px;">
                                                <%--  <img src="/m/wuzhoulogo80x80.png" style="width: 50px;height: 50px;margin-top: 10px">--%>
                                                <i class="fa {{acl1.icon}}" style="font-size: 35px;margin-top: 30px;"></i>
                                                <p style="margin-top: 15px;font-size: 20px;color: #369">{{acl1.name}}</p>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


