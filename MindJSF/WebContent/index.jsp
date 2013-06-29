<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE>
<html>
<head>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Mind Management</title>
<link rel="stylesheet" href="css/2mstyle.css">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/bootstrap-responsive.css" rel="stylesheet">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <link rel="stylesheet" href="css/2mstyleIE8.css"></link>
    <![endif]-->

<style>
</style>


</head>
<body>
	<f:view>

		<div id="wrap">
			<div class="container">
				<div class="navbar navbar-inverse navbar-fixed-top">
					<div class="navbar-inner">
						<div class="container-fluid">
							<button type="button" class="btn btn-navbar"
								data-toggle="collapse" data-target=".nav-collapse">
								<span class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
							<h:form>

								<h:commandLink styleClass="brand">
									<img src="img/logo.png">
								</h:commandLink>

								<div class="nav-collapse collapse">

									<ul class="nav">
										<h:panelGroup
											rendered="#{autorizacionController.programador }">
											<li><h:commandLink
													action="#{indexController.irAProcesos }"
													style="color:white; " value="Procesos"></h:commandLink></li>
										</h:panelGroup>
										<h:panelGroup rendered="#{autorizacionController.cliente }">
											<li><h:commandLink
													action="#{indexController.irAPruebas }" value="Pruebas"
													style="color:white;"></h:commandLink></li>
										</h:panelGroup>
										<h:panelGroup
											rendered="#{autorizacionController.programador }">
											<li><h:commandLink
													action="#{indexController.irAEvaluados }" value="Evaluados"
													style="color:white;"></h:commandLink></li>
										</h:panelGroup>
										<h:panelGroup rendered="#{autorizacionController.cliente }">
											<li><h:commandLink
													action="#{indexController.irAProgramadores }"
													value="Programadores" style="color:white;"></h:commandLink></li>
										</h:panelGroup>
										<h:panelGroup rendered="#{autorizacionController.maestro }">
											<li><h:commandLink
													action="#{indexController.irAClientes }" value="Clientes"
													style="color:white;"></h:commandLink></li>
										</h:panelGroup>
									</ul>
									<ul class="navbar-text pull-right">
										<div class="btn-group" id="barraUsuario">
											<h:commandLink styleClass="btn btn-inverse"
												style="margin-top:5px;"
												action="#{indexController.irACuenta }">
												<i class="icon-user icon-white"></i>
												<h:outputText value="#{homeController.nombreUsuario}"></h:outputText>
											</h:commandLink>
											<h:commandLink styleClass="btn btn-danger"
												style="margin-top:5px;"
												action="#{indexController.cerrarSesion }">
												<i class="icon-remove icon-white"></i>
											</h:commandLink>
										</div>
									</ul>
								</div>
							</h:form>
							<!--/.nav-collapse -->
						</div>
					</div>
				</div>
				<!--Cierro Navegador-->
				<div id="contenedorHome" style="height: 500px;">

					<div class="homeShow" id="programarProcesoPD">
						<!--Pone aqui los eventos Listeners-->
						<img src="img/proceso.png">
						<h2>Proceso</h2>
						<div class="descripcionP">
							<p>Aqu� crear� y programar� la hora, fecha, los evaluados y
								los temas que desea asignar para la evaluaci�n</p>
						</div>
						<div class="dashBots">
							<h:form>
								<h:commandLink styleClass="btn btn-primary" style="width: 70px;"
									value="Procesos" action="#{indexController.irAProcesos }"></h:commandLink>
							</h:form>
						</div>
					</div>
					<h:panelGroup rendered="#{autorizacionController.cliente }">
						<div class="homeShow" id="programarProcesoPD">
							<!--Pone aqui los eventos Listeners-->
							<img src="img/prueba.png">
							<h2>Prueba</h2>
							<div class="descripcionP">
								<p>Crea y personaliza tus temas para cualquier situaci�n que
									necesites con pocos pasos.</p>
							</div>
							<div class="dashBots">
								<h:form>
									<h:commandLink styleClass="btn btn-primary"
										style="width: 70px;" value="Pruebas"
										action="#{indexController.irAPruebas }"></h:commandLink>
								</h:form>
							</div>
						</div>
					</h:panelGroup>
					<div class="homeShow" id="programarProcesoPD">
						<!--Pone aqui los eventos Listeners-->
						<img src="img/evaluados.png">
						<h2>Evaluados</h2>
						<div class="descripcionP">
							<p>Aqu� se gestiona la creaci�n y edici�n de informaci�n para
								todos los evaluados.</p>
						</div>
						<div class="dashBots">
							<h:form>
								<h:commandLink styleClass="btn btn-primary" style="width: 70px;"
									value="Evaluados" action="#{indexController.irAEvaluados }"></h:commandLink>
							</h:form>
						</div>
					</div>
					<hr>
				</div>
			</div>
			<hr>
			<div class="container">
				<footer style="clear: both; margin-top: 20px;">
					<p>&copy; Mind Management 2013</p>
				</footer>
			</div>


		</div>

	</f:view>
	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.js"></script>
	<script src="js/bootstrap-transition.js"></script>
	<script src="js/bootstrap-alert.js"></script>
	<script src="js/bootstrap-modal.js"></script>
	<script src="js/bootstrap-dropdown.js"></script>
	<script src="js/bootstrap-scrollspy.js"></script>
	<script src="js/bootstrap-tab.js"></script>
	<script src="js/bootstrap-tooltip.js"></script>
	<script src="js/bootstrap-popover.js"></script>
	<script src="js/bootstrap-button.js"></script>
	<script src="js/bootstrap-collapse.js"></script>
	<script src="js/bootstrap-carousel.js"></script>
	<script src="js/bootstrap-typeahead.js"></script>

</body>
</html>