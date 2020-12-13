package com.reactive.mongodb.reactive.service;

import com.reactive.mongodb.reactive.model.Student;
import com.reactive.mongodb.reactive.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Flux<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Mono<Student> save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Mono<Student> findById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return studentRepository.deleteById(id);
    }
}
