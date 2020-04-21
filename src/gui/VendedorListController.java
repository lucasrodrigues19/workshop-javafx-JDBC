package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DB;
import db.ex.DBException;
import ex.MyException;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import modelo.dao.DaoFactory;
import modelo.dao.DepartamentoDAO;
import modelo.entites.Departamento;
import modelo.entites.Vendedor;
import modelo.service.DepartamentoService;
import modelo.service.VendedorService;

public class VendedorListController implements Initializable, DadoAlteradoListener {

	private WorkShopHelper helper = new WorkShopHelper();

	private VendedorService service;
	
	private DepartamentoService dptService = new DepartamentoService(DaoFactory.getDepartamentoDAO(DB.getConexao("db.properties")));

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId; // 1 - tipo da entidade, 2 - tipo da coluna

	@FXML
	private TableColumn<Vendedor, String> tableColumnName;

	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;

	@FXML
	private TableColumn<Vendedor, Date> tableColumnDataNasc;

	@FXML
	private TableColumn<Vendedor, Double> tableColumnSalario;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEdit;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnRemove;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnDpt;

	@FXML
	private Button btNew;

	private ObservableList<Vendedor> obsListDpt;

	// get and service
	public VendedorService getService() {
		return service;
	}

	public void setService(VendedorService service) {
		this.service = service;
	}

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage stage = WorkUtils.palcoAtual(event);
		System.out.println("Botao neew...");
		helper.criarDialogForm(stage, "/gui/VendedorForm.fxml", "Entre com os dados do vendedor",
				(VendedorFormController controller) -> {
					
					try {
						controller.setServices(getService(),dptService);
						controller.setVen(new Vendedor());
						controller.inicalizarComboBox();
						controller.atualizarDadosForm();
						inscreverMeuObjeto(controller);// inscrevendo meu objeto(this) para esperar uma notificacao do subject(controller)	
						
					} catch (MyException e) {
						e.printStackTrace();
						Alerts.showAlert("Erro ao carregar a comboBox", null, e.getMessage(), AlertType.ERROR);
					}
																				// notificação do subject(controller)

				});
	}

	@FXML
	public void onTableDuploClick() {
		tableViewVendedor.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if (event.getClickCount() == 2) {
					Stage stage = WorkUtils.palcoAtual(event);
					helper.criarDialogForm(stage, "/gui/VendedorForm.fxml", "Atualizar dados",
							(VendedorFormController controller) -> {
								controller.setServices(getService(),dptService);
								try {
									controller.inicalizarComboBox();
									controller.setVen(getVendedorTable());
									inscreverMeuObjeto(controller);// inscrevendo meu objeto(this) para esperar uma notificacao do subject(controller)																// notificação do subject(controller)
									controller.atualizarDadosForm();
								} catch (MyException e) {
									e.printStackTrace();
									Alerts.showAlert("Erro ao carregar a comboBox", null, e.getMessage(), AlertType.ERROR);
								}
								
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
		WorkUtils.formatarDataColunaTabela(tableColumnDataNasc, "dd/MM/yyyy");
		tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("baseSalario"));
		WorkUtils.formatarDoubleColunaTabela(tableColumnSalario, 2);

//		tableColumnDpt.setCellValueFactory(new PropertyValueFactory<>("departamento.id"));

		// table view acompanha a altura e largura da janelaMain
		Stage stage = (Stage) Main.getMainScene().getWindow(); // referencia para a janela
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
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
		List<Vendedor> result = getService().pesquisarTodos();

		if (result == null || result.size() <= 0) {
			Alerts.showAlert("Error ", null, "Erro ao carregar os departamentos", AlertType.ERROR);
			throw new IllegalStateException("Erro ao carregar os departamentos");
		}

		obsListDpt = FXCollections.observableArrayList(result);
		tableViewVendedor.setItems(obsListDpt);
		iniciarBotaoEditar(tableColumnEdit, Color.YELLOWGREEN, "yellowgreen", "white");
		iniciarBotaoRemover(tableColumnRemove, Color.RED, "red", "white");
	}

	/**
	 * Metodo para criar um botao de edicao para cada linha da minha tabela
	 * 
	 * @param tableColumn
	 * @param backgroundColor
	 * @param borderColor
	 * @param textFilColor
	 */
	public void iniciarBotaoEditar(TableColumn<Vendedor, Vendedor> tableColumn, Color textFilColor, String borderColor,
			String backgroundColor) {
		tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumn.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}
				button.setStyle("-fx-background-color: " + backgroundColor + ";-fx-border-color: " + borderColor
						+ "; -fx-border-radius: 5px;");
				button.setTextFill(textFilColor);
				button.setCursor(Cursor.HAND);
				button.setEffect(new Reflection());
				setGraphic(button);
				button.setOnAction(event -> helper.criarDialogForm(WorkUtils.palcoAtual(event), "/gui/VendedorForm.fxml",
						"Atualizar dados", (VendedorFormController controller) -> {
							
							try {
								controller.setServices(getService(),dptService);
								controller.inicalizarComboBox();
								controller.setVen(obj);
								inscreverMeuObjeto(controller);// inscrevendo meu objeto(this) para esperar uma notificação
								controller.atualizarDadosForm();
							} catch (MyException e) {
								e.printStackTrace();
								Alerts.showAlert("Erro ao carregar a comboBox", null, e.getMessage(), AlertType.ERROR);
							}
							

						})

				);

			}
		});
	}

	/**
	 * Metodo para criar um botao de remocao para cada linha da minha tabela
	 * 
	 * @param tableColumn
	 * @param backgroundColor
	 * @param borderColor
	 * @param textFilColor
	 */
	public void iniciarBotaoRemover(TableColumn<Vendedor, Vendedor> tableColumn, Color textFilColor, String borderColor,
			String backgroundColor) {
		tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumn.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				if (obj == null) {
					setGraphic(null);
					return;
				}
				button.setStyle("-fx-background-color: " + backgroundColor + ";-fx-border-color: " + borderColor
						+ "; -fx-border-radius: 5px;");
				button.setTextFill(textFilColor);
				button.setCursor(Cursor.HAND);
				button.setEffect(new Reflection());
				setGraphic(button);
				button.setOnAction(event -> removerVendedor(obj));

			}
		});
	}

	private void removerVendedor(Vendedor dpt) {

		Optional<ButtonType> result = Alerts.mostrarConfirmacao("Confirmação", "Deseja excluir esse vendedor?");
		if (result.get() == ButtonType.OK) {
			if (service == null)
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

	/**
	 * Metodo usado para injetar minha dependecia (this) para o subject
	 * 
	 * @param controller subject
	 */
	public void inscreverMeuObjeto(VendedorFormController controller) {
		controller.inscreverDadoAlteradoListener(this);
	}

	/**
	 * Pega os dados do vendedor de acordo com item selecionado
	 * @return
	 */
	public Vendedor getVendedorTable() {

		int id = tableViewVendedor.getSelectionModel().getSelectedItem().getId();
		String nome = tableViewVendedor.getSelectionModel().getSelectedItem().getNome();
		String email = tableViewVendedor.getSelectionModel().getSelectedItem().getEmail();
		Date dataNasc = tableViewVendedor.getSelectionModel().getSelectedItem().getDataNasc();
		Double baseSalario = tableViewVendedor.getSelectionModel().getSelectedItem().getBaseSalario();
		Departamento dpt = tableViewVendedor.getSelectionModel().getSelectedItem().getDepartamento();
		return new Vendedor(id, nome, email, dataNasc, baseSalario, dpt);

	}

}
