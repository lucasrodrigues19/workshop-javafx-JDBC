package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import db.DB;
import ex.MyException;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modelo.dao.DaoFactory;
import modelo.service.DepartamentoService;

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
		try {
			loadView("/gui/DepartamentList.fxml", (DepartamentListController controller) -> {
				controller.setService(
						new DepartamentoService(DaoFactory.getDepartamentoDAO(DB.getConexao("db.properties"))));
				controller.atualizarTableView();
			});
		} catch (MyException e) {
			e.printStackTrace();
			Alerts.showAlert("Error", "Erro ao listar dados da tabela", e.getMessage(), AlertType.ERROR);
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void onMenuItemSobreAction() throws MyException {
		try {

			loadView("/gui/About.fxml", null);
		} catch (MyException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Carrega uma View e preserva os filhos da MainView Executa uma função do tipo
	 * consumer passada como parametro
	 * 
	 * @param <T>
	 * @param nomeAbsoluto caminho da view a ser carregada
	 * @param executar     implementacao da interface consummer
	 * @throws MyException
	 */
	private synchronized <T> void loadView(String nomeAbsoluto, Consumer<T> executar) throws MyException {
		// synchronize garante processamento não seja interrupido durante mult-treading

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

			// executa a funçao passada como parametro(Interface funcional consumer)
			if (executar != null) {
				T controller = loader.getController(); // recebe um controle do tipo T passado como parametro]
				executar.accept(controller); // passa como parametro para executrar a funçao o objeto controler do tipo
												// T (Passado como parametro)
			}
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("Error", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
			throw new MyException(e.getMessage());
		}

	}

	/**
	 * Carrega a MainView
	 * 
	 * @throws MyException
	 */
	private void loadMainView() throws MyException {
		if (Main.getMainScene() == null)
			throw new MyException("Scene invalida! ");

		// peguei a sena
		Scene mainScene = Main.getMainScene();

		// pegar o primeiro elemento
		VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

		// pega o primeiro controle
		Node menuMain = mainVBox.getChildren().get(0);

		// apaga os filhos
		mainVBox.getChildren().clear();

		// adiciona o primeiro item
		mainVBox.getChildren().add(menuMain);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
