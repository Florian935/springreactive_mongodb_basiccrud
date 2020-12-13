package com.reactive.mongodb.reactive.controller;

import com.reactive.mongodb.reactive.model.Student;
import com.reactive.mongodb.reactive.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/reactive")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public Flux<Student> all() {
        return studentService.findAll();
    }

    @PostMapping("/students")
    public Mono<Student> create(@RequestBody Student student) {
        student.setId(UUID.randomUUID().toString());
        return studentService
                .save(student)
                .log();
    }

    @GetMapping("/students/{id}")
    public Mono<Student> get(@PathVariable String id) {
        return studentService.findById(id);
    }

    @PutMapping("/students/{id}")
    public Mono<Student> update(@RequestBody Student student, @PathVariable("id") String id) {
        return this.studentService
                .findById(id)
                .map(
                        s -> {
                            s.setName(student.getName());
                            return s;   // Retourne un Student encapsulé dans un Mono
                        }
                )
                //.map(studentService::save)    -> retournerai un Mono<Student> (type de retour de la méthode save)
                // encapsulé dans un Mono (donc type de retour: Mono<Mono<Student>>). A la place, on veut un
                // Mono<Student> en type de retour, on utilise donc flatMap ci-dessous
                .flatMap(studentService::save);
                // -> le contenu de flatMap doit obligatoirement retourner un Publisher.
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Mono<Void>> delete(@PathVariable String id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(studentService.deleteById(id)
                );
    }
 }
