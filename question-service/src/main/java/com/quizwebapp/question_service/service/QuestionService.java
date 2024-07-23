package com.quizwebapp.question_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizwebapp.question_service.dao.QuestionDao;
import com.quizwebapp.question_service.model.Question;
import com.quizwebapp.question_service.model.QuestionWrapper;
import com.quizwebapp.question_service.model.Response;

@Service
public class QuestionService {

	@Autowired
	QuestionDao questionDao;

	public ResponseEntity<List<Question>> getAllQuestions() {
		try {
			return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		try {
			return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) {
		questionDao.save(question);
		try {
			return new ResponseEntity<>("success", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> deleteQuestion(int id) {
		questionDao.deleteById(id);
		try {

			return new ResponseEntity<>("deleted", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

	}

	public ResponseEntity<String> updateQuestion(Question question) {
		Question existingQuestion = questionDao.findByQuestionTitle(question.getQuestionTitle());
		if (existingQuestion != null) {
			if (question.getOption1() != null) {
				existingQuestion.setOption1(question.getOption1());
			}
			if (question.getOption2() != null) {
				existingQuestion.setOption2(question.getOption2());
			}
			if (question.getOption3() != null) {
				existingQuestion.setOption3(question.getOption3());
			}
			if (question.getOption4() != null) {
				existingQuestion.setOption4(question.getOption4());
			}
			if (question.getQuestionTitle() != null) {
				existingQuestion.setQuestionTitle(question.getQuestionTitle());
			}
			if (question.getRightAnswer() != null) {
				existingQuestion.setRightAnswer(question.getRightAnswer());
			}
			if (question.getDifficultyLevel() != null) {
				existingQuestion.setDifficultyLevel(question.getDifficultyLevel());
			}
			if (question.getCategory() != null) {
				existingQuestion.setCategory(question.getCategory());
			}
			questionDao.save(existingQuestion);
		} else {
			questionDao.save(question);
		}
		try {

			return new ResponseEntity<>("updated", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
		Pageable pageable = PageRequest.of(0, numQuestions);
		List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, pageable);

		return new ResponseEntity<>(questions,HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
		List<QuestionWrapper> wrappers = new ArrayList<>();
		for(Integer questionId:questionIds) {
			Question question=questionDao.findById(questionId).get();
			QuestionWrapper qw = new QuestionWrapper(question.getId(),question.getQuestionTitle(), question.getOption1(),
					question.getOption2(), question.getOption3(), question.getOption4());
			wrappers.add(qw);
		}
		
		return new ResponseEntity<>(wrappers,HttpStatus.OK);
	}

	public ResponseEntity<Integer> calculateResult(List<Response> responses) {
		
		int right =0;
		for(Response response:responses) {
			Question question = questionDao.findById(response.getId()).get();
			if(question.getRightAnswer().equals(response.getResponse())) {
				right++;
			}
		}
		return new ResponseEntity<>(right,HttpStatus.OK);
	}

	

}
