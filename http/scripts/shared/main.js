(function() {
    'use strict';
    angular.module('app.controllers', []).controller('AppCtrl', [
        '$scope', '$location',
        function($scope, $location) {
            $scope.isSpecificPage = function() {
                var path;
                path = $location.path();
                return _.contains(['/404', '/pages/500', '/pages/login', '/pages/signin', '/pages/signin1', '/pages/signin2', '/pages/signup', '/pages/signup1', '/pages/signup2', '/pages/forgot', '/pages/lock-screen'], path);
            };
            return $scope.main = {
                brand: 'Cronos',
                name: 'Lisa Doe'
            };
        }
    ]).controller('DashboardCtrl', ['$scope', '$websocket', '$timeout', function($scope, $websocket) {
        var ws = $websocket.$new("ws://localhost:9003"); //define new ws-address and connect

        $scope.hallo = "60";
        $scope.counter = 1;
        $scope.erpData = []; //the array, where our erp data is pushed to
        $scope.machineData = []; //the array, where our machine data ist pushed to
        $scope.products = {};
        $scope.drillingSpeed = 0;
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            console.log("Connection established");
        });
        ws.$on('$message', function(data) {
            $scope.$apply(function() { //we need to manually apply a scope change, so dynamic array changes will be reflected in view
                if (data.type == "erpData") {
                    $scope.products[data.orderNumber] = {};
                    $scope.products[data.orderNumber].erpData = data.simData;
                    $scope.products[data.orderNumber].state = data.state;
                    $scope.products[data.orderNumber].progress = 0;
                };
                if (data.type == "machineData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress += $scope.calculateProgress(data.state);
<<<<<<< Updated upstream
                        if ($scope.products[data.orderNumber].state == "DRILLING_STATION") {
                            $scope.drillingSpeed = $scope.products[data.orderNumber].simData.value;
                            console.log($scope.products[data.orderNumber].state);
                        }
                        if ($scope.products[data.orderNumber].state == "MILLING_STATION") {
                            //$scope.millingSpeed = $scope.products[data.orderNumber].simData.value;
                            //console.log($scope.products[data.orderNumber].state);
=======
                        if ($scope.products[data.orderNumber].state == "DRILLING_STATION" && data.simData.itemName == "Drilling Speed") {
                            $scope.drillingSpeed = data.simData.value;
                            $scope.gaugeChart1.set($scope.drillingSpeed);
                            console.log($scope.drillingSpeed);
                        }
                        if ($scope.products[data.orderNumber].state == "MILLING_STATION" && data.simData.itemName == "Milling Speed") {
                            $scope.millingSpeed = data.simData.value;
                            $scope.gaugeChart1.set($scope.millingSpeed);

                            console.log($scope.millingSpeed);
>>>>>>> Stashed changes
                        }
                    }
                }
                if (data.type == "saData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress += $scope.calculateProgress(data.state);
                        if ($scope.products[data.orderNumber].state == "FINISH") {
                            $scope.products[data.orderNumber].analysisStatus = data.simData.overallStatus;
                            var scoperef = $scope;
                            setTimeout(function() {
                                delete scoperef.products[data.orderNumber];
                            }, 3000);
                        };
                    }
                }

            });
            $scope.calculateProgress = function(state) {
                switch (state) {
                    case "INIT":
                        return 2;
                    case "LIGHTBARRIER_1":
                        return 2;
                    case "BETWEEN_L1_L2":
                        return 2;
                    case "LIGHTBARRIER_2":
                        return 2;
                    case "BETWEEN_L2_L3":
                        return 2;
                    case "MILLING_STATION":
                        return 4;
                    case "BETWEEN_L3_L4":
                        return 2;
                    case "DRILLING_STATION":
                        return 3;
                    case "BETWEEN_L4_L5":
                        return 2;
                    case "LIGHTBARRIER_5":
                        return 2;
                    case "END_OF_PRODUCTION":
                        return 9;
                    case "FINISH":
                        return 1;
                }
            }
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

        $scope.gaugeChart1 = {
            data: {
<<<<<<< Updated upstream
                maxValue: 25000, //milling 150000 drilling 25000
                animationSpeed: 40,
                val: 2500
=======
                maxValue: 20000, //milling 17000 drilling 20000
                animationSpeed: 40,
                val: 0
>>>>>>> Stashed changes
            },
            options: {
                lines: 12,
                angle: 0,
                lineWidth: 0.47,
                pointer: {
                    length: 0.6,
                    strokeWidth: 0.03,
                    color: '#000000'
                },
                limitMax: 'false',
                colorStart: '#A3C86D',
                colorStop: '#A3C86D',
                strokeColor: '#E0E0E0',
                generateGradient: true,
                percentColors: [
                    [0.0, '#60CD9B'],
                    [1.0, '#60CD9B']
                ]
            }
        };

    }]).controller('HistoryCtrl', ['$scope', '$http', function($scope, $http) {
        var compareData;
        console.log("Sind drin");
        $http.get('/data/getDataByAnalysisResult')
        .success(function(data, status, headers, config){
                console.log("YIPPIE");
                console.log(data);
                $scope.compareData = data.data;
                var colorFunc = function(row, series, type){
                    if(row.label == "OK") return "#61A656";
                    else return "#d32030";
                };

                for(var i = 0; i < $scope.compareData.length; i++){
                    $scope.compareData[i].AvgDrillingHeat = Math.round($scope.compareData[i].AvgDrillingHeat * 10) / 10;
                    $scope.compareData[i].AvgMillingHeat = Math.round($scope.compareData[i].AvgMillingHeat * 10) / 10;
                    $scope.compareData[i].AvgDrillingSpeed = Math.round($scope.compareData[i].AvgDrillingSpeed);
                    $scope.compareData[i].AvgMillingSpeed = Math.round($scope.compareData[i].AvgMillingSpeed);
                    $scope.compareData[i].AvgDrillingTime = Math.round($scope.compareData[i].AvgDrillingTime / 1000 * 100) / 100;
                    $scope.compareData[i].AvgMillingTime = Math.round($scope.compareData[i].AvgMillingTime / 1000 * 100) / 100;
                };

                Morris.Bar({
                    element: 'compareDrillingHeat',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingHeat'],
                    labels: ['Drilling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareDrillingSpeed',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingSpeed'],
                    labels: ['Drilling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareDrillingTime',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingTime'],
                    labels: ['Drilling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'compareMillingHeat',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingHeat'],
                    labels: ['Milling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareMillingSpeed',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingSpeed'],
                    labels: ['Milling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareMillingTime',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingTime'],
                    labels: ['Milling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });

        })
        .error(function(data,status,headers,config){
            console.log("Scheiße gelaufen");
        });

    }]).controller('MaterialCtrl', ['$scope', '$http', function($scope, $http) {
        var compareMaterial;
        $http.get('/data/getDataByMat')
        .success(function(data, status, headers, config){
                console.log("YIPPIE-Material");
                console.log(data);
                $scope.compareMaterial = data.data;
                var colorFunc = function(row, series, type){
                    if(row.label < "7500") return "#5B90BF";
                    else return "#d08770";
                };

                for(var i = 0; i < $scope.compareMaterial.length; i++){
                    $scope.compareMaterial[i].AvgDrillingHeat = Math.round($scope.compareMaterial[i].AvgDrillingHeat * 10) / 10;
                    $scope.compareMaterial[i].AvgMillingHeat = Math.round($scope.compareMaterial[i].AvgMillingHeat * 10) / 10;
                    $scope.compareMaterial[i].AvgDrillingSpeed = Math.round($scope.compareMaterial[i].AvgDrillingSpeed);
                    $scope.compareMaterial[i].AvgMillingSpeed = Math.round($scope.compareMaterial[i].AvgMillingSpeed);
                    $scope.compareMaterial[i].AvgDrillingTime = Math.round($scope.compareMaterial[i].AvgDrillingTime / 1000 * 100) / 100;
                    $scope.compareMaterial[i].AvgMillingTime = Math.round($scope.compareMaterial[i].AvgMillingTime / 1000 * 100) / 100;
                    $scope.compareMaterial[i].OKPercentage = Math.round((1 - $scope.compareMaterial[i].OKPercentage) * 10000) / 100;
                };

                Morris.Bar({
                    element: 'compareDrillingHeat',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingHeat'],
                    labels: ['Drilling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareDrillingSpeed',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingSpeed'],
                    labels: ['Drilling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareDrillingTime',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingTime'],
                    labels: ['Drilling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'compareMillingHeat',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingHeat'],
                    labels: ['Milling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareMillingSpeed',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingSpeed'],
                    labels: ['Milling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareMillingTime',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingTime'],
                    labels: ['Milling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'analysisResultPercentage',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['OKPercentage'],
                    labels: ['Defective Goods'],
                    barColors: colorFunc,
                    postUnits: '%'
                });

        })
        .error(function(data,status,headers,config){
            console.log("Scheiße gelaufen");
        });

    }]);


}).call(this);
