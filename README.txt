GITHUB:

https://github.com/SSANCA02/Login

--------------------------------------------------------------------------
I have use it Eclipse Oxygen.2 Release 4.7.2
JRE 1.8
servlet-api.jar from Tomcat 8.5

-------------------------------------------------------------------------
ISSUES (tambien puedes encontrar parte de estos comentarios en el codigo):

En LoginServlet.java:

			String name=request.getParameter("name");  
			String password=request.getParameter("password");  

/***************************
No existe un metodo para comprobar las entradas del usuario en el Login

Hay que:
sanitizar las entradas

A parte, se ha leido el nombre y la contrasenia y se han almacenado en un string
el nombre y la contrasenia estaran expuestos hasta que el recolector de basura pase
Solucion, usar un char[]  password = c.readPassword ( ” Enter  your  password :  ”);
    
******************************/      


			if(password.equals("admin123")){  
			out.print("You are successfully logged in!");  
			out.print("<br>Welcome, "+name);  


/***************************

No existe un metodo para comprobar otro tipo de contrasenia, pero en esto caso solo tenemos una que vale 

La contrasenia se encuentra sin cifrar y cualquier usuario podria acceder a ella
por lo que seria recomendable tenerla en la base de datos cifrada y compararla desde alli para que no  se escribiera en claro en el codigo

En la mayoria de las ocasiones en la que te dan una contrasenia por defecto, se recomienda cambiarla a otra mas segura (por ejemplo en caso de los routers) por lo que
estaria bien crear un metodo para poder cambiar la contrasenia a una mas segura y que 
dependa del usuario final

	/*
	 * La contrasenia relmente se llamaria de bbdd cifrada como dijimos antes
	 * Se utiliza por ejemplo, cifrado con SHA-256 para simular que las contrasenias
	 * se encriptan y se almacenan encriptaas a la bbdd 
	 */

	private char[] cifrarContrasenia(char[] pass) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} 
		catch (NoSuchAlgorithmException e) {		
			e.printStackTrace();
			return null;
		}
		    
		byte[] hash = md.digest(new String(pass).getBytes());
		StringBuffer sb = new StringBuffer();
		    
		for(byte b : hash) {        
			sb.append(String.format("%02x", b));
		}
		    
		return sb.toString().toCharArray();
		
	}

******************************/  

     
             Cookie ck=new Cookie("name",name);  
             response.addCookie(ck);  

/***************************

Se guarda el nombre de usuario en el cookie para identificar al usuario para posteriores solicitudes. El intento de implementar la funcionalidad "recuérdame" es inseguro porque un atacante con acceso a la máquina del cliente puede obtener esta información directamente en el cliente.
******************************/  


Sanitizar el input, para ello se utilizan whitelist, para prevenir el XSS:

		if(!request.getParameter("name").matches("[\\w*\\s*]*")){
			request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
			request.getRequestDispatcher("WEB-INF/jsp/render.jsp").forward(request,response);
			return;
		}

		if(!request.getParameter("password").matches("[\\w*\\s*]*")){
			request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
			request.getRequestDispatcher("WEB-INF/jsp/render.jsp").forward(request,response);
			return;
		}
		
Tambien se puede sanitizar empleando jSoup. La biblioteca jSoup proporciona funcionalidad para limpiar HTML y solo permite ciertas etiquetas en el contenido. Hay que determinar las etiquetas que quieres permitir, y luego buscar una lista blanca que coincida con lo que quieres, en este caso podemos usar la de basic. Luego simplemente pase la lista blanca a la función Jsoup.clean().

String name = Jsoup.clean(request.getParameter("name"), Whitelist.basic());
char[]  password = Jsoup.clean(request.getParameter("password"), Whitelist.basic());

Tambien podriamos controlar el tamanio del name y del password,por ejemplo, que fuera
menor de 20. Simplemente seria un if que controlara que ambos tuvieran menos de 20 y si su length es menor de 20 se imprimiria un error por pantalla 

Ademas de estos errores, el sistema te deja:

Volver a logearte sin haber cerrado sesion
"Cerrar sesion" (te dice que has cerrado una sesion) sin haber empezado una sesion ()

