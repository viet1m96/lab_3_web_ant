function getTimeSegmentElements(segmentElement) {
    const segmentDisplay = segmentElement.querySelector('.segment-display');
    const segmentDisplayTop = segmentDisplay.querySelector('.segment-display__top');
    const segmentDisplayBottom = segmentDisplay.querySelector('.segment-display__bottom');
    const segmentOverlay = segmentDisplay.querySelector('.segment-overlay');
    const segmentOverlayTop = segmentOverlay.querySelector('.segment-overlay__top');
    const segmentOverlayBottom = segmentOverlay.querySelector('.segment-overlay__bottom');
    return { segmentDisplayTop, segmentDisplayBottom, segmentOverlay, segmentOverlayTop, segmentOverlayBottom };
}
function updateSegmentValues(displayElement, overlayElement, value) {
    displayElement.textContent = value; overlayElement.textContent = value;
}
function updateTimeSegment(segmentElement, timeValue) {
    const els = getTimeSegmentElements(segmentElement);
    if (parseInt(els.segmentDisplayTop.textContent || "-1", 10) === timeValue) return;
    els.segmentOverlay.classList.add('flip');
    updateSegmentValues(els.segmentDisplayTop, els.segmentOverlayBottom, timeValue);
    function finish() {
        updateSegmentValues(els.segmentDisplayBottom, els.segmentOverlayTop, timeValue);
        els.segmentOverlay.classList.remove('flip');
        els.segmentOverlay.removeEventListener('animationend', finish);
    }
    els.segmentOverlay.addEventListener('animationend', finish);
}
function updateTimeSection(sectionID, timeValue) {
    const tens = Math.floor(timeValue / 10) || 0;
    const ones = timeValue % 10 || 0;
    const section = document.getElementById(sectionID);
    const segments = section.querySelectorAll('.time-segment');
    updateTimeSegment(segments[0], tens);
    updateTimeSegment(segments[1], ones);
}

(function() {
    const now = new Date();
    let h = now.getHours();
    let m = now.getMinutes();
    let s11 = now.getSeconds() % 11;

    function render() {
        updateTimeSection('hours', h);
        updateTimeSection('minutes', m);
        updateTimeSection('seconds', s11);
    }

    function emitTick() {
        document.dispatchEvent(new CustomEvent('tick11', {
            detail: { h, m, s11 }
        }));
    }

    render();
    emitTick();
    
    setInterval(() => {
        s11++;
        if (s11 >= 11) {
            s11 = 0;
            m++;
            if (m >= 60) {
                m = 0;
                h = (h + 1) % 24;
            }
        }
        render();
        emitTick();
    }, 1000);
})();
// (function () {
//     "use strict";
//
//     const now = new Date();
//     let h = now.getHours();
//     let m = now.getMinutes();
//     let s11 = now.getSeconds() % 11;
//
//     const params = new URLSearchParams(location.search);
//     const DEV_MIDNIGHT = params.has("dev_midnight");
//     const DEV_SPEED = parseInt(params.get("dev_speed") || "1000", 10);
//
//     if (DEV_MIDNIGHT) {
//         h = 23;
//         m = 59;
//         s11 = 0;
//     }
//
//     if (typeof window.updateTimeSection !== "function") {
//         window.updateTimeSection = function (section, value) {
//             const container =
//                 document.getElementById(section) || document.body;
//
//             const id = section + "-value";
//             let el = document.getElementById(id);
//             if (!el) {
//                 el = document.createElement("span");
//                 el.id = id;
//                 el.style.marginRight = "8px";
//                 el.style.fontFamily = "monospace";
//                 el.style.fontSize = "24px";
//                 container.appendChild(el);
//             }
//
//             el.textContent = String(value).padStart(2, "0");
//         };
//     }
//
//     function render() {
//         updateTimeSection("hours", h);
//         updateTimeSection("minutes", m);
//         updateTimeSection("seconds", s11);
//     }
//
//     function emitTick() {
//         document.dispatchEvent(
//             new CustomEvent("tick11", { detail: { h, m, s11 } })
//         );
//     }
//
//
//
//     window.__setClock11 = function (H, M, S11) {
//         if (
//             !Number.isInteger(H) ||
//             !Number.isInteger(M) ||
//             !Number.isInteger(S11)
//         )
//             return;
//         if (H < 0 || H > 23 || M < 0 || M > 59 || S11 < 0 || S11 > 10) return;
//         h = H;
//         m = M;
//         s11 = S11;
//         render();
//         emitTick();
//     };
//     render();
//     emitTick();
//
//     const intervalMs =
//         Number.isFinite(DEV_SPEED) && DEV_SPEED > 0
//             ? DEV_SPEED
//             : DEV_MIDNIGHT
//                 ? 250
//                 : 1000;
//
//     setInterval(function () {
//         s11++;
//         if (s11 >= 11) {
//             s11 = 0;
//             m++;
//             if (m >= 60) {
//                 m = 0;
//                 h = (h + 1) % 24;
//             }
//         }
//         render();
//         emitTick();
//     }, intervalMs);
// })();

