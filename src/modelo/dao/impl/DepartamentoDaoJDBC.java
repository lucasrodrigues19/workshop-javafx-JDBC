package modelo.dao.impl;
import modelo.dao.DepartamentoDAO;
import modelo.entites.Departamento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

import db.DB;
import db.ex.DBException;


public class DepartamentoDaoJDBC implements DepartamentoDAO {

	private Connection conn;
	private String sql;

	public DepartamentoDaoJDBC(Connection conn) {
		if (conn == null)
			throw new DBException("Conexao nula");

		this.conn = conn;
	}

	@Override
	public void inserir(Departamento obj) {
		if (obj == null)
			throw new DBException("departamento nulo");

		sql = "insert into departamento VALUES (default,?);";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			st = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			int rows = st.executeUpdate();
			if (rows > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next())
					obj.setId(rs.getInt(1));
			} else {
				throw new DBException("Nenhuma linha afetada: ");
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				e.printStackTrace();
				throw new DBException("erro na transancao: " + e.getMessage());
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new DBException("Erro no rollback: " + e1.getMessage());
			}
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	@Override
	public void atualizar(Departamento obj) {
		if (obj == null)
			throw new DBException("Id nulo");

		sql = "update departamento set nome = ? where id = ?";
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = (PreparedStatement) conn.prepareStatement(sql);
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			int rows = st.executeUpdate();
			if (rows == 0)
				throw new DBException("Nenhuma linha afetada!");

			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
				e.printStackTrace();
				throw new DBException("erro na transancao: " + e.getMessage());
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new DBException("Erro no rollback: " + e1.getMessage());
			}
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorID(Integer id) {
		if (id == null)
			throw new DBException("Id nulo");

		sql = "delete from departamento where id = ?";
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
				throw new DBException("erro na transancao: " + e.getMessage());
			} catch (SQLException e1) {
				e.printStackTrace();
				throw new DBException("Erro no rollback: " + e1.getMessage());
			}
		} finally {
			DB.fecharStatement(st);
		}

	}

	@Override
	public Departamento pesquisarPorID(Integer id) {
		if (id == null)
			throw new DBException("Id nulo");

		sql = "select * from departamento where id = ?";
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = (PreparedStatement) conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.first())
				return instanciarDepartamentoRs(rs);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
		return null;

	}

	@Override
	public List<Departamento> pesquisarTodos() {

		sql = "select * from departamento";
		Statement st = null;
		ResultSet rs = null;
		List<Departamento> result = new ArrayList<Departamento>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				result.add(instanciarDepartamentoRs(rs));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}

	// instancia um Departamento
	private Departamento instanciarDepartamentoRs(ResultSet rs) throws SQLException {
		if (rs == null)
			throw new SQLException();

		Departamento dpt = new Departamento();
		dpt.setId(rs.getInt("id"));
		dpt.setName(rs.getString("nome"));
		return dpt;
	}
}