package gui.listeners;
/**
 * Interface que permite que um objeto(Objeto que implementar essa inteface) escute um evento de um outro objeto.
 * 
 * O objeto que emite o sinal para executar o evento(onDadosAlterados), não conhece a implementação dessa interface(O objeto que esta escutando o evento dele)
 * @author lucas.rodrigues
 *
 */
public interface DadoAlteradoListener {

	void onDadosAlterados();
}
