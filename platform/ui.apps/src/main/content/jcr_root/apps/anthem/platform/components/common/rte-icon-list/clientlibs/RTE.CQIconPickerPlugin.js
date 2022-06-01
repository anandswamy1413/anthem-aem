$.getScript('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.js', function() {
    (function($, CUI){
    var GROUP = "custom-lists",
        LINK_FEATURE = "iconPicker",
        TCP_DIALOG = "TouchUIIconPickerDialog",
        REQUESTER = "requester",
        PICKER_URL = "/mnt/overlay/anthem/platform/components/common/rte-icon-list/icon-popover/cq:dialog.html",
        ICON_PATH = "iconPath",
        ADD_SEPARATOR = "addSeparator",
        COLOR = "color";
    addPluginToDefaultUISettings();

    addDialogTemplate();

    var IconPickerDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: TCP_DIALOG,

        initialize: function(config) {
            this.exec = config.execute;
        },

        getDataType: function() {
            return TCP_DIALOG;
        }
    });

    var TouchUIIconPickerPlugin = new Class({
        toString: "TouchUIIconPickerPlugin",

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

            this.pickerUI = tbGenerator.createElement(LINK_FEATURE, this, false, { title: "Icon Picker" });
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.pickerUI, 10);

            var groupFeature = GROUP + "#" + LINK_FEATURE;
            tbGenerator.registerIcon(groupFeature, "taskList");
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

            var listTag = CUI.rte.Common.getTagInPath(context, startNode, "ul");


			var iconPath,addSeparator,color;
            if(listTag) {
				iconPath = $(listTag).attr("data-value");
				addSeparator = $(listTag).attr("data-separator");
                color = $(listTag).attr("data-color");
            }

            var tag = CUI.rte.Common.getTagInPath(context, startNode, "span"), plugin = this, dialog,
                dm = ek.getDialogManager(),
                $container = CUI.rte.UIUtils.getUIContainer($(context.root)),
                propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + LINK_FEATURE
                    }
                };

            if(this.IconPickerDialog){
                dialog = this.IconPickerDialog;
				// set the value on opening dialog again                
            }else{
                dialog = new IconPickerDialog();

                dialog.attach(propConfig, $container, this.editorKernel);

                dialog.$dialog.css("-webkit-transform", "scale(0.9)").css("-webkit-transform-origin", "0 0")
                    .css("-moz-transform", "scale(0.9)").css("-moz-transform-origin", "0px 0px");

                dialog.$dialog.find("iframe").attr("src", getPickerIFrameUrl(iconPath,addSeparator,color));

                this.IconPickerDialog = dialog;
            }

            dm.show(dialog);

            registerReceiveDataListener(receiveMessage);

            function isValidSelection(){
                var winSel = window.getSelection();
                return winSel && winSel.rangeCount == 1 && winSel.getRangeAt(0).toString().length > 0;
            }

            function getPickerIFrameUrl(iconPath,addSeparator,color){
                var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

                if(!_.isEmpty(iconPath)){
                    url = url + "&" + ICON_PATH + "=" + iconPath;
                }

                if(!_.isEmpty(addSeparator)){
                    url = url + "&" + ADD_SEPARATOR + "=" + addSeparator;
                }

                if(!_.isEmpty(addSeparator)){
                    url = url + "&" + COLOR + "=" + color;
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
                if (_.isEmpty(event.data) || (event.data && event.data.name == 'log')) {
                    return;
                }

                var message = JSON.parse(event.data),
                    action;

                if (!message || message.sender !== GROUP) {
                    return;
                }

                action = message.action;
                if (action === "submit") { 
                    if (!_.isEmpty(message.data)) {
						ek.relayCmd(id, message.data);
                       // ek.relayCmd('rteiconpickerhtml',newHtml);
                    }
                }else if(action === "remove"){
                    plugin.IconPickerDialog = null;
                }else if(action === "cancel"){
                    plugin.IconPickerDialog = null;
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

    CUI.rte.plugins.PluginRegistry.register(GROUP,TouchUIIconPickerPlugin);

    var TouchUIIconPickerCmd = new Class({
        toString: "TouchUIIconPickerCmd",

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
                context = execDef.editContext,
                message = execDef.value;

            var tags = common.getTagInPath(context, selection.startNode, 'ul');
            var spanToInsert = '<span style="{colorValue}" class="rte-icon {iconValue}" {ariaLabel} {role}></span>';
            var imgToInsert = '<img style="{colorValue}" class="rte-icon" src="{iconValue}"></img>';
            var htmlToInsert = "";
            var removeIcon = false;
            var colorValue = "";
            var ariaLabelAttr = "";
            var roleAttr = "";
            if(message.color){
                colorValue = "color:"+message.color;
            }	

            if(message.ariaLabel) {
				ariaLabelAttr = "aria-label=" + '"' + message.ariaLabel + '"';
                roleAttr = "role=" + '"img"'; 
            }

            if(message.iconPath && message.iconPath.indexOf("none") != -1){
				removeIcon = true;
            }if(message.iconPath && message.iconPath.indexOf("/content/dam") != -1){
				htmlToInsert = imgToInsert.replace("{iconValue}",message.iconPath);
                htmlToInsert = htmlToInsert.replace("{colorValue}",colorValue);
                htmlToInsert = htmlToInsert.replace("{ariaLabel}",ariaLabelAttr);
                htmlToInsert = htmlToInsert.replace("{role}",roleAttr);
            } else if(message.iconPath) {
				htmlToInsert = spanToInsert.replace("{iconValue}",message.iconPath);
                htmlToInsert = htmlToInsert.replace("{colorValue}",colorValue);
                htmlToInsert = htmlToInsert.replace("{ariaLabel}",ariaLabelAttr);
                htmlToInsert = htmlToInsert.replace("{role}",roleAttr);
            }

            if (tags != null ) {
                if(!removeIcon) {
                    $(tags).removeClass('icon-list').addClass('icon-list');
                    if(message.addSeparator){
						$(tags).removeClass('list-separator').addClass('list-separator');
                        $(tags).attr("data-separator",message.addSeparator);
                    } else {
						$(tags).removeClass('list-separator');
                        $(tags).attr("data-separator","");
                    }
                    if(message.color){
                        $(tags).attr("data-color",message.color);
                    } else {
                        $(tags).attr("data-color","");
                    }
                    $(tags).attr("data-value",message.iconPath);
                    $(tags).children('li').each(function(index, element) {
                        $(this).children('.rte-icon').remove();
                        $(this).prepend(htmlToInsert);
                    });
                } else {
                    $(tags).removeClass('icon-list');
                    $(tags).removeClass('list-separator');
                    $(tags).attr("data-value","");
                    $(tags).attr("data-separator","");
                    $(tags).attr("data-color","");
					$(tags).children('li').each(function(index, element) {
                        $(this).children('.rte-icon').remove();
                    });
                }
                //nodeList.removeNodesByTag(execDef.editContext, 'span', undefined, true);
               //nodeList.removeNodesByTag(execDef.editContext, 'a', undefined, true);
               // nodeList.commonAncestor = nodeList.nodes[0].dom.parentElement;
            } else {
                 try {
				// This is just icon insert
                var tempDiv = context.doc.createElement('div');
       			 tempDiv.innerHTML = htmlToInsert + "&nbsp;";
                var range = context.doc.createRange();
                var startContainer = getRangeContainer(execDef.selection.startNode);
      			var endContainer = getRangeContainer(execDef.selection.endNode);

      			range.setStart(execDef.selection.startNode, execDef.selection.startOffset);
                if (execDef.selection.endNode && execDef.selection.endOffset) {
                    range.setEnd(endContainer, execDef.selection.endOffset);
                } else {
                    range.setEnd(startContainer, execDef.selection.startOffset);
                }
                var iconFrag = context.doc.createDocumentFragment();
                var firstNode, lastNode;
                while ((firstNode = tempDiv.firstChild)) {
                  lastNode = iconFrag.appendChild(firstNode);
                }
                //range.deleteContents();
                // make sure the range still starts where the original selection started
                //range.setStart(startContainer, execDef.selection.startOffset);
                range.insertNode(iconFrag);
                //range.setStartAfter(execDef.selection.endNode);
            	}catch (e) {

                }
			}
            function isNodeHelperLineBreak(node) {
                return !!(node && CUI.rte.Common.isTag(node, 'br'));
              }

             function getRangeContainer(node) {
                return node && isNodeHelperLineBreak(node) ? node.parentNode : node;
              }

        }
    });

    CUI.rte.commands.CommandRegistry.register(LINK_FEATURE, TouchUIIconPickerCmd);

    function addPluginToDefaultUISettings(){
        var toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + LINK_FEATURE);

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + "#" + LINK_FEATURE);
    }

    function addDialogTemplate(){
        var url = PICKER_URL + "?" + REQUESTER + "=" + GROUP;

        var html = "<iframe width='400px' height='400px' frameBorder='0' src='" + url + "'></iframe>";

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
    var SENDER = "custom-lists",
        REQUESTER = "requester",
        ICON_PATH = "iconPath",
        ADD_SEPARATOR = "addSeparator",
        COLOR = "color",
        ARIALABEL="ariaLabel";

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

		var iconPath = queryParams[ICON_PATH];
        var addSeparator = queryParams[ADD_SEPARATOR];
        var color = queryParams[COLOR];
        var ariaLabel = queryParams[ARIALABEL];

        var iconPathField = $dialog.find("[name='./" + ICON_PATH + "']");
        var addSeparatorField = $dialog.find("[name='./" + ADD_SEPARATOR + "']");
        var colorField = $dialog.find("[name='./" + COLOR + "']");
        var ariaField = $dialog.find("[name='./" + ARIALABEL + "']");

        if(!_.isEmpty(iconPath)){
           	iconPathField[0].value = decodeURIComponent(iconPath);
        }

        if(!_.isEmpty(addSeparator)){
           	addSeparatorField[0].value = decodeURIComponent(addSeparator);
        }

        if(!_.isEmpty(color)){
           	colorField[0].value = decodeURIComponent(color);
        }

        if(!_.isEmpty(ariaLabel)){
            ariaField[0].value = decodeURIComponent(ariaLabel);
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

        var iconPathField = $dialog.find("[name='./" + ICON_PATH + "']");
		var addSeparatorField = $dialog.find("[name='./" + ADD_SEPARATOR + "']");
        var colorField = $dialog.find("[name='./" + COLOR + "']");
        var ariaField = $dialog.find("[name='./" + ARIALABEL + "']");

        message.data[ICON_PATH] = iconPathField[0].value;
        message.data[ADD_SEPARATOR] = addSeparatorField[0].value;
        message.data[COLOR] = colorField[0].value;
        message.data[ARIALABEL] = ariaField[0].value;

        parent.postMessage(JSON.stringify(message), "*");
    }
})(jQuery, jQuery(document));
}); 