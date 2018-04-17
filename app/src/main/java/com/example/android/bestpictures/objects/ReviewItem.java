package com.example.android.bestpictures.objects;

/******
 * Created by Victor on 3/12/2018.
 ******/

public class ReviewItem {

    private final String reviewAuthor;
    private final String reviewContent;

    public ReviewItem(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }

    public String getReviewAuthor() {return reviewAuthor;}

    public String getReviewContent() {return reviewContent;}
}
