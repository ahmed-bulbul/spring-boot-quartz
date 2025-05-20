package com.bulbul.spring.quartz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number; // current page
    private int size;   // page size
    private boolean last;
}
