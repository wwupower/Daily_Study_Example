---
title: angular-1.5-Service、Factory、Provider的理解
date: 2018-08-23 21:02:50
tags: angular
categories: 前端
---


angular 服务是在实际项目开发中负责复杂业务逻辑的操作的，所以我们经常要写一些自定义service;具体可以用service、Factory、Provider 来实现,实现的方式也有所不同;
<!--more-->

## 1.service 方式实现

先看用service实现自定义服务
service 参数 第一个参数是服务名称 第二个参数是构造方法，在构造方法里我们可以注入一些服务也就是一些依赖。

```javascript

var app = angular.module("app",[]);	
    app.service("payService",function(){
    	this.doPay = function(price){
    		console.log("支付金额："+price);
    	}
    });
    
    app.service("orderService",function($timeout,payService){
    	this.doPay = function($scope){
    		payService.doPay($scope.goodPrice);
    	}
    });
    app.controller("mainCtrl",function($scope,orderService){
    	$scope.goodPrice = 100;
    	orderService.doPay($scope);
    	
    });

```


## 2.factory 方式实现

factory 参数 第一个参数是服务名称 第二个参数是普通的方法，我们也可以在普通方法上注入一些服务，但是，方法的返回值必须是一个对象实例;

```javascript
        app.service("orderService",function($timeout,payService){
        	this.doPay = function($scope){
        		payService.doPay($scope.goodPrice);
        	}
        });
        
		//factory 的方式
		app.factory("shopCartService",function(payService){
			
			//购物车折扣 私有的
			function discounted(price){
				return price*0.8;
			}
			//暴露给外部使用着
			return {
				doPay:function(price){
					payService.doPay(discounted(price));
				}
			}
		});
		
        app.controller("mainCtrl",function($scope,shopCartService){
			$scope.goodPrice = 100;
			shopCartService.doPay($scope.goodPrice);
		});

```
对比发现不同地方：我们返回具体的对象实例是暴露给调用者调用的，这里我们就可以定义我们自己的私有方法，仅仅内部使用。

很好奇的是为什么这样呢？准备就扒下源码看看：

```javascript
  function factory(name, factoryFn, enforce) {
  
    return provider(name, {
      $get: enforce !== false ? enforceReturnValue(name, factoryFn) : factoryFn
    });
  }

  function service(name, constructor) {
    return factory(name, ['$injector', function($injector) {
      return $injector.instantiate(constructor);
    }]);
  }

```
诶，service原来是返回的是factory方法。而factory又返回的是provider();

找到根源我们继续看看下provider到底是什么东西？我们先用Provider方式去实现。

##  3.Provider 方式实现

其实服务我们还可以通过Provider来创建我们的自定义服务;先看代码

```javascript
        
        //需要注意的是：在注入provider时，名字应该是：providerName+Provider   
	    app.config(function(pdCartProvider){
	    	//设置最小打折金额  可以在config初始化成员属性
	        pdCartProvider.setMinDisPrice(120);       
	    });
	    
	    
		app.provider("pdCart",function(){
			var minDisPrice = 0;
			var me = this;
			this.setMinDisPrice = function(newMinDisPrice){
				minDisPrice = newMinDisPrice
			}
			
			this.discounted = function(price){
				if(price>minDisPrice){
					return price*0.8;
				}else{
					return price;
				}
			}
			//必须通过$get方法返回具体实例
			this.$get = function(payService){
				return {
					doPay:function(price){
						payService.doPay(me.discounted(price));
					}
				}
			}
			
		});
		
		app.controller("mainCtrl",function($scope,orderService,shopCart,pdCart){
			$scope.goodPrice = 100;
			orderService.doPay($scope.goodPrice);
			shopCart.doPay($scope.goodPrice);
			pdCart.doPay($scope.goodPrice);
		});

```
咱们再去看provider源码；

```javascript
function provider(name, provider_) {
    //确保name不等于hasOwnProperty
    assertNotHasOwnProperty(name, 'service');
    //如果提供的是provider_是函数或者数组，直接使用injector实例化
    if (isFunction(provider_) || isArray(provider_)) {
      provider_ = providerInjector.instantiate(provider_);
    }
    // 没$get方法则抛出异常
    if (!provider_.$get) {
      throw $injectorMinErr('pget', "Provider '{0}' must define $get factory method.", name);
    }
    // // 将provider_放入到providerCache中
    return providerCache[name + providerSuffix] = provider_;
  }
  
```
可以发现，最终provider会调用providerInjector.instantiate方法进行实例化。具体想了解怎么实例化和注入的过程请看这篇博文 http://blog.csdn.net/dm_vincent/article/details/52140633

## 4.最后总结

1) 用 Factory 就是创建一个对象，为它添加属性，然后把这个对象返回出来。可以实现成员、方法私有化;

2) Service 给"this"添加属性，然后 service 返回"this"。

3) Providers 是唯一一种你可以传进 .config() 函数的 service。当你想要在 service 对象启用之前，先进行模块范围的配置，那就应该用 provider。

4）Factory/service是第一个注入时才实例化，而provider不是，它是在config之前就已实例 化好

参考资料：http://blog.csdn.net/zcl_love_wx/article/details/51404390

参考资料：http://blog.csdn.net/dm_vincent/article/details/52137733

参考资料：《angularjs 入门与进阶》

