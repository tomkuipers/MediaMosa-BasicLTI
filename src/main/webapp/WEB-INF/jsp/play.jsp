<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
		<script type="text/javascript" src="/mmbltiprovider/javascript/swfobject.js"></script>
		<script type="text/javascript">
		$(function() {
			$('a[class*="btn"]').button().click(function() { return true; });

			$('a[class*="add"]').button({ icons: {primary:'ui-icon-gear'} }).click(function(e) {
				e.preventDefault();
				var href = $(this).attr('href');
				$.post(href, function(data){
					$('#feedback').html('<spring:message code="label.add_ok" />').toggle().delay(2000).queue(function() {
						$(location).attr('href','/mmbltiprovider/mmblti/tool');
					});
				});
		    });			
		});
		</script>
	</head>
	<body>
		<!-- tool -->
		<a href="/mmbltiprovider/mmblti/tool" class="btn"><spring:message code="label.tool"/></a>
		<!-- Options for Instructor -->
		<c:if test="${tool.instructor == true}">
		<jsp:include page="navigation.jsp"/>
		</c:if>
		<div id="feedback" class="ui-state-highlight ui-corner-all" style="display:none;"></div>
		<div style="float:left;">
    		<br/>
    		<c:out value="${media.player}" escapeXml="false" />
		</div>
		<div class="ui-widget ui-widget-content" style="float:left; padding-left:2em;">
			<br/>
			<span class="contentTitle"><c:out value="${media.assetDetails.dublinCore.title}" /></span><br/>
			<c:out value="${media.assetDetails.dublinCore.description}" />
			<p>
			<spring:message code="label.stats_duration"/>: <c:out value="${media.assetDetails.mediafileDuration}" /><br/>
			<spring:message code="label.stats_add"/>: <fmt:formatDate value="${media.assetDetails.videotimestamp.time}" pattern="dd MMM yyyy @ HH:mm" /><br/>
			<spring:message code="label.stats_play"/>: <c:out value="${media.assetDetails.played}" /><br/>
			<spring:message code="label.stats_view"/>: <c:out value="${media.assetDetails.viewed}" /><br/> 
			<!-- Url: ${media.link.output}  -->
			</p>
		</div>
		<a href="/mmbltiprovider/mmblti/tool/add/${media.assetDetails.assetId}" class="add btn">Add to Tool Resources</a>
		

	</body>
</html>