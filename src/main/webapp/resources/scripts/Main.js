window.loadPointsFromJson = function (json) {
    try {
        const arr = (typeof json === 'string') ? JSON.parse(json) : json;
        if (!Array.isArray(arr)) { console.warn('Invalid points JSON'); return; }
        window.pointsXY = arr
            .map(p => ({ x: Number(p.x), y: Number(p.y) }))
            .filter(p => Number.isFinite(p.x) && Number.isFinite(p.y));
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawGraph();
    } catch (e) {
        console.error('Failed to parse points JSON', e);
    }
};

function showResultOverlay(hit) {
    console.log(hit);
    const box = document.getElementById('result-overlay');
    const text = document.getElementById('result-overlay-text');
    if (!box || !text) {
        resolve();
        return;
    }

    text.textContent = hit ? 'HIT' : 'NO HIT';
    box.classList.remove('hit', 'miss', 'show');
    box.classList.add(hit ? 'hit' : 'miss');

    void box.offsetWidth;
    box.classList.add('show');

    box.addEventListener('animationend', () => {
        box.classList.remove('show');
        resolve();
    }, { once: true });

}


document.addEventListener('click', (e) => {

    const group = e.target.closest('.radio-r-group');
    if (!group) return;

    setTimeout(() => {
        const checked = group.querySelector('input[type="radio"]:checked');
        if (!checked) return;
        realR = checked.value;
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        drawGraph();
    }, 0);
}, true);

document.addEventListener('DOMContentLoaded', () => {
    drawGraph();
})