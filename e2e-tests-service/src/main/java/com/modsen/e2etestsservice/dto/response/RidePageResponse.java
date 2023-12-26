package com.modsen.e2etestsservice.dto.response;

import java.util.List;

public record RidePageResponse(
        List<RideResponse> rides,
        Integer pageNumber,
        Long total
) {
}
