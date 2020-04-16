package modelo.service;

import java.util.List;

import db.ex.DBException;
import modelo.dao.VendedorDAO;
import modelo.entites.Vendedor;

public class VendedorService {

	private VendedorDAO venDAO;

	public VendedorService(VendedorDAO venDAO) {
		if (venDAO == null) 
			throw new DBException("objeto dao nulo");
		
		this.venDAO = venDAO;
	}

	public List<Vendedor> pesquisarTodos() {
		try {

			return venDAO.pesquisarTodos();

		} catch (DBException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	public void salvarOuAtualizar(Vendedor dpt) {
		if(dpt == null)
			throw new DBException("vendedor invalido");
		
		if(dpt.getId()==null)
			venDAO.inserir(dpt);
		else
			venDAO.atualizar(dpt);
	}
	public void remover(Vendedor dpt) {
		venDAO.deletarPorID(dpt.getId());
	}
}
