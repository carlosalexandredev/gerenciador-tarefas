package com.example.demo.model.bo;

import com.example.demo.model.Tarefa;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.tarefas.TarefaDTO;
import com.example.demo.model.dto.usuario.UsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.model.dao.TarefaDAO;
import com.example.demo.model.dao.PessoaDAO;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefasBO {
	
	@Autowired
	private TarefaDAO tarefaDAO;
	@Autowired
	private PessoaDAO usuarioDAO;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * @Method buscaTarefasAll()
	 * @Rule 1 - Realiza busca de todos tarefas na base de dados.
	 **/
	public List<TarefaDTO> buscaTarefas(){
		List<Tarefa> tarefas = tarefaDAO.findAll();
		return tarefas.stream()
				.map(task -> modelMapper.map(task, TarefaDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * @Method buscaTarefasById(Long codigo)
	 * @Rule 1 - Realiza busca de uma única tarefa na base de dados.
	 **/
	public Optional<TarefaDTO> buscaTarefasById(Long codigo) {
		Optional<Tarefa> tarefa = tarefaDAO.findById(codigo);
		return Optional.of(modelMapper.map(tarefa.get(), TarefaDTO.class));
	}

	/**
	 * @Method criarTarefa(TarefaDTO tarefa, HttpServletResponse response)
	 * @Rule 1 - Realiza inserção de tarefa na base de dados.
	 * @Rule 2 - Regras checar Validations em TarefaDTO.
	 **/
	public TarefaDTO criarTarefa(TarefaDTO tarefa, HttpServletResponse response) throws PessoaInexistenteOuInativaException {
		Usuario usuario = usuarioDAO.getById(tarefa.getUsuario().getCodigo());
		if(!Objects.nonNull(usuario) || !usuario.isAtivo())
			throw new PessoaInexistenteOuInativaException();
		Tarefa tarefaSalva = tarefaDAO.save(modelMapper.map(tarefa, Tarefa.class));
		return modelMapper.map(tarefa, TarefaDTO.class);
	}

	/**
	 * @Method removerTarefas(Long codigo)
	 * @Rule 1 - Remover o usuário pelo seu codigo.
	 **/
	public void removerTarefas(Long codigo) {
		tarefaDAO.deleteById(codigo);
	}


	public UsuarioDTO atualizaTarefa(Long codigo, TarefaDTO tarefa) throws PessoaInexistenteOuInativaException {
		Tarefa tarefaSalva = tarefaDAO.getById(codigo);
		BeanUtils.copyProperties(tarefa, tarefaSalva, "codigo");
		return modelMapper.map(tarefaDAO.save(tarefaSalva), UsuarioDTO.class);
	}
}
