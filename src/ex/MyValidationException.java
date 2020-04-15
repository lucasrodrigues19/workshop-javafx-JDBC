package ex;

import java.util.HashMap;
import java.util.Map;

public class MyValidationException extends RuntimeException {

	private static final long serialVersionUID = 147755334508005008L;

	private Map<String, String> erros = new HashMap<>();

	public MyValidationException(String msg) {
		super(msg);
	}
	
	public Map <String, String> getErros(){
		return erros;
	}
	public void addErros(String nomeCampo, String msgErro) {
		erros.put(nomeCampo, msgErro);
	}
}
