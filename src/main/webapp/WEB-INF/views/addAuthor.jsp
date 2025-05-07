<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="common/header.jsp" />

<div class="creator-registration-container">
    <h2 class="section-heading">Creator Registration Portal</h2>
    
    <div class="instructions-panel">
        <p>Enter the information about the literary creator below. This information will be used across the system to associate works with their creators.</p>
    </div>

    <form:form action="/authors/add" method="post" modelAttribute="author" cssClass="creator-form">
        <div class="form-field">
            <form:label path="name">Creator's Full Name <span class="required-marker">*</span></form:label>
            <form:input path="name" required="true" placeholder="Enter the creator's complete name" cssClass="input-control" />
            <span class="field-guidance">Use the official name as it appears in published works</span>
        </div>
        
        <div class="form-field">
            <form:label path="bio">Biographical Information</form:label>
            <form:textarea path="bio" rows="6" placeholder="Enter biographical details, notable works, awards, etc." cssClass="textarea-control" />
            <span class="field-guidance">Include relevant information about the creator's background, career highlights, and significant contributions</span>
        </div>
        
        <div class="action-row">
            <button type="submit" class="btn btn-success">Register Creator Profile</button>
            <a href="<c:url value='/authors' />" class="btn">Return to Directory</a>
        </div>
    </form:form>
    
    <div class="supplementary-info">
        <h4>Why Register Creators?</h4>
        <p>Properly registering creators in our system helps maintain accurate attribution of works and enables better cataloging and search functionality throughout the platform.</p>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const nameInput = document.querySelector('input[name="name"]');
        nameInput.focus();
        
        const form = document.querySelector('.creator-form');
        form.addEventListener('submit', function(event) {
            if (!nameInput.value.trim()) {
                event.preventDefault();
                alert('Creator name is required!');
                nameInput.focus();
            }
        });
    });
</script>

<jsp:include page="common/footer.jsp" />