async function validate() {
  return (
    validateNumber("game-id", "Please enter a Game Id.", false) &&
    validateText("api-key", "Please enter an API Key") &&
    validateSelect("type", "Please select a Game Type")
  );
}

async function addGame() {
    const caller = "add-game-button";
    addLoading(caller);

    if (await validate()) {
        const form = document.getElementById("add-game-form");
        const formData = Object.fromEntries(new FormData(form));
        const body = {
          gameId: formData["game-id"],
          apiKey: formData["api-key"].trim(),
          type: formData["type"],
        };

        const response = await submitRequest("/api/games", "POST", body);
        if (response !== null) {
          form.reset();
          window.location = `/games/${response.body.id}`;
        }
    }
    removeLoading(caller);
}
