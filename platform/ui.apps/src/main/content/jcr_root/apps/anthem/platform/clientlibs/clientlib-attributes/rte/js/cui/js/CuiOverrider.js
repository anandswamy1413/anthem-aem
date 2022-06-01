(function ($) {
    "use strict";

    var TYPES = {
        "rtelinkdialog": CUI.rte.ui.cui.EAEMLinkBaseDialog
    };

    CUI.rte.ui.cui.CuiDialogHelper = new Class({

            extend: CUI.rte.ui.cui.CuiDialogHelper,

            instantiateDialog: function(dialogConfig) {
                var Cls,
                    type = dialogConfig.type;
                if (!TYPES.hasOwnProperty(type)) {
                  throw new Error('Unknown dialog type: ' + type);
                }
                Cls = TYPES[type];

                // pre-render items if present
                if (dialogConfig.dialogItems) {
                    for (var i = 0; i < dialogConfig.dialogItems.length; i++) {
                        var item = dialogConfig.dialogItems[i];
                        if (item.item) {
                        var config = item.item;
                        var itemType = config.type;
                        item.rendered = Coral.templates.RichTextEditor['item_' + itemType](config);
                      }
                    }
                  }
                  var context = this.editorKernel.getEditContext();
                  var $editable = $(context.root);
                  var $container = CUI.rte.UIUtils.getUIContainer($editable);
                  var dlg = new Cls();
                  dlg.attach(dialogConfig, $container, this.editorKernel);
                  return dlg;
            }
        });
    /**
      * The dialog's type for indirect instantiation
      * @type {String}
      */
    CUI.rte.ui.DialogHelper.PHONE_DIALOG = 'dynamicphone';
    CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar.splice(5,0,'dynamicphone#addDynamicPhone');
    CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar.splice(13,0,'dynamicphone#addDynamicPhone');

})($);
