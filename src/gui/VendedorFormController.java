package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.ex.DBException;
import ex.MyValidationException;
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
import modelo.entites.Vendedor;
import modelo.service.VendedorService;

public class VendedorFormController implements Initializable {

	private Vendedor ven;

	private WorkShopHelper helper = new WorkShopHelper();

	private VendedorService service;

	private List<DadoAlteradoListener> dadosAlteradosListeners = new ArrayList<DadoAlteradoListener>();

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtID;

	@FXML
	private Label lblErroNome;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setVen(Vendedor ven) {
		this.ven = ven;
	}

	public VendedorService getService() {
		return service;
	}

	public void setService(VendedorService service) {
		this.service = service;
	}

	/**
	 * Adiciona um listener(Esperando um sinal) para receber o evento de minha
	 * classe
	 * 
	 * @param listener
	 */
	public void inscreverDadoAlteradoListener(DadoAlteradoListener listener) {
		dadosAlteradosListeners.add(listener);
	}

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		
		if (service == null)
			throw new IllegalArgumentException("O service está null");
		if (ven == null)
			throw new IllegalArgumentException("O departamento está null");
		
		try {

			ven = getDadosForm();
			service.salvarOuAtualizar(ven);
			// emite o sinal(evento) para que meu listener seja executado
			notificarDadosAlteradosListeners();
			Stage parentStage = WorkUtils.palcoAtual(event);
			helper.FecharView(parentStage);
			// Alerts.showAlert("Confirmação", "Salvamento de dados", "dados salvos com
			// sucesso", AlertType.INFORMATION);
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar o objeto", "Salvamento de dados", e.getMessage(), AlertType.ERROR);
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (MyValidationException e) {
			setMsgErros(e.getErros());
		}
	}

	/**
	 * executa a o evento onDadosAlterados para cada listener(sera executado quando
	 * houver uma modificação ou alteração no controle)
	 */
	private void notificarDadosAlteradosListeners() {
		for (DadoAlteradoListener listener : dadosAlteradosListeners) {
			listener.onDadosAlterados();
		}

	}

	private Vendedor getDadosForm() {
		Vendedor ven = new Vendedor();

		MyValidationException exception = new MyValidationException("Erro de validação");

		ven.setId(WorkUtils.tryParseToInt(txtID.getText()));

		if ("".equals(txtNome.getText().trim())) {
			exception.addErros("nome", "O campo não pode ser vazio");
		}
		ven.setNome(txtNome.getText());

		if (exception.getErros().size() > 0) {
			throw exception;
		}
		return ven;
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
	 * Metodo responsavel por preencher os textField com os dados do objeto
	 */
	public void atualizarDadosForm() {
		if (ven == null)
			throw new IllegalArgumentException("Vendedor  inrregular");

		txtID.setText(String.valueOf(ven.getId()));
		txtNome.setText(ven.getNome());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
		

	}

	private void setMsgErros(Map<String, String> erros) {
		Set<String> filhos = erros.keySet();

		if (filhos.contains("nome")) {
			lblErroNome.setText(erros.get("nome"));
		}
	}
	public void selecionarNome() {
		txtNome.setFocusTraversable(true);
	}
}
