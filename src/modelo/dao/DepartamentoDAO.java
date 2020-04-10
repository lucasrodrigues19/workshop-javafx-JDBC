package modelo.dao;

import java.util.List;

import modelo.entites.Departamento;

public interface DepartamentoDAO {

		void inserir(Departamento obj);

		void atualizar(Departamento obj);

		void deletarPorID(Integer id);

		Departamento pesquisarPorID(Integer id);

		List<Departamento> pesquisarTodos();

}
