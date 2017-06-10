'use-strict';


angular.module('company', [ 'ui.router','invoice.controller', 'bankStatement.controller'])

   
	.config(function($stateProvider, $urlRouterProvider) {


		$stateProvider
			
		.state('invoices', {
				url : '/invoices',
				templateUrl : 'pages/invoices.html',
				controller : 'invoiceController'
		})
		
		.state('bankStatements', {
				url : '/bankStatements',
				templateUrl : 'pages/bankStatements.html',
				controller : 'bankStatementController'
		})
		
		

});
