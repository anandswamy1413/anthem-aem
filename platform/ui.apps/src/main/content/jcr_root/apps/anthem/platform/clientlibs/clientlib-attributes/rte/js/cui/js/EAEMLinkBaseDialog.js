(function ($) {
    'use strict';

    CUI.rte.ui.cui.EAEMLinkBaseDialog = new Class({

        extend: CUI.rte.ui.cui.CQLinkBaseDialog,

        toString: 'EAEMLinkBaseDialog',

        rowsSelectorString: '.rte-dialog-columnContainer',

        dialogSelectorString: '[data-rte-dialog=link] coral-popover-content',

        iconpickerSelectorString: '[data-action="custom-lists#iconPicker"]',

        initialize: function(config) {
            this.superClass.initialize.call(this, config);
            this.$iconPicker = this.$container.find(this.iconpickerSelectorString);
            this.$dialogLinkRTE = this.$container.find(this.dialogSelectorString);
            this.$dialogRows = this.$dialogLinkRTE.find(this.rowsSelectorString);
            this.linkVariant = this.insertNewElement(this.linkVariantOptionHtml(), this.$dialogRows, 'rel',this.$iconPicker );
            this.linkType = this.insertNewElement(this.linkTypeOptionsHtml(), this.$dialogRows, 'data-analytics-additionalcontext',this.$iconPicker );
            this.linkName = this.insertNewElement(this.linkNameHtml(), this.$dialogRows, 'aria-label',this.$iconPicker );
            if(this.$iconPicker.length){
				this.insertIconHtml(this.$dialogRows, 'data-icons',this);
            }

        },

        insertNewElement : function (el, targetRows, type,iconPicker) {
            if(!iconPicker.length){
				$('<div class="rte-dialog-columnContainer"></div>').append($('<div class="rte-dialog-column"></div>').append(el)).insertBefore(targetRows.last());
				return this.getFieldByType(type)[0];
            } else {
				if(type == "aria-label") {
				var $lastRow = targetRows.last().prev();
				var htmlToInsert = $('<div class="rte-dialog-column"></div>').append(el);
                $lastRow.append(htmlToInsert);
            } else if (type == "rel") {
				var $lastRow = targetRows.last().prev();
				var htmlToInsert = $('<div class="rte-dialog-column"></div>').append(el);
                $lastRow.append(htmlToInsert);
            } else {
				$('<div class="rte-dialog-columnContainer"></div>').append($('<div class="rte-dialog-column"></div>').append(el)).insertBefore(targetRows.last());
            }
            return this.getFieldByType(type)[0];
            }

        },

        dlgFromModel: function() {	
            this.superClass.dlgFromModel.call(this);
            this.loadDropDownFieldData('rel');	
            this.loadDataForField('data-analytics-additionalcontext');
            this.loadDataForField('aria-label');
            this.loadDropDownFieldData('data-icons');
            this.loadDropDownFieldData('data-iconpos');
        },

        dlgToModel: function() {
            this.superClass.dlgToModel.call(this);
            this.saveFieldData('data-analytics-additionalcontext');
            this.saveFieldData('aria-label');
            this.saveDropDownFieldData('rel');
            this.saveDropDownFieldData('data-icons');
            this.saveDropDownFieldData('data-iconpos');
        },

        loadDropDownFieldData : function (type, attrName) {
            var select = this.getFieldByType(type);
            if (_.isEmpty(select)) {
                return;
            }
            var typeValue = this.objToEdit.attributes[type];
            $(select).children('coral-select-item').each(function () {
                       var optionValue = $(this).attr('value');

                       if(typeValue == optionValue){
                           $(this).attr("selected","selected");
                       }
                   });
        },

        setOptionSelected : function (collection ,value) {
            _.each(collection, function(item, index) {
                if (item.value === value) {
                    $(item).attr("selected","selected");
                    return;
                } else  {
					$(item).removeAttr("selected");
                }
            });            
        },

        linkVariantOptionHtml : function () {
        	return '<coral-select data-type="rel" placeholder="Rel Tag">' +
            	'<coral-select-item value=""> </coral-select-item>' +
                '<coral-select-item value="noopener noreferrer">noopener noreferrer</coral-select-item>' +
            '</coral-select>';
        },

		iconPositionHtml : function (iconPosValue) {
            if(iconPosValue == 'start') {
				return '<coral-select data-type="data-iconpos" placeholder="Icon Position">' +
                    '<coral-select-item value="start" selected>Start</coral-select-item>' +
                    '<coral-select-item value="end">End</coral-select-item>' + 
                '</coral-select>';
            } else if (iconPosValue == 'end') {
				return '<coral-select data-type="data-iconpos" placeholder="Icon Position">' +
                    '<coral-select-item value="start">Start</coral-select-item>' +
                    '<coral-select-item value="end" selected>End</coral-select-item>' + 
                '</coral-select>';
            } else {
				return '<coral-select data-type="data-iconpos" placeholder="Icon Position">' +
                    '<coral-select-item value="start">Start</coral-select-item>' +
                    '<coral-select-item value="end">End</coral-select-item>' + 
                '</coral-select>';
            }
        },

        insertIconHtml : function (targetRows, type, that) {
            $.ajax({
                url: "/mnt/overlay/anthem/platform/components/common/rte-icon-list/icon-popover/cq:dialog/content/items/column1/items/iconPath.html",
            }).done(function(data) {
                var iconsValue = that.objToEdit.attributes['data-icons'];
                var iconPosValue = that.objToEdit.attributes['data-iconpos'];
                var el = $(data).find('coral-select');
                el.attr("data-type",type);
                if(iconsValue){
                    el.children('coral-select-item').each(function () {
                        var optionValue = $(this).attr('value');

                        if(iconsValue == optionValue){
                            $(this).attr("selected","selected");
                        }
                    });
                }

				$('<div class="rte-dialog-columnContainer"></div>').append($('<div class="rte-dialog-column"></div>').append(el)).insertBefore(targetRows.last());
				that.icons = that.getFieldByType(type)[0];

                var $lastRow = targetRows.last().prev();
                var htmlToInsert = $('<div class="rte-dialog-column"></div>').append(that.iconPositionHtml(iconPosValue));
                $lastRow.append(htmlToInsert);
                that.iconPos = that.getFieldByType('data-iconpos')[0];
            });
        },


       loadDataForField : function (type) {
            var field = this.getFieldByType(type);
          if(field){
                var value = (this.objToEdit && this.objToEdit.attributes && this.objToEdit.attributes[type]
                                ? this.objToEdit.attributes[type] : null);
                field.val(value);
                this.objToEdit.attributes['data-analytics-name'] = window.getSelection().toString();
                var $dialogForms = $(this.$container.context).parents(".coral-Form");
               if($dialogForms){
					var $dialogContext=$dialogForms.context;
					var $cqNameFragment = $dialogForms.find('input[name="cqname"]');
                   if($dialogContext){
                        var $ownerDoc = $dialogContext.ownerDocument;
                        if($ownerDoc){
                            var $cqName = $ownerDoc.all["./cqname"];                           
                    		if($cqName){
							var cqValue = $cqName.value || 'rte'; 
                        	this.objToEdit.attributes['data-analytics-context'] = cqValue;
                    		} else {
                    			var cqNameFragment = 'rte';
                    			if($cqNameFragment) {
                    				cqNameFragment = $cqNameFragment.val() || 'rte'; //content fragment rte
                    			}
                    			this.objToEdit.attributes['data-analytics-context'] = cqNameFragment;
                    		}
                        }
                    }
                }
            }
        },

        saveDropDownFieldData : function (type, attrName) {
            var select = this.getFieldByType(type);
            if(_.isEmpty(select)){
                return;
            }

            var value = select.val();

            if (attrName) {
                this.objToEdit[attrName] = value;
            } else {
                this.objToEdit.attributes[type] = value;
            }
        },

        saveFieldData : function(type) {
            var field = this.getFieldByType(type);
            if (field){
                var value = field.val();
                 if (value != null) {
                    this.objToEdit.attributes[type] = value;
                }
            }
        },
		
        linkTypeOptionsHtml : function () {
            var html =  '<input class="coral3-Textfield" id="data-analytics-tag" data-type="data-analytics-additionalcontext" type="text" placeholder="data analytics tag" value="">';
            return html;
        },

        linkNameHtml : function () {
            var html =  '<input class="coral3-Textfield" id="aria-label-text" data-type="aria-label" type="text" placeholder="Aria Label Text" value="">';
            return html;
        }
    });


})(jQuery);
