 document.querySelector("form").addEventListener("submit", function (e) {
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;

    if (password !== confirmPassword) {
    e.preventDefault();
    const errorEl = document.getElementById("password-error");

    errorEl.textContent = "Passwords do not match!";
    errorEl.classList.remove("hidden");

    errorEl.scrollIntoView({ behavior: "smooth", block: "center" });
}
});

