import java.util.*;
import java.util.function.*;

public class MatrixOperations {

	
	public static <T extends Number> Double[][] matrixMultiply(T[][] LeftMat, T[][] RightMat){
		if((LeftMat == null) || (RightMat == null) || (RightMat.length == 0) || (LeftMat.length == 0)) {
			return null;
		}
		Double[][] LeftMatCopy =matrixConvert(LeftMat);
		Double[][] RightMatCopy =matrixConvert(RightMat);
		return matrixMulCore(LeftMatCopy,RightMatCopy,DoubleOperations.INSTANCE,(rows,cols) -> new Double[rows][cols]);
	}

	
	public static RationalNumber[][] matrixMultiply(RationalNumber [][] LeftMat,RationalNumber [][] RightMat){

		return matrixMulCore(LeftMat,RightMat,RationalNumberOperations.INSTANCE,(rows,cols) -> new RationalNumber[rows][cols]);
		
	}

	private static <T> T[][] matrixMulCore(T[][] LeftMat,T[][] RightMat, NumericOperations<T> ops, BiFunction<Integer,Integer,T[][]> MatrixCreation) {
		if ( (LeftMat == null) || (RightMat == null) || (LeftMat.length==0) || (RightMat.length==0)){
			return null;
		}
		if(!(LeftMat[0].length==RightMat.length)) {
			// not valid pair of matrices for multiply
			return null;
		}
		T[][] result = MatrixCreation.apply(LeftMat.length, RightMat[0].length);
		for (int i=0;i<result.length;i++) {
			for(int j=0;j<result[0].length;j++) {
				T sum= ops.zero();
				for (int k=0;k<RightMat.length;k++) {
					sum=ops.add(sum,ops.mul(LeftMat[i][k],RightMat[k][j]));
				}
				result[i][j]=sum;
			}
		}
		return result;
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
	
	
	private static <T> int[] findFirstNonZeroIdx(T [][] mat, int rowStart, int colStart,Predicate<T> isZero) { 
		
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
	if((mat==null) || (mat.length<2) ||(mat[0].length<2)) {
		return null;
	}
		
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
	
	

	private static <T> int maxZeroRow(T [][] mat, Predicate<T> isZero) { // Det calc helper func to reduce Math operations
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

	private static <T> T matrixDetCore(T[][] mat,NumericOperations<T> ops) {
		// det calculation check for basic condition. if valid calls the recursive calculation func
		if((mat==null) || (mat.length==0) || (mat.length!=mat[0].length)) {
			return null;
		}
		
		if(mat.length==1) {
			return mat[0][0];
		}
		
		return matrixDetCoreRec(mat,ops);
		
	}
	
	private static <T> T matrixDetCoreRec(T[][] mat,NumericOperations<T> ops) {
		// the recursive flow of det calculation. assumes valid matrix
		if(mat.length==2) {
			return ops.sub(ops.mul(mat[0][0],mat[1][1]), ops.mul(mat[0][1], mat[1][0]));
		}		
		int i=maxZeroRow(mat, n -> ops.isZero(n)); 
		T sum=ops.zero();
		for(int j=0;j<mat[0].length;j++) {
			if(ops.isZero(mat[i][j])) {
				continue;
			}
			T element = mat[i][j];
			if((i+j)%2==1) { // equivalent to multiply by -1 for case of odd indices place
				element = ops.sub(ops.zero(), element);
			}
			sum=ops.add(sum, ops.mul(element,matrixDetCoreRec(minorMatrix(mat, i, j),ops)));
			
		}
		
		return sum;
	
	}
	
	public static RationalNumber matrixDet(RationalNumber[][] mat) {
		return matrixDetCore(mat,RationalNumberOperations.INSTANCE);
	}
	
	public static <T extends Number> Double matrixDet(T[][] mat) {
		if((mat==null) || (mat.length==0)) {
			return null;
		}
		Double [][]matCopy=matrixConvert(mat);
		return matrixDetCore(matCopy,DoubleOperations.INSTANCE);

	}

	private static <T extends Number> Double[][] matrixConvert(T[][] mat){
		// in case given matrix is already instanceOf Double, returns the original input (doesnt return new matrix)
		if(mat instanceof Double[][]) {
			return (Double[][])mat;
		}
		return CopyCore(mat,Number :: doubleValue ,(rows,cols) -> new Double[rows][cols]);

	}
	
	public static RationalNumber[][] matrixCopy(RationalNumber[][] mat){
		return CopyCore(mat,RationalNumber :: copy ,(rows,cols) -> new RationalNumber[rows][cols]);
	}
	
	private static <T,R> R[][] CopyCore(T[][] mat, Function<T,R> valueAssert,BiFunction<Integer,Integer,R[][]> MatrixCreation) {
		R[][] ret = MatrixCreation.apply(mat.length, mat[0].length);
		
		for(int i=0;i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				ret[i][j]=valueAssert.apply(mat[i][j]);
			}
		}
		return ret;
	}
	

}
