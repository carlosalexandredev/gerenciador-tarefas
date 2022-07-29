package com.example.demo.controller;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.demo.model.dto.tarefas.TarefaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.bo.TarefasBO;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("api/tarefa")
public class TarefaController {

	@Autowired
	private TarefasBO tarefaBO;


	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<TarefaDTO>> buscarTarefasAll(){
		List<TarefaDTO> tarefas = tarefaBO.buscaTarefas();
		return !tarefas.isEmpty() ? ResponseEntity.ok(tarefas) : ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ResponseEntity<Optional<TarefaDTO>> buscarTarefasId(@PathVariable Long codigo){
		Optional<TarefaDTO> tarefa = tarefaBO.buscaTarefasById(codigo);
		return !tarefa.isEmpty() ? ResponseEntity.ok(tarefa) : ResponseEntity.notFound().build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TarefaDTO> criar(@Valid @RequestBody TarefaDTO tarefa, HttpServletResponse response) throws PessoaInexistenteOuInativaException{
		TarefaDTO tarefaSalvo = tarefaBO.criarTarefa(tarefa, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalvo);
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		tarefaBO.removerTarefas(codigo);
	}
}
