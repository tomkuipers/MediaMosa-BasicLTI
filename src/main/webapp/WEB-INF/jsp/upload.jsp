<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title></title>
		<link rel="stylesheet" href="/mmbltiprovider/css/default_consumer.css" type="text/css" media="all" />
		<link rel="stylesheet" href="/mmbltiprovider/css/swfupload.css" type="text/css" media="all" />
		<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" media="all" />
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/swfupload/swfupload.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/swfupload/swfupload.swfobject.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/fileprogress.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/handlers.js" type="text/javascript"></script>
		<script type="text/javascript">
			var swfu;

			$(document).ready(function() {

				swfu = new SWFUpload({
					// Backend settings
					upload_url: "<c:out value="${upload.uploadurl}"/>",
					file_post_name: "file",
					post_params: {
					  "create_still" : "true"
					},
					http_success : [200, 201, 202],

					// Flash file settings
					file_size_limit : "1000 MB",
					file_types : "<c:out value="${upload.filetypes}"/>",
					file_types_description : "Video Files",
					file_upload_limit : "0",
					file_queue_limit : "1",

					// Event handler settings
					swfupload_loaded_handler : swfUploadLoaded,
					
					file_dialog_start_handler: fileDialogStart,
					file_queued_handler : fileQueued,
					file_queue_error_handler : fileQueueError,
					file_dialog_complete_handler : fileDialogComplete,
					
					upload_progress_handler : uploadProgress,
					upload_error_handler : uploadError,
					upload_success_handler : uploadSuccess,
					upload_complete_handler : uploadComplete,

					// Button Settings
					button_image_url: "<c:out value="${upload.imageurl}"/>",
					button_placeholder_id : "spanButtonPlaceholder",
					button_width: 61,
					button_height: 22,
					
					// Flash Settings
					flash_url : "<c:out value="${upload.flashurl}"/>",

					custom_settings : {
						progress_target : "fsUploadProgress",
						upload_successful : false
					},
					
					// Debug settings
					debug: false
				});
					
			});
		</script>
		<script type="text/javascript">
		$(function() {
			// $('a[class*="btn"]').button().click(function() { return true; });
			// ui-icon-search, ui-icon-home, ui-icon-circle-arrow-n, ui-icon-wrench, ui-icon-key, ui-icon-video, ui-icon-folder-open, ui-icon-help
			$('a[class*="home"]').button({icons: {primary:'ui-icon-home'}, text: true});
			$('a[class*="logout"]').button({icons: {primary:'ui-icon-key'}, text: true}).click(function() { return true; });
			$('a[class*="search"]').button({icons: {primary:'ui-icon-search'}, text: true}).click(function() { return true; });
			$('a[class*="browse"]').button({icons: {primary:'ui-icon-folder-open'}, text: true}).click(function() { return true; });
			$('a[class*="upload"]').button({icons: {primary:'ui-icon-arrowthick-1-n'}, text: true}).click(function() { return true; });
			$('a[class*="edit"]').button({icons: {primary:'ui-icon-wrench'}, text: true}).click(function() { return true; });
		});
		</script>
	</head>
	<body>
		<!-- Options for Instructor -->
		<c:if test="${tool.instructor == true}">
		<jsp:include page="navigation.jsp"/>
		<div id="feedback" class="ui-state-highlight ui-corner-all" style="display:none;"></div>
		
		<form:form method="post" action="upload" commandName="upload">
			<fieldset class="ui-widget ui-widget-content">
				<legend><spring:message code="label.upload"/></legend>
				<input type="text" name="file" id="file" /><span id="spanButtonPlaceholder"></span>
        		<div class="flash" id="fsUploadProgress"></div>
        		<input type="hidden" name="assetId" id="assetId" value="<c:out value="${upload.assetId}"/>" />
        		<input type="hidden" name="owner" id="owner" value="<c:out value="${upload.owner}"/>" />
			</fieldset>
			<fieldset class="ui-widget ui-widget-content">
				<legend><spring:message code="label.metadata"/></legend>
				<label for="title"><spring:message code="label.title"/></label><br/>
				<input type="text" id="title" name="title" size="50" value="<c:out value="${upload.title}"/>" /><br/>
				<label for="description"><spring:message code="label.description"/></label><br/>
				<textarea name="description" id="description" rows="4" cols="48"><c:out value="${upload.description}" /></textarea><br/>
				<input type="submit" id="btnSubmit" value="<spring:message code="label.submit"/>" />
			</fieldset>
		</form:form>
		
		</c:if>
	</body>
</html>
