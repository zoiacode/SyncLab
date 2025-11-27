package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoConnection {
    private Connection conexao;
    
    public DaoConnection() {
        conexao = null;
    }

    public Connection getConnection() {
        return this.conexao;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "switchyard.proxy.rlwy.net";
        String mydatabase = "railway";
        int porta = 57176;
        String url = "jdbc:postgresql://"+ serverName +":"+porta+"/"+ mydatabase;
        String username = "postgres";
        String password = "mTSPJepZVfitbqLfuqtuhqMlyNnckyUE";
        boolean status = false;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            conexao.setAutoCommit(true);
            status = (conexao == null);
        } catch (ClassNotFoundException e) {
            System.out.println("Conexão Não efetuada com o postgres -- driver not founded" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Conexão Não efetuada com o postgres -- " + e.getMessage());
        }

        return status;
    }

    public boolean close() {
            boolean status = false;
            
            try{
                conexao.close();
                status = true;
            }catch(SQLException e) {
                throw new RuntimeException(e);
            }
            return status;
    }

}


