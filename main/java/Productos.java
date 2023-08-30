import java.sql.*;
import java.util.Scanner;

public class Productos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("***SISTEMA DE GESTIÓN DE USUARIOS***");

        System.out.print("Ingresa tu email: ");
        String email = scanner.nextLine();

        System.out.print("Ingresa tu password: ");
        String password = scanner.nextLine();


        if (email.equals("") || password.equals("")) {
            System.out.println("Faltan ingresar credenciales correctamente.");
        } else {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/usuarios";
            String username = "root";
            String pass = "";

            try {
                Class.forName(driver);
                Connection connection = DriverManager.getConnection(url, username, pass);

                String consultaSQL = "SELECT * FROM usuario WHERE email = ? AND password = ?";

                PreparedStatement statement = connection.prepareStatement(consultaSQL);
                statement.setString(1, email);
                statement.setString(2, password);


                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                // Procesar el resultado si existe
                if (resultSet.next()) {
                    String usuario = resultSet.getString("email");
                    String contra = resultSet.getString("password");
                    String tipo = resultSet.getString("tipo");

                    if (tipo.equals("administrador")){
                        System.out.println("Bienvenido a Nuestro sistema de registro Administrador");
                        System.out.println();
                        System.out.println("Desear registrar o eliminar algun producto? ");
                        String respuesta = scanner.nextLine();
                        while (respuesta.equals("registrar")){

                            System.out.print("Ingrese codigo del producto: ");
                            String codigo = scanner.nextLine();

                            System.out.print("Ingrese nombre del producto: ");
                            String nombre = scanner.nextLine();

                            System.out.print("Ingrese cantidad disponible: ");
                            String cantidad = scanner.nextLine();

                            System.out.print("Ingrese su valor: ");
                            String valor = scanner.nextLine();

                            System.out.print("Ingrese su descripcion: ");
                            String descripcion = scanner.nextLine();

                            String driver2 = "com.mysql.cj.jdbc.Driver";
                            String url2 = "jdbc:mysql://localhost:3306/equipos";
                            String username2 = "root";
                            String pass2 = "";

                            Class.forName(driver2);
                            Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

                            Statement statement2 = connection2.createStatement();
                            ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM productos");//TABLA


                            Insert(codigo, nombre, cantidad, valor, descripcion, connection2);
                            connection2.close();
                            statement2.close();
                            resultSet2.close();

                            System.out.println("Desear registrar algun otro producto? ");
                            respuesta = scanner.nextLine();

                        }
                        while (respuesta.equals("eliminar")){
                            System.out.println("Que codigo de producto deseas eliminar? ");
                            String producto_cod = scanner.nextLine();

                            String driver2 = "com.mysql.cj.jdbc.Driver";
                            String url2 = "jdbc:mysql://localhost:3306/equipos";
                            String username2 = "root";
                            String pass2 = "";

                            Class.forName(driver2);
                            Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

                            String sentenciaSQL = "DELETE FROM productos WHERE codigo = ?";
                            PreparedStatement prepareStatement = connection2.prepareStatement(sentenciaSQL);
                            prepareStatement.setString(1, producto_cod);
                            prepareStatement.executeUpdate();

                            System.out.println("Producto eliminado correctamente");
                            System.out.println("Deseas eliminar algun otro producto? ");
                            respuesta = scanner.nextLine();
                        }
                    }else if(tipo.equals("estandar")){
                        String driver2 = "com.mysql.cj.jdbc.Driver";
                        String url2 = "jdbc:mysql://localhost:3306/equipos";
                        String username2 = "root";
                        String pass2 = "";

                        Class.forName(driver2);
                        Connection connection2 = DriverManager.getConnection(url2, username2, pass2);

                        System.out.println("Deseas consultar todos los registros o uno?");
                        String consulta = scanner.nextLine();
                        if (consulta.equals("todos")){
                            Statement statement2 = connection2.createStatement();

                            ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM productos");

                            while(resultSet2.next()){
                                String codigo = resultSet2.getString("codigo");
                                String nombre = resultSet2.getString("nombre");
                                String cantidad = resultSet2.getString("cantidad");
                                String valor = resultSet2.getString("valor");
                                String descripcion = resultSet2.getString("descripcion");
                                System.out.println("este es el codigo "+codigo+  "nombre "+nombre+ "cantidad "+cantidad);
                                System.out.println("valor "+valor+ " descripcion "+ descripcion);
                            }
                        }else if(consulta.equals("uno")){
                            System.out.println("Ingrese codigo de consulta ");
                            String codigo_consulta = scanner.nextLine();
                            String consultaSQLselect = "SELECT * FROM productos WHERE codigo = ?";

                            PreparedStatement statement3 = connection2.prepareStatement(consultaSQLselect);
                            statement3.setString(1, codigo_consulta);

                            ResultSet resultSet2 = statement3.executeQuery();
                            while (resultSet2.next()){
                                String nombre_consultado = resultSet2.getString("nombre");
                                System.out.println("Este es el nombre del producto consultado "+ nombre_consultado);
                            }
                        }
                    }

                }
            }catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    public static void Insert(String codigo, String nombre, String cantidad, String valor, String descripcion, Connection connection){

        try {
            // Sentencia INSERT
            String sql = "INSERT INTO productos (codigo, nombre, cantidad, valor, descripcion) VALUES (?, ?, ?, ?, ?)";
            System.out.println(codigo);
            // Preparar la sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, codigo);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, cantidad);
            preparedStatement.setString(4, valor);
            preparedStatement.setString(5, descripcion);

            // Ejecutar la sentencia
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Poducto " + nombre + " agregado exitosamente.");
            } else {
                System.out.println("No se pudo agregar el producto.");
            }

            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}