package com.astrapay.service;

import com.astrapay.entity.Note;
import com.astrapay.exception.NoteExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class NoteService {
    // NOTE: This handles temporary app storage. Data is lost upon app close.
    private final Map<Long, Note> notes = new ConcurrentHashMap<>();

    private final AtomicLong idCounter = new AtomicLong(1);

    public Note createNote(String title, String content) {
        // Check if a note with the same title already exists
        boolean titleExists = notes.values().stream()
                .anyMatch(note -> note.getTitle().equals(title));

        if (titleExists) {
            throw new NoteExistsException(title);
        }

        Note note = new Note();
        note.setId(idCounter.getAndIncrement());
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        notes.put(note.getId(), note);
        return note;
    }

    public List<Note> getAllNotes() {
        return new ArrayList<>(notes.values());
    }

    public Note getNoteById(Long id) {
        return notes.get(id);
    }

    public void deleteNote(Long id) {
        notes.remove(id);
    }

    public Note updateNote(Long id, String title, String content) {
        Note note = notes.get(id);
        if (note != null) {
            // Check if the new title conflicts with any other note
            boolean titleExists = notes.values().stream()
                    .anyMatch(n -> !n.getId().equals(id) && n.getTitle().equals(title));

            if (titleExists) {
                throw new NoteExistsException(title);
            }

            note.setTitle(title);
            note.setContent(content);
            note.setUpdatedAt(LocalDateTime.now());
            notes.put(id, note);
        }
        return note;
    }
}
