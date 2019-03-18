$(function() {
	TaskBook.global.event_t(".sign-up", "click", ($this) => {
		TaskBook.api.call_we($this, "/v1/users", "POST", () => {
			return TaskBook.global.inputsData($this.dataset.fetch);
		}, ($s) => {});
	})
	TaskBook.global.event_t(".login_box_button", "click", ($this) => {
		TaskBook.api.call_we($this, "/v1/users/auth", "POST", () => {
			return TaskBook.global.inputsData($this.dataset.fetch);
		}, ($s) => {});
	})
})