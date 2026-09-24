
public class DoubleOperations implements NumericOperations<Double> {
	
	public static final DoubleOperations INSTANCE = new DoubleOperations();
	
	
	private DoubleOperations() {
	}
	
	public Double add(Double x,Double y) {
		return x+y;
	}
	
	
	public Double sub(Double x,Double y) {
		return x-y;
	}
	
	public Double mul(Double x,Double y) {
		return x*y;
	}
	
	public Double div(Double x,Double y) {
		return x/y;
	}

	public Double zero() {
		return 0.0;
	}
	public boolean isZero(Double x) {
		return x==0.0;
	}
}
