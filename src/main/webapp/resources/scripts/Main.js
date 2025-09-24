
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