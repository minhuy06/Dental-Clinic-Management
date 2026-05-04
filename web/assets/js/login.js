function togglePass(id, btn) {
    var inp = document.getElementById(id);
    inp.type = inp.type === 'password' ? 'text' : 'password';
    btn.textContent = inp.type === 'password' ? '👁' : '🙈';
}

function handleLogin(e) {
    e.preventDefault();
    var ok = true;
    var acc = document.getElementById('loginAccount').value.trim();
    var pass = document.getElementById('loginPassword').value;
    if (!acc) {
        document.getElementById('accountGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('accountGroup').classList.remove('error');
    }
    if (!pass) {
        document.getElementById('passGroup').classList.add('error');
        ok = false;
    } else {
        document.getElementById('passGroup').classList.remove('error');
    }
    if (ok) {
        window.location.href = window.CONTEXT_PATH + '/index.jsp?loginSuccess=true&phone=' + encodeURIComponent(acc);
    }
    return false;
}

document.getElementById('loginAccount').addEventListener('input', function() {
    document.getElementById('accountGroup').classList.remove('error');
});
document.getElementById('loginPassword').addEventListener('input', function() {
    document.getElementById('passGroup').classList.remove('error');
});
