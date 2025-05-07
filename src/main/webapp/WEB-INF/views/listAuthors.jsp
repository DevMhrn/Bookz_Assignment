<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<div class="creator-directory-container">
    <div class="directory-header">
        <h2 class="section-heading">Creator Directory</h2>
        <div class="directory-controls">
            <a href="<c:url value='/authors/add' />" class="btn">
                <i class="icon-user-plus"></i> Register New Creator
            </a>
        </div>
    </div>
    
    <div class="filter-container">
        <input type="text" id="authorFilter" class="search-input" placeholder="Filter by name or bio...">
    </div>

    <div class="directory-grid">
        <table class="grid-table creator-table">
            <thead>
                <tr>
                    <th>Reference ID</th>
                    <th>Creator Name</th>
                    <th>Biography Summary</th>
                    <th>Administrative Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="creator" items="${authors}">
                    <tr class="creator-entry">
                        <td>${creator.id}</td>
                        <td class="creator-name">${creator.name}</td>
                        <td class="creator-bio">
                            <div class="truncated-text">${creator.bio}</div>
                        </td>
                        <td>
                            <div class="admin-controls">
                                <a href="<c:url value='/authors/update/${creator.id}' />" class="btn">
                                    <i class="icon-edit"></i> Edit Profile
                                </a>
                                <a href="<c:url value='/authors/works/${creator.id}' />" class="btn btn-secondary">
                                    <i class="icon-books"></i> View Works
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Filter functionality
        const filterInput = document.getElementById('authorFilter');
        const creatorRows = document.querySelectorAll('.creator-entry');
        
        filterInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            
            creatorRows.forEach(row => {
                const name = row.querySelector('.creator-name').textContent.toLowerCase();
                const bio = row.querySelector('.creator-bio').textContent.toLowerCase();
                
                if (name.includes(searchTerm) || bio.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
        
        // Truncate long bio texts
        const bioTexts = document.querySelectorAll('.truncated-text');
        bioTexts.forEach(text => {
            if (text.textContent.length > 100) {
                const truncated = text.textContent.substring(0, 100) + '...';
                text.textContent = truncated;
                
                text.addEventListener('click', function() {
                    this.textContent = this.textContent.length <= 103 ? 
                        creator.bio : truncated;
                });
            }
        });
    });
</script>

<jsp:include page="common/footer.jsp" />