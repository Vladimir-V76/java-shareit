package ru.practicum.shareit.review.dto;

import lombok.Data;

@Data
public class ReviewDto {
    Long itemId;
    Long userId;
    boolean isThank;
    String textReview;
}
