const yFeedback = document.getElementById('yFeedback');
(function() {
    document.addEventListener('input', e => {
        const el = e.target;
        if(!el.matches('[data-js-hook="num-only"]')) return;
        const before = el.value;
        let v = before.replace(/[^\d.,\-]/g, '');
        v = v.replace(/,/g, '.');
        v = v.replace(/(?!^)-/g, '');
        v = v.replace(/(\..*)\./g, '$1');
        v = v.replace(/^(-?)\./, '$1');
        if(v !== before) el.value = v;

        const tmp = el.value.trim();

        if (/^3\.0*[1-9]\d*$/.test(tmp) || /^-5\.0*[1-9]\d*$/.test(tmp)) {
            el.value = '';
        } else {
            validateY(tmp);
        }
    }, true);
})();


function validateY(tmp) {
    let checkY = false;
    const num = Number(tmp);
    if(tmp === '') {
        yFeedback.textContent = '';
        yFeedback.classList.remove('valid', 'invalid');
        return;
    }
    if(!isNaN(tmp) && num >= -5 && num <= 3) checkY = true;
    if(checkY) {
        yFeedback.textContent = 'Valid value ✔';
        yFeedback.classList.add('valid');
        yFeedback.classList.remove('invalid');
    } else {
        yFeedback.innerHTML = `Invalid value ✖`;
        yFeedback.classList.add('invalid');
        yFeedback.classList.remove('valid');
    }
}


