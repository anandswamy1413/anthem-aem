(function ($, ns, channel, window, undefined) {
    "use strict";

var BRNDSELECTOR = {
	brandCheckbox: ".inherited-brand-clientLibs",
    brndClntLibPicker:".brand-client-libs-picker"
};

 /**
     * Called when the "inherit" checkbox is checked / unchecked.
     */
    channel.on("change", BRNDSELECTOR.brandCheckbox, function () {
       toggleBrand(this);
    });
	


$(document).on("foundation-contentloaded", function() {
        var brandCheckbox = $(BRNDSELECTOR.brandCheckbox)[0];
        if (brandCheckbox) {
            toggleBrand(brandCheckbox);
        }
    });


function toggleBrand(brnd) {
        if (brnd.checked) {
			$(BRNDSELECTOR.brndClntLibPicker).find('input').attr('disabled','disabled');
            $(BRNDSELECTOR.brndClntLibPicker).find('button').attr('disabled','disabled');
        } else {
			$(BRNDSELECTOR.brndClntLibPicker).find('input').removeAttr('disabled','disabled');
            $(BRNDSELECTOR.brndClntLibPicker).find('button').removeAttr('disabled','disabled');
        }
    }
}(jQuery, Granite.author, jQuery(document), this));