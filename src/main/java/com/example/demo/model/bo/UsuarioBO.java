package com.example.demo.model.bo;

import com.example.demo.model.Usuario;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.usuario.UsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.PessoaDAO;

import javax.servlet.http.HttpServletResponse;
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

	public List<UsuarioDTO> buscaAll(){
		List<Usuario> usuarios = usuarioDAO.findAll();
		return usuarios.stream()
				.map(user -> modelMapper.map(user, UsuarioDTO.class))
				.collect(Collectors.toList());
	}

	public Optional<UsuarioDTO> buscaById(Long codigo) throws PessoaInexistenteOuInativaException {
		Optional<Usuario> usuario = Optional.of(buscarPessoaPeloCodigo(codigo));
		return Optional.of(modelMapper.map(usuario.get(), UsuarioDTO.class));
	}

	public UsuarioDTO criarUser(UsuarioDTO usuario, HttpServletResponse response){
		Usuario usuarioSalva = usuarioDAO.save(modelMapper.map(usuario, Usuario.class));
		return modelMapper.map(usuario, UsuarioDTO.class);
	}

	public void removerUser(Long codigo){
		usuarioDAO.deleteById(codigo);
	}

	public UsuarioDTO atualizaUser(Long codigo, UsuarioDTO usuario) throws PessoaInexistenteOuInativaException {
		Usuario usuarioSalva = (buscarPessoaPeloCodigo(codigo));
		BeanUtils.copyProperties(usuario, usuarioSalva, "codigo");
		return modelMapper.map(usuarioDAO.save(usuarioSalva), UsuarioDTO.class);
	}

	public Usuario buscarPessoaPeloCodigo(Long codigo) {
		Usuario usuarioSalva =  usuarioDAO.findById((codigo)).orElse(null);
		if(!Objects.nonNull(usuarioSalva)){throw new EmptyResultDataAccessException(1);}
		return usuarioSalva;
	}
}
