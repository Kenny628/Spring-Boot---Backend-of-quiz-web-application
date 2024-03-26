package com.example.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.quizapp.dao.QuestionDao;
import com.example.quizapp.dao.QuizDao;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuestionWrapper;
import com.example.quizapp.model.Quiz;
import com.example.quizapp.model.Response;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions=questionDao.findRandomQuestionsByCategory(category,numQ);

        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("success",HttpStatus.OK);
    }
    public ResponseEntity<List<QuestionWrapper>> getQuiz(int id) {

        Optional<Quiz> qd=quizDao.findById(id);
        List<Question> qsList=qd.get().getQuestions();
        List<QuestionWrapper> qwList=new ArrayList<>();
        for(Question questions : qsList){
            QuestionWrapper qw=new QuestionWrapper(questions.getId(),questions.getQuestionTitle(),questions.getOption1(),questions.getOption2(),questions.getOption3(),questions.getOption4());
            qwList.add(qw);
        }
        return new ResponseEntity<>(qwList,HttpStatus.OK);
    }
    public ResponseEntity<Integer> submitAnswers(int id, List<Response> responses) {
        Optional<Quiz> qd=quizDao.findById(id);
        List<Question> qsList=qd.get().getQuestions();
        int count =0;
        for(Response response : responses){
            for(Question question:qsList){
            if(response.getId()==question.getId() && response.getResponse().equals(question.getRightAnswer())){
                count++;
            }
        }
        }
        return new ResponseEntity<>(count,HttpStatus.OK);
    }

}
