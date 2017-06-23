var paymentOrderModule = angular.module('paymentOrder.controller', []);

paymentOrderModule.controller('paymentOrderController', ['$scope','$location', '$http', '$stateParams',
    function ($scope, $location, $http, $stateParams) {

        angular.element(document).ready(function () {

            $http.get('/paymentOrder/getAllPaymentOrders').then(function(response) {
                $scope.paymentOrders = response.data;
                
                for(i=0; i<$scope.paymentOrders.length; i++){
 	   			   var date = new Date($scope.paymentOrders[i].dateOfPayment);
 	   			   var month = date.getMonth() + 1;
 	   			   var day = date.getDate();
 	   			   var year = date.getFullYear();
 	   			   var shortStartDate = day + "/" + month + "/" + year;
 	   			   $scope.paymentOrders[i].dateOfPayment = shortStartDate;
 	   			   
 	   			   var date2 = new Date($scope.paymentOrders[i].dateOfValue);
 	   			   var month2 = date2.getMonth() + 1;
 	   			   var day2 = date2.getDate();
 	   			   var year2 = date2.getFullYear();
 	   			   var shortStartDate2 = day2 + "/" + month2 + "/" + year2;
 	   			   $scope.paymentOrders[i].dateOfValue = shortStartDate2;
 			   }
                
            }, function(response) {
                alert(response.statusText);
            });

        });

    }]);