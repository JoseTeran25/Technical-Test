package hr.kanezi.postgres.fts;

import hr.kanezi.postgres.fts.actor.ActorService;
import hr.kanezi.postgres.fts.actor.ActorView;
import hr.kanezi.postgres.fts.movies.MoviesService;
import hr.kanezi.postgres.fts.movies.MoviesView;
import hr.kanezi.postgres.fts.quotes.QuotesService;
import hr.kanezi.postgres.fts.search.FtsDocuments;
import hr.kanezi.postgres.fts.search.FtsService;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@Value
@Log4j2
public class SearchController {

    FtsService ftsService;
    ActorService actorService;
    MoviesService moviesService;
    QuotesService quotesService;


    @GetMapping
    public String home(Model model) {
        List<ActorView> authors = actorService.getAllActors();
        List<String> types = new ArrayList<>();
        types.add("QUOTES");
        types.add("MOVIES");
        List<MoviesView> movies = moviesService.getMoviesViewRepository().findAll();
        List<String> authorsNames = quotesService.getAllAuthors();
        List<String> genres = moviesService.getAllGenres();
        model.addAttribute("types", types);
        model.addAttribute("actors", authors);
        model.addAttribute("movies", movies);
        model.addAttribute("authors", authorsNames);
        model.addAttribute("genres", genres);
        return "search";
    }

    @PostMapping("/search")
    public String search(String q, RedirectAttributes attributes) {
        log.info("search for : {}", q);
        attributes.addFlashAttribute("q", q);

        List<FtsDocuments> docs = ftsService.search(q, 25L);
        attributes.addFlashAttribute("docs", docs);

        if (docs.isEmpty()) {
            attributes.addFlashAttribute("misspelling", ftsService.misspellings(q));
        }

        return "redirect:/";
    }

    //Created by Jose Terán
    @PostMapping("/advancedSearch")
    public String advancedSearch(@RequestParam(required = false) String type,
                                 @RequestParam(required = false) String movie,
                                 @RequestParam(required = false) String actor,
                                 @RequestParam(required = false) String author,
                                 @RequestParam(required = false) String genre,
                                 @RequestParam(required = false) String keyword,
                                RedirectAttributes attributes) {

        if (type != null && type.isEmpty()) {
            type = null;
        }
        if (movie != null && movie.isEmpty()) {
            movie = null;
        }
        if (actor != null && actor.isEmpty()) {
            actor = null;
        }
        if (author != null && author.isEmpty()) {
            author = null;
        }
        if (keyword != null && keyword.isEmpty()) {
            keyword = null;
        }
        if (genre != null && genre.isEmpty()) {
            genre = null;
        }

        System.out.println("Type: " + type);
        System.out.println("Movie: " + movie);
        System.out.println("Actor: " + actor);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Keyword: " + keyword);
        List<FtsDocuments> docs = ftsService.advancedSearch(type, movie, actor, author,genre, keyword, 100L);
        attributes.addFlashAttribute("docs", docs);
        if (docs.isEmpty()) {
            attributes.addFlashAttribute("misspelling", ftsService.misspellings(keyword));
        }
        return "redirect:/?searchType=power";
    }

}
