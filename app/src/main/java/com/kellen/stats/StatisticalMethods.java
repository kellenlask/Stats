package com.kellen.stats;

import java.lang.Math;
import java.util.Arrays;

/**
 * Created by Kellen on 6/23/2015.
 */

/*
	The purpose of this class is to separate the mathematical logic from the rest of the application
	logic by serving as a statistics utility class.
 */

public class StatisticalMethods {
	public static final double SIG_FIGS = .00001;

//-------------------------------------------
//
//		Single Variable Analysis
//
//-------------------------------------------
	public static double[] getBasicAnalysis(double[] values) {
		double[] returnArray = new double[7];

		//mean
		returnArray[0] = calcMean(values);

		//median
		returnArray[1] = calcMedian(values);

		//range
		returnArray[2] = calcRange(values);

		//min
		returnArray[3] = findMin(values);

		//max
		returnArray[4] = findMax(values);

		//variance
		returnArray[5] = calcVariance(values);

		//standard deviation
		returnArray[6] = calcStdDev(values);

		return returnArray;

	} //End public static double[] getBasicAnalysis(double[])

	public static double calcMean(double[] values) {
		double total = 0;

		for(double n : values) {
			total += n;
		}

		return total / values.length;

	} //End public static double calcMean(double[])

	public static double calcVariance(double[] values) {
		double mean = calcMean(values);
		double variance = 0;

		for(double n : values) {
			variance += Math.pow(mean - n, 2);

		}

		return variance / values.length;

	} //End public static double calcVariance(double[])

	public static double calcStdDev(double[] values) {
		return Math.sqrt(calcVariance(values));

	} //End public static double calcStdDev(double[])

	public static double calcMedian(double[] values) {
		double[] sorted = values.clone();
		Arrays.sort(sorted);

		if(sorted.length % 2 == 0) {
			return (sorted[sorted.length / 2] + sorted[sorted.length / 2 - 1]) / 2;

		} else {
			return sorted[sorted.length / 2];
		}

	} //End public static double calcMedian(double[])

	public static double calcRange(double[] values) {
		if (values.length > 0) {
			double[] sorted = values.clone();
			Arrays.sort(sorted);

			return sorted[sorted.length - 1] - sorted[0];
		}

		return 0;

	} //End public static double calcRange(double[])

	public static double findMin(double[] values) {
		double[] sorted = values.clone();
		Arrays.sort(sorted);

		return sorted[0];

	} //End public static double findMin(double[])

	public static double findMax(double[] values) {
		double[] sorted = values.clone();
		Arrays.sort(sorted);

		return sorted[sorted.length - 1];

	} //End public static double findMax(double[])

//-------------------------------------------
//
//		Calculators
//
//-------------------------------------------
	//		The Array: {pOfA, pOfB, pOfBGivenA, pOfAGivenB}
	public static double[] calcBayes(double[] values, int missingValueIndex) {
		switch(missingValueIndex) {
			case 0:
				values[0] = (values[3] * values[1]) / values[2];
				break;

			case 1:
				values[1] = (values[0] * values[2]) / values[3];
				break;

			case 2:
				values[2] = (values[1] * values[3]) / values[0];
				break;

			case 3:
				values[3] = (values[0] * values[2]) / values[1];
				break;

		} //End Switch

		return values;

	} //End public static double calcBayes(double, double, double)

	public static double calcPoisson(double llambda, double value) {
		return (Math.pow(llambda, value) * Math.pow(Math.E, -1 * llambda)) / calcFactorial(value);

	} //End public static double calcPoisson(double, double)

	public static double calcPopulationZScore(double x, double mu, double sigma) {
		return (x - mu) / sigma;

	} //End public static double calcPopulationZScore(double, double, double)

	public static double calcSampleZScore(double x, double mu, double sigma, double n) {
		return (x - mu) / (sigma / Math.sqrt(n));

	} //End public static double calcSampleZScore(double, double, double, double)

	public static double calcTScore(double x, double s, double mu, double n) {
		double numerator = x - mu;
		double denominator = s / Math.sqrt(n);

		return numerator / denominator;

	} //End public static double calcTScore(double, double, double, double)



//-------------------------------------------
//
//		Regression
//
//-------------------------------------------
	//Standard Linear Least Squares Regression
	public static double[] linearRegression(double[] xVals, double[] yVals) {
		//Grab some values
		double xSum = 0;
		double ySum = 0;
		double xySum = 0;
		double x2Sum = 0; //x squared sum
		double y2Sum = 0; //y squared sum

		//X and Y values will have the same count
		int n = xVals.length;

		for(int i = 0; i < n; i++) {
			xSum += xVals[i];
			ySum += yVals[i];

			xySum += xVals[i] * yVals[i];
			x2Sum += xVals[i] * xVals[i];
			y2Sum += yVals[i] * yVals[i];

		} //End for

		double slope = (n * xySum - xSum * ySum) / (n * x2Sum - xSum * xSum);
		double yInt = (x2Sum * ySum - xSum * xySum) / (n * x2Sum - xSum * xSum);
		double r = (n * xySum - xSum * ySum) / Math.sqrt((n * x2Sum - xSum * xSum) * (n * y2Sum - ySum * ySum));

		return new double[] {slope, yInt, r, r*r};

	} //End public static double[] linearRegression(double[], double[])

	//Logarithmic Regression: y = ln(ax + b)
	public static double[] logReg(double[] xVals, double[] yVals) {
		double[] newYVals = new double[yVals.length];

		//Cancel out the logarithmic form down into a linear form
		for(int i = 0; i < yVals.length; i++) {
			newYVals[i] = Math.exp(yVals[i]);
		} //End for

		return linearRegression(xVals, newYVals);

	} //End public static double[] logReg(double[], double[])

	//Exponential Regression: y = e^(ax+b)
	public static double[] expReg(double[] xVals, double[] yVals) {
		double[] newYVals = new double[yVals.length];

		//Cancel out the exponential form down into a linear form
		for(int i = 0; i < yVals.length; i++) {
			newYVals[i] = Math.log(yVals[i]);
		} //End for

		return linearRegression(xVals, newYVals);

	} //End public static double[] expReg(double[], double[])

	//Power Regression: y = ax^b
	public static double[] powerReg(double[] xVals, double[] yVals) {
		double[] newYVals = new double[yVals.length];
		double[] newXVals = new double[xVals.length];

		//Cancel out the exponential form down into a linear form
		for(int i = 0; i < yVals.length; i++) {
			newYVals[i] = Math.log(yVals[i]);
			newXVals[i] = Math.log(xVals[i]);
		} //End for

		//Adjust the constant value
		double[] returnArray = linearRegression(newXVals, newYVals);
		returnArray[1] = Math.exp(returnArray[1]);

		return returnArray;

	} //End public static double[] powerReg(double[], double[])


//-------------------------------------------
//
//		Misc
//
//-------------------------------------------
	public static double calcFactorial(double n) {
		if(n == 1) {
			return 1;
		}

		return calcFactorial(n - 1) * n;

	} //End public static double calcFactorial(double)

	public static double lambertWFunction(double w) {
		//Find y for y * e^(y) = w
		double y = 0;
		double increment = 1.0;

		while(increment >= SIG_FIGS) {
			if(lambert(y - increment) <= w && lambert(y + increment) >= w) {
				increment /= 10;
				y = y - increment;

			} else if(lambert(y) < w) {
				y = y + increment;

			} else {
				y = y - increment;
			}

		}

		return y;

	} //End public static double lambertWFunction(double)

	public static double lambert(double y) {
		return y * Math.exp(y);
	}

} //End Class
