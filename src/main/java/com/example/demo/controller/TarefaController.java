package com.example.demo.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.demo.model.Tarefa;
import com.example.demo.model.dto.tarefas.TarefaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.bo.event.RecursoCriadoEvent;
import com.example.demo.model.bo.exceptionhandler.ApplicationExceptionHandler.Erro;
import com.example.demo.model.dao.tarefa.TarefaDAO;
import com.example.demo.model.bo.TarefasBO;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("api/tarefa")
public class TarefaController {

	@Autowired
	private TarefasBO tarefaBO;

	@GetMapping()
	public ResponseEntity<List<TarefaDTO>> buscarTarefasAll(){
		List<TarefaDTO> tarefas = tarefaBO.buscaTarefas();
		return !tarefas.isEmpty() ? ResponseEntity.ok(tarefas) : ResponseEntity.notFound().build();
	}
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<TarefaDTO>> buscarTarefasId(@PathVariable Long codigo){
		Optional<TarefaDTO> tarefa = tarefaBO.buscaTarefasById(codigo);
		return !tarefa.isEmpty() ? ResponseEntity.ok(tarefa) : ResponseEntity.notFound().build();
	}
	
	@PostMapping()
	public ResponseEntity<TarefaDTO> criar(@Valid @RequestBody TarefaDTO tarefa, HttpServletResponse response) throws PessoaInexistenteOuInativaException{
		TarefaDTO tarefaSalvo = tarefaBO.criarTarefa(tarefa, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalvo);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		tarefaBO.removerTarefas(codigo);
	}
}
