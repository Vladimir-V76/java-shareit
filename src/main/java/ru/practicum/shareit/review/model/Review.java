package ru.practicum.shareit.review.model;

import lombok.Data;

@Data
public class Review {
    Long id;
    Long itemId;
    Long userId;
    boolean isThank;
    String textReview;
}
