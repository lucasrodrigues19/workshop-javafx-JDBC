package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import db.DB;
import gui.helper.WorkShopHelper;
import gui.listeners.DadoAlteradoListener;
import gui.utils.Alerts;
import gui.utils.WorkUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.dao.DaoFactory;
import modelo.entites.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentListController implements Initializable,DadoAlteradoListener {

	private WorkShopHelper helper = new WorkShopHelper();
	
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
		helper.criarDialogForm(stage, "/gui/DepartamentForm.fxml","Entre com os dados do departamento", (DepartamentFormController controller)->{
			controller.setService(getService());
			controller.inscreverDadoAlteradoListener(this);//me escrevendo(this)para receber o evento
			
		});
	}

	@FXML
	public void tableColumnIdAction() {
		tableViewDepartamento.setOnMouseClicked((MouseEvent event) ->{
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				if(event.getClickCount() == 2) {
					String nome = tableViewDepartamento.getSelectionModel().getSelectedItem().getNome();
					int id = tableViewDepartamento.getSelectionModel().getSelectedItem().getId();
					Stage stage = WorkUtils.palcoAtual(event);
					helper.criarDialogForm(stage, "/gui/DepartamentForm.fxml", "Atualizar dados", (DepartamentFormController controller)->{
						controller.setDpt(new Departamento(id, nome));
						controller.setService(getService());
						controller.inscreverDadoAlteradoListener(this);//me escrevendo(this)para receber o evento(onDadosAlterados)
						controller.atualizarDadosForm();
					});
					
				}
			}
		});
		
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

	@Override
	public void onDadosAlterados() {
		atualizarTableView();
		
	}

	
}
