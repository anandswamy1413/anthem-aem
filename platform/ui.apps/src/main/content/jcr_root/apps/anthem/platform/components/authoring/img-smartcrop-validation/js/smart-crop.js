(function(document, $, ns) {
	"use strict";

	$(document).on("click", ".cq-dialog-submit", function(e) {
        var img = $('.smart-crop-img').find('input').val();
        var mobileImg = $('.smart-crop-mobile-img').find('input').val();
        var smartCrop;
        if ($('.smart-crop')[0]) {
            smartCrop = $('.smart-crop')[0].checked;
        }
        var mobileSmartCrop;
        if ($('.smart-crop-mobile')[0]) {
            mobileSmartCrop = $('.smart-crop-mobile')[0].checked;
        }
        var items = $(".smart-crop-profile").find("coral-select-item");
        var cropProfile = "";
        for(var i = 0;i<items.length;i++){
            var item = items[i];
            if(item.selected){
				cropProfile = item.value;
            }
        }
        var cropItems = $(".smart-crop-mobile-profile").find("coral-select-item");
        var mobileCropProfile = "";
        for(var i = 0;i<cropItems.length;i++){
            var cropItem = cropItems[i];
            if(cropItem.selected){
				mobileCropProfile = cropItem.value;
            }
        }
        var ui = $(window).adaptTo("foundation-ui");
        var errorFound = false;
        var mobProfileErrorFound = false;

        $.ajax({
            type: 'GET',
            url: '/conf/anthem/servlet/smart-crop-validation',
            async: false,
            data: {
            	imgPath: img,
                mobileImgPath: mobileImg,
            	cropProfile: cropProfile,
                mobileCropProfile: mobileCropProfile
            },
            success: function(response) {
                var respObj = JSON.parse(response);
                if(smartCrop && !respObj.crop){
                    errorFound = true;
                }
                if(mobileImg && mobileSmartCrop && !respObj.mobileCrop){
                    mobProfileErrorFound = true;
                }
            }

        });
        
		if (errorFound || mobProfileErrorFound) {
            var errorMsg = "";
            if(errorFound){
				errorMsg = "Smart Crop feature is not enabled for the image selected.";
            }else if(mobProfileErrorFound){
				errorMsg = "Smart Crop feature is not enabled for the mobile image selected.";
            }
            ui.alert("Error", errorMsg, "error");
            return false;
        }

     });


})(document, Granite.$, Granite.author);