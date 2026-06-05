<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ToneMarket - Login</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 300px; }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; color: #666; }
        input[type="email"], input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #28a745; border: none; color: white; font-size: 16px; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #218838; }
        .error-message { color: red; text-align: center; margin-bottom: 15px; font-weight: bold; }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Accedi a ToneMarket</h2>
    
    <%-- Recupera l'eventuale messaggio di errore inviato dalla Servlet --%>
    <% 
        String errore = (String) request.getAttribute("erroreLogin"); 
        if (errore != null) { 
    %>
        <div class="error-message"><%= errore %></div>
    <% 
        } 
    %>

    <%-- Il form punta alla Servlet mappata su /Login --%>
    <form action="Login" method="POST">
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required placeholder="Inserisci la tua email">
        </div>
        
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required placeholder="Inserisci la password">
        </div>
        
        <button type="submit">Accedi</button>
    </form>
</div>

</body>
</html>