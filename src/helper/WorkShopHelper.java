package helper;

import java.util.List;

import ex.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import modelo.entites.GenericaCombo;

/**
 * Classe usada para da suporte ao sistema
 * 
 * @author lucas.rodrigues
 *
 */
public class WorkShopHelper {

	/**
	 * Metodo usado para adicionar items a uma comboBox
	 * 
	 * @param cmb      ComboBox<GenericaCombo>
	 * @param obsLista ObservableList<GenericaCombo>
	 * @param list     List<GenericaCombo>
	 * @throws MyException
	 */
	public static void addItemsComboBox(ComboBox<GenericaCombo> cmb, ObservableList<GenericaCombo> obsLista,
			List<GenericaCombo> list) throws MyException {

		if (list == null || list.size() <= 0)
			throw new MyException("Algum parametro esta nulo");

		obsLista = FXCollections.observableArrayList(list);
		cmb.setItems(obsLista);
	}

	/**
	 * Metodo usado para definir os dados que aparecerao na ComboBox
	 * 
	 * @param cmb ComboBox
	 * @throws MyException
	 */
	public static void setExibirComboBox(ComboBox<GenericaCombo> cmb) throws MyException {

		Callback<ListView<GenericaCombo>, ListCell<GenericaCombo>> factory = lv -> new ListCell<GenericaCombo>() {
			@Override
			protected void updateItem(GenericaCombo item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDados());
			}
		};
		cmb.setCellFactory(factory);
		cmb.setButtonCell(factory.call(null));

	}
}
