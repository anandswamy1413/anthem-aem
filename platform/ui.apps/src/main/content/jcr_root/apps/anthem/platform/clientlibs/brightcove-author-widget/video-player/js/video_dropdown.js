(function($, $document) {
    "use strict";

    // Global property constants
    var ACCOUNTID = "./account";
    var PLAYERS = "./playerPath"; 
    var VIDEOS = "./videoPlayer";
   
    var VIDEO_ACTION = "videos";
    var VIDEO_DROPDOWN_KEY = "title";
    var PLAYER_ACTION = "players";
    
    var API_URL = "/bin/brightcove/api";
    var ACCOUNTS_API = "/bin/brightcove/accounts.json";
    var VIDEO_API = "/bin/brightcove/getLocalVideoList.json";
    var PLAYER_API = "/bin/brightcove/getLocalVideoList.json";
    
    var existingValues = {};

    function adjustLayoutHeight(){
        $(".coral-FixedColumn-column").css("height", "20rem");
    }

    $document.on("dialog-ready", function(e) {
            adjustLayoutHeight();

            var accountSelector =  $("[name='" + ACCOUNTID +"']").get(0);
            var contentSelector =  $("[name='" + VIDEOS +"']").get(0)
            var playerSelector =  $("[name='" + PLAYERS +"']").get(0);
    
            $.getJSON($('.cq-Dialog form').attr("action") + ".json").done(function(data) {
                existingValues = data;
                accountSelector.trigger('coral-select:showitems');
            });
    
            accountSelector.addEventListener('coral-select:showitems', function(event) {
                if (accountSelector.items.length == 0) {
                    $.getJSON(ACCOUNTS_API).done(function(data) {
                        var accounts = data.accounts;
                        event.preventDefault();
                        accounts.forEach(function(value, index) {
                            var item = {
                                value: value.value,
                                content: {
                                    textContent: value.text
                                }
                            }
                            if (existingValues.account == null && index == 0) {
                                item.selected = true;
                            } else if (item.value == existingValues.account) {
                                item.selected = true;
                            }
                            accountSelector.items.add(item);
                        });
    
                        // now trigger the other fields
                        contentSelector.trigger('coral-select:showitems');            
                        playerSelector.trigger('coral-select:showitems');
                    });
                }
            });
    
            contentSelector.addEventListener('coral-select:showitems', function(event) {
                if (contentSelector.items.length == 0) {
                    var CONDITION = existingValues.videoPlayer;
                    $.getJSON(VIDEO_API , {
                        source: VIDEO_ACTION
                    }).done(function(data) {
                        var videos = data.items;
                        event.preventDefault();
                        videos.forEach(function(value, index) {
                            var item = {
                                value: value.id,
                                content: {
                                    textContent: value[VIDEO_DROPDOWN_KEY]
                                }
                            }
                            if ( (CONDITION != null) && (item.value == CONDITION) ) {
                                item.selected = true;
                            }
                            contentSelector.items.add(item);
                        });
                    });
                }
            });
    
            // /bin/brightcove/api?a=local_players&account_id=6066350955001&limit=30&start=0
    
            playerSelector.addEventListener('coral-select:showitems', function(event) {
                if (playerSelector.items.length == 0) {
                    $.getJSON(PLAYER_API , {
                        source: PLAYER_ACTION
                    }).done(function(data) {
                        var players = data.items;
                        event.preventDefault();
                        players.forEach(function(value, index) {
                            var item = {
                                value: value.id,
                                content: {
                                    textContent: value.name
                                }
                            }
                            if (existingValues.playerPath != null && item.value == existingValues.playerPath) {
                                item.selected = true;
                            }
                            playerSelector.items.add(item);
                        });
                    });
                }
            });
      
    });

})($, $(document));