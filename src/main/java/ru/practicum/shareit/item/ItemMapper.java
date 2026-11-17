package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class ItemMapper {

    public static Item mapToItem(NewItemRequest newItem, User user) {
        Item item = new Item();
        item.setOwner(user);
        item.setName(newItem.getName());
        item.setDescription(newItem.getDescription());
        item.setAvailable(newItem.getAvailable());

        return item;
    }

    public static ItemDto mapToItemDto(Item item, List<CommentDto> comments, ItemBookingDatesDto itemBookingDatesDto) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setComments(comments);
        itemDto.setLastBooking(itemBookingDatesDto.getLastBooking());
        itemDto.setNextBooking(itemBookingDatesDto.getNextBooking());

        return itemDto;
    }

    public static void updateItemFields(Item item, UpdateItemRequest updateItem) {
        if (updateItem.hasName()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.hasDescription()) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.hasAvailable()) {
            item.setAvailable(updateItem.getAvailable());
        }
    }

    public static List<CommentDto> mapToListCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getId(),
                        c.getAuthor().getName(),
                        c.getCreated().toString()))
                .toList();
    }

    public static ItemWithoutCommentDto mapToItemWithoutCommentDto(Item item) {
        return new ItemWithoutCommentDto(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated().toString());
    }

    public static Comment mapToComment(Item item, User author, NewCommentRequest newComment) {
        Comment comment = new Comment();
        comment.setText(newComment.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(newComment.getCreated());

        return comment;
    }
}
