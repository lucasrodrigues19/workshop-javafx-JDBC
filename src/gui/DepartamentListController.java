package gui;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.scenario.effect.Effect;

import application.Main;
import db.ex.DBException;
import gui.helper.WorkShopHelper;
import gui.listeners.DadoAlteradoListener;
import gui.utils.Alerts;
import gui.utils.WorkUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modelo.entites.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentListController implements Initializable, DadoAlteradoListener {

	private WorkShopHelper helper = new WorkShopHelper();

	private DepartamentoService service;

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColumnId; // 1 - tipo da entidade, 2 - tipo da coluna

	@FXML
	private TableColumn<Departamento, String> tableColumnName;

	@FXML
	private TableColumn<Departamento, Departamento> tableColumnEdit;
	
	@FXML
	private TableColumn<Departamento, Departamento> tableColumnRemove;

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
		helper.criarDialogForm(stage, "/gui/DepartamentForm.fxml", "Entre com os dados do departamento",
				(DepartamentFormController controller) -> {
					controller.setDpt(new Departamento());
					controller.setService(getService());
					inscreverMeuObjeto(controller);// me escrevendo(this)para receber o evento

				});
	}

	@FXML
	public void onTableDuploClick() {
		tableViewDepartamento.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
					String nome = tableViewDepartamento.getSelectionModel().getSelectedItem().getNome();
					int id = tableViewDepartamento.getSelectionModel().getSelectedItem().getId();
					Stage stage = WorkUtils.palcoAtual(event);
					helper.criarDialogForm(stage, "/gui/DepartamentForm.fxml", "Atualizar dados",
							(DepartamentFormController controller) -> {
								controller.setDpt(new Departamento(id, nome));
								controller.setService(getService());
								controller.atualizarDadosForm();
								inscreverMeuObjeto(controller);// me escrevendo(this)para receber o
																// evento(onDadosAlterados)
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
			Alerts.showAlert("Error ", null, "O service esta nulo", AlertType.ERROR);
			throw new IllegalStateException("O service esta nulo");
		}
		List<Departamento> result = getService().pesquisarTodos();

		if (result == null || result.size() <= 0) {
			Alerts.showAlert("Error ", null, "Erro ao carregar os departamentos",
					AlertType.ERROR);
			throw new IllegalStateException("Erro ao carregar os departamentos");
		}

		obsListDpt = FXCollections.observableArrayList(result);
		tableViewDepartamento.setItems(obsListDpt);
		iniciarBotaoEditar(tableColumnEdit,Color.YELLOWGREEN,"yellowgreen","white");
		iniciarBotaoRemover(tableColumnRemove, Color.RED, "red", "white");
	}

	/**
	 * Metodo para criar um botao de edicao para cada linha da minha tabela
	 * @param tableColumn 
	 * @param backgroundColor 
	 * @param borderColor 
	 * @param textFilColor 
	 */
	public void iniciarBotaoEditar(TableColumn<Departamento, Departamento> tableColumn, Color textFilColor, String borderColor, String backgroundColor) {
		tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumn.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}
				button.setStyle("-fx-background-color: "+ backgroundColor+";-fx-border-color: "+borderColor+"; -fx-border-radius: 5px;");
				button.setTextFill(textFilColor);
				button.setCursor(Cursor.HAND);
				button.setEffect(new Reflection());
				setGraphic(button);
				button.setOnAction(event -> helper.criarDialogForm(WorkUtils.palcoAtual(event),
						"/gui/DepartamentForm.fxml", "Atualizar dados", (DepartamentFormController controller) -> {
							controller.setDpt(obj);
							inscreverMeuObjeto(controller);
							controller.setService(getService());
							controller.atualizarDadosForm();
						})

				);
				
			}
		});
	}
	/**
	 * Metodo para criar um botao de remocao para cada linha da minha tabela
	 * @param tableColumn 
	 * @param backgroundColor 
	 * @param borderColor 
	 * @param textFilColor 
	 */
	public void iniciarBotaoRemover(TableColumn<Departamento, Departamento> tableColumn, Color textFilColor, String borderColor, String backgroundColor) {
		tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumn.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}
				button.setStyle("-fx-background-color: "+ backgroundColor+";-fx-border-color: "+borderColor+"; -fx-border-radius: 5px;");
				button.setTextFill(textFilColor);
				button.setCursor(Cursor.HAND);
				button.setEffect(new Reflection());
				setGraphic(button);
				button.setOnAction(event -> removerDepartamento(obj));
					
			}
		});
	}
	
	private void removerDepartamento(Departamento dpt) {
		
		
		Optional<ButtonType>result = Alerts.mostrarConfirmacao("Confirmação", "Desaeja excluir esse departamento?");
		if(result.get() == ButtonType.OK) {
			if(service == null)
				throw new IllegalStateException("O service esta nulo");
			
			try {
				service.remover(dpt);
				atualizarTableView();
			} catch (DBException e) {
				Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
			
				
		
	}

	@Override
	public void onDadosAlterados() {
		atualizarTableView();

	}

	public void inscreverMeuObjeto(DepartamentFormController controller) {
		controller.inscreverDadoAlteradoListener(this);
	}

}
