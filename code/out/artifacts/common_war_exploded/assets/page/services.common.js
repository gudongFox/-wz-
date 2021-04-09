angular.module('services.common', [])


    .factory('commonFormDataService', function ($q, $http) {
        var listData = function (referType,referId,q,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listData.json',
                params: {
                    referType:referType,
                    referId:referId,
                    q:q,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByUser = function (referType,q,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listDataByUser.json',
                params: {
                    referType:referType,
                    enLogin:enLogin,
                    q:q
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
                url: '/common/formData/listPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listActPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listActPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelById = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getModelById.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getFormDataById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getFormDataById.json',
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


        var getModelByBusinessKey = function (businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getModelByBusinessKey.json',
                params: {
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (referType,referId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getNewModel.json',
                params: {
                    referType:referType,
                    referId:referId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelByUser = function (referType,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getNewModelByUser.json',
                params: {
                    referType:referType,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var save = function (data) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/save.json',
                data:data
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getTplConfig = function (processInstanceId,businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/getTplConfig.json',
                params: {
                    processInstanceId:processInstanceId,
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listNewPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listNewData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listFileType = function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/formData/listFileType.json',
                params: {
                    businessKey:businessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            listData:function (referType,referId,q,enLogin) {
                return listData(referType,referId,q,enLogin);
            },
            listDataByUser:function (referType,q,enLogin) {
                return listDataByUser(referType,q,enLogin);
            },
            listPagedData:function (params) {
                return listPagedData(params);
            },
            listActPagedData:function (params) {
                return listActPagedData(params);
            },
            listNewPagedData:function (params) {
                return listNewPagedData(params);
            },
            listNewData:function (params) {
                return listNewData(params);
            },
            getModelById:function (id,enLogin) {
                return getModelById(id,enLogin);
            },
            getFormDataById:function (id) {
                return getFormDataById(id);
            },
            getModelByBusinessKey:function(businessKey,enLogin){
                return getModelByBusinessKey(businessKey,enLogin);
            },
            getNewModel:function(referType,referId,enLogin){
                return getNewModel(referType,referId,enLogin);
            },
            getNewModelByUser:function(referType,enLogin){
                return getNewModelByUser(referType,enLogin);
            },
            save:function (data) {
                return save(data);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            getTplConfig:function (processInstanceId,businessKey,enLogin) {
                return getTplConfig(processInstanceId,businessKey,enLogin);
            },
            listFileType:function (businessKey) {
                return listFileType(businessKey);
            }
        }
    })

    .factory('commonEdArrangeUserService', function ($q, $http) {

        var listData = function (businessKey,buildBusinessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangeUser/listData.json',
                params: {
                    businessKey:businessKey,
                    buildBusinessKey:buildBusinessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var setUser = function (arrangeId,roleName,roleCode,allUser) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangeUser/setUser.json',
                params: {
                    arrangeId:arrangeId,
                    roleName:roleName,
                    roleCode:roleCode,
                    allUser:allUser
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var buildCoDirData = function (stepId,buildBusinessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangeUser/buildCoDirData.json',
                params: {
                    stepId:stepId,
                    buildBusinessKey:buildBusinessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var initDataFromEdTaskWz = function (arrangeFormDataId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangeUser/initDataFromEdTaskWz.json',
                params: {
                    arrangeFormDataId:arrangeFormDataId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDefaultArrangeBusinessKey = function (stepId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangeUser/getDefaultArrangeBusinessKey.json',
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
            listData:function (businessKey,buildBusinessKey) {
                return listData(businessKey,buildBusinessKey);
            },
            setUser:function (arrangeId,roleName,roleCode,allUser) {
                return setUser(arrangeId,roleName,roleCode,allUser);
            },
            buildCoDirData:function(stepId,buildBusinessKey){
                return buildCoDirData(stepId,buildBusinessKey);
            },
            initDataFromEdTaskWz:function (arrangeFormDataId) {
                return initDataFromEdTaskWz(arrangeFormDataId);
            },
            getDefaultArrangeBusinessKey:function(stepId){
                return getDefaultArrangeBusinessKey(stepId);
            }
        }
    })

    .factory('commonEdArrangePlanService', function ($q, $http) {
        var listData = function (businessKey,buildBusinessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangePlan/listData.json',
                params: {
                    businessKey:businessKey,
                    buildBusinessKey:buildBusinessKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var save = function (id,planName,planArea,planDesc) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangePlan/save.json',
                params: {
                    id:id,
                    planName:planName,
                    planArea:planArea,
                    planDesc:planDesc
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var setSeq = function (id,up) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangePlan/setSeq.json',
                params: {
                    id:id,
                    up:up
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (businessKey,buildBusinessKey,buildName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangePlan/getNewModel.json',
                params: {
                    businessKey:businessKey,
                    buildBusinessKey:buildBusinessKey,
                    buildName:buildName
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edArrangePlan/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            listData:function (businessKey,buildBusinessKey) {
                return listData(businessKey,buildBusinessKey);
            },
            save:function (id,planName,planArea,planDesc) {
                return save(id,planName,planArea,planDesc);
            },
            getNewModel:function(businessKey,buildBusinessKey,buildName){
                return getNewModel(businessKey,buildBusinessKey,buildName);
            },
            setSeq:function (id,up) {
                return setSeq(id,up);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            }
        }
    })

    .factory('commonEdBuildService', function ($q, $http) {
        var listData = function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edBuild/listData.json',
                params: {
                    businessKey:businessKey
                }
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
                url: '/common/edBuild/getModelById.json',
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

        var getNewModel = function (businessKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edBuild/getNewModel.json',
                params: {
                    businessKey:businessKey,
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
                url: '/common/edBuild/remove.json',
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

        var save = function (data) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edBuild/save.json',
                data:data
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listData:function (businessKey) {
                return listData(businessKey);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            getNewModel:function(businessKey){
                return getNewModel(businessKey);
            },
            save:function (data) {
                return save(data);
            },
            remove:function (id) {
                return remove(id);
            }
        }
    })

    .factory('commonFileService', function ($q, $http) {

        var listData = function (businessKey,dirId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listData.json',
                params: {
                    businessKey:businessKey,
                    dirId:dirId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataCount = function (businessKey,dirId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listDataCount.json',
                params: {
                    businessKey:businessKey,
                    dirId:dirId
                }
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
                url: '/common/file/getModelById.json',
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

        var remove=function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var save=function (id,fileName,fileType,seq,remark,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/save.json',
                params: {
                    id:id,
                    fileName:fileName,
                    fileType:fileType,
                    seq:seq,
                    remark:remark,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var removeHistory=function (id,attachId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/removeHistory.json',
                params: {
                    id:id,
                    attachId:attachId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listHistory=function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listHistory.json',
                params: {
                    id:id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listFileType=function (tenetId,fileTypeCode) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listFileType.json',
                params: {
                    tenetId:tenetId,
                    fileTypeCode:fileTypeCode
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var download = function (url,params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: url,
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var transferCoToEd=function (coFiles,destBusinessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/transferCoToEd.json',
                params: {
                    destBusinessKey:destBusinessKey,
                    enLogin:enLogin
                },
                data:coFiles
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var listCoPublishFile=function (businessKey,parentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listCoPublishFile.json',
                params: {
                    businessKey:businessKey,
                    parentId:parentId,
                    enLogin:enLogin
                },
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var listCoPublishDir=function (businessKey,parentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listCoPublishDir.json',
                params: {
                    businessKey:businessKey,
                    parentId:parentId,
                    enLogin:enLogin
                },
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertCoPublish=function (businessKey,parentId,dirIds,fileIds,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/listCoPublishDir.json',
                params: {
                    businessKey:businessKey,
                    parentId:parentId,
                    dirIds:dirIds,
                    fileIds:fileIds,
                    enLogin:enLogin
                },
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            getModelById:function (id) {
                return getModelById(id);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            listData:function (businessKey,dirId) {
                return listData(businessKey,dirId);
            },
            listDataCount:function (businessKey,dirId) {
                return listDataCount(businessKey,dirId);
            },
            save:function (id,fileName,fileType,seq,remark,enLogin) {
                return save(id,fileName,fileType,seq,remark,enLogin);
            },
            removeHistory:function (id,attachId,enLogin) {
                return removeHistory(id,attachId,enLogin);
            },
            listHistory:function (id) {
                return listHistory(id);
            },
            listFileType:function (tenetId,fileTypeCode) {
                return listFileType(tenetId,fileTypeCode);
            },
            download:function(url,params){
                return download(url,params);
            },
            transferCoToEd:function(coFiles,destBusinessKey,enLogin){
                return transferCoToEd(coFiles,destBusinessKey,enLogin);
            },
            listCoPublishFile:function(businessKey,parentId,enLogin){
                return listCoPublishFile(businessKey,parentId,enLogin);
            },
            listCoPublishDir:function(businessKey,parentId,enLogin){
                return listCoPublishDir(businessKey,parentId,enLogin);
            },
            insertCoPublish:function(businessKey,parentId,dirIds,fileIds, enLogin){
                return insertCoPublish(businessKey,parentId,dirIds,fileIds, enLogin);
            },
        }
    })

    .factory('commonFileCoService', function ($q, $http) {

        var listSelectDir = function (businessKey,coParentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/co/listSelectDir.json',
                params: {
                    businessKey:businessKey,
                    coParentId:coParentId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSelectFile = function (businessKey,coParentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/co/listSelectFile.json',
                params: {
                    businessKey:businessKey,
                    coParentId:coParentId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listBreadcrumb = function (businessKey,coParentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/co/listBreadcrumb.json',
                params: {
                    businessKey:businessKey,
                    coParentId:coParentId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var insertSelect=function (businessKey,coParentId,coDirIds,coFileIds,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/file/co/insertSelect.json',
                params: {
                    businessKey:businessKey,
                    coParentId:coParentId,
                    coDirIds:coDirIds,
                    coFileIds:coFileIds,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            listSelectDir:function(businessKey,coParentId,enLogin){
                return listSelectDir(businessKey,coParentId,enLogin);
            },
            listSelectFile:function(businessKey,coParentId,enLogin){
                return listSelectFile(businessKey,coParentId,enLogin);
            },
            listBreadcrumb:function(businessKey,coParentId,enLogin){
                return listBreadcrumb(businessKey,coParentId,enLogin);
            },
            insertSelect:function(businessKey,coParentId,coDirIds,coFileIds,enLogin){
                return insertSelect(businessKey,coParentId,coDirIds,coFileIds,enLogin);
            }
        }
    })

    .factory('commonDirService', function ($q, $http) {


        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/getModelById.json',
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


        var listData = function (businessKey,parentId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/listData.json',
                params: {
                    businessKey:businessKey,
                    parentId:parentId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var removeMore = function (ids,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/removeMore.json',
                params: {
                    ids:ids,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var newDir=function (businessKey,dirName,parentId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/newDir.json',
                params: {
                    businessKey:businessKey,
                    dirName:dirName,
                    parentId:parentId,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }


        var listBreadcrumb = function (businessKey,dirId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/listBreadcrumb.json',
                params: {
                    businessKey:businessKey,
                    dirId:dirId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var save=function (id,dirName,seq,remark,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/dir/save.json',
                params: {
                    id:id,
                    dirName:dirName,
                    seq:seq,
                    remark:remark,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }


        return {
            getModelById:function (id) {
                return getModelById(id);
            },
            listData:function (businessKey,parentId) {
                return listData(businessKey,parentId);
            },
            listBreadcrumb:function(businessKey,dirId){
                return listBreadcrumb(businessKey,dirId);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            removeMore:function (ids,enLogin) {
                return removeMore(ids,enLogin);
            },
            newDir:function (businessKey,dirName,parentId,enLogin) {
                return newDir(businessKey,dirName,parentId,enLogin);
            },
            save:function (id,dirName,seq,remark,enLogin) {
                return save(id,dirName,seq,remark,enLogin);
            }

        }
    })

    .factory('commonEdMarkService', function ($q, $http) {

        var listData = function (businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/listData.json',
                params: {
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/getNewModel.json',
                params: {
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelById = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/getModelById.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var save = function (data) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/save.json',
                data: data
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var setColor = function (id,questionColor, enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/setColor.json',
                params: {
                    id:id,
                    questionColor:questionColor,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var setAnswer = function (id,questionAnswer,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/setAnswer.json',
                params: {
                    id:id,
                    questionAnswer:questionAnswer,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByFileId = function (fileId,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/listDataByFileId.json',
                params: {
                    fileId:fileId,
                    onlyCloud:false,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downloadMark = function (businessKey,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edMark/downloadMark.json',
                responseType: "blob",
                transformResponse: function(data, headers,cc){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    businessKey:businessKey,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listData:function (businessKey,enLogin) {
                return listData(businessKey,enLogin);
            },
            getModelById:function (id,enLogin) {
                return getModelById(id,enLogin);
            },
            getNewModel:function (businessKey,enLogin) {
                return getNewModel(businessKey,enLogin);
            },
            save:function (data) {
                return save(data);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            setColor:function (id,questionColor,enLogin) {
                return setColor(id,questionColor,enLogin);
            },
            setAnswer:function (id,questionAnswer,enLogin) {
                return setAnswer(id,questionAnswer,enLogin);
            },
            listDataByFileId:function (fileId,enLogin) {
                return listDataByFileId(fileId,enLogin);
            },
            downloadMark:function (businessKey,enLogin) {
                return downloadMark(businessKey,enLogin);
            }
        }
    })

    .factory('commonEdQuestionService', function ($q, $http) {

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edQuestion/listPagedData.json',
                params:params
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
                url: '/common/edQuestion/getModelById.json',
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

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edQuestion/getNewModel.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var save = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edQuestion/save.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/edQuestion/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedData:function (params) {
                return listPagedData(params);
            },
            getModelById:function (enLogin) {
                return getModelById(enLogin);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            save:function (item) {
                return save(item);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            }

        }
    })

    .factory('commonEdDwgPictureService', function ($q, $http) {

        var head="/common/edDwgPicture";

        var getNewModelLast = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModelLast.json',
                params: {
                    enLogin:enLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    enLogin:enLogin,
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
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            getNewModelLast:function (enLogin) {
                return getNewModelLast(enLogin);
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('commonEdDwgStdService', function ($q, $http) {

        var head="/common/edDwgStd";

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    enLogin:enLogin,
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
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('commonEdStampService', function ($q, $http) {

        var head="/common/edStamp";

        var getNewModel = function (tenetId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    tenetId:tenetId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var save = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+ '/save.json',
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

        var selectAll = function (tenetId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/selectAll.json',
                params: {
                    tenetId:tenetId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
            selectAll:function (tenetId) {
                return selectAll(tenetId);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove : function (id) {
                return remove(id);
            },
            getNewModel:function (tenetId) {
                return getNewModel(tenetId);
            },
            save:function (item) {
                return save(item);
            }
        }
    })

    .factory('commonEdLayerExtractionService', function ($q, $http) {

        var head="/common/edLayerExtraction";

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    enLogin:enLogin,
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
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            update:function (item) {
                return update(item);
            }
        }
    })

    .factory('commonCadPluginService', function ($q, $http) {

        var head="/common/cadPlugin";

        var getLatest = function (enLogin) {
            var deferred = $q.defer(enLogin);
            return $http({
                method: 'POST',
                url: head+ '/getLatest.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    enLogin:enLogin,
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
            getLatest: function (enLogin) {
                return getLatest(enLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove : function (id) {
                return remove(id);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            update:function (item) {
                return update(item);
            }
        }

    })

    .factory('commonFormService', function ($q, $http) {

        var head="/common/form";

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModel.json',
                params: {
                    enLogin:enLogin,
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


        var getModelByKey = function (tenetId,formKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelByKey.json',
                params: {
                    tenetId:tenetId,
                    formKey:formKey
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
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

        var removeDetail = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/removeDetail.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getModelDetailById.json',
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

        var getNewModelDetail = function (formId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/getNewModelDetail.json',
                params: {
                    formId:formId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (detail) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:head+ '/updateDetail.json',
                data: detail
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (formId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetail.json',
                params: {
                    formId:formId
                }
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
            getModelByKey:function (tenetId,formKey) {
                return getModelByKey(tenetId,formKey);
            },
            remove : function (id,enLogin) {
                return remove(id,enLogin);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            update:function (item) {
                return update(item);
            },
            listDetail: function (formId) {
                return listDetail(formId);
            },

            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },

            removeDetail : function (id,enLogin) {
                return removeDetail(id,enLogin);
            },

            getNewModelDetail:function (formId) {
                return getNewModelDetail(formId);
            },

            updateDetail:function (detail) {
                return updateDetail(detail);
            }

        }

    })

    .factory('commonUserService', function ($q, $http) {

        var listUserJsTreeData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/user/listUserJsTreeData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listUser = function (enLogin,deptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/user/listUser.json',
                params:{
                    enLogin:enLogin,
                    deptId:deptId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getUser = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/user/getUser.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDept = function  (enLogin,parentId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/user/listDept.json',
                params: {
                    enLogin: enLogin,
                    parentId: parentId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDept = function  (deptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/user/getDept.json',
                params: {
                    deptId: deptId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listUserJsTreeData:function (params) {
                return listUserJsTreeData(params);
            },
            listUser:function (enLogin,deptId) {
                return listUser(enLogin,deptId);
            },
            listDept:function (enLogin,parentId) {
                return listDept(enLogin,parentId);
            },
            getDept:function (deptId) {
                return getDept(deptId);
            },
            getUser:function (enLogin) {
                return getUser(enLogin);
            }
        }
    })

    .factory('commonAttachService', function ($q, $http) {

        var listLatestData = function (enLogin,size) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/attach/listLatestData.json',
                params: {
                    enLogin:enLogin,
                    size:size
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listLatestData:function(enLogin,size){
                return listLatestData(enLogin,size);
            }
        }
    })

    .factory('commonRequestService', function ($q, $http) {

        var listPagedData = function (params) {
            //console.log(params);
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/request/listPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listRequestName = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/request/listRequestName.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedData:function (params) {
                return listPagedData(params);
            },
            listRequestName:function (enLogin) {
                return listRequestName(enLogin);
            }
        }
    })

    .factory('commonExportService', function ($q, $http) {


        var listData = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/export/listData.json',
                params: {
                    enLogin:enLogin
                }
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
                url: '/common/export/getModelById.json',
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

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/export/getNewModel.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var save = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/export/save.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/export/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {

            listData:function (enLogin) {
                return listData(enLogin);
            },
            getModelById:function (enLogin) {
                return getModelById(enLogin);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            save:function (item) {
                return save(item);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            }

        }
    })

    .factory('commonCodeService', function ($q, $http) {

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/listPagedData.json',
                params:params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listCodeCategory = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/listCodeCategory.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listDataByCatalog = function (enLogin,codeCatalog) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/listDataByCatalog.json',
                params: {
                    enLogin:enLogin,
                    codeCatalog:codeCatalog
                }
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
                url: '/common/code/getModelById.json',
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

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/getNewModel.json',
                params: {
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var save = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/save.json',
                data:item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/remove.json',
                params: {
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (tenetId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/code/selectAll.json',
                params: {
                    tenetId:tenetId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listPagedData:function (params) {
                return listPagedData(params);
            },
            listCodeCategory:function(enLogin){
                return listCodeCategory(enLogin);
            },
            listDataByCatalog:function (enLogin,codeCatalog) {
                return listDataByCatalog(enLogin,codeCatalog);
            },
            getModelById:function (enLogin) {
                return getModelById(enLogin);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            },
            save:function (item) {
                return save(item);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            selectAll:function (tenetId) {
                return selectAll(tenetId);
            },

        }
    })

    .factory('commonBlackService', function ($q, $http) {

        var selectAll = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/black/selectAll.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var save = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/black/save.json',
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
                url: '/common/black/getModelById.json',
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

        var remove = function (id,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/black/remove.json',
                params:{
                    id:id,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/common/black/getNewModel.json',
                params:{
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            selectAll:function (enLogin) {
                return selectAll(enLogin);
            },
            getModelById:function (id) {
                return getModelById(id);
            },
            remove:function (id,enLogin) {
                return remove(id,enLogin);
            },
            save:function (item) {
                return save(item);
            },
            getNewModel:function (enLogin) {
                return getNewModel(enLogin);
            }
        }
    })

    .factory('fiveOaSignQuoteService2', function ($q, $http) {

        var head = "/five/oa/signQuote";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
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
        return {
            getPrintData: function (id) {
                return getPrintData(id);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

        }
    })

    .factory('fiveOaDispatchService2', function ($q, $http) {

        var head = "/five/oa/dispatch";

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

        var getModelByBusinessKey = function (businessKey,fileType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:   '/wuzhou/file/getModelByBusinessKey.json',
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

        return {
            update: function (item) {
                return update(item);
            },

            getModelById: function (id) {
                return getModelById(id);
            },

            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },

            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },

            getPrintData:function(id){
                return getPrintData(id);
            },
            getPrintData:function(id){
                return getPrintData(id);
            },
            getModelByBusinessKey: function (businessKey,fileType) {
                return getModelByBusinessKey(businessKey,fileType);
            },

        }
    })

    .factory('fiveOaDepartmentPostService2', function ($q, $http) {

        var head = "/five/oa/departmentPost";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
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

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })

    .factory('fiveOaFileInstructionService2', function ($q, $http) {

        var head = "/five/oa/fileInstruction";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
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

        var getNewModelByType = function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:  '/five/supervise/getNewModelByType.json',
                params: {
                    userLogin: userLogin,
                    superviseType:superviseType,
                    companyLeader:companyLeader,
                    businessId:businessId,
                    fileTitle:fileTitle,
                    view:view,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            getNewModelByType:function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
                return getNewModelByType(userLogin,superviseType,companyLeader,businessId,fileTitle,view);
            },
        }
    })

    .factory('fiveOaReportService2', function ($q, $http) {

        var head = "/five/oa/report";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
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

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })

    .factory('businessCustomerService2', function ($q, $http) {

        var head = "/business/customer";

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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
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
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
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

        var loadPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/loadPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listCooperationProject = function (customerId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listCooperationProject.json',
                params: {
                    customerId: customerId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelByNameAndLinMan = function (customerName,linkMan) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByNameAndLinMan.json',
                params: {
                    customerName: customerName,
                    linkMan:linkMan
                }
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
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            loadPagedData: function (params) {
                return loadPagedData(params);
            },
            selectAll: function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            listCooperationProject: function (customerId) {
                return listCooperationProject(customerId);
            },
            downloadModel:function(userLogin){
                return downloadModel(userLogin);
            },
            getModelByNameAndLinMan:function(customerName,linkMan){
                return getModelByNameAndLinMan(customerName,linkMan);
            },

        }
    })

    .factory('fiveOaBidApplyService2', function ($q, $http) {

        var head = "/five/oa/bidApply";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
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

        var updateRecord = function (applyId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateRecord.json',
                params: {
                    applyId:applyId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listRecordByUserLogin = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/listRecordByUserLogin.json',
                params: {
                    userLogin:userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            listRecordByUserLogin: function (userLogin,recordId) {
                return listRecordByUserLogin(userLogin,recordId);
            },
            updateRecord: function (applyId) {
                return updateRecord(applyId);
            },
        }
    })

    .factory('fiveBusinessContractReviewService2', function ($q, $http) {

        var head = "/business/contractReview";

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

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var changeOpen = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/changeOpen.json',
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

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateDetail.json',
                data:item
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

        var listDetailById = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/listDetailById.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getNewDetailModel = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewDetailModel.json',
                params: {
                    contractReviewId:contractReviewId,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getDetailModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getDetailModelById.json',
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

        var addCustomer = function (userLogin,recordId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/addCustomer.json',
                params: {
                    userLogin: userLogin,
                    recordId:recordId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var insertContractLibrary = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/insertContractLibrary.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var selectNotHaveContract = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/selectNotHaveContract.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getContractNo = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getContractNo.json',
                params: {
                    contractReviewId:contractReviewId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }
        var getMainContractNo = function (contractReviewId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getMainContractNo.json',
                params: {
                    contractReviewId:contractReviewId
                }
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
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            changeOpen: function (id, userLogin) {
                return changeOpen(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

            updateDetail:function(item){
                return updateDetail(item);
            },
            removeDetail:function(id){
                return removeDetail(id);
            },
            listDetailById: function (contractReviewId) {
                return listDetailById(contractReviewId);
            },
            getNewDetailModel:function (contractReviewId) {
                return getNewDetailModel(contractReviewId);
            },
            getDetailModelById:function(id){
                return getDetailModelById(id);
            },

            addCustomer:function(userLogin,recordId){
                return addCustomer(userLogin,recordId);
            },
            insertContractLibrary:function(contractReviewId){
                return insertContractLibrary(contractReviewId);
            },
            selectNotHaveContract:function(contractReviewId){
                return selectNotHaveContract(contractReviewId);
            },
            getContractNo:function(contractReviewId){
                return getContractNo(contractReviewId);
            },
            getMainContractNo:function(contractReviewId){
                return getMainContractNo(contractReviewId);
            },


        }
    })

    .factory('commonHrTrainDetailService', function ($q, $http) {

        var head = "/hr/hrTrainDetail";

        var listHrTrainMember = function (trainKey) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listHrTrainMember.json',
                params: {
                    trainKey:trainKey,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeHrMember = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeHrMember.json',
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

        var save = function (map) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/save.json',
                params: {
                    map:map,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var newData = function (trainKey,owners,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/newData.json',
                params: {
                    trainKey:trainKey,
                    owners:owners,
                    enLogin:enLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            listHrTrainMember:function (trainKey) {
                return listHrTrainMember(trainKey);
            },
            removeHrMember:function (id) {
                return removeHrMember(id);
            },
            save:function (map) {
                return save(map);
            },
            newData:function (trainKey,owners,enLogin) {
                return newData(trainKey,owners,enLogin);
            },

        }
    })

    .factory('commonPrintTableService', function ($q, $http) {

        var head = "/common/printTable";

        var getPrintDate = function (businessKey,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPrintDate.json',
                params: {
                    businessKey:businessKey,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            getPrintDate:function (businessKey,userLogin) {
                return getPrintDate(businessKey,userLogin);
            },

        }
    })

    .factory('fiveOaStampApplyOfficeService2', function ($q, $http) {

        var head = "/five/oa/stampApplyOffice";

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

        var remove = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin:userLogin,
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
                data:item,
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
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id,userLogin) {
                return remove(id,userLogin);
            },
            update: function (item) {
                return update(item);
            },
        }

    })


