package com.video.ezeestreaming.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.video.ezeestreaming.exception.VideoNotFoundException;
import com.video.ezeestreaming.model.Video;
import com.video.ezeestreaming.repository.VideoRepository;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {
	
	@Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    private Video testVideo;

    @BeforeEach
    void setUp() {
        testVideo = new Video();
        testVideo.setId(1L);
        testVideo.setTitle("Test Movie");
        testVideo.setDirector("John Doe");
        testVideo.setDeleted(false);
    }

    @Test
    void publishVideo_ShouldSaveAndReturnVideo() {
        when(videoRepository.save(testVideo)).thenReturn(testVideo);

        Video savedVideo = videoService.publishVideo(testVideo);

        assertNotNull(savedVideo);
        assertEquals("Test Movie", savedVideo.getTitle());
        verify(videoRepository, times(1)).save(testVideo);
    }

    @Test
    void updateVideo_ShouldUpdateAndReturnVideo() {
        Video updatedVideo = new Video();
        updatedVideo.setTitle("Updated Movie");

        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(videoRepository.save(any(Video.class))).thenReturn(updatedVideo);

        Video result = videoService.updateVideo(1L, updatedVideo);

        assertNotNull(result);
        assertEquals("Updated Movie", result.getTitle());
        verify(videoRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void updateVideo_ShouldThrowException_WhenVideoNotFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.updateVideo(1L, testVideo));
        verify(videoRepository, times(1)).findById(1L);
    }

    @Test
    void delistVideo_ShouldMarkVideoAsDeleted() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));

        videoService.delistVideo(1L);

        assertTrue(testVideo.isDeleted());
        verify(videoRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).save(testVideo);
    }

    @Test
    void getVideo_ShouldReturnVideoAndIncrementImpressions() {
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));

        Optional<Video> result = videoService.getVideo(1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getImpressions());
        verify(videoRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void playVideo_ShouldReturnVideoUrlAndIncrementViews() {
        testVideo.setVideoUrl("http://video.url");

        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo);

        String videoUrl = videoService.playVideo(1L);

        assertEquals("http://video.url", videoUrl);
        assertEquals(1, testVideo.getViews());
        verify(videoRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void playVideo_ShouldThrowException_WhenVideoNotFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.playVideo(1L));
        verify(videoRepository, times(1)).findById(1L);
    }
}
