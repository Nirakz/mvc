//Catch windows resize function, modify site's layout
$(window).on("resize", function() {
	// console.log("Browser height", $(window).height());
	// console.log("Browser width", $(window).width());
	// Caculate height for list friend when resizing window
	$('.list_friend_content_area').css("height", $(window).height() - 70);
});

// Catch event when user refresh page
// $(window).bind("beforeunload", function(){
// if(isProcess == true){
// return "Tin nhắn đăng được xử lí, bạn có chắc chắn muốn tải lại trang?";
// }
// });

$(document)
		.ready(
				function() {
					// Caculate height for list friend
					$('.list_friend_content_area').css("height",
							$(window).height() - 70);
					controlScrollbar();
					initScrollbar();

					// init fancybox for image in chatbox
					$(".fancybox").fancybox({
					       minHeight    : 10,
						   minWidth : 10,
						 // if fancybox 2.x
						 fitToView: false,
						afterShow : function() {
							if (!$(".fancybox-wrap").is(":focus")) {
								$(".fancybox-wrap").focus();
							}
						},
						afterClose : function() {
						},
						key: {
						    next : {
						        39 : 'left', // right arrow
						        40 : 'up'    // down arrow
						    },
						    prev : {
						        37 : 'right',  // left arrow
						        38 : 'down'    // up arrow
						    },
						    play   : [32], // space - start/stop slideshow
						    close  : [27], // escape key
						},
						showCloseButton: true
					});

					// User start chat with another user
					$('.list_friend_item')
							.on(
									"click",
									function(e) {
										var chatboxtitle = $(this).find(
												".user_name").html();
										var idTo = $(this).attr('id');

										if ($.inArray(idTo, all_chatboxes) == -1) { // new
											// chatbox
											chatWith(idTo, chatboxtitle);
											getHistory(id, idTo,
													updateChatArea,
													getMessageOfflineFromMe);
											groupFrom[idTo] = null;
										} else {
											chatWith(idTo, chatboxtitle);
										}
										if ($(this).find($('div.user_online'))
												.html() == null
												&& $("#ch_box_" + idTo).find(
														".notify_offline")
														.html() == null) {
											$("#ch_box_" + idTo)
													.append(
															"<div class='notify_offline'>"
																	+ chatboxtitle
																	+ " hiện offline và sẽ nhận được tin nhắn khi online</div>");
										}
										scrollToBottom(idTo);
									});

					// Handler emotion tab
					$("body").on(
							"click",
							".rE",
							function() {
								var id = $(this).attr('id'); // Emotion tab
								// control's
								// id
								var title = id.substring(5); // Find title
								var prefix = id.substring(0, 4);
								var emo_id = prefix + "_content_" + title; // Emtion
								// content's
								// id
								if ($("#emo_show_" + title).find("#" + emo_id)
										.css("display") == "none") {
									$("#emotion_control_" + title + " .Q")
											.removeClass("Q");
									$("#emotion_control_" + title + " #" + id)
											.addClass("Q");
									$("#emo_show_" + title + " .kZ").css(
											"display", "none");
									$("#emo_show_" + title).find("#" + emo_id)
											.css("display", "inline-block");
								}
							});

					// Show/Close emotion board (When click emotion icon)
					$("body").on("click", ".bt_emotion", function() {
						var id = $(this).attr("id");
						var title = id.replace("bt_emo_", "");
						id = "emo_" + title;
						if ($("#" + id).hasClass("cd")) {
							$("#" + id).removeClass("cd");
							$("#txt_" + title).focus();
						} else {
							$("#" + id).addClass("cd");
							$("#txt_" + title).focus();
						}
					});

					// Close emotion board (When click X button on emtion board)
					$("body").on("click", ".emotion_close", function() {
						var id = $(this).attr("id");
						var title = id.replace("emo_close_", "");
						var emo_id = "emo_" + title;
						$("#" + emo_id).addClass("cd");
					});

					// Search user in friends list
					$("#search_friend_field").on("keyup",function() {
						var strSearch = $.trim($(this).val());
						// alert(strSearch);
						if (strSearch == "") {
							$(".user_name").each(
									function() {
										if ($(this).parent().css("display") == "none") {
											$(this).parent().css("display","block");
										}
									});
						} else {
							$(".user_name").each(
									function() {
										if ($(this).text().indexOf(strSearch) == -1) {
											if ($(this).parent().css("display") == "block") {
												$(this).parent().css("display","none");
											}

										} else {
											if ($(this).parent().css("display") == "none") {
												$(this).parent().css("display","block");
											}
										}
									});
								}

							});

				});

// Hide or show list user chat
function controlScrollbar() {
	var show_list = 0;
	$("#list_friend_title").click(function() {
		$(this).animate({
			right : parseInt((show_list % 2 - 1) * 100) + 'px'
		}, 500);

		$("#list_friend_content").animate({
			right : parseInt((show_list % 2 - 1) * 263) + 'px'
		}, 500);

		$("#search_friend").animate({
			right : parseInt((show_list % 2 - 1) * 263) + 'px'
		}, 500);

		// Displace chatboxes when user shows or hides list friends
		if (show_list % 2 === 0) {
			$('.chatbox').animate({
				right : "-=263px",
				left : "auto"
			}, 500);
		} else {
			$('.chatbox').animate({
				right : "+=263px",
				left : "auto"
			}, 500);
		}

		show_list++;
	});
}

// Show scroll bar for list friends
function initScrollbar() {
	$('.list_friend_content_area').jScrollPane({
		horizontalGutter : 5,
		verticalGutter : 5,
		'showArrows' : false,
		mouseWheelSpeed : 20,
		autoReinitialise : true
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
