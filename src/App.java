import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;   
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    
    private static final String URL = "jdbc:mysql://localhost:3306/base_prueba1?serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "brayan"; 

    public static void main(String[] args) {
        System.out.println("Intentando conectar a MySQL...");
        
        try (Connection conectar = DriverManager.getConnection(URL, USUARIO, CONTRASENA)) {
            if (conectar != null) {
                System.out.println("\n=========================================");
                System.out.println("¡CONEXIÓN EXITOSA CON BASE_PRUEBA1!");
                System.out.println("=========================================");
                
                Statement statement = conectar.createStatement();
                
                // -----------------------------------------------------------
                // PASO 1: INSERTAR 3 USUARIOS Y 2 PRODUCTOS
                // -----------------------------------------------------------
                System.out.println("\n🚀 [PASO 1] Insertando datos nuevos...");
                
                // Insertar los 3 usuarios (usando correos nuevos para evitar el error UNIQUE)
                statement.executeUpdate("INSERT INTO usuarios (nombre, correo) VALUES ('Carlos Mendoza', 'carlos@correo.com')");
                statement.executeUpdate("INSERT INTO usuarios (nombre, correo) VALUES ('Diana Restrepo', 'diana@correo.com')");
                statement.executeUpdate("INSERT INTO usuarios (nombre, correo) VALUES ('Julian Torres', 'julian@correo.com')");
                System.out.println("✔️ 3 Usuarios registrados correctamente.");
                
                // Insertar los 2 productos en la tabla 'productos'
                statement.executeUpdate("INSERT INTO productos (nombre, descripcion, precio, stock) VALUES ('Balón de Fútbol', 'Balón profesional número 5', 85000.00, 15)");
                statement.executeUpdate("INSERT INTO productos (nombre, descripcion, precio, stock) VALUES ('Camiseta Deportiva', 'Camiseta transpirable talla M', 45000.00, 20)");
                System.out.println("✔️ 2 Productos registrados correctamente.");
                
                
                // -----------------------------------------------------------
                // PASO 2: CONSULTAR LOS DATOS INICIALES
                // -----------------------------------------------------------
                System.out.println("\n📋 [PASO 2] Mostrando registros iniciales en la base de datos:");
                
                // Consulta de Usuarios
                System.out.println("\n--- TABLA USUARIOS ---");
                ResultSet rsUsuarios = statement.executeQuery("SELECT * FROM usuarios");
                while (rsUsuarios.next()) {
                    System.out.println("ID: " + rsUsuarios.getInt("id") + " | Nombre: " + rsUsuarios.getString("nombre") + " | Correo: " + rsUsuarios.getString("correo"));
                }
                rsUsuarios.close();
                
                // Consulta de Productos
                System.out.println("\n--- TABLA PRODUCTOS ---");
                ResultSet rsProductos = statement.executeQuery("SELECT * FROM productos");
                while (rsProductos.next()) {
                    System.out.println("ID: " + rsProductos.getInt("id") + " | Producto: " + rsProductos.getString("nombre") + " | Precio: $" + rsProductos.getDouble("precio") + " | Stock: " + rsProductos.getInt("stock"));
                }
                rsProductos.close();
                
                
                // -----------------------------------------------------------
                // PASO 3: ACTUALIZAR UN DATO
                // -----------------------------------------------------------
                System.out.println("\n🔄 [PASO 3] Modificando datos...");
                
                // Vamos a actualizar el precio y stock de la 'Camiseta Deportiva' usando su nombre como referencia
                String actualizarProducto = "UPDATE productos SET precio = 48000.00, stock = 18 WHERE nombre = 'Camiseta Deportiva'";
                int prodActualizados = statement.executeUpdate(actualizarProducto);
                
                if (prodActualizados > 0) {
                    System.out.println("✔️ ¡Producto 'Camiseta Deportiva' actualizado con éxito!");
                }
                
                
                // -----------------------------------------------------------
                // PASO 4: ELIMINAR UN DATO
                // -----------------------------------------------------------
                System.out.println("\n❌ [PASO 4] Eliminando un dato de la tabla usuarios...");
                
                // Eliminamos a 'Julian Torres' usando su correo único para asegurar el proceso limpio en la secuencia
                String correoEliminar = "julian@correo.com";
                String eliminarUsuario = "DELETE FROM usuarios WHERE correo = '" + correoEliminar + "'";
                int userEliminados = statement.executeUpdate(eliminarUsuario);
                
                if (userEliminados > 0) {
                    System.out.println("✔️ ¡El usuario con correo '" + correoEliminar + "' ha sido eliminado!");
                }
                
                
                // -----------------------------------------------------------
                // PASO 5: CONSULTA FINAL DE COMPROBACIÓN
                // -----------------------------------------------------------
                System.out.println("\n📋 [PASO 5] REGISTROS FINALES TRAS ACTUALIZAR Y ELIMINAR:");
                
                System.out.println("\n--- TABLA USUARIOS ACTUALIZADA (Ya no debe estar Julian) ---");
                ResultSet rsFinalUsuarios = statement.executeQuery("SELECT * FROM usuarios");
                while (rsFinalUsuarios.next()) {
                    System.out.println("ID: " + rsFinalUsuarios.getInt("id") + " | Nombre: " + rsFinalUsuarios.getString("nombre") + " | Correo: " + rsFinalUsuarios.getString("correo"));
                }
                rsFinalUsuarios.close();
                
                System.out.println("\n--- TABLA PRODUCTOS ACTUALIZADA (Verificar nuevo precio y stock) ---");
                ResultSet rsFinalProductos = statement.executeQuery("SELECT * FROM productos");
                while (rsFinalProductos.next()) {
                    System.out.println("ID: " + rsFinalProductos.getInt("id") + " | Producto: " + rsFinalProductos.getString("nombre") + " | Precio: $" + rsFinalProductos.getDouble("precio") + " | Stock: " + rsFinalProductos.getInt("stock"));
                }
                rsFinalProductos.close();
                
                // Cierre de la herramienta principal de comandos
                statement.close();
                System.out.println("\n🔒 Todos los pasos del CRUD se ejecutaron. Conexiones cerradas correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("\n❌ Ocurrió un error en la ejecución del paso a paso:");
            e.printStackTrace();
        }
    }
}