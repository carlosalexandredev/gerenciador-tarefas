package com.example.demo.model.dao.usuario;

import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaDAO extends JpaRepository<Usuario, Long>{
}
