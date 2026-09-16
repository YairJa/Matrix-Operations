import java.util.*;
import java.util.function.*;

public class MatrixOperations {

	
	public static <T extends Number> double[][] matrixMultiply(T[][] LeftMat, T[][] RightMat){
		if ((LeftMat.length==0) || (RightMat.length==0)){
			return null;
		}
		if(!(LeftMat[0].length==RightMat.length)) {
			return null;
		}
		double [][] Mat= new double[LeftMat.length][RightMat[0].length];
		for (int i=0;i<Mat.length;i++) {
			for(int j=0;j<Mat[0].length;j++) {
				double sum=0;
				for (int k=0;k<RightMat.length;k++) {
					sum+= (LeftMat[i][k].doubleValue())*(RightMat[k][j].doubleValue());
					
				}
				Mat[i][j]=sum;
			}
		}
		
		
		return Mat;
	}
	
	public static RationalNumber[][] matrixMultiply(RationalNumber [][] LeftMat,RationalNumber [][] RightMat){
		if ((LeftMat.length==0) || (RightMat.length==0)){
			return null;
		}
		if(!(LeftMat[0].length==RightMat.length)) {
			return null;
		}
		RationalNumber [][] Mat= new RationalNumber[LeftMat.length][RightMat[0].length];
		for (int i=0;i<Mat.length;i++) {
			for(int j=0;j<Mat[0].length;j++) {
				RationalNumber sum= LeftMat[i][0].multiply(RightMat[0][j]);
				for (int k=1;k<RightMat.length;k++) {
					sum=sum.add( LeftMat[i][k].multiply(RightMat[k][j]));
				}
				Mat[i][j]=sum;
			}
		}
		
		
		return Mat;
	}
	
	public static void matrixRankInPlace(RationalNumber [][] mat ) { ///full matrix rank process, using all helper functions below
		int curR=0; int curC = 0;int i; int j; 
		int[] nonZeroIdx = findFirstNonZeroIdx(mat, curR, curC,num -> num.isZero());
		while(nonZeroIdx[0]!=-1) {
			i =nonZeroIdx [0]; j = nonZeroIdx[1];
			rowInterchangeInPlace(mat, curR, i);
			rowDivisionInPlace(mat, curR, mat[curR][j]);
			for(int k=0; k<mat.length;k++) {
				if (k==curR) {
					continue;
				}
				rowSubtractionInPlace(mat, k, curR, mat[k][j].divide(mat[curR][j]));
			}
			curR++;curC=j+1;
			nonZeroIdx = findFirstNonZeroIdx(mat, curR, curC,num -> num.isZero());
		}
		
	}
	
	public static RationalNumber[][] matrixRank(RationalNumber [][] mat ) { ///full matrix rank process, using all helper functions below
		RationalNumber[][] copy = matrixCopy(mat);
		matrixRankInPlace(copy);
		return copy;
		
	}
	
	
	public static <T> int[] findFirstNonZeroIdx(T [][] mat, int rowStart, int colStart,Predicate<T> isZero) { 
		
		for(int j=colStart;j<mat[0].length;j++) {
			for(int i=rowStart;i<mat.length;i++) {
			
			if(!(isZero.test( mat[i][j]))) {
				return new int[] {i,j};
			}
		}
	}
	return new int[] {-1,-1};
}
	

	///Elementary Matrix row Operation, subtract martix row by other  row multiplied by scalar
	public static void rowSubtractionInPlace(RationalNumber[][] mat, int rowEditIdx, int rowSubIdx ,RationalNumber scalar) { 
		for(int i=0; i<mat[rowEditIdx].length; i++) {
			mat[rowEditIdx][i] = mat[rowEditIdx][i].subtract(mat[rowSubIdx][i].multiply(scalar));
		}
	}
	
	public static RationalNumber[][] rowSubtraction(RationalNumber[][] mat, int rowEditIdx, int rowSubIdx ,RationalNumber scalar) { 
		RationalNumber[][] copy = matrixCopy(mat);
		rowSubtractionInPlace(copy,rowEditIdx,rowSubIdx, scalar );
		return copy;
	}
	
	///Elementary Matrix row Operation, divide row by a nonzero scalar
	public static void rowDivisionInPlace(RationalNumber[][] mat, int rowEditIdx, RationalNumber scalar) {
		if(!scalar.isZero()) {
		for(int i=0; i<mat[rowEditIdx].length; i++) {
			mat[rowEditIdx][i] = mat[rowEditIdx][i].divide(scalar);
		}
		}
	}
	
	public static RationalNumber[][] rowDivision(RationalNumber[][] mat, int rowEditIdx, RationalNumber scalar) {
		RationalNumber[][] copy = matrixCopy(mat);
		rowDivisionInPlace(copy,rowEditIdx,scalar );
		return copy;
	}
	

	///Elementary Matrix row Operation, switching place of 2 rows
	public static <T> void rowInterchangeInPlace(T[][] mat, int RowAIdx , int RowBIdx){ 
		T [] temp = mat[RowAIdx];
		mat[RowAIdx]=mat[RowBIdx];
		mat[RowBIdx] = temp;
	}
	

	
	public static RationalNumber[][] rowInterchange(RationalNumber[][] mat, int RowAIdx , int RowBIdx){
		RationalNumber[][] copy = matrixCopy(mat);
		rowInterchangeInPlace(copy,RowAIdx,RowBIdx );
		return copy;
	}
	
	
	public static <T> T[][] minorMatrix(T[][] mat, int rowIdx , int colIdx){ // generic func returns matrix without specified row and col
	T[][] minor = Arrays.copyOf(mat, mat.length - 1);
	for (int row = 0; row < minor.length; row++) {
		minor[row] = Arrays.copyOf(mat[row], mat[0].length - 1);

	}
	int rowPass=0; int colPass=0;
	for(int i =0;i<minor.length;i++) {
		
		if(i==rowIdx) {
			rowPass=1;
		}
		
		for (int j=0;j<minor[0].length;j++) {
		
			if(j==colIdx) {
				colPass=1;
			}
			minor[i][j]=mat[i+rowPass][j+colPass];
			
		}
		colPass=0;
		
	}
	return minor;	
	
	}
	
	

	public static <T> int maxZeroRow(T [][] mat, Predicate<T> isZero) { // Det calc helper func to reduce Math operations
		int idx=0;int maxCnt=0;
		for(int i=0;i<mat.length;i++) {
			int cnt=0;
		for(int j=0;j<mat[0].length;j++) {
			if(isZero.test(mat[i][j])) {
				cnt++;
			}
		}
		if(cnt>maxCnt) {
			idx=i;
			maxCnt=cnt;
		}
		}
		return idx;
	}
	
	public static RationalNumber matrixDet(RationalNumber[][] mat) {
	if(mat.length==2) {
		RationalNumber x = mat[0][0].multiply(mat[1][1]);  
		return x.subtract(mat[1][0].multiply(mat[0][1]));
	}
	RationalNumberFactory factory = new FractionRationalNumberFactory();
	int i=maxZeroRow(mat, num -> num.isZero()); 
	RationalNumber sum = factory.zero();
	for(int j=0;j<mat[0].length;j++) {
		if(mat[i][j].isZero()) {
			continue;
		}
		RationalNumber ret = mat[i][j];
		if((i+j)%2==1) {
			ret = factory.zero().subtract(ret);
		}
		sum = sum.add(ret.multiply(matrixDet(minorMatrix(mat, i, j))));
	}
	
	return sum;
	}
	
	public static <T extends Number> double matrixDet(T[][] mat) {
	if(mat.length==2) {
		return (mat[0][0].doubleValue()*mat[1][1].doubleValue())- (mat[1][0].doubleValue()*mat[0][1].doubleValue());
	}
		
	int i=maxZeroRow(mat, n -> n.doubleValue()==0); double sum = 0;
	for(int j=0;j<mat[0].length;j++) {
		if(mat[i][j].doubleValue()==0) {
			continue;
		}
		int sign =  (int) Math.pow(-1, i+j);
		sum+=mat[i][j].doubleValue()*sign*matrixDet(minorMatrix(mat, i, j));
	}
	
	return sum;
	}
	
	public static RationalNumber[][] matrixCopy(RationalNumber[][] mat){
		RationalNumber[][] newMat = new RationalNumber[mat.length][mat[0].length];
		
		for(int i=0;i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				newMat[i][j]=mat[i][j].copy();
			}
		}
		return newMat;
	}
	

}
