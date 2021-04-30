
var appName = "h5App";
var openTypeList=["oaStampApplyOffice","fiveOaSelfCarApply"];

function  initApp() {

    var mobileApp = angular.module(appName, ['ui.router', 'ui.router.state.events','services.act','services.common'])
        .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,$sceDelegateProvider) {

            $locationProvider.html5Mode({
                enable: true,
                requireBase: false
            });
            $sceDelegateProvider.resourceUrlWhitelist([
                // Allow same origin resource loads.
                'self',
                // Allow loading from our assets domain.  Notice the difference between * and **.
                'https://owa.wuzhou.com.cn/**']);

            $stateProvider
                .state('dashboard', {
                    url: "/dashboard",
                    templateUrl: function () {
                        return "/h5/home/dashboard.html";
                    },
                    controller: 'DashboardController'
                });

            $stateProvider
                .state('task', {
                    url: "/task",
                    templateUrl: function () {
                        return "/h5/home/task.html";
                    },
                    controller: 'TaskController'
                });

            $stateProvider
                .state('project', {
                    url: "/project",
                    templateUrl: function () {
                        return "/h5/home/project.html";
                    },
                    controller: 'ProjectController'
                });

            $stateProvider
                .state('me', {
                    url: "/me",
                    templateUrl: function () {
                        return "/h5/home/me.html";
                    },
                    controller: 'MeController'
                });

            initApplyRoute($stateProvider);


            var ngIndex= window.localStorage.getItem("ngIndex");
            if(ngIndex){
                $urlRouterProvider.otherwise(ngIndex);
            }else {
                if (user.tenetId == "mcc") {
                    $urlRouterProvider.otherwise("task");
                } else {
                    $urlRouterProvider.otherwise("dashboard");
                }
            }
        });


    mobileApp.run(function ($rootScope, $state, $http) {
        $rootScope.user = user;
        $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
            var stateName = toState.name;
            var findIndex=-1;
            $(".mui-tab-item").each(function (index) {
                var url = $(this).attr("ui-sref");
                if (url == stateName) {
                    findIndex = index;
                    window.localStorage.setItem("ngIndex",url);
                }
            });

            if(findIndex>=0) {
                $(".mui-tab-item").each(function (index) {
                    if (findIndex === index) {
                        $(this).addClass("mui-active");
                    } else {
                        $(this).removeClass("mui-active");
                    }
                });
            }
        });
    });

    mobileApp

        .controller('BaseController', function ($rootScope,$scope,actTaskQueryService) {
            $rootScope.user=user;
            $rootScope.appName=appName;
            $rootScope.loadTaskCount=function () {
                actTaskQueryService.getTaskCount({enLogin:user.userId}).then(function (value) {
                    $rootScope.taskCount=value.data.data;
                })
            }
        })

        .controller('DashboardController', function ($rootScope,$scope) {
            document.title = "首页";
            $scope.loadTaskCount();
        })

        .controller('TaskController', function ($rootScope,$scope,actTaskQueryService,actProcessQueryService) {

            var size=10;

            $scope.qq="";

            $scope.init=function(){

                document.title = "我的任务";

                $scope.q=$scope.qq;

                $scope.tasks=[];

                $scope.initiatorList=[];

                $scope.involvedList=[];

                $scope.loadMoreTask();

                $scope.loadMoreInitiator();

                $scope.loadMoreInvolved();

                $scope.loadTaskCount();


                $(".mui-control-item").bind('click',function () {
                    var html=$(this).html();
                    var findIndex=0;
                    $(".mui-control-item").each(function (index) {
                        if($(this).html()==html){
                            findIndex=index;
                            $(this).addClass("mui-active");
                        }else{
                            $(this).removeClass("mui-active");
                        }
                    });

                    $(".mui-control-content").each(function (index) {
                        if(findIndex==index){
                            $(this).addClass("mui-active");
                        }else{
                            $(this).removeClass("mui-active");
                        }
                    });
                })

            }

            $scope.loadMoreTask = function () {
                actTaskQueryService.listTaskByH5(user.userId, $scope.q, $scope.tasks.length, size).then(function (value) {
                    if($scope.tasks.length>0){
                        mui.toast("加载待办任务成功");
                    }
                    var list = value.data.data.list;
                    for (var i = 0; i < list.length; i++) {
                        $scope.tasks.push(list[i]);
                    }
                    $scope.taskTotal = value.data.data.total;

                });
            }

            $scope.loadMoreInitiator = function () {
                actProcessQueryService.listProcessInstanceByH5(user.userId,"",$scope.q,$scope.initiatorList.length,size).then(function (value) {
                    if($scope.initiatorList.length>0){
                        mui.toast("加载发起的流程成功");
                    }

                    var list=value.data.data.list;
                    for(var i=0;i<list.length;i++) {
                        $scope.initiatorList.push(list[i]);
                    }
                    $scope.initiatorTotal=value.data.data.total;
                });
            }

            $scope.loadMoreInvolved = function () {
                actProcessQueryService.listProcessInstanceByH5("",user.userId,$scope.q,$scope.involvedList.length,size).then(function (value) {
                    if($scope.involvedList.length>0){
                        mui.toast("加载参与的流程成功");
                    }
                    var list=value.data.data.list;
                    for(var i=0;i<list.length;i++) {
                        $scope.involvedList.push(list[i]);
                    }
                    $scope.involvedCount=value.data.data.total;
                });
            }

            $scope.showTask = function (businessKey) {
                window.location.href = '/h5/taskDetail.html?businessKey=' + businessKey;
            }


            $scope.taskSearch=function(){
                if(event.keyCode==13){
                    $scope.init();
                }
            }

            $scope.init();

        })

        .controller('ProjectController', function ($rootScope,$scope) {
            document.title = "协同设计";
        })

        .controller('MeController', function ($rootScope,$scope) {
            document.title = "我";

            $scope.toggleUser=function () {
                window.localStorage.clear();
                window.location.href="/h5/login.html";
            }
        })

        .controller('ApplyListController', function ($rootScope,$scope,$stateParams,commonFormDataService) {
            var vm=this;
            var businessKey=$stateParams.businessKey;

            vm.newAable=jQuery.inArray(businessKey, openTypeList)>=0;


            vm.init=function () {
                commonFormDataService.listDataByUser(businessKey,"",user.enLogin).then(function (value) {
                    vm.data=value.data.data;
                    document.title=vm.data.template.formDesc;
                })
            }

            vm.newData=function() {
                mui.confirm("您确认发起新的申请吗?",function (result) {
                    if(result.index){
                        commonFormDataService.getNewModelByUser(businessKey, user.enLogin).then(function (value) {
                            if (value.data.ret) {
                                window.location.href="/h5/taskDetail.html?businessKey="+value.data.data.businessKey;
                            } else {
                                mui.alert(value.data.msg);
                            }
                        })
                    }
                })
            }

            vm.init();

            return vm;
        })

        .controller('ApplyDetailController', function ($rootScope,$scope,$stateParams,commonFormDataService) {
            var vm=this;
            var commonFormDataId=$stateParams.commonFormDataId;
            vm.init=function () {
                commonFormDataService.getModelById(commonFormDataId,user.enLogin).then(function (value) {
                    vm.data = value.data.data.data;
                    vm.groupList = value.data.data.groupList;
                });
            }
            vm.init();
            return vm;
        })

        .controller('ApplyStampOfficeController', function ($rootScope,$scope,$state,$stateParams,commonFormDataService,fiveOaStampApplyOfficeService2) {
            var vm=this;

            var uiSref="five.oaStampApplyOffice";
            vm.params = {noticeTitles:"",pageNum: 1, pageSize: $scope.pageSize,total:999};
            vm.pageInfo = {pageNum:  vm.params.pageNum, pageSize:8,total:vm.params.total};
            vm.init=function(){
                vm.loadPagedData();
            }

            vm.loadPagedData = function () {
                var params = {pageNum: vm.pageInfo.pageNum, pageSize: vm.pageInfo.pageSize,noticeTypes:vm.params.noticeTypes,uiSref:uiSref,userLogin:user.enLogin,
                    noticeTitles:vm.params.noticeTitles};
                fiveOaStampApplyOfficeService2.listPagedData(params).then(function (value) {
                    if (value.data.ret) {
                        vm.list = value.data.data.list;
                    }
                })
            };

            vm.show = function (id) {
                fiveOaStampApplyOfficeService2.getModelById(id).then(function (value) {
                    var businessKey = value.data.data.businessKey;
                    //$state.go("task.stampOfficeDetail", {businessKey: businessKey});
                    window.location.href="/h5/taskDetail.html?businessKey="+businessKey;
                })
            }

            vm.add = function () {
                fiveOaStampApplyOfficeService2.getNewModel(user.enLogin).then(function (value) {
                    if (value.data.ret) {
                        vm.show(value.data.data);
                    }
                })
            }

            vm.remove=function(id) {
                bootbox.confirm("您确定要删除吗?无法恢复,请谨慎操作!", function (result) {
                    if(result){
                        fiveOaStampApplyOfficeService2.remove(id,user.enLogin).then(function (value) {
                            if (value.data.ret){
                                toastr.success("删除成功!")
                                vm.loadPagedData();
                            }
                        });
                    }
                })
            }

            vm.init();

            return vm;

        })


}



var user = {};
angular.element(document).ready(function () {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/getUserBySession.json",
        success: function (data) {
            if (data.ret) {
                user = data.data;
                appName=user.tenetId+"H5App";
                initApp();
                angular.bootstrap(document, [appName]);
                window.localStorage.setItem("enLogin",user.enLogin);
            } else {
                window.localStorage.clear();
                window.location.href = "/h5/login.html";
            }
        }
    });
});



function initApplyRoute($stateProvider) {
    $stateProvider
        .state('apply', {
            url: "/apply",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('apply.list', {
            url: "/list?businessKey",
            templateUrl: function () {
                return "/h5/apply/list.html";
            },
            controller: 'ApplyListController as vm'
        })
        .state('apply.detail', {
            url: "/detail?commonFormDataId",
            templateUrl: function () {
                return "/h5/apply/detail.html";
            },
            controller: 'ApplyDetailController as vm'
        })

        .state('apply.stampOffice', {
            url: "/stampOffice?businessKey",
            templateUrl: function () {
                return "/h5/task/stampOffice.html";
            },
            controller: 'ApplyStampOfficeController as vm'
        });
}