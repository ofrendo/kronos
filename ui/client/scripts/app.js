(function() {
    'use strict';
    angular.module('app', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'easypiechart', 'textAngular', 'ngTagsInput', 'ngWebsocket', 'app.ui.services', 'app.controllers', 'app.directives', 'app.chart.directives']).config([
        '$routeProvider',
        function($routeProvider) {
            return $routeProvider.when('/', {
                redirectTo: '/dashboard'
            }).when('/dashboard', {
                templateUrl: 'views/dashboard.html'
            }).when('/history', {
                templateUrl: 'views/history.html'
            }).when('/material', {
                templateUrl: 'views/material.html'
            }).when('/404', {
                templateUrl: 'views/pages/404.html'
            }).when('/pages/500', {
                templateUrl: 'views/pages/500.html'
            }).when('/pages/blank', {
                templateUrl: 'views/pages/blank.html'
            }).otherwise({
                redirectTo: '/404'
            });
        }
    ])

}).call(this);
