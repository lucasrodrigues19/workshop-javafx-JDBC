package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.ex.DBException;
import ex.MyException;
import ex.MyValidationException;
import gui.helper.WorkShopHelper;
import gui.listeners.DadoAlteradoListener;
import gui.utils.Alerts;
import gui.utils.Constraints;
import gui.utils.WorkUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.entites.GenericaCombo;
import modelo.entites.Vendedor;
import modelo.service.DepartamentoService;
import modelo.service.VendedorService;

public class VendedorFormController implements Initializable {

	private Vendedor ven;

	private WorkShopHelper helper = new WorkShopHelper();

	private VendedorService service;
	
	private DepartamentoService dptService;

	
	private List<DadoAlteradoListener> dadosAlteradosListeners = new ArrayList<DadoAlteradoListener>();

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtID;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpDataNasc;
	
	@FXML
	private TextField txtSalario;

	@FXML
	private Label lblErroNome;
	
	@FXML
	private Label lblErroEmail;

	
	@FXML
	private Label lblErroDataNasc;

	@FXML
	private Label lblErroSalario;

	@FXML
	private ComboBox<GenericaCombo>cmbDpt;
	
	@FXML
	private ObservableList<GenericaCombo> obsDpt;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	public void setVen(Vendedor ven) {
		this.ven = ven;
	}

	
	public void setServices(VendedorService service,DepartamentoService dptService) {
		this.service = service;
		this.dptService = dptService;
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
		
		
		if("".equals(txtEmail.getText().trim())) {
			exception.addErros("email", "O campo não pode ser vazio");
		}
		ven.setEmail(txtEmail.getText());
		
		
		if("".equals(txtSalario.getText().trim())) {
			exception.addErros("salario", "O campo não pode ser vazio");
		}
		ven.setBaseSalario(Double.parseDouble(txtSalario.getText()));
		
		
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
		Constraints.setTextFielMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtSalario);
		Constraints.setTextFielMaxLength(txtEmail, 65);
		WorkUtils.formatarDatePicker(dpDataNasc, "dd/MM/yyyy");
		

	}

	/**
	 * Metodo responsavel por preencher os textField com os dados do objeto
	 */
	public void atualizarDadosForm() {
		if (ven == null)
			throw new IllegalArgumentException("Vendedor  inrregular");

		txtID.setText(String.valueOf(ven.getId()));
		txtNome.setText(ven.getNome());
		txtEmail.setText(ven.getEmail());
		
		if(ven.getDataNasc() != null)
			dpDataNasc.setValue(LocalDate.ofInstant(ven.getDataNasc().toInstant(), ZoneId.systemDefault())); //ZoneId.sistemDefault = pega o fuso horario da maquina
		
		Locale.setDefault(Locale.US);
		txtSalario.setText(String.format("%.2f",ven.getBaseSalario()));
		
		if(ven.getDepartamento() == null)
			cmbDpt.getSelectionModel().selectFirst();
		else
			cmbDpt.setValue(ven.getDepartamento());
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
	public void inicalizarComboBox() throws MyException {
		if(dptService == null)
			throw new IllegalStateException("Departamento service sesta nulo");
		
		List<GenericaCombo>list = new ArrayList<GenericaCombo>();
		list.addAll(dptService.pesquisarTodos());
		helper.addItemsComboBox(cmbDpt, obsDpt,list);
		helper.setExibirComboBox(cmbDpt);
	}
}
