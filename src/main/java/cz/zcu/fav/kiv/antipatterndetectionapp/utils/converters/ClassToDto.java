package cz.zcu.fav.kiv.antipatterndetectionapp.utils.converters;
import java.util.List;

public interface ClassToDto<F,T> {
    T convert(F source);
    List<T> convert(List<F> source);
}
