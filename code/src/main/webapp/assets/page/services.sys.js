angular.module('services.sys', [])
    .factory('myActService', function ($q, $http) {
        var head="/myAct";

        var listHistoryTask = function (processInstanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listHistoryTask.json',
                params: {
                    processInstanceId:processInstanceId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getMyProcessInstance = function (processInstanceId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getMyProcessInstance.json',
                params: {
                    processInstanceId:processInstanceId,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var fetchFlow = function (processInstanceId,userLogin,comment) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/fetchFlow.json',
                params: {
                    processInstanceId:processInstanceId,
                    userLogin:userLogin,
                    comment:comment
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var sendFlow = function (taskId,users,userLogin,comment) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/sendFlow.json',
                params: {
                    taskId:taskId,
                    users:users,
                    userLogin:userLogin,
                    comment:comment
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var backFlow = function (taskId,users,userLogin,comment) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/backFlow.json',
                params: {
                    taskId:taskId,
                    users:users,
                    userLogin:userLogin,
                    comment:comment
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listTask=function (userLogin,processDefinitionName,description) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listTask.json',
                params: {
                    userLogin:userLogin,
                    processDefinitionName:processDefinitionName,
                    description:description
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listDoneTask=function (userLogin,pageNum,pageSize,processDefinitionName,description) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDoneTask.json',
                params: {
                    userLogin:userLogin,
                    pageNum:pageNum,
                    pageSize:pageSize,
                    processDefinitionName:processDefinitionName,
                    description:description
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listDoneEndTask=function (userLogin,pageNum,pageSize,processDefinitionName,description) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDoneEndTask.json',
                params: {
                    userLogin:userLogin,
                    pageNum:pageNum,
                    pageSize:pageSize,
                    processDefinitionName:processDefinitionName,
                    description:description
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listMyProcess=function (userLogin,pageNum,pageSize,finish,processDefinitionName,description) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listMyProcess.json',
                params: {
                    userLogin:userLogin,
                    pageNum:pageNum,
                    pageSize:pageSize,
                    finish:finish,
                    processDefinitionName:processDefinitionName,
                    description:description
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }



        var listNextStep = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listNextStep.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listBackStep = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listBackStep.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listTask:function (userLogin,processDefinitionName,description) {
                return listTask(userLogin,processDefinitionName,description);
            },
            listDoneTask:function (userLogin,pageNum,pageSize,processDefinitionName,description) {
                return listDoneTask(userLogin,pageNum,pageSize,processDefinitionName,description);
            },
            listDoneEndTask:function (userLogin,pageNum,pageSize,processDefinitionName,description) {
                return listDoneEndTask(userLogin,pageNum,pageSize,processDefinitionName,description);
            },
            listMyProcess:function (userLogin,pageNum,pageSize,finish,processDefinitionName,description) {
                return listMyProcess(userLogin,pageNum,pageSize,finish,processDefinitionName,description);
            },
            listHistoryTask: function (processInstanceId) {
                return listHistoryTask(processInstanceId);
            },
            getMyProcessInstance: function (processInstanceId,userLogin) {
                return getMyProcessInstance(processInstanceId,userLogin);
            },
            fetchFlow: function (processInstanceId,userLogin,comment) {
                return fetchFlow(processInstanceId,userLogin,comment);
            },
            sendFlow:function (taskId,users,userLogin,comment) {
                return sendFlow(taskId,users,userLogin,comment);
            },
            backFlow:function (taskId,users,userLogin,comment) {
                return backFlow(taskId,users,userLogin,comment);
            },
            listNextStep:function (params) {
                return listNextStep(params);
            },
            listBackStep:function (params) {
                return listBackStep(params);
            }
        }
    })

    .factory('actService', function ($q, $http) {

        var head="/act";



        var getEdStepTimeline = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getEdStepTimeline.json',
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

        var getExploreStepTimeline = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getExploreStepTimeline.json',
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

        var getProjectTimeline = function (projectId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getProjectTimeline.json',
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

        var getExploreTimeline = function (projectId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getExploreTimeline.json',
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

        var getNgRedirectUrl=function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNgRedirectUrl.json',
                params: {
                    businessKey:businessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getNgActRelevanceUrl=function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNgActRelevanceUrl.json',
                params: {
                    businessKey:businessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            getNgActRelevanceUrl: function (businessKey) {
                return getNgActRelevanceUrl(businessKey);
            },
            getNgRedirectUrl: function (businessKey) {
                return getNgRedirectUrl(businessKey);
            },
            getEdStepTimeline: function (stepId) {
                return getEdStepTimeline(stepId);
            },
            getExploreStepTimeline: function (stepId) {
                return getExploreStepTimeline(stepId);
            },
            getProjectTimeline: function (projectId) {
                return getProjectTimeline(projectId);
            },
            getExploreTimeline: function (projectId) {
                return getExploreTimeline(projectId);
            }
        }
    })

    .factory('sysRequestService', function ($q, $http) {

        var listPagedRequest = function (q,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/request/listPagedRequest.json',
                params: {
                    q:q,
                    pageNum:pageNum,
                    pageSize:pageSize
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSafetyData = function (q,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/request/listSafetyData.json',
                params: {
                    q:q,
                    pageNum:pageNum,
                    pageSize:pageSize
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSysAdminData = function (q,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/request/listSysAdminData.json',
                params: {
                    q:q,
                    pageNum:pageNum,
                    pageSize:pageSize
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAuditData = function (q,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/request/listAuditData.json',
                params: {
                    q:q,
                    pageNum:pageNum,
                    pageSize:pageSize
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listUserData = function (q,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/request/listUserData.json',
                params: {
                    q:q,
                    pageNum:pageNum,
                    pageSize:pageSize
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedRequest: function (q,pageNum,pageSize) {
                return listPagedRequest(q,pageNum,pageSize);
            },
            listAuditData: function (q,pageNum,pageSize) {
                return listAuditData(q,pageNum,pageSize);
            },
            listSysAdminData: function (q,pageNum,pageSize) {
                return listSysAdminData(q,pageNum,pageSize);
            },
            listSafetyData: function (q,pageNum,pageSize) {
                return listSafetyData(q,pageNum,pageSize);
            },
            listUserData: function (q,pageNum,pageSize) {
                return listUserData(q,pageNum,pageSize);
            }
        }
    })

    .factory('sysDeptService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/dept/getModelById.json',
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
                url: '/sys/dept/insert.json',
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
                url: '/sys/dept/update.json',
                data: item
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
                url: '/sys/dept/remove.json',
                params: {
                    deptId:id,
                    userLogin:userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (parentId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/dept/getNewModel.json',
                params: {
                    parentId:parentId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSortedAll = function (excludeDeptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/dept/listSortedAll.json',
                params:{
                    excludeDeptId:excludeDeptId
                }

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDeptAndSelect = function (selected) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/dept/listDeptAndSelect.json',
                params:{
                    selected:selected
                }

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
            removeDept:function(id,userLogin){
            return remove(id,userLogin);
        },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(parentId){
                return getNewModel(parentId);
            },
            listSortedAll:function (excludeDeptId) {
                return listSortedAll(excludeDeptId);
            },
            listDeptAndSelect:function (selected) {
            return listDeptAndSelect(selected);
        }
        }
    })

    .factory('sysUserService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/user/getModelById.json',
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
                url: '/sys/user/insert.json',
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
                url: '/sys/user/update.json',
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
                url: '/sys/user/getNewModel.json',
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
                url: '/sys/user/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var resetPwd = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/user/resetPwd.json',
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
        var batchAdd = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/user/batchAdd.json',
                params:{
                    id:id
                }

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
            resetPwd:function(id){
                return resetPwd(id);
            },
            batchAdd:function (id) {
                return batchAdd(id);
            },

        }
    })

    .factory('sysAclModuleService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/aclModule/getModelById.json',
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
                url: '/sys/aclModule/insert.json',
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
                url: '/sys/aclModule/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (parentId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/aclModule/getNewModel.json',
                params: {
                    parentId:parentId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSortedAll = function (excludeId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/aclModule/listSortedAll.json',
                params:{
                    excludeId:excludeId
                }

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
            getNewModel:function(parentId){
                return getNewModel(parentId);
            },
            listSortedAll:function (aclModule) {
                return listSortedAll(aclModule);
            }


        }
    })

    .factory('sysAclService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/acl/getModelById.json',
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
                url: '/sys/acl/insert.json',
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
                url: '/sys/acl/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (aclModuleId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/acl/getNewModel.json',
                params: {
                    aclModuleId:aclModuleId
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
                url: '/sys/acl/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listAclByModules = function (moduleId,q) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/acl/listAclByModules.json',
                params: {
                    moduleId:moduleId,
                    q:q
                }

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
            getNewModel:function(parentId){
                return getNewModel(parentId);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            listAclByModules:function (moduleId,q) {
                return listAclByModules(moduleId,q);
            }
        }
    })

    .factory('sysRoleService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getModelById.json',
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
                url: '/sys/role/insert.json',
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
                url: '/sys/role/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getNewModel.json'
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSortedAll = function (queryName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/listSortedAll.json',
                params:{
                    queryName:queryName
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSortedByRoleIds = function (roleIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/listSortedByRoleIds.json',
                params: {
                    roleIds:roleIds
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll=function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/selectAll.json'
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }


        var listAclByRoleId = function (q,roleId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/listAclByRoleId.json',
                params: {
                    q:q,
                    roleId:roleId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var deleteAcl = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/deleteAcl.json',
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

        var getAclTreeByRoleId = function (roleId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getAclTreeByRoleId.json',
                params: {
                    roleId:roleId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listMyAclInfoByUserLogin = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/listMyAclInfoByUserLogin.json',
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


        var getAclTreeByUserLogin = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getAclTreeByUserLogin.json',
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

        var saveUserAclDeptConfig = function (userLogin,aclId,selectDepts) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/saveUserAclDeptConfig.json',
                params: {
                    userLogin:userLogin,
                    aclId:aclId,
                    selectDepts:selectDepts
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var removeUserAcl = function (userLogin,aclId,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/removeUserAcl.json',
                params: {
                    userLogin:userLogin,
                    aclId:aclId,
                    type:type
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var saveRoleAclList = function (roleId,aclList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/saveRoleAclList.json',
                data: {
                    roleId:roleId,
                    aclList:aclList
                },
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                transformRequest: function(obj) {
                    var str = [];
                    for (var s in obj) {
                        str.push(encodeURIComponent(s) + "=" + encodeURIComponent(obj[s]));
                    }
                    return str.join("&");
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var saveUserAclList = function (userLogin,aclList) {

            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/saveUserAclList.json',
                data: {
                    userLogin:userLogin,
                    aclList:aclList
                },
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                transformRequest: function(obj) {
                    var str = [];
                    for (var s in obj) {
                        str.push(encodeURIComponent(s) + "=" + encodeURIComponent(obj[s]));
                    }
                    return str.join("&");
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var deleteUser = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/deleteUser.json',
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

        var getUserTree = function (roleId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getUserTree.json',
                params: {
                    roleId:roleId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var saveRoleUserList = function (roleId,userList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/saveRoleUserList.json',
                params: {
                    roleId:roleId,
                    userList:userList
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getAclInfoByUserLogin = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/role/getAclInfoByUserLogin.json',
                params: {
                    userLogin:userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            selectAll:function(){
                return selectAll();
            },
            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(parentId){
                return getNewModel(parentId);
            },
            listSortedAll:function (queryName) {
                return listSortedAll(queryName);
            },
            listSortedByRoleIds:function (roleIds) {
                return listSortedByRoleIds(roleIds);
            },
            listAclByRoleId:function (q,roleId) {
                return listAclByRoleId(q,roleId);
            },
            deleteAcl:function (id) {
                return deleteAcl(id);
            },
            getAclTreeByRoleId:function (roleId) {
                return getAclTreeByRoleId(roleId);
            },
            listMyAclInfoByUserLogin:function (userLogin) {
                return listMyAclInfoByUserLogin(userLogin);
            },
            saveUserAclDeptConfig:function (userLogin,aclId,selectDepts) {
                return saveUserAclDeptConfig(userLogin,aclId,selectDepts);
            },
            removeUserAcl:function(userLogin,aclId,type){
                return removeUserAcl(userLogin,aclId,type);
            },
            getAclTreeByUserLogin:function (userLogin) {
                return getAclTreeByUserLogin(userLogin);
            },
            saveRoleAclList:function (roleId,aclList) {
                return saveRoleAclList(roleId,aclList);
            },
            saveUserAclList:function (userLogin,aclList) {
                return saveUserAclList(userLogin,aclList);
            },
            deleteUser:function (id) {
                return deleteUser(id);
            },
            getUserTree:function (roleId) {
                return getUserTree(roleId);
            },
            saveRoleUserList:function (roleId,userList) {
                return saveRoleUserList(roleId,userList);
            },
            getAclInfoByUserLogin:function (userLogin,uiSref) {
                return getAclInfoByUserLogin(userLogin,uiSref);
            }
        }
    })

    .factory('sysAttachService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/attach/getModelById.json',
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
        var getModelByName = function (name) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/attach/getModelByName.json',
                params: {
                    name:name
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var download=function (id) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: '/sys/attach/download/'+id,
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                }
            }).then(function successCallback(response) {
                deferred.resolve(response);
            });
            return deferred.promise;
        }

        var downloadEdFileXml=function (id) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: '/sys/attach/downloadEdFileXml/'+id,
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                }
            }).then(function successCallback(response) {
                deferred.resolve(response);
            });
            return deferred.promise;
        }

        var downloadEdFile=function (id) {
            var deferred = $q.defer();
            $http({
                method: 'POST',
                url: '/sys/attach/downloadEdFile/'+id,
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                }
            }).then(function successCallback(response) {
                deferred.resolve(response);
            });
            return deferred.promise;
        }



        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/attach/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            download:function (id) {
                return download(id);
            },
            downloadEdFileXml:function (id) {
                return downloadEdFileXml(id);
            },
            downloadEdFile:function (id) {
                return downloadEdFile(id);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getModelByName:function(name){
                return getModelByName(name);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            }
        }
    })

    .factory('sysCodeService', function ($q, $http) {

        var head = "/sys/code";

        var listDataByCatalog = function (catalog,selected) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByCatalog.json',
                params: {
                    catalog: catalog,
                    selected:selected
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listAllStage = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllStage.json'

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAllMajor = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllMajor.json'

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAllMajorName = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listAllMajorName.json'

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        var selectAll = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json'
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

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelById.json',
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

        var remove = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (codeCatalog) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params:{
                    codeCatalog:codeCatalog
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listCategory = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCategory.json'

            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listDataByCodeAndCatalog = function (codeValue,codeCatalog) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByCodeAndCatalog.json',
                params:{
                    codeValue:codeValue,
                    codeCatalog:codeCatalog,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listDataByCatalog:function (catalog,selected) {
                return listDataByCatalog(catalog,selected);
            },
            selectAll:function () {
                return selectAll();
            },
            listAllMajor:function () {
                return listAllMajor();
            },
            listAllMajorName:function () {
                return listAllMajorName();
            },
            listAllStage:function () {
                return listAllStage();
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            update:function (item) {
                return update(item);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove:function (id) {
                return remove(id);
            },
            getNewModel:function (codeCatalog) {
                return getNewModel(codeCatalog);
            },
            listCategory:function () {
                return listCategory();
            },
            listDataByCodeAndCatalog:function (codeValue,codeCatalog) {
            return listDataByCodeAndCatalog(codeValue,codeCatalog);
        },
        }

    })

    .factory('sysPluginService', function ($q, $http) {

    var head="/sys/plugin";

    var getNewModel = function (userLogin) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head+ '/getNewModel.json',
            params:{
                userLogin:userLogin
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
            url:head+ '/update.json',
            data: item
        }).success(function (data) {
            deferred.resolve(data);
        }).error(function (error) {
            deferred.reject(error);
        });
        return deferred.promise;
    };


    var getModelById = function (id) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head+ '/getModelById.json',
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

    var remove = function (id) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head+ '/remove.json',
            params: {
                id:id,
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


    return {
        listPagedData: function (params) {
            return listPagedData(params);
        },
        getModelById:function (id) {
            return getModelById(id);
        },
        remove : function (id) {
            return remove(id);
        },
        getNewModel:function (userLogin) {
            return getNewModel(userLogin);
        },
        update:function (item) {
            return update(item);
        }
    }
})

    .factory('sysEdQuestionService', function ($q, $http) {

        var head="/sys/edQuestion";

        var addEdQuestion = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
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

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelById.json',
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

        var remove = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
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


        return {
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove : function (id) {
                return remove(id);
            },
            add:function (userLogin) {
                return addEdQuestion(userLogin);
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('sysDwgPictureService', function ($q, $http) {

        var head="/sys/dwgPicture";

        var getNewModel = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json'
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
                url:head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelById.json',
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

        var remove = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
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


        return {
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove : function (id) {
                return remove(id);
            },
            getNewModel:function () {
                return getNewModel();
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('sysConfigService', function ($q, $http) {

        var getConfig = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/config/getConfig.json'
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
                url: '/sys/config/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            getConfig : function () {
                return getConfig();
            },
            update:function(item){
                return update(item);
            }
        }
    })

    .factory('sysSoftwareService', function ($q, $http) {

        var head="/sys/software";

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params:{
                    userLogin:userLogin
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
                url:head+ '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelById.json',
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

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
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


        return {
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove : function (id,userLogin) {
                return remove(id,userLogin);
            },
            getNewModel:function (userLogin) {
                return getNewModel(userLogin);
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('sysScheduleService', function ($q, $http) {

        var head="/sys/schedule";

        var selectAll = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/selectAll.json',
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
        var insert = function (start,end,type,title,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/insert.json',
                params: {
                    start:start,
                    end:end,
                    type:type,
                    title:title,
                    userLogin:userLogin
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
                url: head+ '/update.json',
                data: item
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
                url: head+ '/remove.json',
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

        return {
           selectAll : function (userLogin) {
               return selectAll(userLogin)
           },
            insert: function (start,end,type,title,userLogin) {
                return insert(start,end,type,title,userLogin)
            },
            update : function (item) {
                return update(item)
            },
            remove : function (id,userLogin) {
                return remove(id,userLogin);
            },
        }
    })

    .factory('fiveCommonCodeService', function ($q, $http) {

        var head="/fiveCommonCode";

        var listDataByCodeAndCodeCatalog = function (code,codeCatalog) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDataByCodeAndCodeCatalog.json',
                params: {
                    code:code,
                    codeCatalog:codeCatalog
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listAllMajorName = function (code,codeCatalog) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listAllMajorName.json',
                params: {
                    code:code,
                    codeCatalog:codeCatalog
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            listDataByCodeAndCodeCatalog : function (code,codeCatalog) {
                return listDataByCodeAndCodeCatalog(code,codeCatalog);
            },

            listAllMajorName : function () {
                return listAllMajorName();
            },
        }
    })

    .factory('fiveContentFileService', function ($q, $http) {

        var head = "/wuzhou/file";

        var getModelByBusinessKey = function (businessKey,fileType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                    fileType:fileType,
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
                url: head + '/remove.json',
                params: {
                    id: id,
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
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadContent =function (businessKey) {
            var deferred = $q.defer();
            console.log(1)
            return $http({
                method: 'POST',
                /*url: head + '/downloadContent/{businessKey}',*/
                /*url: head + '/downloadContent'+businessKey,*/
                url: head + '/downloadContent/'+businessKey,
                params: {
                    businessKey: businessKey
                }
            }).success(function (data) {
                console.log(2)
                deferred.resolve(data);
            }).error(function (error) {
                console.log(3)
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            getModelByBusinessKey: function (businessKey,fileType) {
                return getModelByBusinessKey(businessKey,fileType);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove: function (id) {
                return remove(id);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            downloadContent:function (businessKey) {
                return downloadContent(businessKey);
            },
        }
    })

    .factory('fiveOaWordSizeService', function ($q, $http) {

        var head = "/sys/wordSize";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getCanUseWord = function (keyNames,year,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getCanUseWord.json',
                params: {
                    keyNames:keyNames,
                    year:year,
                    type:type,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateWordSize = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateWordSize.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getMarkByChange = function (word,year) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getMarkByChange.json',
                params: {
                    word: word,
                    year:year,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listUserWord = function (word,year,key) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listUserWord.json',
                params: {
                    word: word,
                    year:year,
                    key:key,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeWordByBusinessKey = function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeWordByBusinessKey.json',
                params: {
                    businessKey: businessKey,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listItem = function (advanceId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listItem.json',
                params: {
                    advanceId: advanceId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelItem = function (word) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelItem.json',
                params: {
                    word:word,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelItemById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelItemById.json',
                params: {
                    id: id
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
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            getCanUseWord: function (keyNames,year,type) {
                return getCanUseWord(keyNames,year,type);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            updateWordSize: function (params) {
                return updateWordSize(params);
            },
            getMarkByChange: function (word,year) {
                return getMarkByChange(word,year);
            },
            listUserWord: function (word,year,key) {
                return listUserWord(word,year,key);
            },
            removeWordByBusinessKey: function (businessKey){
                return removeWordByBusinessKey(businessKey);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelItemById:function (id) {
                return getModelItemById(id);
            },
            listItem: function (params) {
                return listItem(params);
            },
            getNewModelItem: function (word) {
                return getNewModelItem(word);
            },

        }
    })



