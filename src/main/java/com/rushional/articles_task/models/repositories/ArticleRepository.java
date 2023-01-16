package com.rushional.articles_task.models.repositories;

import com.rushional.articles_task.models.DailyArticlesCountRow;
import com.rushional.articles_task.models.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

//    There isn't a direct conversion from timestamp to date, so I had to come up with a workaround
    @Query("SELECT new com.rushional.articles_task.models.DailyArticlesCountRow(to_date(to_char(article.postDate,'DD-MM-YYYY'),'DD-MM-YYYY'), COUNT(article)) "
        + " FROM Article article "
        + " WHERE to_date(to_char(article.postDate,'DD-MM-YYYY'),'DD-MM-YYYY') BETWEEN CURRENT_DATE - 6 AND CURRENT_DATE "
        + " group by to_date(to_char(article.postDate,'DD-MM-YYYY'),'DD-MM-YYYY') "
        + "order by to_date(to_char(article.postDate,'DD-MM-YYYY'),'DD-MM-YYYY') "
    )
    public List<DailyArticlesCountRow> getDailyPostCountsForPastWeek();
}
