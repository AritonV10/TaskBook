/**
 * 
 */
$(function() {
	/**
	 * Adding a subgroup
	 */
	TaskBook.global.event_t(".add_subgroup_button", "click", ($this) => {
		let url = "/v1/groups/" + g__ + "/subgroup"
		let this_ = $this;
		TaskBook.api.call_we($this, url, "POST", () => {
			return TaskBook.global.valuesAsJson(this_.parentNode, ["INPUT"]);
		}, ($payload) => {
			
			let p = {
					source: g__,
					action: "Add",
					message: "A new category has been added, " + $payload.subgroup,
					target: "SubGroup",
					payload: $payload
			}
			TaskBook.socket.send(JSON.stringify(p));
			document.querySelector(".categories_").innerHTML = document.querySelector(".subgroups_option").innerHTML;
			
		});
	})
	
	function init_options() {
		document.querySelector(".categories_").innerHTML = document.querySelector(".subgroups_option").innerHTML;
	};
	init_options();
	
	document.addEventListener("click", function(event){
		let target_ = event.target;
		if(target_.dataset.modal != null) {
			document.querySelector("." + target_.dataset.modal).style.display = "block";
		} else if (target_.classList.contains("modal_b")) {
			target_.style.display = "none";
		}
	})
	
	TaskBook.global.event_t(".g_invite_code", "click", ($this) => {
		TaskBook.api.call("PUT", "/v1/groups/" + g__ + "/invite", null, ($data) => {
			area = TaskBook.global.createChild("input", [], {});
			area.value = $data.code;
			document.body.append(area);
			area.select();
			try {
				document.execCommand("copy");
				document.body.removeChild(area);
				TaskBook.modal.info("The new code has been copied: " + $data.code);
			} catch (e) {
				console.log(e);
			}
		}, ($fail) => {}, () => {}, () => {});
	})
	
	TaskBook.global.event_t(".add_task_button", "click", ($this) => {
		let url = "/v1/groups/" + g__ + "/subgroup/task"
		let this_ = $this;
		TaskBook.api.call_we($this, url, "POST", () => {
			return TaskBook.global.valuesAsJson(this_.parentNode, ["INPUT", "TEXTAREA", "SELECT"]);
		}, ($payload) => {
			/*
			$payload['status'] = 'Not Taken';
			let p = {
					source: g__,
					action: "Add",
					message: "A new task has been added in " + $payload.category,
					target: "Task",
					payload: $payload
			}
			TaskBook.socket.send(JSON.stringify(p));
			*/
			
		});
	})
})
	