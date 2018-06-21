package com.piotrkalitka.noteman.repository;

import com.piotrkalitka.noteman.model.Note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByCreatedBy(Long userId);

}