/**
 * @class CUI.rte.commands.RTEInsertHtml
 * @extends CUI.rte.commands.Command
 * @private
 */
/* global Class: true */
/* jshint strict: false */
(function (CUI) {
  //'use strict';

  var INSERTION_ERROR_MESSAGE = 'An error occurred while inserting HTML';

  var com = CUI.rte.Common;

  /**
   * Is the provided node a helper line break
   *
   * @param {HTMLElement} node    - node to be evaluated
   * @returns {boolean}
   */
  function isNodeHelperLineBreak(node) {
   // return !!(node && com.isTag(node, 'br') && com.isAttribDefined(node, com.BR_TEMP_ATTRIB));
    return !!(node && com.isTag(node, 'br'));
  }

  /**
   * Inserts the provided HTML value into the HTMLElement marked as contentEditable using the exectCommand API
   *
   * @param {CUI.rte.EditContext} context     - context of the edition
   * @param {string} htmlToInsert             - string representation of the HTML structure to be inserted
   * @returns {boolean}                       - the insertion is supported and enabled
   */
  function executeInsertCommand(context, htmlToInsert) {
    // Depending on the implementation, execCommand can either throw an exception or return a boolean if the command isn't supported or enabled
    // https://developer.mozilla.org/en-US/docs/Web/API/Document/execCommand
    try {
      return context.doc.execCommand && context.doc.execCommand('rteinserthtml', false, htmlToInsert);
    } catch (e) {
      throw new Error(INSERTION_ERROR_MESSAGE);
    }
  }

  /**
   * Returns the container element suitable for the range selection
   *
   * @param {HTMLElement} node  - Node from which to extract a container
   * @returns {*}
   */
  function getRangeContainer(node) {
    return node && isNodeHelperLineBreak(node) ? node.parentNode : node;
  }

  /**
   * Inserts the provided HTML value into the HTMLElement marked as contentEditable
   *
   * <p>the current function is used as a fallback when {@link document.execCommand} is not successfully inserting the provided HTML</p>
   *
   * @param {CUI.rte.EditContext} context     - context of the edition
   * @param {string} htmlToInsert             - string representation of the HTML structure to be inserted
   * @param {{}} execDef                      - execution configuration
   */
  function insert(context, htmlToInsert, execDef) {
    // even IE with W3C compliant selection model don't support the
    // "inserthtml" command, so use the old selection model + pasteHTML
    // to insert the HTML
    // Also, for Firefox (<=45) "inserthtml" does not work fine for '&nbsp;'. See CRTE-67
    // Browser command "inserthtml" has issues with Chrome as well. See CRTE-157. So, use our own implementation
    try {
      // create range based on the given selection
      var range = context.doc.createRange();
      var startContainer = getRangeContainer(execDef.selection.startNode);
      var endContainer = getRangeContainer(execDef.selection.endNode);

      range.setStart(execDef.selection.startNode, execDef.selection.startOffset);

      if (execDef.selection.endNode && execDef.selection.endOffset) {
        range.setEnd(endContainer, execDef.selection.endOffset);
      } else {
        range.setEnd(startContainer, execDef.selection.startOffset);
      }

      // range.pasteHTML was used to handle old IE implementations
      // Since, we now support >IE11, we should remove this in near future (6.5), but only after extensive testing on
      // all browsers including Chrome, Edge etc.
      if (range.pasteHTML) {
        range.pasteHTML(htmlToInsert);
      } else {
        var tempDiv = context.doc.createElement('div');
        tempDiv.innerHTML = htmlToInsert;
        console.log(tempDiv.firstChild);
        var textFrag = context.doc.createDocumentFragment();
        var firstNode, lastNode;
        while ((firstNode = tempDiv.firstChild)) {
          lastNode = textFrag.appendChild(firstNode);
        }
        range.deleteContents();
        // make sure the range still starts where the original selection started
        range.setStart(startContainer, execDef.selection.startOffset);
        range.insertNode(textFrag);
        range.setStartAfter(lastNode);
      }
    } catch (e) {
      if (!executeInsertCommand(context, htmlToInsert)) {
        throw new Error(INSERTION_ERROR_MESSAGE);
      }
    }
  }

  CUI.rte.commands.RTEIconPickerHtml = new Class({

    toString: 'rteiconpickerhtml',

    extend: CUI.rte.commands.Command,

    isCommand: function (cmdStr) {
      return (cmdStr.toLowerCase() === 'rteiconpickerhtml');
    },

    getProcessingOptions: function () {
      var cmd = CUI.rte.commands.Command;
      return cmd.PO_SELECTION;
    },

    execute: function (execDef) {
      var htmlToInsert = execDef.value;
      var context = execDef.editContext;
      if (htmlToInsert && (htmlToInsert.length > 0)) {
        insert(context, htmlToInsert, execDef);
      }
    },

    queryState: function (selectionDef, cmd) {
      return false;
    }

  });


// register command
  CUI.rte.commands.CommandRegistry.register('rteiconpickerhtml',
    CUI.rte.commands.RTEIconPickerHtml);

}(window.CUI));

