


angular.module('services.ed', [])


    .factory('edFileService', function ($q, $http) {

        var head="/ed/file";

        var updateFileType = function (id,fileType,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateFileType.json',
                params: {
                    id:id,
                    fileType:fileType,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listData = function (businessId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listData.json',
                params: {
                    businessId:businessId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var fileCheckByBusinessKey = function (businessId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/fileCheckByBusinessKey.json',
                params: {
                    businessId:businessId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var showVersion = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listFileHistory.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var recoverFile = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/recoverFile.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDeletedFile = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDeletedFile.json',
                params: params,
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        return {
            listData: function (businessId) {
                return listData(businessId);
            },
            fileCheckByBusinessKey: function (businessId) {
                return fileCheckByBusinessKey(businessId);
            },
            remove:function (id,userLogin) {
                return remove(id,userLogin);
            },
            updateFileType:function (id,fileType,userLogin) {
                return updateFileType(id,fileType,userLogin);
            },
            showVersion: function (id) {
                return showVersion(id)
            },
            recoverFile: function (id) {
                return recoverFile(id)
            },
            listDeletedFile: function (params) {
                return listDeletedFile(params)
            }
        }
    })

    .factory('edProjectTreeService', function ($q, $http) {

        var head="/ed/projectTree";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var genProjectTree = function (projectId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/genProjectTree.json',
                params: {
                    projectId:projectId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var listProjectTree = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listProjectTree.json',
                params: {
                    projectId:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var rememberNodeState = function (id,value,stateType,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/rememberNodeState.json',
                params: {
                    id:id,
                    value:value,
                    stateType:stateType,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listProjectJsTree = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listProjectJsTree.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            getModelById:function(id){
                return getModelById(id);
            },
            genProjectTree:function(projectId){
                return genProjectTree(projectId);
            },
            listProjectTree: function (id,userLogin) {
                return listProjectTree(id,userLogin);
            },
            listProjectJsTree: function (params) {
                return listProjectJsTree(params);
            },
            rememberNodeState:function (id,value,stateType,userLogin) {
                return rememberNodeState(id,value,stateType,userLogin);
            },

        }
    })

    .factory('edProjectService', function ($q, $http) {

        var head="/ed/project";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insert.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (deptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    deptId:deptId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAttendProject = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listAttendProject.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(deptId){
                return getNewModel(deptId);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listAttendProject: function (params) {
                return listAttendProject(params);
            }
        }
    })

    .factory('edProjectStepService', function ($q, $http) {

        var head="/ed/projectStep";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (stageNodeId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    stageNodeId:stageNodeId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var toggleCadHide = function (stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/toggleCadHide.json',
                params: {
                    stepId:stepId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/remove.json',
                params: {
                    id:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStageNodeId = function (stageNodeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDataByStageNodeId.json',
                params: {
                    stageNodeId:stageNodeId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listCpPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listCpPagedData.json',
                params:params

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listPagedAttendStep = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedAttendStep.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listPagedAllStep = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listPagedAllStep.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var checkIsChargeUser = function (id,stepId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/checkIsChargeUser.json',
                params: {
                    id:id,
                    stepId:stepId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listAllStep = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listAllStep.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listAllStepByDeptId = function (deptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listAllStepByDeptId.json',
                params: {
                    deptId:deptId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listCpData = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listCpData.json',
                params: {
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update:function(item){
                return update(item);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(stageNodeId,userLogin){
                return getNewModel(stageNodeId,userLogin);
            },
            listDataByStageNodeId: function (stageNodeId) {
                return listDataByStageNodeId(stageNodeId);
            },
            listCpPagedData: function (params) {
                return listCpPagedData(params);
            },
            listPagedAttendStep:function (params) {
                return listPagedAttendStep(params);
            },
            listPagedAllStep:function (params) {
                return listPagedAllStep(params);
            },
            checkIsChargeUser:function(id,stepId,userLogin){
                return checkIsChargeUser(id,stepId,userLogin);
            },
            toggleCadHide:function(stepId,userLogin){
                return toggleCadHide(stepId,userLogin);
            },
            listAllStep:function(item){
                return listAllStep(item);
            },
            listAllStepByDeptId:function(deptId){
                return listAllStepByDeptId(deptId);
            },
            listCpData:function(userLogin){
                return listCpData(userLogin);
            }
        }
    })

    .factory('edStepBuildService', function ($q, $http) {

        var head="/ed/stepBuild";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };




        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    stepId:stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/remove.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByStepId = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDataByStepId.json',
                params: {
                    stepId:stepId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            update:function(item){
                return update(item);
            },
            remove:function(id){
                return remove(id);
            },
            getNewModel:function(id){
                return getNewModel(id);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(stepId){
                return getNewModel(stepId);
            },
            listDataByStepId: function (stepId) {
                return listDataByStepId(stepId);
            }
        }
    })

