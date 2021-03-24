angular.module('services.hr1', [])

    .factory('hrIncomeProofService', function ($q, $http) {
        var head="/hr/incomeProof";
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
        var getNewModel = function (foreignKey,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    foreignKey:foreignKey,
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
                url:  head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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
            getNewModel:function(foreignKey,userLogin){
                return getNewModel(foreignKey,userLogin);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrLeaveService', function ($q, $http) {
        var head="/hr/leave";
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
        var getNewModel = function (foreignKey,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    foreignKey:foreignKey,
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
                url:  head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(foreignKey,userLogin){
                return getNewModel(foreignKey,userLogin);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrVacationService', function ($q, $http) {
        var head="/hr/vacation";
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
        var getNewModel = function (foreignKey,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    foreignKey:foreignKey,
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
                url:  head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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
            getNewModel:function(foreignKey,userLogin){
                return getNewModel(foreignKey,userLogin);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrVacationDetailService', function ($q, $http) {
        var head="/hr/vacationDetail";
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
        var getNewModel = function (vacationId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    vacationId:vacationId,

                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByForeignKey = function (vacationId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:  head+'/listDataByForeignKey.json',
                params: {
                    vacationId:vacationId,

                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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
            getNewModel:function(vacationId){
                return getNewModel(vacationId);
            },
            listDataByForeignKey:function (vacationId) {
                return listDataByForeignKey(vacationId);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrDeptService', function ($q, $http) {
        var head = "/hr/dept";

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

        var listParentCandicate = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listParentCandicate.json',
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

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
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
                url: head + '/getNewModel.json',
                params: {
                    parentId: parentId
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
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByDeptIdList = function (deptIdList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDataByDeptIdList.json',
                params: {
                    deptIdList: deptIdList
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var selectByDeptIds = function (deptIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectByDeptIds.json',
                params: {
                    deptIds: deptIds,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectByDeptLeader = function (deptIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectByDeptLeader.json',
                params: {
                    deptIds: deptIds,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var selectDeptByDeptIds = function (deptIds, bigDept) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectDeptByDeptIds.json',
                params: {
                    deptIds: deptIds,
                    bigDept: bigDept
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectAllByUiSref = function (userLogin, uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAllByUiSref.json',
                params: {
                    userLogin: userLogin,
                    uiSref: uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listMultipleDept = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listMultipleDept.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var getDefaultDept = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDefaultDept.json',
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


        return {
            getDefaultDept: function (id) {
                return getDefaultDept(id);
            },
            listMultipleDept: function () {
                return listMultipleDept();
            },
            listParentCandicate: function (id) {
                return listParentCandicate(id);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (parentId) {
                return getNewModel(parentId);
            },
            selectAll: function (params) {
                return selectAll(params);
            },
            selectByDeptIds: function (deptIds) {
                return selectByDeptIds(deptIds);
            },
            selectByDeptLeader: function (deptIds) {
                return selectByDeptLeader(deptIds);
            },
            selectDeptByDeptIds: function (deptIds, bigDept) {
                return selectDeptByDeptIds(deptIds, bigDept);
            },
            selectAllByUiSref: function (userLogin, uiSref) {
                return selectAllByUiSref(userLogin, uiSref);
            },
            remove: function (id) {
                return remove(id);
            },
            listDataByDeptIdList: function (deptIdList) {
                return listDataByDeptIdList(deptIdList);
            }
        }
    })

    .factory('hrRegisterService', function ($q, $http) {
        var head="/hr/register";

        var getModelById = function (contractId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:contractId
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
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
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
        var listData = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listData.json',
                params: {
                    userLogin: userLogin
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
        var downloadModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
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

            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(contractId){
                return getModelById(contractId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listData:function (userLogin) {
                return listData(userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
        }
    })

    .factory('hrEmployeeProofService', function ($q, $http) {
        var head="/hr/employeeProof";
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
        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getPrintData.json',
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
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
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

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:  head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getPrintData:function(id){
            return getPrintData(id);
        },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrInsureService', function ($q, $http) {
        var head = "/hr/insure";
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
        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintData.json',
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

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin
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
        }

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
        }
    })




