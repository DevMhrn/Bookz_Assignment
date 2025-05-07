<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="common/header.jsp" />

<div class="profile-editor-wrapper">
    <h2 class="section-heading">Creator Profile Modification</h2>
    
    <div class="editor-card">
        <div class="card-header">
            <span class="profile-id">Creator ID: ${author.id}</span>
        </div>
        
        <form:form action="/authors/update" method="post" modelAttribute="author" cssClass="profile-form">
            <form:hidden path="id" />
            
            <div class="form-field">
                <form:label path="name" cssClass="field-label">Creator Identity<span class="required-field">*</span></form:label>
                <form:input path="name" required="true" cssClass="name-field" />
                <span class="field-note">This name will appear across all associated publications</span>
            </div>
            
            <div class="form-field">
                <form:label path="bio" cssClass="field-label">Biographical Context</form:label>
                <form:textarea path="bio" rows="7" cssClass="bio-field" />
                <span class="field-note">Include important details about the creator's life, career, and contributions</span>
                <div class="character-counter">
                    <span id="charCount">0</span> characters
                </div>
            </div>
            
            <div class="form-controls">
                <button type="submit" class="btn btn-primary">Apply Changes</button>
                <a href="<c:url value='/authors' />" class="btn btn-light">Revert Changes</a>
            </div>
        </form:form>
        
        <div class="related-works">
            <h4>Related Publications</h4>
            <p class="note">Changes to this profile will affect all associated publications.</p>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Character counter for bio field
        const bioField = document.querySelector('.bio-field');
        const charCount = document.getElementById('charCount');
        
        function updateCounter() {
            charCount.textContent = bioField.value.length;
        }
        
        bioField.addEventListener('input', updateCounter);
        updateCounter(); // Initial count
        
        // Confirmation before leaving with unsaved changes
        const form = document.querySelector('.profile-form');
        let originalFormData = new FormData(form);
        
        window.addEventListener('beforeunload', function(e) {
            let currentFormData = new FormData(form);
            let isDirty = false;
            
            for (let [key, value] of currentFormData.entries()) {
                if (originalFormData.get(key) !== value) {
                    isDirty = true;
                    break;
                }
            }
            
            if (isDirty) {
                e.preventDefault();
                e.returnValue = '';
            }
        });
    });
</script>

<jsp:include page="common/footer.jsp" />