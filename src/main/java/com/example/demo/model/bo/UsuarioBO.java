package com.example.demo.model.bo;

import com.example.demo.model.Usuario;
import com.example.demo.model.bo.event.RecursoCriadoEvent;
import com.example.demo.model.bo.exceptionhandler.ApplicationExceptionHandler;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.usuario.UsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.usuario.PessoaDAO;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioBO {
	
	@Autowired
	private PessoaDAO usuarioDAO;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * @Method buscaAll()
	 * @Rule 1 - Realiza busca de todos os usuários na base de dados.
	 **/
	public List<UsuarioDTO> buscaAll(){
		List<Usuario> usuarios = usuarioDAO.findAll();
		return usuarios.stream()
				.map(user -> modelMapper.map(user, UsuarioDTO.class))
				.collect(Collectors.toList());
	}

	/**
	 * @Method buscaById(Long codigo)
	 * @Rule 1 - Realiza busca de um único usuário na base de dados.
	 **/
	public Optional<UsuarioDTO> buscaById(Long codigo) throws PessoaInexistenteOuInativaException {
		Optional<Usuario> usuario = Optional.of(buscarPessoaPeloCodigo(codigo));
		return Optional.of(modelMapper.map(usuario.get(), UsuarioDTO.class));
	}

	/**
	 * @Method criarUser(UsuarioDTO usuario, HttpServletResponse response)
	 * @Rule 1 - Realiza inserção de usuário na base de dados.
	 * @Rule 2 - Regras checar Validations em UsuarioDTO.
	 **/
	public UsuarioDTO criarUser(UsuarioDTO usuario, HttpServletResponse response){
		Usuario usuarioSalva = usuarioDAO.save(modelMapper.map(usuario, Usuario.class));
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalva.getCodigo()));
		return modelMapper.map(usuario, UsuarioDTO.class);
	}

	/**
	 * @Method atualizaUser(Long codigo, UsuarioDTO usuario)
	 * @Rule 1 - Realiza atualização de usuário na base de dados.
	 * @Rule 2 - Regras checar Validations em UsuarioDTO.
	 **/
	public UsuarioDTO atualizaUser(Long codigo, UsuarioDTO usuario) throws PessoaInexistenteOuInativaException {
		Usuario usuarioSalva = (buscarPessoaPeloCodigo(codigo));
		BeanUtils.copyProperties(usuario, usuarioSalva, "codigo");
		return modelMapper.map(usuarioDAO.save(usuarioSalva), UsuarioDTO.class);
	}

	/**
	 * @Method atualizarPropriedadeAtiva(Long codigo, Boolean ativo)
	 * @Rule 1 - Realiza atualização para ativar ou desativar usuário na base de dados.
	 **/
	public void atualizarPropriedadeAtiva(Long codigo, Boolean ativo) {
		Usuario usuarioSalva =(buscarPessoaPeloCodigo(codigo));
		usuarioSalva.setAtivo(ativo);
		usuarioDAO.save(usuarioSalva);
	}

	/**
	 * @Method removerUser(Long codigo)
	 * @Rule 1 - Remover o usuário pelo seu codigo.
	 **/
	public void removerUser(Long codigo){
		usuarioDAO.deleteById(codigo);
	}

	/**
	 * @Method buscarPessoaPeloCodigo(Long codigo)
	 * @Rule 1 - Realiza busca do usuário pelo seu codigo.
	 **/
	public Usuario buscarPessoaPeloCodigo(Long codigo) {
		Usuario usuarioSalva =  usuarioDAO.findById((codigo)).orElse(null);
		if(!Objects.nonNull(usuarioSalva)){throw new EmptyResultDataAccessException(1);}
		return usuarioSalva;
	}
}
