package hr.kanezi.postgres.fts.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MoviesViewRepository extends JpaRepository<MoviesView, Long> {
    @Query(value = " select distinct  vm.genres from  v_movies vm ", nativeQuery = true)

    List<String> getAllGenres();
}
