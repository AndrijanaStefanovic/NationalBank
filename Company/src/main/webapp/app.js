'use-strict';


angular.module('company', [ 'ui.router','invoice.controller'])

   
	.config(function($stateProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise('/invoices');

		$stateProvider
			
		.state('invoices', {
				url : '/invoices',
				templateUrl : 'pages/invoices.html',
				controller : 'invoiceController'
		})
		
		

});
