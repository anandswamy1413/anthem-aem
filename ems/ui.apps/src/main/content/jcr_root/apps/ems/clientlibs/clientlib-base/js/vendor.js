$(function () {
  let $selectBtn = $(".selectBtn-cntr");
  let $grpState = $("#grpState");
  let $formState = $("#formState");
  let $stateEmployees = $("#stateEmployees");
  let $formEmployees = $("#formEmployees");
  let $select_input_field = $(".select-input-field");
  let $submitBtn = $(".form-cnt-wrp  .submit-btn");
  let $backBtn = $(".form-succ-cntr .succ-cntr .back-btn");
  let $fields = $(".form-cnt-wrp  input");
  let $stateDefaultVal = $(".form-cnt-wrp  .selectBtn").attr("data-type");
  let lastActiveOption;
  let lastActiveOption1;
  var lastElement;
  var firstElement;
  var prevActive = "";
  let index = 0;
  let options = "";
  let targetedEle = "";
  var stateData = [];
  var $dynCntContainer = $(".dynamic-access-text .text-wrapper");
  var defaultAccessCnt = $(".dynamic-access-text .text-wrapper").attr(
    "data-content"
  );
  // State and No of employees analytic tag
  var formState = $(".form-cnt-wrp  .selectBtn").attr(
    "data-form-state-analytic-tag"
  );
  var formEmployee = $formEmployees.attr("data-form-employee-analytic-tag");
  var groupState = $selectBtn
    .find(".selectBtn")
    .attr("data-state-analytic-tag");
  var groupEmployee = $stateEmployees.attr("data-employee-analytic-tag");
  var minEmployeeLimit =
    parseInt($stateEmployees.attr("data-min-employees-limit")) || 2;
  //ajax to populate the state
  $.ajax({
    type: "GET",
    url: "/content/anthem/jcr:content/servlets/link/state-content",
    success: function (response) {
      response.sort((state1, state2) => {
        return state1.stateTitle > state2.stateTitle ? 1 : -1;
      });
      response.map((record) => stateData.push(record));
      var options = $.map(response, function (lcn) {
        return `<li class="option" tabindex = "0" data-type='${lcn.stateTitle}'>
                  <span class="motif-icon motif-checkmark checkmark"></span>
                    ${lcn.stateTitle}
                  </li>`;
      }).join("");
      $($select_input_field).html(options);
      $(document).on("click", ".option", function (event) {
        selectHandler(event);
      });
      $(document).on("keydown", ".option", function (event) {
        if (event.keyCode == 13) {
          selectHandler(event);
        }
      });
      prePopulateStateDropdown();
    },
  });

  //enable the submit button after filled all the fields
  $($fields, ".selectBtn-cntr").keyup(function () {
    enableSubmit();
  });
  function enableSubmit() {
    if (
      isEmpty($fields) &&
      $(".selectBtn").attr("data-type") !== $stateDefaultVal
    ) {
      $($submitBtn).removeClass("disabled");
      $($submitBtn).removeAttr("disabled");
    } else {
      $($submitBtn).addClass("disabled");
      $($submitBtn).attr("disabled", true);
    }
  }
  // checking the field is empty or not
  function isEmpty($fields) {
    return (
      $fields.filter(function () {
        return this.value === "" && this.hasAttribute("required");
      }).length == 0
    );
  }
  //adding click event to the select btn
  $($selectBtn).on("click", function (event) {
    $($selectBtn).siblings(".select-input-field").removeClass("toggle");
    event.stopPropagation();
    const next = $(event.target)
      .closest(".selectBtn-cntr")
      .siblings(".select-input-field");
    $(next).hasClass("toggle")
      ? $(next).removeClass("toggle")
      : $(next).addClass("toggle");
    index = 0;
  });
  //state dropdown select function
  function selectHandler(event) {
    event.preventDefault();
    if (lastActiveOption || lastActiveOption1) {
      $(lastActiveOption).removeClass("selected");
      $(lastActiveOption1).removeClass("selected");
    }
    $(event.target).closest(".select").css("border", "initial");
    $(event.target).closest(".select").next(".errMsg").hide();
    $(event.target).addClass("selected");
    $(event.target).parent().removeClass("toggle");
    $($formState).text($(event.target).text());
    $($formState).attr("data-type", $(event.target).attr("data-type"));
    $($grpState).text($(event.target).text());
    $($grpState).attr("data-type", $(event.target).attr("data-type"));

    if (
      $(event.target).parent().parent().find(".selectBtn").attr("id") ==
      "formState"
    ) {
      $($grpState)
        .parent()
        .siblings("ul")
        .find(".option")
        .filter(function () {
          if ($(this).attr("data-type") === $(event.target).attr("data-type")) {
            $(this).addClass("selected");
            lastActiveOption1 = $(this);
          }
        });
      changeDynamicContent(
        "#form",
        $(event.target).text(),
        $stateEmployees.val() || $formEmployees.val()
      );
      updatePageAnalytics("formState", $(event.target).text());
    } else {
      $($formState).parent().parent().css("border", "initial");
      $($formState).parent().parent().siblings(".required").hide();
      $($formState)
        .parent()
        .siblings("ul")
        .find(".option")
        .filter(function () {
          if ($(this).attr("data-type") === $(event.target).attr("data-type")) {
            $(this).addClass("selected");
            lastActiveOption1 = $(this);
          }
        });
      changeDynamicContent(
        "#group",
        $(event.target).text(),
        $stateEmployees.val() || $formEmployees.val()
      );
      updatePageAnalytics("state", $(event.target).text());
    }
    lastActiveOption = event.target;
    enableSubmit();
  }
  //close  and open the dropdown on clicking esc and enter

  $(".selectBtn-cntr ,.select-input-field").keydown(function (event) {
    firstElement = $(this).parent().find(".option").first();
    lastElement = $(this).parent().find(".option").last();
    if (options == "" && targetedEle == "") {
      options = $(this).siblings(".select-input-field").find(".option");
      targetedEle = $(this).siblings(".select-input-field");
    }
    switch (event.key) {
      case "Escape":
        $select_input_field.removeClass("toggle");
        break;
      case "Enter":
        $(this).siblings(".select-input-field").addClass("toggle");
        break;
      case "Tab":
        if ($(".select-input-field").hasClass("toggle")) {
          event.preventDefault();
        }
        break;
      case "ArrowDown":
        if ($(targetedEle).hasClass("toggle")) {
          if (index !== 0 && index < options.length) {
            prevActive = $(prevActive).next().focus();
          } else {
            index = 0;
            prevActive = $(firstElement[0]).focus();
          }
          index !== options.length ? index++ : index;
          event.preventDefault();
        }
        break;
      case "ArrowUp":
        if ($(targetedEle).hasClass("toggle")) {
          index == options.length ? (index = index - 1) : index;
          if (index > 0 && index - 1 < options.length) {
            prevActive = $(prevActive).prev().focus();
            index--;
          } else {
            prevActive = $(lastElement[0]).focus();
            index = options.length - 1;
          }
          event.preventDefault();
        }
        break;
      default:
        break;
    }
  });
  //close the dropdown on clicking outside
  $(document).click(function () {
    $($select_input_field).removeClass("toggle");
  });
  //changing state in one place reflect in another place
  $($stateEmployees).on("keyup", function (event) {
    if ($(this).val() >= minEmployeeLimit) {
      updatePageAnalytics("employee", $(this).val());
      changeDynamicContent(null, $("#grpState").text(), $(this).val());
      $formEmployees.val($(this).val());
      $formEmployees.siblings(".validate").hide();
      $formEmployees.siblings(".required").hide();
      $formEmployees.css("border", "initial");
      $(this).siblings(".validate").hide();
      $(event.target).css("border", "initial");
    } else {
      $(this).css("border", "1px solid #d20a3c");
      $($formEmployees).val("");
      $(this).siblings(".validate").show();
    }
    enableSubmit();
  });
  // Hide error message on focus out of employee text box if it's empty
  $($stateEmployees).on("focusout", function () {
    if ($(this).val().trim() == "") {
      $(this).siblings(".validate").hide();
      $(this).css("border", "initial");
    }
  });
  $($formEmployees).on("change keyup", function () {
    if ($(this).val() >= minEmployeeLimit) {
      updatePageAnalytics("formEmployee", $(this).val());
      changeDynamicContent(null, $("#formState").text(), $(this).val());
      $stateEmployees.val($(this).val());
      $stateEmployees.siblings(".validate").hide();
      $stateEmployees.siblings(".required").hide();
      $stateEmployees.css("border", "initial");
    } else {
      $(this).css("border", "1px solid #d20a3c");
      $(this).siblings(".validate").show();
      $($stateEmployees).val("");
      $stateEmployees.siblings(".validate").hide();
      $stateEmployees.siblings(".required").hide();
      $stateEmployees.css("border", "initial");
    }
  });
  // Function to change the access content when both state and number of employees fields are filled
  function changeDynamicContent(type, state, noOfEmployee) {
    if (state.trim() !== $stateDefaultVal && state.trim() != null) {
      var fetchData = stateData.filter(
        (data) => data.stateTitle === state.trim()
      );
      if (
        fetchData[0] &&
        fetchData[0].redirectURL !== "" &&
        (type == "#form" || type == "#group") &&
        window.location.href != fetchData[0].redirectURL
      ) {
        var link = document.createElement("a");
        link.href = fetchData[0].redirectURL + type;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } else if (noOfEmployee >= minEmployeeLimit) {
        $($dynCntContainer).find("p").remove();
        $($dynCntContainer).append(defaultAccessCnt);
        var $accessContRef = $($dynCntContainer).find("h2");
        if (fetchData[0] && Number(noOfEmployee) <= Number(fetchData[0].smallGroupMaxLimit)) 
        {
		  $($dynCntContainer).find("p").remove();
          $accessContRef ? $(fetchData[0].smallGroupAccessDescription).insertAfter($accessContRef)
            : $($dynCntContainer).append(fetchData[0].smallGroupAccessDescription);
        } else {
		  $($dynCntContainer).find("p").remove();
          $accessContRef ? $(fetchData[0] && fetchData[0].largeGroupAccessDescription).insertAfter($accessContRef)
            : $($dynCntContainer).append(fetchData[0].largeGroupAccessDescription);
        } 
      }
    }
  }
  // Function to update the page data properties
  function updatePageAnalytics(type, value) {
    try {
      if ($(".stateAndGroup .dev-env").length) {
        switch (type) {
          case "state":
            groupState && (digitalData.aem.page[groupState] = value.trim());
            break;
          case "employee":
            groupEmployee &&
              (digitalData.aem.page[groupEmployee] = value.trim());
            break;
          case "formEmployee":
            formEmployee && (digitalData.aem.page[formEmployee] = value.trim());
            break;
          case "formState":
            formState && (digitalData.aem.page[formState] = value.trim());
            break;
          default:
            break;
        }
      } else {
        switch (type) {
          case "state":
            groupState && (digitalData.page[groupState] = value.trim());
            break;
          case "employee":
            groupEmployee && (digitalData.page[groupEmployee] = value.trim());
            break;
          case "formEmployee":
            formEmployee && (digitalData.page[formEmployee] = value.trim());
            break;
          case "formState":
            formState && (digitalData.page[formState] = value.trim());
            break;
          default:
            break;
        }
      }
    } catch (e) {}
  }
  $(window).on("DOMNodeInserted", function () {
    if (!$(".vjs-poster .video-details").length > 0) {
      var title = $("#video-meta-data").attr("data-title");
      var description = $("#video-meta-data").attr("data-description");
      if (title || description) {
        let videoDetailsDOM = document.createElement("div");
        videoDetailsDOM.classList.add("video-details");
        let videoTitleDOM = document.createElement("div");
        videoTitleDOM.classList.add("video-title");
        let videoDescDOM = document.createElement("div");
        videoDescDOM.classList.add("video-description");
        videoTitleDOM.innerText = title ? DOMPurify.sanitize(title) : "";
        videoDescDOM.innerText = description
          ? DOMPurify.sanitize(description)
          : "";
        videoDetailsDOM.appendChild(videoTitleDOM);
        videoDetailsDOM.appendChild(videoDescDOM);
        $(".vjs-poster").html(videoDetailsDOM);
      }
    }
  });
  // Function to remove the hash after redirection
  $(window).on("DOMContentLoaded", function () {
    setTimeout(function () {
      window.history.replaceState(
        "",
        document.title,
        window.location.pathname + window.location.search
      );
    }, 500);
  });
  // Function to prePopulate the state data
  function prePopulateStateDropdown() {
    var selectedState = $selectBtn.find(".selectBtn").attr("data-state-select");
    if (selectedState != null) {
      var selectedOptions = $("ul").find(`[data-type='${selectedState}']`);
      selectedOptions.addClass("selected");
      $formState.text(selectedState);
      $formState.attr("data-type", selectedState);
      $grpState.text(selectedState);
      $grpState.attr("data-type", selectedState);
      updatePageAnalytics("formState", selectedState);
      updatePageAnalytics("state", selectedState);
      lastActiveOption = selectedOptions;
    }
  }
  // reset all the fields on clicking the back btn
  $($backBtn).on("click", function () {
    if (!$(".form-succ-cntr.form-error-cntr").is(":visible")) {
      linkForm.reset();
      $($grpState).text($stateDefaultVal);
      $($stateEmployees).siblings(".validate").hide();
      $($stateEmployees).css("border", "initial");
      $($formState).text($stateDefaultVal);
      $($grpState).attr("data-type", $stateDefaultVal);
      $($formState).attr("data-type", $stateDefaultVal);
      $(".submit-btn").addClass("disabled");
      $(".submit-btn").attr("disabled", "");
      $($stateEmployees).val("");
      $(".select-input-field .option").removeClass("selected");
      $(".add-agent-cntr").show();
      $(".add-agent").hide();
      prePopulateStateDropdown();
    }
    $(".form-cnt-wrp ").show();
    $(".form-succ-cntr").hide();
    if (window.innerWidth < 768) {
      $(".form-cnt-wrp ")
        .siblings("picture")
        .css({ "min-height": "1700px", "max-height": "1850px" });
    } else {
      $(".form-cnt-wrp ")
        .siblings("picture")
        .css({ "min-height": "1256px", "max-height": "1256px" });
    }
    $(".form-cmp picture img").css("object-position", "bottom");
  });
});
