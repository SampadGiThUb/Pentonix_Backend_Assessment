package Assesment;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Task2 
{
    private static Map<Integer, String> departmentDatabase = new HashMap<>();
    private static Map<Integer, Employee> employeeDatabase = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        // Populate the in-memory databases
        departmentDatabase.put(10, "Admin");
        departmentDatabase.put(20, "Accounts");
        departmentDatabase.put(30, "Sales");
        departmentDatabase.put(40, "Marketing");
        departmentDatabase.put(50, "Purchasing");
        
        employeeDatabase.put(1, new Employee(1, "Amal", 10, 30000));
        employeeDatabase.put(2, new Employee(2, "Shyamal", 30, 50000));
        employeeDatabase.put(3, new Employee(3, "Kamal", 40, 10000));
        employeeDatabase.put(4, new Employee(4, "Nirmal", 50, 60000));
        employeeDatabase.put(5, new Employee(5, "Bimal", 20, 40000));
        employeeDatabase.put(6, new Employee(6, "Parimal", 10, 20000));
        
        // Create an HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/api", new DepartmentHandler());
        server.start();
        
        System.out.println("Server started on port 9000...");
    }
    
    static class DepartmentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String[] queryParts = query.split("=");
            
            if (queryParts.length != 2 || !queryParts[0].equals("ENO")) {
                sendResponse(exchange, "Invalid request", 400);
                return;
            }
            
            int eno = Integer.parseInt(queryParts[1]);
            Employee employee = employeeDatabase.get(eno);
            
            if (employee != null) {
                String departmentName = departmentDatabase.get(employee.getDno());
                Department department = new Department(employee.getDno(), departmentName);
                
                sendResponse(exchange, 
                        "Employee Details: ENO: " + employee.getEno()
                        + ", ENAME: " + employee.getEname()
                        + ", DNO: " + employee.getDno()
                        + ", SALARY: " + employee.getSalary()
                        + ", DNAME: " + department.getDname(), 200);
            } else {
                sendResponse(exchange, "Employee not found", 404);
            }
        }
        
        private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    
    public static class Employee 
    {
        private int eno;
        private String ename;
        private int dno;
        private int salary;

        public Employee(int eno, String ename, int dno, int salary) 
        {
            this.eno = eno;
            this.ename = ename;
            this.dno = dno;
            this.salary = salary;
        }

        public int getEno() 
        {
            return eno;
        }

        public String getEname() 
        {
            return ename;
        }

        public int getDno() 
        {
            return dno;
        }

        public int getSalary() 
        {
            return salary;
        }
    }
    public static class Department
    {
        private int dno;
        private String dname;
        
        public Department(int dno, String dname)
        {
            this.dno = dno;
            this.dname = dname;
        }
        
        public int getDno() 
        {
            return dno;
        }
        
        public String getDname() 
        {
            return dname;
        }
    }
}