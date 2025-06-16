package com.astrapay.dto;

import com.astrapay.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {
    private Long id;
    private String title;
    private String content;

    public static NoteDto fromEntity(Note note) {
        return new NoteDto(note.getId(), note.getTitle(), note.getContent());
    }

    public static List<NoteDto> fromEntities(List<Note> notes) {
        return notes.stream()
                .map(NoteDto::fromEntity)
                .collect(Collectors.toList());
    }
}
