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
		<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" media="all" />
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/jquery-framedialog-1.1.2.js" type="text/javascript"></script>
		<script type="text/javascript">
		$(function() {
			$('input:submit[class*="btn"], input:button[class*="btn"]').button();
			$('input:button[class*="btn"]').click(function () {
				jQuery.FrameDialog.cancelDialog();
		    });
		});
		</script>
		<style type="text/css">
			
			label {
				float: left;
				width: 120px;
				font-weight: bold;
			}

			input[type='text'], textarea {
				width: 250px;
				margin-bottom: 5px;
			}

			textarea {
				height: 150px;
			}

			input[type='submit'] {
				margin-left: 120px;
				margin-top: 5px;
			}

			br {clear: left;}
		</style>
	</head>
	<body>
	<form:form method="post" action="/mmbltiprovider/mmblti/tool/edit" commandName="resource" class="ui-widget">
		<fieldset class="ui-widget-content">
			<label for="title"><spring:message code="label.title"/></label>
			<input type="text" id="title" name="title" value="<c:out value="${resource.title}"/>" class="ui-state-default"/><br/>
			<label for="description"><spring:message code="label.description"/></label>
			<textarea id="description" name="description" class="ui-state-default"><c:out value="${resource.description}"/></textarea>
			<input type="hidden" id="id" name="id" value="<c:out value="${resource.id}"/>"/>
			<input type="hidden" id="assetId" name="assetId" value="<c:out value="${resource.assetId}"/>"/>
			<input type="hidden" id="contextId" name="contextId" value="<c:out value="${resource.contextId}"/>"/>
			<input type="hidden" id="resourceLinkId" name="resourceLinkId" value="<c:out value="${resource.resourceLinkId}"/>"/>			
			<input type="submit" class="btn" value="<spring:message code="label.submit"/>"/>
			<input type="button" class="btn" value="<spring:message code="label.cancel"/>"/>
		</fieldset>
	</form:form>
	</body>
</html>
 