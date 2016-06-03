package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
public class addseat extends HttpServlet {
private static final long serialVersionUID = 1L;
@Resource(name = "jdbc/bookseat")
private DataSource jdbcTest;
private Connection conn;
public void init() {
	
try {
conn = jdbcTest.getConnection();
} catch (Exception ex) {
System.out.println(ex);
}
}
protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
response.setContentType("text/html;charset=UTF-8");
PrintWriter out = response.getWriter();
out.println("<html>");
out.println("<head>");
out.println("<title>Add a new book</title>");
out.println("</head>");
out.println("<body>");
out.println("<h1> Add a new book </h1>");
try {
String customerinfo = request.getParameter("name");
customerinfo = customerinfo.concat(" "+request.getParameter("tel"));
customerinfo = customerinfo.concat(" "+request.getParameter("email"));
String data1 = request.getParameter("count");
int count = Integer.parseInt(data1);
int numofseat=0;
String[] seat = new String[count];
for (int i=0 ;i<count;i++){
	if(request.getParameter("seat"+i)!=null){
		seat[numofseat] = request.getParameter("seat"+i);
		
		numofseat++;
	}
}
Statement stmt = conn.createStatement();
String sql = "INSERT INTO `bookseat`.`bookseat` (`customerinfo`) VALUES('"+ customerinfo + "')";
int numRow = stmt.executeUpdate(sql);

String query ="SELECT * FROM `bookseat`.`bookseat` where customerinfo = '"+customerinfo+"'";
// Send query to database and store results.
ResultSet resultSet = stmt.executeQuery(query);
int idbook=0;
while(resultSet.next()) {
	 idbook = resultSet.getInt("idbook");
  }
for (int i=0 ;i<numofseat;i++){
	out.println("seat"+i+" = "+seat[i]);
	stmt = conn.createStatement();
	sql = "INSERT INTO `bookseat`.`seat_bookseat` (`idbook`,`seat`) VALUES('" + idbook + "','" + seat[i] + "')";
	numRow = stmt.executeUpdate(sql);
}

RequestDispatcher obj =request.getRequestDispatcher("thankyou.html");
if (numRow == 1 && obj != null) {
obj.forward(request, response);
}
} catch (SQLException ex) {
out.println("Error " + ex);
return;
}
out.println("</body>");
out.println("</html>");
out.close();
}
protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
processRequest(request, response);
}
protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
processRequest(request, response);
}
}