package com.example.demo.model.dao.tarefa;

import com.example.demo.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaDAO extends JpaRepository<Tarefa, Long> /*, TarefaQuery */ {
}
