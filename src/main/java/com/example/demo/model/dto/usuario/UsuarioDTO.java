package com.example.demo.model.dto.usuario;

import com.example.demo.model.dto.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Table(name = "pessoa" )
public class UsuarioDTO {

	private Long codigo;

	@NotNull
	@Size(max = 80)
	private String nome;

	@NotNull
	@Size(max = 40)
	private String senha;

	@NotNull
	@Min(value = 18, message = "Usuario deve ser maior de 18 anos")
	private Integer idade;

	@Email
	@NotNull
	@Size(max = 100)
	private String email;

	@NotNull
	@Size(max = 20)
	private String equipe;

	@NotNull
	@Size(max = 80)
	private String funcao;

	@NotNull
	private boolean ativo;

	@NotNull
	private TipoUsuario privilegio;
}
