package com.lucle.user_management_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // chi parse nhung field not null ra json tra ve
public class ApiResponse<T> {
    int code = 1000; // mac dinh gia tri 1000 la thanh cong
    String message;
    T result; // kieu tra ve co the thay doi tuy thuoc vao tung api
}
