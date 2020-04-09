package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entites.Departamento;

public class DepartamentListController implements Initializable {

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColumnId; // 1 - tipo da entidade, 2 - tipo da coluna

	@FXML
	private TableColumn<Departamento, String> tableColumnName;

	@FXML
	private Button btNew;

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
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("ID"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		//table view acompanha a altura e largura da janelaMain
		Stage stage = (Stage) Main.getMainScene().getWindow(); //referencia para a janela
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

}
