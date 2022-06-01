$.getScript('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.js', function() {
    (function($, CUI){
    var GROUP = "custom-rte",
        LINK_FEATURE = "telLink",
        TCP_DIALOG = "TouchUITelLinkDialog",
        REQUESTER = "requester",
        PICKER_URL = "/mnt/overlay/anthem/platform/components/common/rte-tel-link/tel-popover/cq:dialog.html",
        HREF="href",
        LINK_TITLE="linkTitle",
        TTY_TITLE="ttyTitle",
        DATA_ANALYTICS="dataAnalytics";

    addPluginToDefaultUISettings();

    addDialogTemplate();

    var TelLinkDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: "TelLinkDialog",

        initialize: function(config) {
            this.exec = config.execute;
        },

        getDataType: function() {
            return TCP_DIALOG;
        }
    });

    var TouchUITelLinkPlugin = new Class({
        toString: "TouchUITelLinkPlugin",

        extend: CUI.rte.plugins.Plugin,

        pickerUI: null,

        getFeatures: function() {
            return [ LINK_FEATURE ];
        },

        initializeUI: function(tbGenerator) {
            var plg = CUI.rte.plugins;

            if (!this.isFeatureEnabled(LINK_FEATURE)) {
                return;
            }

            this.pickerUI = tbGenerator.createElement(LINK_FEATURE, this, false, { title: "Tel Link" });
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.pickerUI, 10);

            var groupFeature = GROUP + "#" + LINK_FEATURE;
            tbGenerator.registerIcon(groupFeature, "devicePhone");
        },

        execute: function (id, value, envOptions) {
           // if(!isValidSelection()){
             //   return;
            //}
        	
            var context = envOptions.editContext,
                selection = CUI.rte.Selection.createProcessingSelection(context),
                ek = this.editorKernel,
                startNode = selection.startNode;

            if ( (selection.startOffset === startNode.length) && (startNode != selection.endNode)) {
                startNode = startNode.nextSibling;
            }

            var anchorTag = CUI.rte.Common.getTagInPath(context, startNode, "a");


			var href,dataAnalytics,linkTitle,ttyTitle;
            if(anchorTag) {
				href = $(anchorTag).attr(HREF);
				dataAnalytics = $(anchorTag).data("analytics");
                linkTitle = $(anchorTag).text();               
				var ttyTag = $(anchorTag).find("span")[0];
                if(ttyTag) {
    				ttyTitle = $(ttyTag).text();
                    linkTitle = linkTitle.replace(ttyTitle, "");
                }
            }


            var tag = CUI.rte.Common.getTagInPath(context, startNode, "span"), plugin = this, dialog,
                dm = ek.getDialogManager(),
                $container = CUI.rte.UIUtils.getUIContainer($(context.root)),
                propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + LINK_FEATURE
                    }
                };

            if(this.TelLinkDialog){
                dialog = this.TelLinkDialog;
            }else{
                dialog = new TelLinkDialog();

                dialog.attach(propConfig, $container, this.editorKernel);

                dialog.$dialog.css("-webkit-transform", "scale(0.9)").css("-webkit-transform-origin", "0 0")
                    .css("-moz-transform", "scale(0.9)").css("-moz-transform-origin", "0px 0px");

                dialog.$dialog.find("iframe").attr("src", getPickerIFrameUrl(href,dataAnalytics,linkTitle,ttyTitle));

                this.TelLinkDialog = dialog;
            }

            dm.show(dialog);

            registerReceiveDataListener(receiveMessage);

            function isValidSelection(){
                var winSel = window.getSelection();
                return winSel && winSel.rangeCount == 1 && winSel.getRangeAt(0).toString().length > 0;
            }

            function getPickerIFrameUrl(href,dataAnalytics,linkTitle,ttyTitle){
                var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

                if(!_.isEmpty(href)){
                    url = url + "&" + HREF + "=" + href;
                }

                if(!_.isEmpty(linkTitle)){
                    url = url + "&" + LINK_TITLE + "=" + linkTitle;
                }

                if(!_.isEmpty(ttyTitle)){
                    url = url + "&" + TTY_TITLE + "=" + ttyTitle;
                }

                if(!_.isEmpty(dataAnalytics)){
                    url = url + "&" + DATA_ANALYTICS + "=" + dataAnalytics;
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
                var newHtml = '<span><a class="rte-tel" href="{href}" _rte_href="{href}" data-analytics="{dataAnalytics}">{linkTitle} <span class="rte-tty">{ttyTitle}</span></a></span>';
                if (action === "submit") {
                    if (!_.isEmpty(message.data)) {
                        newHtml = newHtml.replace("{href}",message.data.href)
                        			.replace("{href}", message.data.href || '')
                        			.replace("{dataAnalytics}", message.data.dataAnalytics || '' )
                        			.replace("{linkTitle}", message.data.linkTitle || '')
                        			.replace("{ttyTitle}",message.data.ttyTitle || '');
						ek.relayCmd(id);
                        ek.relayCmd('rteinserthtml',newHtml);
                    }
                }else if(action === "remove"){
                    ek.relayCmd(id);
                }else if(action === "cancel"){
                    plugin.TelLinkDialog = null;
                }

                dm.hide(dialog);

                removeReceiveDataListener(receiveMessage);
            }
        },

        //to mark the icon selected/deselected
        updateState: function(selDef) {
            var hasUC = this.editorKernel.queryState(LINK_FEATURE, selDef);

            if (this.pickerUI != null) {
                this.pickerUI.setSelected(hasUC);
            }
        }
    });

    CUI.rte.plugins.PluginRegistry.register(GROUP,TouchUITelLinkPlugin);

    var TouchUITelLinkCmd = new Class({
        toString: "TouchUITelLinkCmd",

        extend: CUI.rte.commands.Command,

        isCommand: function(cmdStr) {
            return (cmdStr.toLowerCase() == LINK_FEATURE);
        },

        getProcessingOptions: function() {
            var cmd = CUI.rte.commands.Command;
            return cmd.PO_SELECTION | cmd.PO_BOOKMARK | cmd.PO_NODELIST;
        },

        _getTagObject: function(color) {
            return {
                "tag": "span"               
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

            var tags = common.getTagInPath(context, selection.startNode, 'span');

            if (tags != null) {
                nodeList.removeNodesByTag(execDef.editContext, 'span', undefined, true);
                nodeList.removeNodesByTag(execDef.editContext, 'a', undefined, true);
                nodeList.commonAncestor = nodeList.nodes[0].dom.parentElement;
            }

        }
    });

    CUI.rte.commands.CommandRegistry.register(LINK_FEATURE, TouchUITelLinkCmd);

    function addPluginToDefaultUISettings(){
        var toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + LINK_FEATURE);

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + LINK_FEATURE);
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
}(jQuery, window.CUI,jQuery(document)));

(function($, $document){
    var SENDER = "custom-rte",
        REQUESTER = "requester",
        HREF="href",
        LINK_TITLE="linkTitle",
        TTY_TITLE="ttyTitle",
        DATA_ANALYTICS="dataAnalytics";

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

		var href = queryParams[HREF], 
            linkTitle=queryParams[LINK_TITLE],
            ttyTitle=queryParams[TTY_TITLE],
            dataAnalytics=queryParams[DATA_ANALYTICS];

        var hrefField = $dialog.find("[name='./" + HREF + "']"),
			linkTitleField = $dialog.find("[name='./" + LINK_TITLE + "']"),
        	ttyTitleField = $dialog.find("[name='./" + TTY_TITLE + "']"),
        	dataAnalyticsField = $dialog.find("[name='./" + DATA_ANALYTICS + "']");

        if(!_.isEmpty(href)){
           	hrefField[0].value = decodeURIComponent(href);
        }

        if(!_.isEmpty(linkTitle)){
           	linkTitleField[0].value = decodeURIComponent(linkTitle);
        }

        if(!_.isEmpty(ttyTitle)){
           	ttyTitleField[0].value = decodeURIComponent(ttyTitle);
        }

        if(!_.isEmpty(dataAnalytics)){
           	dataAnalyticsField[0].value = decodeURIComponent(dataAnalytics);
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

        var hrefField = $dialog.find("[name='./" + HREF + "']"),
			linkTitleField = $dialog.find("[name='./" + LINK_TITLE + "']"),
        	ttyTitleField = $dialog.find("[name='./" + TTY_TITLE + "']"),
        	dataAnalyticsField = $dialog.find("[name='./" + DATA_ANALYTICS + "']");


        message.data[HREF] = hrefField[0].value;
		message.data[LINK_TITLE] = linkTitleField[0].value;
        message.data[TTY_TITLE] = ttyTitleField[0].value;
        message.data[DATA_ANALYTICS] = dataAnalyticsField[0].value;

        parent.postMessage(JSON.stringify(message), "*");
        hrefField[0].value = '';
		linkTitleField[0].value = '';
		ttyTitleField[0].value = '';
		dataAnalyticsField[0].value = '';
    }
})(jQuery, jQuery(document));
}); 