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
		<link rel="stylesheet" href="/mmbltiprovider/css/demo_table_jui.css" type="text/css" media="all" />
		<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" type="text/css" media="all" />
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
		<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="/mmbltiprovider/javascript/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="/mmbltiprovider/javascript/swfobject.js"></script>
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

		    $('a[class*="add"]').button({ icons: {primary:'ui-icon-plus'} }).click(function(e) {
				e.preventDefault();
				var href = $(this).attr('href');
				$.post(href, function(data){
					$('#feedback').html('<spring:message code="label.add_ok" />').toggle().delay(2000).queue(function() {
						$(location).attr('href','/mmbltiprovider/mmblti/tool');
					});
				});
		    });

		    $('a[class*="delete"]').button({ icons: {primary:'ui-icon-trash'} }).click(function(e) {
				e.preventDefault();
				var href = $(this).attr('href');
				$.post(href, function(data){
					$('#feedback').html('<spring:message code="label.delete_ok" />').toggle().delay(2000).queue(function() {
						$(location).attr('href','/mmbltiprovider/mmblti/tool');
					});
				});
		    });
		    
		    $('#browseAssets').dataTable({
			    "bJQueryUI": true,
			    "bAutoWidth": true,
			    "bStateSave": true,
			    "bInfo": false
			    /*,
			    "sPaginationType": "full_numbers",
			    
			    "bInfo": false,
			    "bLengthChange": false,
			    "bProcessing": true,
			    "bScrollCollapse": true,
			    "sScrollY": "600px" */
			});
		} );
		</script>
	</head>
	<body>
		<!-- Options for Instructor -->
		<c:if test="${tool.instructor == true}">
		<jsp:include page="navigation.jsp"/>
		</c:if>
		<div id="feedback" class="ui-state-highlight ui-corner-all" style="display:none;"></div>
		<display:table id="asset" name="assets" htmlId="browseAssets" class="ui-widget ui-widget-content">
  			<display:column property="dublinCore.title" title="Title" />
  			<display:column title="Still">
  			<a href="../play/${asset.assetId}/details">
  				<img src='${asset.mediafileContainerType == "mp3" ? "/mmbltiprovider/images/audio.png" : asset.vpxStillUrl}' width="176" class="still" alt="${asset.dublinCore.title}" />
  			</a>
  			</display:column>
  			<display:column title="Date">
  			<fmt:formatDate value="${asset.videotimestamp.time}" pattern="dd MMM yyyy @ HH:mm" />
  			</display:column>
  			<display:column property="assetId" title="Id"/>
  			<display:column>
  				<a href="add/${asset.assetId}" class="add btn">Add to Tool Resources</a>
  				<a href="asset/${asset.assetId}/delete" class="delete btn">Delete asset</a>
  			</display:column>
  			<display:setProperty name="basic.msg.empty_list"><spring:message code="label.table.empty_list"/></display:setProperty>
		</display:table>
	</body>
</html>
