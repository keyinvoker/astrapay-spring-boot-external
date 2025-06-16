package com.astrapay.controller.v1.note;

import com.astrapay.dto.ApiResponse;
import com.astrapay.entity.Note;
import com.astrapay.exception.NoteExistsException;
import com.astrapay.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotes() {
        try {
            List<Note> notes = noteService.getAllNotes();
            return ResponseEntity.ok(ApiResponse.success("Notes retrieved successfully", notes));
        } catch (Exception e) {
            log.error("Error retrieving notes: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to retrieve notes: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Note>> getNoteById(@PathVariable Long id) {
        try {
            Note note = noteService.getNoteById(id);
            if (note != null) {
                return ResponseEntity.ok(ApiResponse.success("Note retrieved successfully", note));
            }
            return ResponseEntity.ok(ApiResponse.notFound("Note not found with id: " + id));
        } catch (Exception e) {
            log.error("Error retrieving note: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to retrieve note: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Note>> createNote(@RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note.getTitle(), note.getContent());
            return ResponseEntity.ok(ApiResponse.success("Note created successfully", createdNote));
        } catch (NoteExistsException e) {
            log.warn("Note creation conflict: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating note: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to create note: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Note>> updateNote(@PathVariable Long id, @RequestBody Note note) {
        try {
            Note updatedNote = noteService.updateNote(id, note.getTitle(), note.getContent());
            if (updatedNote != null) {
                return ResponseEntity.ok(ApiResponse.success("Note updated successfully", updatedNote));
            }
            return ResponseEntity.ok(ApiResponse.notFound("Note not found with id: " + id));
        } catch (NoteExistsException e) {
            log.warn("Note update conflict: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating note: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to update note: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.ok(ApiResponse.success("Note deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting note: ", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to delete note: " + e.getMessage()));
        }
    }
}
