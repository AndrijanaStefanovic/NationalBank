'use-strict';


angular.module('company', [ 'ui.router','invoice.controller', 'invoiceItem.controller', 'company.controller'])

   
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
		
		.state('companies', {
				url : '/companies',
				templateUrl : 'pages/companies.html',
				controller : 'companyController'
		})		
		
});
