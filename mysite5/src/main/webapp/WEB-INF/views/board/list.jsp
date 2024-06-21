<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.servletContext.contextPath }/assets/css/board.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"/>
		<div id="content">
			<div id="board">
				<form id="search_form" action="${pageContext.servletContext.contextPath }/board" method="post">
					<input type="text" id="kwd" name="kwd" value="${keyword }">
					<input type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach items="${list }" var="vo" varStatus="status">			
						<tr>
							<td>${topViewNo - status.index }</td>
							<td style="text-align: left; padding-left: ${20*vo.depth }px">
								<c:if test="${vo.depth > 0 }">
									<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
								</c:if>
								<a href="${pageContext.servletContext.contextPath }/board/view?n=${vo.no }&kwd=${keyword }">${vo.title }</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							
							<sec:authorize access="isAuthenticated()">
								<sec:authentication property="principal" var="authUser"/>
								<c:if test="${vo.userName eq authUser.name }">
									<td><a href="${pageContext.servletContext.contextPath }/board/delete?n=${vo.no }" class="del">
									<img src="${pageContext.servletContext.contextPath }/assets/images/recycle.png">
									</a></td>
								</c:if>
							</sec:authorize>
						</tr>
					</c:forEach>
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<c:choose>
							<c:when test="${prevPage <= 1 }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevPage - 1 }&kwd=${keyword }">◀</a></li>
							</c:otherwise>
						</c:choose>
						<c:forEach begin="0" end="${pagerSize - 1 }" var="i">
						<c:choose>
							<c:when test="${currentPage == (prevPage + i) }">
								<li class="selected">${prevPage + i }</li>	
							</c:when>
							<c:when test="${prevPage + i > endPage }">
								<li>${prevPage + i }</li>	
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevPage + i }&kwd=${keyword }">${prevPage + i }</a></li>
							</c:otherwise>
						</c:choose>
						</c:forEach>
						
						<c:choose>
							<c:when test="${prevPage + pagerSize -1 > endPage - 1 }">
								<li>▶</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevPage + pagerSize }&kwd=${keyword }">▶</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<sec:authorize access="isAuthenticated()">
					<div class="bottom">
						<a href="${pageContext.servletContext.contextPath }/board/write" id="new-book">글쓰기</a>
					</div>		
				</sec:authorize>	
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>