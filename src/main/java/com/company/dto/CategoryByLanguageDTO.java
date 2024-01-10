package com.company.dto;

import java.time.LocalDateTime;

public record CategoryByLanguageDTO(int id,
                                    String name,
                                    boolean visible,
                                    String key,
                                    String slag) {
}
