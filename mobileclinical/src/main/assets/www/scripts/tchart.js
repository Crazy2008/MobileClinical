//create by lsh 2016.05.24
var TChart = function (id, config) {
    var $this = this, $inner = {};
    var $element = typeof (id) == "string" ? document.getElementById(id) : id;
    var $settings = {
        ybcolor: "#009bdc", //腋表颜色
        kbcolor: "#8fc926", //口表颜色
        gwcolor: "#ed7c00", //肛温颜色
        hxcolor: "#95E340", //呼吸颜色
        mbcolor: "#e9c21b", //脉搏颜色
        margin: 5//px
    };
    for (var s in (config || {})) $settings[s] = config[s];

    //每个canvas显示2周;
    var $weeks = 2;
    //
    var $rows = 8 + 25;
    var $columns = 6 + 42;

    var $width = $element.parentElement.offsetWidth;
    var $height = $element.parentElement.offsetHeight - 10;


    var $defined = {
        fontcolor: "#424242",
        linecolor: "#F1F1F1",
        labelcolor: "#C5C5C5",
        fieldcolor: "#193CDA",
        hbackcolor: "#fDfDfD",
        hcell: ($height - $settings.margin * 2.0) / $rows,
        wcell: ($width - $settings.margin * 2.0) / $columns,
        font: Math.round((($width - $settings.margin * 2.0) / $columns) * 0.7 * 100) / 100 + "px 'Microsoft YaHei'"
    };
    $inner.drawers = [];
    $inner.apply = function (d, o) {
        if (!o) return d;
        d = d || {};
        for (var p in o) d[p] = o[p];
    };

    $inner.find = function (arr, fn) {
        var r = [];
        for (var i = 0; i < arr.length; i++) {
            if (fn.apply(arr[i], Array.prototype.slice.call(arguments, 2))) r.push(arr[i]);
        }
        return r;
    };

    $inner.buildLabel = function (time) {
        if ($inner.labels) return $inner.labels;
        var arr = [];
        var t = parseInt(time.substring(0, 2));
        while (t - 4 > -1) t -= 4;
        for (var i = 0; i < 6; i++) {
            var v = "0" + (t + (i * 4));
            arr[i] = v.substr(v.length - 2);
        }
        return $inner.labels = arr;
    };

    //体温（圆形）
    $inner.drawTwDot = function (drawer, x, y, r, color) {
        drawer.beginPath();
        drawer.fillStyle = color;
        drawer.arc(x, y, r, 0, 2 * Math.PI);
        drawer.fill();
        drawer.closePath();
    };
    //脉搏（正方形）
    $inner.drawMbDot = function (drawer, x, y, r, color) {
        drawer.beginPath();
        drawer.fillStyle = color;
        drawer.fillRect(x, y, r, r);
        drawer.closePath();
    };
    //呼吸（三角形）
    $inner.drawHxDot = function (drawer, x, y, r, color) {
        drawer.moveTo(x, y - r);
        drawer.beginPath();
        drawer.fillStyle = color;
        drawer.lineTo(x - (r * Math.sqrt(3, 2)) / 2 / 2, y + r / 2);
        drawer.lineTo(x + (r * Math.sqrt(3, 2)) / 2 / 2, y + r / 2);
        drawer.lineTo(x, y - r / 2);
        drawer.fill();
        drawer.closePath();
    };

    $inner.drawLegend = function () {
        var legend = document.createElement("canvas");
        legend.width = $defined.wcell;
        legend.height = $defined.hcell;
        var ldrawer = legend.getContext("2d");
        var icon = {};
        $inner.drawTwDot(ldrawer, $defined.wcell / 2, $defined.hcell / 2, Math.min($defined.wcell, $defined.hcell) / 2, $settings.ybcolor);
        icon.tw_icon = legend.toDataURL("image/png");
        ldrawer.clearRect(0, 0, $defined.wcell, $defined.hcell);
        $inner.drawMbDot(ldrawer, ($defined.wcell - Math.min($defined.wcell, $defined.hcell)) / 2, ($defined.hcell - Math.min($defined.wcell, $defined.hcell)) / 2, Math.min($defined.wcell, $defined.hcell), $settings.mbcolor);
        icon.mb_icon = legend.toDataURL("image/png");
        ldrawer.clearRect(0, 0, $defined.wcell, $defined.hcell);
        $inner.drawHxDot(ldrawer, $defined.wcell / 2, $defined.hcell / 2, Math.min($defined.wcell, $defined.hcell), $settings.hxcolor);
        icon.hx_icon = legend.toDataURL("image/png");
        return icon;
    };

    $inner.drawGrid = function (drawer, weeks) {
        var cols = weeks * 7 * 6.0;
        drawer.strokeStyle = $defined.linecolor;
        drawer.beginPath();
        //画行
        for (var i = 0; i < $rows; i++) {
            var y = $defined.hcell * (i + 1);
            if (i >= 8) y = ($defined.hcell + 3.0 / ($rows - 8)) * (i + 1);//3像素误差
            if (i < 8 && (i + 1) % 2 == 1) continue;
            drawer.moveTo(0, y);
            drawer.lineTo(cols * $defined.wcell, y);
        }
        //画列
        for (var i = 0; i < cols - 1; i++) {
            if ((i + 1) % 6 == 0) drawer.moveTo((i + 1) * $defined.wcell, 0);
            else drawer.moveTo((i + 1) * $defined.wcell, $defined.hcell * 6);
            drawer.lineTo((i + 1) * $defined.wcell, $height);
        }
        drawer.stroke();

        //画体温警告线
        drawer.strokeStyle = $settings.ybcolor;
        drawer.beginPath();
        var warn_line_y = ($defined.hcell * 8) + (1 - ((38 - 34) / 10.0)) * $defined.hcell * 25.0;
        var w = cols * $defined.wcell;
        for (var x = 0, i = 0; x <= w; x += 3, i++) {
            if ((i + 1) % 2 == 0) continue;
            drawer.moveTo(x, warn_line_y);
            drawer.lineTo(x + 3, warn_line_y);
        }
        drawer.stroke();
    };
    $inner.drawData = function (drawer, data) {
        //画点
        var types = ["yb", "kb", "gw"];
        var points = { tw: [], mb: [], hx: [] };
        var labels = null;

        drawer.font = $defined.font;
        var lheight = $defined.hcell * 0.8;

        for (var i = 0; i < data.length; i++) {
            var rec = eval("(" + (data[i].json || "false") + ")") || data[i];
            var twdata = $inner.find(rec.items, function () { return this.type == 0; })[0];
            if (i == 0) labels = $inner.buildLabel(twdata.value[0].time);

            drawer.fillStyle = $defined.fontcolor;
            //画日期
            var date = rec.date.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
            if (!$inner.dateWidth) $inner.dateWidth = drawer.measureText(date).width;
            drawer.fillText(date, i * 6 * $defined.wcell + ($defined.wcell * 6 - $inner.dateWidth) / 2, ($defined.hcell * 2) - (($defined.hcell * 2 - lheight) / 2));

            //画住院天数
            var zyts = rec.zyts + "";
            var textWidth = drawer.measureText(zyts).width;
            drawer.fillText(zyts, i * 6 * $defined.wcell + ($defined.wcell * 6 - textWidth) / 2, ($defined.hcell * 2) + ($defined.hcell * 2) - (($defined.hcell * 2 - lheight) / 2));

            //画术后天数
            var arrshts = (rec.shts || "").replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "").split("/");
            for (var x = arrshts.length - 1; x > -1 ; x--) {
                if (parseInt(arrshts[x]) < 1) arrshts.pop();
                else break;
            }
            var shts = arrshts.join("/");
            textWidth = arrshts.length && drawer.measureText(shts).width;
            if (arrshts.length > 0) drawer.fillText(shts, i * 6 * $defined.wcell + ($defined.wcell * 6 - textWidth) / 2, ($defined.hcell * 4) + ($defined.hcell * 2) - (($defined.hcell * 2 - lheight) / 2));

            //画点,画label
            var filter = function (t) { return this.time.indexOf(t) == 0; };
            for (var k = 0; k < 6; k++) {
                var x = (i * 6 * $defined.wcell) + (k * $defined.wcell) + $defined.wcell / 2;
                var records = twdata && $inner.find(twdata.value, filter, labels[k]) || null;
                var item = records && records.length ? records[0] : null;
                //label
                drawer.fillStyle = $defined.fontcolor;
                if (!$inner.labelWidth) $inner.labelWidth = drawer.measureText("24").width;
                drawer.fillText(labels[k], (i * 6 * $defined.wcell) + ($defined.wcell * k) + (($defined.wcell - $inner.labelWidth) / 2), ($defined.hcell * 6) + ($defined.hcell * 2) - (($defined.hcell * 2 - lheight) / 2));
                if (!item) continue;
                //体温
                item.tw = item.tw && item.tw >= 34 ? item.tw : (!item.tw ? 0 : 34);
                if (item.tw > 0) {
                    var tw_y = ($defined.hcell * 8) + (1 - ((item.tw - 34) / 10.0)) * $defined.hcell * 25.0;
                    $inner.drawTwDot(drawer, x, tw_y, Math.min($defined.wcell, $defined.hcell) / 4, $settings[types[item.twlx] + "color"]);
                    points.tw.push({ x: x, y: tw_y });
                }
                if (!points.twcolor) points.twcolor = $settings[types[item.twlx] + "color"];
                //脉搏
                item.mb = item.mb && item.mb >= 20 ? item.mb : (!item.mb ? 0 : 20);
                if (item.mb > 0) {
                    var mb_y = ($defined.hcell * 8) + (1 - ((item.mb - 20) / 150.0)) * $defined.hcell * 25.0
                    $inner.drawMbDot(drawer, x - Math.min($defined.wcell, $defined.hcell) / 4.0, mb_y - Math.min($defined.wcell, $defined.hcell) / 4.0, Math.min($defined.wcell, $defined.hcell) / 2, $settings.mbcolor);
                    points.mb.push({ x: x, y: mb_y });
                }
                if (!points.mbcolor) points.mbcolor = $settings.mbcolor;
                //呼吸
                item.hx = item.hx && item.hx >= 10 ? item.hx : (!item.hx ? 0 : 10);
                if (item.hx > 0) {
                    var hx_y = ($defined.hcell * 8) + (1 - ((item.hx - 10) / 50.0)) * $defined.hcell * 25.0
                    $inner.drawHxDot(drawer, x, hx_y, Math.min($defined.wcell, $defined.hcell) / 2, $settings.hxcolor);
                    points.hx.push({ x: x, y: hx_y });
                }
                if (!points.hxcolor) points.hxcolor = $settings.hxcolor;
            }
        }
        //脉搏体温
        for (var idx = 0; points.tw.length > 1 && idx < points.tw.length - 1; idx++) {
            if (idx == 0) {
                drawer.beginPath();
                drawer.strokeStyle = points.twcolor;
                drawer.moveTo(points.tw[idx].x, points.tw[idx].y);
            }
            drawer.lineTo(points.tw[idx + 1].x, points.tw[idx + 1].y);
            if (idx == points.tw.length - 2) drawer.stroke();
        }
        if (points.tw.length > 0) $inner.borderPoints.tw.push({ first: points.tw[0], last: points.tw[points.tw.length - 1] });
        $inner.borderPoints.twcolor = points.twcolor;
        //脉搏连线
        for (var idx = 0; points.mb.length > 1 && idx < points.mb.length - 1; idx++) {
            if (idx == 0) {
                drawer.beginPath();
                drawer.strokeStyle = points.mbcolor;
                drawer.moveTo(points.mb[idx].x, points.mb[idx].y);
            }
            drawer.lineTo(points.mb[idx + 1].x, points.mb[idx + 1].y);
            if (idx == points.mb.length - 2) drawer.stroke();
        }
        if (points.mb.length > 0) $inner.borderPoints.mb.push({ first: points.mb[0], last: points.mb[points.mb.length - 1] });
        $inner.borderPoints.mbcolor = points.mbcolor;
        //呼吸连线
        for (var idx = 0; points.hx.length > 1 && idx < points.hx.length - 1; idx++) {
            if (idx == 0) {
                drawer.beginPath();
                drawer.strokeStyle = points.hxcolor;
                drawer.moveTo(points.hx[idx].x, points.hx[idx].y);
            }
            drawer.lineTo(points.hx[idx + 1].x, points.hx[idx + 1].y);
            if (idx == points.hx.length - 2) drawer.stroke();
        }
        if (points.hx.length > 0) $inner.borderPoints.hx.push({ first: points.hx[0], last: points.hx[points.hx.length - 1] });
        $inner.borderPoints.hxcolor = points.hxcolor;

        $inner.borderPoints.len = $inner.borderPoints.len + 1;
    };

    $inner.drawBorders = function () {
        if ($inner.borderPoints.len < 2) return;
        for (var i = 0; i < $inner.borderPoints.len; i++) {
            var canvas = $inner.drawers[i];
            var drawer = $inner.drawers[i].drawer;
            var twtuple = $inner.borderPoints.tw[i];
            var mbtuple = $inner.borderPoints.mb[i];
            var hxtuple = $inner.borderPoints.hx[i];

            if (i > 0) {
                var pcanvas = $inner.drawers[i - 1];
                var pdrawer = $inner.drawers[i - 1].drawer;
                var ptwtuple = $inner.borderPoints.tw[i - 1];
                var pmbtuple = $inner.borderPoints.mb[i - 1];
                var phxtuple = $inner.borderPoints.hx[i - 1];
                //上一个canvas的尾部体温
                pdrawer.beginPath();
                pdrawer.strokeStyle = $inner.borderPoints.twcolor;
                pdrawer.moveTo(ptwtuple.last.x, ptwtuple.last.y);
                pdrawer.lineTo(pcanvas.width, ptwtuple.last.y - (ptwtuple.last.y - twtuple.first.y) / 2);
                pdrawer.stroke();

                //上一个canvas的尾部脉搏
                pdrawer.beginPath();
                pdrawer.strokeStyle = $inner.borderPoints.mbcolor;
                pdrawer.moveTo(pmbtuple.last.x, pmbtuple.last.y);
                pdrawer.lineTo(pcanvas.width, pmbtuple.last.y - (pmbtuple.last.y - mbtuple.first.y) / 2);
                pdrawer.stroke();

                //上一个canvas的尾部呼吸
                pdrawer.beginPath();
                pdrawer.strokeStyle = $inner.borderPoints.hxcolor;
                pdrawer.moveTo(phxtuple.last.x, phxtuple.last.y);
                pdrawer.lineTo(pcanvas.width, phxtuple.last.y - (phxtuple.last.y - hxtuple.first.y) / 2);
                pdrawer.stroke();

                //当前canvas的开头体温
                drawer.beginPath();
                drawer.strokeStyle = $inner.borderPoints.twcolor;
                drawer.moveTo(0, ptwtuple.last.y - (ptwtuple.last.y - twtuple.first.y) / 2);
                drawer.lineTo(twtuple.first.x, twtuple.first.y);
                drawer.stroke();

                //当前canvas的开头脉搏
                drawer.beginPath();
                drawer.strokeStyle = $inner.borderPoints.mbcolor;
                drawer.moveTo(0, pmbtuple.last.y - (pmbtuple.last.y - mbtuple.first.y) / 2);
                drawer.lineTo(mbtuple.first.x, mbtuple.first.y);
                drawer.stroke();

                //当前canvas的开头呼吸
                drawer.beginPath();
                drawer.strokeStyle = $inner.borderPoints.hxcolor;
                drawer.moveTo(0, phxtuple.last.y - (phxtuple.last.y - hxtuple.first.y) / 2);
                drawer.lineTo(hxtuple.first.x, hxtuple.first.y);
                drawer.stroke();
            }
        }
    };
    //初始化容器
    $inner.initContainer = function (data) {
        var tpl = ["<div style=\"float:left;width:12.5%;background-color:{hbackcolor};border:2px solid {linecolor};color:{labelcolor};\">",
                            "<div style=\"height:{hcell * 2};line-height:{hcell * 2};text-align:center;border-bottom:1px solid {linecolor};\">日期</div>",
                            "<div style=\"height:{hcell * 2};line-height:{hcell * 2};text-align:center;border-bottom:1px solid {linecolor};\">住院天数</div>",
                            "<div style=\"height:{hcell * 2};line-height:{hcell * 2};text-align:center;border-bottom:1px solid {linecolor};\">术后天数</div>",
                            "<div style=\"height:{hcell * 2};line-height:{hcell * 2};text-align:center;border-bottom:1px solid {linecolor};\">时间</div>",
                            "<div style=\"height:{hcell * 25};color:{fontcolor};\">",
                                "<div style=\"float:left;width:33.333%;border-right:1px {linecolor} solid;height:100%;\">",
                                    "<div style=\"height:{hcell * 8};text-align:center;color:{fieldcolor};\">呼<br />吸<br/><br/><img src=\"{hx_icon}\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">40</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">30</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">20</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">10</span></div>",
                                "</div>",
                                "<div style=\"float:left;width:33.334%;border-right:1px {linecolor} solid;height:100%;\">",
                                    "<div style=\"height:{hcell * 8};text-align:center;color:{fieldcolor};\">脉<br />搏<br/><br/><img src=\"{mb_icon}\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">110</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">80</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">50</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">20</span></div>",
                                "</div>",
                                "<div style=\"float:left;width:33.333%;height:100%;\">",
                                    "<div style=\"height:{hcell * 8};text-align:center;color:{fieldcolor};\">体<br />温<br/><br/><img src=\"{tw_icon}\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">40</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">38</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">36</span></div><div style=\"height:{hcell * 3};\"></div>",
                                    "<div style=\"height:{hcell * 2};position:relative;\"><span style=\"width:100%;text-align:center;position:absolute;bottom: 0px;\">34</span></div>",
                                "</div>",
                            "</div>",
                      "</div>",
                      "<div style=\"float:right;width:87.5%;overflow-x:auto;\"><div></div></div>",
                       "<div class=\"clear\"></div>"];
        $element.style.margin = $settings.margin + "px";
        $element.style.fontSize = (Math.round(data["wcell"] * 0.8 * 10000) / 10000 / 16) + "em";
        $element.innerHTML = tpl.join("").replace(/\{hcell \* [0-9]+\}/g, function (v) { return (data["hcell"] * v.match(/[0-9]+/)[0]) + "px"; }).replace(/\{\w+\}/g, function (v) { return data[v.substr(1, v.length - 2)]; });
        $inner.container = $element.children[1].children[0];
    };

    $inner.initView = function () {
        var defaultCanvas = $inner.createCanvas(1);
        defaultCanvas.style.borderRight = "2px " + $defined.linecolor + " solid";
        var drawer = defaultCanvas.getContext("2d");
        defaultCanvas.drawer = drawer;
        $inner.drawGrid(drawer, 1);
        $inner.drawers.push(defaultCanvas);
        $inner.container.style.width = defaultCanvas.width + "px";
        $inner.container.appendChild(defaultCanvas);
    };

    $inner.createCanvas = function (weeks) {
        var cvs = document.createElement("canvas");
        cvs.height = $height - 6;//6像素误差
        cvs.width = weeks * 7 * 6.0 * $defined.wcell;
        cvs.style.width = cvs.width + "px";
        cvs.style.height = cvs.height + "px";
        cvs.style.borderTop = "2px " + $defined.linecolor + " solid";
        cvs.style.borderBottom = "2px " + $defined.linecolor + " solid";
        cvs.style.styleFloat = "left";
        return cvs;
    };

    //绘制界面
    $inner.buildCanvas = function (days) {
        var length = $inner.drawers.length;
        var allweeks = Math.ceil(days / (7.0 * $weeks));
        if (allweeks > length) {
            //add
            for (var i = 1 * $weeks * 7 ; i < days; i += ($weeks * 7)) {
                var weeks = Math.ceil((Math.min(days, i + ($weeks * 7)) - i) / 7);
                var cvs = $inner.createCanvas(weeks);
                cvs.drawer = cvs.getContext("2d");
                $inner.drawers.push(cvs);
                $inner.container.appendChild(cvs);
                $inner.drawers[$inner.drawers.length - 2].style.borderRight = "1px " + $defined.linecolor + " solid";
            }
        }
        else if (allweeks < length) {
            //remove
            $inner.drawers.splice(allweeks, length - allweeks);
            var children = Array.prototype.slice.call($inner.container.children, 0);
            var nodes = children.splice(allweeks, length - allweeks);
            for (var r = 0; r < nodes.length; r++) nodes[r].parentElement.removeChild(nodes[r]);
            //resize last canvas
            if (days % ($weeks * 7) > 7) {
                $inner.drawers[$inner.drawers.length - 1].width = $weeks * 7 * 6.0 * $defined.wcell;
                $inner.drawers[$inner.drawers.length - 1].style.width = $inner.drawers[$inner.drawers.length - 1].width + "px";
            }
            else {
                $inner.drawers[$inner.drawers.length - 1].width = 7 * 6.0 * $defined.wcell;
                $inner.drawers[$inner.drawers.length - 1].style.width = $inner.drawers[$inner.drawers.length - 1].width + "px";
            }
        }
        if (days > 7) {
            //resize first canvas
            $inner.drawers[0].width = $weeks * 7 * 6.0 * $defined.wcell;
            $inner.drawers[0].style.width = $inner.drawers[0].width + "px";
        }
        //resize container
        $inner.container.style.width = Math.ceil(days / 7.0) * 7 * 6.0 * $defined.wcell + "px";
        $inner.drawers[$inner.drawers.length - 1].style.borderRight = "2px " + $defined.linecolor + " solid";
        //clear and draw grid line.
        for (var i = 0; i < $inner.drawers.length; i++) {
            $inner.drawers[i].drawer.clearRect(0, 0, $inner.drawers[i].width, $inner.drawers[i].height);
            $inner.drawGrid($inner.drawers[i].drawer, Math.round($inner.drawers[i].width / (7 * 6.0 * $defined.wcell)));
        }
    };

    //绑定数据
    this.load = function (data) {
        if (!data || !data.length) return;
        $inner.buildCanvas(data.length);
        $inner.borderPoints = { tw: [], mb: [], hx: [], twcolor: "", mbcolor: "", hxcolor: "", len: 0 };
        for (var i = 0, j = 0; i < data.length ; i += ($weeks * 7), j++) {
            var last = Math.min(data.length, i + ($weeks * 7));
            var arr = data.slice(i, last);
            $inner.drawData($inner.drawers[j].drawer, arr);
        }
        $inner.drawBorders();
        //滚动到最后面
        if (data.length > 7) $inner.container.parentElement.scrollLeft = Math.floor(data.length / 7 - 1) * 7 * 6 * $defined.wcell + data.length % 7 * 6 * $defined.wcell;
    };

    //初始化视图
    var legend = $inner.drawLegend();
    $inner.apply($defined, legend);
    $inner.initContainer($defined);
    $inner.initView();
}