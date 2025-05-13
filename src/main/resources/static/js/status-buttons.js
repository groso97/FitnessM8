function setStatusCreate(isCompleted) {
    document.getElementById("completedStatusCreate").value = isCompleted;

    const btnCompleted = document.getElementById("btnCompletedCreate");
    const btnInProgress = document.getElementById("btnInProgressCreate");

    if (isCompleted) {
        btnCompleted.classList.remove("bg-gray-700");
        btnCompleted.classList.add("bg-green-600");

        btnInProgress.classList.remove("bg-red-600");
        btnInProgress.classList.add("bg-gray-700");
    } else {
        btnInProgress.classList.remove("bg-gray-700");
        btnInProgress.classList.add("bg-red-600");

        btnCompleted.classList.remove("bg-green-600");
        btnCompleted.classList.add("bg-gray-700");
    }
}

function setStatusEdit(isCompleted) {
    document.getElementById("completedStatusEdit").value = isCompleted;

    const btnCompleted = document.getElementById("btnCompletedEdit");
    const btnInProgress = document.getElementById("btnInProgressEdit");

    if (isCompleted) {
        btnCompleted.classList.remove("bg-gray-700");
        btnCompleted.classList.add("bg-green-600");

        btnInProgress.classList.remove("bg-red-600");
        btnInProgress.classList.add("bg-gray-700");
    } else {
        btnInProgress.classList.remove("bg-gray-700");
        btnInProgress.classList.add("bg-red-600");

        btnCompleted.classList.remove("bg-green-600");
        btnCompleted.classList.add("bg-gray-700");
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const editInput = document.getElementById("completedStatusEdit");
    if (editInput) {
        setStatusEdit(editInput.value === "true");
    }
});
