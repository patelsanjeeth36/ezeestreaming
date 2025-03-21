package com.video.ezeestreaming.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.video.ezeestreaming.exception.VideoNotFoundException;
import com.video.ezeestreaming.model.Video;
import com.video.ezeestreaming.repository.VideoRepository;

@Service
public class VideoService {

	@Autowired
	public VideoRepository videoRepository;

	public Video publishVideo(Video video) {
		return videoRepository.save(video);
	}

	public Video updateVideo(Long id, Video updatedVideo) {
		return videoRepository.findById(id).map(video -> {
			video.setTitle(updatedVideo.getTitle());
			video.setSynopsis(updatedVideo.getSynopsis());
			video.setDirector(updatedVideo.getDirector());
			video.setCast(updatedVideo.getCast());
			video.setYearOfRelease(updatedVideo.getYearOfRelease());
			video.setGenre(updatedVideo.getGenre());
			video.setRunningTime(updatedVideo.getRunningTime());
			video.setVideoUrl(updatedVideo.getVideoUrl());
			return videoRepository.save(video);
		}).orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found."));
	}

	public void delistVideo(Long id) {
		Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found."));
        video.setDeleted(true);
        videoRepository.save(video);
	}

	public Optional<Video> getVideo(Long id) {
		Optional<Video> video = videoRepository.findById(id);
        video.ifPresent(v -> {
            v.setImpressions(v.getImpressions() + 1);
            videoRepository.save(v);
        });
        return video;
	}

	public String playVideo(Long id) {
		Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found."));
        video.setViews(video.getViews() + 1);
        videoRepository.save(video);
        return video.getVideoUrl();
	}

	public List<Video> listAvailableVideos() {
		return videoRepository.findByDeletedFalse();
	}

	public List<Video> searchVideosByDirector(String director) {
		List<Video> findByDirectorList = videoRepository.findByDirectorContainingIgnoreCaseAndDeletedFalse(director);
		return findByDirectorList.stream()
				.map(video -> new Video(video.getId(), video.getTitle(), null, video.getDirector(), null, 0,
						video.getGenre(), video.getRunningTime(), false, 0, 0, null, null, null))
				.collect(Collectors.toList());

	}

	public List<Video> searchVideosByTitle(String title) {

		List<Video> findByTitleList = videoRepository.findByTitleContainingIgnoreCaseAndDeletedFalse(title);
		return findByTitleList.stream()
				.map(video -> new Video(video.getId(), video.getTitle(), null, video.getDirector(), null, 0,
						video.getGenre(), video.getRunningTime(), false, 0, 0, null, null, null))
				.collect(Collectors.toList());
	}

	public int getVideoImpressions(Long id) {
		return videoRepository.findById(id).map(Video::getImpressions)
				.orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found."));
				 
	}

	public int getVideoViews(Long id) {
		return videoRepository.findById(id).map(Video::getViews)
				.orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found."));
	}

}
