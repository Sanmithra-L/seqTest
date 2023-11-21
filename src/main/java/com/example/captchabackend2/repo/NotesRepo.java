package com.example.captchabackend2.repo;

import com.example.captchabackend2.entity.UserNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepo extends JpaRepository<UserNotes,Integer> {
    @Query("SELECT un FROM UserNotes un WHERE un.user_id = :userId")
    List<UserNotes> findUserNotesByUserId(Integer userId);
}
