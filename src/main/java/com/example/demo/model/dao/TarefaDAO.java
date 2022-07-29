package com.example.demo.model.dao;

import com.example.demo.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaDAO extends JpaRepository<Tarefa, Long>{
}
