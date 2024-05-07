package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(nativeQuery = true, value = """
    SELECT * FROM (
    SELECT DISTINCT tb_movie.id, tb_movie.genre_id
    FROM tb_movie
    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
    ) AS tb_result
    """, countQuery = """
    SELECT COUNT(*) FROM (
    SELECT DISTINCT tb_movie.id
    FROM tb_movie
    WHERE (:genreId IS NULL OR tb_movie.genre_id = :genreId)
    ) AS tb_result
    """)
    Page<MovieProjection> searchByGenre(Long genreId, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre WHERE obj.genre.id = :genreId")
    List<Movie> searchMovieWithGenre(List<Long> genreId);
}