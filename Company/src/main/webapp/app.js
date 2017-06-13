'use-strict';

angular.module('company', [ 'ui.router','invoice.controller', 'invoiceItem.controller', 'bankStatement.controller','company.controller', 'businessPartner.controller'])


	.config(function($stateProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise('/invoices');
		
		$stateProvider

		.state('invoices', {
				url : '/invoices',
				templateUrl : 'pages/invoices.html',
				controller : 'invoiceController'
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

	});
