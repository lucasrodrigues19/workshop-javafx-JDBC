package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import db.ex.DBException;

public class DB {
	private static Connection con;

	/**
	 * Metodo responsavel para pegar a conexao com o banco de dados
	 * 
	 * @param path
	 * @return
	 */
	public static Connection getConexao(String path) {
		if (con == null) {
			try {
				Properties prop = loadProperties(path);
				String url = prop.getProperty("dburl");
				con = (Connection) DriverManager.getConnection(url, prop);
				System.out.println("Conexao realizada");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DBException("Erro ao conectar " + e.getMessage());
			}
			
		}
		return con;
	}


	/**
	 * Metodo responsavel por fechar conexao com o banco de dados
	 */
	public static void fecharConexao() {
		try {
			if (con == null)
				throw new DBException("Erro ao fechar conexão: você nao possui uma conexao para ser fechada");

			con.close();
			System.out.println("Conexao fechada");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Erro ao fechar conexão: " + e.getMessage());
		}
	}

	/**
	 * Metodo responsavel por fechar o Statment
	 * 
	 * @param statment
	 */
	public static void fecharStatement(Statement st) {
		try {
			if (st == null)
				throw new DBException("Erro ao fechar statment: você nao possui uma statment para ser fechada");

			st.close();
			System.out.println("Statement fechada");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Erro ao fechar Statment: " + e.getMessage());
		}
	}

	/**
	 * Metodo responsavel por fechar o ResultSet
	 * 
	 * @param resultSet
	 */
	public static void fecharResultSet(ResultSet rs) {
		try {
			if (rs == null)
				throw new DBException("Erro ao fechar resultSet: você nao possui um resultSet para ser fechada");

			rs.close();
			System.out.println("resultSet fechada");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Erro ao fechar resultSet: " + e.getMessage());
		}
	}

	/**
	 * Metodo responsavel por ler um .propiertes responsavel pelos dados do banco
	 * 
	 * @param path caminho do arquivo
	 * @return
	 */
	private static Properties loadProperties(String path) {
		try (FileInputStream fs = new FileInputStream(path)) {
			Properties prop = new Properties();
			prop.load(fs);
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DBException("Erro ao carregar propiedades do banco: " + e.getMessage());
		}
	}
}
