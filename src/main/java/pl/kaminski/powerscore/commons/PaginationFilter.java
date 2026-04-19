package pl.kaminski.powerscore.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationFilter {

    private final static Integer DEFAULT_PAGE = 0;
    private final static Integer DEFAULT_PAGE_SIZE = 20;

    private Integer page;
    private Integer pageSize;

    public Integer getPage() {
        return Optional.ofNullable(page).orElse(DEFAULT_PAGE);
    }
    public Integer getPageSize() {
        return Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);
    }

    public PageRequest asPageRequest() {
        return PageRequest.of(getPage(), getPageSize());
    }

    public static PaginationFilter defaultFilter() {
        return new PaginationFilter(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
    }
}
