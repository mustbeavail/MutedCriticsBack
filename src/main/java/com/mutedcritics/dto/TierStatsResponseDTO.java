package com.mutedcritics.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TierStatsResponseDTO {
    private List<TierStatDTO> tierStats;
    private List<UserClassificationDTO> userList;
}
