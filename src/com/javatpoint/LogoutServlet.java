package com.javatpoint;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LogoutServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		
		request.getRequestDispatcher("link.html").include(request, response);
		
		/*
		 * 
		 * Uso de cookies
		 * 
		 * Se guarda el nombre de usuario en la cookie para identificar 
		 * al usuario para posteriores solicitudes.
		 * 
		 * El  inseguro porque un atacante con acceso a la máquina del cliente puede obtener 
		 * esta información directamente en la parte del cliente.
		 * 
		 *
		 * En este caso es solo el name, por lo que no hace falta hacer un random de la contrasenia
		 */
		
		Cookie ck=new Cookie("name","");
		ck.setMaxAge(0);
		response.addCookie(ck);
		
	
		
		out.print("you are successfully logged out!");
	}
}
