<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<div class="section-container">
    <div class="page-header-flex">
        <h2 class="section-heading">Literary Collection</h2>
        <div class="action-controls">
            <a href="<c:url value='/books/add' />" class="btn btn-primary">
                <i class="icon-plus"></i> Register New Publication
            </a>
        </div>
    </div>
    
    <div class="filter-row">
        <input type="text" id="bookSearchInput" class="search-input" placeholder="Search by title, ISBN, or author...">
    </div>

    <div class="table-responsive">
        <table class="grid-table book-collection-grid">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Publication Title</th>
                    <th>Reference Code</th>
                    <th>Creator Name</th>
                    <th>Management Options</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="publication" items="${books}">
                    <tr class="book-entry">
                        <td class="id-cell">${publication.id}</td>
                        <td class="title-cell">${publication.title}</td>
                        <td class="code-cell">${publication.isbn}</td>
                        <td class="creator-cell">${publication.author.name}</td>
                        <td class="actions-cell">
                            <div class="button-group">
                                <a href="<c:url value='/books/update/${publication.id}' />" class="btn btn-edit">
                                    <i class="icon-pencil"></i> Modify
                                </a>
                                <a href="<c:url value='/books/details/${publication.id}' />" class="btn">
                                    <i class="icon-eye"></i> View
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <div class="pagination-controls">
        <span class="results-count">Showing ${books.size()} publications</span>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const searchInput = document.getElementById('bookSearchInput');
        
        searchInput.addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const bookRows = document.querySelectorAll('.book-entry');
            
            bookRows.forEach(row => {
                const title = row.querySelector('.title-cell').textContent.toLowerCase();
                const isbn = row.querySelector('.code-cell').textContent.toLowerCase();
                const author = row.querySelector('.creator-cell').textContent.toLowerCase();
                
                if(title.includes(searchTerm) || isbn.includes(searchTerm) || author.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    });
</script>

<jsp:include page="common/footer.jsp" />