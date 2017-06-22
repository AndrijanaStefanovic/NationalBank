'use-strict';

<<<<<<< HEAD
angular.module('company', [ 'ui.router','invoice.controller', 'getXML.controller', 'invoiceItem.controller', 'bankStatement.controller','company.controller', 'businessPartner.controller',
							'bankStatementItem.controller'])
=======
angular.module('company', [ 'ui.router','invoice.controller', 'getXML.controller', 'invoiceItem.controller', 'bankStatement.controller','company.controller', 'businessPartner.controller', 'paymentOrder.controller'])
>>>>>>> 612bc18641528c7371437e2f064b6aa20016b867


	.config(function($stateProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise('/invoices');
		
		$stateProvider

		.state('invoices', {
				url : '/invoices',
				templateUrl : 'pages/invoices.html',
				controller : 'invoiceController'
		})
		
		.state('invoices/getXML', {
				url : '/invoices/getXML',
				templateUrl : 'pages/getXML.html',
				controller : 'getXMLController'
		})
		
		.state('invoiceItems', {
				url : '/invoiceItems/:invoiceId',
				templateUrl : 'pages/invoiceItems.html',
				controller : 'invoiceItemController'
		})
		
		.state('bankStatements', {
				url : '/bankStatements',
				templateUrl : 'pages/bankStatements.html',
				controller : 'bankStatementController'
		})

		.state('companies', {
				url : '/companies',
				templateUrl : 'pages/companies.html',
				controller : 'companyController'
		})

	    .state('businessPartners', {
		        url : '/businessPartners/:companyId',
		        templateUrl : 'pages/businessPartners.html',
		        controller : 'businessPartnerController'
	    })
	    
	    .state('bankStatementItems', {
				url : '/bankStatementItems/:id',
				templateUrl : 'pages/bankStatementItems.html',
				controller : 'bankStatementItemController'
		})

		.state('paymentOrders', {
			url : '/paymentOrders',
			templateUrl : 'pages/paymentOrders.html',
			controller : 'paymentOrderController'
		})

	});
