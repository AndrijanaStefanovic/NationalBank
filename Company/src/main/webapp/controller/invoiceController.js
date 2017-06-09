var invoiceModule = angular.module('invoice.controller', []);
 

invoiceModule.controller('invoiceController', ['$scope','$window', '$location', '$http', '$stateParams',
  	function ($scope, $window, $location, $http, $stateParams) {

	angular.element(document).ready(function () {
			$http.get('/invoice/getSentInvoices').then(function(response) {
			   $scope.sentInvoices = response.data;
			}, function(response) {
				alert(response.statusText);
		    });
			
			$http.get('/invoice/getReceivedInvoices').then(function(response) {
				   $scope.receivedInvoices = response.data;
				}, function(response) {
					alert(response.statusText);
			    });
			
			$http.get('/company/getAllCompanies').then(function(response) {
			   $scope.companies = response.data;
			}, function(response) {
				alert(response.statusText);
			});
			
			$http.get('/businesspartner/getAllBusinessPartners').then(function(response) {
			   $scope.businessPartners = response.data;
			}, function(response) {
				alert(response.statusText);
			});
	});
	
	$scope.submitInvoice = function () { 
		//toastr.info($scope.busPar.name);
		//toastr.info($scope.cmpId);
		$http.post('invoice/create', $scope.invoice).then(function mySuccess(response) {
	    		if(response.data == "200"){
		    		toastr.success("Created!");
		    	}
	    		$window.location.reload();
	    }, function myError(response) {
	    	alert(response.statusText);
	    });
	}
	
	$scope.showInvoiceItems = function(id){
		$location.path("/invoiceItems/"+id);
	}
	
	$scope.deleteInvoice = function(id){
		$http.post('invoice/delete', id).then(function mySuccess(response) {
			if(response.data == "HasItems"){
	    		toastr.error("Unable to delete an invoice that has items.")
	    	} else {
	    		$window.location.reload();
	    	}
		}, function myError(response) {
			alert(response.statusText);
		});
	}
	
	$scope.payInvoice = function(id){
		$scope.payInvoiceId = id;
		$("#payInvoiceButton").click();
	}
	
	$scope.submitPaymentOrder = function(){
		$scope.paymentOrder.invoiceId = $scope.payInvoiceId;
		if(document.getElementById('paymentOrderUrgent').checked){
			$scope.paymentOrder.urgent = "true";
		} else {
			$scope.paymentOrder.urgent = "false";
		}  
		$http.post('invoice/pay', $scope.paymentOrder).then(function mySuccess(response) {
			
		}, function myError(response) {
			alert(response.statusText);
		});
	}
}]);
