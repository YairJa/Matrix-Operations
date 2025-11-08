
public abstract class AbstractRationalNumber implements RationalNumber{

	public int [] absDivide(int thisN, int thisD, int otherN, int otherD) {
		if(otherN==0) {
			return new int [] {0,1};
		}
		int newN= thisN* otherD;
		int newD= thisD*otherN;
		if(newD<0) {
			newD*=-1;
			newN*=-1;
		}
		return new int [] {newN,newD};
	}
	
	public int [] absAdd(int thisN, int thisD, int otherN, int otherD) {
		int newD=thisD*otherD;
		int n1= thisN*otherD;
		int n2= thisD*otherN;
		int newN=n1+n2;
		return new int [] {newN,newD};

	}
	

	public int [] absMultiply(int thisN, int thisD, int otherN, int otherD) {
		int newN= thisN*otherN;
		int newD= thisD*otherD;
		return new int [] {newN,newD};
	}
	
	public int [] absSubtract(int thisN, int thisD, int otherN, int otherD) {
		int newD=thisD*otherD;
		int n1= thisN*otherD;
		int n2= thisD*otherN;
		int newN=n1-n2;
		return new int [] {newN,newD};
	}
	
	
}
