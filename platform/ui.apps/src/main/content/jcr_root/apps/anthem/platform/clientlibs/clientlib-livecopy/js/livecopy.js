(function($, $document) {
    "use strict";

    $document.on("dialog-ready", function(e) {
        	var $dialogForm = $document.find(".coral-Form");
        	if($dialogForm) {
                var $mixinName = $dialogForm.find('input[name="./jcr:mixinTypes"]');
                if($mixinName){
					$mixinName.each(function() {
                        $(this).val("cq:LiveSyncCancelled")
        			});
                }
        	}

    });

    $document.on("foundation-contentloaded", function(e) {
        	var $dialogForm = $document.find(".coral-Form");
        	if($dialogForm) {
                var $mixinName = $dialogForm.find('input[name="./jcr:mixinTypes"]');
                if($mixinName){
					$mixinName.each(function() {
                        $(this).val("cq:LiveSyncCancelled")
        			});
                }
        	}

    });


})($, $(document));