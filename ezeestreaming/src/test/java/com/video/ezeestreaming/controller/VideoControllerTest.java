package com.video.ezeestreaming.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.ezeestreaming.model.Video;
import com.video.ezeestreaming.service.VideoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VideoService videoService;
    
    @Test
    void shouldPublishVideo() throws Exception {
        Video video = new Video();
        video.setTitle("Test Movie");

        String jsonRequest = objectMapper.writeValueAsString(video);

        mockMvc.perform(post("/api/videos/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Movie"));
    }

    @Test
    void shouldUpdateVideo() throws Exception {
        Video video = new Video();
        video.setTitle("Updated Movie");

        String jsonRequest = objectMapper.writeValueAsString(video);

        mockMvc.perform(put("/api/videos/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Movie"));
    }

    @Test
    void shouldReturnNotFound_WhenUpdatingNonExistentVideo() throws Exception {
        Video video = new Video();
        video.setTitle("Non-Existent Movie");

        String jsonRequest = objectMapper.writeValueAsString(video);

        mockMvc.perform(put("/api/videos/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDelistVideo() throws Exception {
        mockMvc.perform(delete("/api/videos/delist/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Video delisted successfully."));
    }

    @Test
    void shouldReturnNotFound_WhenPlayingNonExistentVideo() throws Exception {
        mockMvc.perform(get("/api/videos/999/play"))
                .andExpect(status().isNotFound());
    } 

}
