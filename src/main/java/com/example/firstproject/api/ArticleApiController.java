package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ArticleApiController {
    @Autowired
    private ArticleRepository articleRepository;

    //    GET
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleRepository.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    //    POST
    @PostMapping("/api/articles")
    public Article create(@RequestBody ArticleForm dto) {
        Article article = dto.toEntity();
        return articleRepository.save(article);
    }

    //    PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Object> update(@Autowired Long id, @RequestBody ArticleForm dto) {
//        1. dto 를 엔티티로 변환
        Article article = dto.toEntity();
        log.info("id : {}, article : {}", id, article.toString());
//        2. 타깃 조회
        Article target = articleRepository.findById(id).orElse(null);
//        3. 잘못된 요청 처리
        if (target == null | id != article.getId()) {
            log.info("Wrong Request! id: {}, articles: {}", id, article.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
//        4. 업데이트 및 정상응답
        target.patch(article);
        Article updated = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    //    DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
//        1.대상 찾기
        Article target = articleRepository.findById(id).orElse(null);
//        2.잘못된 요청 처리
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
//        3.대상 삭제
        articleRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
