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
		<script src="/mmbltiprovider/javascript/jquery-framedialog-1.1.2.js" type="text/javascript"></script>
		<script src="/mmbltiprovider/javascript/jquery.cookies.2.2.0.js" type="text/javascript"></script>
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
			
			var icons = {
				header: "ui-icon-triangle-1-e",
				headerSelected: "ui-icon-triangle-1-s"
			};

			var cookieName = 'stickyAccordion_<c:out value="${basiclticontext_resourcelink_id}"/>';
			
			$("#accordion").accordion({
				autoHeight: false, 
				icons: icons,
				active: ($.cookies.get(cookieName) || 0),
				change: function( e, ui )
				{
					$.cookies.set(cookieName, $(this).find('h3').index(ui.newHeader[0]));
				}
			});
			
			$('a[class*="delete"]').button({ icons: {primary:'ui-icon-trash'}, text: true}).click(function(e) {
				e.preventDefault();
				var href = $(this).attr('href');
				$.post(href, function(data){
					$('#feedback').html('Successfully removed.').toggle().delay(2000).queue(function() {
						$(location).attr('href','/mmbltiprovider/mmblti/tool');
					});
				});
		    });

			$(function() {
				$('a[class*="edit"]').click(function(e) {
					e.preventDefault();
					var href = $(this).attr('href');
					var title = $(this).attr('title');
					var $dialog = jQuery.FrameDialog.create({
						url: href,
						title: title,
						width: 500,
						height: 500,
						modal: true,
						buttons: {},
						hide: 'slide',
						show: 'slide',
						closeOnEscape: true,
						autoOpen: false
					})
					.bind('dialogclose', function(event, ui) {
						location.reload(true);
					});
					$dialog.dialog('open');
				});
			});
		});
		</script>
	</head>
	<body>
		<!-- Options for Instructor -->
		<c:if test="${tool.instructor == true}">
		<jsp:include page="navigation.jsp"/>
		</c:if>
		<div id="feedback" class="ui-state-highlight ui-corner-all ui-helper-hidden"></div>
		
		<div style="float: left;" class="ui-widget"><spring:message code="label.tool_main"/> <c:out value="${tool.contextTitle}"/></div>
		<c:if test="${tool.instructor == true}">
		<div style="float: right;" class="ui-widget"><c:out value="${tool.instructorName}"/> [Instructor]</div>
		</c:if>
		<br style="clear: both;"/>
		
		<div id="accordion">
	
		<c:forEach items="${resources}" var="resource">
			<h3><a href="#<c:out value="${resource.id}"/>"><c:out value="${resource.title}"/></a></h3>
			<div>
				<p>
				<div id="${resource.assetId}_${resource.id}"><p style="background:#000;color:#fff;width:300px;height:200px;padding:5px;"><spring:message code="label.loading"/></p></div>
				<script type="text/javascript"> 
					//<![CDATA[
					$(function() {
						$.ajax({
							url: 'play/${resource.assetId}',
							type: 'GET',
							dataType: 'html',
							timeout: 30000,
							success: function(data) {
								$("#${resource.assetId}_${resource.id}").html(data);
							}
						});
					});
					//]]>
				</script>
				<c:if test="${tool.instructor == true}">
				<div class="asset_controls">
					<a href="tool/delete/${resource.id}" class="delete btn"><spring:message code="label.remove"/></a>
					<a href="tool/edit/${resource.id}" class="edit btn"><spring:message code="label.edit"/></a>
				</div>
				</c:if>
				<c:out value="${resource.description}"/>
				</p>
			</div>
		</c:forEach>
	
		</div>	
	</body>
</html>