package com.webfluxplayground.streaming.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId,
                             Long productsCount) {
}