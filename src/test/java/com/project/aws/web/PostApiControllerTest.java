package com.project.aws.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aws.domain.posts.Posts;
import com.project.aws.domain.posts.PostsRepository;
import com.project.aws.web.dto.PostsSaveRequestDto;
import com.project.aws.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    public void Posts_save() throws Exception {
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        //when
        String url = "http://localhost:"+port+"/api/v1/posts";

//        //when
//        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url,dto, Long.class);
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getTitle()).isEqualTo(title);
//        assertThat(all.get(0).getContent()).isEqualTo(content);

        //when
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all2 = postsRepository.findAll();
        assertThat(all2.get(0).getTitle()).isEqualTo(title);
        assertThat(all2.get(0).getContent()).isEqualTo(content);
    }
    @Test
    @WithMockUser(roles="USER")
    public void Posts_update() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                            .title("title")
                            .content("content")
                            .author("author")
                            .build());
        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";
        PostsUpdateRequestDto requestDto =
             PostsUpdateRequestDto.builder()
                                   .title(expectedTitle)
                                   .content(expectedContent)
                                   .build();

        String url = "http://localhost:"+port+"/api/v1/posts/"
                +updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity =
                new HttpEntity<>(requestDto);

//        //when
//        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT,requestEntity,Long.class);
//
//        //then
//        assertThat(responseEntity.getStatusCode())
//                .isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);
//
//        List<Posts> all = postsRepository.findAll();
//        assertThat(all.get(0).getTitle())
//                .isEqualTo(expectedTitle);
//        assertThat(all.get(0).getContent())
//                .isEqualTo(expectedContent);

        //when
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all2 = postsRepository.findAll();
        assertThat(all2.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all2.get(0).getContent()).isEqualTo(expectedContent);
    }

}
