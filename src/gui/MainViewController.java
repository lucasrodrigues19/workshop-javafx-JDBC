package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import ex.MyException;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Classe usada para da suporte aos eventos e modificações na GUI gerada a
 * partir do documento MainView.fxml
 * 
 * @author lucas.rodrigues
 *
 */
public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemSobre;

	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println(menuItemVendedor.getText());
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println(menuItemDepartamento.getText());
	}

	@FXML
	public void onMenuItemSobreAction() throws MyException {
		try {
			loadView("/gui/About.fxml");
		} catch (MyException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Carrega uma View e preserva os filhos da MainView
	 * 
	 * @param nomeAbsoluto Caminho da View para ser carregada
	 * @throws MyException
	 */
	private synchronized void loadView(String nomeAbsoluto) throws MyException { // synchronized garante que o
																					// processamento não seja
																					// interrupido durante mult-treading

		if (nomeAbsoluto == null || "".equals(nomeAbsoluto))
			throw new MyException("Parametro nulo");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox newVBox = loader.load();
			Scene mainScane = Main.getMainScene();

			// pega o primeiro elemento do mainScene(no caso o ScrollPane)
			VBox mainVBox = (VBox) ((ScrollPane) mainScane.getRoot()).getContent(); // referencía o que esta dentro do
																					// content;

			// referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0); // pega o primeiro filho da janela principal

			// limpa os filhos do vBox
			mainVBox.getChildren().clear();

			// adc os nodes
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren()); // adc todos os filhos do newVBOX

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Error", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
			throw new MyException(e.getMessage());
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
