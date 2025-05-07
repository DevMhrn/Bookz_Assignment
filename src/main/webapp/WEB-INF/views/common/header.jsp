<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BookVerse - Literature Management Portal</title>
    <style>
        :root {
            --main-color: #34495e;
            --highlight-color: #2980b9;
            --contrast-color: #e67e22;
            --bg-neutral: #f5f7fa;
            --font-dark: #2c3e50;
            --font-light: #ecf0f1;
        }
        
        body, html {
            margin: 0;
            padding: 0;
            font-family: 'Arial', sans-serif;
            background-color: var(--bg-neutral);
            color: var(--font-dark);
            line-height: 1.7;
        }
        
        .container {
            width: 90%;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .page-header {
            background: var(--main-color);
            padding: 1.25rem 0;
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.15);
            margin-bottom: 2.5rem;
        }
        
        .portal-title {
            color: var(--font-light);
            text-align: center;
            font-size: 2.2rem;
            font-weight: 400;
            margin: 0;
            letter-spacing: 0.05em;
        }
        
        .main-menu {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            background-color: rgba(0, 0, 0, 0.15);
            padding: 0.7rem 0;
            margin-top: 1.2rem;
        }
        
        .menu-link {
            color: var(--font-light);
            text-decoration: none;
            padding: 0.6rem 1.6rem;
            margin: 0 0.4rem;
            border-radius: 3px;
            transition: all 0.25s ease;
        }
        
        .menu-link:hover {
            background-color: rgba(255, 255, 255, 0.2);
            transform: translateY(-2px);
        }
        
        .main-content {
            background: white;
            padding: 2.5rem;
            border-radius: 6px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            margin-bottom: 2rem;
        }
        
        .section-heading {
            color: var(--main-color);
            margin-bottom: 1.8rem;
            padding-bottom: 0.7rem;
            border-bottom: 3px solid var(--highlight-color);
            font-weight: 500;
        }
        
        .grid-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin: 1.8rem 0;
        }
        
        .grid-table th, .grid-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .grid-table th {
            background-color: var(--main-color);
            color: var(--font-light);
            font-weight: 500;
        }
        
        .grid-table tr:nth-child(even) {
            background-color: rgba(0, 0, 0, 0.02);
        }
        
        .grid-table tr:hover {
            background-color: rgba(41, 128, 185, 0.08);
        }
        
        .btn {
            display: inline-block;
            background: var(--highlight-color);
            color: white;
            padding: 0.6rem 1.2rem;
            border-radius: 4px;
            text-decoration: none;
            border: none;
            cursor: pointer;
            font-size: 0.95rem;
            transition: all 0.2s ease;
            margin-right: 0.5rem;
            margin-bottom: 0.5rem;
        }
        
        .btn:hover {
            background: #1a5276;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        
        .btn-danger {
            background: var(--contrast-color);
        }
        
        .btn-danger:hover {
            background: #d35400;
        }
        
        .form-field {
            margin-bottom: 1.8rem;
        }
        
        .form-field label {
            display: block;
            margin-bottom: 0.6rem;
            font-weight: 500;
            color: var(--main-color);
        }
        
        .form-field input, .form-field textarea, .form-field select {
            width: 100%;
            padding: 0.9rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-family: inherit;
            font-size: 1rem;
            transition: border 0.2s ease;
        }
        
        .form-field input:focus, .form-field textarea:focus, .form-field select:focus {
            border-color: var(--highlight-color);
            outline: none;
        }
        
        .form-field textarea {
            min-height: 140px;
            resize: vertical;
        }
        
        .alert {
            padding: 1.2rem;
            margin-bottom: 1.8rem;
            border-radius: 4px;
            border-left: 4px solid;
        }
        
        .alert-success {
            background-color: #e8f5e9;
            color: #1b5e20;
            border-left-color: #4caf50;
        }
        
        .alert-error {
            background-color: #ffebee;
            color: #b71c1c;
            border-left-color: #f44336;
        }
        
        .search-container {
            display: flex;
            gap: 12px;
            margin-bottom: 1.8rem;
        }
        
        .search-input {
            flex-grow: 1;
            padding: 0.9rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
        }
    </style>
</head>
<body>
    <header class="page-header">
        <div class="container">
            <h1 class="portal-title">BookVerse - Literature Management Portal</h1>
            <nav class="main-menu">
                <a href="<c:url value='/catalog'/>" class="menu-link">Book Collection</a>
                <a href="<c:url value='/catalog/detailed-view'/>" class="menu-link">Comprehensive View</a>
                <a href="<c:url value='/creators'/>" class="menu-link">Authors Gallery</a>
                <a href="<c:url value='/about'/>" class="menu-link">Information</a>
            </nav>
        </div>
    </header>
    <div class="container">
        <div class="main-content">
            <c:if test="${not empty notification}">
                <div class="alert alert-success">${notification}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">${errorMessage}</div>
            </c:if>