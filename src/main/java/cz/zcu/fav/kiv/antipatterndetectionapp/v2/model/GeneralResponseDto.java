package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeneralResponseDto<T> {
    private String message;
    private List<T> responseBody;

    public static <T> GeneralResponseDtoBuilder<T> builder() {
        return new GeneralResponseDtoBuilder<T>();
    }

}
