
public class FractionRationalNumberFactory implements RationalNumberFactory {

	public RationalNumber zero() {
    	return new FractionRationalNumber(0,1);
	}
	
}
