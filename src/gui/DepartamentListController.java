package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.text.TabExpander;

import application.Main;
import gui.utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNewAction() {
		System.out.println("Botao neew...");
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
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));

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
}
