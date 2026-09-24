
public abstract class AbstractRationalNumber implements RationalNumber{

	public int [] absDivide(int thisN, int thisD, int otherN, int otherD) {
		if(otherN==0) {
			//invalid Division, Option to raise an error
			return new int [] {0,1};
		}
		int reciprocalD=otherN;
		int reciprocalN=otherD;
		if(reciprocalD<0) {
			// moving negative sign from mulD to mulN 
			reciprocalN*=-1;
			reciprocalD*=-1;
		}
		
		return absMultiply(thisN, thisD, reciprocalN, reciprocalD);
		
	}
	
	public int [] absAdd(int thisN, int thisD, int otherN, int otherD) {
		int g = RationalNumber.GCD(thisD, otherD);
		
		int newD=thisD*(otherD/g);
		int n1= thisN*(otherD/g);
		int n2= (thisD/g)*otherN;
		int newN=n1+n2;
		return new int [] {newN,newD};

	}
	

	public int [] absMultiply(int thisN, int thisD, int otherN, int otherD) {
		int g1 = RationalNumber.GCD(Math.abs(thisN), otherD);
		int g2 = RationalNumber.GCD(Math.abs(otherN), thisD);

		int newN= (thisN/g1)*(otherN/g2);
		int newD= (thisD/g2)*(otherD/g1);
		return new int [] {newN,newD};
	}
	
	public int [] absSubtract(int thisN, int thisD, int otherN, int otherD) {
		return absAdd(thisN, thisD, -otherN, otherD);
	}
	
	
}
