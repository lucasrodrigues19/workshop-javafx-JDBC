package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gui.helper.WorkShopHelper;
import gui.listeners.DadoAlteradoListener;
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

	private List<DadoAlteradoListener> dadosAlteradosListeners = new ArrayList<DadoAlteradoListener>();
	
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

	/**
	 * Adiciona um listener(Esperando um sinal) para receber o evento de minha classe
	 * @param listener
	 */
	public void inscreverDadoAlteradoListener(DadoAlteradoListener listener) {
		dadosAlteradosListeners.add(listener);
	}
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		Departamento dpt = getDadosForm();
		if(service == null)
			throw new IllegalArgumentException("O service está null");
		if(dpt == null)
			throw new IllegalArgumentException("O departamento está null");
			
		service.salvarOuAtualizar(dpt);
		//emite o sinal(evento) para que meu listener seja executado
		notificarDadosAlteradosListeners();
		Stage parentStage = WorkUtils.palcoAtual(event);
		helper.FecharView(parentStage);
		//Alerts.showAlert("Confirmação", "Salvamento de dados", "dados salvos com sucesso", AlertType.INFORMATION);
	}

	/**
	 * executa a o evento onDadosAlterados para cada listener(sera executado quando houver uma modificação ou alteração no controle)
	 */
	private void notificarDadosAlteradosListeners() {
		for(DadoAlteradoListener listener : dadosAlteradosListeners) {
			listener.onDadosAlterados();
		}
		
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
