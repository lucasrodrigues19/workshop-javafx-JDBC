package modelo.service;

import java.util.ArrayList;
import java.util.List;

import modelo.entites.Departamento;

public class DepartamentoService {

	public List<Departamento> pesquisarTodos() {
		List<Departamento> result = new ArrayList<Departamento>();
		result.add(new Departamento(1, "Livros"));
		result.add(new Departamento(2, "Computadores"));
		result.add(new Departamento(3, "Eletronicos"));
		return result;
	}
}
