package pl.kaminski.powerscore.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
    private final List<T> rows;
    private final Long totalElements;
    private final Integer totalPages;
    private final Integer page;
    private final Integer size;

    public <N> PaginatedResponse<N> mapRows(Function<T, N> mapper) {
        return new PaginatedResponse<>(
                rows.stream().map(mapper).collect(Collectors.toList()),
                totalElements,
                totalPages,
                page,
                size
        );
    }
}
