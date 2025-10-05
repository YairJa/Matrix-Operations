import java.util.*;

public class MatrixOperations {

	public static double[][] matrixMultiply(double[][] LeftMat, double[][] RightMat){
		if ((LeftMat.length==0) || (RightMat.length==0)){
			return null;
		}
		if(!(LeftMat[0].length==RightMat.length)) {
			return null;
		}
		double [][] Mat= new double[LeftMat.length][RightMat[0].length];
		for (int i=0;i<Mat.length;i++) {
			for(int j=0;j<Mat[0].length;j++) {
				int sum=0;
				for (int k=0;k<RightMat.length;k++) {
					sum+= LeftMat[i][k]*RightMat[k][j];
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
	
	public static void matrixRank(RationalNumber [][] mat ) { ///full matrix rank process, using all helper functions below
		int cnt=0;
		int curR=0; int curC = 0;int i; int j; 
		int[] nonZeroIdx = findFirstNonZeroIdx(mat, curR, curC);
		while(nonZeroIdx[0]!=-1) {
			i =nonZeroIdx [0]; j = nonZeroIdx[1];
			rowInterchange(mat, curR, i);
			rowDivision(mat, curR, mat[curR][j]);
			for(int k=0; k<mat.length;k++) {
				if (k==curR) {
					continue;
				}
				rowSubtraction(mat, k, curR, mat[k][j].divide(mat[curR][j]));
			}
			curR++;curC=j+1;
			nonZeroIdx = findFirstNonZeroIdx(mat, curR, curC);
			cnt++;
		}
		
	}
	
	///search for nonzero value in the minor matrix set by int parameters, search order is by column
	public static int[] findFirstNonZeroIdx(RationalNumber [][] mat, int rowStartIdx, int colStartIdx) { 
			for(int j=colStartIdx;j<mat[0].length;j++) {
				for(int i=rowStartIdx;i<mat.length;i++) {
				
				if(!(mat[i][j].isZero())) {
					return new int[] {i,j};
				}
			}
		}
		return new int[] {-1,-1};
	}
	

	///Elementary Matrix row Operation, subtract martix row by other  row multiplied by scalar
	public static void rowSubtraction(RationalNumber[][] mat, int rowEditIdx, int rowSubIdx ,RationalNumber scalar) { 
		for(int i=0; i<mat[rowEditIdx].length; i++) {
			mat[rowEditIdx][i] = mat[rowEditIdx][i].subtract(mat[rowSubIdx][i].multiply(scalar));
		}
	}
	
	///Elementary Matrix row Operation, divide row by a nonzero scalar
	public static void rowDivision(RationalNumber[][] mat, int rowEditIdx, RationalNumber scalar) {
		if(!scalar.isZero()) {
		for(int i=0; i<mat[rowEditIdx].length; i++) {
			mat[rowEditIdx][i] = mat[rowEditIdx][i].divide(scalar);
		}
		}
	}
	

	///Elementary Matrix row Operation, switching place of 2 rows
	public static void rowInterchange(RationalNumber[][] mat, int RowAIdx , int RowBIdx){ 
		RationalNumber [] temp = mat[RowAIdx];
		mat[RowAIdx]=mat[RowBIdx];
		mat[RowBIdx] = temp;
	}
	

}
