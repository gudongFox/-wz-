function initFiveOaRoute($stateProvider) {
    $stateProvider
        //五洲综合办公-公司办
        .state('five.oaGeneralCountersign', {
            url: "/oa/oaGeneralCountersign",
            templateUrl: function () {
                return "/five/oa/generalCountersign";
            },
            controller: 'FiveOaGeneralCountersignController as vm'
        })
        .state('five.oaGeneralCountersignDetail', {
            url: "/oa/oaGeneralCountersignDetail?generalId",
            templateUrl: function () {
                return "/five/oa/generalCountersignDetail";
            },
            controller: 'FiveOaGeneralCountersignDetailController as vm'
        })
        .state('five.oaReport', {
            url: "/oa/oaReport",
            templateUrl: function () {
                return "/five/oa/report";
            },
            controller: 'FiveOaReportController as vm'
        })
        .state('five.oaReportDetail', {
            url: "/oa/oaReportDetail?reportId",
            templateUrl: function () {
                return "/five/oa/reportDetail";
            },
            controller: 'FiveOaReportDetailController as vm'
        })
        .state('five.oaFileInstruction', {
            url: "/oa/oaFileInstruction",
            templateUrl: function () {
                return "/five/oa/fileInstruction";
            },
            controller: 'FiveOaFileInstructionController as vm'
        })
        .state('five.oaFileInstructionDetail', {
            url: "/oa/oaFileInstructionDetail?instructionId",
            templateUrl: function () {
                return "/five/oa/fileInstructionDetail";
            },
            controller: 'FiveOaFileInstructionDetailController as vm'
        })
        .state('five.oaDispatch', {
            url: "/oa/dispatch",
            templateUrl: function () {
                return "/five/oa/dispatch";
            },
            controller: 'FiveOaDispatchController as vm'
        })
        .state('five.oaDispatchDetail', {
            url: "/oa/dispatchDetail?dispatchId",
            templateUrl: function () {
                return "/five/oa/dispatchDetail";
            },
            controller: 'FiveOaDispatchDetailController as vm'
        })

        .state('five.oaDecisionMaking', {
            url: "/oa/decisionMaking",
            templateUrl: function () {
                return "/five/oa/decisionMaking";
            },
            controller: 'FiveOaDecisionMakingController as vm'
        })
        .state('five.oaDecisionMakingDetail', {
            url: "/oa/decisionMakingDetail?makingId",
            templateUrl: function () {
                return "/five/oa/decisionMakingDetail";
            },
            controller: 'FiveOaDecisionMakingDetailController as vm'
        })

        .state('five.oaStampApplyOffice', {
            url: "/oa/stampApplyOffice",
            templateUrl: function () {
                return "/five/oa/stampApplyOffice";
            },
            controller: 'FiveOaStampApplyOfficeController as vm'
        })
        .state('five.oaStampApplyOfficeDetail', {
            url: "/oa/stampApplyOfficeDetail?applyId",
            templateUrl: function () {
                return "/five/oa/stampApplyOfficeDetail";
            },
            controller: 'FiveOaStampApplyOfficeDetailController as vm'
        })


        .state('five.oaSignQuote', {
            url: "/oa/oaSignQuote",
            templateUrl: function () {
                return "/five/oa/signQuote";
            },
            controller: 'FiveOaSignQuoteController as vm'
        })
        .state('five.oaSignQuoteDetail', {
            url: "/oa/oaSignQuoteDetail?signQuoteId",
            templateUrl: function () {
                return "/five/oa/signQuoteDetail";
            },
            controller: 'FiveOaSignQuoteDetailController as vm'
        })

        .state('five.oaInstitutionCountSign', {
            url: "/oa/oaInstitutionCountSign",
            templateUrl: function () {
                return "/five/oa/institutionCountSign";
            },
            controller: 'FiveOaInstitutionCountSignController as vm'
        })
        .state('five.oaInstitutionCountSignDetail', {
            url: "/oa/oaInstitutionCountSignDetail?institutionCountSignId",
            templateUrl: function () {
                return "/five/oa/institutionCountSignDetail";
            },
            controller: 'FiveOaInstitutionCountSignDetailController as vm'
        })

        .state('five.oaCar', {
            url: "/oa/oaCar",
            templateUrl: function () {
                return "/five/oa/car";
            },
            controller: 'FiveOaCarController as vm'
        })
        .state('five.oaCarDetail', {
            url: "/oa/oaCarDetail?id",
            templateUrl: function () {
                return "/five/oa/carDetail";
            },
            controller: 'FiveOaCarDetailController as vm'
        })

        .state('five.oaSelfCarApply', {
            url: "/oa/oaSelfCarApply",
            templateUrl: function () {
                return "/five/oa/carApply";
            },
            controller: 'FiveOaSelfCarApplyController as vm'
        })
        .state('five.oaSelfCarApplyDetail', {
            url: "/oa/oaSelfCarApplyDetail?id",
            templateUrl: function () {
                return "/five/oa/carApplyDetail";
            },
            controller: 'FiveOaSelfCarApplyDetailController as vm'
        })
        .state('five.oaLeaderCarApply', {
            url: "/oa/oaLeaderCarApply",
            templateUrl: function () {
                return "/five/oa/carApply";
            },
            controller: 'FiveOaLeaderCarApplyController as vm'
        })
        .state('five.oaLeaderCarApplyDetail', {
            url: "/oa/oaLeaderCarApplyDetail?id",
            templateUrl: function () {
                return "/five/oa/carApplyDetail";
            },
            controller: 'FiveOaLeaderCarApplyDetailController as vm'
        })
        .state('five.oaMeetingRoom', {
            url: "/oa/oaMeetingRoom",
            templateUrl: function () {
                return "/five/oa/meetingRoom";
            },
            controller: 'FiveOaMeetingRoomController as vm'
        })
        .state('five.oaMeetingRoomDetail', {
            url: "/oa/oaMeetingRoomDetail?id",
            templateUrl: function () {
                return "/five/oa/meetingRoomDetail";
            },
            controller: 'FiveOaMeetingRoomDetailController as vm'
        })

        .state('five.oaMeetingRoomApply', {
            url: "/oa/oaMeetingRoomApply",
            templateUrl: function () {
                return "/five/oa/meetingRoomApply";
            },
            controller: 'FiveOaMeetingRoomApplyController as vm'
        })
        .state('five.oaMeetingRoomApplyDetail', {
            url: "/oa/oaMeetingRoomApplyDetail?id",
            templateUrl: function () {
                return "/five/oa/meetingRoomApplyDetail";
            },
            controller: 'FiveOaMeetingRoomApplyDetailController as vm'
        })

        //五洲综合办公-经营发展部
        .state('five.oaReviewContract', {
            url: "/oa/oaReviewContract",
            templateUrl: function () {
                return "/five/oa/reviewContract";
            },
            controller: 'FiveOaReviewContractController as vm'
        })
        .state('five.oaReviewContractDetail', {
            url: "/oa/oaReviewContractDetail?contractId",
            templateUrl: function () {
                return "/five/oa/reviewContractDetail";
            },
            controller: 'FiveOaReviewContractDetailController as vm'
        })
        .state('five.oaPlatformRecord', {
            url: "/oa/oaPlatformRecord",
            templateUrl: function () {
                return "/five/oa/platformRecord";
            },
            controller: 'FiveOaPlatformRecordController as vm'
        })
        .state('five.oaPlatformRecordDetail', {
            url: "/oa/oaPlatformRecordDetail?recordId",
            templateUrl: function () {
                return "/five/oa/platformRecordDetail";
            },
            controller: 'FiveOaPlatformRecordDetailController as vm'
        })
        .state('five.oaContractSign', {
            url: "/oa/oaContractSign",
            templateUrl: function () {
                return "/five/oa/contractSign";
            },
            controller: 'FiveOaContractSignController as vm'
        })
        .state('five.oaContractSignDetail', {
            url: "/oa/oaContractSignDetail?signId",
            templateUrl: function () {
                return "/five/oa/contractSignDetail";
            },
            controller: 'FiveOaContractSignDetailController as vm'
        })
        .state('five.oaBidApply', {
            url: "/oa/oaBidApply",
            templateUrl: function () {
                return "/five/oa/bidApply";
            },
            controller: 'FiveOaBidApplyController as vm'
        })
        .state('five.oaBidApplyDetail', {
            url: "/oa/oaBidApplyDetail?applyId",
            templateUrl: function () {
                return "/five/oa/bidApplyDetail";
            },
            controller: 'FiveOaBidApplyDetailController as vm'
        })

        //五洲综合办公-人力资源部
        .state('five.oaGoAbroad', {
            url: "/oa/oaGoAbroad",
            templateUrl: function () {
                return "/five/oa/goAbroad";
            },
            controller: 'FiveOaGoAbroadController as vm'
        })
        .state('five.oaGoAbroadDetail', {
            url: "/oa/oaGoAbroadDetail?goAbroadId",
            templateUrl: function () {
                return "/five/oa/goAbroadDetail";
            },
            controller: 'FiveOaGoAbroadDetailController as vm'
        })
        .state('five.oaEmployeeEntryExamine', {
            url: "/oa/oaEmployeeEntryExamine",
            templateUrl: function () {
                return "/five/oa/employeeEntryExamine";
            },
            controller: 'FiveOaEmployeeEntryExamineController as vm'
        })
        .state('five.oaEmployeeEntryExamineDetail', {
            url: "/oa/oaEmployeeEntryExamineDetail?employeeEntryExamineId",
            templateUrl: function () {
                return "/five/oa/employeeEntryExamineDetail";
            },
            controller: 'FiveOaEmployeeEntryExamineDetailController as vm'
        })
        .state('five.oaEmployeeTransferExamine', {
            url: "/oa/oaEmployeeTransferExamine",
            templateUrl: function () {
                return "/five/oa/employeeTransferExamine";
            },
            controller: 'FiveOaEmployeeTransferExamineController as vm'
        })
        .state('five.oaEmployeeTransferExamineDetail', {
            url: "/oa/oaEmployeeTransferExamineDetail?employeeTransferExamineId",
            templateUrl: function () {
                return "/five/oa/employeeTransferExamineDetail";
            },
            controller: 'FiveOaEmployeeTransferExamineDetailController as vm'
        })
        .state('five.oaEmployeeDimissionExamine', {
            url: "/oa/oaEmployeeDimissionExamine",
            templateUrl: function () {
                return "/five/oa/employeeDimissionExamine";
            },
            controller: 'FiveOaEmployeeDimissionExamineController as vm'
        })
        .state('five.oaEmployeeDimissionExamineDetail', {
            url: "/oa/oaEmployeeDimissionExamineDetail?employeeDimissionExamineId",
            templateUrl: function () {
                return "/five/oa/employeeDimissionExamineDetail";
            },
            controller: 'FiveOaEmployeeDimissionExamineDetailController as vm'
        })
        .state('five.oaEmployeeRetireExamine', {
            url: "/oa/oaEmployeeRetireExamine",
            templateUrl: function () {
                return "/five/oa/employeeRetireExamine";
            },
            controller: 'FiveOaEmployeeRetireExamineController as vm'
        })
        .state('five.oaEmployeeRetireExamineDetail', {
            url: "/oa/oaEmployeeRetireExamineDetail?employeeRetireExamineId",
            templateUrl: function () {
                return "/five/oa/employeeRetireExamineDetail";
            },
            controller: 'FiveOaEmployeeRetireExamineDetailController as vm'
        })
        .state('five.oaProcessDevelopApply', {
            url: "/oa/oaProcessDevelopApply",
            templateUrl: function () {
                return "/five/oa/processDevelopApply";
            },
            controller: 'FiveOaProcessDevelopApplyController as vm'
        })
        .state('five.oaProcessDevelopApplyDetail', {
            url: "/oa/oaProcessDevelopApplyDetail?processDevelopApplyId",
            templateUrl: function () {
                return "/five/oa/processDevelopApplyDetail";
            },
            controller: 'FiveOaProcessDevelopApplyDetailController as vm'
        })

        //五洲综合办公-档案图书室
        .state('five.oaMaterialBorrow', {
            url: "/oa/materialBorrow",
            templateUrl: function () {
                return "/five/oa/materialBorrow";
            },
            controller: 'FiveOaMaterialBorrowController as vm'
        })
        .state('five.oaMaterialBorrowDetail', {
            url: "/oa/materialBorrowDetail?materialBorrowId",
            templateUrl: function () {
                return "/five/oa/materialBorrowDetail";
            },
            controller: 'FiveOaMaterialBorrowDetailController as vm'
        })
        .state('five.oaMaterialPurchase', {
            url: "/oa/oaMaterialPurchase",
            templateUrl: function () {
                return "/five/oa/materialPurchase";
            },
            controller: 'FiveOaMaterialPurchaseController as vm'
        })
        .state('five.oaMaterialPurchaseDetail', {
            url: "/oa/oaMaterialPurchaseDetail?materialPurchaseId",
            templateUrl: function () {
                return "/five/oa/materialPurchaseDetail";
            },
            controller: 'FiveOaMaterialPurchaseDetailController as vm'
        })
        //五洲综合办公-计算机所
        .state('five.oaComputerApplication', {
            url: "/oa/computerApplication",
            templateUrl: function () {
                return "/five/oa/computerApplication";
            },
            controller: 'FiveOaComputerApplicationController as vm'
        })
        .state('five.oaComputerApplicationDetail', {
            url: "/oa/computerApplicationDetail?computerApplicationId",
            templateUrl: function () {
                return "/five/oa/computerApplicationDetail";
            },
            controller: 'FiveOaComputerApplicationDetailController as vm'
        })
        .state('five.oaComputerPurchase', {
            url: "/oa/computerPurchase",
            templateUrl: function () {
                return "/five/oa/computerPurchase";
            },
            controller: 'FiveOaComputerPurchaseController as vm'
        })
        .state('five.oaComputerPurchaseDetail', {
            url: "/oa/computerPurchaseDetail?computerPurchaseId",
            templateUrl: function () {
                return "/five/oa/computerPurchaseDetail";
            },
            controller: 'FiveOaComputerPurchaseDetailController as vm'
        })
        .state('five.oaComputerMaintain', {
            url: "/oa/computerMaintain",
            templateUrl: function () {
                return "/five/oa/computerMaintain";
            },
            controller: 'FiveOaComputerMaintainController as vm'
        })
        .state('five.oaComputerMaintainDetail', {
            url: "/oa/computerMaintainDetail?computerMaintainId",
            templateUrl: function () {
                return "/five/oa/computerMaintainDetail";
            },
            controller: 'FiveOaComputerMaintainDetailController as vm'
        })
        //五洲综合办公-信息化建设与管理部
        .state('five.oaInventPayment', {
            url: "/oa/oaInventPayment",
            templateUrl: function () {
                return "/five/oa/inventPayment";
            },
            controller: 'FiveOaInventPaymentController as vm'
        })
        .state('five.oaInventPaymentDetail', {
            url: "/oa/oaInventPaymentDetail?paymentId",
            templateUrl: function () {
                return "/five/oa/inventPaymentDetail";
            },
            controller: 'FiveOaInventPaymentDetailController as vm'
        })
        .state('five.oaInventApply', {
            url: "/oa/oaInventApply",
            templateUrl: function () {
                return "/five/oa/inventApply";
            },
            controller: 'FiveOaInventApplyController as vm'
        })
        .state('five.oaInventApplyDetail', {
            url: "/oa/oaInventApplyDetail?inventId",
            templateUrl: function () {
                return "/five/oa/inventApplyDetail";
            },
            controller: 'FiveOaInventApplyDetailController as vm'
        })
        .state('five.oaAssociationApply', {
            url: "/oa/oaAssociationApply",
            templateUrl: function () {
                return "/five/oa/associationApply";
            },
            controller: 'FiveOaAssociationApplyController as vm'
        })
        .state('five.oaAssociationApplyDetail', {
            url: "/oa/oaAssociationApplyDetail?applyId",
            templateUrl: function () {
                return "/five/oa/associationApplyDetail";
            },
            controller: 'FiveOaAssociationApplyDetailController as vm'
        })
        .state('five.oaAssociationChange', {
            url: "/oa/oaAssociationChange",
            templateUrl: function () {
                return "/five/oa/associationChange";
            },
            controller: 'FiveOaAssociationChangeController as vm'
        })
        .state('five.oaAssociationChangeDetail', {
            url: "/oa/oaAssociationChangeDetail?changeId",
            templateUrl: function () {
                return "/five/oa/associationChangeDetail";
            },
            controller: 'FiveOaAssociationChangeDetailController as vm'
        })

        .state('five.oaAssociationPayment', {
            url: "/oa/oaAssociationPayment",
            templateUrl: function () {
                return "/five/oa/associationPayment";
            },
            controller: 'FiveOaAssociationPaymentController as vm'
        })
        .state('five.oaAssociationPaymentDetail', {
            url: "/oa/oaAssociationPaymentDetail?paymentId",
            templateUrl: function () {
                return "/five/oa/associationPaymentDetail";
            },
            controller: 'FiveOaAssociationPaymentDetailController as vm'
        })

        .state('five.oaSoftware', {
            url: "/oa/oaSoftware",
            templateUrl: function () {
                return "/five/oa/software";
            },
            controller: 'FiveOaSoftwareController as vm'
        })
        .state('five.oaSoftwareDetail', {
            url: "/oa/oaSoftwareDetail?softwareId",
            templateUrl: function () {
                return "/five/oa/softwareDetail";
            },
            controller: 'FiveOaSoftwareDetailController as vm'
        })
        .state('five.oaDeptJournal', {
            url: "/oa/oaDeptJournal",
            templateUrl: function () {
                return "/five/oa/deptJournal";
            },
            controller: 'FiveOaDeptJournalController as vm'
        })
        .state('five.oaDeptJournalDetail', {
            url: "/oa/oaDeptJournalDetail?journalId",
            templateUrl: function () {
                return "/five/oa/deptJournalDetail";
            },
            controller: 'FiveOaDeptJournalDetailController as vm'
        })
        .state('five.oaOutSpecialist', {
            url: "/oa/oaOutSpecialist",
            templateUrl: function () {
                return "/five/oa/outSpecialist";
            },
            controller: 'FiveOaOutSpecialistController as vm'
        })
        .state('five.oaOutSpecialistDetail', {
            url: "/oa/oaOutSpecialistDetail?outSpecialistId",
            templateUrl: function () {
                return "/five/oa/outSpecialistDetail";
            },
            controller: 'FiveOaOutSpecialistDetailController as vm'
        })
        .state('five.oaFileTransfer', {
            url: "/oa/oaFileTransfer",
            templateUrl: function () {
                return "/five/oa/fileTransfer";
            },
            controller: 'FiveOaFileTransferController as vm'
        })
        .state('five.oaFileTransferDetail', {
            url: "/oa/oaFileTransferDetail?transferId",
            templateUrl: function () {
                return "/five/oa/fileTransferDetail";
            },
            controller: 'FiveOaFileTransferDetailController as vm'
        })
        .state('five.oaComputerNetwork', {
            url: "/oa/oaComputerNetwork",
            templateUrl: function () {
                return "/five/oa/computerNetwork";
            },
            controller: 'FiveOaComputerNetworkController as vm'
        })
        .state('five.oaComputerNetworkDetail', {
            url: "/oa/oaComputerNetworkDetail?networkId",
            templateUrl: function () {
                return "/five/oa/computerNetworkDetail";
            },
            controller: 'FiveOaComputerNetworkDetailController as vm'
        })
        .state('five.oaComputerChange', {
            url: "/oa/oaComputerChange",
            templateUrl: function () {
                return "/five/oa/computerChange";
            },
            controller: 'FiveOaComputerChangeController as vm'
        })
        .state('five.oaComputerChangeDetail', {
            url: "/oa/oaComputerChangeDetail?changeId",
            templateUrl: function () {
                return "/five/oa/computerChangeDetail";
            },
            controller: 'FiveOaComputerChangeDetailController as vm'
        })
        .state('five.oaMessageExport', {
            url: "/oa/oaMessageExport",
            templateUrl: function () {
                return "/five/oa/messageExport";
            },
            controller: 'FiveOaMessageExportController as vm'
        })
        .state('five.oaMessageExportDetail', {
            url: "/oa/oaMessageExportDetail?exportId",
            templateUrl: function () {
                return "/five/oa/messageExportDetail";
            },
            controller: 'FiveOaMessageExportDetailController as vm'
        })

        .state('five.oaInformationEquipmentExamine', {
            url: "/oa/oaInformationEquipmentExamine",
            templateUrl: function () {
                return "/five/oa/informationEquipmentExamine";
            },
            controller: 'FiveOaInformationEquipmentExamineController as vm'
        })
        .state('five.oaInformationEquipmentExamineDetail', {
            url: "/oa/oaInformationEquipmentExamineDetail?informationEquipmentExamineId",
            templateUrl: function () {
                return "/five/oa/informationEquipmentExamineDetail";
            },
            controller: 'FiveOaInformationEquipmentExamineDetailController as vm'
        })

        //五洲综合办公-信息化建设与管理部
        .state('five.oaDepartmentPost', {
            url: "/oa/oaDepartmentPost",
            templateUrl: function () {
                return "/five/oa/departmentPost";
            },
            controller: 'FiveOaDepartmentPostController as vm'
        })
        .state('five.oaDepartmentPostDetail', {
            url: "/oa/oaDepartmentPostDetail?departmentPostId",
            templateUrl: function () {
                return "/five/oa/departmentPostDetail";
            },
            controller: 'FiveOaDepartmentPostDetailController as vm'
        })

        .state('five.oaContractLawExamine', {
            url: "/oa/oaContractLawExamine",
            templateUrl: function () {
                return "/five/oa/contractLawExamine";
            },
            controller: 'FiveOaContractLawExamineController as vm'
        })
        .state('five.oaContractLawExamineDetail', {
            url: "/oa/oaContractLawExamineDetail?contractLawExamineId",
            templateUrl: function () {
                return "/five/oa/contractLawExamineDetail";
            },
            controller: 'FiveOaContractLawExamineDetailController as vm'
        })

        .state('five.oaRuleLawExamine', {
            url: "/oa/oaRuleLawExamine",
            templateUrl: function () {
                return "/five/oa/ruleLawExamine";
            },
            controller: 'FiveOaRuleLawExamineController as vm'
        })
        .state('five.oaRuleLawExamineDetail', {
            url: "/oa/oaRuleLawExamineDetail?ruleLawExamineId",
            templateUrl: function () {
                return "/five/oa/ruleLawExamineDetail";
            },
            controller: 'FiveOaRuleLawExamineDetailController as vm'
        })

        .state('five.oaNonIndependentDeptSet', {
            url: "/oa/oaNonIndependentDeptSet",
            templateUrl: function () {
                return "/five/oa/nonIndependentDeptSet";
            },
            controller: 'FiveOaNonIndependentDeptSetController as vm'
        })
        .state('five.oaNonIndependentDeptSetDetail', {
            url: "/oa/oaNonIndependentDeptSetDetail?nonIndependentDeptSetId",
            templateUrl: function () {
                return "/five/oa/nonIndependentDeptSetDetail";
            },
            controller: 'FiveOaNonIndependentDeptSetDetailController as vm'
        })

        .state('five.oaNonSecretEquipmentScrap', {
            url: "/oa/oaNonSecretEquipmentScrap",
            templateUrl: function () {
                return "/five/oa/nonSecretEquipmentScrap";
            },
            controller: 'FiveOaNonSecretEquipmentScrapController as vm'
        })
        .state('five.oaNonSecretEquipmentScrapDetail', {
            url: "/oa/oaNonSecretEquipmentScrapDetail?nonSecretEquipmentScrapId",
            templateUrl: function () {
                return "/five/oa/nonSecretEquipmentScrapDetail";
            },
            controller: 'FiveOaNonSecretEquipmentScrapDetailController as vm'
        })

        .state('five.oaGoAbroadTrainAsk', {
            url: "/oa/oaGoAbroadTrainAsk",
            templateUrl: function () {
                return "/five/oa/goAbroadTrainAsk";
            },
            controller: 'FiveOaGoAbroadTrainAskController as vm'
        })
        .state('five.oaGoAbroadTrainAskDetail', {
            url: "/oa/oaGoAbroadTrainAskDetail?goAbroadTrainAskId",
            templateUrl: function () {
                return "/five/oa/goAbroadTrainAskDetail";
            },
            controller: 'FiveOaGoAbroadTrainAskDetailController as vm'
        })
        .state('five.oaInformationEquipmentApply', {
            url: "/oa/oaInformationEquipmentApply",
            templateUrl: function () {
                return "/five/oa/informationEquipmentApply";
            },
            controller: 'FiveOaInformationEquipmentApplyController as vm'
        })
        .state('five.oaInformationEquipmentApplyDetail', {
            url: "/oa/oaInformationEquipmentApplyDetail?informationEquipmentApplyId",
            templateUrl: function () {
                return "/five/oa/informationEquipmentApplyDetail";
            },
            controller: 'FiveOaInformationEquipmentApplyDetailController as vm'
        })

        .state('five.oaInformationEquipmentProcurement', {
            url: "/oa/oaInformationEquipmentProcurement",
            templateUrl: function () {
                return "/five/oa/informationEquipmentProcurement";
            },
            controller: 'FiveOaInformationEquipmentProcurementController as vm'
        })
        .state('five.oaInformationEquipmentProcurementDetail', {
            url: "/oa/oaInformationEquipmentProcurementDetail?informationEquipmentProcurementId",
            templateUrl: function () {
                return "/five/oa/informationEquipmentProcurementDetail";
            },
            controller: 'FiveOaInformationEquipmentProcurementDetailController as vm'
        })

        .state('five.oaGoAbroadTrainDeclare', {
            url: "/oa/oaGoAbroadTrainDeclare",
            templateUrl: function () {
                return "/five/oa/goAbroadTrainDeclare";
            },
            controller: 'FiveOaGoAbroadTrainDeclareController as vm'
        })
        .state('five.oaGoAbroadTrainDeclareDetail', {
            url: "/oa/oaGoAbroadTrainDeclareDetail?goAbroadTrainDeclareId",
            templateUrl: function () {
                return "/five/oa/goAbroadTrainDeclareDetail";
            },
            controller: 'FiveOaGoAbroadTrainDeclareDetailController as vm'
        })

        .state('five.oaProfessionalSkillTrain', {
            url: "/oa/oaProfessionalSkillTrain",
            templateUrl: function () {
                return "/five/oa/professionalSkillTrain";
            },
            controller: 'FiveOaProfessionalSkillTrainController as vm'
        })
        .state('five.oaProfessionalSkillTrainDetail', {
            url: "/oa/oaProfessionalSkillTrainDetail?professionalSkillTrainId",
            templateUrl: function () {
                return "/five/oa/professionalSkillTrainDetail";
            },
            controller: 'FiveOaProfessionalSkillTrainDetailController as vm'
        })

        .state('five.oaOutTechnicalExchange', {
            url: "/oa/oaOutTechnicalExchange",
            templateUrl: function () {
                return "/five/oa/outTechnicalExchange";
            },
            controller: 'FiveOaOutTechnicalExchangeController as vm'
        })
        .state('five.oaOutTechnicalExchangeDetail', {
            url: "/oa/oaOutTechnicalExchangeDetail?outTechnicalExchangeId",
            templateUrl: function () {
                return "/five/oa/outTechnicalExchangeDetail";
            },
            controller: 'FiveOaOutTechnicalExchangeDetailController as vm'
        })

        .state('five.oaTechnologyArticleExamine', {
            url: "/oa/oaTechnologyArticleExamine",
            templateUrl: function () {
                return "/five/oa/technologyArticleExamine";
            },
            controller: 'FiveOaTechnologyArticleExamineController as vm'
        })
        .state('five.oaTechnologyArticleExamineDetail', {
            url: "/oa/oaTechnologyArticleExamineDetail?technologyArticleExamineId",
            templateUrl: function () {
                return "/five/oa/technologyArticleExamineDetail";
            },
            controller: 'FiveOaTechnologyArticleExamineDetailController as vm'
        })

        .state('five.oaPressurePipSealExamine', {
            url: "/oa/oaPressurePipSealExamine",
            templateUrl: function () {
                return "/five/oa/pressurePipSealExamine";
            },
            controller: 'FiveOaPressurePipSealExamineController as vm'
        })
        .state('five.oaPressurePipSealExamineDetail', {
            url: "/oa/oaPressurePipSealExamineDetail?pressurePipSealExamineId",
            templateUrl: function () {
                return "/five/oa/pressurePipSealExamineDetail";
            },
            controller: 'FiveOaPressurePipSealExamineDetailController as vm'
        })

        .state('five.oaScientificTaskCostApply', {
            url: "/oa/oaScientificTaskCostApply",
            templateUrl: function () {
                return "/five/oa/scientificTaskCostApply";
            },
            controller: 'FiveOaScientificTaskCostApplyController as vm'
        })
        .state('five.oaScientificTaskCostApplyDetail', {
            url: "/oa/oaScientificTaskCostApplyDetail?scientificTaskCostApplyId",
            templateUrl: function () {
                return "/five/oa/scientificTaskCostApplyDetail";
            },
            controller: 'FiveOaScientificTaskCostApplyDetailController as vm'
        })


        //工程管理部
        .state('five.oaProjectFundPlan', {
            url: "/oa/oaProjectFundPlan",
            templateUrl: function () {
                return "/five/oa/projectFundPlan";
            },
            controller: 'FiveOaProjectFundPlanController as vm'
        })
        .state('five.oaProjectFundPlanDetail', {
            url: "/oa/oaProjectFundPlanDetail?projectFundPlanId",
            templateUrl: function () {
                return "/five/oa/projectFundPlanDetail";
            },
            controller: 'FiveOaProjectFundPlanDetailController as vm'
        })
        //党群工作部
        .state('five.oaNewsExamine', {
            url: "/oa/oaNewsExamine",
            templateUrl: function () {
                return "/five/oa/newsExamine";
            },
            controller: 'FiveOaNewsExamineController as vm'
        })
        .state('five.oaNewsExamineDetail', {
            url: "/oa/oaNewsExamineDetail?newsExamineId",
            templateUrl: function () {
                return "/five/oa/newsExamineDetail";
            },
            controller: 'FiveOaNewsExamineDetailController as vm'
        });
}
