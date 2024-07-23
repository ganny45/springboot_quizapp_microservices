package com.quizwebapp.quiz_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizwebapp.quiz_service.dao.QuizDao;
import com.quizwebapp.quiz_service.feign.QuizInterface;
import com.quizwebapp.quiz_service.model.QuestionWrapper;
import com.quizwebapp.quiz_service.model.Quiz;
import com.quizwebapp.quiz_service.model.Response;

@Service
public class QuizService {

	@Autowired
	QuizDao quizDao;
	
	@Autowired
	QuizInterface quizInterface;

	public ResponseEntity<String> createQuiz(String category, Integer numQ, String title) {
		
		List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestionIds(questions);
		quizDao.save(quiz);
		return new ResponseEntity<>("success", HttpStatus.CREATED);

	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
		Quiz quiz = quizDao.findById(id).get();
		
		ResponseEntity<List<QuestionWrapper>> qwrapper = quizInterface.getQuestionsFromId(quiz.getQuestionIds());
		
		return qwrapper;
	}

	public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
		
		return quizInterface.getScore(responses);
    }

}
