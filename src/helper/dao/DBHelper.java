package helper.dao;

import modelo.entites.Departamento;
import modelo.entites.Vendedor;

public class DBHelper {

	public static Departamento instanciarDepartamento(Departamento dpt) {
		if(dpt == null)
			return new Departamento();
		
		return dpt;
	}

	public static Vendedor instanciarVendedor(Vendedor ven) {
		if(ven == null) 
			return new Vendedor();
		
		return ven;
	}
}
