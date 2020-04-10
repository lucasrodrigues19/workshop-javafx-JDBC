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

public class DepartamentFormController implements Initializable {

	private Departamento dpt;
	
	private WorkShopHelper helper = new WorkShopHelper();
	

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

	@FXML
	public void onBtSalvarAction() {
		Alerts.showAlert("Confirmação", "Salvamento de dados", "dados salvos com sucesso", AlertType.INFORMATION);
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {
//		Alerts.showAlert("Confirmação", "Cancelamento de dados", "ação cancelada", AlertType.CONFIRMATION);
		Stage parentStage = WorkUtils.palcoAtual(event);
		helper.FecharView(parentStage);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}

	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtID);
		Constraints.setTextFielMaxLength(txtNome, 30);

	}

	/**
	 * Metodo responsavel por preencher as caixinhas de texto com os dados do objeto
	 */
	public void atualizarDadosForm() {
		if(dpt == null)
			throw new IllegalArgumentException("Parametro inrregular");
		
		txtID.setText(String.valueOf(dpt.getId()));
		txtNome.setText(dpt.getNome());
	}
}
