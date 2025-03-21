package com.video.ezeestreaming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.video.ezeestreaming.model.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

	List<Video> findByDeletedFalse();

	List<Video> findByDirectorContainingIgnoreCaseAndDeletedFalse(String director);

	List<Video> findByTitleContainingIgnoreCaseAndDeletedFalse(String title);
}
