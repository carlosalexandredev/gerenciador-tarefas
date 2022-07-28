package com.example.demo.model.dto.tarefas;

import com.example.demo.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class TarefaDTO {
	private Long codigo;

	@NotNull
	@Size(max = 50)
	private String nome;

	@NotNull
	@Size(max = 50)
	private String assunto;

	@Size(max = 80)
	private String descricao;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimento;

	@NotNull
	private Usuario usuario;
	
}
