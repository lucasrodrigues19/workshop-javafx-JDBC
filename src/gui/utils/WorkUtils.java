package gui.utils;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WorkUtils {

	/**
	 * Metodo responsavel para acessar o stage de onde meu controle recebeu o
	 * evento(Action)
	 * 
	 * @param event(Action) Evento que o botao recebeu
	 * @return
	 */
	public static Stage palcoAtual(ActionEvent event) {
		// a partir do evento vou pegar o Node e o Window(Super classe do Stage)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	/**
	 * Metodo responsavel para acessar o stage de onde meu controle recebeu o
	 * evento(Mouse)
	 * 
	 * @param event (MouseEvent) Evento que o botao recebeu
	 * @return
	 */
	public static Stage palcoAtual(MouseEvent event) {
		// a partir do evento vou pegar o Node e o Window(Super classe do Stage)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	/**
	 * Metodo usado para formatar a data de uma tabela
	 * @param <T>
	 * @param tableColumn
	 * @param formato
	 */
	public static <T> void formatarDataColunaTabela(TableColumn<T, Date> tableColumn, String formato) {
		
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>(){
				private SimpleDateFormat sdf =  new SimpleDateFormat(formato);
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if(empty) {
						setText(null);
					}else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}
	/**
	 * Metodo usaddo para formatar um valor double na tabela
	 * @param <T>
	 * @param tableColumn
	 * @param casasDecimais
	 */
	public static <T> void formatarDoubleColunaTabela(TableColumn<T, Double>tableColumn, int casasDecimais) {
		
		tableColumn.setCellFactory(column->{
			TableCell<T, Double> cell = new TableCell<T, Double>(){
				@Override
				protected void updateItem(Double item, boolean empty) {	
					super.updateItem(item, empty);
					if(empty) {
						setText(null);
					}else {
						Locale.setDefault(Locale.US);
						setText(String.format("%."+casasDecimais+"f", item));
					}
				}
			};
			return cell;
		});
		
	}
}
