package com.nttdata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    @Builder.Default
    private Integer code = 200;
    private String message;
    private List<String> errors;
    private T data;
}
