package com.rushional.articles_task;

import com.rushional.articles_task.models.entities.AppUser;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import com.rushional.articles_task.models.repositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryUnitTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
        .withUsername("postgres")
        .withPassword("ostkaka");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    private AppUser testUser1 = new AppUser("Just your average user");
    private AppUser testUser2 = new AppUser("This user has a dark secret");

    @BeforeEach
    void init(){
        userRepository.saveAndFlush(testUser1);
        userRepository.saveAndFlush(testUser2);
    }

    @Test
    void addingNormalArticles() {
        articleRepository.saveAndFlush(new Article(
            "Just your average article",
            testUser1,
            "Content of the article. Something about ants going in circles, not being able to stop. Looks cool, but kinda creepy",
            ZonedDateTime.now())
        );

        articleRepository.saveAndFlush(new Article(
            "This title has 100 symbols!BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
            testUser2,
            "With such a cool title I don't really need to actually write an article, I'll get views anyway! Work smarter, not harder!",
            ZonedDateTime.now())
        );

        List<Article> allArticles = articleRepository.findAll();

        assertEquals(2, allArticles.size());
    }

    @Test
    void savingArticleWithMissingFieldShouldFail() {
        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(new Article(
            "Title",
            testUser1,
            null,
            ZonedDateTime.now())
        ));

        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(new Article(
            "Title",
            null,
            "The most boring article ever written, which makes it interesting and thus not boring",
            ZonedDateTime.now())
        ));

        Article articleWithNoContent = new Article();
        articleWithNoContent.setTitle("Definitely not null");
        articleWithNoContent.setAuthor(testUser2);
//        *Not* setting the content
        articleWithNoContent.setPostDate(ZonedDateTime.now());
        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(articleWithNoContent));
    }

    @Test
    void savingArticleWithLongTitleShouldFail() {
        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(new Article(
            "+This title has 101 symbols!BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
            testUser1,
            "I sure hope I didn't overdo it with the fancy title!",
            ZonedDateTime.now())
        ));
    }
}
