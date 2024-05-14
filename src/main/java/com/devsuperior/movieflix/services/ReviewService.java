package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.projections.ReviewProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private MovieRepository movieRepository;
    private AuthService authService;

    public ReviewService(ReviewRepository reviewRepository, MovieRepository movieRepository, AuthService authService) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> findByMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new ResourceNotFoundException("Id not found " + movieId);
        }
        List<ReviewProjection> list = reviewRepository.searchByMovie(movieId);
        return list.stream().map(ReviewDTO::new).toList();
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        User user = authService.authenticated();
        try {
            Review entity = new Review();
            entity.setMovie(movieRepository.getReferenceById(reviewDTO.getMovieId()));
            entity.setUser(user);
            entity.setText(reviewDTO.getText());
            entity = reviewRepository.save(entity);
            return new ReviewDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + reviewDTO.getMovieId());
        }
    }
}