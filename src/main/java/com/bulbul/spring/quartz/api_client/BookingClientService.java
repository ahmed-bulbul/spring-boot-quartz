package com.bulbul.spring.quartz.api_client;

import com.bulbul.spring.quartz.dto.response.PageResponse;
import com.bulbul.spring.quartz.job.booking.BookingCheckinProjection;
import com.bulbul.spring.quartz.job.booking.BookingCheckinProjectionImpl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BookingClientService {

    private final WebClient webClient;

    public BookingClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PageResponse<BookingCheckinProjectionImpl>> getBookingsBeforeCheckin(int days, int pageNumber) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tasks/bookings/before-checkin/{days}/{pageNumber}")
                        .build(days, pageNumber))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

    }
}
