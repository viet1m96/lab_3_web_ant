const canvas = document.getElementById('graph');
const ctx    = canvas.getContext('2d');
const logCurX = document.getElementById('screen-log-x');
const logCurY = document.getElementById('screen-log-y');
const EPS = 0.05;

function snap(v, eps = EPS) {
    const round = Math.round(v);
    const newVal = Math.abs(v - round) < eps ? round : v;
    return Number(newVal.toFixed(2));
}

let zoom   = 1;
let panX   = 0;
let panY   = 0;
let isDrag = false;
let lastX = 0;
let lastY = 0;
let scale = 0.2;
let pretendedR = 1;
let realR = null;
let RArea = null;
let RDraw;


function drawTriangle () {
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.lineTo(RArea,0);
    ctx.lineTo(0, -RArea / 2);
    ctx.closePath();
    ctx.fill();
}
function drawSquare ()   { ctx.fillRect(0, 0, -RArea / 2, RArea); }
function drawCircle () {
    ctx.beginPath();
    ctx.moveTo(0, 0);
    ctx.arc(0, 0, RArea, Math.PI / 2, 0, true);
    ctx.closePath();
    ctx.fill();
}


function drawNet() {

    const left   = (-panX - canvas.width  / 2) / zoom;
    const right  = ( canvas.width  / 2 - panX) / zoom;
    const top    = (canvas.height / 2 + panY) / zoom;
    const bottom = (panY - canvas.height / 2) / zoom;

    const startX = Math.floor(left  / RDraw) ;
    const endX   = Math.ceil (right / RDraw) ;
    const startY = Math.floor(bottom / RDraw) ;
    const endY   = Math.ceil (top   / RDraw) ;

    ctx.save();
    ctx.beginPath();
    ctx.setLineDash([5, 10]);
    ctx.lineWidth = 1 / zoom;


    for (let x = startX * RDraw; x <= endX * RDraw; x += RDraw) {
        if (Math.abs(x) < 1e-8) continue;
        ctx.moveTo(x, bottom);
        ctx.lineTo(x, top);
    }

    for (let y = startY * RDraw; y <= endY * RDraw; y += RDraw) {
        if (Math.abs(y) < 1e-8) continue;
        ctx.moveTo(left, y);
        ctx.lineTo(right, y);
    }

    ctx.stroke();

    ctx.setLineDash([]);
    ctx.beginPath();
    ctx.moveTo(left, 0);
    ctx.lineTo( right, 0);
    ctx.moveTo( 0, bottom); ctx.lineTo(0,  top);
    ctx.stroke();

    ctx.restore();

    const tickPx = 8;
    const fontPx = 15;

    ctx.save();
    ctx.lineWidth = 1 / zoom;
    ctx.strokeStyle = 'black';

    for (let i = startX; i <= endX; i++) {
        if (i === 0) continue;
        const x = i * RDraw;
        ctx.beginPath();
        ctx.moveTo(x, -tickPx / (2 * zoom));
        ctx.lineTo(x,  tickPx / (2 * zoom));
        ctx.stroke();
    }

    for (let j = startY; j <= endY; j++) {
        if (j === 0) continue;
        const y = j * RDraw;
        ctx.beginPath();
        ctx.moveTo(-tickPx / (2 * zoom), y);
        ctx.lineTo( tickPx / (2 * zoom), y);
        ctx.stroke();
    }
    ctx.restore();

    ctx.save();
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.fillStyle = 'red';
    ctx.font = `bold ${fontPx}px sans-serif`;
    const axisX_screen = panX + canvas.width / 2;
    const axisY_screen = panY + canvas.height / 2;

    ctx.textAlign = 'center';
    ctx.textBaseline = 'top';
    for (let i = startX; i <= endX; i++) {
        if (i === 0) continue;
        const x_world = i * RDraw;
        const x_screen = x_world * zoom + axisX_screen;
        ctx.fillText(String(i), x_screen, axisY_screen + tickPx + 2);
    }

    ctx.textAlign = 'right';
    ctx.textBaseline = 'middle';
    for (let j = startY; j <= endY; j++) {
        if (j === 0) continue;
        const y_world = j * RDraw;
        const y_screen = -y_world * zoom + axisY_screen;
        ctx.fillText(String(j), axisX_screen - tickPx - 4, y_screen);
    }

    ctx.restore();
}



function drawGraph() {
    canvas.width  = canvas.clientWidth;
    canvas.height = canvas.clientHeight;

    RDraw = Math.min(canvas.width, canvas.height) * scale;
    if(!isNaN(realR)) RArea = RDraw * (realR / pretendedR);
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.translate(panX + canvas.clientWidth / 2,
        panY + canvas.clientHeight / 2);
    ctx.scale(zoom, -zoom);

    ctx.fillStyle   = 'rgba(50, 150, 255, 0.5)';
    ctx.strokeStyle = '#000';
    ctx.lineWidth   = 1 / zoom;

    drawNet();
    if(realR !== null && RArea !== null) {
        drawCircle();
        drawTriangle();
        drawSquare();
        drawPointsOperation();
    }
}
window.drawGraph = drawGraph;
window.addEventListener('resize', drawGraph);

function curCoordinatesInLogicWorld(e) {
    const rect          = canvas.getBoundingClientRect();
    const mouseXCanvas  = e.clientX - rect.left;
    const mouseYCanvas  = e.clientY - rect.top;
    const centerXCanvas = canvas.width  / 2;
    const centerYCanvas = canvas.height / 2;

    let curX = (mouseXCanvas - (centerXCanvas + panX)) / zoom;
    let curY = (centerYCanvas + panY - mouseYCanvas)  / zoom;

    const err = RDraw;
    curX /= err;
    curY /= err;
    curX = snap(curX);
    curY = snap(curY);
    return {curX, curY};
}

function onWheel(e) {
    if (!e.ctrlKey) e.preventDefault();

    const scaleFactor = e.deltaY < 0 ? 1.1 : 1 / 1.1;
    const oldZoom     = zoom;
    zoom = Math.min(Math.max(zoom * scaleFactor, 0.2), 10);

    const rect          = canvas.getBoundingClientRect();
    const mouseXCanvas  = e.clientX - rect.left;
    const mouseYCanvas  = e.clientY - rect.top;
    const centerXCanvas = canvas.width  / 2;
    const centerYCanvas = canvas.height / 2;

    const curX = (mouseXCanvas - (centerXCanvas + panX)) / oldZoom;
    const curY = (centerYCanvas + panY - mouseYCanvas)  / oldZoom;

    panX = mouseXCanvas - (curX * zoom + centerXCanvas);
    panY = mouseYCanvas - (centerYCanvas - curY * zoom);

    drawGraph();
}

function onDrag(e) {
    if(!isDrag) return;
    panX += e.clientX - lastX;
    panY += e.clientY - lastY;
    lastX = e.clientX;
    lastY = e.clientY;

    drawGraph();
}

function displayCurCoordinates(e) {
    if(isDrag) return;
    if(realR === null) {
        logCurX.innerText = "R is null";
        logCurY.innerText = "R is null";
        return;
    }
    const curCoords = curCoordinatesInLogicWorld(e);
    logCurX.innerText = curCoords.curX.toString();
    logCurY.innerText = curCoords.curY.toString();
}



canvas.addEventListener('mousedown', e => {
    lastX = e.clientX;
    lastY = e.clientY;
    isDrag = true;
    canvas.style.cursor = 'grabbing';
});


canvas.addEventListener('mousemove', displayCurCoordinates);
canvas.addEventListener('mousemove', onDrag);

['mouseup', 'mouseleave'].forEach(e =>
    canvas.addEventListener(e, () => {
        isDrag = false;
        canvas.style.cursor = 'default';
    }));
canvas.addEventListener('dblclick', e => {
    if(realR === null) {
        Swal.fire({
            title: "Illegal click",
            html: 'You still do not pick R.<br>Please choose R on your left!',
            icon: "error",
            confirmButtonText: "Close"
        });
    } else {
        // const curCoords = curCoordinatesInLogicWorld(e);
        // const curX = curCoords.curX;
        // const curY = curCoords.curY;
        //
        // if(curX >= -5 && curX <= 5) checkX = true;
        // if(yList.includes(curY)) checkY = true;
        // if(checkX && checkY) {
        //     inputX.value = curX;
        //     ySelect.value = String(Number(curY));
        //     form.requestSubmit();
        // } else {
        //     Swal.fire({
        //         title: "Illegal coordinates",
        //         html: `You picked point (${curX}, ${curY}) <br> Please read the conditions on your left to pick again!`,
        //         icon: "error",
        //         confirmButtonText: "Close"
        //     });
        // }
    }
});
canvas.addEventListener('wheel', onWheel, { passive: false });
