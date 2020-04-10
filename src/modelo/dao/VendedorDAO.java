package modelo.dao;

import java.util.List;

import modelo.entites.Departamento;
import modelo.entites.Vendedor;

public interface VendedorDAO {
	void inserir(Vendedor obj);

	void atualizar(Vendedor obj);

	void deletarPorID(Integer id);

	Vendedor pesquisarPorID(Integer id);

	List<Vendedor> pesquisarTodos();

	List<Vendedor> pesquisarPorDepartamento(Departamento dpt);
}
