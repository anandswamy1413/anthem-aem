(function($, ns, document) {
	"use strict";
	const OK = [{text:"Ok"}];
	let registry = $(window).adaptTo("foundation-registry");
	registry.register("foundation.form.response.ui.success", {
		name: "anthem.flush-akamai",
		handler: function(formEl, config, data, textStatus, xhr, resp) {
			let ui = $(window).adaptTo("foundation-ui");
			if(resp.httpStatus === 201){
				let msg = `<div><h3>Flush submitted</h3>
				<strong>Purge ID:</strong> ${resp.purgeId}</strong>
				</div>`;
				ui.prompt("Success", msg, 'success', OK);
			} else{
				ui.prompt("Error", 'Error flushing paths:', 'error', OK);
			}
		}
	});
}(window.jQuery, window.anthemTouchUI = window.anthemTouchUI || {}, document));
