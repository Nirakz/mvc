<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <title>File Upload </title>
    <style type="text/css">
        body { font: 13px arial,helvetica,clean,sans-serif; text-align: center; }
        .container { text-align: left; width: 25em; border: 1px solid #000; padding: 0.5em; }
        .form-field { margin-bottom: 0.5em; }
        .form-field input { border: 1px solid #aaa; padding: 0.2em; }
        .btn-container { text-align: right; }
    </style>
</head>
<body>
    <div class="container">
    <form action="imageUploadServlet" method="post" enctype="multipart/form-data">
        <div class="form-field">
            <label>Tên của tui nè: </label>
            <input type="text" name="name" size="20" />
        </div>
        <div class="form-field">
            <label>Your File: </label>
            <input type="file" name="document" size="25" />
        </div>
        <div class="btn-container">
            <input type="submit" value="Submit"/>
        </div>
    </form>
    </div>
</body>
</html>