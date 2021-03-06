<!DOCTYPE html>
<head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<!-- Load plupload and all it's runtimes and finally the jQuery queue widget -->
<script type="text/javascript" src="http://www.plupload.com/plupload/js/plupload.full.js"></script>
<script type="text/javascript" src="http://www.jacklmoore.com/colorbox/jquery.colorbox.js"></script>

</head>
<body>
<h3>Custom example</h3>
<a class='inline' href="#container">colorbox</a>
<div id="container">
    <div id="filelist">No runtime found.</div>
    <br />
    <a id="pickfiles" href="#">[Select files]</a>
    <a id="uploadfiles" href="#">[Upload files]</a>
</div>
<script type="text/javascript">
// Custom example logic
$(function() {

    var uploader = new plupload.Uploader({
        runtimes : 'html5,flash,silverlight,browserplus',
        browse_button : 'pickfiles',
        container : 'container',
        max_file_size : '10mb',
        url : 'upload.php',
        flash_swf_url : '/plupload/js/plupload.flash.swf',
        silverlight_xap_url : '/plupload/js/plupload.silverlight.xap',
        filters : [
            {title : "Image files", extensions : "jpg,gif,png"},
            {title : "Zip files", extensions : "zip"}
        ],
        resize : {width : 320, height : 240, quality : 90}
    });

    uploader.bind('Init', function(up, params) {
        $('#filelist').html("<div>Current runtime: " + params.runtime + "</div>");
    });

    uploader.bind('PostInit', function(up) {
        $(".inline").colorbox({
            inline:true, 
            width:"50%",
            onComplete: function() {
                up.refresh();
            }
         }).click();
    });

    $('#uploadfiles').click(function(e) {
        uploader.start();
        e.preventDefault();
    });

    uploader.init();

    uploader.bind('FilesAdded', function(up, files) {
        $.each(files, function(i, file) {
            $('#filelist').append(
                '<div id="' + file.id + '">' +
                file.name + ' (' + plupload.formatSize(file.size) + ') <b></b>' +
            '</div>');
        });

        up.refresh(); // Reposition Flash/Silverlight
    });

    uploader.bind('UploadProgress', function(up, file) {
        $('#' + file.id + " b").html(file.percent + "%");
    });

    uploader.bind('Error', function(up, err) {
        $('#filelist').append("<div>Error: " + err.code +
            ", Message: " + err.message +
            (err.file ? ", File: " + err.file.name : "") +
            "</div>"
        );

        up.refresh(); // Reposition Flash/Silverlight
    });

    uploader.bind('FileUploaded', function(up, file) {
        $('#' + file.id + " b").html("100%");
    });

});
</script>
</body>
</html>