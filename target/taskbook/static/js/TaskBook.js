var TaskBook = TaskBook || {}
TaskBook.global = (function($) {
	setAttributes = (elem, map_) => {
		for(let index in map_) {
			elem.setAttribute(index, map_[index]);
		} 
	}

	setClasses = (elem, classes) => {
		for(let index in classes) {
			elem.classList.add(classes[index]);
		}
	}

	return {
		/**
		 * Add an event to a certain element
		 */
		event_: function(element, type, callback) {
			document.querySelector(element).addEventListener(type, callback);
		},
		/**
		 * Add an event to a certain element and return the reference to it
		 * 
		 */
		event_t: function(element, type, callback) {
			document.querySelector(element)
				.addEventListener(type, function() {
					$this = document.querySelector(element);
					return callback($this);
				})
		},
		/**
		 * Create an element
		 */
		createChild: (type, classes, attributes) => {
			let child = document.createElement(type);
			setAttributes(child, attributes);
			setClasses(child, classes);
			return child;
		}, 
		/**
		 * Iterate through an element and fetch the input values and return it as a map
		 */
		inputsData: function(target_) {
			let map_ = {};
			document.querySelector("." + target_).querySelectorAll("input")
				.forEach(elem => {
					map_[elem.name] = elem.value;
				})
			return map_;
		},
		
		/**
		 * Iterate through an element and fetch the values from specific types only
		 */
		valuesAsJson: function(parent, types) {
			let map_ = {};
			let children_ = parent.children;
			for(let child_ in children_) {
				for(let type_ in types) {
					let type = types[type_];
					if(type == children_[child_].tagName) {
						if(children_[child_].tagName == "SELECT") {
							let option = children_[child_];
							map_[children_[child_].name] = option.options[option.selectedIndex].text;
						} else {
							map_[children_[child_].name] = children_[child_].value;
						}
					}
				}
			}
			return map_;
		}
	}; 
})();

TaskBook.api = (function() {
	return {
		
		/**
		 * Fire a JQuery request
		 */
		call: function(method, url, payload, doneCallback, failCallback, beforeSendCallback, completeCallback) {
			$settings = { 
					method: method, 
					contentType: "application/json", 
					data: payload, url: url, 
					beforeSend: beforeSendCallback,
					complete: completeCallback};
			$.ajax($settings)
			.done(function(data) {
				doneCallback(data);
			}).fail(function(xhr) {
				failCallback(xhr);
			})
		},
		
		/**
		 * Fire a JQuery request
		 * If the request has errors, fetch the @{errors} and display them
		 * If success, then display the success message in the modal
		 */
		call_we: function (element, url, method, valuesCallback, wbSendOnSuccessCallback) {
			let payload = valuesCallback(); //TaskBook.global.inputsData(element.dataset.fetch);
			TaskBook.api.call(method, url, JSON.stringify(payload), (data) => {
				TaskBook.modal.info(data.message)
				wbSendOnSuccessCallback(payload);
				if(data.redirect != null) {
					setTimeout(function() {
						window.location = data.redirect;
					}, 2000);
				}
			}, (data) => {
				let errors_ = JSON.parse(data.responseText).errors,
					list = document.querySelector("." + element.dataset.err);
					list.innerHTML = "";
				for(let index in errors_) {
					let liNode = TaskBook.global.createChild("li", [], {});
					liNode.innerHTML = errors_[index];
					list.appendChild(liNode);
				}
			}, () => { document.querySelector(".j_l").classList.toggle("loading"); },
				() => { document.querySelector(".j_l").classList.toggle("loading")}
				);
		}
	}
})();

TaskBook.socket = (function() {
	var wb;
	return {
		connect: function(url, onMessageCallback) {
            wb = new WebSocket(url);
            wb.onopen = function() {
            	console.log("Connection with enstablished");
            }
            wb.onmessage = function(evt) {
            	onMessageCallback(evt);
            }
		},
		send: function(payload) {
			wb.send(payload);
		},
	}
})();

TaskBook.modal = (function() {
	var flag = false,
		modal_info = {
			settings: {
				class: "modal_info",
				child_animation: "up_fade"
			},
			tab: {
				title: document.title,
			},
			parent: null,
			intervalId: null,
			tab_notify: function (count_) {
				if(count_ != 0) {
					document.title = this.tab.title + "(" + (count_).toString() + ")";
				} else {
					document.title = this.tab.title
				}
				
			},
			append_message: function(text) {
				// the element to append to the list
				let element = TaskBook.global.createChild("li", [], {}),
				// a reference to this because it gets lost if we call a function that's outside of this scope
					_this = this;
				element.innerHTML = text;
				// if not true then it means the list hasn't been added to the dom so we do it
				// and set it to true in case another notification comes in
				if(!flag) {
					this.parent = TaskBook.global.createChild("ul", [this.settings.class], {});
					document.body.appendChild(this.parent);
					flag = true;
				}

				// append the new notification to the list and update the tab
				this.parent.appendChild(element);
				this.tab_notify(this.parent.children.length);

				// we use an interval in case the user is not on the tab when the notifications come
				// so they don't get removed from the DOM until the user is on the tab
				let intervalId = setInterval(function(){
					// checking every 2 seconds if ever is on the same tab
					// if it is then we proceed
					if(!document.hidden) {
						setTimeout(function() {
							
							// adding the fade animation to the element
							element.classList.add(_this.settings.child_animation);
							
							setTimeout(function() {
								// remove the oldest child from the list
								_this.parent.removeChild(element);
								_this.tab_notify(_this.parent.children.length);
								
								// if its true then it means there are no more notifications
								// thus we can remove the list from the DOM as well
								if(_this.parent.children.length === 0) {
									document.body.removeChild(_this.parent);
									flag = false;
								}
							}, 2000);
						}, 3000)

						// clear the interval if the user is on the same tab
						clearInterval(intervalId);
					}
				}, 2000)
			}
	};
	return {
		info: (text) => {
			modal_info.append_message(text);
		}
	}
})();