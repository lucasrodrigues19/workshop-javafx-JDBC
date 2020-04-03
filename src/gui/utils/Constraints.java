package gui.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Classe usada para da suporte a Regras para os controles de uma "GUI"
 * @author lucas.rodrigues
 *
 */
public class Constraints {

	/**
	 * Metodo usado para que um TextField aceite apenas numeros Inteiros
	 * @param txt
	 * 			TextField
	 */
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs,valorAnterior,novoValor)->{
			if(novoValor != null && !novoValor.matches("\\d*")) {
				txt.setText(valorAnterior);
			}
		});
		
	}
	/**
	 * Metodo usado para determinar um numero de caracteres que pode ser digitado a partir de um TextField
	 * @param txt
	 * 			TextField
	 * @param max
	 * 			Número max de caracteres
	 */
	public static void setTextFielMaxLength(TextField txt, int max) {
		txt.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String valorAnterior, String novoValor) {
				if(novoValor != null && novoValor.length() > max) {
					txt.setText(valorAnterior);
				}
				
			}
		});
	}
	/**
	 * Metodo usado para que um TextField aceite apenas numeros Double
	 * @param txt
	 * 			TextField
	 */
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs,valorAnterior,novoValor)->{
			if(novoValor != null && !novoValor.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(valorAnterior);
			}
		});
		
	}
}
