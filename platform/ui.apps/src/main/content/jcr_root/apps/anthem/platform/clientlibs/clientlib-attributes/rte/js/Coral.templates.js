window["Coral"]["templates"]["RichTextEditor"]["dlg_dynamicphone"] = (function anonymous(data_0
/*``*/) {
  var frag = document.createDocumentFragment();
  var data = data_0;
  var el37 = document.createElement("div");
  el37.className += " rte-dialog-columnContainer";
  var el38 = document.createTextNode("\n    ");
  el37.appendChild(el38);
  var el39 = document.createElement("div");
  el39.className += " rte-dialog-column rte-dialog-column--rightAligned";
  var el40 = document.createTextNode("\n        ");
  el39.appendChild(el40);
  var el41 = document.createElement("button","coral-button");
  el41.setAttribute("is", "coral-button");
  el41.setAttribute("icon", "close");
  el41.setAttribute("title", CUI["rte"]["Utils"]["i18n"]('dialog.cancel'));
  el41.setAttribute("aria-label", CUI["rte"]["Utils"]["i18n"]('dialog.cancel'));
  el41.setAttribute("iconsize", "S");
  el41.setAttribute("type", "button");
  el41.setAttribute("data-type", "cancel");
  el41.setAttribute("tabindex", "-1");
  el39.appendChild(el41);
  var el42 = document.createTextNode("\n        ");
  el39.appendChild(el42);
  var el43 = document.createElement("button","coral-button");
  el43.setAttribute("is", "coral-button");
  el43.setAttribute("icon", "check");
  el43.setAttribute("title", CUI["rte"]["Utils"]["i18n"]('dialog.apply'));
  el43.setAttribute("aria-label", CUI["rte"]["Utils"]["i18n"]('dialog.apply'));
  el43.setAttribute("iconsize", "S");
  el43.setAttribute("variant", "primary");
  el43.setAttribute("type", "button");
  el43.setAttribute("data-type", "apply");
  el43.setAttribute("tabindex", "-1");
  el39.appendChild(el43);
  var el44 = document.createTextNode("\n    ");
  el39.appendChild(el44);
  el37.appendChild(el39);
  var el45 = document.createTextNode("\n");
  el37.appendChild(el45);
  frag.appendChild(el37);
  var el46 = document.createTextNode("\n");
  frag.appendChild(el46);
  return frag;
});