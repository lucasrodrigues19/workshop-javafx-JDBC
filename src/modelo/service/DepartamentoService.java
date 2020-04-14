package modelo.service;

import java.util.List;

import db.ex.DBException;
import ex.MyException;
import ex.MyRuntimeException;
import modelo.dao.DepartamentoDAO;
import modelo.entites.Departamento;

public class DepartamentoService {

	private DepartamentoDAO dptDAO;

	public DepartamentoService(DepartamentoDAO dptDAO) {
		if (dptDAO == null) 
			throw new DBException("objeto dao nulo");
		
		this.dptDAO = dptDAO;
	}

	public List<Departamento> pesquisarTodos() {
		try {

			return dptDAO.pesquisarTodos();

		} catch (DBException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	public void salvarOuAtualizar(Departamento dpt) {
		if(dpt == null)
			throw new DBException("departamento invalido");
		
		if(dpt.getId()==null)
			dptDAO.inserir(dpt);
		else
			dptDAO.atualizar(dpt);
	}
}
