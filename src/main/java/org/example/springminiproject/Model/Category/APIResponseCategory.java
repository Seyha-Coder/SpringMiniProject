package org.example.springminiproject.Model.Category;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponseCategory<T> {
    private String message;
    private T payload;
    private HttpStatus status;
    private LocalDateTime time;
}