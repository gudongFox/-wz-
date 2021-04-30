angular.module('services.hr2', [])

    .factory('hrDetailService', function ($q, $http) {

        var head="/hr/detail";

        var getNewModel = function (referType,owner,enLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getNewModel.json',
                params: {
                    referType:referType,
                    owner:owner,
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
            getNewModel:function(referType,owner,enLogin){
                return getNewModel(referType,owner,enLogin);
            }
        }
    })

    .factory('hrTrainMemberService', function ($q, $http) {

        var head = "/hr/train";

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

        var saveHrMember = function (data) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/saveHrMember.json',
                data: data
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

        var save = function (data) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head +'/save.json',
                data:data
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
            saveHrMember:function (map) {
                return saveHrMember(map);
            },
            newData:function (trainKey,owners,enLogin) {
                return newData(trainKey,owners,enLogin);
            },
            save:function (data) {
                return save(data);
            }

        }
    })

    .factory('hrEmployeeService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/getModelById.json',
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

        var getModelByUserLogin = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/getModelByUserLogin.json',
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

        var listByUserLogin = function (userLogins) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listByUserLogin.json',
                params: {
                    userLogins: userLogins
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var selectSimpleAll = function (selectName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/selectSimpleAll.json',
                params: {
                    selectName:selectName
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
                url: '/hr/employee/update.json',
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
                url: '/hr/employee/getNewModel.json'
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
                url: '/hr/employee/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listSimplePagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listSimplePagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listSimpleDeptPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listSimpleDeptPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var listSimpleRolePagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listSimpleRolePagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listEmployeeByDeptId = function (deptId,containChild) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listEmployeeByDeptId.json',
                params: {
                    deptId: deptId,
                    containChild:containChild
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var listPagedDataByRoleId = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listPagedDataByRoleId.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedDataByPositionId = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listPagedDataByPositionId.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateRoleIds = function (userLogin,roleIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updateRoleIds.json',
                params: {
                    userLogin: userLogin,
                    roleIds:roleIds
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updatePositionIds = function (userLogin,positionIds) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updatePositionIds.json',
                params: {
                    userLogin: userLogin,
                    positionIds:positionIds
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateType = function (userLogin,type) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updateType.json',
                params: {
                    userLogin: userLogin,
                    type:type
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var updateStatus = function (userLogin,status) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updateStatus.json',
                params: {
                    userLogin: userLogin,
                    status:status
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDeptId = function (userLogin,deptId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updateDeptId.json',
                params: {
                    userLogin: userLogin,
                    deptId:deptId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var remove = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/remove.json',
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


        var resetPwd = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/resetPwd.json',
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

        var resetPassword = function (userLogin,password,checkCode) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/resetPassword.json',
                params: {
                    userLogin: userLogin,
                    password:password,
                    checkCode:checkCode
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var insert = function (userLogin,userName,deptId,userType,mobile) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/insert.json',
                params: {
                    userLogin:userLogin,
                    userName:userName,
                    deptId:deptId,
                    userType:userType,
                    mobile:mobile
                }
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
                url: '/hr/employee/selectAll.json'
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDataByUserLoginList = function (userLoginList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/listDataByUserLoginList.json',
                params:{
                    userLoginList:userLoginList
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectByUserLoginList = function (userLoginList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/selectByUserLoginList.json',
                params:{
                    userLoginList:userLoginList
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var selectAllSimple = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/selectAllSimple.json',

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
                url:'/hr/employee/downloadModel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: {
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateMobile = function (enLogin,mobile) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/employee/updateMobile.json',
                params: {
                    enLogin: enLogin,
                    mobile:mobile,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            selectAllSimple: function () {
                return selectAllSimple();
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getModelByUserLogin: function (userLogin) {
                return getModelByUserLogin(userLogin);
            },
            listByUserLogin: function (userLogins) {
                return listByUserLogin(userLogins);
            },
            getNewModel: function () {
                return getNewModel();
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            listSimplePagedData: function (params) {
                return listSimplePagedData(params);
            },
            listSimpleDeptPagedData: function (params) {
                return listSimpleDeptPagedData(params);
            },
            listSimpleRolePagedData: function (params) {
                return listSimpleRolePagedData(params);
            },
            listEmployeeByDeptId:function (id,containChild) {
                return listEmployeeByDeptId(id,containChild);
            },
            listPagedDataByRoleId:function(params){
                return listPagedDataByRoleId(params);
            },
            listPagedDataByPositionId:function(params){
                return listPagedDataByPositionId(params);
            },
            remove: function (userLogin) {
                return remove(userLogin);
            },
            updatePositionIds : function (userLogin,positionIds){
                return updatePositionIds(userLogin,positionIds);
            },
            updateType : function (userLogin,type){
                return updateType(userLogin,type);
            },
            updateStatus : function (userLogin,status){
                return updateStatus(userLogin,status);
            },

            updateRoleIds : function (userLogin,roleIds){
                return updateRoleIds(userLogin,roleIds);
            },
            updateDeptId : function (userLogin,deptId){
                return updateDeptId(userLogin,deptId);
            },
            resetPwd: function (userLogin) {
                return resetPwd(userLogin);
            },
            insert: function (userLogin,userName,deptId,userType,mobile) {
                return insert(userLogin,userName,deptId,userType,mobile);
            },
            selectAll:function () {
                return selectAll();
            },
            selectSimpleAll: function (selectName) {
                return selectSimpleAll(selectName);
            },
            resetPassword: function (userLogin,password,checkCode) {
                return resetPassword(userLogin,password,checkCode);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            listDataByUserLoginList:function (userLoginList) {
                return listDataByUserLoginList(userLoginList);
            },
            selectByUserLoginList:function (userLoginList) {
                return selectByUserLoginList(userLoginList);
            },
            updateMobile: function (enLogin,mobile) {
                return updateMobile(enLogin,mobile);
            },

        }

    })

    .factory('hrEducationService', function ($q, $http) {
        var head="/hr/education";
        var getModelById = function (educationId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    educationId:educationId
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
                url: '/hr/education/listData.json',
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
        return {
            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(educationId){
                return getModelById(educationId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listData:function (userLogin) {
                return listData(userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrContractService', function ($q, $http) {
        var head="/hr/contract";
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
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrHonorService', function ($q, $http) {
        var head="/hr/honor";

        var getModelById = function (honorId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    id:honorId
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
                url: head+'/listPagedData.json',
                params: params
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
            getModelById: function (honorId) {
                return getModelById(honorId);
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


    .factory('hrQualifyService', function ($q, $http) {
        var head="/hr/qualify";
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

        var initData = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/initData.json'
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var toggleEnable = function (id,role) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/toggleEnable.json',
                params:{
                    id:id,
                    role:role
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateMajor = function (id,majorName) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/updateMajor.json',
                params:{
                    id:id,
                    majorName:majorName
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


        var copy = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+ '/copy.json',
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
                url: head+'/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var successApply = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/successApply.json',
                params: params
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
            getModelById: function (contractId) {
                return getModelById(contractId);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listData: function (userLogin) {
                return listData(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            copy: function (id, userLogin) {
                return copy(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            successApply: function () {
                return successApply();
            },
            initData:function () {
                return initData();
            },
            toggleEnable:function (id,role) {
                return toggleEnable(id,role);
            },
            updateMajor:function (id,majorName) {
                return updateMajor(id,majorName);
            }
        }
    })

    .factory('hrCertificationService', function ($q, $http) {
        var head="/hr/certification";
        var getModelById = function (certificationId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getModelById.json',
                params: {
                    certificationId:certificationId
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



        return {
            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(educationId){
                return getModelById(educationId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listData:function (userLogin) {
                return listData(userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
        }
    })

    .factory('hrPositionService', function ($q, $http) {

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/position/getModelById.json',
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
                url: '/hr/position/insert.json',
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
                url: '/hr/position/update.json',
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
                url: '/hr/position/getNewModel.json'
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
                url: '/hr/position/selectAll.json'
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
                url: '/hr/position/listPagedData.json',
                params: params
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
                url: '/hr/position/remove.json',
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
        var listSortedAll = function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/position/listSortedAll.json'
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getUserTree = function (positionId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/position/getUserTree.json',
                params: {
                    positionId:positionId
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var savePositionUserList = function (positionId,userList) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/hr/position/savePositionUserList.json',
                params: {
                    positionId:positionId,
                    userList:userList
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
            getNewModel: function () {
                return getNewModel();
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            remove: function (id) {
                return remove(id);
            },
            getModelById:function(id){
                return getModelById(id);
            },
            selectAll:function () {
                return selectAll();
            },
            listSortedAll:function () {
                return listSortedAll();
            },
            getUserTree:function (positionId) {
                return getUserTree(positionId);
            },
            savePositionUserList:function (positionId,userList) {
                return savePositionUserList(positionId,userList);
            },
        }

    })

    .factory('hrInventService', function ($q, $http) {
        var head = "/hr/invent";

        var getModelById = function (inventId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    inventId: inventId
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
        var listData = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listData.json',
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
            insert:function(item){
                return insert(item);
            },
            update:function(item){
                return update(item);
            },
            getModelById:function(inventId){
                return getModelById(inventId);
            },
            getNewModel:function(userLogin){
                return getNewModel(userLogin);
            },
            listData:function (userLogin) {
                return listData(userLogin);
            },
            remove:function(id,userLogin){
                return remove(id,userLogin);
            },
            listPagedData:function (params) {
              return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
        }
    })





