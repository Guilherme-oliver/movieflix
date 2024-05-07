package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Movie movie = movieRepository.getReferenceById(reviewDTO.getMovieId());
        User user = userRepository.getReferenceById(reviewDTO.getUserId());
        Review review = new Review();
        review.setText(reviewDTO.getText());
        review.setMovie(movie);
        review.setUser(user);
        review = reviewRepository.save(review);
        ReviewDTO dto = new ReviewDTO(review);
        dto.setText(review.getText());
        dto.setUserName(dto.getUserName());
        dto.setUserEmail(dto.getUserEmail());
        dto.setMovieId(dto.getMovieId());
        dto.setUserId(dto.getUserId());
        return dto;
    }
}