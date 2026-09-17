// SPRING BOOT BACKEND BASE URL (Running on port 8080)
const API_BASE = 'http://localhost:8080/api';

let currentAccount = null;
let currentAdmin = null;

// 1. TAB SWITCHER (Sign In vs Open Account vs Admin)
function switchAuthTab(mode) {
    const alertBox = document.getElementById('authAlert');
    alertBox.style.display = 'none';

    const tabLogin = document.getElementById('tabCustomerLogin');
    const tabSignup = document.getElementById('tabCustomerSignup');
    const tabAdmin = document.getElementById('tabAdminLogin');

    const formLogin = document.getElementById('customerLoginForm');
    const formSignup = document.getElementById('customerSignupForm');
    const formAdmin = document.getElementById('adminLoginForm');

    const heading = document.getElementById('authTitle');
    const subtext = document.getElementById('authSubtitle');

    // Reset tab active classes
    tabLogin.classList.remove('active');
    tabSignup.classList.remove('active');
    tabAdmin.classList.remove('active');

    // Hide all forms
    formLogin.style.display = 'none';
    formSignup.style.display = 'none';
    formAdmin.style.display = 'none';

    if (mode === 'CUSTOMER_LOGIN') {
        tabLogin.classList.add('active');
        formLogin.style.display = 'block';
        heading.innerText = 'Customer Sign In';
        subtext.innerText = 'Access your personal banking account';
    } else if (mode === 'CUSTOMER_SIGNUP') {
        tabSignup.classList.add('active');
        formSignup.style.display = 'block';
        heading.innerText = 'Open a New Bank Account';
        subtext.innerText = 'Fill in your details to create an instant account in MySQL';
    } else if (mode === 'ADMIN_LOGIN') {
        tabAdmin.classList.add('active');
        formAdmin.style.display = 'block';
        heading.innerText = 'Bank Auditor Login';
        subtext.innerText = 'Administrative oversight & audit console';
    }
}

// 2. CUSTOMER SIGNUP (POST /api/account/signup)
async function signupCustomer(e) {
    e.preventDefault();

    const accountNumber = parseInt(document.getElementById('signupAccNum').value);
    const holderName = document.getElementById('signupHolderName').value.trim();
    const password = document.getElementById('signupPassword').value;
    const balance = parseFloat(document.getElementById('signupInitialBalance').value);

    const payload = {
        accountNumber: accountNumber,
        holderName: holderName,
        password: password,
        balance: balance
    };

    try {
        const response = await fetch(`${API_BASE}/account/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            throw new Error(errorMsg || 'Account creation failed');
        }

        const newAccount = await response.json();

        // Switch to Customer Login tab with success message
        switchAuthTab('CUSTOMER_LOGIN');
        showFeedback('authAlert', `Account #${newAccount.accountNumber} created successfully! Please sign in.`, 'success');

        // Pre-fill the account number in login form
        document.getElementById('loginAccNum').value = newAccount.accountNumber;
        document.getElementById('loginPassword').value = '';

        // Clear signup fields
        document.getElementById('signupAccNum').value = '';
        document.getElementById('signupHolderName').value = '';
        document.getElementById('signupPassword').value = '';
        document.getElementById('signupInitialBalance').value = '';

    } catch (err) {
        showFeedback('authAlert', err.message, 'error');
    }
}

// 3. CUSTOMER LOGIN (POST /api/account/login)
async function loginCustomer(e) {
    e.preventDefault();

    const accountNumber = document.getElementById('loginAccNum').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const res = await fetch(`${API_BASE}/account/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ accountNumber: accountNumber, password: password })
        });

        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Invalid Account Number or Password');
        }

        currentAccount = await res.json();

        // Setup Header Navigation
        document.getElementById('userInfo').style.display = 'flex';
        document.getElementById('displayName').innerText = currentAccount.holderName;
        const badge = document.getElementById('roleBadge');
        badge.innerText = 'Customer';
        badge.className = 'role-pill role-user';

        // Switch to User Panel
        document.getElementById('authSection').classList.remove('active');
        document.getElementById('userSection').classList.add('active');

        renderUserDashboard();
    } catch (err) {
        showFeedback('authAlert', err.message, 'error');
    }
}

// 4. RENDER CUSTOMER DASHBOARD
function renderUserDashboard() {
    document.getElementById('balanceDisplay').innerText = currentAccount.balance.toLocaleString('en-IN', { minimumFractionDigits: 2 });
    document.getElementById('holderNameDisplay').innerText = currentAccount.holderName;
    document.getElementById('accNumberDisplay').innerText = currentAccount.accountNumber;
}

// 5. CUSTOMER DEPOSIT (PUT /api/account/{id}/deposit?amount=X)
async function handleDeposit(e) {
    e.preventDefault();
    const amount = parseFloat(document.getElementById('depositAmount').value);

    try {
        const res = await fetch(`${API_BASE}/account/${currentAccount.accountNumber}/deposit?amount=${amount}`, {
            method: 'PUT'
        });

        if (!res.ok) throw new Error(await res.text());
        currentAccount = await res.json();

        renderUserDashboard();
        showFeedback('userFeedback', `Deposited ₹${amount.toLocaleString('en-IN')} successfully!`, 'success');
        document.getElementById('depositAmount').value = '';
    } catch (err) {
        showFeedback('userFeedback', err.message, 'error');
    }
}

// 6. CUSTOMER WITHDRAW (PUT /api/account/{id}/withdraw?amount=X)
async function handleWithdraw(e) {
    e.preventDefault();
    const amount = parseFloat(document.getElementById('withdrawAmount').value);

    try {
        const res = await fetch(`${API_BASE}/account/${currentAccount.accountNumber}/withdraw?amount=${amount}`, {
            method: 'PUT'
        });

        if (!res.ok) throw new Error(await res.text());
        currentAccount = await res.json();

        renderUserDashboard();
        showFeedback('userFeedback', `Withdrew ₹${amount.toLocaleString('en-IN')} successfully!`, 'success');
        document.getElementById('withdrawAmount').value = '';
    } catch (err) {
        showFeedback('userFeedback', err.message, 'error');
    }
}

// 7. ADMIN LOGIN (POST /api/admin/login)
async function loginAdmin(e) {
    e.preventDefault();
    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    try {
        const res = await fetch(`${API_BASE}/admin/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: username, password: password })
        });

        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Invalid Admin Credentials');
        }

        currentAdmin = await res.json();

        // Setup Header Navigation
        document.getElementById('userInfo').style.display = 'flex';
        document.getElementById('displayName').innerText = currentAdmin.fullName;
        const badge = document.getElementById('roleBadge');
        badge.innerText = 'Admin';
        badge.className = 'role-pill role-admin';

        // Switch to Admin Panel
        document.getElementById('authSection').classList.remove('active');
        document.getElementById('adminSection').classList.add('active');

        loadAdminLedger();
    } catch (err) {
        showFeedback('authAlert', err.message, 'error');
    }
}

// 8. LOAD ADMIN LEDGER & TOTAL RESERVES
async function loadAdminLedger() {
    try {
        const [accountsRes, vaultRes] = await Promise.all([
            fetch(`${API_BASE}/admin/accounts`),
            fetch(`${API_BASE}/admin/vault`)
        ]);

        const accounts = await accountsRes.json();
        const vaultData = await vaultRes.json();

        document.getElementById('statAccountsCount').innerText = vaultData.totalAccounts;
        document.getElementById('statVaultSum').innerText = vaultData.totalVaultBalance.toLocaleString('en-IN', { minimumFractionDigits: 2 });

        const tbody = document.getElementById('accountsTableBody');
        if (accounts.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; color: var(--text-muted);">No accounts found in database.</td></tr>`;
            return;
        }

        tbody.innerHTML = accounts.map(acc => `
            <tr>
                <td><strong>#${acc.accountNumber}</strong></td>
                <td>${acc.holderName}</td>
                <td style="color: var(--success); font-weight: 600;">₹ ${acc.balance.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                <td>
                    <button class="btn-delete" onclick="deleteAccount(${acc.accountNumber})">
                        <i class="fa-solid fa-trash"></i> Close
                    </button>
                </td>
            </tr>
        `).join('');

    } catch (err) {
        showFeedback('adminFeedback', 'Failed to load accounts ledger: ' + err.message, 'error');
    }
}

// 9. ADMIN DELETE ACCOUNT (DELETE /api/admin/accounts/{id})
async function deleteAccount(accNumber) {
    if (!confirm(`Are you sure you want to close Account #${accNumber}? This permanently deletes the row from MySQL.`)) {
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/admin/accounts/${accNumber}`, { method: 'DELETE' });
        if (!res.ok) throw new Error(await res.text());

        showFeedback('adminFeedback', `Account #${accNumber} closed successfully!`, 'success');
        loadAdminLedger();
    } catch (err) {
        showFeedback('adminFeedback', err.message, 'error');
    }
}

// 10. UI NOTIFICATION HELPER
function showFeedback(id, msg, type) {
    const el = document.getElementById(id);
    el.innerText = msg;
    el.className = `alert ${type === 'success' ? 'alert-success' : 'alert-error'}`;
    el.style.display = 'block';
    setTimeout(() => {
        el.style.display = 'none';
    }, 4500);
}

// 11. LOGOUT
function handleLogout() {
    currentAccount = null;
    currentAdmin = null;

    document.getElementById('userInfo').style.display = 'none';
    document.getElementById('userSection').classList.remove('active');
    document.getElementById('adminSection').classList.remove('active');
    document.getElementById('authSection').classList.add('active');

    // Clear form fields
    document.getElementById('loginAccNum').value = '';
    document.getElementById('loginPassword').value = '';
    document.getElementById('adminUsername').value = '';
    document.getElementById('adminPassword').value = '';
    document.getElementById('depositAmount').value = '';
    document.getElementById('withdrawAmount').value = '';
}