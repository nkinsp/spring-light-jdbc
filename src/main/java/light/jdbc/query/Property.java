package light.jdbc.query;

@FunctionalInterface
public interface Property<T,R> {

    R apply(T t);
}
