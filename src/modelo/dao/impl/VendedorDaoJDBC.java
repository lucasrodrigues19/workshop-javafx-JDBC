package modelo.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

import db.DB;
import db.ex.DBException;
import helper.dao.DBHelper;
import modelo.dao.VendedorDAO;
import modelo.entites.Departamento;
import modelo.entites.Vendedor;

public class VendedorDaoJDBC implements VendedorDAO {

	private Connection conn;
	private String sql;

	public VendedorDaoJDBC(Connection conn) {
		if (conn == null)
			throw new DBException("Conexao nula");

		this.conn = conn;
	}

	@Override
	public void inserir(Vendedor obj) {
		if (obj == null || obj.getDepartamento() == null)
			throw new DBException("O vendedor esta nulo");

		sql = "insert into vendedor VALUES (default,?,?,?,?,?)";
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);

			st = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getBaseSalario());
			st.setInt(5, obj.getDepartamento().getId());
			int row = st.executeUpdate();
			System.out.println("Linhas inseridas: " + 1);
			if (row > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DBException("Nenhuma linha afetada: ");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw new DBException("Erro na transanção: " + e.getMessage());
			} catch (SQLException er) {
				throw new DBException("Erro no roolback: " + e.getMessage());
			}
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Vendedor obj) {
		if (obj == null)
			throw new DBException("vededor nulo");

		sql = "update vendedor set nome = ?,email = ?,dataNasc = ?,salarioBase = ?,dptId = ? where id = ?";
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = (PreparedStatement) conn.prepareStatement(sql);
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getBaseSalario());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			int rows = st.executeUpdate();
			if (rows == 0)
				throw new DBException("Nenhuma linha afetada!");
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				e.printStackTrace();
				System.out.println("Erro na transanção: " + e.getMessage());
			} catch (SQLException e1) {
				e.printStackTrace();
				System.out.println("Erro no roolback: " + e.getMessage());
			}
		} finally {
			DB.fecharStatement(st);
		}

	}

	@Override
	public void deletarPorID(Integer id) {
		if (id == null)
			throw new DBException("O id esta nulo");

		sql = "delete from vendedor where id = ?";
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = (PreparedStatement) conn.prepareStatement(sql);
			st.setInt(1, id);
			int rows = st.executeUpdate();
			if (rows == 0)
				throw new DBException("Nenhuma linha afetada!");
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				e.printStackTrace();
				throw new DBException("Erro na transanção");
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new DBException("Erro no rollback");
			} finally {
				DB.fecharStatement(st);
			}
		}
	}

	@Override
	public Vendedor pesquisarPorID(Integer id) {
		if (id == null)
			throw new DBException("Id esta nulo");

		sql = "select vendedor.* , departamento.nome from vendedor INNER JOIN departamento on vendedor.dptId = departamento.id where vendedor.id = ?";
		PreparedStatement st = null;
		ResultSet rs = null;
		Vendedor vendedor = null;
		try {
			st = (PreparedStatement) conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.first()) {
				vendedor = instanciarVendedorRs(rs, instanciarDepartamentoRs(rs));
			}
			return vendedor;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> pesquisarTodos() {

		sql = "select vendedor.* , departamento.nome from vendedor INNER JOIN departamento on vendedor.dptId = departamento.id";
		Statement st = null;
		ResultSet rs = null;
		List<Vendedor> result = new ArrayList<Vendedor>();
		Vendedor vendedor = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				vendedor = instanciarVendedorRs(rs, instanciarDepartamentoRs(rs));
				result.add(vendedor);
			}
			return result;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> pesquisarPorDepartamento(Departamento obj) {
		if (obj == null)
			throw new DBException("Id do departamento esta nulo");

		sql = "select vendedor.* , departamento.nome from vendedor INNER JOIN departamento on vendedor.dptId = departamento.id where dptId = ? ORDER BY  vendedor.nome";
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Vendedor> result = new ArrayList<Vendedor>();
		Map<Integer, Departamento> dptMap = new HashMap<Integer, Departamento>();
		Vendedor vendedor = null;
		try {
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, obj.getId());
			rs = ps.executeQuery();
			Departamento dpt = null;
			while (rs.next()) {
				// departamento 1 --- N vendedores.
				// O vendedor aponta apenas para um objeto departamento
				if (dptMap != null && dptMap.size() > 0) {
					if (!dptMap.containsKey(rs.getInt("dptId"))) {
						dpt = dptMap.get(rs.getInt("dptId"));
					}
				} else {
					dpt = instanciarDepartamentoRs(rs);
					dptMap.put(rs.getInt("dptId"), DBHelper.instanciarDepartamento(dpt));
				}

				vendedor = instanciarVendedorRs(rs, DBHelper.instanciarDepartamento(dpt));
				result.add(vendedor);
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharStatement(ps);
			DB.fecharResultSet(rs);
		}

	}

	/**
	 * instancia o Departamento e Vendedor a partir do resultSet
	 * 
	 * @param rs  ResultSet
	 * @param dpt Departamento
	 * @return
	 * @throws SQLException
	 */
	private Vendedor instanciarVendedorRs(ResultSet rs, Departamento dpt) throws SQLException {
		if (rs == null || dpt == null) {
			throw new SQLException();
		}
		Vendedor vendedor = new Vendedor();
		vendedor.setId(rs.getInt("id"));
		vendedor.setNome(rs.getString("nome"));
		vendedor.setDataNasc(rs.getDate("dataNasc"));
		vendedor.setEmail(rs.getString("email"));
		vendedor.setBaseSalario(rs.getDouble("salarioBase"));
		vendedor.setDepartamento(dpt);
		return vendedor;
	}

	/**
	 * instancia um departamento a partir do resultSet
	 * 
	 * @param rs ResultSet
	 * @return
	 * @throws SQLException
	 */
	private Departamento instanciarDepartamentoRs(ResultSet rs) throws SQLException {
		if (rs == null)
			throw new SQLException();

		Departamento dpt = new Departamento();
		dpt.setId(rs.getInt("dptId"));
		dpt.setName(rs.getString("departamento.nome"));
		return dpt;
	}
}