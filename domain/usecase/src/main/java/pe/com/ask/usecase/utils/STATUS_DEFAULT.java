package pe.com.ask.usecase.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum STATUS_DEFAULT {
    PENDING_REVIEW("Pendiente de revisión");

    private final String name;
}