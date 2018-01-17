<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/inc/header.jsp" %>

<div class="container-fluid" style="margin-top:80px;">
    <div class="row">
        <div class="col-md-12">
            <h3>${DOCKER_USER }</h3>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <c:forEach var="item" items="${headers }">
                        <th>${item }</th>
                    </c:forEach>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.image}</td>
                        <td>${item.command}</td>
                        <td>${item.created }</td>
                         <td>${item.status }</td>
                        <td>${item.ports}</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-xs" 
                            onclick="goContainer('${item.id}')">进入
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $("ul.nav.navbar-nav li").removeClass("active");
        $("#li_containers").addClass("active");
    });

    function goContainer(cId) {
    	window.open("/mgr/go/container/"+cId,"_blank");
    }
</script>
<%@ include file="/WEB-INF/jsp/inc/footer.jsp" %>