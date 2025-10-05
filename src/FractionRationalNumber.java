import java.math.*;

public class FractionRationalNumber implements RationalNumber {

	private int numerator;
    private int denominator; // positive number; 

    public FractionRationalNumber(int n, int d) { // assumes d is positive
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
    

    
	public RationalNumber divide(RationalNumber other){ 
		if(other.isZero()) {
			return new FractionRationalNumber(0,1);
		}
		int newN= this.numerator* other.getDenominator();
		int newD= this.denominator*other.getNumerator();
		if(newD<0) {
			newD*=-1;
			newN*=-1;
		}
		return new FractionRationalNumber(newN,newD);
	}
	
	public RationalNumber add(RationalNumber other) {
		int newD=this.denominator*other.getDenominator();
		int n1= this.numerator*other.getDenominator();
		int n2= this.denominator*other.getNumerator();
		int newN=n1+n2;
		return new FractionRationalNumber(newN,newD);

	}
	
	public RationalNumber subtract(RationalNumber other) {
		int newD=this.denominator*other.getDenominator();
		int n1= this.numerator*other.getDenominator();
		int n2= this.denominator*other.getNumerator();
		int newN=n1-n2;
		return new FractionRationalNumber(newN,newD);	
	}
	
	public RationalNumber multiply(RationalNumber other) {
		int newN= this.numerator*other.getNumerator();
		int newD= this.denominator*other.getDenominator();
		return new FractionRationalNumber(newN,newD);
	}
	
    public boolean isZero() {
    	return this.numerator==0;
    }
    
    public int getNumerator() {
    	return this.numerator;
    }
    
    public int getDenominator() {
    	return this.denominator;
    }
    
    public int getWhole() {
    	return this.numerator / this.denominator;
    }

	@Override
	public String toString() {
		return  numerator + "/" + denominator ;
	}
	
}
