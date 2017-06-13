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
			
			$http.get('/businesspartner/getBusinessPartners').then(function(response) {
			   $scope.businessPartners = response.data;
			}, function(response) {
				alert(response.statusText);
			});
						
			$http.get('/businessPartner/getAllBusinessPartners').then(function(response) {
			   $scope.businessPartners = response.data;
			}, function(response) {
				alert(response.statusText);
			});
	});
	
	$scope.submitInvoice = function () { 
		//mi pravimo fakturu, mi smo supplieri
		$scope.invoice.supplierName = $scope.comp.name;
		$scope.invoice.supplierAddress = $scope.comp.companyAddress;
		$scope.invoice.supplierPIB = $scope.comp.companyPIB;
		$scope.invoice.billingAccountNumber = $scope.comp.companyAccount;
		$scope.invoice.buyerName = $scope.busPar.name;
		$scope.invoice.buyerAddress = $scope.busPar.partnerAddress;
		$scope.invoice.buyerPIB = $scope.busPar.partnerPIB;
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
	
	$scope.showXML = function(){
		$http.get('/invoice/getXML').then(function(response) {
			   $scope.xmlresult = response.data;
			}, function(response) {
				alert(response.statusText);
			});
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
	
	$scope.exportInvoice = function(id) {
		console.log(id);
		$http.post('invoice/export/', id).then(function mySuccess(response) {
			if(response.data == "200"){
				toastr.success("Invoice exported!");
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
		$('#payInvoiceModal').modal('toggle');
		toastr.info("Payment order is being sent...");
		$http.post('invoice/pay', $scope.paymentOrder).then(function mySuccess(response) {
			if(response.data == "OK") {
				$window.location.reload();
				toastr.success("Payed!");
			}
			toastr.info(response.data);
		}, function myError(response) {
			alert(response.statusText);
		});
	}
}]);
