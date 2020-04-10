package gui.utils;



import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class WorkUtils {

	/**
	 * Metodo responsavel para acessar o stage de onde meu controle recebeu o evento
	 * @param event Evento que o botao recebeu
	 * @return
	 */
	public static Stage palcoAtual(ActionEvent event ) {
		//a partir do evento vou pegar o Node e o Window(Super classe do Stage)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
