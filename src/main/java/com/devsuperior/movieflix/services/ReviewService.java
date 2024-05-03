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
    public ReviewDTO insert(ReviewDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Review review = new Review(null, dto.getText(), movie, user);
        review = reviewRepository.save(review);

        return new ReviewDTO(review.getId(), review.getText(), review.getMovie().getId(), review.getUser().getId(), review.getUser().getUsername(), review.getUser().getPassword());
    }
}