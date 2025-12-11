package com.parkingshare.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOwnerStatusRequest {

    @NotNull(message = "Owner status is required")
    private Boolean isOwner;
}
