
public interface RationalNumber {
	
    public static int GCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
	
	RationalNumber divide(RationalNumber other);
	RationalNumber add(RationalNumber other);
	RationalNumber subtract(RationalNumber other);
	RationalNumber multiply(RationalNumber other);
	boolean isZero();
    int getNumerator();
    int getDenominator();
    int getWhole();
    
   

}
