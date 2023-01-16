package com.rushional.articles_task;

import com.rushional.articles_task.models.DailyArticlesCountRow;
import com.rushional.articles_task.models.entities.AppUser;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.entities.Role;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import com.rushional.articles_task.models.repositories.RoleRepository;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @Autowired
    private RoleRepository roleRepository;

    private Role adminRole = new Role();
    private Role userRole = new Role();
    private ArrayList<Role> adminRoles = new ArrayList<>();
    private ArrayList<Role> userRoles = new ArrayList<>();
    private AppUser admin1 = new AppUser();
    private AppUser user1 = new AppUser();

    @BeforeEach
    void init(){
        adminRole.setName("ADMIN");
        userRole.setName("USER");
        adminRoles.add(adminRole);
        userRoles.add(userRole);
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        user1.setUsername("Just your average user");
//        It says "Average password"
        user1.setPassword("$2a$08$uGbeTr5L1YMwQDuaN4lWcutgiunyD6kQIp11WBQZutp1O64XOL1my");
        user1.setRoles(userRoles);

        admin1.setUsername("This user has a dark secret");
//        It says "Dark secret"
        admin1.setPassword("$2a$08$pmYxOXfSKLwZprsau.2.Ouhj5uKOmI3eItinaSkWWsNZPMKMfftJK");
        admin1.setRoles(adminRoles);

        userRepository.saveAndFlush(admin1);
        userRepository.saveAndFlush(user1);
    }

    @Test
    void addingNormalArticlesShouldSucceed() {
        articleRepository.saveAndFlush(new Article(
            "Just your average article",
            admin1,
            "Content of the article. Something about ants going in circles, not being able to stop. Looks cool, but kinda creepy",
            ZonedDateTime.now())
        );

        articleRepository.saveAndFlush(new Article(
            "This title has 100 symbols!BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
            user1,
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
            admin1,
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
        articleWithNoContent.setAuthor(user1);
//        *Not* setting the content
        articleWithNoContent.setPostDate(ZonedDateTime.now());
        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(articleWithNoContent));
    }

    @Test
    void savingArticleWithLongTitleShouldFail() {
        assertThrows(ConstraintViolationException.class, () -> articleRepository.saveAndFlush(new Article(
            "+This title has 101 symbols!BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
            admin1,
            "I sure hope I didn't overdo it with the fancy title!",
            ZonedDateTime.now())
        ));
    }

    @Test
    void dailyCountsShouldReturnCorrectValues() {
        articleRepository.saveAndFlush(new Article(
            "Last week's article",
            admin1,
            "Content of the article. Something about ants going in circles, not being able to stop. Looks cool, but kinda creepy",
            ZonedDateTime.now().minusDays(7))
        );

        articleRepository.saveAndFlush(new Article(
            "First article today",
            user1,
            "Contents gonna be content",
            ZonedDateTime.now())
        );

        articleRepository.saveAndFlush(new Article(
            "2nd Article today",
            admin1,
            "I'm out of cool stuff to say",
            ZonedDateTime.now())
        );

        articleRepository.saveAndFlush(new Article(
            "An article yesterday!",
            user1,
            "But I do believe all the other stuff I was saying was indeed cool! " +
                "It was nice having such a great audience! See you later when you totally hire me",
            ZonedDateTime.now().minusDays(1))
        );

        List<DailyArticlesCountRow> dailyCounts = articleRepository.getDailyPostCountsForPastWeek();

        assertEquals(2, dailyCounts.size());
        // Only one post yesterday
        assertEquals(1, dailyCounts.get(0).getArticlesAmount());
        assertEquals(Date.valueOf(LocalDate.now().minusDays(1)), dailyCounts.get(0).getDate());
        // Two posts today
        assertEquals(2, dailyCounts.get(1).getArticlesAmount());
        assertEquals(Date.valueOf(LocalDate.now()), dailyCounts.get(1).getDate());
    }

    @Test
    void dailyCountsShouldNotIncludeOldDays() {
        articleRepository.saveAndFlush(new Article(
            "Last week's article",
            admin1,
            "Content of the article. Something about ants going in circles, not being able to stop. Looks cool, but kinda creepy",
            ZonedDateTime.now().minusDays(7))
        );

        articleRepository.saveAndFlush(new Article(
            "First article today",
            user1,
            "Contents gonna be content",
            ZonedDateTime.now())
        );

        List<DailyArticlesCountRow> dailyCounts = articleRepository.getDailyPostCountsForPastWeek();

        assertEquals(1, dailyCounts.size());
    }
}
