package com.astrapay.controller.v1.note;

import com.astrapay.dto.ApiResponse;
import com.astrapay.dto.NoteDto;
import com.astrapay.entity.Note;
import com.astrapay.exception.NoteExistsException;
import com.astrapay.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<ApiResponse<List<NoteDto>>> getAllNotes() {
        try {
            List<Note> notes = noteService.getAllNotes();
            return ApiResponse.ok("Notes retrieved successfully", NoteDto.fromEntities(notes));
        } catch (Exception e) {
            log.error("Error retrieving notes: ", e);
            return ApiResponse.internalServerError("Failed to retrieve notes: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteDto>> getNoteById(@PathVariable Long id) {
        try {
            Note note = noteService.getNoteById(id);
            if (note != null) {
                return ApiResponse.ok("Note retrieved successfully", NoteDto.fromEntity(note));
            }
            return ApiResponse.notFound("Note not found with id: " + id);
        } catch (Exception e) {
            log.error("Error retrieving note: ", e);
            return ApiResponse.internalServerError("Failed to retrieve note: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NoteDto>> createNote(@RequestBody NoteDto noteDto) {
        try {
            Note note = noteService.createNote(noteDto.getTitle(), noteDto.getContent());
            return ApiResponse.ok("Note created successfully", NoteDto.fromEntity(note));
        } catch (NoteExistsException e) {
            log.warn("Note creation conflict: {}", e.getMessage());
            return ApiResponse.conflict(e.getMessage());
        } catch (Exception e) {
            log.error("Error creating note: ", e);
            return ApiResponse.internalServerError("Failed to create note: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoteDto>> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto) {
        try {
            Note updatedNote = noteService.updateNote(id, noteDto.getTitle(), noteDto.getContent());
            if (updatedNote != null) {
                return ApiResponse.ok("Note updated successfully", NoteDto.fromEntity(updatedNote));
            }
            return ApiResponse.notFound("Note not found with id: " + id);
        } catch (NoteExistsException e) {
            log.warn("Note update conflict: {}", e.getMessage());
            return ApiResponse.conflict(e.getMessage());
        } catch (Exception e) {
            log.error("Error updating note: ", e);
            return ApiResponse.internalServerError("Failed to update note: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ApiResponse.ok("Note deleted successfully", null);
        } catch (Exception e) {
            log.error("Error deleting note: ", e);
            return ApiResponse.internalServerError("Failed to delete note: " + e.getMessage());
        }
    }
}
