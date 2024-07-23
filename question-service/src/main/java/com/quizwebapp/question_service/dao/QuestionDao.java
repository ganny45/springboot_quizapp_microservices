package com.quizwebapp.question_service.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quizwebapp.question_service.model.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer>{
	
	List<Question> findByCategory(String category);
	
	Question findByQuestionTitle(String question);
	
	@Query(value = "SELECT q.id FROM Question q WHERE q.category = :category ORDER BY RAND()")
	List<Integer> findRandomQuestionsByCategory(String category, Pageable pageable);
}
