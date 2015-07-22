(function() {
    'use strict';
    angular.module('app', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'easypiechart', 'textAngular', 'ngTagsInput', 'ngWebsocket', 'app.ui.ctrls', 'app.ui.directives', 'app.ui.services', 'app.controllers', 'app.directives', 'app.form.validation', 'app.ui.form.ctrls', 'app.ui.form.directives', 'app.tables', 'app.chart.ctrls', 'app.chart.directives', 'app.page.ctrls']).config([
        '$routeProvider',
        function($routeProvider) {
            return $routeProvider.when('/', {
                redirectTo: '/dashboard'
            }).when('/dashboard', {
                templateUrl: 'views/dashboard.html'
            }).when('/ui/typography', {
                templateUrl: 'views/ui/typography.html'
            }).when('/ui/buttons', {
                templateUrl: 'views/ui/buttons.html'
            }).when('/ui/icons', {
                templateUrl: 'views/ui/icons.html'
            }).when('/ui/grids', {
                templateUrl: 'views/ui/grids.html'
            }).when('/ui/widgets', {
                templateUrl: 'views/ui/widgets.html'
            }).when('/ui/components', {
                templateUrl: 'views/ui/components.html'
            }).when('/ui/timeline', {
                templateUrl: 'views/ui/timeline.html'
            }).when('/ui/nested-lists', {
                templateUrl: 'views/ui/nested-lists.html'
            }).when('/ui/pricing-tables', {
                templateUrl: 'views/ui/pricing-tables.html'
            }).when('/forms/elements', {
                templateUrl: 'views/forms/elements.html'
            }).when('/forms/layouts', {
                templateUrl: 'views/forms/layouts.html'
            }).when('/forms/validation', {
                templateUrl: 'views/forms/validation.html'
            }).when('/tables/static', {
                templateUrl: 'views/tables/static.html'
            }).when('/tables/responsive', {
                templateUrl: 'views/tables/responsive.html'
            }).when('/tables/dynamic', {
                templateUrl: 'views/tables/dynamic.html'
            }).when('/charts/others', {
                templateUrl: 'views/charts/charts.html'
            }).when('/charts/morris', {
                templateUrl: 'views/charts/morris.html'
            }).when('/charts/flot', {
                templateUrl: 'views/charts/flot.html'
            }).when('/pages/features', {
                templateUrl: 'views/pages/features.html'
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
