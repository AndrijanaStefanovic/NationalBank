var invoiceXMLModule = angular.module('getXML.controller', []);

invoiceXMLModule.controller('getXMLController', ['$scope','$window', '$location', '$http', '$stateParams',
  	function ($scope, $window, $location, $http, $stateParams) {
	
	angular.element(document).ready(function () {
			$http.get('/invoice/getXML').then(function(response) {
				   $scope.messageId = response.data[9];
				   $scope.supplierName = response.data[13];
				   $scope.supplierAddress = response.data[12];
				   $scope.supplierPIB = response.data[14];
				   $scope.buyerName = response.data[3];
				   $scope.buyerAddress = response.data[2];
				   $scope.buyerPIB = response.data[4];
				   $scope.accountNumber = response.data[0];
				   $scope.dateOfInvoice = response.data[6];
				   $scope.merchandiseValue = response.data[8];
				   $scope.servicesValue = response.data[11];
				   $scope.totalValue = response.data[18];
				   $scope.totalDiscount = response.data[15];
				   $scope.totalTax = response.data[17];
				   $scope.currency = response.data[5];
				   $scope.totalDue = response.data[16];
				   $scope.billingAccountNumber = response.data[1];
				   $scope.dateOfValue = response.data[7];
				   
				   $scope.number = response.data[24];
				   $scope.name = response.data[23];
				   $scope.amount = response.data[19];
				   $scope.measurmentUnit = response.data[22];
				   $scope.unitPrice = response.data[28];
				   $scope.value = response.data[29];
				   $scope.discountPercent = response.data[20];
				   $scope.totalDiscount =response.data[26];
				   $scope.subtractedDiscount = response.data[25];
				   $scope.totalTax = response.data[27];
				}, function(response) {
					alert(response.statusText);
				});
	});
	
}]);