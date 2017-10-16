
//init
$(document).ready(function () {
    var combo = function () {
        var dropdown = $($(this).data("dropdown"));
        if (dropdown.data("expanded")) {
            dropdown.hide();
            dropdown.data("expanded", false);
        } else {
            $(".combo-panel").data("expanded", false).hide();
            var refer = $($(this).data("refer") || this);
            var location = refer.offset();
            location.top = location.top + refer.outerHeight();
            location.width = refer.outerWidth();
            dropdown.css(location).show();

            $(document).one("click", function () {
                $(".combo-panel").data("expanded", false).hide();
            });
            dropdown.data("expanded", true);

            var defaultEl = "<div class=\"nodata\">&lt;无&gt;></div>"
            var data = $(this).data("source");
            if (data == "no") return;
            dropdown.empty();
            if (data.length == 0) dropdown.html(defaultEl);
            else $(data).each(function (i) { $("<span>").text(this.name).appendTo(dropdown).data("item", this).on("click", onExpandElClick); });
        }
    };

    $(".combo").each(function (i) {
        this.combo = $.proxy(combo, this);
        var caret = $(this).data("caret") || false;
        if (caret) $(caret).on("click", $.proxy(function () { this.combo(); event.stopPropagation(); }, this));
    }).on("click", function () { this.combo(); event.stopPropagation(); });

    $("#dosage-selector > .dosage > span").on("click", function () {
        event.stopPropagation();

        var v = $(this).text();
        var el = $("#dosage-field");
        var dosage = el.text();
        if (this.title == "dot" && (dosage.length < 1 || dosage.indexOf(".") > -1)) return;
        if (this.title == "del" && dosage.length > 0) el.text(dosage.substr(0, dosage.length - 1));
        else if (this.title != "del") el.text(dosage + v);
        window.isEditting = !!$("#item-field").data("item");
    });

    $(".key-board").children().on("click", function () {
        var dom = document.getElementById("filter-keyword");//$("#filter-keyword");
        var el = $(dom);
        var text = el.text();
        if (this.id == "key-close") return;
        if (this.id == "key-del" && text.length > 0) el.text(text.substr(0, text.length - 1));
        else el.text(text + this.innerText);
        dom.delay = 0;
    });

    var tableEl = document.getElementById("datalist");
    //if (!tableEl.touchUtil) {
    //    tableEl.touchUtil = util.toucher(tableEl);
    //    tableEl.touchUtil.on("swipeStart", onOrderRowSwipeStart);
    //    tableEl.touchUtil.on("swipe", onOrderRowSwipe);
    //    tableEl.touchUtil.on("swipeEnd", onOrderRowSwipeEnd);
    //}
    setInterval(listenToQuery, 100)
    //setTimeout(loadData, 1);
});

//event
function onOrderRowSwipeStart(e) {
    event.preventDefault()
    var el = e.target || e.srcElement;
    var row = findParent(el, function () { return this.className && this.className.indexOf("data-row") > -1; });
    this.swipeEl = row;
    $("#order-edit-action").data("order", row.id.substr(4));
}
function onOrderRowSwipe(e) {
    var distance = e.startX - e.pageX;
    if (distance < 0) return;
    var top = this.swipeEl.rowIndex * $(this.swipeEl).outerHeight();
    var el = $("#order-edit-action").show().css({ top: top });
    el.css("right", (distance > 480 ? 0 : Math.round(-240 + distance / 2)) + "px");
}

function onOrderRowSwipeEnd(e) {
    var distance = e.startX - e.pageX;
    if (distance < 0) return;
    var el = $("#order-edit-action");
    if (distance >= 480);
    else if (distance >= 240 && distance < 480) el.animate({ right: 0 }, 300, "linear");
    else el.animate({ right: "-240px" }, 300, "linear", function () { $(this).hide(); });
}

function showItemSelector(e) {

    $("#item-selector").css("left", "100%").show().animate({ left: 0 }, 400, "linear", function () { $("#goback").hide(); });
};

function listenToQuery() {
    var dom = document.getElementById("filter-keyword");
    if (dom.delay === undefined) return;
    else dom.delay = dom.delay + 1;

    if (dom.delay == 4 && dom.isBusy !== true) {
        dom.isBusy = true;
        doQuery(dom.innerText);
        dom.isBusy = false;
    }
}

function doQuery(keyword) {
    if (!keyword) {
        window.Items = window["Items_bak"];
        bindingData("itemlist");
        return;
    }

    var findOut = [];
    var fields = ["py", "wb"];
    for (var idx = 0; idx < window["Items_bak"].length; idx++) {
        var info = window["Items_bak"][idx];
        var arr = (info.keyword || "").split(";");
        $.extend(info, { py: (arr.length > 0 ? (arr[0] || "") : ""), wb: (arr.length > 1 ? (arr[1] || "") : "") });
        for (var k = 0; k < fields.length; k++) {
            if (!info[fields[k]] || info[fields[k]].indexOf(keyword) < 0) continue;
            findOut.push(info);
            break;
        }
    }

    window.Items = findOut.sort(function (y, x) {
        for (var i = 0; i < fields.length; i++) {
            if (x[fields[i]].indexOf(keyword) == 0 && y[fields[i]].indexOf(keyword) == 0) return x[fields[i]].length < y[fields[i]].length ? 1 : -1;
            else if (x[fields[i]].indexOf(keyword) == 0) return 1;
            else if (y[fields[i]].indexOf(keyword) == 0) return -1;
        }

        for (var i = 0; i < fields.length; i++) {
            if (x[fields[i]].indexOf(keyword) > 0 && y[fields[i]].indexOf(keyword) > 0) return Math.abs(x[fields[i]].localeCompare(keyword)) < Math.abs(y[fields[i]].localeCompare(keyword)) ? 1 : -1;
            else if (x[fields[i]].indexOf(keyword) > 0) return 1;
            else if (y[fields[i]].indexOf(keyword) > 0) return -1;
        }
        return 0;
    });
    bindingData("itemlist");
}

function selectRow(td) {
    var id = td.parentElement.id.substr(4);
    var itemTable = document.getElementById("itemlist");
    var item = itemTable["meta-data"][id];
    $("#item-field").removeClass("f-blank").data("item", item).text(item.name);
    $("#dosage-field").text(item.units[0].ratio);
    $("#dosage-caret").data("item", item.units[0]).html(item.units[0].name + "<span class=\"combo-caret\"></span>");
    $("#unit-list").children("div").remove();
    for (var i = 0, j = 0; i < item.units.length || j < 3; i++, j++) {
        var unit = item.units[i];
        if (i >= item.units.length) $("#unit-list").find("span[title='cancel']").before($("<div><span style='border:none;background:none;filter:none;'>&nbsp;</span></div>"));
        else if ((window.isLeave && unit.type == 3) || (!window.isLeave && unit.type == 2)) $("#unit-list").find("span[title='cancel']").before($("<div><span style='border:none;background:none;filter:none;'>&nbsp;</span></div>"));
        else $("#unit-list").find("span[title='cancel']").before($("<div><span>" + unit.name + "</span></div>").data("item", unit).on("click", onUnitClick));
    }
    $("#item-selector").hide();
    window.isEditting = true;
}
function onExpandElClick() {
    var item = $(this).data("item");
    if (this.parentElement.id == "usage-list") {
        $("#usage").removeClass("f-blank").data("item", item).text(item.name);
        window.isEditting = !!$("#item-field").data("item");
    }
    else if (this.parentElement.id == "freq-list") {
        $("#frequency").removeClass("f-blank").data("item", item).text(item.name);
        window.isEditting = !!$("#item-field").data("item");
    }
}

function onUpDownClick(dir) {
    var n = dir == "down" ? -1 : 1;
    var ratio = parseFloat(($("#dosage-caret").data("item") && $("#dosage-caret").data("item").ratio) || "1");
    var dosage = parseFloat($("#dosage-field").text() || "0");
    if (n == 1 || dosage > ratio) $("#dosage-field").text(Number((dosage + ratio * n).toFixed(2)));
    window.isEditting = !!$("#item-field").data("item");
}

function onUnitClick() {
    var unit = $(this).data("item");
    $("#dosage-caret").data("item", unit).html(unit.name + "<span class=\"combo-caret\"></span>");
    window.isEditting = !!$("#item-field").data("item");
}

function onEditOrderClick(id) {
    if (isEditting && !$App.confirm("提示", "您正在编辑，是否撤销更改？")) return;
    beginEdit(id)
}

function onCreateClick() {
    if (isEditting && !$App.confirm("提示", "是否撤销更改，并新建医嘱？")) return;
    createOrder();
}

function onSaveClick(sender) {
    $(sender).text("处理中...");
    var start_time = $("#start_time").text().replace(/[\- ]/g, "");
    var item = $("#item-field").data("item");
    var dosage = $("#dosage-field").text();
    var unit = $("#dosage-caret").data("item");
    var usage = $("#usage").data("item");
    var freq = $("#frequency").data("item");
    var memo = $("#description").val();
    if (memo.indexOf("请输入") >= 0) memo = "";

    if (window.edittingOrder.rec_state == RecordState.Empty && !item) return $(sender).text("确定");

    var data = {
        syxh: $syxh,
        yexh: $yexh,
        xh: window.edittingOrder.id,
        form: {
            start_time: start_time,
            item: item.id,
            dosage: dosage,
            unit: unit,
            usage: usage.id,
            freq: freq.id,
            memo: memo
        }
    }
    window.EditOrders = $App.postData("order", "save-form", JSON.stringify(data));
    $App.alert("保存成功！");
    $(sender).text("确定");
    bindingData('datalist');
    this.createOrder();
}

function delEditOrder() {
    var order = $("#order-edit-action").hide().data("order");
    if (isNaN(parseInt(order))) return $App.alert("无效的操作！");
    else window.EditOrders = $App.postData("order", "delete", JSON.stringify({ syxh: $syxh, yexh: $yexh, xh: order }));
    bindingData('datalist');
    this.createOrder();
    $App.alert("删除成功！");
}
//logic

window.EditOrders = [];
window.Items = [];
window.edittingOrder = null;
window.isEditting = false;
window.isLeave = false;
window.RecordState = {
    Empty: -99,
    Normal: 0,
    Changed: 1,
    Deleted: 2,
    Created: 3,
    Commit: 99
};

function loadEditOrders() {
    window.EditOrders = [];
    for (var i = 0; i < window.allOrders.length; i++) {
        var order = $decode(this.allOrders[i].json);
        if (order.rec_state == RecordState.Empty) window.edittingOrder = order;
        else if (order.state == RecordState.Normal) window.EditOrders.push(order);
    }
    bindingData('datalist');

    window.Items = $App.findData("order", "all-item", "{}");
    window["Items_bak"] = window.Items;
    bindingData('itemlist');

    var usages_data = $App.findData("order", "usage", "");
    //var usages_data = $decode($App.getData("_ORDER_USAGES_") || "false");
    //if (!usages_data) $App.setData("_ORDER_USAGES_", usages_data = $App.findData("order", "usage", ""));
    $("#usage").data("source", usages_data);

    var freqs_data = $App.findData("order", "frequency", "");
    //var freqs_data = $decode($App.getData("_ORDER_FREQS_") || "false");
    //if (!freqs_data) $App.setData("_ORDER_FREQS_", freqs_data = $App.findData("order", "frequency", ""));
    $("#frequency").data("source", freqs_data);

    createOrder();
}

function createOrder() {
    isEditting = false;
    $("#start_time").text(Date.now().format("yyyy-MM-dd HH:mm"));
    $("#item-field").addClass("f-blank").data("item", null).text("");
    $("#dosage-field").text("1");
    $("#dosage-caret").data("item", null).html("&nbsp;&nbsp;<span class=\"combo-caret\"></span>");
    $("#usage").addClass("f-blank").data("item", null).text("");
    $("#frequency").addClass("f-blank").data("item", null).text("");
    $("#description").addClass("f-blank").val("     <请输入...>");
    $("#save-button").text("确定");
    if (!window.edittingOrder || window.edittingOrder.rec_state == RecordState.Empty) window.edittingOrder = $App.findData("order", "create", String.format("{\"syxh\":{0},\"yexh\":{1},\"ysdm\":\"{2}\"}", $syxh, $yexh, $App.getDoctor().id));
}

function beginEdit(id) {
    var el = document.getElementById("datalist");
    var order = el["meta-data"][id];
    if (!order.item) $.extend(order, $App.findData("order", "edit", String.format("{\"syxh\":{0},\"yexh\":{1},\"id\":\"{2}\"}", $syxh, $yexh, order.id)));

    $("#start_time").text(Date.from(order.start_time).format("yyyy-MM-dd HH:mm"));
    $("#item-field").removeClass("f-blank").data("item", order.item).text(order.item_name);
    $("#dosage-field").text(order.dosage);
    for (var i = 0; i < order.item.units.length; i++) {
        if (order.item.units[i].name !== order.unit) continue;
        $("#dosage-caret").data("item", order.item.units[i]).html(order.unit + "<span class=\"combo-caret\"></span>");
        break;
    }
    $("#usage").removeClass("f-blank").data("item", order.usage).text(order.usage_name);
    $("#frequency").removeClass("f-blank").data("item", order.freq).text(order.freq_name);
    if (order.memo) $("#description").removeClass("f-blank").val(order.memo);
    else if (!$("#description").hasClass("f-blank")) $("#description").addClass("f-blank");
    window.edittingOrder = order;
}