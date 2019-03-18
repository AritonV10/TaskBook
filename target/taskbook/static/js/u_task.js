$(function() {
	TaskBook.global.event_t(".create_community_button_p", "click", ($this) => {
		TaskBook.api.call_we($this, "/v1/users/user/groups", "POST", () => {
			return TaskBook.global.inputsData($this.dataset.fetch);
		}, () => {});
	});
	TaskBook.global.event_t(".join_community_button_p", "click", ($this) => {
		TaskBook.api.call_we($this, "/v1/users/user/groups", "PUT", () => {
			return TaskBook.global.inputsData($this.dataset.fetch);
		}, () => {});
	})
	document.onclick = function($event) {
		let data_ = $event.target;
		if(data_.dataset != null && data_.dataset.redirect != null) {
			let u = data_.dataset.redirect;
			window.location = "http://127.0.0.1:8080/en/u/" + u;
		}
	}
})