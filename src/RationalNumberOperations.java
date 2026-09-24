
public class RationalNumberOperations implements NumericOperations<RationalNumber> {
	
	public static final RationalNumberOperations INSTANCE = new RationalNumberOperations(new FractionRationalNumberFactory());
	
	private final RationalNumberFactory factory;
	
	private RationalNumberOperations(RationalNumberFactory factory) {
		this.factory=factory;
	}
	
	public RationalNumber add(RationalNumber x,RationalNumber y) {
		return x.add(y);
	}
	
	
	public RationalNumber sub(RationalNumber x,RationalNumber y) {
		return x.subtract(y);
	}
	
	public RationalNumber mul(RationalNumber x,RationalNumber y) {
		return x.multiply(y);
	}
	
	public RationalNumber div(RationalNumber x,RationalNumber y) {
		return x.divide(y);
	}

	public RationalNumber zero() {
		return factory.zero();
	}
	public boolean isZero(RationalNumber x) {
		return x.isZero();
	}
}
