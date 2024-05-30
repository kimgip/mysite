<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
				<form id="search_form" action="" method="post">
					<input type="text" id="kwd" name="kwd" value="">
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
							<td>${_1viewNo - status.index }</td>
							<td style="text-align: left; padding-left: ${20*vo.depth }px">
								<c:if test="${vo.depth > 0 }">
									<img src="${pageContext.servletContext.contextPath }/assets/images/reply.png">
								</c:if>
								<a href="${pageContext.servletContext.contextPath }/board?a=viewform&no=${vo.no }">${vo.title }</a>
							</td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							
							<c:if test="${vo.userName eq authUser.name }">
								<td><a href="${pageContext.servletContext.contextPath }/board?a=delete&no=${vo.no }" class="del">삭제</a></td>
							</c:if>
						</tr>
					</c:forEach>
				</table>
				
				<!-- pager 추가 -->
				<div class="pager">
					<ul>
						<c:choose>
							<c:when test="${prevP <= 1 || empty param.p }">
								<li>◀</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevP - 1 }">◀</a></li>
							</c:otherwise>
						</c:choose>
						<c:forEach begin="0" end="${pagerCnt - 1 }" var="i">
						<c:choose>
							<c:when test="${param.p == (prevP + i) || (empty param.p && i == 0) }">
								<li class="selected">${prevP + i }</li>	
							</c:when>
							<c:when test="${prevP + i > endP }">
								<li>${prevP + i }</li>	
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevP + i }">${prevP + i }</a></li>
							</c:otherwise>
						</c:choose>
						</c:forEach>
						
						<c:choose>
							<c:when test="${prevP + pagerCnt -1 > endP - 1 }">
								<li>▶</li>
							</c:when>
							<c:otherwise>
								<li><a href="${pageContext.servletContext.contextPath }/board?p=${prevP + pagerCnt }">▶</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>					
				<!-- pager 추가 -->
				
				<c:if test="${not empty authUser }">
					<div class="bottom">
						<a href="${pageContext.servletContext.contextPath }/board?a=writeform" id="new-book">글쓰기</a>
					</div>		
				</c:if>		
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"/>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"/>
	</div>
</body>
</html>