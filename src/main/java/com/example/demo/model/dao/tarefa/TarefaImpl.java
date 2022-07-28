//package com.example.demo.model.dao.lancamento;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import com.example.demo.model.Tarefa;
//import com.example.demo.model.Tarefa_;
//
//import com.example.demo.model.dto.tarefas.filter.TarefaFilter;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//public class TarefaImpl implements TarefaQuery {
//
//	@PersistenceContext
//	private EntityManager manager;
//
//	@Override
//	public Page<Tarefa> filtrar(TarefaFilter tarefaFilter, Pageable pageable) {
//		CriteriaBuilder builder = manager.getCriteriaBuilder();
//		CriteriaQuery<Tarefa> criteria = builder.createQuery(Tarefa.class);
//		Root<Tarefa> root = criteria.from(Tarefa.class);
//
//		Predicate[] predicates = criarRestricoes(tarefaFilter, builder, root);
//		criteria.where(predicates);
//
//		TypedQuery<Tarefa> query = manager.createQuery(criteria);
//		adicionarRestricoesPaginacao(query, pageable);
//
//
//		return new PageImpl<>(query.getResultList(), pageable, total(tarefaFilter));
//	}
//
//	private Predicate[] criarRestricoes(TarefaFilter tarefaFilter, CriteriaBuilder builder,
//										Root<Tarefa> root) {
//
//		List<Predicate> predicates = new ArrayList<>();
//
//		if (!StringUtils.isEmpty(tarefaFilter.getDescricao())){
//			//WHERE descricao LIKE '%algumaCoisaAqui%'
//			predicates.add(builder.like(builder.lower(root.get(Tarefa_.descricao)), "%" + tarefaFilter.getDescricao().toLowerCase() + "%"));
//		}
//		if(tarefaFilter.getDataVencimentoDe() != null){
//			predicates.add(builder.greaterThanOrEqualTo(root.get(Tarefa_.dataVencimento), tarefaFilter.getDataVencimentoDe()));
//		}
//		if(tarefaFilter.getDataVencimentoAte() != null) {
//			predicates.add(builder.lessThanOrEqualTo(root.get(Tarefa_.dataVencimento), tarefaFilter.getDataVencimentoAte() ));
//		}
//
//		return predicates.toArray(new Predicate[predicates.size()]);
//	}
//
//	private void adicionarRestricoesPaginacao(TypedQuery<Tarefa> query, Pageable pageable) {
//		Integer paginaAtual = pageable.getPageNumber();
//		Integer totalRegistrosPorPagina = pageable.getPageSize();
//		Integer primeiroRegistroPagina = paginaAtual * totalRegistrosPorPagina;
//
//		query.setFirstResult(primeiroRegistroPagina);
//		query.setMaxResults(totalRegistrosPorPagina);
//	}
//
//	private Long total(TarefaFilter tarefaFilter) {
//		CriteriaBuilder builder = manager.getCriteriaBuilder();
//		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
//		Root<Tarefa> root = criteria.from(Tarefa.class);
//
//		Predicate[] predicates = criarRestricoes(tarefaFilter, builder, root);
//		criteria.where(predicates);
//
//		criteria.select(builder.count(root));
//		return manager.createQuery(criteria).getSingleResult();
//	}
//}
