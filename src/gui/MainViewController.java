package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;	
	
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println(menuItemVendedor.getText());
	}
	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println(menuItemDepartamento.getText());
	}
	@FXML
	public void onMenuItemSobreAction() {
		System.out.println(menuItemSobre.getText());
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		
	}

	
}
