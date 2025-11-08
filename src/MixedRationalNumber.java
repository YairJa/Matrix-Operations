
public class MixedRationalNumber extends AbstractRationalNumber {

	private int numerator;
	private int denominator;
	private int whole;

	
	public MixedRationalNumber(int w, int n, int d) { // assumes d is positive, |n|<d
		this.whole=w;
    	if(n==0) {
        	this.numerator=0;
        	this.denominator=1;
    	}
    	else {
    	int g = RationalNumber.GCD(Math.abs(n),d);
    	this.numerator=n/g;
    	this.denominator=d/g;
    	}
	}
	
	public MixedRationalNumber(int n, int d) {// assumes d is positive
		this.whole=n/d;
		int r =n%d;
    	if(r==0) {
        	this.numerator=0;
        	this.denominator=1;
    	}
    	else {
    	int g = RationalNumber.GCD(Math.abs(r),d);
    	this.numerator=r/g;
    	this.denominator=d/g;
    	}
		
	}
	
	public RationalNumber divide(RationalNumber other) {
		int [] arr = absDivide(this.getNumerator(),this.getDenominator(),other.getNumerator(),other.getDenominator());
		return new MixedRationalNumber(arr[0],arr[1]);
	}
	
	public RationalNumber add(RationalNumber other) {
		int [] arr = absAdd(this.getNumerator(),this.getDenominator(),other.getNumerator(),other.getDenominator());
		return new MixedRationalNumber(arr[0],arr[1]);
	}
	
	public RationalNumber subtract(RationalNumber other) {
		int [] arr = absSubtract(this.getNumerator(),this.getDenominator(),other.getNumerator(),other.getDenominator());
		return new MixedRationalNumber(arr[0],arr[1]);
	}
	
	public RationalNumber multiply(RationalNumber other) {
		int [] arr = absMultiply(this.getNumerator(),this.getDenominator(),other.getNumerator(),other.getDenominator());
		return new MixedRationalNumber(arr[0],arr[1]);
	}
	
	 public boolean isZero() {
	    	return (this.numerator==0)||(this.whole==0);
	    }
	
    public int getNumerator() {
    	return this.numerator+(this.denominator*this.whole);
    }
    
    public int getDenominator() {
    	return this.denominator;
    }
    
    public int getWhole() {
    	return this.whole;
    }
    
    public RationalNumber copy() {
    	return new MixedRationalNumber(this.whole,this.numerator,this.denominator);
    }
    


	@Override
	public String toString() {
		return whole+" and " +numerator + "/" + denominator ;
	}
}
