/**全局作用域，用于传递给其他函数*/
var global = this;

/**添加一个log函数，可以用在javascript中进行输出调试*/
var loggerClass = Packages.com.fr.android.stable.IFLogger;
log = function (msg) {
    loggerClass.error(msg);
};

/**扩展函数对象*/
Function.prototype.createDelegate = function (obj) {
    var method = this;
    var args = arguments[1];
    var appendArgs = arguments[2];
    return function () {
        var callArgs = args || arguments;
        if (appendArgs === true) {
            callArgs = Array.prototype.slice.call(arguments, 0);
            callArgs = callArgs.concat(args);
        } else if (typeof appendArgs == "number") {
            callArgs = Array.prototype.slice.call(arguments, 0);
            // copy arguments first
            var applyArgs = [appendArgs, 0].concat(args);
            // create method call params
            Array.prototype.splice.apply(callArgs, applyArgs);
            // splice them in
        }
        return method.apply(obj, callArgs);
    };

};

/**定义全局的FR对象*/
FR = {};

// TODO:还没有实现国际化函数
FR.i18nText = function (key) {
    return 'i18n';
};

FR.tc = function (fn, context, args) {
    try {
        return fn.apply(context, args);
    } catch (e) {
        log(e);
        throw e;
    }
};

_g = function(){
    return globalObject;
};

/**引入消息相关的java类用于定义和实现本地化的消息控件*/
var messageClass = Packages.com.fr.android.script.IFMessage;
FR.Msg = {};
FR.Msg.toast = function (message) {
    messageClass.toast(javaContext, jsContext, avoidEmpty(message));
};

FR.Msg.alert = function (title, message, callback) {
    messageClass.alert(javaContext, jsContext, global, avoidEmpty(title), avoidEmpty(message), callback);
};

FR.Msg.prompt = function (title, message, value, callback) {
    messageClass.prompt(javaContext, jsContext, global, avoidEmpty(title), avoidEmpty(message), avoidEmpty(value), callback);
};

FR.Msg.confirm = function (title, message, callback) {
    messageClass.confirm(javaContext, jsContext, global, avoidEmpty(title), avoidEmpty(message), callback);
};

/**
* Mobile相关
*/

deviceClass = Packages.com.fr.android.stable.IFDeviceUtils;

FR.Mobile = {};

FR.Mobile.getDeviceInfo = function(){
    var devInfo = {};
    devInfo.Model = deviceClass.getDeviceName();
    devInfo.DeviceID = deviceClass.getDeviceID(javaContext);
    return devInfo;
}

/**
 * 客户端计算公式
 * @static
 * @param {String} formula 要计算的公式
 * @param {Object} initValue 初始值
 * @param {Boolean} must  是否必须总是计算
 * @returns {Object} 计算后的值
 */
var hyperlinkClass = Packages.com.fr.android.script.IFJSDoHyperlink;

FR.formulaEvaluator = function(formula, initValue, must) {
    return function() {
        if(initValue == undefined || must) {
            return hyperlinkClass.remoteEvaluate(javaContext, jsContext, global, formula, jssessionid);
        }

        return initValue;
    }
};

FR.remoteEvaluate = function(formula){
   return hyperlinkClass.remoteEvaluate(javaContext, jsContext, global, formula, jssessionid);
}

/**引入超链java类用实现响应 超链*/


FR.doHyperlinkByPost = function (url, config, target, feature) {
    if(url && url.url) {
        hyperlinkClass.doHyperlinkByPost(javaContext, jsContext, global, jsonToString(url, true), config, jssessionid);
    } else {
        hyperlinkClass.doHyperlinkByPost(javaContext, jsContext, global, url, config, jssessionid);
    }
};

FR.doHyperlinkByGet = function (url, config, target, feature) {
    if(url && url.url) {// json格式 or 字符串
        hyperlinkClass.doHyperlinkByGet(javaContext, jsContext, global, jsonToString(url, true), config, jssessionid);
    } else {
        hyperlinkClass.doHyperlinkByGet(javaContext, jsContext, global, url, config, jssessionid);
    }
};


var ajaxClass = Packages.com.fr.android.script.IFJSAjax;

FR.ajax = function(jsonString){
    ajaxClass.doAjax(javaContext, jsContext, global, jsonString);
};


var locationHelper = Packages.com.fr.android.script.IFJSLocation;

/**
 * 地理位置
 * @param fn
 */
FR.location = function (fn) {
    //初始化GPS
    locationHelper.location(javaContext, jsContext, global,fn);
};

var codeHelper = Packages.com.fr.android.utils.IFCodeUtils;

FR.cjkEncode = function(str){
    return String(codeHelper.cjkEncode(str));  //返回值转化为js的String
};

FR.cjkDecode = function(str){
    return String(codeHelper.cjkDecode(str));
};

var timerExecutorOfAndroid = Packages.com.fr.android.script.IFJSTimer;

setTimeout = function(fn,time){
    return timerExecutorOfAndroid.setTimeout(javaContext, jsContext, global, fn, time, jssessionid);
};

setInterval = function(fn, time) {
    if(typeof fn === 'string'){
        return timerExecutorOfAndroid.setInterval(javaContext, jsContext, global, fn, time, jssessionid, true);
    } else {
        return timerExecutorOfAndroid.setInterval(javaContext, jsContext, global, fn, time, jssessionid, false);
    }
};

clearTimeout = function(id) {
    timerExecutorOfAndroid.clearInterval(id, jssessionid);
};

clearInterval = function(id) {
    timerExecutorOfAndroid.clearInterval(id, jssessionid);
};

var webUtils = Packages.com.fr.android.script.IFJSWebUtils;
FR.Chart = {};
FR.Chart.WebUtils = FR.Chart.WebUtils || {};
FR.Chart.WebUtils.getChart = function(str){
    return webUtils.getChart(str);
};

FR.logoutApp = function() {
    webUtils.logoutApp(javaContext, global);
};

FR.sentMail = function(mail) {
    webUtils.sentMail(javaContext, jsonToString(mail));
};

FS = {};
FS.tabPane = {};
FS.tabPane.addItem = function(json) {
    webUtils.addItem(javaContext, json);
};

FS.tabPane.closeActiveTab = function() {
    webUtils.closeActiveTab(javaContext);
};

// window.close
close = function() {
    webUtils.close(javaContext);
};

function jsonToString (obj, isfirst){
    var THIS = this;
    var hasMaps = [];
    switch(typeof(obj)){
        case 'string':
            return obj;
        case 'array':
            return '[' + obj.map(THIS.jsonToString).join(',') + ']';
        case 'object':
            if(obj instanceof Array){
                var strArr = [];
                var len = obj.length;
                for(var i=0; i<len; i++){
                    strArr.push(THIS.jsonToString(obj[i]));
                }
                return '[' + strArr.join(',') + ']';
            }else if(obj==null){
                return 'null';

            }else{
                var string = [];
                for (var property in obj) {
                    var name = THIS.jsonToString(property);
                    if(hasMaps.indexOf(name) != -1) {// 过滤掉重复的.
                        continue;
                    }

                    hasMaps.push(name);
                    var value = obj[property];
                    if(value != undefined && value != null) {
                        if(isfirst) {// 只加载第一层的. 因为默认的js获取的nativieObject 可能会循环.

                            if(typeof(value) == "string") {
                                string.push("\"" +  String(name) +　"\"" + ':' + "\"" +  value + "\"");
                            } else {
                                var toValue = THIS.jsonToString(value);
                                string.push("\"" +  String(name) +　"\"" + ':' + toValue);
                            }
                        } else {
                            string.push("\"" + String(name) +　"\"" + ':' + "\"" + value + "\"");// 不再深入解析 导致循环.
                        }
                    } else {
                        string.push("\"" + String(name) +　"\"" + ':' + " ");
                    }
                }
                return '{' + string.join(',') + '}';
            }
        case 'number':
            return obj;
        case false:
            return obj;
    }
}

function avoidEmpty(arg) {
    return (typeof arg !== 'undefined' && arg !== null) ? arg : '';
}

