<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="common/header.jsp" />

<div class="catalog-view-container">
    <h2 class="section-heading">Comprehensive Literary Catalog</h2>
    
    <div class="catalog-description">
        <p>This view presents a comprehensive listing of all registered literary works along with their respective creators.</p>
    </div>
    
    <div class="catalog-controls">
        <div class="search-wrapper">
            <input type="text" id="catalogSearch" class="search-input" placeholder="Filter catalog entries...">
            <button id="searchButton" class="btn">Search</button>
        </div>
        
        <div class="view-options">
            <button id="gridView" class="btn btn-small active">Grid View</button>
            <button id="listView" class="btn btn-small">List View</button>
        </div>
    </div>
    
    <div class="catalog-container grid-view">
        <table class="grid-table catalog-table">
            <thead>
                <tr>
                    <th class="work-column">Literary Work</th>
                    <th class="identifier-column">Reference Number</th>
                    <th class="creator-column">Literary Creator</th>
                </tr>
            </thead>
            <tbody id="catalogContent">
                <c:forEach var="catalogEntry" items="${booksWithAuthors}">
                    <tr class="catalog-entry">
                        <td class="work-name">${catalogEntry[0]}</td> <!-- Work Title -->
                        <td class="work-identifier">${catalogEntry[1]}</td> <!-- Reference Code -->
                        <td class="work-creator">${catalogEntry[2]}</td> <!-- Creator Name -->
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <div class="catalog-footer">
        <div class="entry-count">
            <span id="visibleCount">${booksWithAuthors.size()}</span> entries displayed
        </div>
        
        <div class="navigation-buttons">
            <a href="<c:url value='/books' />" class="btn btn-secondary">Return to Publication Management</a>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Search functionality
        const searchInput = document.getElementById('catalogSearch');
        const catalogEntries = document.querySelectorAll('.catalog-entry');
        const visibleCountElement = document.getElementById('visibleCount');
        
        function filterCatalog() {
            const searchTerm = searchInput.value.toLowerCase();
            let visibleCount = 0;
            
            catalogEntries.forEach(entry => {
                const title = entry.querySelector('.work-name').textContent.toLowerCase();
                const isbn = entry.querySelector('.work-identifier').textContent.toLowerCase();
                const author = entry.querySelector('.work-creator').textContent.toLowerCase();
                
                if (title.includes(searchTerm) || isbn.includes(searchTerm) || author.includes(searchTerm)) {
                    entry.style.display = '';
                    visibleCount++;
                } else {
                    entry.style.display = 'none';
                }
            });
            
            visibleCountElement.textContent = visibleCount;
        }
        
        searchInput.addEventListener('input', filterCatalog);
        document.getElementById('searchButton').addEventListener('click', filterCatalog);
        
        // View switching
        const gridViewBtn = document.getElementById('gridView');
        const listViewBtn = document.getElementById('listView');
        const catalogContainer = document.querySelector('.catalog-container');
        
        gridViewBtn.addEventListener('click', function() {
            catalogContainer.className = 'catalog-container grid-view';
            gridViewBtn.classList.add('active');
            listViewBtn.classList.remove('active');
        });
        
        listViewBtn.addEventListener('click', function() {
            catalogContainer.className = 'catalog-container list-view';
            listViewBtn.classList.add('active');
            gridViewBtn.classList.remove('active');
        });
    });
</script>

<style>
    .catalog-container.list-view .catalog-table {
        display: block;
    }
    
    .catalog-container.list-view thead {
        display: none;
    }
    
    .catalog-container.list-view tbody {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
    }
    
    .catalog-container.list-view tr {
        display: flex;
        flex-direction: column;
        width: calc(33.33% - 10px);
        background: #f9f9f9;
        border-radius: 5px;
        padding: 15px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }
    
    .catalog-container.list-view td {
        border: none;
        padding: 5px 0;
    }
    
    .catalog-container.list-view .work-name {
        font-weight: bold;
        font-size: 1.1em;
        color: var(--main-color);
    }
    
    .catalog-container.list-view .work-identifier {
        font-size: 0.9em;
        color: #666;
    }
    
    .catalog-container.list-view .work-creator {
        font-style: italic;
        margin-top: 5px;
    }
</style>

<jsp:include page="common/footer.jsp" />