function validateField(name, errorText, type, allowZero = false) {
  const field = document.getElementById(name);
  const fieldError = document.getElementById(`${name}-error`);

  function setFieldError() {
    fieldError.textContent = errorText;
    field.classList.add("is-danger");
    field.focus();
  }

  let fieldValue;
  switch (type) {
    case "number":
      fieldValue = parseInt(field.value, 10);
      if ((fieldValue === 0 && !allowZero) || isNaN(fieldValue)) {
        setFieldError();
        return false;
      }
      break;
    default:
      fieldValue = field.value.trim();
      if (fieldValue === "") {
        setFieldError();
        return false;
      }
  }

  field.classList.remove("is-danger");
  fieldError.textContent = "";
  return true;
}

function validateText(name, errorText) {
  return validateField(name, errorText, "text");
}

function validateNumber(name, errorText, allowZero) {
  return validateField(name, errorText, "number", allowZero);
}

function validateSelect(name, errorText) {
  return validateField(name, errorText, "text");
}
