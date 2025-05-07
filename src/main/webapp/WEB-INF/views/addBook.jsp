<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="common/header.jsp" />

<h2>Add New Book</h2>

<form:form action="/books/add" method="post" modelAttribute="book">
    <div class="form-group">
        <form:label path="title">Title</form:label>
        <form:input path="title" required="true" />
    </div>
    <div class="form-group">
        <form:label path="isbn">ISBN</form:label>
        <form:input path="isbn" required="true" />
    </div>
    <div class="form-group">
        <form:label path="author">Author</form:label>
        <form:select path="author" required="true">
            <form:option value="" label="-- Select Author --" />
            <form:options items="${authors}" itemValue="id" itemLabel="name" />
        </form:select>
    </div>
    <button type="submit" class="btn">Save Book</button>
    <a href="<c:url value='/books' />" class="btn">Cancel</a>
</form:form>

<jsp:include page="common/footer.jsp" />