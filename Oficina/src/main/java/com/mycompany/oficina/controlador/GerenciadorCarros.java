package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;

/**
 * Classe responsável pelo gerenciamento dos carros da oficina.
 * <p>
 * Permite buscar, listar, cadastrar, editar e remover carros.
 * Os dados são persistidos usando arquivos JSON.
 */
public class GerenciadorCarros extends GerenciadorGenerico<Carro> {

    /**
     * Construtor do gerenciador de carros.
     *
     * @param persistencia Objeto responsável pela leitura e escrita dos dados no arquivo "carros".
     */
    public GerenciadorCarros(PersistenciaJson persistencia) {
        super(persistencia, "carros", new TypeToken<ArrayList<Carro>>() {});
    }

    /**
     * Cria e cadastra um novo carro, associando-o a um cliente.
     * Este método constrói um novo objeto Carro com base nos dados fornecidos.
     *
     * @param clienteSelecionado O cliente que será o dono do carro.
     * @param fabricante         O nome do fabricante do carro.
     * @param modelo             O modelo do carro.
     * @param placa              A placa do carro.
     * @param chassi             O número do chassi (identificador único).
     * @return O carro recém-criado, ou null se o chassi já existir.
     */
    public Carro cadastrarCarro(Cliente clienteSelecionado, String fabricante, String modelo, String placa, String chassi) {
        Carro novoCarro = new Carro(clienteSelecionado.getNome(), clienteSelecionado.getCpf(), fabricante, modelo, placa, chassi);

        // Usa o método "adicionar" herdado da classe pai
        super.adicionar(novoCarro);

        return novoCarro;
    }

    /**
     * Edita os dados de um carro existente, usando o chassi como chave.
     *
     * @param chassi         O chassi do carro a ser editado.
     * @param novoFabricante Novo nome do fabricante.
     * @param novoModelo     Novo modelo do carro.
     * @param novaPlaca      Nova placa do carro.
     * @return true se o carro foi encontrado e editado com sucesso; false caso contrário.
     */
    public boolean editarCarro(String chassi, String novoFabricante, String novoModelo, String novaPlaca) {
        Carro carroParaEditar = this.buscarPorIdentificador(chassi);

        if (carroParaEditar != null) {
            carroParaEditar.setFabricante(novoFabricante);
            carroParaEditar.setModelo(novoModelo);
            carroParaEditar.setPlaca(novaPlaca);
            super.salvarAlteracoes();
            return true;
        }
        return false;
    }
}
