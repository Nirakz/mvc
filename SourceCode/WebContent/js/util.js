/* init */
function initIndex() {
	/*
	 * Global var
	 */
	URL_MESSAGE = "/WebChat/api/message";
	URL_USER = "/WebChat/account";
	URL_HISTORY = "/WebChat/history";
	messageHistoryIndex = [];
	isLoading = [];

	countUserOnline = 0;

	// group message
	groupFrom = [];
	groupTo = [];
	groupLoadMoreFrom = [];
	groupLoadMoreTo = [];

	// id
	id = $('#myID').val();

	// init polling
	pollUserOnline(updateListUser);
	pollMessage(id, updateChatArea);
	getNotification(id, showNotification);

	// window.onunload = quit;
	window.onbeforeunload = quit;

	// Max chatbox height
	maxCHeight = $(window).height() - 100;

	// Max chatbox width
	maxCWidth = $(window).width() - 400;

	// Array contains all all_chatboxes
	all_chatboxes = new Array();
	
	//Array store previous cookie
//	preCookie = new Array();

	// List order and visible chatboxes
//	if(readCookie("dbcookies") != null){
//		listCbOrder = readCookie("dbcookies").split(",");
//	}else{
//		listCbOrder = new Array();
//	}
	
	listCbOrder = new Array();
	
	// Array contain chatboxes' width before minimize
	listCbWidth = {};

	// Chatbox input length
	inputLength = 187;

	// List notify box
	listNotify = new Array();

	// Whether or no the server is procressing message
	isProcess = true;




}
/*
 * POLL MESSAGE
 */
function pollMessage(name, updateChatArea) {
	$.ajax({
		type : 'POST',
		url : URL_MESSAGE,
		timeout: 40000,
		data : {
			action : "getListMessage",
			name : name,
		},
		dataType : 'text',
		success : function(data) {
			if (data != "") {
				var listMessage = JSON.parse(data);
				updateChatArea(name, listMessage, null, false, false);
			} else {
				console.log("No message, time out now!!!!!!!!!");
			}
			pollMessage(name, updateChatArea);
		},
		error: function(){
			console.log("poll message file, start connecting again");
			$("#uch_" + name + " .m_sk").filter(":last").css("visibility",
					"hidden");
			$("#uch_" + name + " .m_th").filter(":last").css("visibility",
					"visible");
			$("#uch_" + name + " .m_sv").filter(":last").css("visibility",
					"hidden");
			
			setTimeout(function(){
				pollMessage(name, updateListUser);
			}, 10000);
			
		}
	});
}

function updateChatArea(name, listMessage, arrayID, isLoadMore, isOffline) {
	var chatboxtitle;
	var currentID;

	for ( var i = 0; i < listMessage.length; i++) {
		var scrollAtBottom = false;
		var message = listMessage[i];

		var currentGroup;

		if (message.idFrom != name) { // receive

			currentID = message.idFrom;
			scrollAtBottom = isScrollAtBottom(currentID);
			chatboxtitle = $("#" + currentID).find(".user_name").html();

			if ($.inArray(currentID, all_chatboxes) == -1) {
				chatWith(currentID, chatboxtitle, function(){
					//Call back function run after finish intalizing chatbox
					getHistory(name, currentID, updateChatArea,
							getMessageOfflineFromMe);
					scrollToBottom(currentID);
				});
					

				groupTo[currentID] = null; // close history group
			} else if ($("#chatbox_" + currentID).css("display") == "none") {
				reStructureall_chatboxes();
				var rightPos = caculateRightPos();
				$("#chatbox_" + currentID).css("right", rightPos + "px");
				listCbOrder.push(currentID);
				$("#chatbox_" + currentID).css("display", "block");
				$("#chatbox_" + currentID).css("width", CHATBOX_WIDTH + "px");
				$("#chatbox_" + currentID + " .chatboxcontent").css("height",
						247 + "px");
				$("#chatbox_" + currentID).css("display", "block");
			} else if ($("#chatbox_" + currentID + " .chatboxview").css(
					"display") == "none") {
				$("#chatbox_" + currentID + " .chatboxview").css("display",
						"block");

			}
			if (isLoadMore == true) {
				if (groupLoadMoreTo[currentID] == null) {

					groupLoadMoreTo[currentID] = createGroupMes(message.idTo,
							message.time.toLocaleString(), "left",
							message.idFrom, true);

					$("#ch_box_" + currentID).prepend(
							groupLoadMoreTo[currentID]);

					groupLoadMoreFrom[currentID] = null;
				}
				currentGroup = groupLoadMoreTo[currentID].find(".m_jl");
			} else {
				if (groupTo[currentID] == null) {

					groupTo[currentID] = createGroupMes(message.idTo,
							message.time.toLocaleString(), "left",
							message.idFrom, true);

					$("#ch_box_" + currentID).append(groupTo[currentID]);

					groupFrom[currentID] = null;
				}
				currentGroup = groupTo[currentID].find(".m_jl");
			}

		} else { // send
			currentID = message.idTo;
			scrollAtBottom = true;
			chatboxtitle = $("#" + currentID).find(".user_name").html();
			if (isLoadMore == true) {
				if (groupLoadMoreFrom[currentID] == null) {
					groupLoadMoreFrom[currentID] = createGroupMes(message.idTo,
							message.time.toLocaleString(), "right",
							message.idFrom, true);

					$("#ch_box_" + currentID).prepend(
							groupLoadMoreFrom[currentID]);

					groupLoadMoreTo[currentID] = null;
				}
				currentGroup = groupLoadMoreFrom[currentID].find(".m_jl");
			} else {
				if (groupFrom[currentID] == null) {
					groupFrom[currentID] = createGroupMes(message.idTo,
							message.time.toLocaleString(), "right",
							message.idFrom, true);
					$("#ch_box_" + currentID).append(groupFrom[currentID]);

					groupTo[currentID] = null;
				}
				currentGroup = groupFrom[currentID].find(".m_jl");
			}
		}

		if (message.type == 0) { // text
			if (isLoadMore == true) {
				$(currentGroup).prepend(createMes(message.content));
			} else {
				if (isOffline == true || message.idFrom != name) {
					$(currentGroup).append(createMes(message.content));
				}

				$("#uch_" + message.idTo + " .m_sk").filter(":last").css(
						"visibility", "hidden");
				$("#uch_" + message.idTo + " .m_th").filter(":last").css(
						"visibility", "hidden");
				$("#uch_" + message.idTo + " .m_sv").filter(":last").text(
						message.time.toLocaleString());
				$("#uch_" + message.idTo + " .m_sv").filter(":last").css(
						"visibility", "visible");
			}
		} else if (message.type == 2 || message.type == 3) { // image and
			// file
			if (isLoadMore == true) {
				$(currentGroup).prepend(createMes(message.content));
			} else {
				$(currentGroup).append(createMes(message.content));
				$("#uch_" + message.idTo + " .m_sk").filter(":last").css(
						"visibility", "hidden");
				$("#uch_" + message.idTo + " .m_th").filter(":last").css(
						"visibility", "hidden");
				$("#uch_" + message.idTo + " .m_sv").filter(":last").text(
						message.time.toLocaleString());
				$("#uch_" + message.idTo + " .m_sv").filter(":last").css(
						"visibility", "visible");
				scrollToBottom(currentID);
			}
		} else if (message.type == 1) { // link
			var linkDescriptor = JSON.parse(message.content);
			var wrapLink = document.createElement("div");
			wrapLink.className = "wrap_link";

			var title = document.createElement("a"); // title and url

			title.innerHTML = linkDescriptor.title;
			title.setAttribute("href", linkDescriptor.url);
			title.className = "title_link";

			var thumbnail = document.createElement("img"); // thumbnail
			thumbnail.src = linkDescriptor.thumbnail;
			thumbnail.style.width = '54px';
			thumbnail.style.height = '54px';
			thumbnail.className = "thumbnail_link";

			var description = document.createElement("span");
			if (linkDescriptor.description == null) {
				description.innerHTML = "";
			} else {
				description.innerHTML = linkDescriptor.description;
			}

			description.className = "description_link";

			$(wrapLink).append(thumbnail);
			$(wrapLink).append(title);
			$(wrapLink).append(description);
			if (isLoadMore == true) {
				$(currentGroup).prepend(wrapLink);
			} else {
				$(currentGroup).append(wrapLink);
			}

			$("#uch_" + message.idTo + " .m_sk").filter(":last").css(
					"visibility", "hidden");
			$("#uch_" + message.idTo + " .m_th").filter(":last").css(
					"visibility", "hidden");
			$("#uch_" + message.idTo + " .m_sv").filter(":last").text(
					message.time.toLocaleString());
			$("#uch_" + message.idTo + " .m_sv").filter(":last").css(
					"visibility", "visible");
		}
		// get array ID offline
		if (arrayID != null) {
			arrayID.push(message.id);
		}

		// auto scroll to bottom
		if (isLoadMore == true) {
			var top = $("#ch_box_" + currentID).scrollTop();
			$("#ch_box_" + currentID).scrollTop(top + 30);
		} else {
			if (scrollAtBottom == true) {
				scrollToBottom(currentID);
				hideNotifyScroll(currentID);
			} else {
				createNotifyScroll(currentID);
			}
		}

	}
}

function sendMessage(idFrom, idTo, content) {
	var data;
	if (ValidUrl(content)) { // link
		data = {
			idFrom : idFrom,
			idTo : idTo,
			content : content,
			type : 1,
			offline : false
		};
	} else { // text
		data = {
			idFrom : idFrom,
			idTo : idTo,
			content : content,
			type : 0,
			offline : false
		};
	}
	$.ajax({
		type : 'POST',
		url : URL_MESSAGE + '?action=sendMessage',
		contentType : 'application/json; charset=UTF-8',
		data : JSON.stringify(data),
		dataType : 'text',
		success : function(data) {
			console.log("post success");
			// setTimeout(function(){
			// //Server finish procressing message
			// isProcess = false;
			// }, 1000);
		},
		error : function() {
			console.log("post fail");
			$("#uch_" + idTo + " .m_sk").filter(":last").css("visibility",
					"hidden");
			$("#uch_" + idTo + " .m_th").filter(":last").css("visibility",
					"visible");
			$("#uch_" + idTo + " .m_sv").filter(":last").css("visibility",
					"hidden");
		}
	});

}
/*
 * POLL USER ONLINE
 */
function pollUserOnline(updateListUser) {

	$.ajax({
		type : 'POST',
		url : URL_USER,
		timeout: 40000, 
		data : {
			action : "getListUser",
			countUserOnline : countUserOnline,
			username : id

		},
		dataType : 'text',
		success : function(data) {
			hideListUserError();
			if (data != "") {
				var userOnlineData = JSON.parse(data);
				updateListUser(userOnlineData.listUserOnline,
						userOnlineData.idChangedUser);

			} else {
				console.log("Get list user online time out");
			}
			pollUserOnline(updateListUser);
		},
		error: function(){
			console.log("poll user online fail, start connecting again");
			setTimeout(function(){
				createListUserError();
			}, 2000);
			setTimeout(function(){
				pollUserOnline(updateListUser);
			}, 10000);
		}

	});
}
function updateListUser(listUser, idChange) {

	$('.user_online').remove();

	countUserOnline = listUser.length;
	for ( var i = 0; i < listUser.length; i++) {
		var user = listUser[i];
		var online = document.createElement('div');
		online.className = "user_online";
		$("#" + user).append(online);
	}
	// var name = $("#chatbox_" + idChange + " .chatboxtitle").text();
	var name = $("#" + idChange).find(".user_name").html();
	var messageNotify = "";
	var scrollAtBottom = isScrollAtBottom(idChange);
	if ($.inArray(idChange, listUser) == -1) {
		$("#ch_box_" + idChange).append(
				"<div class='notify_offline'>" + name + " vừa offline</div>");
		messageNotify = name + " offline";

		// scrollToBottom(idChange);
	} else {
		$("#ch_box_" + idChange).append(
				"<div class='notify_online'>" + name + " vừa online</div>");
		messageNotify = name + " online";
		// scrollToBottom(idChange);
	}
	groupTo[idChange] = null;
	groupFrom[idChange] = null;
	if (scrollAtBottom == true) {
		scrollToBottom(idChange);
	}

	if (id != idChange) {
		createNotify(idChange, messageNotify);
	}

}

function quit() {
	$.ajax({
		type : 'POST',
		url : URL_USER,
		data : {
			action : "quit",
			username : id
		},
		success : function(data) {
			console.log("quittttt");
		}
	});
}
/*
 * GET HISTORY
 */
function getHistory(idFrom, idTo, updateChatArea, getMessageOfflineFromMe) {
	if (messageHistoryIndex[idTo] == null) {
		messageHistoryIndex[idTo] = 0;
	}
	$.ajax({
		async : false,
		type : 'POST',
		url : URL_HISTORY,
		data : {
			action : "getHistory",
			idFrom : idFrom,
			idTo : idTo,
			index : messageHistoryIndex[idTo]
		},
		dataType : 'text',
		success : function(data) {
			hideDisConnectNofity();
			if (data != "[]") { // have history
				var listMessage = JSON.parse(data);

				// listMessage.reverse();
				var size = listMessage.length;
				messageHistoryIndex[idTo] = listMessage[size - 1].id;
				updateChatArea(idFrom, listMessage, null, true, false);
			}

			getMessageOfflineFromMe(id, idTo, updateChatArea,
					getMessageOfflineToMe);
		},
		error: function(){
			createDisConnectNofity(idTo);
		}
	});
}


/*
 * GET MESSAGE OFFLINE
 */
function getMessageOfflineFromMe(idFrom, idTo, updateChatArea,
		getMessageOfflineToMe) {
	$.ajax({
		type : 'POST',
		url : URL_HISTORY,
		data : {
			action : "getMessageOffline",
			idFrom : idFrom,
			idTo : idTo,
			updateOffline: false
		},
		dataType : 'text',
		success : function(data) {
			if (data != "[]") {
				var listMessage = JSON.parse(data);
				// updateChatArea(id, listMessage, null);

				updateChatArea(id, listMessage, null, false, true);

			}
			getMessageOfflineToMe(idTo, idFrom, updateOfflineMessage,
					updateChatArea);
		}
	});
}
function getMessageOfflineToMe(idFrom, idTo, updateOfflineMessage,
		updateChatArea) {
	$.ajax({
		type : 'POST',
		url : URL_HISTORY,
		data : {
			action : "getMessageOffline",
			idFrom : idFrom,
			idTo : idTo,
			updateOffline: true
		},
		dataType : 'text',
		success : function(data) {
			if (data != "[]") {
				var arrayID = new Array();
				var listMessage = JSON.parse(data);
				updateChatArea(id, listMessage, arrayID, false, true);
				updateOfflineMessage(arrayID);
				groupTo[idFrom] = null;
			}
		}

	});
}

function updateOfflineMessage(arrayID) {
	$.ajax({
		type : 'POST',
		url : URL_HISTORY,
		data : {
			action : "updateOfflineMessage",
			arrayID : JSON.stringify(arrayID)
		},
		dataType : 'text',
		success : function(data) {
			console.log("update offline success!!!");
		},

	});
}

/*
 * GET NOTIFICATION
 */
function getNotification(id, showNotification) {
	$.ajax({
		type : 'POST',
		url : URL_HISTORY,
		data : {
			action : "getNotification",
			id : id
		},
		dataType : 'text',
		success : function(data) {
			if (data != "[]") {
				var listNotification = JSON.parse(data);
				showNotification(listNotification);
			}
		}
	});
}

function showNotification(listNotification) {
	for ( var i = 0; i < listNotification.length; i++) {
		var noti = listNotification[i];
		$("#" + noti.idFrom).find(".notification").html(noti.count);
	}
}

function ValidUrl(str) {
	/*
	 * var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
	 * '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
	 * '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
	 * '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
	 * '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string '(\\#[-a-z\\d_]*)?$', 'i'); //
	 * fragment locator
	 * 
	 */
	var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
	'(([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}.*'); // domain name

	if (!pattern.test(str)) {
		return false;
	} else {
		return true;
	}
}

function scrollToBottom(chatBoxId) {
	var top = 0;
	$("#ch_box_" + chatBoxId + " .grp_messages").each(function() {
		top += parseInt($(this).css("height"));
	});
	top+= 5000;
	$("#ch_box_" + chatBoxId).scrollTop(top);

}

function isScrollAtBottom(chatBoxId) {
	var chatBox = $("#ch_box_" + chatBoxId);
	var currentPos = $(chatBox).scrollTop();
	var testPos = currentPos + 1;
	$(chatBox).scrollTop(testPos);
	var realPos = $(chatBox).scrollTop();
	if (realPos > currentPos) { // not reach bottom
		$(chatBox).scrollTop(currentPos);
		return false;
	} else {
		return true;
	}
}

function loadMoreHistory(idFrom, idTo) {
	if (isLoading[idTo] == null) {
		isLoading[idTo] = false;
	}
	if ($("#ch_box_" + idTo).scrollTop() == 0 && isLoading[idTo] == false
			&& messageHistoryIndex[idTo] != 0) {
		isLoading[idTo] = true;
		$.ajax({
			async : false,
			type : 'POST',
			url : URL_HISTORY,
			data : {
				action : "getHistory",
				idFrom : idFrom,
				idTo : idTo,
				index : messageHistoryIndex[idTo]
			},
			dataType : 'text',
			success : function(data) {
				if (data != "[]") {
					var listMessage = JSON.parse(data);
					var size = listMessage.length;
					messageHistoryIndex[idTo] = listMessage[size - 1].id;
					// listMessage.reverse();
					updateChatArea(id, listMessage, null, true, false);
					isLoading[idTo] = false;
				} else { // no more history
					messageHistoryIndex[idTo] = 0;
				}
			}
		});
	}
}

//Create cookie
//If days = 0, cookie will expire when user close browser
function setCookie(name, value, days){
	if(days){
		var date = new Date();
		date.setTime(date.getTime() + (days*24*60*60*1000));
		var expires = "; expires=" + date.toUTCString();
	}else{
		expires = "";
	}
	document.cookie = name + "=" + value + expires + "; path=/";
}

//Read cookie from name
function readCookie(name){
	var nameEQ = name + "=";
	var ca = document.cookie.split(";");
	for(var i = 0; i < ca.length; i++){
		var c = ca[i];
		while(c.charAt(0) == " "){
			c = c.substring(1, c.length);
		}
		if(c.indexOf(nameEQ) == 0){
			return c.substring(nameEQ.length, c.length);
		}
	}
	return null;
}

//Delete cookie 
function deleteCookie(name){
	setCookie(name, "", -1);
}

//Reload all chatboxes
function preLoad(){
	for ( var i = 0; i < listCbOrder.length; i++) {
		var title = $("#" + listCbOrder[i] + " .user_name").text();
		createChatBox(listCbOrder[i], title);
	}
}

//Check if cookie has changed
function checkCookieChange(name, callback){
	setInterval(function(){
		if(!preCookie[name]){
			preCookie[name] = readCookie(name);
		}else{
			if(preCookie[name] != readCookie(name)){
				preCookie[name] = readCookie(name);
				return callback();
			}
		}
	}, 200);
}

//If cookie contains chatbox changes
function changeChatBox(){
	for ( var i = 0; i < listCbOrder.length; i++) {
		closeChatBox(listCbOrder[i]);
	}
	preLoad();
}