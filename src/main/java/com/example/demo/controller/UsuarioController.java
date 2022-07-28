package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.bo.UsuarioBO;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioBO usuarioBO;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listar(){
		List<UsuarioDTO> usuarios = usuarioBO.buscaAll();
		return !usuarios.isEmpty() ?  ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}
	 
	@GetMapping("/{codigo}")
	public ResponseEntity<Optional<UsuarioDTO>> buscarPelaPessoa(@PathVariable Long codigo) throws PessoaInexistenteOuInativaException {
		Optional<UsuarioDTO> usuario = usuarioBO.buscaById(codigo);
		return !usuario.isEmpty() ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuario, HttpServletResponse response) {
		UsuarioDTO usuarioCriado = usuarioBO.criarUser(usuario, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long codigo, @Valid @RequestBody UsuarioDTO usuario) throws PessoaInexistenteOuInativaException {
		UsuarioDTO usuarioCriado = usuarioBO.atualizaUser(codigo, usuario);
		return ResponseEntity.ok(usuarioCriado);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		usuarioBO.atualizarPropriedadeAtiva(codigo, ativo);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		usuarioBO.removerUser(codigo);
	}
}
	
	
	

