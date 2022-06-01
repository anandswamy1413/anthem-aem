   (function(document, $) {
	"use strict";

       $(document).on("foundation-contentloaded",function(e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideCtaConfig($(".showhide-cta", e.target));
        showHideDescConfig($(".showhide-desc", e.target));
        showHideSubheaderConfig($(".showhide-subheader", e.target));
        showHideImageConfig($(".showhide-image", e.target));
        showHideDynamicDataConfig($(".showhide-dynamicdata", e.target));

    });

       

    //To hide/show link list 
    $(document).on("change", ".showhide-cta", function(e) {
        showHideCtaConfig($(this));
    });

     $(document).on("change", ".showhide-desc", function(e) {
        showHideDescConfig($(this));
    });

     $(document).on("change", ".showhide-subheader", function(e) {
        showHideSubheaderConfig($(this));
    });

       $(document).on("change", ".showhide-image", function(e) {
        showHideImageConfig($(this));
    });

       $(document).on("change", ".showhide-dynamicdata", function(e) {
        showHideDynamicDataConfig($(this));
    });



    function showHideCtaConfig(el) {
        el.each(function(i, element) {
            if ($(element).prop('checked')) {

             // $(el).siblings(".showhide-heading-target").show();
                $(element).siblings(".showhide-cta-target").removeClass("hide");


            } else {

              // $(element).siblings(".showhide-heading-target").hide();
                $(element).siblings(".showhide-cta-target").addClass("hide");

            }
        })

    }

       function showHideDescConfig(el) {
        el.each(function(i, element) {
            if ($(element).prop('checked')) {

             // $(el).siblings(".showhide-heading-target").show();
                $(element).siblings(".showhide-desc-target").removeClass("hide");


            } else {

              // $(element).siblings(".showhide-heading-target").hide();
                $(element).siblings(".showhide-desc-target").addClass("hide");

            }
        })

    }

        function showHideSubheaderConfig(el) {
        el.each(function(i, element) {
            if ($(element).prop('checked')) {

             // $(el).siblings(".showhide-heading-target").show();
                $(element).siblings(".showhide-subheader-target").removeClass("hide");


            } else {

              // $(element).siblings(".showhide-heading-target").hide();
                $(element).siblings(".showhide-subheader-target").addClass("hide");

            }
        })

    }

       function showHideImageConfig(el) {
        el.each(function(i, element) {
            if ($(element).prop('checked')) {

             // $(el).siblings(".showhide-heading-target").show();
                $(element).siblings(".showhide-image-target").removeClass("hide");


            } else {

              // $(element).siblings(".showhide-heading-target").hide();
                $(element).siblings(".showhide-image-target").addClass("hide");

            }
        })

    }

       function showHideDynamicDataConfig(el) {
        el.each(function(i, element) {
            if ($(element).prop('checked')) {

                $(element).siblings(".showhide-dynamicdata-target").removeClass("hide");

            } else {

                $(element).siblings(".showhide-dynamicdata-target").addClass("hide");

            }
        })

    }

  })(document, Granite.$);
