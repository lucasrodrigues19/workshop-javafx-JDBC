package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.helper.WorkShopHelper;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.WorkUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.entites.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentFormController implements Initializable {

	private Departamento dpt;

	private WorkShopHelper helper = new WorkShopHelper();

	private DepartamentoService service;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtID;

	@FXML
	private Label lblErro;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setDpt(Departamento dpt) {
		this.dpt = dpt;
	}

	public DepartamentoService getService() {
		return service;
	}

	public void setService(DepartamentoService service) {
		this.service = service;
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		Departamento dpt = getDadosForm();
		if(service == null)
			throw new IllegalArgumentException("O service está null");
		if(dpt == null)
			throw new IllegalArgumentException("O departamento está null");
			
		service.salvarOuAtualizar(dpt);
		Stage parentStage = WorkUtils.palcoAtual(event);
		helper.FecharView(parentStage);
		Alerts.showAlert("Confirmação", "Salvamento de dados", "dados salvos com sucesso", AlertType.INFORMATION);
	}

	private Departamento getDadosForm() {
		return new Departamento(WorkUtils.tryParseToInt(txtID.getText()), txtNome.getText());
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
//		Alerts.showAlert("Confirmação", "Cancelamento de dados", "ação cancelada", AlertType.CONFIRMATION);
		Stage parentStage = WorkUtils.palcoAtual(event);
		helper.FecharView(parentStage);
	}

	

	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextFielMaxLength(txtNome, 30);

	}

	/**
	 * Metodo responsavel por preencher as caixinhas de texto com os dados do objeto
	 */
	public void atualizarDadosForm() {
		if (dpt == null)
			throw new IllegalArgumentException("Departamento  inrregular");

		txtID.setText(String.valueOf(dpt.getId()));
		txtNome.setText(dpt.getNome());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
		
	}
}
