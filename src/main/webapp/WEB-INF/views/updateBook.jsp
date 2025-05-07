<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="common/header.jsp" />

<div class="edit-publication-container">
    <h2 class="section-heading">Modify Publication Details</h2>
    
    <div class="operation-context">
        <div class="context-info">
            <span class="label">Publication ID:</span>
            <span class="value">${book.id}</span>
        </div>
        <div class="context-info">
            <span class="label">Last Updated:</span>
            <span class="value">Current Session</span>
        </div>
    </div>

    <form:form action="/books/update" method="post" modelAttribute="book" cssClass="publication-edit-form">
        <form:hidden path="id" />
        
        <div class="form-field">
            <form:label path="title">Publication Title<span class="mandatory-marker">*</span></form:label>
            <form:input path="title" required="true" cssClass="text-input" />
            <div class="validation-feedback"></div>
        </div>
        
        <div class="form-field">
            <form:label path="isbn">Identification Code<span class="mandatory-marker">*</span></form:label>
            <form:input path="isbn" required="true" cssClass="text-input" />
            <div class="helper-text">Standard ISBN format (10 or 13 digits)</div>
        </div>
        
        <div class="form-field">
            <form:label path="author">Attribution<span class="mandatory-marker">*</span></form:label>
            <form:select path="author" required="true" cssClass="dropdown-input">
                <form:option value="" label="-- Assign to Creator --" />
                <form:options items="${authors}" itemValue="id" itemLabel="name" />
            </form:select>
            <div class="helper-text">Select the primary creator of this work</div>
        </div>
        
        <div class="form-actions-panel">
            <button type="submit" class="btn btn-primary">Save Changes</button>
            <a href="<c:url value='/books' />" class="btn btn-secondary">Discard Changes</a>
        </div>
    </form:form>
    
    <div class="version-info">
        <p>You are editing version 1.0 of this record.</p>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('.publication-edit-form');
        
        form.addEventListener('submit', function(event) {
            const titleInput = document.querySelector('input[name="title"]');
            const isbnInput = document.querySelector('input[name="isbn"]');
            const authorSelect = document.querySelector('select[name="author"]');
            
            let isValid = true;
            
            // Simple validation
            if (!titleInput.value.trim()) {
                document.querySelector('input[name="title"] + .validation-feedback')
                    .textContent = 'Title is required';
                isValid = false;
            }
            
            if (!isbnInput.value.trim()) {
                document.querySelector('input[name="isbn"] + .validation-feedback')
                    .textContent = 'ISBN is required';
                isValid = false;
            }
            
            if (!authorSelect.value) {
                document.querySelector('select[name="author"] + .validation-feedback')
                    .textContent = 'Please select an author';
                isValid = false;
            }
            
            if (!isValid) {
                event.preventDefault();
            }
        });
    });
</script>

<jsp:include page="common/footer.jsp" />