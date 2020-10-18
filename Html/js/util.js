$(document).ready(function() {
    //User start chat with another user
    //$('.list_friend_item').on("click", chatWith($(this).attr("id")), false);
    $('.list_friend_item').on("click", function(e) {
        //alert($(this).attr('id'));
        chatWith($(this).attr('id'));
    });
});