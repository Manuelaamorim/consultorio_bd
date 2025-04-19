package com.consultorio.service;

import com.consultorio.dao.*;
import com.consultorio.dto.CadastroFuncionarioDTO;
import com.consultorio.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService {

    @Autowired
    private PessoaDAO pessoaDAO;

    @Autowired
    private TelefoneDAO telefoneDAO;

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private FuncionarioDAO funcionarioDAO;

    @Autowired
    private DentistaDAO dentistaDAO;

    @Autowired
    private AuxiliarDAO auxiliarDAO;

    public void cadastrarFuncionario(CadastroFuncionarioDTO dto) {
        if (dto.getPessoa() == null || dto.getPessoa().getNome() == null || dto.getPessoa().getCpf() == null) {
            throw new IllegalArgumentException("Dados da pessoa inválidos!");
        }

        if (!"dentista".equalsIgnoreCase(dto.getTipoFuncionario()) &&
                !"auxiliar".equalsIgnoreCase(dto.getTipoFuncionario())) {
            throw new IllegalArgumentException("Tipo de funcionário inválido!");
        }

        // Salvar a pessoa e recuperar o ID gerado automaticamente
        Long pessoaId = pessoaDAO.salvar(dto.getPessoa());

        // Configurar o pessoaId no endereço
        dto.getEndereco().setPessoaId(pessoaId);
        enderecoDAO.salvar(dto.getEndereco());

        // Configurar o pessoaId nos telefones
        for (Telefone telefone : dto.getTelefones()) {
            telefone.setPessoaId(pessoaId);
            telefoneDAO.salvar(telefone);
        }

        // Criar e salvar o objeto Funcionario com o id da pessoa
        Funcionario funcionario = new Funcionario();
        funcionario.setId(pessoaId);  // O ID de pessoa é o mesmo do funcionário
        funcionarioDAO.salvar(funcionario);

        // Obter o ID do funcionário recém-criado
        Long funcionarioId = funcionarioDAO.getUltimoId();

        // Se for dentista, salvar os dados do dentista
        if ("dentista".equalsIgnoreCase(dto.getTipoFuncionario())) {
            if (dto.getCro() == null || dto.getEspecialidade() == null) {
                throw new IllegalArgumentException("Dados de Dentista são obrigatórios!");
            }

            Dentista dentista = new Dentista();
            dentista.setId(funcionarioId);
            dentista.setCro(dto.getCro());
            dentista.setEspecialidade(dto.getEspecialidade());
            dentistaDAO.salvar(dentista);
        } else if ("auxiliar".equalsIgnoreCase(dto.getTipoFuncionario())) {
            // Se for auxiliar, salvar o auxiliar
            Auxiliar aux = new Auxiliar();
            aux.setId(funcionarioId);
            auxiliarDAO.salvar(aux);
        }
    }
}
