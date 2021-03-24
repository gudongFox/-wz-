angular.module('services.five.hr', [])

    .factory('fiveHrQualifyApplyService', function ($q, $http) {
        var head="/five/hr/qualifyApply";

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

        var getNewModel = function (uiSref,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
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

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (qualifyApplyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listDetail.json',
                params: {
                    qualifyApplyId:qualifyApplyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var clearDetail = function (qualifyApplyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/clearDetail.json',
                params: {
                    qualifyApplyId:qualifyApplyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var initDetailList = function (qualifyApplyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/initDetailList.json',
                params: {
                    qualifyApplyId:qualifyApplyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var insertDetail = function (qualifyApplyId,userLoginList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertDetail.json',
                params: {
                    qualifyApplyId:qualifyApplyId,
                    userLoginList:userLoginList
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var toggleDetail = function (id,optType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/toggleDetail.json',
                params: {
                    id:id,
                    optType:optType
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (id,majorName,projectType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                params: {
                    id:id,
                    majorName:majorName,
                    projectType:projectType
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var copyDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/copyDetail.json',
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

        return {
            update:function(item){
                return update(item);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            getNewModel:function(uiSref,userLogin){
                return getNewModel(uiSref,userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listDetail:function(qualifyApplyId){
                return listDetail(qualifyApplyId);
            },
            initDetailList:function(qualifyApplyId){
                return initDetailList(qualifyApplyId);
            },
            clearDetail:function(qualifyApplyId){
                return clearDetail(qualifyApplyId);
            },
            insertDetail:function(qualifyApplyId,userLoginList){
                return insertDetail(qualifyApplyId,userLoginList)
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            toggleDetail:function (id,optType) {
                return toggleDetail(id,optType);
            },
            updateDetail:function (id,majorName,projectType) {
                return updateDetail(id,majorName,projectType);
            },
            copyDetail:function (id) {
                return copyDetail(id);
            }
        }
    })

    .factory('fiveHrApproveQualifyApplyService', function ($q, $http) {
        var head="/five/hr/approveQualifyApply";
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
                    userLogin:userLogin,
                    foreignKey:foreignKey
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
                url: head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailById.json',
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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/removeDetail.json',
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

        var getNewDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetail.json',
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
        var listDetailByQualifyId = function (qualifyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailByQualifyId.json',
                params: {
                    qualifyId:qualifyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downloadModel = function (modelId,pointId) {
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
                    modelId:modelId,
                    pointId:pointId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var updateApprove = function (item,qualifyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateApprove.json',
                data: item,
                params: {
                    qualifyId:qualifyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var changeApprove= function (detailId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/changeApprove.json',
                params: {
                    detailId:detailId
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
            getNewModel:function(foreignKey,userLogin){
                return getNewModel(foreignKey,userLogin);
            },
            listData:function (userLogin) {
                return listData(userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            updateDetail:function(item){
                return updateDetail(item);
            },
            getDetailById:function(id){
                return getDetailById(id);
            },
            removeDetail:function(id,userLogin){
                return removeDetail(id,userLogin);
            },
            getNewDetail:function(productId,userLogin){
                return getNewDetail(productId,userLogin);
            },
            listDetailByQualifyId: function (qualifyId) {
                return listDetailByQualifyId(qualifyId);
            },
            updateApprove:function(item,qualifyId){
                return updateApprove(item,qualifyId);
            },
            changeApprove:function(detailId){
                return changeApprove(detailId);
            },
            downloadModel:function(id){
                return downloadModel(id);
            },
        }
    })



    .factory('fiveHrQualifyChiefService', function ($q, $http) {
        var head="/five/hr/qualifyChief";
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
        var getNewModel = function (uiSref,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
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
        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listPagedData.json',
                params: params
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
        var downloadModel = function (modelId,pointId) {
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
                    modelId:modelId,
                    pointId:pointId
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
            getNewModel:function(uiSref,userLogin){
                return getNewModel(uiSref,userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData:function(id){
                return getPrintData(id);
            },
            downloadModel:function(id){
                return downloadModel(id);
            },
        }
    })

    .factory('fiveHrQualifyExternalService', function ($q, $http) {
        var head="/five/hr/qualifyExternal";
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
        var getNewModel = function (uiSref,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
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
        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listPagedData.json',
                params: params
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
        var downloadModel = function (modelId,pointId) {
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
                    modelId:modelId,
                    pointId:pointId
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
            getNewModel:function(uiSref,userLogin){
                return getNewModel(uiSref,userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData:function(id){
                return getPrintData(id);
            },
            downloadModel:function(id){
                return downloadModel(id);
            },
        }
    })


