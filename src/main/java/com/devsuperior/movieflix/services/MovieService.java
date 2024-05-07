package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieDetailsDTO findById(Long id) {
        Optional<Movie> obj = movieRepository.findById(id);
        Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found!"));
        return new MovieDetailsDTO(entity);
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> searchByGenres(Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        List<MovieCardDTO> dtos = moviesPage.getContent().stream()
                .sorted((m1, m2) -> m1.getGenre().getId().compareTo(m2.getGenre().getId()))
                .map(MovieCardDTO::new)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, moviesPage.getTotalElements());
    }

    /*
    @Transactional(readOnly = true)
    public Page<MovieCardDTO> searchByGenres(Long genreId, Pageable pageable) {
        Page<MovieProjection> movieProjections = movieRepository.searchByGenre(genreId, pageable);
        List<Long> movieIds = movieProjections.map(MovieProjection::getId).toList();

        List<Movie> movies = movieRepository.searchMovieWithGenre(movieIds);
        List<MovieCardDTO> dtos = movies.stream().map(MovieCardDTO::new).toList();

        return new PageImpl<>(dtos, movieProjections.getPageable(), movieProjections.getTotalElements());
    }
     */
}