var invoiceItemModule = angular.module('invoiceItem.controller', []);
 

invoiceItemModule.controller('invoiceItemController', ['$scope', '$window', '$location', '$http', '$stateParams',
  	function ($scope, $window, $location, $http, $stateParams) {

	angular.element(document).ready(function () {
			
			$http.get('/invoiceItem/getInvoiceItems/'+$stateParams.invoiceId).then(function(response) {
				   $scope.invoiceItems = response.data;
			}, function(response) {
					alert(response.statusText);
			});
	});
	
	$scope.submitInvoiceItem = function () { 
		if (document.getElementById('radioMerchandise').checked) {
			 $scope.invoiceItem.kind = "merchandise";
		} else {
			 $scope.invoiceItem.kind = "service";
		}
		$http.post('invoiceItem/create/'+$stateParams.invoiceId, $scope.invoiceItem).then(function mySuccess(response) {
	    		if(response.data == "200"){
		    		toastr.success("Created!");
		    	}
	    		$window.location.reload();
	    }, function myError(response) {
	    	alert(response.statusText);
	    });
	}
	
	$scope.deleteInvoiceItem = function(id) {
		$http.post('invoiceItem/delete', id).then(function mySuccess(response) {
    		if(response.data == "200"){
	    		toastr.success("Deleted!");
	    	}
    		$window.location.reload();
    }, function myError(response) {
    	alert(response.statusText);
    });
	}
}]);
