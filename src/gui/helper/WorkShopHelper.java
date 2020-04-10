package gui.helper;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import application.Main;
import ex.MyException;
import gui.DepartamentFormController;
import gui.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import modelo.entites.Departamento;
import modelo.entites.GenericaCombo;

/**
 * Classe usada para da suporte ao sistema
 * 
 * @author lucas.rodrigues
 *
 */
public class WorkShopHelper {

	/**
	 * Metodo usado para adicionar items a uma comboBox
	 * 
	 * @param cmb      ComboBox<GenericaCombo>
	 * @param obsLista ObservableList<GenericaCombo>
	 * @param list     List<GenericaCombo>
	 * @throws MyException
	 */
	public void addItemsComboBox(ComboBox<GenericaCombo> cmb, ObservableList<GenericaCombo> obsLista,
			List<GenericaCombo> list) throws MyException {

		if (list == null || list.size() <= 0)
			throw new MyException("Algum parametro esta nulo");

		obsLista = FXCollections.observableArrayList(list);
		cmb.setItems(obsLista);
	}

	/**
	 * Metodo usado para definir os dados que aparecerao na ComboBox
	 * 
	 * @param cmb ComboBox
	 * @throws MyException
	 */
	public void setExibirComboBox(ComboBox<GenericaCombo> cmb) throws MyException {

		Callback<ListView<GenericaCombo>, ListCell<GenericaCombo>> factory = lv -> new ListCell<GenericaCombo>() {
			@Override
			protected void updateItem(GenericaCombo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDados());
			}
		};
		cmb.setCellFactory(factory);
		cmb.setButtonCell(factory.call(null));

	}
	// 10/04/2020

	/**
	 * Carrega uma View e preserva os filhos da MainView Executa uma função do tipo
	 * consumer passada como parametro
	 * 
	 * @param <T>
	 * @param nomeAbsoluto caminho da view a ser carregada
	 * @param executar     implementacao da interface consummer
	 * @throws MyException
	 */
	public synchronized <T> void loadView(String nomeAbsoluto, Scene scenePai, Consumer<T> executar) throws MyException {
		// synchronize garante processamento não seja interrupido durante mult-treading

		if (nomeAbsoluto == null || "".equals(nomeAbsoluto))
			throw new MyException("Parametro nulo");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox newVBox = loader.load();
			Scene mainScane = scenePai;

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
	public void loadMainView() throws MyException {
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

	/**
	 * Metodo responsavel por criar uma janela de dialogo
	 * 
	 * @param <T>
	 * @param departamento
	 * 
	 * @param parentStage  stage pai
	 * @param absoluteName caminho para o dialag
	 */
	public <T> void criarDialogForm(Stage parentStage, String absoluteName, String titulo, Consumer<T> executar) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = (Pane) loader.load();
			
			if (executar != null) {
				T controller = loader.getController();
				executar.accept(controller);
			}
			// configurar o stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle(titulo);
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);// a janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);// stage pai do dialogStage
			dialogStage.initModality(Modality.WINDOW_MODAL); // vai dizer o comportamento da janela
			dialogStage.showAndWait();// vai abrir em cima do stage pai, e vai ter um comportmento modal

		} catch (Exception e) {
			Alerts.showAlert("Error", "Erro ao abrir a view", e.getMessage(), AlertType.ERROR);
		}
	}
	 
		public void FecharView(Stage parentStage) {
			try {
				
				parentStage.close();

			} catch (Exception e) {
				Alerts.showAlert("Error", "Erro ao fechar a view", e.getMessage(), AlertType.ERROR);
			}
		}
}
