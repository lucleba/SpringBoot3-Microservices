package com.lucle.user_management_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "USERNAME_INVALID") // goi den enum ErrorCode USERNAME_INVALID
    String username;

    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;

    String firstName;
    String lastName;

//    @DobConstraint(min = 10, message = "INVALID_DOB")
    LocalDate dob;
}
