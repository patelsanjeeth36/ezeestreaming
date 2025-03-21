package com.video.ezeestreaming.controller;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.video.ezeestreaming.exception.VideoNotFoundException;
import com.video.ezeestreaming.model.Video;
import com.video.ezeestreaming.service.VideoService;
 

@RestController
@RequestMapping("/api/videos")
public class VideoController {

	@Autowired
	public VideoService videoService;

	@PostMapping("/publish")
	public ResponseEntity<Video> publishVideo(@RequestBody Video video) {
		return ResponseEntity.status(HttpStatus.CREATED).body(videoService.publishVideo(video));

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Video> updateVideo(@PathVariable Long id, @RequestBody Video video) {
		return ResponseEntity.ok(videoService.updateVideo(id, video));
	}

	@DeleteMapping("/delist/{id}")
	public ResponseEntity<String> delistVideo(@PathVariable Long id) {
		videoService.delistVideo(id);
		return ResponseEntity.ok("Video delisted successfully.");
	}

	@GetMapping("/{id}")
	public ResponseEntity<Video> getVideo(@PathVariable Long id) {
		return ResponseEntity.ok(videoService.getVideo(id)
				.orElseThrow(() -> new VideoNotFoundException("Video with ID " + id + " not found.")));
	}

	@GetMapping("/{id}/play")
	public ResponseEntity<String> playVideo(@PathVariable Long id) {
		return ResponseEntity.ok(videoService.playVideo(id));
	}

	@GetMapping("/list")
	public ResponseEntity<List<Video>> listAvailableVideos() {
		List<Video> videos = videoService.listAvailableVideos().stream()
				.map(video -> new Video(video.getId(), video.getTitle(), null, video.getDirector(), null, 0,
						video.getGenre(), video.getRunningTime(), false, 0, 0, null, null, null))
				.collect(Collectors.toList());
		return ResponseEntity.ok(videos);
	}

	@GetMapping("/search")
	public ResponseEntity<List<Video>> searchVideos(@RequestParam(required = false) String director,
			@RequestParam(required = false) String title) {
		List<Video> videos;
		if (director != null) {
			videos = videoService.searchVideosByDirector(director);
		} else if (title != null) {
			videos = videoService.searchVideosByTitle(title);
		} else {
			videos = List.of();
		}
		return ResponseEntity.ok(videos);
	}

	@GetMapping("/{id}/engagement")
	public ResponseEntity<String> getEngagementStats(@PathVariable Long id) {
		int impressions = videoService.getVideoImpressions(id);
		int views = videoService.getVideoViews(id);
		return ResponseEntity.ok("Impressions: " + impressions + ", Views: " + views);
	}
}
