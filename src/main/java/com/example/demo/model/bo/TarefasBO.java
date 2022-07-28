package com.example.demo.model.bo;

import com.example.demo.model.Tarefa;
import com.example.demo.model.bo.event.RecursoCriadoEvent;
import com.example.demo.model.bo.exceptionhandler.ApplicationExceptionHandler;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.tarefas.TarefaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.model.dao.tarefa.TarefaDAO;
import com.example.demo.model.dao.usuario.PessoaDAO;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
		publisher.publishEvent(new RecursoCriadoEvent(this, response, tarefaSalva.getCodigo()));
		return modelMapper.map(tarefa, TarefaDTO.class);
	}

	/**
	 * @Method removerTarefas(Long codigo)
	 * @Rule 1 - Remover o usuário pelo seu codigo.
	 **/
	public void removerTarefas(Long codigo) {
		usuarioDAO.deleteById(codigo);
	}

	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<ApplicationExceptionHandler.Erro> erros = Arrays.asList(new ApplicationExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
