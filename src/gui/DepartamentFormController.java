package gui;

import java.net.URL;
import java.util.ResourceBundle;

import ex.MyException;
import gui.utils.Alerts;
import gui.utils.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entites.Departamento;
import javafx.scene.control.Alert.AlertType;

public class DepartamentFormController implements Initializable {

	private Departamento dpt;

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
		Alerts.showAlert("Confirmação", "Salvamento de dados", "dados salvos com sucesso", AlertType.CONFIRMATION);
	}

	@FXML
	public void onBtCancelarAction() {
		Alerts.showAlert("Confirmação", "Cancelamento de dados", "ação cancelada", AlertType.CONFIRMATION);
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
