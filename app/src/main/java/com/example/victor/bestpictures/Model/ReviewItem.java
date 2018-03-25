package com.example.victor.bestpictures.Model;

/**
 * Created by Victor on 3/12/2018.
 */

public class ReviewItem {

    private String reviewAuthor;
    private String reviewContent;

    public ReviewItem(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewAuthor() {return reviewAuthor;}

    public String getReviewContent() {return reviewContent;}
}
