package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
//        System.out.println(form.toString());
        //1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(article.toString());
//        System.out.println(article.toString());
        //2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
//        System.out.println(saved.toString());
        return "redirect:/articles/" + saved.getId(); //리다이렉트
    }

    @GetMapping("/articles/{id}") // 데이터 조회 접수 요청
    public String show(@PathVariable Long id, Model model) { //매개변수로 id 가져오기
        log.info("id = " + id);
//      1. id를 조회해 db에서 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
//      2. 모델에서 데이터 등록하기
        model.addAttribute("article", articleEntity);
//      3. 뷰 페이지 반환하기
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
//        1. db에서 모든 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
//        2. 가져온 Article 묶음을 모델에 등록
        model.addAttribute("articleList", articleEntityList);
//        3. 사용자에게 보여줄 뷰 페이지 설정
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
//        수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
//        모델이 데이터 등록
        model.addAttribute("article", articleEntity);
//        뷰 페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());
//        1.dto를 엔티티로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
//        2.엔티티를 db에 저장
//        2-1.db에서 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

//        2-2.기존 데이터값 갱신
        if (target != null) {
            articleRepository.save(articleEntity);
        }
//        3.수정결과 페이지 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("Request Deleting");
//        1.삭제 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
//        2.대상 엔티티 삭제하기
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "Delete Complete!");
        }
//        3.결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}
