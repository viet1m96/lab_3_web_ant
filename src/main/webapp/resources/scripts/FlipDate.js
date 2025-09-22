(function () {
    "use strict";
    var SECONDS_BASE = 11;

    var DOW = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    var MON = ["January","February","March","April","May","June",
        "July","August","September","October","November","December"];

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
    var elDow  = ensureNode("dow11",  containerId, "span");
    var elDay  = ensureNode("day11",  containerId, "span");
    var elMon  = ensureNode("mon11",  containerId, "span");
    var elYear = ensureNode("year11", containerId, "span");


    var now = new Date();
    var dateObj = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    var hh = now.getHours();
    var mm = now.getMinutes();
    var s11 = now.getSeconds() % SECONDS_BASE;

    function pad2(n) { return (n < 10 ? "0" + n : "" + n); }

    function renderDate() {
        elDow.textContent  = DOW[ dateObj.getDay() ];
        elDay.textContent  = pad2( dateObj.getDate() );
        elMon.textContent  = MON[ dateObj.getMonth() ];
        elYear.textContent = dateObj.getFullYear();
    }

    renderDate();
    var timer = setInterval(function () {
        s11++;
        if (s11 >= SECONDS_BASE) {
            s11 = 0;
            mm++;
            if (mm >= 60) {
                mm = 0;
                hh++;
                if (hh >= 24) {
                    hh = 0;
                    dateObj.setDate(dateObj.getDate() + 1);
                    renderDate();
                }
            }
        }
    }, 1000);

    var obs = new MutationObserver(function () {
        var container = $(containerId);
        if (!container || !document.body.contains(container)) {
            clearInterval(timer);
            obs.disconnect();
        }
    });
    obs.observe(document.body, { childList: true, subtree: true });
})();
