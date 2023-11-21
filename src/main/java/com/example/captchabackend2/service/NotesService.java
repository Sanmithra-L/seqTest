package com.example.captchabackend2.service;

import com.example.captchabackend2.entity.UserNotes;
import com.example.captchabackend2.repo.NotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class NotesService {
    @Autowired
    NotesRepo notesRepo;
    public UserNotes addNote(UserNotes userNote){
        return notesRepo.save(userNote);
    }
    public UserNotes findNote(Integer note_id){
        return notesRepo.findById(note_id).orElse(null);
    }
    public String updateNote(UserNotes userNote,Integer note_id){
        UserNotes actualNote=notesRepo.findById(note_id).orElse(null);
        if(actualNote==null){
            return "Note not found";
        }
        actualNote.setData(userNote.getData());
        notesRepo.save(actualNote);
        return "Note updated successfully";
    }
    public String deleteNote(Integer note_id){
        notesRepo.deleteById(note_id);
        return "Note Deleted Successfully";
    }
    public List<UserNotes> findUserNotesbyUserId(Integer userId){
        return notesRepo.findUserNotesByUserId(userId);
    }
    public UserNotes setCompletedNote(Integer userId){
        UserNotes temp=notesRepo.findById(userId).orElse(null);
        assert temp != null;
        temp.setCompleted(true);
        return notesRepo.save(temp);
    }
}
