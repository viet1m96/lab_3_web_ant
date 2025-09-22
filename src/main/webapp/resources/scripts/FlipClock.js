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
    let h = now.getHours(), m = now.getMinutes(), s11 = now.getSeconds() % 11;
    function render(){ updateTimeSection('hours',h); updateTimeSection('minutes',m); updateTimeSection('seconds',s11); }
    render();
    setInterval(() => {
        s11++; if (s11>=11){ s11=0; m++; if(m>=60){ m=0; h=(h+1)%24; } }
        render();
    }, 1000);
})();