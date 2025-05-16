package live.akbarov.bsspringboot.config;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice
public class PageableHeaderAdviceConfig implements ResponseBodyAdvice<Page<?>> {

    public static final String PAGE_HEADER = "Page";
    public static final String TOTAL_HEADER = "Total";
    public static final String TOTAL_PAGES_HEADER = "Total-Pages";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Page.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Page<?> beforeBodyWrite(Page<?> page,
                                   MethodParameter methodParameter,
                                   MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                   ServerHttpRequest serverHttpRequest,
                                   ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.getHeaders().add(TOTAL_HEADER, String.valueOf(page.getTotalElements()));
        serverHttpResponse.getHeaders().add(PAGE_HEADER, String.valueOf(page.getNumber()));
        serverHttpResponse.getHeaders().add(TOTAL_PAGES_HEADER, String.valueOf(page.getTotalPages()));
        return new ResponsePage<>(page);
    }

    static class ResponsePage<T> extends PageImpl<T> {

        public ResponsePage(Page<T> page) {
            super(page.getContent(), page.getPageable(), page.getTotalElements());
        }

        @JsonValue
        public List<T> getContent() {
            return super.getContent();
        }
    }
}
