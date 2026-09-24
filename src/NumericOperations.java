
public interface NumericOperations<T> {
	
	T add(T x,T y);
	T sub(T x, T y);
	T mul(T x, T y);
	T div(T x,T y);
	T zero();
	boolean isZero(T x);
}
