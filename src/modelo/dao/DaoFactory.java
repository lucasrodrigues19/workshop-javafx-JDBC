package modelo.dao;

import com.mysql.jdbc.Connection;

import db.ex.DBException;
import modelo.dao.impl.DepartamentoDaoJDBC;
import modelo.dao.impl.VendedorDaoJDBC;

public class DaoFactory {

	/**
	 * Retorna um objeto que implementa VendedorDAO
	 * @param conn
	 * @return
	 */
	public static VendedorDAO getVendedorDAO(Connection conn) {
		if(conn == null)
			throw new DBException("Conexao nula");
		
		return new VendedorDaoJDBC(conn);
	}
	/**
	 * Retorna um objeto que implementa DepartamentoDAO
	 * @param conn
	 * @return
	 */
	public static DepartamentoDAO getDepartamentoDAO(Connection conn) {
		if(conn == null)
			throw new DBException("Conexao nula");
		
		return new DepartamentoDaoJDBC(conn);
		}
}
