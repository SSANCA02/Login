package com.javatpoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		if(!request.getParameter("content").matches("[\\w*\\s*]*")){
			request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
			request.getRequestDispatcher("WEB-INF/jsp/render.jsp").forward(request,response);
			return;
		}
		request.getRequestDispatcher("link.html").include(request, response);
		
		/*
         * No existe un metodo para comprobar las entradas del usuario
         * 
         * Hay que sanitizar las entradas y limpiar
         * La mejor manera de sanitizar entradas para prevenir XSS
         * en una aplicacion web es usar whitelist, para que solo pueda entrar letras numeros y espacios
         * 
         * A parte, se ha leido el nombre y la contrasenia y se han almacenado en un string
         * el nombre y la contrasenia estaran expuestos hasta que el recolector de basura pase
         * 
         * Solucion, usar un char[]  password = c . readPassword ( � Enter  your  password :  �) ;
         */
		
		String name=request.getParameter("name");
		char[]  password=request.getParameter("password").toCharArray();
		
		/*
         * No existe un metodo para comprobar otro tipo de contrasenia, 
         * 
         * La contrasenia se encuentra sin cifrar y cualquier usuario podria acceder a ella
         * 
         * por lo que seria
         * recomendable tenerla en la base de datos cifrada y compararla desde alli para que no 
         * se escribiera en claro en el codigo
         * 
         * En la mayoria de las ocasiones en la que te dan una contrasenia por defecto, se recomienda cambiarla a otra mas segura (por ejemplo en caso de los routers) por lo que
		 * estaria bien crear un metodo para poder cambiar la contrasenia a una mas segura y que 
		 * dependa del usuario final, obviamente obligandole a tener unas medidas deseguridad (longitud, mayus y minus, uso de numeros...)
         * lo que se dice una contrasenia completa y fuerte, no un admin123
         */
		
		char[] passQueNoDeberiaEstarEnclaro = ("admin123").toCharArray();
		passQueNoDeberiaEstarEnclaro = cifrarContrasenia(passQueNoDeberiaEstarEnclaro);
		
		password = cifrarContrasenia(password);
		
		if(password.equals("admin123")){
			out.print("You are successfully logged in!");
			out.print("<br>Welcome, "+name);
			
			/*
			 * 
			 * Uso de cookies
			 * 
			 * Se guarda el nombre de usuario en la cookie para identificar 
			 * al usuario para posteriores solicitudes.
			 * 
			 * El  inseguro porque un atacante con acceso a la m�quina del cliente puede obtener 
			 * esta informaci�n directamente en la parte del cliente.
			 * 
			 * En este caso es solo el name, por lo que no hace falta hacer un random de la contrasenia
			 * 
			 */
			
			
			boolean validated = false;
			
			if(request.getCookies()[0]!=null && request.getCookies()[0].getValue()!=null) {
				String[] value = request.getCookies()[0].getValue().split(";");
				if (value.length !=2) {
					out.print("Username or pass error!");
				}
			} else {
				validated = isUserValid(name, password);
				if(!validated) {
					out.print("sorry, username or password error!");
				}
				
				Cookie ck=new Cookie("name",name);
				response.addCookie(ck);
			}
			
			
		}else{
			out.print("sorry, username or password error!");
			request.getRequestDispatcher("login.html").include(request, response);
		}
		
		out.close();
	}

	private boolean isUserValid(String name, char[] password) {
		//aqui realmente se llamaria la base dedatos para saber si el usuario es valido
		
		return true;
	}
	
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
			//e.printStackTrace();
			//ERR01-J. Do not allow exceptions to expose sensitive information
			System.out.print("sorry, username or password error!");
			return null;
		}
		    
		byte[] hash = md.digest(new String(pass).getBytes());
		StringBuffer sb = new StringBuffer();
		    
		for(byte b : hash) {        
			sb.append(String.format("%02x", b));
		}
		    
		return sb.toString().toCharArray();
		
	}
}
