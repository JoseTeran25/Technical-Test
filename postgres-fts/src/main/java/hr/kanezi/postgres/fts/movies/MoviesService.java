package hr.kanezi.postgres.fts.movies;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Value
public class MoviesService {

    MoviesViewRepository moviesViewRepository;

    Optional<MoviesView> findMovieInfo(Long movieId) {
        return moviesViewRepository.findById(movieId);
    }

    public List<String> getAllGenres() {
        return moviesViewRepository.getAllGenres();
    }
}
