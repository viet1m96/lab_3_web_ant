(function () {
    "use strict";

    var SECONDS_BASE = 11;

    var DOW = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    var MON = ["January","February","March","April","May","June","July","August","September","October","November","December"];

    function $(id) { return document.getElementById(id); }
    function ensureNode(id, parentId, tag) {
        var el = $(id);
        if (!el) {
            var parent = $(parentId) || document.body;
            el = document.createElement(tag || "span");
            el.id = id;
            parent.appendChild(el);
        }
        return el;
    }

    var containerId = "date11";
    ensureNode(containerId, null, "div");
    var elDow  = ensureNode("dow11",  containerId, "span");
    var elDay  = ensureNode("day11",  containerId, "span");
    var elMon  = ensureNode("mon11",  containerId, "span");
    var elYear = ensureNode("year11", containerId, "span");

    var dateObj = new Date();

    function renderDate() {
        elDow.textContent  = DOW[dateObj.getDay()] + ", ";
        elDay.textContent  = String(dateObj.getDate()).padStart(2, "0") + " ";
        elMon.textContent  = MON[dateObj.getMonth()] + " ";
        elYear.textContent = String(dateObj.getFullYear());
    }

    renderDate();

    document.addEventListener('tick11', function (e) {
        var d = e.detail || {};
        if (d.h === 0 && d.m === 0 && d.s11 === 0) {
            dateObj.setDate(dateObj.getDate() + 1);
            renderDate();
        }
    });
})();
