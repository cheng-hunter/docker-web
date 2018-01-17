<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<div class="container-fluid" style="margin-top:80px;width:1000px;"  id="xterm"></div>
<script type="text/javascript">
	var term = new Terminal({
	    cursorBlink: true,
	    cols: 100,
	    rows: 50
	});
	term.open(document.getElementById('xterm'));
	var socket = new WebSocket('ws://${ip}:${port}/ws/container/exec?width=100&height=50&containerId=${id}');
	term.attach(socket);
	term.focus();
</script>
<%@ include file="/WEB-INF/jsp/inc/footer.jsp" %>