(function() {
    document.addEventListener('input', e => {
        const el = e.target;
        if(!el.matches('[data-js-hook="num-only"]')) return;
        const before = el.value;
        let v = before.replace(/[^\d.\-]/g, '');
        v = v.replace(/(?!^)-/g, '');
        v = v.replace(/(\..*)\./g, '$1');
        v = v.replace(/^(-?)\./, '$1');
        if(v !== before) el.value = v;
    }, true);
})();