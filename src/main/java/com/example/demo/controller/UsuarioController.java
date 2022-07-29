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
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.bo.UsuarioBO;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioBO usuarioBO;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<UsuarioDTO>> listar(){
		List<UsuarioDTO> usuarios = usuarioBO.buscaAll();
		return !usuarios.isEmpty() ?  ResponseEntity.ok(usuarios) : ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ResponseEntity<Optional<UsuarioDTO>> buscarPelaPessoa(@PathVariable Long codigo) throws PessoaInexistenteOuInativaException {
		Optional<UsuarioDTO> usuario = usuarioBO.buscaById(codigo);
		return !usuario.isEmpty() ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO usuario, HttpServletResponse response) {
		UsuarioDTO usuarioCriado = usuarioBO.criarUser(usuario, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.PUT)
	public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long codigo, @Valid @RequestBody UsuarioDTO usuario) throws PessoaInexistenteOuInativaException {
		UsuarioDTO usuarioCriado = usuarioBO.atualizaUser(codigo, usuario);
		return ResponseEntity.ok(usuarioCriado);
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		usuarioBO.removerUser(codigo);
	}
}
	
	
	

