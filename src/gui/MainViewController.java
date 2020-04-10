package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import db.DB;
import ex.MyException;
import gui.helper.WorkShopHelper;
import gui.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import modelo.dao.DaoFactory;
import modelo.service.DepartamentoService;

/**
 * Classe usada para da suporte aos eventos e modificações na GUI gerada a
 * partir do documento MainView.fxml
 * 
 * @author lucas.rodrigues
 *
 */
public class MainViewController implements Initializable {
	
	private WorkShopHelper helper = new WorkShopHelper();

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
		try {
			helper.loadView("/gui/DepartamentList.fxml",Main.getMainScene(), (DepartamentListController controller) -> {
				controller.setService(
						new DepartamentoService(DaoFactory.getDepartamentoDAO(DB.getConexao("db.properties"))));
				controller.atualizarTableView();
			});
		} catch (MyException e) {
			e.printStackTrace();
			Alerts.showAlert("Error", "Erro ao listar dados da tabela", e.getMessage(), AlertType.ERROR);
			System.out.println(e.getMessage());
		}
	}

	@FXML
	public void onMenuItemSobreAction() throws MyException {
		try {

			helper.loadView("/gui/About.fxml",Main.getMainScene(), null);
		} catch (MyException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
