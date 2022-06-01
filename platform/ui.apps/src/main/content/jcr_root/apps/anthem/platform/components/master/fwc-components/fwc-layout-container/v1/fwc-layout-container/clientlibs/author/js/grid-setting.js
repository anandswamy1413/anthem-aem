(function ($, ns, channel, window) {
  "use strict";
  var columnSelector;
  var NS_COLUMNSELECTOR = ".cmp-columnelector";
  var POPOVER_MIN_WIDTH = "4rem"; // looks better
  /**
   * @typedef {Object} ColumnSelectorConfig Represents a Column Selector configuration object
   * @property {Granite.author.Editable} editable The [Editable]{@link Granite.author.Editable} against which to create the column selector
   * @property {HTMLElement} target The target against which to attach the column selector UI
   */
  var ColumnSelector = ns.util.createClass({
    /**
       * The Column Selector configuration Object
       *
       * @member {ColumnSelectorConfig} ColumnSelector#_config
       */
    _config: {},
    /**
     * An Object that is used to cache the internal HTMLElements for this Column Selector
     *
     * @member {Object} ColumnSelector#_elements
     */
    _elements: {},
    constructor: function ColumnSelector(config) {
      var that = this;
      that._handleOutOfAreaClickBound = that._handleOutOfAreaClick.bind(that)
      that._handleButtonClickBound = that._handleButtonClick.bind(that)
      that._config = config;
      that._render();
      that._bindEvents();
    },
    // if the column selector is open
    isOpen: function () {
      return (this._elements.popover && this._elements.popover.open);
    },
    /**
     * Renders the Column Selector, adds its items and attaches it to the DOM
     *
     * @private
     */
    _render: function () {
      this._elements.popover = this._createPopover();
      this._elements.buttonList = this._createColumnButtonList();
      // append column list to the popover
      this._elements.popover.content.appendChild(this._elements.buttonList);
      // append popover to the content frame for re-use.
      ns.ContentFrame.scrollView[0].appendChild(this._elements.popover);
    },
    /**
     * Creates a simple [Coral.Popover]{@link Coral.Popover}
     *
     * @private
     * @returns {Coral.Popover} a simple coral popover.
     */
    _createPopover: function () {
      var that = this;
      var popover = new Coral.Popover().set({
        alignAt: Coral.Overlay.align.LEFT_BOTTOM,
        alignMy: Coral.Overlay.align.LEFT_TOP,
        target: that._config.target,
        interaction: Coral.Popover.interaction.OFF,
        open: true
      });
      // this is 9.375rem by default.. didnt want to css it
      popover.style.minWidth = POPOVER_MIN_WIDTH;
      return popover;
    },
    /**
     * Creates the [Coral.ButtonList]{@link Coral.ButtonList}; 12 buttons for 12 columns)
     *
     * @private
     * @returns {Coral.ButtonList}  List of buttons of all 12 columns.
     */
    _createColumnButtonList: function() {
      var buttonList = new Coral.ButtonList();

      // columns 1...12
        [{"value":12, "label" : "Reset Column 12"}, {"value":6, "label" : "Reset Column 6"}]
        .map(function (n) {
          var btnItem = new Coral.ButtonList.Item()
            .set({
              innerHTML: n.label,
              value: n.value
            })
          btnItem.style.textAlign = "center"
          btnItem.style.width = "100%";
          return btnItem;
        })
        .forEach(function (listItem) {
          return buttonList.items.add(listItem)
        });
      return buttonList;
    },
    /**
     * Binds interaction events
     *
     * @private
     */
    _bindEvents: function () {
      var that = this;
      // escape key
      $(document).off("keyup" + NS_COLUMNSELECTOR).on("keyup" + NS_COLUMNSELECTOR, function (event) {
        if (event.keyCode === 27) {
          that._finish();
        }
      });
      // out of area clicks
      document.removeEventListener("click", that._handleOutOfAreaClickBound);
      document.addEventListener("click", that._handleOutOfAreaClickBound, true);
      // click handlers for each button
      this._elements.buttonList.items.getAll()
        .forEach(function (buttonItem) {
          buttonItem.addEventListener("click", that._handleButtonClickBound, true);
        });
      // reposition the popover with overlay change,
      // as the editable toolbar can jump following navigation to a panel
      channel.off("cq-overlays-repositioned" + NS_COLUMNSELECTOR).on("cq-overlays-repositioned" + NS_COLUMNSELECTOR, function () {
        if (that._elements.popover) {
          that._elements.popover.reposition();
        }
      });
    },
    /**
     * Handles click on a clolumn button.
     * @param {Event} e the button click event.
     */
    _handleButtonClick: function (e) {
      var that = this;
      var breakpoint = ns.responsive.getCurrentBreakpoint();
      // setBreakpointConfig is not documented in the following url, but works nonetheless:
      // https://helpx.adobe.com/experience-manager/6-4/sites/developing/using/reference-materials/jsdoc/ui-touch/editor-core/index.html
        ns.responsive.persistence.setBreakpointConfig(that._config.editable, breakpoint, { width: e.target.value, offset: 0 })
        .done(function () {
          // refresh after making the change.
          ns.edit.EditableActions.REFRESH.execute(that._config.editable.getParent()).done(function () {
            that._config.editable.overlay.setSelected(false);
          });
        });
      that._finish();
    },
    /**
     * Handles clicks outside of the Column Selector popover
     *
     * @private
     * @param {Event} event The click event
     */
    _handleOutOfAreaClick: function (event) {
      var that = this;
      if (!$(event.target).closest(that._elements.popover).length) {
        that._finish();
      }
    },
    /**
     * Unbinds event handlers
     *
     * @private
     */
    _unbindEvents: function () {
      var that = this;
      $(document).off("click" + NS_COLUMNSELECTOR);
      // unbind click events here
      this._elements.buttonList.items.getAll().forEach(function (buttonItem) {
        buttonItem.removeEventListener("click", that._handleButtonClick);
      })
    },
    /**
     * Finishes column selection, hides it and cleans up.
     *
     * @private
     */
    _finish: function () {
      var that = this;
      if (that._elements.popover && that._elements.popover.parentNode) {
        that._elements.popover.open = false;
        that._unbindEvents();
        that._elements.popover.parentNode.removeChild(that._elements.popover);
      }
    }
  });
  /**
   * Toolbar action that works on responsivegrid component to allow authors
   * to chose the grid column width/count
   */
  var columnSelect = new ns.ui.ToolbarAction({
    name: "COLUMN_SELECT",
    text: Granite.I18n.get("Reset Columns"),
    icon: "tableEdit",
    execute: function (editable, param, target) {
      if (!columnSelector || !columnSelector.isOpen()) {
        columnSelector = new ColumnSelector({
          "editable": editable,
          "target": target[0]
        });
      }
      // do not close the toolbar
      return false;
    },
    // make sure this is responsivegrid and that current component does not have live relationship.
    condition: function (editable) {
      if (editable.type !== "anthem/www-sites/components/content/container") {
        return false;
      }
      if (editable.config[MSM.MSMCommons.Constants.PROP_LIVE_RELATIONSHIP]) {
        return MSM.MSMCommons.isInheritanceCancelled(editable) || MSM.MSMCommons.isManuallyCreated(editable);
      } else {
        return true
      }
    },
    isNonMulti: true
  });
  channel.on("cq-layer-activated", function (event) {
    if (event.layer === "Edit") {
      ns.EditorFrame.editableToolbar.registerAction("COLUMN_SELECT", columnSelect);
    }
  });
}(jQuery, Granite.author, jQuery(document), this));
