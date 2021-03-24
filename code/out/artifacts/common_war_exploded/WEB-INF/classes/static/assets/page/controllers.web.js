angular.module('controllers.web', [])

    .controller('DashboardController',function ($state,$stateParams,$scope){

        var vm=this;



        return vm;
    })


    .controller('FootController1',function ($state,$stateParams,$scope,$rootScope,commonFormDataService){

        var vm=this;


        $rootScope.getTplConfig=function (processInstanceId,businessKey,enLogin) {
            commonFormDataService.getTplConfig(processInstanceId, businessKey, enLogin).then(function (value) {
                $rootScope.tplConfig = value.data.data;
            });
        }


        return vm;
    })


    .controller('CadDirController',function ($state,$stateParams,$scope,commonDirService,commonFileService ){
        var id=$stateParams.id

        $scope.loadData=function() {
            commonDirService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
            })
            commonFileService.listData("",id).then(function (value) {
                $scope.files = value.data.data;
            });
        }

        $scope.save=function (){
            if($scope.item.seq===undefined){
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }
            commonDirService.save($scope.item.id, $scope.item.cnName, $scope.item.seq, $scope.item.remark, user.enLogin).then(function (value) {
                if (value.data.ret) {
                    toastr.success("保存成功");
                    $scope.loadData();
                }
            })
        }

        $scope.loadData();

    })
    .controller('CadFileController',function ($state,$stateParams,$scope,commonFileService,commonFormDataService){
        var id=$stateParams.id;

        $scope.loadData=function() {
            commonFileService.getModelById(id).then(function (value) {
                $scope.item = value.data.data;
                commonFormDataService.listFileType($scope.item.businessKey).then(function (value) {
                    $scope.fileTypes = value.data.data;
                })
                $scope.loadHistory();
            })
        }

        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }

        $scope.removeHistory=function (attachId) {
            bootbox.confirm("确认要删除该版本吗?",function (result) {
                if(result){
                    commonFileService.removeHistory($scope.item.id,attachId,user.enLogin).then(function (value) {
                        if(value.data.ret){
                            $scope.loadHistory();
                            toastr.success("删除成功");
                        }
                    });
                }
            })
        }

        $scope.save=function (){
            if( $scope.item.seq===undefined){
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }
            commonFileService.save( $scope.item.id, $scope.item.fileName, $scope.item.fileType, $scope.item.seq,
                $scope.item.remark,user.enLogin).then(function (value) {
                if(value.data.ret){
                    toastr.success("保存成功");
                    $scope.loadData();
                }
            })
        }

        $scope.loadHistory=function(){
            commonFileService.listHistory($scope.item.id).then(function (value) {
                $scope.historyList=value.data.data;
            })
        }

        $scope.loadData();

    })

    .controller('CadTaskController',function ($http,$state,$stateParams,$scope,commonFormDataService){
        $scope.businessKey=$stateParams.businessKey;

        $http.post('/getNgDirectUrl.json?businessKey='+$scope.businessKey).then(function(value){
            if(value.data.ret){
                var result=value.data.data;
                $state.go(result.url,result.params);
            }
        });

    });

