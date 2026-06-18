<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="header_admin.jsp" />

<main class="admin-dashboard-container">

    <h1 class="admin-title">Pannello di Controllo</h1>

    <section class="admin-grid-buttons">
        <a href="${pageContext.request.contextPath}/Admin/GestioneProdotti" class="btn-admin-box">
            Gestisci Catalogo
        </a>
        <a href="${pageContext.request.contextPath}/GestioneOrdini" class="btn-admin-box">
            Gestisci Ordini
        </a>
        <a href="${pageContext.request.contextPath}/jsp/admin/ordiniAdmin.jsp" class="btn-admin-box">
            Gestisci Ticket Assistenza
        </a>
    </section>

</main>

<jsp:include page="/jsp/footer.jsp" />