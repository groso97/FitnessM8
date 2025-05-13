function handleCheckboxChange(checkbox, inputName) {
    const deleteBtn = document.getElementById('deleteSelectedBtn');
    const container = document.getElementById('checkboxContainer');

    const existing = container.querySelector('input[value="' + checkbox.value + '"]');
    if (existing) {
        container.removeChild(existing);
    }

    if (checkbox.checked) {
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = inputName;
        hiddenInput.value = checkbox.value;
        container.appendChild(hiddenInput);
    }

    deleteBtn.disabled = container.children.length === 0;
}