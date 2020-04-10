package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.utils.Alerts;
import gui.utils.WorkUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entites.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentListController implements Initializable {

	private DepartamentoService service;

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColumnId; // 1 - tipo da entidade, 2 - tipo da coluna

	@FXML
	private TableColumn<Departamento, String> tableColumnName;

	@FXML
	private Button btNew;

	private ObservableList<Departamento> obsListDpt;

	// get and service
	public DepartamentoService getService() {
		return service;
	}

	public void setService(DepartamentoService service) {
		this.service = service;
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage stage = WorkUtils.palcoAtual(event);
		System.out.println("Botao neew...");
		criarDialogForm(new Departamento(),stage, "/gui/DepartamentForm.fxml");
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicalizarElementos();
	}

	/**
	 * Metodo para incializar comportamento dos elementos
	 */
	private void inicalizarElementos() {
		// padrao do javafx, para iniciar o comportamento da colunas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("nome"));

		// table view acompanha a altura e largura da janelaMain
		Stage stage = (Stage) Main.getMainScene().getWindow(); // referencia para a janela
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	/**
	 * metodo responsavel por acessar o servico, carregar os departamentos, colocar
	 * na observableList e associar a observableList com a tableView
	 */
	public void atualizarTableView() {
		if (service == null) {
			Alerts.showAlert("Error ", "Erro ao atualizar a tabela", "O service esta nulo", AlertType.ERROR);
			throw new IllegalStateException("O service esta nulo");
		}
		List<Departamento> result = getService().pesquisarTodos();

		if (result == null || result.size() <= 0) {
			Alerts.showAlert("Error ", "Erro ao atualizar a tabela", "Erro ao carregar os departamentos",
					AlertType.ERROR);
			throw new IllegalStateException("Erro ao carregar os departamentos");
		}

		obsListDpt = FXCollections.observableArrayList(result);
		tableViewDepartamento.setItems(obsListDpt);
	}

	/**
	 * Metodo responsavel por criar uma janela de dialogo
	 * @param departamento 
	 * 
	 * @param parentStage  stage pai
	 * @param absoluteName caminho para o dialag
	 */
	public void criarDialogForm(Departamento departamento, Stage parentStage, String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = (Pane) loader.load();
			
			DepartamentFormController controller = loader.getController();
			controller.setDpt(departamento);
			controller.atualizarDadosForm();

			//configurar o stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do departamento");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);// a janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);// stage pai do dialogStage
			dialogStage.initModality(Modality.WINDOW_MODAL); // vai dizer o comportamento da janela
			dialogStage.showAndWait();// vai abrir em cima do stage pai, e vai ter um comportmento modal

		} catch (Exception e) {
			Alerts.showAlert("Error", "Erro ao abrir a view", e.getMessage(), AlertType.ERROR);
		}
	}
}
