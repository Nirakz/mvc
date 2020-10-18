//Catch windows resize function, modify site's layout
$(window).on("resize", function() {
    console.log("Browser height", $(window).height());
    console.log("Browser width", $(window).width());
    //Caculate height for list friend when resizing window
    $('.list_friend_content_area').css("height", $(window).height() - 70);
    console.log("Main height", $('.list_friend_content_area').css("height"));
});

$(document).ready(function() {
    //Caculate height for list friend
    $('.list_friend_content_area').css("height", $(window).height() - 70);
    
	controlScrollbar();

    initScrollbar();

    //Handler emotion tab
    $("body").on("click", ".rE",function() {
        var id = $(this).attr('id'); 					//Emotion tab control's id
		var title = id.substring(5);					//Find title
		var prefix = id.substring(0, 4);	
		var emo_id = prefix + "_content_" + title; 		//Emtion content's id
        if ($("#emo_show_" + title).find("#" + emo_id).css("display") == "none") {
			$("#emotion_control_"+ title +" .Q").removeClass("Q");
			$("#emotion_control_"+ title +" #" + id).addClass("Q");
            $("#emo_show_"+ title +" .kZ").css("display", "none");
            $("#emo_show_"+ title).find("#" + emo_id).css("display", "inline-block");
        }
    });
	
	//Show/Close emotion board (When click emotion icon)
	$("body").on("click", ".bt_emotion", function(){
		var id = $(this).attr("id");
		var title = id.replace("bt_emo_", "");
		id = "emo_" + title;
		if($("#" + id).hasClass("cd")){
			$("#" + id).removeClass("cd");
		}else{
			$("#" + id).addClass("cd");
		}
	});
	
	//Close emotion board (When click X button on emtion board)
	$("body").on("click", ".emotion_close", function(){
		var id = $(this).attr("id");
		var title = id.replace("emo_close_", "");
		var emo_id = "emo_" + title;
		$("#" + emo_id).addClass("cd");
	});
	
	$("body").on("click", ".Cy", function(){
		var id = $(this).attr("string");
		var emo_link = "emoji_u" + id +".png";
		
		var title = $(this).parent().attr("id").replace("emo1_content_", "");
		//$("#txt_" + title).execCommand("insertImage", false, "../images/emo_collection/" + emo_link);
		var temp = document.getElementById("txt_" + title);
		//execCommandOnElement(temp, "insertImage", "images/emo_collection/" + emo_link);
		execCommandOnElement(temp, "insertImage", "file:///C:/Users/phamduong/Desktop/fresher-finalproject-chat/SourceCode/Html/images/emo_collection/" + emo_link);
	});
});

/************/
function pasteHtmlAtCaret(html) {
    var sel, range;
    if (window.getSelection) {
        // IE9 and non-IE
        sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            // Range.createContextualFragment() would be useful here but is
            // non-standard and not supported in all browsers (IE9, for one)
            var el = document.createElement("div");
            el.innerHTML = html;
            var frag = document.createDocumentFragment(), node, lastNode;
            while ( (node = el.firstChild) ) {
                lastNode = frag.appendChild(node);
            }
            range.insertNode(frag);

            // Preserve the selection
            if (lastNode) {
                range = range.cloneRange();
                range.setStartAfter(lastNode);
                range.collapse(true);
                sel.removeAllRanges();
                sel.addRange(range);
            }
        }
    } else if (document.selection && document.selection.type != "Control") {
        // IE < 9
        document.selection.createRange().pasteHTML(html);
    }
}

/************/
function execCommandOnElement(el, commandName, value) {
    if (typeof value == "undefined") {
        value = null;
    }

    if (typeof window.getSelection != "undefined") {
        // Non-IE case
        var sel = window.getSelection();

        // Save the current selection
        var savedRanges = [];
        for (var i = 0, len = sel.rangeCount; i < len; ++i) {
            savedRanges[i] = sel.getRangeAt(i).cloneRange();
        }

        // Temporarily enable designMode so that
        // document.execCommand() will work
        document.designMode = "on";

        // Select the element's content
        sel = window.getSelection();
        var range = document.createRange();
        range.selectNodeContents(el);
        sel.removeAllRanges();
        sel.addRange(range);

        // Execute the command
        document.execCommand(commandName, false, value);

        // Disable designMode
        document.designMode = "off";

        // Restore the previous selection
        sel = window.getSelection();
        sel.removeAllRanges();
        for (var i = 0, len = savedRanges.length; i < len; ++i) {
            sel.addRange(savedRanges[i]);
        }
    } else if (typeof document.body.createTextRange != "undefined") {
        // IE case
        var textRange = document.body.createTextRange();
        textRange.moveToElementText(el);
        textRange.execCommand(commandName, false, value);
    }
}

//Hide or show list user chat
function controlScrollbar(){	
    var show_list = 0;
    $("#list_friend_title").click(function() {
        $(this).animate({
            right: parseInt((show_list % 2 - 1) * 100) + 'px'
        }, 500);

        $("#list_friend_content").animate({
            right: parseInt((show_list % 2 - 1) * 263) + 'px'
        }, 500);

        $("#search_friend").animate({
            right: parseInt((show_list % 2 - 1) * 263) + 'px'
        }, 500);

        //Displace chatboxes when user shows or hides list friends
        if (show_list % 2 === 0) {
            $('.chatbox').animate({
                right: "-=263px"
            }, 500);
        } else {
            $('.chatbox').animate({
                right: "+=263px"
            }, 500);
        }

        show_list++;
    });
}

//Show scroll bar for list friends
function initScrollbar(){	
    $('.list_friend_content_area').jScrollPane({
        horizontalGutter: 5,
        verticalGutter: 5,
        'showArrows': false,
        mouseWheelSpeed: 20,
        autoReinitialise: true
    });

    $('.jspDrag').hide();
    $('.list_friend_content_area').mouseenter(function() {
        $('.jspDrag').stop(true, true).fadeIn(0);
    });
    $('.list_friend_content_area').mouseleave(function() {
        $('.jspDrag').stop(true, true).fadeOut('slow');
    });

    $('.jspTrack').mouseenter(function() {
        $('.jspTrack').stop(true, true).addClass('jspTrackHover');
        $('.jspDrag').css("width", "7px");
    });
    $('.jspTrack').mouseleave(function() {
        $('.jspTrack').stop(true, true).removeClass('jspTrackHover');
        $('.jspDrag').css("width", "");
    });
}