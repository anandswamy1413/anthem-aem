(function ($, ns, channel, window, undefined) {
    "use strict";

var HEADSELECTOR = {
	inheritCheckbox: ".inherited-header-footer",
    headerFooterPicker:".xf-header-footer-picker"
};

 /**
     * Called when the "inherit" checkbox is checked / unchecked.
     */
    channel.on("change", HEADSELECTOR.inheritCheckbox, function () {
       toggleComponentState(this);
    });
	
 window.onload = function() {

  var inheritCheckbox = $(HEADSELECTOR.inheritCheckbox)[0];
        if (inheritCheckbox) {
            toggleComponentState(inheritCheckbox);
        }
  };

$(document).on("foundation-contentloaded", function() {
        var inheritCheckbox = $(HEADSELECTOR.inheritCheckbox)[0];
        if (inheritCheckbox) {
            toggleComponentState(inheritCheckbox);
        }
    });


function toggleComponentState(ch) {
        if (ch.checked) {
			$(HEADSELECTOR.headerFooterPicker).find('input').attr('disabled','disabled');
            $(HEADSELECTOR.headerFooterPicker).find('button').attr('disabled','disabled');
        } else {
			$(HEADSELECTOR.headerFooterPicker).find('input').removeAttr('disabled','disabled');
            $(HEADSELECTOR.headerFooterPicker).find('button').removeAttr('disabled','disabled');
        }
    }
}(jQuery, Granite.author, jQuery(document), this));