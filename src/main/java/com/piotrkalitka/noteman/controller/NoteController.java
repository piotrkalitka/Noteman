package com.piotrkalitka.noteman.controller;

import com.piotrkalitka.noteman.exception.EntityNotFoundException;
import com.piotrkalitka.noteman.exception.UnauthorisedException;
import com.piotrkalitka.noteman.model.Note;
import com.piotrkalitka.noteman.payload.AddNoteRequest;
import com.piotrkalitka.noteman.payload.UpdateNoteRequest;
import com.piotrkalitka.noteman.repository.NoteRepository;
import com.piotrkalitka.noteman.repository.UserRepository;
import com.piotrkalitka.noteman.security.CurrentUser;
import com.piotrkalitka.noteman.security.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RequestMapping("/api/note")
@RestController
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createNote(@Valid @RequestBody AddNoteRequest requestBody) {
        Note note = new Note(requestBody.getTitle(), requestBody.getContent());
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@CurrentUser UserPrincipal userPrincipal, @PathVariable("id") Long noteId) {
        Note note = noteRepository.findById(noteId).orElseThrow(EntityNotFoundException::new);
        if (!note.getCreatedBy().equals(userPrincipal.getId())) {
            throw new UnauthorisedException();
        }
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public ResponseEntity<?> getNotes(@CurrentUser UserPrincipal userPrincipal) {
        List<Note> userNotes = noteRepository.findByCreatedBy(userPrincipal.getId());
        return ResponseEntity.ok(userNotes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@CurrentUser UserPrincipal userPrincipal, @PathVariable("id") Long noteId, @Valid @RequestBody UpdateNoteRequest requestBody) {
        Note note = noteRepository.findById(noteId).orElseThrow(EntityNotFoundException::new);
        if (!note.getCreatedBy().equals(userPrincipal.getId())) {
            throw new UnauthorisedException();
        }
        note.update(new Note(requestBody.getTitle(), requestBody.getContent()));
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<?> deleteNote(@CurrentUser UserPrincipal userPrincipal, @PathVariable("ids") List<Long> noteIds) {
        List<Note> notes = new ArrayList<>();
        for (Long noteId : noteIds) {
            Note note = noteRepository.findById(noteId).orElseThrow(EntityNotFoundException::new);
            if (!note.getCreatedBy().equals(userPrincipal.getId()))
                throw new UnauthorisedException();
            notes.add(note);
        }
        for (Note note : notes) {
            noteRepository.delete(note);
        }
        return ResponseEntity.ok().build();
    }

}