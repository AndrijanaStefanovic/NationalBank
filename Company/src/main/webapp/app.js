'use-strict';


angular.module('company', [ 'ui.router','invoice.controller','invoiceItem.controller', 'bankStatement.controller'])

   
	.config(function($stateProvider, $urlRouterProvider) {


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
		
		

});
