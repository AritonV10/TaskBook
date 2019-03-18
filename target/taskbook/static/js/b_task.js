const g__ = function() {
			let url = window.location.href;
			return url.substring(url.lastIndexOf("/") + 1, url.length);
		}();
$(function() {
	
	let flag_task = false,
		__mov = false,
		// the coordinates of the mouse and the target element
		x, y, elem_ = null, elem_clone = null,
		// if the mouse is no longer pressed
		isDown = false,
		colors = {"Easy" : "#4caf50", "Intermediate" : "#03a9f4", "Difficult" : "#ff5252"}
		el = {	
			task_drop: document.querySelector(".drag_n_drop_wrapper"), 
			taken_body: "taken_body", 
			user_tasks: document.querySelector(".task_list"), 
			g_tasks: document.querySelector(".dashboard_tasks_list")
		},
		// the currently selected option (subgroup)
		current_option = null;	
	
	
	/**
	 * Creating the task elements based on the subgroup's name
	 */
	function GETTasks(data, s_name) {
		let parent = document.querySelector("._tasks_list_");
		let element = TaskBook.global.createChild("li", [], {"data-cat": s_name});
		let list = TaskBook.global.createChild("ul", ["dashboard_tasks_list"], {});
		for(let i in data) {
			let obj_ = data[i];
			let list_child = TaskBook.global.createChild("li", [], {});
			let div_ = document.querySelector(".d_data").cloneNode(true);
			div_.children[0].style.background = colors[obj_.difficulty];
			div_.classList.remove("d_data");
			div_.setAttribute("data-entity", obj_.transientID);
			for(let key in obj_) {
				if(div_.querySelector("[data-" + key + "]") != undefined) {
					div_.querySelector("[data-" + key + "]").innerHTML = obj_[key];
				};
			}
			div_.style.display = "block";
			list_child.appendChild(div_);
			list.appendChild(list_child);
		}
		element.appendChild(list);
		parent.appendChild(element);
	}
	
	
	
	$("document").ready(function() {
		(function() {
			let option_element = document.querySelector(".subgroups_option");
			if(option_element.children.length >= 1) {
				let child_ = option_element.children[0];
				current_option = child_;
				let s_name = child_.dataset.sid;
				let url = "/v1/groups/" + g__ + "/" + s_name + "/tasks";
				TaskBook.api.call("GET", url, null, ($data) => {
					GETTasks($data, s_name);
				}, () => {}, () => {}, () => {});
			}
		})();
	})
	
	/**
	 * Copies the invitation code
	 */
	TaskBook.global.event_t(".dashboard_invite_code", "click", ($this) => {
		area = TaskBook.global.createChild("input", [], {});
		area.value = $this.dataset.code;
		document.body.append(area);
		area.select();
		try {
			document.execCommand("copy");
			document.body.removeChild(area);
			TaskBook.modal.info($this.dataset.code + " has been copied")
		} catch (e) {
			console.log(e);
		}
	});
		
	
	
	
	
	/**
	 * Handling the socket response message
	 */
	TaskBook.socket.connect("ws://" + window.location.host + "/taskbook_socket?g=" + g__, ($evt) => {
		let data_ = JSON.parse($evt.data);
		switch(data_.target) {
		case "SubGroup":
			TaskBook.modal.info(data_.message);
			let select_ = document.querySelector(".subgroups_option");
			let value;
			if(select_.children.length <= 0) {
				value = 1;
			} else {
				value = Number(select_.children[select_.children.length - 1].value) + 1;
			}
			let option_ = TaskBook.global.createChild("option", [], {
				"value": value,
				"data-sid": data_.payload.subgroup
			});
			option_.innerHTML = option_.dataset.sid;
			select_.appendChild(option_);
			if(current_option == null) {
				current_option = option_;
			}
			break;
		case "Task":
			switch(data_.action) {
				case "Add":
					let payload = JSON.parse(data_.payload);
					let element;
					let list;
					let exists = true;
					if(document.querySelector("[data-cat='" + payload.category + "']") == undefined) {
						element = TaskBook.global.createChild("li", [], {"data-cat": payload.category});
						list = TaskBook.global.createChild("ul", ["dashboard_tasks_list"], {});
						exists = false;
					} else {
						element = document.querySelector("[data-cat='" + payload.category + "']");
						list = element.querySelector(".dashboard_tasks_list");
					}
					let list_child = TaskBook.global.createChild("li", [], {});
					let div_ = document.querySelector(".d_data").cloneNode(true);
					div_.children[0].style.background = colors[payload.difficulty];
					div_.classList.remove("d_data");
					div_.setAttribute("data-entity", payload.transientID);
						for(let key in payload) {
							if(div_.querySelector("[data-" + key + "]") != undefined) {
								div_.querySelector("[data-" + key + "]").innerHTML = payload[key];
							};
						}
					div_.style.display = "block";
					list_child.appendChild(div_);
					list.appendChild(list_child);
					if(!exists) {
						element.appendChild(list);
						document.querySelector("._tasks_list_").appendChild(element);
					}
					TaskBook.modal.info(data_.message);
					break;
				case "Remove":
					let payload_ = data_.payload;
					let t_children = document.querySelector("._tasks_list_");
					let subgroup_ = payload_.from;
					visit(t_children, ($p) => { return $p.dataset.entity == payload_.id }, ($m) => {
						$m.parentNode.parentNode.removeChild($m.parentNode);
						return 1;
					});
					TaskBook.modal.info(data_.message);
					break;
				case "Return":
					let payload__ = data_.payload;
					let t_children_ = document.querySelector("._tasks_list_");
					let t = visit(t_children_, ($p) => { return $p.dataset.entity == payload__.id; }, ($m) => {
						return $m;
					});
					t.querySelector("[data-status]").innerHTML = payload__.status;
					break;
			}
		}
	})
	
	
	/**
	 * Change the list of tasks based on the subgroup selected
	 * If the list doesn't exist, then fetch it
	 * else just display it
	 */
	TaskBook.global.event_t(".subgroups_option", "change", ($this) => {
		let tasksList = document.querySelectorAll("[data-cat]");
		let element = $this.options[$this.selectedIndex];
		current_option = element;
		let element_sid = element.dataset.sid;
		if(document.querySelector("[data-cat='" + element_sid + "']") == undefined) {
			let url = "/v1/groups/" + g__ + "/" + element_sid + "/tasks";
			TaskBook.api.call("GET", url, null, ($data) => {
				GETTasks($data, element_sid);
			}, () => {}, () => {}, () => {});
		}
		if(tasksList.length >= 1) {
			for(let i = 0; i < tasksList.length; ++i) {
				if(tasksList[i].dataset != undefined) {
					let data_ = tasksList[i].dataset.cat;
					let elt = tasksList[i];
					if(element_sid == data_) {
						elt.style.display = "block";
					} else {
						elt.style.display = "none";
					}
				}
			}
		}
	});
	
	/**
	 * Sorting
	 */
	TaskBook.global.event_t(".sort_", "change", ($this) => {
		let selected_ = $this.options[$this.selectedIndex];
		let order = document.querySelector(".order");
		let t__p = document.querySelector("._tasks_list_");
		let t__ = t__p.children;
		// checking to see if there are > 1 elements
		// else there is no point sorting just 1 element
		if(t__p.querySelector("[data-cat='" + current_option.text + "']").children[0].children.length > 1) {
			for(let i = 0; i < t__.length; ++i) {
				if(t__[i].dataset != undefined && t__[i].dataset.cat == current_option.text) {
					t__p.removeChild(t__[i]);
				}
			}
			let url = 
				"/v1/groups/" + g__ + "/" + current_option.text + "/tasks?sort=" + selected_.text.toLowerCase() + "&order=" + order.options[order.selectedIndex].text.toLowerCase();
			TaskBook.api.call("GET", url, null, ($data) => {
				GETTasks($data, current_option.text);
			}, () => {}, () => {}, () => {});
		}
	})
				
	/**
	 * Moving the tasks
	 */
	// the reason It's mouseover It's because dynamically added elements aren't
	// recongnized by the DOM, so we have to see the target the mouse is on
	TaskBook.global.event_(".dashboard_taken_tasks", "mouseover", (event) => {
		
		// if flag_task is true then it means there exists @{_task_} a tag element
		if(flag_task && (event.target.classList.contains("dashboard_taken_tasks") || event.target.classList.contains("task_list"))) {

			// we take all the elements with the class name 'taken_body' and we iterate throught them
			// since we don't know in which @{taken_body} element exist the @{_task_} element
			Array.from(document.getElementsByClassName(el.taken_body)).forEach(child => {
				if(child.children.length == 1) {
					child.removeChild(child.children[0])
				}
			})
			// we set this to true so we know there are no more @{_task_} elements
			flag_task = false;
		} else if(event.target.dataset.id_ != null) {
			// we verify if the current element has already a @{_task_} element
			if(event.target.children.length === 1) {
				return;
			}
			Array.from(document.getElementsByClassName(el.taken_body)).forEach(child => {
				if(event.target.dataset.id_ !== child.dataset.id_){
					if(child.children.length === 1) {
						child.removeChild(child.children[0])
					}
				}	
			})
			// we append a task element to the current element and set @{flag_task} to true
			let cur_element = event.target, task_ = new task(cur_element.dataset.id_)
			cur_element.appendChild(task_.parent);
			flag_task = true;
		}
	})	

	// moving the tasks
	document.addEventListener('mousedown', function(e) {
	    // checking to see if the node has dataset (in case you hover over the scrollbar, since it doesn't have dataset)
	    if(e.target.parentNode.dataset != undefined) {
	    	if(e.target.parentNode.dataset.mov != null) {
	    		elem_ = e.target.parentNode;
	    		elem_.style.position = "absolute";
	    		elem_.style.zIndex = "100";
	    		
	    		// letting the @{mousemove} event know that we've clicked an element
	    		isDown = true;
	    		el.task_drop.style.display = "block";
		    	offset = [
		        	elem_.offsetLeft - e.clientX,
		        	elem_.offsetTop - e.clientY
		    	];
	    	}
	    }
		    
	}, true);

	/**
	 * We check where the elements position - if It's near the the spot that you can drop
	 * it to take it, then fire a POST
	 */
	document.addEventListener('mouseup', function() {
	    if(elem_ === null) {
	    	return;
	    }
		// set the flag to false so it stops moving the task element
	    isDown = false;

	    // it goes away 2 fast
	    setTimeout(() => { el.task_drop.style.display = "none"}, 200);

	    // take the position of the task element and the center of the drop box
	    let taken_pos = document.querySelector(".drag_n_drop").getBoundingClientRect(),
	    	p = elem_.getBoundingClientRect() || 0,
	    	left_ = p.left - taken_pos.left;
	    
	    // checking to see if the task element is close to the drop box element
	   	if(p.top < taken_pos.bottom && p.top <= (taken_pos.top - 20) && left_ >= 0 && left_ <= taken_pos.right) {
	    	// reference to the task header
	    	let tasks_header = elem_.children[0].cloneNode(true),
	    		li_ = document.createElement('li'),
	    		parent_node = TaskBook.global.createChild('div', ["task_body"], {"data-taken":"true"}),
	    		entity_id = elem_.dataset.entity;
	    	tasks_header.setAttribute("data-id_", entity_id);
	    	tasks_header.setAttribute("data-from", current_option.text);
	    	tasks_header.classList.add(el.taken_body)
	    	parent_node.appendChild(tasks_header);
	    	// append the new node to the users' list of tasks
	    	li_.append(parent_node);
	    	let _url_ = "/v1/groups/" + g__ + "/subgroups/tasks/" + entity_id;
	    	TaskBook.api.call("POST", _url_, null, ($resp) => {
    			TaskBook.modal.info($resp.message);
    			el.user_tasks.append(li_);
    	    	elem_.querySelector("[data-status]").innerHTML = "Taken";
    	    	elem_ = null;
    		}, ($xhr) => {
    			TaskBook.modal.info(JSON.parse($xhr.responseText).errors[0]);
    		}, () => {}, () => {})    	
	    }
	}, true);

	/**
	 * Update the task position while the MouseDown event is triggered
	 */
	document.addEventListener('mousemove', function(event) {
	    event.preventDefault();
	    
	    // if flag is true it means that we haven't let go of the click yet
	    // so we keep moving the target element to the mouse position
	    if (isDown) {
	        mousePosition = {
	            x : event.clientX,
	            y : event.clientY
	        };
	        elem_.style.left = (mousePosition.x + offset[0]) + 'px';
	        elem_.style.top  = (mousePosition.y + offset[1]) + 'px';
	    }
	}, true);
	
	document.onclick = function($event) {
		let data_ = $event.target;
		if(data_.dataset != undefined && data_.classList.contains("_task_finish")) {
			let target_ = visit(document.querySelector(".task_list"), ($p) => { return $p.dataset.id_ == data_.parentNode.getAttribute("data-_target") }, ($m) => {
				return $m;
			});
			let _url = "/v1/groups/" + g__ + "/subgroups/tasks/" + target_.getAttribute("data-id_");
			TaskBook.api
				.call("DELETE", _url, null, ($data) => {
					TaskBook.modal.info($data.message);
					let p = {
							source: g__,
							action: "Remove",
							message: "A user has finished a task from " + target_.dataset.from,
							target: "Task",
							payload: { "id": target_.dataset.id_,
										"from": target_.dataset.from
							}
					}
					TaskBook.socket.send(JSON.stringify(p));
					let __t_ = document.querySelector(".task_list");
					visit(__t_, ($p) => { 
						return $p.dataset.id_ == target_.dataset.id_;
					}, ($m) => {
						__t_.removeChild($m.parentNode.parentNode)
						return 1;
					});
				}, ($fail) => {
					TaskBook.modal.info(JSON.parse($fail.responseText).errors[0]);
				}, () => {}, () => {});
			
		} else if (data_.dataset != undefined && data_.classList.contains("_task_remove")) {
			let target_ = visit(document.querySelector(".task_list"), ($p) => { return $p.dataset.id_ == data_.parentNode.getAttribute("data-_target") }, ($m) => {
				return $m;
			});
			let _url = "/v1/groups/" + g__ + "/subgroups/tasks/" + target_.getAttribute("data-id_");
			TaskBook.api
				.call("PUT", _url, null, ($data) => {
					TaskBook.modal.info($data.message);
					let p = {
							source: g__,
							action: "Return",
							message: "A user has returned a task back to " + target_.getAttribute("data-from"),
							target: "Task",
							payload: { "id": target_.getAttribute("data-id_"),
										"status": "Not Taken"
							}
					}
					TaskBook.socket.send(JSON.stringify(p));
					let __t = document.querySelector(".task_list");
					visit(__t, ($p) => { 
						return $p.dataset.id_ == target_.dataset.id_;
					}, ($m) => {
						__t.removeChild($m.parentNode.parentNode)
						return 1;
					});
				}, ($fail) => {
					TaskBook.modal.info(JSON.parse($fail.responseText).errors[0]);
				}, () => {}, () => {});
		}
	}
})

function visit(node, predicate, callback) {
	let q = [];
	  q.push(node);
	  while(q.length != 0) {
	    let n = q[0];
	    q = q.splice(1);
	    let c = n.children;
	    for(let i = 0; i < c.length; ++i) {
	    	if(c[i] != null && predicate(c[i])) {
	      	return callback(c[i]);
	      } else {
	      	q.push(c[i]);
	      }
	    }
	  }
}

// @{_task_} element constructor
function task(id) {
	this.parent = TaskBook.global.createChild("div", ["_task_", "up"], {"data-_target":id})
	let secondChild = TaskBook.global.createChild("div", ["_task_finish"], {"aria-describedby":"atom-tooltip", "data-atom":"Finish Task", "atom-action":"down"});
	secondChild.appendChild(TaskBook.global.createChild("i", ["fas", "fa-check"], {}));
	let firstChild = TaskBook.global.createChild("div", ["_task_remove"], {"aria-describedby":"atom-tooltip", "data-atom":"Remove Task", "atom-action":"down"});
	firstChild.appendChild(TaskBook.global.createChild("i", ["fas", "fa-times"], {}));
	this.parent.append(secondChild);
	this.parent.append(firstChild);
}




