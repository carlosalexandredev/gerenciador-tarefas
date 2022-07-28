package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.model.dto.enuns.TipoLancamento;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name ="TAREFA")
public class Tarefa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name= "codigo_pessoa")
	private Usuario usuario;
	
}
