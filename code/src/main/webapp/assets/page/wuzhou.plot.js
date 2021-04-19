var appName = "wuzhou.plot";

var mccApp = angular.module(appName, ['ui.router', 'ui.router.state.events', 'ui.my.directives', "services.common", "controllers.common", "services.act", "controllers.act",
    'controllers.mcc', 'controllers.sys', 'controllers.five.finance',
    "controllers.hr1", "controllers.hr2", "controllers.ed", 'controllers.ed.input', "controllers.oa2", "controllers.oa.notice","controllers.web",
    'controllers.me', 'controllers.business', 'controllers.five.oa',
    "controllers.five", "controllers.five1", 'controllers.five.business', 'controllers.five.hr', 'controllers.five.home', 'controllers.five.supervise', 'controllers.five.asset', 'controllers.five.budget',
    'services.business', "services.oa1", "services.oa2", 'services.five.finance',
    'services.sys',  'services.hr1', 'services.hr2', 'services.ed',
    'services.five', 'services.five1', 'services.five.hr', 'services.five.oa', 'services.five.home', 'services.five.supervise', 'services.five.asset', 'services.five.budget'])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider,$sceDelegateProvider) {
        $sceDelegateProvider.resourceUrlWhitelist([
            // Allow same origin resource loads.
            'self',
            // Allow loading from our assets domain.  Notice the difference between * and **.
            'https://owa.wuzhou.com.cn/**']);


        $locationProvider.html5Mode({
            enable: true,
            requireBase: false
        });
        $httpProvider.interceptors.push('myHttpInterceptor');
        $httpProvider.defaults.headers.common = {'jwt': user.jwt, 'enLogin': user.enLogin};


        initActRoute($stateProvider, "v1");

        initFormRoute($stateProvider, 'v1');

        initCommonRoute($stateProvider, 'v1');

        initHrRoute($stateProvider);

        initCadRoute($stateProvider);


        $stateProvider
            .state('dashboard', {
                url: "/dashboard",
                templateUrl: function () {
                    return "/dashboard";
                },
                controller: 'DashboardController as vm'
            })
            .state('noticeShow', {
                url: "/noticeShow",
                templateUrl: function () {
                    return "/noticeShow";
                },
                controller: 'NoticeShowController as vm'
            })
            .state('five.dashboard', {
                url: "/dashboard",
                templateUrl: function () {
                    return "/dashboard";
                },
                controller: 'FiveDashboardController as vm'
            });

        $stateProvider
            .state('finance', {
                url: "/finance",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('finance.entrance', {
                url: "/finance/entrance",
                templateUrl: function () {
                    return "/finance/entrance";
                },
                controller: 'FiveFinanceEntranceController as vm'
            })
            .state('finance.travelExpense', {
                url: "/finance/travelExpense",
                templateUrl: function () {
                    return "/finance/travelExpense";
                },
                controller: 'FiveFinanceTravelExpenseController as vm'
            })
            .state('finance.travelExpenseDetail', {
                url: "/finance/travelExpenseDetail?travelExpenseId",
                templateUrl: function () {
                    return "/finance/travelExpenseDetail";
                },
                controller: 'FiveFinanceTravelExpenseDetailController as vm'
            })

            .state('finance.travelExpenseFunction', {
                url: "/finance/travelExpense",
                templateUrl: function () {
                    return "/finance/travelExpense";
                },
                controller: 'FiveFinanceTravelExpenseFunctionController as vm'
            })
            .state('finance.travelExpenseFunctionDetail', {
                url: "/finance/travelExpenseDetail?travelExpenseId",
                templateUrl: function () {
                    return "/finance/travelExpenseDetail";
                },
                controller: 'FiveFinanceTravelExpenseFunctionDetailController as vm'
            })

            .state('finance.travelExpenseRed', {
                url: "/finance/travelExpenseRed",
                templateUrl: function () {
                    return "/finance/travelExpense";
                },
                controller: 'FiveFinanceTravelExpenseRedController as vm'
            })
            .state('finance.travelExpenseRedDetail', {
                url: "/finance/travelExpenseRedDetail?travelExpenseId",
                templateUrl: function () {
                    return "/finance/travelExpenseDetail";
                },
                controller: 'FiveFinanceTravelExpenseRedDetailController as vm'
            })

            .state('finance.travelExpenseBuild', {
                url: "/finance/travelExpenseBuild",
                templateUrl: function () {
                    return "/finance/travelExpense";
                },
                controller: 'FiveFinanceTravelExpenseBuildController as vm'
            })
            .state('finance.travelExpenseBuildDetail', {
                url: "/finance/travelExpenseBuildDetail?travelExpenseId",
                templateUrl: function () {
                    return "/finance/travelExpenseDetail";
                },
                controller: 'FiveFinanceTravelExpenseBuildDetailController as vm'
            })

            .state('finance.subpackagePayment', {
                url: "/finance/subpackagePayment",
                templateUrl: function () {
                    return "/finance/subpackagePayment";
                },
                controller: 'FiveFinanceSubpackagePaymentController as vm'
            })
            .state('finance.subpackagePaymentDetail', {
                url: "/finance/subpackagePaymentDetail?subpackagePaymentId",
                templateUrl: function () {
                    return "/finance/subpackagePaymentDetail";
                },
                controller: 'FiveFinanceSubpackagePaymentDetailController as vm'
            })

            .state('finance.reimburse', {
                url: "/finance/reimburse",
                templateUrl: function () {
                    return "/finance/reimburse";
                },
                controller: 'FiveFinanceReimburseController as vm'
            })
            .state('finance.reimburseDetail', {
                url: "/finance/reimburseDetail?reimburseId",
                templateUrl: function () {
                    return "/finance/reimburseDetail";
                },
                controller: 'FiveFinanceReimburseDetailController as vm'
            })
            .state('finance.reimburseFunction', {
                url: "/finance/reimburse",
                templateUrl: function () {
                    return "/finance/reimburse";
                },
                controller: 'FiveFinanceReimburseFunctionController as vm'
            })
            .state('finance.reimburseFunctionDetail', {
                url: "/finance/reimburseDetail?reimburseId",
                templateUrl: function () {
                    return "/finance/reimburseDetail";
                },
                controller: 'FiveFinanceReimburseFunctionDetailController as vm'
            })
            .state('finance.reimburseRed', {
                url: "/finance/reimburse",
                templateUrl: function () {
                    return "/finance/reimburse";
                },
                controller: 'FiveFinanceReimburseRedController as vm'
            })
            .state('finance.reimburseRedDetail', {
                url: "/finance/reimburseDetail?reimburseId",
                templateUrl: function () {
                    return "/finance/reimburseDetail";
                },
                controller: 'FiveFinanceReimburseRedDetailController as vm'
            })
            .state('finance.reimburseBuild', {
                url: "/finance/reimburseBuild",
                templateUrl: function () {
                    return "/finance/reimburse";
                },
                controller: 'FiveFinanceReimburseBuildController as vm'
            })
            .state('finance.reimburseBuildDetail', {
                url: "/finance/reimburseBuildDetail?reimburseId",
                templateUrl: function () {
                    return "/finance/reimburseDetail";
                },
                controller: 'FiveFinanceReimburseBuildDetailController as vm'
            })
            .state('finance.income', {
                url: "/income",
                templateUrl: function () {
                    return "/finance/income";
                },
                controller: 'FiveFinanceIncomeController as vm'
            })
            .state('finance.incomeLibrary', {
                url: "/incomeLibrary",
                templateUrl: function () {
                    return "/finance/incomeLibrary";
                },
                controller: 'FiveFinanceIncomeLibraryController as vm'
            })
            .state('finance.incomeDetail', {
                url: "/incomeDetail?incomeId",
                templateUrl: function () {
                    return "/finance/incomeDetail";
                },
                controller: 'FiveFinanceIncomeDetailController as vm'
            })

            .state('finance.invoice', {
                url: "/invoice",
                templateUrl: function () {
                    return "/finance/invoice";
                },
                controller: 'FiveFinanceInvoiceController as vm'
            })
            .state('finance.invoiceDetail', {
                url: "/invoiceDetail?invoiceId",
                templateUrl: function () {
                    return "/finance/invoiceDetail";
                },
                controller: 'FiveFinanceInvoiceDetailController as vm'
            })

            .state('finance.projectBudget', {
                url: "/projectBudget",
                templateUrl: function () {
                    return "/finance/projectBudget";
                },
                controller: 'FiveFinanceProjectBudgetController as vm'
            })
            .state('finance.projectBudgetDetail', {
                url: "/projectBudgetDetail?projectBudgetId",
                templateUrl: function () {
                    return "/finance/projectBudgetDetail";
                },
                controller: 'FiveFinanceProjectBudgetDetailController as vm'
            })

            .state('finance.deptBudget', {
                url: "/deptBudget",
                templateUrl: function () {
                    return "/finance/deptBudget";
                },
                controller: 'FiveFinanceDeptBudgetController as vm'
            })
            .state('finance.deptBudgetDetail', {
                url: "/deptBudgetDetail?deptBudgetId",
                templateUrl: function () {
                    return "/finance/deptBudgetDetail";
                },
                controller: 'FiveFinanceDeptBudgetDetailController as vm'
            })

            .state('finance.stampTax', {
                url: "/stampTax",
                templateUrl: function () {
                    return "/finance/stampTax";
                },
                controller: 'FiveFinanceStampTaxController as vm'
            })
            .state('finance.stampTaxDetail', {
                url: "/stampTaxDetail?stampTaxId",
                templateUrl: function () {
                    return "/finance/stampTaxDetail";
                },
                controller: 'FiveFinanceStampTaxDetailController as vm'
            })

            .state('finance.deptFund', {
                url: "/deptFund",
                templateUrl: function () {
                    return "/finance/deptFund";
                },
                controller: 'FiveFinanceDeptFundController as vm'
            })
            .state('finance.deptFundDetail', {
                url: "/deptFundDetail?deptFundId",
                templateUrl: function () {
                    return "/finance/deptFundDetail";
                },
                controller: 'FiveFinanceDeptFundDetailController as vm'
            })

            .state('finance.invoiceCancel', {
                url: "/invoiceCancel",
                templateUrl: function () {
                    return "/finance/invoiceCancel";
                },
                controller: 'FiveFinanceInvoiceCancelController as vm'
            })
            .state('finance.invoiceCancelDetail', {
                url: "/invoiceCancelDetail?invoiceCancelId",
                templateUrl: function () {
                    return "/finance/invoiceCancelDetail";
                },
                controller: 'FiveFinanceInvoiceCancelDetailController as vm'
            })
            .state('finance.invoiceCollection', {
                url: "/invoiceCollection",
                templateUrl: function () {
                    return "/finance/invoiceCollection";
                },
                controller: 'FiveFinanceInvoiceCollectionController as vm'
            })
            .state('finance.invoiceCollectionDetail', {
                url: "/invoiceCollectionDetail?invoiceCollectionId",
                templateUrl: function () {
                    return "/finance/invoiceCollectionDetail";
                },
                controller: 'FiveFinanceInvoiceCollectionDetailController as vm'
            })

            .state('finance.incomeConfirm', {
                url: "/incomeConfirm",
                templateUrl: function () {
                    return "/finance/incomeConfirm";
                },
                controller: 'FiveFinanceIncomeConfirmController as vm'
            })
            .state('finance.incomeConfirmDetail', {
                url: "/incomeConfirmDetail?incomeConfirmId",
                templateUrl: function () {
                    return "/finance/incomeConfirmDetail";
                },
                controller: 'FiveFinanceIncomeConfirmDetailController as vm'
            })
            .state('finance.incomeConfirmDetailAdd', {
                url: "/incomeConfirmDetail?incomeId",
                templateUrl: function () {
                    return "/finance/incomeConfirmDetail";
                },
                controller: 'FiveFinanceIncomeConfirmDetailAddController as vm'
            })
            .state('finance.node', {
                url: "/node",
                templateUrl: function () {
                    return "/finance/node";
                },
                controller: 'FiveFinanceNodeController as vm'
            })
            .state('finance.nodeDetail', {
                url: "/nodeDetail?nodeId",
                templateUrl: function () {
                    return "/finance/nodeDetail";
                },
                controller: 'FiveFinanceNodeDetailController as vm'
            })
            .state('finance.balance', {
                url: "/balance",
                templateUrl: function () {
                    return "/finance/balance";
                },
                controller: 'FiveFinanceBalanceController as vm'
            })
            .state('finance.balanceDetail', {
                url: "/balanceDetail?balanceId",
                templateUrl: function () {
                    return "/finance/balanceDetail";
                },
                controller: 'FiveFinanceBalanceDetailController as vm'
            })

            .state('finance.outSupply', {
                url: "/outSupply",
                templateUrl: function () {
                    return "/finance/outSupply";
                },
                controller: 'FiveFinanceOutSupplyController as vm'
            })
            .state('finance.outSupplyDetail', {
                url: "/outSupplyDetail?outSupplyId",
                templateUrl: function () {
                    return "/finance/outSupplyDetail";
                },
                controller: 'FiveFinanceOutSupplyDetailController as vm'
            })
            .state('finance.selfBank', {
                url: "/selfBank",
                templateUrl: function () {
                    return "/finance/selfBank";
                },
                controller: 'FiveFinanceSelfBankController as vm'
            })
            .state('finance.selfBankDetail', {
                url: "/selfBankDetail?selfBankId",
                templateUrl: function () {
                    return "/finance/selfBankDetail";
                },
                controller: 'FiveFinanceSelfBankDetailController as vm'
            })
            .state('finance.companyBank', {
                url: "/companyBank",
                templateUrl: function () {
                    return "/finance/companyBank";
                },
                controller: 'FiveFinanceCompanyBankController as vm'
            })
            .state('finance.companyBankDetail', {
                url: "/companyBankDetail?companyBankId",
                templateUrl: function () {
                    return "/finance/companyBankDetail";
                },
                controller: 'FiveFinanceCompanyBankDetailController as vm'
            })
            .state('finance.loan', {
                url: "/finance/loan",
                templateUrl: function () {
                    return "/finance/loan";
                },
                controller: 'FiveFinanceLoanController as vm'
            })
            .state('finance.loanDetail', {
                url: "/finance/loanDetail?loanId",
                templateUrl: function () {
                    return "/finance/loanDetail";
                },
                controller: 'FiveFinanceLoanDetailController as vm'
            })
            .state('finance.loanFunction', {
                url: "/finance/loan",
                templateUrl: function () {
                    return "/finance/loan";
                },
                controller: 'FiveFinanceLoanFunctionController as vm'
            })
            .state('finance.loanFunctionDetail', {
                url: "/finance/loanDetail?loanId",
                templateUrl: function () {
                    return "/finance/loanDetail";
                },
                controller: 'FiveFinanceLoanFunctionDetailController as vm'
            })
            .state('finance.loanRed', {
                url: "/finance/loan",
                templateUrl: function () {
                    return "/finance/loan";
                },
                controller: 'FiveFinanceLoanRedController as vm'
            })
            .state('finance.loanRedDetail', {
                url: "/finance/loanDetail?loanId",
                templateUrl: function () {
                    return "/finance/loanDetail";
                },
                controller: 'FiveFinanceLoanRedDetailController as vm'
            })
            .state('finance.loanBuild', {
                url: "/finance/loanBuild",
                templateUrl: function () {
                    return "/finance/loan";
                },
                controller: 'FiveFinanceLoanBuildController as vm'
            })
            .state('finance.loanBuildDetail', {
                url: "/finance/loanBuildDetail?loanId",
                templateUrl: function () {
                    return "/finance/loanDetail";
                },
                controller: 'FiveFinanceLoanBuildDetailController as vm'
            })
            .state('finance.refund', {
                url: "/finance/refund",
                templateUrl: function () {
                    return "/finance/refund";
                },
                controller: 'FiveFinanceRefundController as vm'
            })
            .state('finance.refundDetail', {
                url: "/finance/refundDetail?refundId",
                templateUrl: function () {
                    return "/finance/refundDetail";
                },
                controller: 'FiveFinanceRefundDetailController as vm'
            })
            .state('finance.transferAccounts', {
                url: "/finance/transferAccounts",
                templateUrl: function () {
                    return "/finance/transferAccounts";
                },
                controller: 'FiveFinanceTransferAccountsController as vm'
            })
            .state('finance.transferAccountsDetail', {
                url: "/finance/transferAccountsDetail?transferAccountsId",
                templateUrl: function () {
                    return "/finance/transferAccountsDetail";
                },
                controller: 'FiveFinanceTransferAccountsDetailController as vm'
            })
            .state('finance.transferAccountsRed', {
                url: "/finance/transferAccountsRed",
                templateUrl: function () {
                    return "/finance/transferAccounts";
                },
                controller: 'FiveFinanceTransferAccountsRedController as vm'
            })
            .state('finance.transferAccountsRedDetail', {
                url: "/finance/transferAccountsRedDetail?transferAccountsId",
                templateUrl: function () {
                    return "/finance/transferAccountsDetail";
                },
                controller: 'FiveFinanceTransferAccountsRedDetailController as vm'
            })
            .state('finance.transferAccountsBuild', {
                url: "/finance/transferAccountsBuild",
                templateUrl: function () {
                    return "/finance/transferAccounts";
                },
                controller: 'FiveFinanceTransferAccountsBuildController as vm'
            })
            .state('finance.transferAccountsBuildDetail', {
                url: "/finance/transferAccountsBuildDetail?transferAccountsId",
                templateUrl: function () {
                    return "/finance/transferAccountsDetail";
                },
                controller: 'FiveFinanceTransferAccountsBuildDetailController as vm'
            })
            .state('finance.transferFee', {
                url: "/finance/transferFee",
                templateUrl: function () {
                    return "/finance/transferFee";
                },
                controller: 'FiveFinanceTransferFeeController as vm'
            })
            .state('finance.transferFeeDetail', {
                url: "/finance/transferDetail?transferId",
                templateUrl: function () {
                    return "/finance/transferDetail";
                },
                controller: 'FiveFinanceTransferFeeDetailController as vm'
            })
            .state('finance.receipt', {
                url: "/finance/receipt",
                templateUrl: function () {
                    return "/finance/receipt";
                },
                controller: 'FiveFinanceReceiptController as vm'
            })
            .state('finance.receiptDetail', {
                url: "/finance/receiptDetail?receiptId",
                templateUrl: function () {
                    return "/finance/receiptDetail";
                },
                controller: 'FiveFinanceReceiptDetailController as vm'
            })
            .state('finance.backLetter', {
                url: "/finance/backLetter",
                templateUrl: function () {
                    return "/finance/backLetter";
                },
                controller: 'FiveFinanceBackLetterController as vm'
            })
            .state('finance.backLetterDetail', {
                url: "/finance/backLetterDetail?backLetterId",
                templateUrl: function () {
                    return "/finance/backLetterDetail";
                },
                controller: 'FiveFinanceBackLetterDetailController as vm'
            })
        ;
        $stateProvider
            .state('me', {
                url: "/me",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('me.edProject', {
                url: "/edProject",
                templateUrl: function () {
                    return "/me/edProject";
                },
                controller: 'MeEdProjectController as vm'
            })
            .state('me.all', {
                url: "/all",
                templateUrl: function () {
                    return "/me/all";
                },
                controller: 'MeAllController as vm'
            })
            .state('me.cpStep', {
                url: "/cpStep",
                templateUrl: function () {
                    return "/me/cpStep";
                },
                controller: 'MeCpStepController as vm'
            })
            .state('me.stampAll', {
                url: "/stampAll",
                templateUrl: function () {
                    return "/me/stampAll";
                },
                controller: 'MeDetailPartialStampAllController as vm'
            })
            .state('me.stampAllDetail', {
                url: "/stampAllDetail?stampId",
                templateUrl: function () {
                    return "/me/stampAllDetail";
                },
                controller: 'MeDetailPartialStampAllDetailController as vm'
            })
            .state('me.stampNoDetail', {
                url: "/stampNoDetail?stampId",
                templateUrl: function () {
                    return "/me/stampNoDetail";
                },
                controller: 'MeDetailPartialStampNoDetailController as vm'
            })
        ;


        //五洲财务管理-预算
        $stateProvider
            .state('budget', {
                url: "/budget",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('budget.fee', {
                url: "/budget/fee",
                templateUrl: function () {
                    return "/budget/fee";
                },
                controller: 'FiveBudgetFeeController as vm'
            })
            .state('budget.feeDetail', {
                url: "/budget/feeDetail?feeId",
                templateUrl: function () {
                    return "/budget/feeDetail";
                },
                controller: 'FiveBudgetFeeDetailController as vm'
            })
            .state('budget.feeChange', {
                url: "/budget/feeChange",
                templateUrl: function () {
                    return "/budget/feeChange";
                },
                controller: 'FiveBudgetFeeChangeController as vm'
            })
            .state('budget.feeChangeDetail', {
                url: "/budget/feeChangeDetail?feeId",
                templateUrl: function () {
                    return "/budget/feeChangeDetail";
                },
                controller: 'FiveBudgetFeeChangeDetailController as vm'
            })
            .state('budget.maintain', {
                url: "/budget/maintain",
                templateUrl: function () {
                    return "/budget/maintain";
                },
                controller: 'FiveBudgetMaintainController as vm'
            })
            .state('budget.maintainDetail', {
                url: "/budget/maintainDetail?maintainId",
                templateUrl: function () {
                    return "/budget/maintainDetail";
                },
                controller: 'FiveBudgetMaintainDetailController as vm'
            })
            .state('budget.business', {
                url: "/budget/business",
                templateUrl: function () {
                    return "/budget/business";
                },
                controller: 'FiveBudgetBusinessController as vm'
            })
            .state('budget.businessDetail', {
                url: "/budget/businessDetail?businessId",
                templateUrl: function () {
                    return "/budget/businessDetail";
                },
                controller: 'FiveBudgetBusinessDetailController as vm'
            })
            .state('budget.independent', {
                url: "/independent",
                templateUrl: function () {
                    return "/budget/independent";
                },
                controller: 'FiveBudgetIndependentController as vm'
            })
            .state('budget.independentDetail', {
                url: "/independentDetail?independentId",
                templateUrl: function () {
                    return "/budget/independentDetail";
                },
                controller: 'FiveBudgetIndependentDetailController as vm'
            })
            .state('budget.production', {
                url: "/production",
                templateUrl: function () {
                    return "/budget/production";
                },
                controller: 'FiveBudgetProductionController as vm'
            })
            .state('budget.productionDetail', {
                url: "/productionDetail?productionId",
                templateUrl: function () {
                    return "/budget/productionDetail";
                },
                controller: 'FiveBudgetProductionDetailController as vm'
            })
            .state('budget.function', {
                url: "/function",
                templateUrl: function () {
                    return "/budget/function";
                },
                controller: 'FiveBudgetFunctionController as vm'
            })
            .state('budget.functionDetail', {
                url: "/functionDetail?functionId",
                templateUrl: function () {
                    return "/budget/functionDetail";
                },
                controller: 'FiveBudgetFunctionDetailController as vm'
            })
            .state('budget.postExpense', {
                url: "/postExpense",
                templateUrl: function () {
                    return "/budget/postExpense";
                },
                controller: 'FiveBudgetPostExpenseController as vm'
            })
            .state('budget.postExpenseDetail', {
                url: "/postExpenseDetail?postExpenseId",
                templateUrl: function () {
                    return "/budget/postExpenseDetail";
                },
                controller: 'FiveBudgetPostExpenseDetailController as vm'
            })

            .state('budget.scientificFundsOut', {
                url: "/scientificFundsOut",
                templateUrl: function () {
                    return "/budget/scientificFundsOut";
                },
                controller: 'FiveBudgetScientificFundsOutController as vm'
            })
            .state('budget.scientificFundsOutDetail', {
                url: "/scientificFundsOutDetail?scientificFundsOutId",
                templateUrl: function () {
                    return "/budget/scientificFundsOutDetail";
                },
                controller: 'FiveBudgetScientificFundsOutDetailController as vm'
            })
            .state('budget.scientificFundsIn', {
                url: "/scientificFundsIn",
                templateUrl: function () {
                    return "/budget/scientificFundsIn";
                },
                controller: 'FiveBudgetScientificFundsInController as vm'
            })
            .state('budget.scientificFundsInDetail', {
                url: "/scientificFundsInDetail?scientificFundsInId",
                templateUrl: function () {
                    return "/budget/scientificFundsInDetail";
                },
                controller: 'FiveBudgetScientificFundsInDetailController as vm'
            })

            .state('budget.publicFunds', {
                url: "/publicFunds",
                templateUrl: function () {
                    return "/budget/publicFunds";
                },
                controller: 'FiveBudgetPublicFundsController as vm'
            })
            .state('budget.publicFundsDetail', {
                url: "/publicFundsDetail?publicFundsId",
                templateUrl: function () {
                    return "/budget/publicFundsDetail";
                },
                controller: 'FiveBudgetPublicFundsDetailController as vm'
            })

            .state('budget.capitalOut', {
                url: "/capitalOut",
                templateUrl: function () {
                    return "/budget/capitalOut";
                },
                controller: 'FiveBudgetCapitalOutController as vm'
            })
            .state('budget.capitalOutDetail', {
                url: "/capitalOutDetail?capitalOutId",
                templateUrl: function () {
                    return "/budget/capitalOutDetail";
                },
                controller: 'FiveBudgetCapitalOutDetailController as vm'
            })

            .state('budget.turnInRent', {
                url: "/turnInRent",
                templateUrl: function () {
                    return "/budget/turnInRent";
                },
                controller: 'FiveBudgetTurnInRentController as vm'
            })
            .state('budget.turnInRentDetail', {
                url: "/turnInRentDetail?turnInRentId",
                templateUrl: function () {
                    return "/budget/turnInRentDetail";
                },
                controller: 'FiveBudgetTurnInRentDetailController as vm'
            })
            .state('budget.turnIn', {
                url: "/budget/turnIn",
                templateUrl: function () {
                    return "/budget/turnIn";
                },
                controller: 'FiveBudgetTurnInController as vm'
            })
            .state('budget.turnInDetail', {
                url: "/budget/turnInDetail?turnInId",
                templateUrl: function () {
                    return "/budget/turnInDetail";
                },
                controller: 'FiveBudgetTurnInDetailController as vm'
            })

            .state('budget.laborCost', {
                url: "/budget/laborCost",
                templateUrl: function () {
                    return "/budget/laborCost";
                },
                controller: 'FiveBudgetLaborCostController as vm'
            })
            .state('budget.laborCostDetail', {
                url: "/budget/laborCostDetail?laborCostId",
                templateUrl: function () {
                    return "/budget/laborCostDetail";
                },
                controller: 'FiveBudgetLaborCostDetailController as vm'
            })
            .state('budget.staffNumber', {
                url: "/budget/staffNumber",
                templateUrl: function () {
                    return "/budget/staffNumber";
                },
                controller: 'FiveBudgetStaffNumberController as vm'
            })
            .state('budget.staffNumberDetail', {
                url: "/budget/staffNumberDetail?staffNumberId",
                templateUrl: function () {
                    return "/budget/staffNumberDetail";
                },
                controller: 'FiveBudgetStaffNumberDetailController as vm'
            })
            .state('budget.stock', {
                url: "/budget/stock",
                templateUrl: function () {
                    return "/budget/stock";
                },
                controller: 'FiveBudgetStockController as vm'
            })
            .state('budget.stockDetail', {
                url: "/budget/stockDetail?stockId",
                templateUrl: function () {
                    return "/budget/stockDetail";
                },
                controller: 'FiveBudgetStockDetailController as vm'
            })

        ;

        $stateProvider
            .state('business', {
                url: "/business",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('business.customer', {
                url: "/customer",
                templateUrl: function () {
                    return "/business/customer";
                },
                controller: 'BusinessCustomerController as vm'
            })

            .state('business.contract', {
                url: "/contract",
                templateUrl: function () {
                    return "/business/contract";
                },
                controller: 'BusinessContractController as vm'
            })
            .state('business.contractDetail', {
                url: "/contractDetail?contractId",
                templateUrl: function () {
                    return "/business/contractDetail";
                },
                controller: 'BusinessContractDetailController as vm'
            })
            .state('business.deptContract', {
                url: "/deptContract",
                templateUrl: function () {
                    return "/business/deptContract";
                },
                controller: 'BusinessDeptContractController as vm'
            })
            .state('business.bidApply', {
                url: "/bidApply",
                templateUrl: function () {
                    return "/business/bidApply";
                },
                controller: 'BusinessBidApplyController as vm'
            })
            .state('business.bidApplyDetail', {
                url: "/bidApplyDetail?bidApplyId",
                templateUrl: function () {
                    return "/business/bidApplyDetail";
                },
                controller: 'BusinessBidApplyDetailController as vm'
            })
            .state('business.bidAttend', {
                url: "/bidAttend",
                templateUrl: function () {
                    return "/business/bidAttend";
                },
                controller: 'BusinessBidAttendController as vm'
            })
            .state('business.bidAttendDetail', {
                url: "/bidAttendDetail?attendId",
                templateUrl: function () {
                    return "/business/bidAttendDetail";
                },
                controller: 'BusinessBidAttendDetailController as vm'
            })
            .state('business.bidProjectCharge', {
                url: "/bidProjectCharge",
                templateUrl: function () {
                    return "/business/bidProjectCharge";
                },
                controller: 'BusinessBidProjectChargeController as vm'
            })
            .state('business.bidProjectChargeDetail', {
                url: "/bidProjectChargeDetail?projectChargeId",
                templateUrl: function () {
                    return "/business/bidProjectChargeDetail";
                },
                controller: 'BusinessBidProjectChargeDetailController as vm'
            })
            .state('business.bidContract', {
                url: "/bidContract",
                templateUrl: function () {
                    return "/business/bidContract";
                },
                controller: 'BusinessBidContractController as vm'
            })
            .state('business.bidContractDetail', {
                url: "/bidContractDetail?contractId",
                templateUrl: function () {
                    return "/business/bidContractDetail";
                },
                controller: 'BusinessBidContractDetailController as vm'
            })
            .state('business.record', {
                url: "/record",
                templateUrl: function () {
                    return "/business/record";
                },
                controller: 'BusinessRecordController as vm'
            })
            .state('business.recordDetail', {
                url: "/recordDetail?recordId",
                templateUrl: function () {
                    return "/business/recordDetail";
                },
                controller: 'BusinessRecordDetailController as vm'
            })
            .state('five.businessContractReview', {
                url: "/contractReview",
                templateUrl: function () {
                    return "/business/contractReview";
                },
                controller: 'FiveBusinessContractReviewController as vm'
            })
            .state('five.businessContractReviewDetail', {
                url: "/contractReviewDetail?contractReviewId",
                templateUrl: function () {
                    return "/business/contractReviewDetail";
                },
                controller: 'FiveBusinessContractReviewDetailController as vm'
            })
            .state('five.businessContractLibrary', {
                url: "/contractLibrary",
                templateUrl: function () {
                    return "/business/contractLibrary";
                },
                controller: 'FiveBusinessContractLibraryController as vm'
            })
            .state('five.businessContractLibraryDetail', {
                url: "/contractLibraryDetail?contractLibraryId",
                templateUrl: function () {
                    return "/business/contractLibraryDetail";
                },
                controller: 'FiveBusinessContractLibraryDetailController as vm'
            })
            .state('five.businessContractLibrarySubpackage', {
                url: "/contractLibrarySubpackage",
                templateUrl: function () {
                    return "/business/contractLibrarySubpackage";
                },
                controller: 'FiveBusinessContractLibrarySubpackageController as vm'
            })
            .state('five.businessContractLibrarySubpackageDetail', {
                url: "/contractLibrarySubpackageDetail?contractLibrarySubpackageId",
                templateUrl: function () {
                    return "/business/contractLibrarySubpackageDetail";
                },
                controller: 'FiveBusinessContractLibrarySubpackageDetailController as vm'
            })
            .state('five.businessSupplier', {
                url: "/supplier",
                templateUrl: function () {
                    return "/business/supplier";
                },
                controller: 'FiveBusinessSupplierController as vm'
            })
            .state('five.businessSupplierDetail', {
                url: "/supplierDetail?supplierId",
                templateUrl: function () {
                    return "/business/supplierDetail";
                },
                controller: 'FiveBusinessSupplierDetailController as vm'
            })
            .state('five.businessSubpackage', {
                url: "/subpackage",
                templateUrl: function () {
                    return "/business/subpackage";
                },
                controller: 'FiveBusinessSubpackageController as vm'
            })
            .state('five.businessSubpackageDetail', {
                url: "/subpackageDetail?subpackageId",
                templateUrl: function () {
                    return "/business/subpackageDetail";
                },
                controller: 'FiveBusinessSubpackageDetailController as vm'
            })
            .state('five.businessPurchase', {
                url: "/purchase",
                templateUrl: function () {
                    return "/business/purchase";
                },
                controller: 'FiveBusinessPurchaseController as vm'
            })
            .state('five.businessPurchaseDetail', {
                url: "/purchaseDetail?purchaseId",
                templateUrl: function () {
                    return "/business/purchaseDetail";
                },
                controller: 'FiveBusinessPurchaseDetailController as vm'
            })
            .state('five.businessIncome', {
                url: "/income",
                templateUrl: function () {
                    return "/business/income";
                },
                controller: 'FiveBusinessIncomeController as vm'
            })
            .state('five.businessIncomeDetail', {
                url: "/incomeDetail?incomeId",
                templateUrl: function () {
                    return "/business/incomeDetail";
                },
                controller: 'FiveBusinessIncomeDetailController as vm'
            })
            .state('five.businessOutAssist', {
                url: "/outAssist",
                templateUrl: function () {
                    return "/business/outAssist";
                },
                controller: 'FiveBusinessOutAssistController as vm'
            })
            .state('five.businessOutAssistDetail', {
                url: "/outAssistDetail?outAssistId",
                templateUrl: function () {
                    return "/business/outAssistDetail";
                },
                controller: 'FiveBusinessOutAssistDetailController as vm'
            })

            .state('five.businessCooperationDelegation', {
                url: "/cooperationDelegation",
                templateUrl: function () {
                    return "/business/cooperationDelegation";
                },
                controller: 'FiveBusinessCooperationDelegationController as vm'
            })
            .state('five.businessCooperationDelegationDetail', {
                url: "/cooperationDelegationDetail?cooperationDelegationId",
                templateUrl: function () {
                    return "/business/cooperationDelegationDetail";
                },
                controller: 'FiveBusinessCooperationDelegationDetailController as vm'
            })

            .state('five.businessCooperationContract', {
                url: "/cooperationContract",
                templateUrl: function () {
                    return "/business/cooperationContract";
                },
                controller: 'FiveBusinessCooperationContractController as vm'
            })
            .state('five.businessCooperationContractDetail', {
                url: "/cooperationContractDetail?cooperationContractId",
                templateUrl: function () {
                    return "/business/cooperationContractDetail";
                },
                controller: 'FiveBusinessCooperationContractDetailController as vm'
            })
        ;


        $stateProvider
            .state('sys', {
                url: "/sys",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('sys.request', {
                url: "/request",
                templateUrl: function () {
                    return "/sys/request/index";
                },
                controller: 'SysRequestController as vm'
            })
            .state('sys.code', {
                url: "/code",
                templateUrl: function () {
                    return "/sys/code";
                },
                controller: 'SysCodeController as vm'
            })
            .state('sys.dept', {
                url: "/dept",
                templateUrl: function () {
                    return "/sys/dept/index";
                },
                controller: 'SysDeptController as vm'
            })
            .state('sys.aclModule', {
                url: "/aclModule",
                templateUrl: function () {
                    return "/sys/aclModule/index";
                },
                controller: 'SysAclModuleController as vm'
            })
            .state('sys.role', {
                url: "/role",
                templateUrl: function () {
                    return "/sys/role/index";
                },
                controller: 'SysRoleController as vm'
            })
            .state('sys.attach', {
                url: "/attach",
                templateUrl: function () {
                    return "/sys/attach";
                },
                controller: 'SysAttachController as vm'
            })
            .state('sys.edQuestion', {
                url: "/edQuestion",
                templateUrl: function () {
                    return "/sys/edQuestion/index";
                },
                controller: 'SysEdQuestionController as vm'
            })
            .state('sys.plugin', {
                url: "/plugin",
                templateUrl: function () {
                    return "/sys/plugin";
                },
                controller: 'SysPluginController as vm'
            })
            .state('sys.dwgPicture', {
                url: "/dwgPicture",
                templateUrl: function () {
                    return "/sys/dwgPicture";
                },
                controller: 'SysDwgPictureController as vm'
            })
            .state('sys.config', {
                url: "/config",
                templateUrl: function () {
                    return "/sys/config";
                },
                controller: 'SysConfigController as vm'
            })
            .state('sys.software', {
                url: "/software",
                templateUrl: function () {
                    return "/sys/software";
                },
                controller: 'SysSoftwareController as vm'
            })
            .state('sys.recoverFile', {
                url: "/recoverFile",
                templateUrl: function () {
                    return "/sys/recoverFile";
                },
                controller: 'SysRecoverFileController as vm'
            })
            .state('sys.recoverCpFile', {
                url: "/recoverCpFile",
                templateUrl: function () {
                    return "/sys/recoverCpFile";
                },
                controller: 'SysRecoverCpFileController as vm'
            })
            .state('sys.me', {
                url: "/me",
                templateUrl: function () {
                    return "/sys/me";
                },
                controller: 'SysMeController as vm'
            })
            .state('sys.schedule', {
                url: "/schedule",
                templateUrl: function () {
                    return "/sys/schedule";
                },
                controller: 'SysScheduleController as vm'
            });


        $stateProvider
            .state('bid', {
                url: "/bid",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('bid.attend', {
                url: "/attend",
                templateUrl: function () {
                    return "/bid/attend";
                },
                controller: 'BidAttendController as vm'
            })
            .state('bid.success', {
                url: "/success",
                templateUrl: function () {
                    return "/bid/success";
                },
                controller: 'BidSuccessController as vm'
            })
            .state('bid.request', {
                url: "/request",
                templateUrl: function () {
                    return "/bid/request";
                },
                controller: 'BidRequestController as vm'
            })
            .state('bid.requestDetail', {
                url: "/requestDetail",
                templateUrl: function () {
                    return "/bid/request-detail";
                },
                controller: 'BidRequestDetailController as vm'
            })
            .state('bid.review', {
                url: "/review",
                templateUrl: function () {
                    return "/bid/review";
                },
                controller: 'BidReviewController as vm'
            })
            .state('bid.reviewDetail', {
                url: "/reviewDetail",
                templateUrl: function () {
                    return "/bid/review-detail";
                },
                controller: 'BidReviewDetailController as vm'
            });



        $stateProvider
            .state('cooper', {
                url: "/cooper",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('cooper.project', {
                url: "/project",
                templateUrl: function () {
                    return "/cooper/project/index";
                },
                controller: 'CooperProjectController as vm'
            });


        $stateProvider
            .state('ed', {
                url: "/ed",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('ed.project', {
                url: "/project",
                templateUrl: function () {
                    return "/ed/project/index";
                },
                controller: 'EdProjectController as vm'
            })
            .state('ed.detail', {
                url: "/detail?id",
                templateUrl: function () {
                    return "/ed/project/detail";
                },
                controller: 'EdDetailController as vm'
            })
            .state('ed.detail.show', {
                url: "/show?id",
                templateUrl: function () {
                    return "/ed/project/partial-show";
                },
                controller: 'EdDetailPartialShowController as vm'
            })
            .state('ed.detail.stepShow', {
                url: "/stepShow?nodeId",
                templateUrl: function () {
                    return "/ed/project/partial-stepShow";
                },
                controller: 'EdDetailPartialStepShowController as vm'
            })
            .state('ed.detail.step', {
                url: "/step?nodeId",
                templateUrl: function () {
                    return "/ed/project/partial-step";
                },
                controller: 'EdDetailPartialStepController as vm'
            })
            .state('ed.detail.stepDetail', {
                url: "/stepDetail?stepId",
                templateUrl: function () {
                    return "/ed/project/partial-stepDetail";
                },
                controller: 'EdDetailPartialStepDetailController as vm'
            })

            .state('ed.detail.stampEd', {
                url: "/stampEd?nodeId",
                templateUrl: function () {
                    return "/ed/project/partial-stampEd";
                },
                controller: 'EdDetailPartialStampEdController as vm'
            })
            .state('ed.detail.stampEdDetail', {
                url: "/stampEdDetail?stampEdId",
                templateUrl: function () {
                    return "/ed/project/partial-stampEdDetail";
                },
                controller: 'EdDetailPartialStampEdDetailController as vm'
            })

        ;


        $stateProvider
            .state('five', {
                url: "/five",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('five.businessCustomer', {
                url: "/businessCustomer",
                templateUrl: function () {
                    return "/five/business/customer";
                },
                controller: 'FiveBusinessCustomerController as vm'
            })
            .state('five.businessCustomerDetail', {
                url: "/businessCustomerDetail?customerId",
                templateUrl: function () {
                    return "/five/business/customerDetail";
                },
                controller: 'FiveBusinessCustomerDetailController as vm'
            })
            .state('five.businessChangeCustomer', {
                url: "/businessChangeCustomer",
                templateUrl: function () {
                    return "/five/business/changeCustomer";
                },
                controller: 'FiveBusinessChangeCustomerController as vm'
            })
            .state('five.businessChangeCustomerDetail', {
                url: "/businessChangeCustomerDetail?changeCustomerId",
                templateUrl: function () {
                    return "/five/business/changeCustomerDetail";
                },
                controller: 'FiveBusinessChangeCustomerDetailController as vm'
            })
            .state('five.businessChangeSupplier', {
                url: "/businessChangeSupplier",
                templateUrl: function () {
                    return "/five/business/changeSupplier";
                },
                controller: 'FiveBusinessChangeSupplierController as vm'
            })
            .state('five.businessChangeSupplierDetail', {
                url: "/businessChangeSupplierDetail?changeSupplierId",
                templateUrl: function () {
                    return "/five/business/changeSupplierDetail";
                },
                controller: 'FiveBusinessChangeSupplierDetailController as vm'
            })

            .state('five.businessRecord', {
                url: "/businessRecord",
                templateUrl: function () {
                    return "/five/business/record";
                },
                controller: 'FiveBusinessRecordController as vm'
            })
            .state('five.businessRecordDetail', {
                url: "/businessRecordDetail?recordId",
                templateUrl: function () {
                    return "/five/business/recordDetail";
                },
                controller: 'FiveBusinessRecordDetailController as vm'
            })
            .state('five.businessPreContract', {
                url: "/businessPreContract",
                templateUrl: function () {
                    return "/five/business/preContract";
                },
                controller: 'FiveBusinessPreContractController as vm'
            })
            .state('five.businessPreContractDetail', {
                url: "/businessPreContractDetail?preContractId",
                templateUrl: function () {
                    return "/five/business/preContractDetail";
                },
                controller: 'FiveBusinessPreContractDetailController as vm'
            })


            .state('five.businessAdvance', {
                url: "/advance",
                templateUrl: function () {
                    return "/business/advance";
                },
                controller: 'FiveBusinessAdvanceController as vm'
            })
            .state('five.businessAdvanceDetail', {
                url: "/advanceDetail?advanceId",
                templateUrl: function () {
                    return "/business/advanceDetail";
                },
                controller: 'FiveBusinessAdvanceDetailController as vm'
            })

            .state('five.businessAdvanceCollect', {
                url: "/advanceCollect",
                templateUrl: function () {
                    return "/business/advanceCollect";
                },
                controller: 'FiveBusinessAdvanceCollectController as vm'
            })
            .state('five.businessAdvanceCollectDetail', {
                url: "/advanceCollectDetail?advanceCollectId",
                templateUrl: function () {
                    return "/business/advanceCollectDetail";
                },
                controller: 'FiveBusinessAdvanceCollectDetailController as vm'
            })

            .state('five.businessTenderDocumentReview', {
                url: "/tenderDocumentReview",
                templateUrl: function () {
                    return "/business/tenderDocumentReview";
                },
                controller: 'FiveBusinessTenderDocumentReviewController as vm'
            })
            .state('five.businessTenderDocumentReviewDetail', {
                url: "/tenderDocumentReviewDetail?tenderId",
                templateUrl: function () {
                    return "/business/tenderDocumentReviewDetail";
                },
                controller: 'FiveBusinessTenderDocumentReviewDetailController as vm'
            })

            .state('five.businessInputContract', {
                url: "/businessInputContract",
                templateUrl: function () {
                    return "/five/business/inputContract";
                },
                controller: 'FiveBusinessInputContractController as vm'
            })
            .state('five.businessInputContractDetail', {
                url: "/businessInputContractDetail?inputContractId",
                templateUrl: function () {
                    return "/five/business/inputContractDetail";
                },
                controller: 'FiveBusinessInputContractDetailController as vm'
            })
            .state('five.businessContract', {
                url: "/businessContract",
                templateUrl: function () {
                    return "/five/business/contract";
                },
                controller: 'FiveBusinessContractController as vm'
            })
            .state('five.businessContractDetail', {
                url: "/businessContractDetail?contractId",
                templateUrl: function () {
                    return "/five/business/contractDetail";
                },
                controller: 'FiveBusinessContractDetailController as vm'
            })

            .state('five.meContract', {
                url: "/meContract",
                templateUrl: function () {
                    return "/five/me/contract";
                },
                controller: 'FiveMeContractController as vm'
            })
            .state('five.meStamp', {
                url: "/meStamp",
                templateUrl: function () {
                    return "/five/me/stamp";
                },
                controller: 'FiveMeStampController as vm'
            })
            .state('five.meStampDetail', {
                url: "/meStampDetail?stampId",
                templateUrl: function () {
                    return "/five/me/stampDetail";
                },
                controller: 'FiveMeStampDetailController as vm'
            })

            .state('five.hrQualify', {
                url: "/hr/qualify",
                templateUrl: function () {
                    return "/five/hr/qualify";
                },
                controller: 'FiveHrQualifyController as vm'
            })
            .state('five.hrQualifyDetail', {
                url: "/hr/qualifyDetail?qualifyId",
                templateUrl: function () {
                    return "/five/hr/qualifyDetail";
                },
                controller: 'FiveHrQualifyDetailController as vm'
            })
            .state('five.hrQualifyApply', {
                url: "/hr/qualifyApply",
                templateUrl: function () {
                    return "/five/hr/qualifyApply";
                },
                controller: 'FiveHrQualifyApplyController as vm'
            })
            .state('five.hrQualifyApplyDetail', {
                url: "/hr/qualifyApplyDetail?qualifyApplyId",
                templateUrl: function () {
                    return "/five/hr/qualifyApplyDetail";
                },
                controller: 'FiveHrQualifyApplyDetailController as vm'
            })
            .state('five.hrExternalApply', {
                url: "/hr/externalApply",
                templateUrl: function () {
                    return "/five/hr/externalApply";
                },
                controller: 'FiveHrExternalApplyController as vm'
            })
            .state('five.hrExternalApplyDetail', {
                url: "/hr/externalApplyDetail?id",
                templateUrl: function () {
                    return "/five/hr/externalApplyDetail";
                },
                controller: 'FiveHrExternalApplyDetailController as vm'
            })
            .state('five.hrChiefApply', {
                url: "/apply/chiefApply",
                templateUrl: function () {
                    return "/five/hr/chiefApply";
                },
                controller: 'FiveHrChiefApplyController as vm'
            })
            .state('five.hrChiefApplyDetail', {
                url: "/hr/chiefApplyDetail?id",
                templateUrl: function () {
                    return "/five/hr/chiefApplyDetail";
                },
                controller: 'FiveHrChiefApplyDetailController as vm'
            })
            .state('five.hrProChiefApply', {
                url: "/hr/proChiefApply",
                templateUrl: function () {
                    return "/five/hr/chiefApply";
                },
                controller: 'FiveHrProChiefApplyController as vm'
            })
            .state('five.hrProChiefApplyDetail', {
                url: "/hr/proChiefApplyDetail?id",
                templateUrl: function () {
                    return "/five/hr/chiefApplyDetail";
                },
                controller: 'FiveHrProChiefApplyDetailController as vm'
            })
            .state('five.hrApproveApply', {
                url: "/hr/approveApply",
                templateUrl: function () {
                    return "/five/hr/approveApply";
                },
                controller: 'FiveHrApproveApplyController as vm'
            })
            .state('five.hrApproveApplyDetail', {
                url: "/hr/approveApplyDetail?qualifyApplyId",
                templateUrl: function () {
                    return "/five/hr/approveApplyDetail";
                },
                controller: 'FiveHrApproveApplyDetailController as vm'
            })

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
            .state('five.oaFileSynergy', {
                url: "/oa/oaFileSynergy",
                templateUrl: function () {
                    return "/five/oa/fileSynergy";
                },
                controller: 'FiveOaFileSynergyController as vm'
            })
            .state('five.oaFileSynergyDetail', {
                url: "/oa/oaFileSynergyDetail?synergyId",
                templateUrl: function () {
                    return "/five/oa/fileSynergyDetail";
                },
                controller: 'FiveOaFileSynergyDetailController as vm'
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
            .state('five.oaDecisionMakingList', {
                url: "/oa/decisionMakingList",
                templateUrl: function () {
                    return "/five/oa/decisionMakingList";
                },
                controller: 'FiveOaDecisionMakingListController as vm'
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
            .state('five.oaCarMaintain', {
                url: "/oa/oaCarMaintain",
                templateUrl: function () {
                    return "/five/oa/carMaintain";
                },
                controller: 'FiveOaCarMaintainController as vm'
            })
            .state('five.oaCarMaintainDetail', {
                url: "/oa/oaCarMaintainDetail?id",
                templateUrl: function () {
                    return "/five/oa/carMaintainDetail";
                },
                controller: 'FiveOaCarMaintainDetailController as vm'
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

            .state('five.oaMeetingArrange', {
                url: "/oa/oaMeetingArrange",
                templateUrl: function () {
                    return "/five/oa/meetingArrange";
                },
                controller: 'FiveOaMeetingArrangeController as vm'
            })
            .state('five.oaMeetingArrangeDetail', {
                url: "/oa/oaMeetingArrangeDetail?id",
                templateUrl: function () {
                    return "/five/oa/meetingArrangeDetail";
                },
                controller: 'FiveOaMeetingArrangeDetailController as vm'
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
            //办公家具采购
            .state('five.oaFurniturePurchase', {
                url: "/oa/oaFurniturePurchase",
                templateUrl: function () {
                    return "/five/asset/furniturePurchase";
                },
                controller: 'FiveOaFurniturePurchaseController as vm'
            })
            .state('five.oaFurniturePurchaseDetail', {
                url: "/oa/oaFurniturePurchaseDetail?furnitureId",
                templateUrl: function () {
                    return "/five/asset/furniturePurchaseDetail";
                },
                controller: 'FiveOaFurniturePurchaseDetailController as vm'
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
            .state('five.oaComputerPersonRepair', {
                url: "/oa/computerPersonRepair",
                templateUrl: function () {
                    return "/five/oa/computerPersonRepair";
                },
                controller: 'FiveOaComputerPersonRepairController as vm'
            })
            .state('five.oaComputerPersonRepairDetail', {
                url: "/oa/computerPersonRepairDetail?computerPersonRepairId",
                templateUrl: function () {
                    return "/five/oa/computerPersonRepairDetail";
                },
                controller: 'FiveOaComputerPersonRepairDetailController as vm'
            })
            .state('five.oaSecretSystem', {
                url: "/oa/secretSystem",
                templateUrl: function () {
                    return "/five/oa/secretSystem";
                },
                controller: 'FiveOaSecretSystemController as vm'
            })
            .state('five.oaSecretSystemDetail', {
                url: "/oa/oaSecretSystemDetail?id",
                templateUrl: function () {
                    return "/five/oa/secretSystemDetail ";
                },
                controller: 'FiveOaSecretSystemDetailController as vm'
            })

            .state('five.oaComputerSafe', {
                url: "/oa/computerSafe",
                templateUrl: function () {
                    return "/five/oa/computerSafe";
                },
                controller: 'FiveOaComputerSafeController as vm'
            })
            .state('five.oaComputerSafeDetail', {
                url: "/oa/computerSafeDetail?computerSafeId",
                templateUrl: function () {
                    return "/five/oa/computerSafeDetail";
                },
                controller: 'FiveOaComputerSafeDetailController as vm'
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
            .state('five.oaInformationPlan', {
                url: "/oa/oaInformationPlan",
                templateUrl: function () {
                    return "/five/oa/informationPlan";
                },
                controller: 'FiveOaInformationPlanController as vm'
            })
            .state('five.oaInformationPlanDetail', {
                url: "/oa/oaInformationPlanDetail?planId",
                templateUrl: function () {
                    return "/five/oa/informationPlanDetail";
                },
                controller: 'FiveOaInformationPlanDetailController as vm'
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

            .state('five.oaInformationEquipmentExamineList', {
                url: "/oa/oaInformationEquipmentExamineList",
                templateUrl: function () {
                    return "/five/oa/oaInformationEquipmentExamineList";
                },
                controller: 'FiveOaInformationEquipmentExamineListController as vm'
            })
            .state('five.oaInformationEquipmentExamineListDetail', {
                url: "/oa/oaInformationEquipmentExamineListDetail?listId",
                templateUrl: function () {
                    return "/five/oa/oaInformationEquipmentExamineListDetail";
                },
                controller: 'FiveOaInformationEquipmentExamineListDetailController as vm'
            })

            .state('five.oaPrivilegeManagement', {
                url: "/oa/oaPrivilegeManagement",
                templateUrl: function () {
                    return "/five/oa/oaPrivilegeManagement";
                },
                controller: 'FiveOaPrivilegeManagementController as vm'
            })
            .state('five.oaPrivilegeManagementDetail', {
                url: "/oa/oaPrivilegeManagementDetail?privilegeManagementId",
                templateUrl: function () {
                    return "/five/oa/oaPrivilegeManagementDetail";
                },
                controller: 'FiveOaPrivilegeManagementDetailController as vm'
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

            //外部科研项目申请
            .state('five.oaExternalResearchProjectApply',{
                url:"/oa/oaExternalResearchProjectApply",
                templateUrl:function () {
                    return"/five/oa/externalResearchProjectApply";
                },
                controller:'FiveOaExternalResearchProjectApplyController as vm'

            })
            .state('five.oaExternalResearchProjectApplyDetail',{
                url:"/oa/oaExternalResearchProjectApplyDetail?projectId",
                templateUrl:function () {
                    return"/five/oa/externalResearchProjectApplyDetail";
                },
                controller:'FiveOaExternalResearchProjectApplyDetailController as vm'

            })

            //外部标准规范、图集项目申请
            .state('five.oaExternalStandardApply',{
                url:"/oa/oaExternalStandardApply",
                templateUrl:function () {
                    return"/five/oa/externalStandardApply";
                },
                controller:'FiveOaExternalStandardApplyController as vm'

            })
            .state('five.oaExternalStandardApplyDetail',{
                url:"/oa/oaExternalStandardApplyDetail?externalStandardApplyId",
                templateUrl:function () {
                    return"/five/oa/externalStandardApplyDetail";
                },
                controller:'FiveOaExternalStandardApplyDetailController as vm'

            })
            //内部项目申请
            .state('five.oaInlandProjectApply',{
                url:"/oa/oaInlandProjectApply",
                templateUrl:function () {
                    return"/five/oa/inlandProjectApply";
                },
                controller:'FiveOaInlandProjectApplyController as vm'

            })
            .state('five.oaInlandProjectApplyDetail',{
                url:"/oa/oaInlandProjectApplyDetail?applyId",
                templateUrl:function () {
                    return"/five/oa/inlandProjectApplyDetail";
                },
                controller:'FiveOaInlandProjectApplyDetailController as vm'

            })
            //科技开发费项目评审
            .state('five.oaResearchProjectReview',{
                url:"/oa/oaResearchProjectReview",
                templateUrl:function () {
                    return"/five/oa/oaResearchProjectReview";
                },
                controller:'FiveOaResearchProjectReviewController as vm'

            })
            .state('five.oaResearchProjectReviewDetail',{
                url:"/oa/oaResearchProjectReviewDetail?projectId",
                templateUrl:function () {
                    return"/five/oa/oaResearchProjectReviewDetail";
                },
                controller:'FiveOaResearchProjectReviewDetailController as vm'

            })
            //科研项目库
            .state('five.oaResearchProjectLibrary',{
                url:"/oa/oaResearchProjectLibrary",
                templateUrl:function () {
                    return"/five/oa/oaResearchProjectLibrary";
                },
                controller:'FiveOaResearchProjectLibraryController as vm'

            })
            .state('five.oaResearchProjectLibraryDetail',{
                url:"/oa/oaResearchProjectLibraryDetail?libraryId",
                templateUrl:function () {
                    return"/five/oa/oaResearchProjectLibraryDetail";
                },
                controller:'FiveOaResearchProjectLibraryDetailController as vm'

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
            })
            //职工充值卡变动
            .state('five.oaCardChange', {
                url: "oa/oaCardChange",
                templateUrl: function () {
                    return "five/asset/cardChange";
                },
                controller: 'FiveOaCardChangeController as vm'
            })
            .state('five.oaCardChangeDetail', {
                url: "oa/oaCardChangeDetail?changeId",
                templateUrl: function () {
                    return "five/asset/cardChangeDetail";
                },
                controller: 'FiveOaCardChangeDetailController as vm'
            })
            //设备验收
            .state('five.oaEquipmentAcceptance', {
                url: "oa/oaEquipmentAcceptance",
                templateUrl: function () {
                    return "five/asset/equipmentAcceptance";
                },
                controller: 'FiveOaEquipmentAcceptanceController as vm'
            })
            .state('five.oaEquipmentAcceptanceDetail', {
                url: "oa/oaEquipmentAcceptanceDetail?equipmentId",
                templateUrl: function () {
                    return "five/asset/equipmentAcceptanceDetail";
                },
                controller: 'FiveOaEquipmentAcceptanceDetailController as vm'
            })


            //非密计算机及信息化设备台帐
            .state('five.oaAssetComputer', {
                url: "oa/oaAssetComputer",
                templateUrl: function () {
                    return "five/asset/assetComputer";
                },
                controller: 'FiveAssetComputerController as vm'
            })
            .state('five.oaAssetComputerDetail', {
                url: "oa/oaAssetComputerDetail?computerId",
                templateUrl: function () {
                    return "five/asset/assetComputerDetail";
                },
                controller: 'FiveAssetComputerDetailController as vm'
            })

            //非密计算机及信息化设备台帐(补录)
            .state('five.oaTrackOaAssetComputer',{
                url:"oa/oaTrackOaAssetComputer",
                templateUrl:function () {
                    return"five/asset/trackAssetComputer"
                },
                controller:"FiveTrackAssetComputerController as vm"
            })
            .state('five.oaTrackAssetComputerDetail', {
                url: "oa/oaTrackAssetComputerDetail?computerId",
                templateUrl: function () {
                    return "five/asset/trackAssetComputerDetail";
                },
                controller: 'FiveTrackAssetComputerDetailController as vm'
            })


            .state('five.assetAllot', {
                url: "/asset/assetAllot",
                templateUrl: function () {
                    return "five/asset/assetAllot";
                },
                controller: 'FiveAssetAllotController as vm'
            })
            .state('five.assetAllotDetail', {
                url: "/asset/assetAllotDetail?assetAllotId",
                templateUrl: function () {
                    return "five/asset/assetAllotDetail";
                },
                controller: 'FiveAssetAllotDetailController as vm'
            })

            .state('five.assetScrap', {
                url: "/asset/assetScrap",
                templateUrl: function () {
                    return "five/asset/assetScrap";
                },
                controller: 'FiveAssetScrapController as vm'
            })
            .state('five.assetScrapDetail', {
                url: "/asset/assetScrapDetail?assetScrapId",
                templateUrl: function () {
                    return "five/asset/assetScrapDetail";
                },
                controller: 'FiveAssetScrapDetailController as vm'
            })

            .state('five.computerScrap', {
                url: "/asset/computerScrap",
                templateUrl: function () {
                    return "five/asset/computerScrap";
                },
                controller: 'FiveComputerScrapController as vm'
            })
            .state('five.computerScrapDetail', {
                url: "/asset/computerScrapDetail?computerScrapId",
                templateUrl: function () {
                    return "five/asset/computerScrapDetail";
                },
                controller: 'FiveComputerScrapDetailController as vm'
            })


        $stateProvider
            .state('supervise', {
                url: "/supervise",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('five.supervise', {
                url: "/supervise/supervise",
                templateUrl: function () {
                    return "/five/supervise/supervise";
                },
                controller: 'FiveSuperviseController as vm'
            })
            .state('five.superviseDetail', {
                url: "/supervise/superviseDetail?superviseId",
                templateUrl: function () {
                    return "/five/supervise/superviseDetail";
                },
                controller: 'FiveSuperviseDetailController as vm'
            })
            .state('five.superviseYear', {
                url: "/supervise/superviseYear",
                templateUrl: function () {
                    return "/five/supervise/superviseYear";
                },
                controller: 'FiveSuperviseYearController as vm'
            })
            .state('five.superviseYearDetail', {
                url: "/supervise/superviseYearDetail?superviseId",
                templateUrl: function () {
                    return "/five/supervise/superviseYearDetail";
                },
                controller: 'FiveSuperviseYearDetailController as vm'
            })
            .state('five.superviseFile', {
                url: "/supervise/superviseFile",
                templateUrl: function () {
                    return "/five/supervise/superviseFile";
                },
                controller: 'FiveSuperviseFileController as vm'
            })
            .state('five.superviseFileDetail', {
                url: "/supervise/superviseFileDetail?superviseId",
                templateUrl: function () {
                    return "/five/supervise/superviseFileDetail";
                },
                controller: 'FiveSuperviseFileDetailController as vm'
            })
            .state('five.superviseDetailChild', {
                url: "/supervise/superviseDetailChild?detailId",
                templateUrl: function () {
                    return "/five/supervise/superviseDetailChild";
                },
                controller: 'FiveSuperviseDetailChildController as vm'
            })

        $stateProvider
            .state('oa', {
                url: "/oa",
                template: "<div ui-view></div>",
                abstract: true
            })
            .state('oa.functionEntrance', {
                url: "/oa/functionEntrance?moduleId",
                templateUrl: function () {
                    return "/oa/functionEntrance";
                },
                controller: 'FunctionEntranceController as vm'
            })
            .state('oa.link', {
                url: "/oa/link",
                templateUrl: function () {
                    return "/oa/link";
                },
                controller: 'OaLinkController as vm'
            })
            .state('oa.notice', {
                url: "/notice",
                templateUrl: function () {
                    return "/oa/notice";
                },
                controller: 'OaNoticeController as vm'
            })
            .state('oa.noticeDetail', {
                url: "/noticeDetail?id",
                templateUrl: function () {
                    return "/oa/noticeDetail";
                },
                controller: 'OaNoticeDetailController as vm'
            })

            .state('oa.noticeApply', {
                url: "/noticeApply",
                templateUrl: function () {
                    return "/oa/noticeApply";
                },
                controller: 'OaNoticeApplyController as vm'
            })
            .state('oa.noticeApplyDetail', {
                url: "/noticeApplyDetail?id",
                templateUrl: function () {
                    return "/oa/noticeApplyDetail";
                },
                controller: 'OaNoticeApplyDetailController as vm'
            })
            .state('oa.noticeTrain', {
                url: "/noticeTrain",
                templateUrl: function () {
                    return "/oa/noticeTrain";
                },
                controller: 'OaNoticeTrainController as vm'
            })
            .state('oa.noticeType', {
                url: "/noticeType",
                templateUrl: function () {
                    return "/oa/noticeType";
                },
                controller: 'OaNoticeTypeController as vm'
            })
            .state('oa.noticeFile', {
                url: "/noticeFile",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFile"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeSecrecyOffice', {
                url: "/noticeSecrecyOffice",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeSecrecyOffice"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticePartyAffairs', {
                url: "/noticePartyAffairs",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticePartyAffairs"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeBusiness', {
                url: "/noticeBusiness",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeBusiness"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeQuality', {
                url: "/noticeQuality",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeQuality"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeOffice', {
                url: "/noticeOffice",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeOffice"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeFinance', {
                url: "/noticeFinance",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFinance"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeHuman', {
                url: "/noticeHuman",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeHuman"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeProject', {
                url: "/noticeProject",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeProject"},
                controller: 'OaNoticeController as vm'
            })
            .state('oa.noticeFirstCourtyard', {
                url: "/noticeFirstCourtyard",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFirstCourtyard"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeSecondCourtyard', {
                url: "/noticeSecondCourtyard",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeSecondCourtyard"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeEnergy', {
                url: "/noticeEnergy",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeEnergy"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeArchitecture', {
                url: "/noticeArchitecture",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeArchitecture"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeSteelStructure', {
                url: "/noticeSteelStructure",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeSteelStructure"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeModernProject', {
                url: "/noticeModernProject",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeModernProject"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticePathwayTraffic', {
                url: "/noticePathwayTraffic",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticePathwayTraffic"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticePetroleum', {
                url: "/noticePetroleum",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticePetroleum"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeFiveCentre', {
                url: "/noticeFiveCentre",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFiveCentre"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeFiveParticularly', {
                url: "/noticeFiveParticularly",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFiveParticularly"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeFiveLoop', {
                url: "/noticeFiveLoop",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeFiveLoop"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeAdministration', {
                url: "/noticeAdministration",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeAdministration"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeScientific', {
                url: "/noticeScientific",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeScientific"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeDiscipline', {
                url: "/noticeDiscipline",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeDiscipline"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticePhoto', {
                url: "/noticePhoto",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticePhoto"},
                controller: 'OaNoticeController as vm'
            })


            //通知公告
            .state('oa.noticeMessage', {
                url: "/noticeMessage",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeMessage"},
                controller: 'OaNoticeController as vm'
            })

            //公司制度
            .state('oa.noticeCompany', {
                url: "/noticeCompany",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeCompany"},
                controller: 'OaNoticeController as vm'
            })

            //集团制度
            .state('oa.noticeGroup', {
                url: "/noticeGroup",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeGroup"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.noticeParty', {
                url: "/noticeParty",
                templateUrl: function () {
                    return "/oa/notice";
                },
                params: {type: "noticeParty"},
                controller: 'OaNoticeController as vm'
            })

            .state('oa.articleRegime', {
                url: "/articleRegime?regime",
                templateUrl: function () {
                    return "/oa/articleRegime";
                },
                controller: 'OaArticleRegimeController as vm'
            })
            .state('oa.articleDept', {
                url: "/articleDept?deptName",
                templateUrl: function () {
                    return "/oa/articleDept";
                },
                controller: 'OaArticleDeptController as vm'
            })
            .state('oa.article', {
                url: "/article?type",
                templateUrl: function () {
                    return "/oa/article";
                },
                controller: 'OaArticleController as vm'
            })
            .state('oa.articleDetail', {
                url: "/articleDetail?id",
                templateUrl: function () {
                    return "/oa/articleDetail";
                },
                controller: 'OaArticleDetailController as vm'
            })
            .state('oa.articleDetailOWA', {
                url: "/articleDetailOWA?id",
                templateUrl: function () {
                    return "/oa/articleDetailOWA";
                },
                controller: 'OaArticleDetailController as vm'
            })
            .state('oa.softwareApply', {
                url: "/softwareApply",
                templateUrl: function () {
                    return "/oa/softwareApply";
                },
                controller: 'OaSoftwareApplyController as vm'
            })
            .state('oa.softwareApplyDetail', {
                url: "/softwareApplyDetail?id",
                templateUrl: function () {
                    return "/oa/softwareApplyDetail";
                },
                controller: 'OaSoftwareApplyDetailController as vm'
            })
            .state('oa.archive', {
                url: "/archive",
                templateUrl: function () {
                    return "/oa/archive";
                },
                controller: 'OaArchiveController as vm'
            })
            .state('oa.archiveDetail', {
                url: "/archiveDetail?id",
                templateUrl: function () {
                    return "/oa/archiveDetail";
                },
                controller: 'OaArchiveDetailController as vm'
            })
            .state('oa.knowledge', {
                url: "/knowledge",
                templateUrl: function () {
                    return "/oa/knowledge";
                },
                controller: 'OaKnowledgeController as vm'
            })
            .state('oa.knowledgeDetail', {
                url: "/knowledgeDetail?id",
                templateUrl: function () {
                    return "/oa/knowledgeDetail";
                },
                controller: 'OaKnowledgeDetailController as vm'
            })
            .state('oa.archiveApply', {
                url: "/archiveApply",
                templateUrl: function () {
                    return "/oa/archiveApply";
                },
                controller: 'OaArchiveApplyController as vm'
            })
            .state('oa.archiveApplyDetail', {
                url: "/archiveApplyDetail?id",
                templateUrl: function () {
                    return "/oa/archiveApplyDetail";
                },
                controller: 'OaArchiveApplyDetailController as vm'
            })
            .state('oa.car', {
                url: "/car",
                templateUrl: function () {
                    return "/oa/car";
                },
                controller: 'OaCarController as vm'
            })
            .state('oa.carDetail', {
                url: "/carDetail?id",
                templateUrl: function () {
                    return "/oa/carDetail";
                },
                controller: 'OaCarDetailController as vm'
            })
            .state('oa.carApply', {
                url: "/carApply",
                templateUrl: function () {
                    return "/oa/carApply";
                },
                controller: 'OaCarApplyController as vm'
            })
            .state('oa.carApplyDetail', {
                url: "/carApplyDetail?id",
                templateUrl: function () {
                    return "/oa/carApplyDetail";
                },
                controller: 'OaCarApplyDetailController as vm'
            })
            .state('oa.meetingRoom', {
                url: "/meetingRoom",
                templateUrl: function () {
                    return "/oa/meetingRoom";
                },
                controller: 'OaMeetingRoomController as vm'
            })
            .state('oa.meetingRoomDetail', {
                url: "/meetingRoomDetail?id",
                templateUrl: function () {
                    return "/oa/meetingRoomDetail";
                },
                controller: 'OaMeetingRoomDetailController as vm'
            })
            .state('oa.meetingRoomApply', {
                url: "/meetingRoomApply",
                templateUrl: function () {
                    return "/oa/meetingRoomApply";
                },
                controller: 'OaMeetingRoomApplyController as vm'
            })
            .state('oa.meetingRoomApplyDetail', {
                url: "/meetingRoomApplyDetail?id",
                templateUrl: function () {
                    return "/oa/meetingRoomApplyDetail";
                },
                controller: 'OaMeetingRoomApplyDetailController as vm'
            })
            .state('oa.trainIn', {
                url: "/trainIn",
                templateUrl: function () {
                    return "/oa/trainIn";
                },
                controller: 'OaTrainInController as vm'
            })
            .state('oa.trainInDetail', {
                url: "/trainInDetail",
                templateUrl: function () {
                    return "/oa/trainInDetail";
                },
                controller: 'OaTrainInDetailController as vm'
            })
            .state('oa.trainOut', {
                url: "/trainOut",
                templateUrl: function () {
                    return "/oa/trainOut";
                },
                controller: 'OaTrainOutController as vm'
            })
            .state('oa.trainOutDetail', {
                url: "/trainOutDetail",
                templateUrl: function () {
                    return "/oa/trainOutDetail";
                },
                controller: 'OaTrainOutDetailController as vm'
            })
            .state('oa.stampApply', {
                url: "/stampApply",
                templateUrl: function () {
                    return "/oa/stampApply";
                },
                controller: 'OaStampApplyController as vm'
            })
            .state('oa.stampApplyDetail', {
                url: "/stampApplyDetail?stampId",
                templateUrl: function () {
                    return "/oa/stampApplyDetail";
                },
                controller: 'OaStampApplyDetailController as vm'
            })
            .state('five.home', {
                url: "/home",
                templateUrl: function () {
                    return "/home";
                },
                controller: 'FiveHomeController as vm'
            })

            .state('oa.hardware', {
                url: "/hardware",
                templateUrl: function () {
                    return "/oa/hardware";
                },
                controller: 'OaHardwareController as vm'
            })
            .state('oa.hardwareDetail', {
                url: "/hardwareDetail?id",
                templateUrl: function () {
                    return "/oa/hardwareDetail";
                },
                controller: 'OaHardwareDetailController as vm'
            })
            .state('oa.software', {
                url: "/software",
                templateUrl: function () {
                    return "/oa/software";
                },
                controller: 'OaSoftwareController as vm'
            })
            .state('oa.softwareDetail', {
                url: "/softwareDetail?id",
                templateUrl: function () {
                    return "/oa/softwareDetail";
                },
                controller: 'OaSoftwareDetailController as vm'
            });


        initEdDetailRoute($stateProvider, 'v1');
    });

mccApp.factory('myHttpInterceptor', function ($rootScope, $q) {
    var httpInterceptor = {
        request: function (config) {
            Metronic.blockUI({
                boxed: true
            });
            config.requestTimestamp = new Date().getTime();
            return config;
        },
        requestError: function (rejection) {
            Metronic.unblockUI();
            toastr.error(rejection.data.msg);
            return $q.reject(rejection);
        },
        response: function (response) {
            Metronic.unblockUI();
            response.config.responseTimestamp = new Date().getTime();
            //排除掉其他情况,然后判断返回ret:false
            if (response.data.ret != undefined && !response.data.ret) {
                toastr.error(response.data.msg);
            }
            return response;
        },
        responseError: function (rejection) {

            Metronic.unblockUI();
            toastr.error(rejection.data.msg);
            return $q.reject(rejection);
        }
    };
    return httpInterceptor;
});

mccApp.run(function ($rootScope, $state, $location,actProcessQueryService, actProcessHandleService, actTaskQueryService,actTaskHandleService,commonUserService) {
    $rootScope.contentHeight = contentHeight;
    $rootScope.pageSize = 10;

    if (contentHeight > 1100) {
        $rootScope.pageSize = 20;
    } else if (contentHeight > 800) {
        $rootScope.pageSize = 15;
    }
    $rootScope.browserVersion = "other";
    pageSize = $rootScope.pageSize;
    if ($.browser.msie && $.browser.version <= 9) {
        $rootScope.browserVersion = "ie9";
    }

    $rootScope.menus = menus;
    $rootScope.user = user;
    $rootScope.$on("$stateChangeSuccess", function (event, toState, toParams, fromState, fromParams) {
        $rootScope.previousUrl = fromState.name;
        $rootScope.previoustStateParams = fromParams;
    });
    $rootScope.back = function () {
        if ($rootScope.previoustStateParams) {
            $state.go($rootScope.previousUrl, $rootScope.previoustStateParams);
        }
    };

    initActFunction($rootScope, actProcessQueryService, actProcessHandleService, actTaskQueryService, actTaskHandleService);

    initSelectUserModal($rootScope,commonUserService);
    var url=$location.search().url;
    var id=$location.search().id;
    var names=$location.search().name;
    var sss={[names]:id};

    $state.go(url, sss);

});

mccApp.directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    }
})


var menus = [];
var user = {};
var pageSize = 10;

angular.element(document).ready(function () {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "/user/listSystemMenu.json",
        success: function (d) {
            if (d.ret) {
                menus = d.data.menus;
                user = d.data.user;
                user.enLogin = "HNZ";//关联流程选择一个默认人，所有页面会自动disable
                $("#span_userName").html(user.userName);
                angular.bootstrap(document, [appName]);
            } else {
                toastr.error("System Error");
            }
        }
    });
});

function initHrRoute($stateProvider) {
    $stateProvider
        .state('hr', {
            url: "/hr",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('hr.insure', {
            url: "/insure",
            templateUrl: function () {
                return "/hr/insure";
            },
            controller: 'HrInsureController as vm'
        })
        .state('hr.insureDetail', {
            url: "/insureDetail?insureId",
            templateUrl: function () {
                return "/hr/insureDetail";
            },
            controller: 'HrInsureDetailController as vm'
        })
        .state('hr.honor', {
            url: "/honor",
            templateUrl: function () {
                return "/hr/honor";
            },
            controller: 'HrHonorController as vm'
        })
        .state('hr.honorDetail', {
            url: "/honorDetail?honorId",
            templateUrl: function () {
                return "/hr/honorDetail";
            },
            controller: 'HrHonorDetailController as vm'
        })
        .state('hr.employeeProof', {
            url: "/employeeProof",
            templateUrl: function () {
                return "/hr/employeeProof";
            },
            controller: 'HrEmployeeProofController as vm'
        })
        .state('hr.employeeProofDetail', {
            url: "/employeeProofDetail?employeeProofId",
            templateUrl: function () {
                return "/hr/employeeProofDetail";
            },
            controller: 'HrEmployeeProofDetailController as vm'
        })
        .state('hr.incomeProof', {
            url: "/incomeProof",
            templateUrl: function () {
                return "/hr/incomeProof";
            },
            controller: 'HrIncomeProofController as vm'
        })
        .state('hr.incomeProofDetail', {
            url: "/incomeProofDetail?incomeProofId",
            templateUrl: function () {
                return "/hr/incomeProofDetail";
            },
            controller: 'HrIncomeProofDetailController as vm'
        })
        .state('hr.employee', {
            url: "/employee",
            templateUrl: function () {
                return "/hr/employee";
            },
            controller: 'HrEmployeeController as vm'
        })
        .state('hr.employeeDetail', {
            url: "/employeeDetail?userLogin",
            templateUrl: function () {
                return "/hr/employee-detail";
            },
            controller: 'HrEmployeeDetailController as vm'
        })
        .state('hr.leave', {
            url: "/leave",
            templateUrl: function () {
                return "/hr/leave";
            },
            controller: 'HrLeaveController as vm'
        })
        .state('hr.leaveDetail', {
            url: "/leaveDetail?leaveId",
            templateUrl: function () {
                return "/hr/leaveDetail";
            },
            controller: 'HrLeaveDetailController as vm'
        })
        .state('hr.vacation', {
            url: "/vacation",
            templateUrl: function () {
                return "/hr/vacation";
            },
            controller: 'HrVacationController as vm'
        })
        .state('hr.vacationDetail', {
            url: "/vacationDetail?vacationId",
            templateUrl: function () {
                return "/hr/vacationDetail";
            },
            controller: 'HrVacationDetailController as vm'
        })
        .state('hr.contract', {
            url: "/contract",
            templateUrl: function () {
                return "/hr/contract";
            },
            controller: 'HrContractController as vm'
        })
        .state('hr.contractDetail', {
            url: "/contractDetail?contractId",
            templateUrl: function () {
                return "/hr/contractDetail";
            },
            controller: 'HrContractDetailController as vm'
        })
        .state('hr.qualify', {
            url: "/qualify",
            templateUrl: function () {
                return "/hr/qualify";
            },
            controller: 'HrQualifyController as vm'
        })
        .state('hr.myQualify', {
            url: "/myQualify",
            templateUrl: function () {
                return "/hr/myQualify";
            },
            controller: 'HrMyQualifyController as vm'
        })
        .state('hr.qualifyDetail', {
            url: "/qualifyDetail?qualifyId",
            templateUrl: function () {
                return "/hr/qualifyDetail";
            },
            controller: 'HrQualifyDetailController as vm'
        })
        .state('hr.certification', {
            url: "/certification",
            templateUrl: function () {
                return "/hr/certification";
            },
            controller: 'HrCertificationController as vm'
        })
        .state('hr.certificationDetail', {
            url: "/certificationDetail?certificationId",
            templateUrl: function () {
                return "/hr/certificationDetail";
            },
            controller: 'HrCertificationDetailController as vm'
        })
        .state('hr.invent', {
            url: "/invent",
            templateUrl: function () {
                return "/hr/invent";
            },
            controller: 'HrInventController as vm'
        })
        .state('hr.inventDetail', {
            url: "/inventDetail?inventId",
            templateUrl: function () {
                return "/hr/inventDetail";
            },
            controller: 'HrInventDetailController as vm'
        })
        .state('hr.basic', {
            url: "/basic",
            templateUrl: function () {
                return "/hr/basic";
            },
            controller: 'HrBasicController as vm'
        })
        .state('hr.basicDetail', {
            url: "/basicDetail?userLogin",
            templateUrl: function () {
                return "/hr/basic";
            },
            controller: 'HrBasicDetailController as vm'
        })
        .state('hr.basicAdmin', {
            url: "/basicAdmin?userLogin",
            templateUrl: function () {
                return "/hr/basicAdmin";
            },
            controller: 'HrBasicDetailController as vm'
        })
        .state('hr.education', {
            url: "/education",
            templateUrl: function () {
                return "/hr/education";
            },
            controller: 'HrEducationController as vm'
        })
        .state('hr.educationDetail', {
            url: "/educationDetail?educationId",
            templateUrl: function () {
                return "/hr/educationDetail";
            },
            controller: 'HrEducationDetailController as vm'
        })
        .state('hr.position', {
            url: "/position",
            templateUrl: function () {
                return "/hr/position";
            },
            controller: 'HrPositionController as vm'
        })
        .state('hr.dept', {
            url: "/dept",
            templateUrl: function () {
                return "/hr/dept";
            },
            controller: 'HrDeptController as vm'
        })
        .state('hr.deptUser', {
            url: "/deptUser",
            templateUrl: function () {
                return "/hr/deptUser";
            },
            controller: 'HrDeptUserController as vm'
        })
        .state('hr.registerDetail', {
            url: "/registerDetail?registerId",
            templateUrl: function () {
                return "/hr/registerDetail";
            },
            controller: 'HrRegisterDetailController as vm'
        })
        .state('hr.register', {
            url: "/register",
            templateUrl: function () {
                return "/hr/register";
            },
            controller: 'HrRegisterController as vm'
        })
        .state('hr.detail', {
            url: "/detail?owner",
            templateUrl: function () {
                return "/hr/detail";
            },
            controller: 'HrDetailController as vm'
        })

        .state('hr.train', {
            url: "/train",
            templateUrl: function () {
                return "/hr/train";
            },
            controller: 'HrTrainController as vm'
        })
        .state('hr.trainDetail', {
            url: "/trainDetail?formDataId",
            templateUrl: function () {
                return "/hr/trainDetail";
            },
            controller: 'HrTrainDetailController as vm'
        })
        .state('hr.form', {
            url: "/form?referType",
            templateUrl: function () {
                return "/hr/form";
            },
            controller: 'HrFormController as vm'
        })
        .state('hr.formDetail', {
            url: "/formDetail?formDataId",
            templateUrl: function () {
                return "/hr/formDetail";
            },
            controller: 'HrFormDetailController as vm'
        })
        .state('hr.detail.form', {
            url: "/form?referType",
            templateUrl: function () {
                return "/hr/detail/form";
            },
            controller: 'HrDetailFormController as vm'
        })
        .state('hr.detail.formDetail', {
            url: "/formDetail?formDataId",
            templateUrl: function () {
                return "/hr/detail/formDetail";
            },
            controller: 'HrDetailFormDetailController as vm'
        })
        .state('hr.detail.basicDetail', {
            url: "/basicDetail?formDataId",
            templateUrl: function () {
                return "/hr/detail/basicDetail";
            },
            controller: 'HrDetailBasicDetailController as vm'
        })

}

function initCadRoute($stateProvider) {
    $stateProvider
        .state('cad', {
            url: "/cad",
            template: "<div ui-view></div>",
            abstract: true
        })
        .state('cad.dir', {
            url: "/dir?id",
            templateUrl: function () {
                return "/cad/dir";
            },
            controller: 'CadDirController'
        })
        .state('cad.file', {
            url: "/file?id",
            templateUrl: function () {
                return "/cad/file";
            },
            controller: 'CadFileController'
        })
        .state('cad.task', {
            url: "/task?businessKey",
            templateUrl: function () {
                return "/cad/task";
            },
            controller: 'CadTaskController'
        })
        .state('cad.task.detail', {
            url: "/detail",
            templateUrl: function () {
                return "/cad/task/detail";
            },
            controller: 'CadTaskDetailController'
        })
        .state('cad.task.validateDetail', {
            url: "/validateDetail?formDataId",
            templateUrl: function () {
                return "/ed/tpl/validateDetail";
            },
            controller: 'EdDetailValidateDetailController as vm'
        })
        .state('cad.task.arrangeDetail', {
            url: "/arrangeDetail?formDataId",
            templateUrl: function () {
                return "/ed/tpl/arrangeDetail";
            },
            controller: 'EdDetailArrangeDetailController as vm'
        })
}