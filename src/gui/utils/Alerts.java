package gui.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * Classe usada para da suporte aos Alerts no sistema
 * @author lucas.rodrigues
 *
 */
public class Alerts {

	public static void showAlert(String titulo, String cabcalho, String conteudo, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(titulo);
		alert.setHeaderText(cabcalho);
		alert.setContentText(conteudo);
		alert.show();
	}
	public static Optional<ButtonType> mostrarConfirmacao(String titulo, String conteudo){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(conteudo);
		
		return alert.showAndWait();
	}
}
