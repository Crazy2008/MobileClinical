(function ($) {
    $.fn.helpinput = function () {
        return this;
    };
})(jQuery);

(function ($) {
    $.fn.toPicker = function () {
        var datetimepicker_click = function () {
            var $editor = $(this);
            var g = $editor.data("group") || ("control-" + $editor.data("form-index"));
            var isTimeSelector = $(window.__date_control[g][0]).data("type");
            var isRangeSelector = window.__date_control[g].length == 1 ? "0" : "1";
            var title = $(window.__date_control[g][0]).data("title") || "";
            var min = $(window.__date_control[g][0]).data("min") || "", max = $(window.__date_control[g][0]).data("max");
            var begin = $(window.__date_control[g][0]).val(), end = window.__date_control[g].length == 1 ? "" : $(window.__date_control[g][1]).val();

            var iscode = /^\[.*\]$/
            if (iscode.test(min)) min = eval(min.substr(1, min.length - 2));
            if (iscode.test(max)) max = eval(max.substr(1, max.length - 2));
            if (iscode.test(begin)) begin = eval(begin.substr(1, begin.length - 2));
            else begin = Date.fromRq16(begin.replace(/[\- ]/g, "")).format("yyyyMMddHH:mm");
            if (iscode.test(end)) end = eval(end.substr(1, end.length - 2));
            else end = Date.fromRq16(end.replace(/[\- ]/g, "")).format("yyyyMMddHH:mm");

            $App.datetimepicker((isTimeSelector === undefined ? 1 : isTimeSelector) + "", isRangeSelector, title, min, max, begin, end, window.__date_control[g][0].id, window.__date_control[g][1] && window.__date_control[g][1].id);
        };

        return this.each(function (idx) {
            var $this = $(this);
            if (!$App.datetimepicker) return $this.attr("type", "date");
            var group = $this.data("form-index", idx).data("group") || ("control-" + idx);
            if (!window.__date_control) window.__date_control = {};
            if (!window.__date_control[group]) window.__date_control[group] = [];
            window.__date_control[group].push(this);
            $(this).on("click", $.proxy(datetimepicker_click, this)).attr("readonly", "true");
        });
    };
})(jQuery);

window.$App = (function (app) {
    var external = {
        //弹出警告提示框
        //@text <string> 要提示内容
        alert: function (text) {
            if (app && app.alert) app.alert(text);
            else window.alert(text);
        },
        confirm: function (title, message) {
            if (!app || !app.confirm) return window.confirm(message);
            return app.confirm(title, message);
        },
        //获取当前登录医生信息
        //@return <string> doctorinfo的json格式字符串。
        getDoctor: function () {
            return (app && app.getDoctor && $decode(app.getDoctor())) || {};
        },
        //获取当前科室
        //@return <string> DeptWardMapInfo的json格式字符串。
        getDept: function () {
            return (app && app.getDept && $decode(app.getDept())) || {};
        },

        //获取暂存的数据
        //@key <string>  数据关键字
        //@return <string> 字符串数据.
        getData: function (key) {
            if (app && app.getData) return $decode(app.getData(key)) || {};
            var arr = document.cookie.split(";");
            for (var i = 0; i < arr.length; i++) {
                if (arr[0].indexOf(key + "=") == 0) return $decode(unescape(arr[0].substr(key.length + 1)));
            }
            return {};
        },
        //暂存数据
        //@key <string> 暂存数据的关键字
        //@val <string> 暂存的数据字符串
        setData: function (key, val) {
            if (app && app.setData) app.setData(key, val);
            else document.cookie = "key=" + escape(val);
            return val;
        },
        //注册患者信息（包含床位和房间号）
        getBedSearch: function (syxh, yexh, name, sex, age, cwdm, blh, zdmc, room, lcljbz, ryrq) {
            if (app) app.getBedSearch.apply(app, arguments);
        },

        //切换到患者数据展示界面(病历、医嘱、检查、检验等);
        switchPatient: function (syxh, yexh, name, sex, age, blh, ryrq, zdmc, lcljbz) {
            if (app) app.switchPatient.apply(app, arguments);
        },
        //获取数据
        findData: function (provider, service, jsonargs) {
            return (app && app.findData && $decode(app.findData(provider, service, jsonargs))) || {};
        },
        //提交数据
        postData: function (provider, service, jsonargs) {
            return (app && app.postData && $decode(app.postData(provider, service, jsonargs))) || {};
        },
        //下载整个科室病区数据
        downloadAll: function () {
            var dept = this.getDept();
            if (app && app.downWard) return app.downWard(dept.ksdm, dept.bqdm);
        },
        //下载指定病人数据
        download: function (syxh, yexh) {
            if (app && app.downSingle) return app.downSingle(parseInt(syxh), parseInt(yexh));
        },
        //获取指定病区已下载患者列表
        getDownloaded: function () {
            var dept = this.getDept();
            return (app && app.getDownPaient && $decode(app.getDownPaient(dept.ksdm, dept.bqdm))) || {};
        },
        //转到查房界面
        forwardToVisit: function (syxh, yexh, name, sex, age, blh, ryrq, zdmc, lcljbz) {
            if (app && app.forwardCFJL) app.forwardCFJL.apply(app, arguments);
        },
        //转到查房历史界面
        showVisits: function () {
            if (app && app.showVisits) app.showVisits();
        },
        //是否在线
        //@return <int>  在线状态:1，在线   0, 离线
        isOnline: function () {
            if (app && app.isOnline) return app.isOnline() == 1;
            return false;
        },
        doEditBookmark: function (syxh, yexh, emrid) {
            if (app && app.doEditBookmark) return app.doEditBookmark(syxh, yexh, emrid);
            return false;
        }
    };

    if (app && app.datetimepicker) {
        external.datetimepicker = function (time, range, title, min, max, begin, end, editor1, editor2) {
            var format = ["yyyy-MM-dd", "yyyy-MM-dd HH:mm"][parseInt(time)];
            //alert(Array.prototype.slice.call(arguments, 0).join(','));
            var script = ["delete window['datetimepicker_callback'];",
                                "if(begin) begin = Date.fromRq16(begin).format('" + format + "');",
                                "if(end) end = Date.fromRq16(end).format('" + format + "');",
                                "document.getElementById('" + editor1 + "').value = begin;",
                                "var ev = document.createEvent('Event');",
                                "ev.initEvent('change',true,true);",
                                "document.getElementById('" + editor1 + "').dispatchEvent(ev);"];
            if (editor2) script.push("document.getElementById('" + editor2 + "').value = end;")
            window["datetimepicker_callback"] = new Function("begin", "end", script.join("\n"));
            return app.datetimepicker(time, range, title, min, max, begin, end);
        }
    }

    return external;
})(window.App);

var calc = function (expression) {
    try {
        return eval("(" + expression.substr(2, expression.length - 3) + ")") || "";
    } catch (e) {
        return "";
    }
};

Date.fromRq16 = function (rq16) {
    if (!rq16 || !rq16.length || rq16.length < 8) return new Date();
    var y = parseInt(rq16.substr(0, 4));
    var m = parseInt(rq16.substr(4, 2)) - 1;
    var d = parseInt(rq16.substr(6, 2));

    var h = parseInt(rq16.length >= 10 ? rq16.substr(8, 2) : "01");
    var mi = parseInt(rq16.length >= 13 ? rq16.substr(11, 2) : "01");
    var s = parseInt(rq16.length >= 16 ? rq16.substr(14, 2) : "01");

    return new Date(y, m, d, h, mi, s);
}

//格式化日期
Date.prototype.format = function (mask) {
    var o = {
        "y+": this.getFullYear(),
        "MM?": this.getMonth() + 1, //month 
        "dd?": this.getDate(), //day 
        "HH?": this.getHours(), //hour 
        "mm?": this.getMinutes(), //minute 
        "ss?": this.getSeconds(), //second 
        "qq?": Math.floor((this.getMonth() + 3) / 3), //quarter 
        "S": this.getMilliseconds() //millisecond 
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(mask)) mask = mask.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("00" + o[k]).length - RegExp.$1.length));
    }
    return mask;
}
//获取当前时间
Date.now = function () {
    return new Date();
};
Date.from = function (v) {
    if (/Date\(\-?[\d+]+\)/.test(v)) return eval('new ' + v.replace(/\//g, ''));
    alert("error format " + v);
}


Array.prototype.toConfigObject = function (keyfield, valuefield) {
    var obj = {};
    for (var i = 0; i < this.length; i++) {
        if (typeof (this[i]) != "object") continue;
        obj[this[i][keyfield] + ""] = this[i][valuefield];
    }
    return obj;
}

var $decode = function (json, defaultVal) {
    return eval("(" + (json || defaultVal) + ")");
}
String.format = function (m) {
    var values = Array.prototype.slice.call(arguments, 0).slice(1);
    var regex = /\{[0-9]+\}/g;
    return m.replace(regex, function (v) {
        return values[parseInt(v.replace(/^\{|\}$/g, ""))];
    });
}
var bindingData = function (component) {
    var fields = window["data-binding-fields"];
    for (var p in fields) $(fields[p]).html(calc(p)).show();

    $(window["data-binding-components"]).each(function () {
        if (!!component && this.id != component) return;

        var $self = $(this);
        var $this = this;
        var data = eval("(" + $self.data("source").replace(/^\{binding |\}$/g, "") + ")");
        var formater = eval("(" + (($self.data("formater") && ("window." + $self.data("formater"))) || "false") + ")");
        var defaultRowCount = parseInt($self.data("default-rows") || "0");
        var key_expr = $self.data("key-expression");
        var fn_key = key_expr ? new Function("idx", "return " + key_expr + " + '';") : new Function("idx", "return idx + '';");
        var rowbindEvent = $self.data("row-bind");
        var rowbindHandler = window[rowbindEvent] || false;

        var dataRows = [];
        var dataSource = {};

        if ($self.prop("tagName").toLowerCase() == "table") $self.children("tbody").empty();
        else $self.empty();
        for (var i = 0; !!data && (i < data.length || i < defaultRowCount) ; i++) {
            var row = $this.row_template;
            var regex = /\{\$[0-9a-zA-Z_]+\}/g;
            if (i < data.length) {
                var rec = data[i];
                if (rec.json) {
                    var mjson = rec.json;
                    rec = $decode(mjson);
                    rec._mjson = mjson;
                }
                var key = fn_key.call(rec, i);
                dataSource[key] = rec;
                row = row.replace(/\{data\-key\}/g, key).replace(/tpl\-row/g, "data-row").replace(regex, function (v) {
                    var field = v.substr(2, v.length - 3);
                    if (formater && formater[field]) return formater[field].call($this, rec, i);
                    else return (rec[field] === 0 ? 0 : (rec[field] || ""));
                });
                if (rowbindHandler && $.isFunction(rowbindHandler)) rowbindHandler.call($this, rec);
            } else {
                row = row.replace(/tpl\-row/g, "data-row").replace(/\{data\-key\}/g, i).replace(regex, "");
            }
            dataRows.push(row);
        }
        if (dataRows.length > 0) $self.append(dataRows.join(""));
        $this["meta-data"] = dataSource;
    });
}
$(document).ready(function () {
    //识别所有 data-binding fields
    var regex = /\{\$[a-zA-Z]+.?[a-zA-Z\.\(\)]*\}/g;
    var fields = {};
    window["data-binding-fields"] = fields;

    var components = [];
    window["data-binding-components"] = components;

    $(".data-binding").each(function () {
        var $this = this;
        $($this).html($($this).html().replace(regex, function (v) {
            fields[v] = $this;
            return "";
        })).show();
    });
    $(".data-source-binding").each(function () {
        components.push(this);
        var $this = $(this);
        var defaultRowCount = parseInt($(this).data("default-rows") || "0");
        var tpl = [], dataRows = [];
        $this.find(".tpl-row").each(function () {
            tpl.push(this.outerHTML);
        });
        var row = tpl.join("");
        this.row_template = row;
        if ($this.prop("tagName").toLowerCase() == "table") $this.children("tbody").empty();
        else $this.empty();
        for (var i = 0; i < defaultRowCount; i++) {
            var regex = /\{\$[0-9a-zA-Z_]+\}/g;
            dataRows.push(row.replace(regex, "").replace(/\{data\-key\}/g, i).replace(/tpl\-row/g, "data-row"));
        }
        $this.append(dataRows.join(""));
    });

    $(".app-binding").each(function () {
        var regex = /\{\$[a-zA-Z]+.?[a-zA-Z\.\(\)]*\}/g;
        $(this).html($(this).html().replace(regex, function (v) {
            return calc(v);
        }));
    });

    $(".date-picker").toPicker();
});

window.findParent = function (el, fn) {
    if (fn.apply(el) === true) return el;
    else if (el.parentElement) return findParent(el.parentElement, fn);
    else return false;
}



window.onerror = function (msg, url, line) {
    var error = ["error: ", msg, "\nurl: ", url, "\nline: ", line];
    if (window.$App) window.$App.alert(error.join(""));
    else alert(error.join(""));
}

//处理优化后的json数组，转化为对象数组。
Array.prototype.toObjectList = function () {
    var data = [];
    if (this.length == 0) return this;
    var fields = this[0];
    for (var i = 1; i < this.length; i++) {
        var instance = {};
        for (var j = 0; j < fields.length; j++) instance[fields[j]] = this[i][j];
        data.push(instance);
    }
    return data;
}