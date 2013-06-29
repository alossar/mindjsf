<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<title>Login - Mind Management</title>
<style type="text/css">
body {
	padding-top: 70px;
	padding-bottom: 40px;
	background-color: rgb(46, 55, 60);
	font-family: arial;
}

.form-signin {
	max-width: 300px;
	padding: 19px 29px 29px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin input[type="text"],.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin-bottom: 15px;
	padding: 7px 9px;
}
</style>
</head>
<body>
	<f:view>
		<div class="container">
			<center>
				<img src="img/login/login.png" />
			</center>
			<div class="form-signin">
				<h:form>
					<h:outputText value="Correo Electr�nico:"></h:outputText>
					<h:inputText id="correo" required="true"
						styleClass="input-block-level"
						requiredMessage="Ingrese su correo electr�nico."
						validatorMessage="El correo electr�nico no es v�lido."
						value="#{loginController.correo }">
						<f:validateRegex
							pattern="^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$" />
					</h:inputText>
					<p>
						<h:message for="correo" styleClass="alert alert-error" />
					</p>
					<h:outputText value="Contrase�a:"></h:outputText>
					<h:inputSecret id="pass" required="true"
						requiredMessage="Ingrese su contrase�a."
						styleClass="input-block-level" value="#{loginController.pass }"></h:inputSecret>
					<p>
						<h:message for="pass" styleClass="alert alert-error" />
					</p>
					<center>
						<h:commandButton id="ingresar" value="Ingresar"
							action="#{loginController.autenticar }"
							styleClass="btn btn-inverse"></h:commandButton>
					</center>
				</h:form>
				<h:form>
					<center>
						<h:commandLink id="recuperar" value="Recuperar contrase�a" 
							action="#{loginController.irARecuperarContrasena }"
							style="btn btn-link "></h:commandLink>
					</center>
				</h:form>
			</div>
			<center>
				<a></a>
			</center>

		</div>
	</f:view>
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>
</html>