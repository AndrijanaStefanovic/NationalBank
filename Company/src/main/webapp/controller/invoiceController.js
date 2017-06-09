var invoiceModule = angular.module('invoice.controller', []);
 

invoiceModule.controller('invoiceController', ['$scope','$location', '$http', '$stateParams',
  	function ($scope, $location, $http, $stateParams) {

	angular.element(document).ready(function () {
			$http.get('/invoice/getAllInvoices').then(function(response) {
			   $scope.invoices = response.data;
			}, function(response) {
				alert(response.statusText);
		    });
	});
	
	$scope.submitInvoice = function () { 
		
		$http.post('invoice/create', $scope.invoice)
	    	.then(function mySuccess(response) {
	    		if(response.data == "200"){
		    		toastr.success("Created!");
		    	}
	    }, function myError(response) {
	    	alert(response.statusText);
	    });
	}
	
	$scope.showInvoiceItems = function(id){
		toastr.info(id);
	}
}]);
