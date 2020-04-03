package gui.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe usada para da suporte aos Alerts no sistema
 * @author lucas.rodrigues
 *
 */
public class Alerts {

	public static void showAlert(String titulo, String cabcalho, String conteudo, AlertType type) {
		Alert alert = new Alert(type);
		alert.setHeaderText(cabcalho);
		alert.setContentText(conteudo);
		alert.show();
	}
}
