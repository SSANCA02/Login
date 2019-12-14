package com.javatpoint;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		request.getRequestDispatcher("link.html").include(request, response);
		
		/*
         * No existe un metodo para comprobar las entradas del usuario
         * 
         * Hay que:
         * 
         *sanitizar las entradas
         * 
         * 
         * A parte, se ha leido el nombre y la contrasenia y se han almacenado en un string
         * el nombre y la contrasenia estaran expuestos hasta que el recolector de basura pase
         * 
         * Solucion, usar un char[]  password = c . readPassword ( ” Enter  your  password :  ”) ;
         */
		
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		
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
			* dependa del usuario final

         */
		
		if(password.equals("admin123")){
			out.print("You are successfully logged in!");
			out.print("<br>Welcome, "+name);
			
			/*
			 * 
			 * Uso de cookies
			 * 
			 * Se guarda el nombre de usuarioen la cookie para identificar 
			 * al usuario para posteriores solicitudes.
			 * 
			 * El intento de implementar la funcionalidad "recuérdame" es inseguro 
			 * porque un atacante con acceso a la máquina del cliente puede obtener 
			 * esta información directamente en la parte del cliente.
			 * 
			 */
			
			//Cookie ck=new Cookie("name",name);
			//response.addCookie(ck);
			
			if(request.getCookies()[0]!=null && request.getCookies()[0].getValue()!=null) {
				String[] value = request.getCookies()[0].getValue().split(";");
				if (value.length !=2) {
					out.print("Username or pass error!");
				}
			}
			
			
		}else{
			out.print("sorry, username or password error!");
			request.getRequestDispatcher("login.html").include(request, response);
		}
		
		out.close();
	}

}
