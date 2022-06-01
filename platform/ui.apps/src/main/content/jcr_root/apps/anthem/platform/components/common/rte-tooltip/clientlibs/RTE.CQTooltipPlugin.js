$.getScript('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.js', function() {
    (function($, CUI){ 
    var GROUP = "custom-tooltip",
        TOOLTIP_FEATURE = "tooltipRte",
        TCP_DIALOG = "TouchUITooltipDialog",
        REQUESTER = "requester",
        PICKER_URL = "/mnt/overlay/anthem/platform/components/common/rte-tooltip/tooltip-popover/cq:dialog.html",
        DESCRIPTION = "description",
        ENABLE_LIGHT_COLOR = "isLight",
        ENABLE_TOOLTIP_ICON = "enableTooltipIcon";
 
        addPluginToDefaultUISettings();

    	addDialogTemplate();
   
  	var TooltipDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: "TooltipDialog",

        initialize: function(config) {
            this.exec = config.execute;
        },

        getDataType: function() {
            return TCP_DIALOG;
        }
    });
 
    var TouchUITooltipPlugin = new Class({
        toString: "TouchUITooltipPlugin",

        extend: CUI.rte.plugins.Plugin,

        pickerUI: null,

        getFeatures: function() {
            return [ TOOLTIP_FEATURE ];
        },

        initializeUI: function(tbGenerator) {
            var plg = CUI.rte.plugins;

            if (!this.isFeatureEnabled(TOOLTIP_FEATURE)) {
                return;
            }

            this.pickerUI = tbGenerator.createElement(TOOLTIP_FEATURE, this, false, { title: "Add Tooltip" });
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.pickerUI, 10);

            var groupFeature = GROUP + "#" + TOOLTIP_FEATURE;
            tbGenerator.registerIcon(groupFeature, "more");
        },

        execute: function (id, value, envOptions) {
           	if(!isValidSelection()){
                return;
            }
        	
            var context = envOptions.editContext,
                selection = CUI.rte.Selection.createProcessingSelection(context),
                ek = this.editorKernel,
                startNode = selection.startNode;

            if ( (selection.startOffset === startNode.length) && (startNode != selection.endNode)) {
                startNode = startNode.nextSibling;
            }

			 var tag = CUI.rte.Common.getTagInPath(context, startNode, "fwc-tooltip"), plugin = this, dialog,
                 description =  $(tag).attr("message"), enableTooltipIcon =  $(tag).attr("icon"), isLight =  $(tag).attr("light") ; 

            var dm = ek.getDialogManager(),
                $container = CUI.rte.UIUtils.getUIContainer($(context.root)),
                propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + TOOLTIP_FEATURE
                    }
                };


                dialog = new TooltipDialog();

                dialog.attach(propConfig, $container, this.editorKernel);

                dialog.$dialog.css("-webkit-transform", "scale(0.9)").css("-webkit-transform-origin", "0 0")
                    .css("-moz-transform", "scale(0.9)").css("-moz-transform-origin", "0px 0px");

                dialog.$dialog.find("iframe").attr("src", getPickerIFrameUrl(description,enableTooltipIcon,isLight));

                this.TooltipDialog = dialog;


            dm.show(dialog);

            registerReceiveDataListener(receiveMessage);

            function isValidSelection(){
                var winSel = window.getSelection();
                return winSel && winSel.rangeCount == 1 && winSel.getRangeAt(0).toString().length > 0;
            }

            function getPickerIFrameUrl(description,enableTooltipIcon,isLight){
                var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

                
				if(!_.isEmpty(description)){
                    url = url + "&" + DESCRIPTION + "=" + description;
                }
				if(!_.isEmpty(enableTooltipIcon)){
                    url = url + "&" + ENABLE_TOOLTIP_ICON + "=" + enableTooltipIcon;
                }
                if(!_.isEmpty(isLight)){
                    url = url + "&" + ENABLE_LIGHT_COLOR + "=" + isLight;
                }

                return url;
            }

            function removeReceiveDataListener(handler) {
                if (window.removeEventListener) {
                    window.removeEventListener("message", handler);
                } else if (window.detachEvent) {
                    window.detachEvent("onmessage", handler);
                }
            }

            function registerReceiveDataListener(handler) {
                if (window.addEventListener) {
                    window.addEventListener("message", handler, false);
                } else if (window.attachEvent) {
                    window.attachEvent("onmessage", handler);
                }
            }

            function receiveMessage(event) {
                if (_.isEmpty(event.data)) {
                    return;
                }

                var message = JSON.parse(event.data),
                    action;

                if (!message || message.sender !== GROUP) {
                    return;
                }

                action = message.action;

				var selectedText= window.getSelection().toString();
				//var newHtml ="<fwc-tooltip term='" + selectedText + "' message='{description}' icon='{enableTooltipIcon}' data-content='" +  JSON.stringify(message.data) + "'><span class='hide-tooltip-term'>" + selectedText + "</span></fwc-tooltip>";

                
                if (action === "submit") {
                    if (!_.isEmpty(message.data)) {
                        var newHtml ='<fwc-tooltip term="' + selectedText + '" message="{description}"';
						
						if(!_.isEmpty(message.data.enableTooltipIcon) && message.data.enableTooltipIcon === 'true' ){ 
						
                   		 newHtml = newHtml + ' icon="{enableTooltipIcon}"' ;
                        }
                        if(!_.isEmpty(message.data.isLight) && message.data.isLight === 'true' ){ 
						
                   		 newHtml = newHtml + ' light={isLight}' ;
                		}		
						newHtml = newHtml + ' ><span class="hide-tooltip-term" style="color: #286CE2; border-bottom: 1px dotted #286CE2;">' + selectedText + '</span></fwc-tooltip>'; 

                        newHtml = newHtml.replace("{description}", message.data.description || '' )
                                         .replace("{enableTooltipIcon}", message.data.enableTooltipIcon || '')
                                         .replace("{isLight}", message.data.isLight || '');

						ek.relayCmd(id);
                        ek.relayCmd('rteinserthtml',newHtml);
                    }
                }else if(action === "remove"){
                    ek.relayCmd(id);
                }else if(action === "cancel"){
                    plugin.TooltipDialog = null;
                }

                dm.hide(dialog);

                removeReceiveDataListener(receiveMessage);
            }
        },

        //to mark the icon selected/deselected
        updateState: function(selDef) {
            var hasUC = this.editorKernel.queryState(TOOLTIP_FEATURE, selDef);

            if (this.pickerUI != null) {
                this.pickerUI.setSelected(hasUC);
            }
        }
    });

    CUI.rte.plugins.PluginRegistry.register(GROUP,TouchUITooltipPlugin);


	 var TouchUITooltipCmd = new Class({
        toString: "TouchUITooltipCmd",

        extend: CUI.rte.commands.Command,

        isCommand: function(cmdStr) {
            return (cmdStr.toLowerCase() == TOOLTIP_FEATURE);
        },

        getProcessingOptions: function() {
            var cmd = CUI.rte.commands.Command;
            return cmd.PO_SELECTION | cmd.PO_BOOKMARK | cmd.PO_NODELIST;
        },

        _getTagObject: function(color) {
            return {
                "tag": "fwc-tooltip"               
            };
        },

        execute: function (execDef) {
                selection = execDef.selection,
                nodeList = execDef.nodeList;

            if (!selection || !nodeList) {
                return;
            }

            var common = CUI.rte.Common,
                context = execDef.editContext;

            var tags = common.getTagInPath(context, selection.startNode, 'fwc-tooltip');

            if (tags != null) {
                nodeList.removeNodesByTag(execDef.editContext, 'fwc-tooltip', undefined, true);
                nodeList.removeNodesByTag(execDef.editContext, 'span', undefined, true);
                nodeList.commonAncestor = nodeList.nodes[0].dom.parentElement;
            }

        }
    });
    
	CUI.rte.commands.CommandRegistry.register(TOOLTIP_FEATURE, TouchUITooltipCmd);

    function addPluginToDefaultUISettings(){
        var toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + TOOLTIP_FEATURE);

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + TOOLTIP_FEATURE);
    }

    function addDialogTemplate(){
        var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

        var html = "<iframe width='400px' height='300px' frameBorder='0' src='" + url + "'></iframe>";

        if(_.isUndefined(CUI.rte.Templates)){
            CUI.rte.Templates = {};
        }

        if(_.isUndefined(CUI.rte.templates)){
            CUI.rte.templates = {};
        }

        CUI.rte.templates['dlg-' + TCP_DIALOG] = CUI.rte.Templates['dlg-' + TCP_DIALOG] = Handlebars.compile(html);
    }
}(jQuery, window.CUI, jQuery(document)));

(function($, $document){
    var SENDER = "custom-tooltip",
        REQUESTER = "requester",
        DESCRIPTION="description",
        ENABLE_LIGHT_COLOR="isLight",
		ENABLE_TOOLTIP_ICON = "enableTooltipIcon";

    if(queryParameters()[REQUESTER] !== SENDER ){
        return;
    }

    $(function(){
        _.defer(stylePopoverIframe);
    });

   function queryParameters() {
        var result = {}, param,
            params = document.location.search.split(/\?|\&/);

        params.forEach( function(it) {
            if (_.isEmpty(it)) {
                return;
            }

            param = it.split("=");
            result[param[0]] = param[1];
        });

        return result;
    }

    function stylePopoverIframe(){
        var queryParams = queryParameters(),
            $dialog = $("coral-dialog");
        if(_.isEmpty($dialog)){
            return;
        }

        $dialog.css("overflow", "hidden").css("background-color", "#fff");

        $dialog[0].open = true;
		
        var description=queryParams[DESCRIPTION],
            isLight =  queryParams[ENABLE_LIGHT_COLOR],
            enableTooltipIcon = queryParams[ENABLE_TOOLTIP_ICON];

        var descriptionField = $dialog.find("[name='./" + DESCRIPTION + "']"),
            isLightField = $dialog.find("[name='./" + ENABLE_LIGHT_COLOR + "']"),
            enableTooltipIconField = $dialog.find("[name='./" + ENABLE_TOOLTIP_ICON + "']");

        
		if(!_.isEmpty(description)){
           	descriptionField[0].value = decodeURIComponent(description);
        }
		if(!_.isEmpty(enableTooltipIcon)){
           	enableTooltipIconField[0].value = decodeURIComponent(enableTooltipIcon);
        }
        if(!_.isEmpty(isLight)){
            isLightField[0].value = decodeURIComponent(isLight);
     }

        adjustHeader($dialog);

    }

    function adjustHeader($dialog){
        var $header = $dialog.css("background-color", "#fff").find(".coral3-Dialog-header");

       // $header.find(".cq-dialog-submit").remove();

        $header.find(".cq-dialog-submit").click(function(event){
				event.preventDefault();
            	sendDataMessage();
            	//$dialog.remove();
        });

        $header.find(".cq-dialog-cancel").click(function(event){
            event.preventDefault();

            $dialog.remove();

            sendCancelMessage();
        });
    }

    function sendCancelMessage(){
        var message = {
            sender: SENDER,
            action: "cancel"
        };

        parent.postMessage(JSON.stringify(message), "*");
    }

    function sendRemoveMessage(){
        var message = {
            sender: SENDER,
            action: "remove"
        };

        parent.postMessage(JSON.stringify(message), "*");
    }

    function sendDataMessage(){
        var message = {
            sender: SENDER,
            action: "submit",
            data: {}
        }, $dialog;

        $dialog = $(".cq-dialog");

		var descriptionField = $dialog.find("[name='./" + DESCRIPTION + "']"),
            enableTooltipIconField = $dialog.find("[name='./" + ENABLE_TOOLTIP_ICON + "']"),
            isLightField = $dialog.find("[name='./" + ENABLE_LIGHT_COLOR + "']");
		

		message.data[DESCRIPTION] = descriptionField[0].value;
        message.data[ENABLE_TOOLTIP_ICON] = enableTooltipIconField[0].value;
        message.data[ENABLE_LIGHT_COLOR] = isLightField[0].value;

        parent.postMessage(JSON.stringify(message), "*");

		descriptionField[0].value = '';
        enableTooltipIconField[0].value = '';
        isLightField[0].value = '';

    }
})(jQuery, jQuery(document));
}); 