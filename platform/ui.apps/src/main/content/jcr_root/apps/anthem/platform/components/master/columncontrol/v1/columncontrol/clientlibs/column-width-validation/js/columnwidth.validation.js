(function ($, $document, ns) {
    "use strict";
    
    $(document).on("click", ".cq-dialog-submit", function (e) {
        e.stopPropagation();
        e.preventDefault();

		var total = 0;
        var $form = $(this).closest("form.foundation-form"),
         	numOfColumns = $form.find("[name='./numOfColumns']").filter(':input').each(function(index, item){
   			total = total+Number(item.value);
});

        if(total > 12) {
                ns.ui.helpers.prompt({
                title: Granite.I18n.get("Invalid Input"),
                message: "Please select all values that add up to 100 percentage",
                actions: [{
                    id: "CANCEL",
                    text: "CANCEL",
                    className: "coral-Button"
                }],
            callback: function (actionId) {
                if (actionId === "CANCEL") {
                }
            }
        });
 
        }else{
                 $form.submit();
        }

    });
    
})($, $(document), Granite.author);