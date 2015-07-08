package com.kellen.stats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Summary extends Fragment {
//-------------------------------------------
//
//		Fields
//
//-------------------------------------------
	//GUI elements
	View rootView;
	TextView summary;

	//Logical Elements
	ArrayList<Double> xValues;
	ArrayList<Double> yValues;

//-------------------------------------------
//
//		System
//
//-------------------------------------------
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static Summary newInstance(int sectionNumber) {
		Summary fragment = new Summary();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_summary, container, false);
		return rootView;
	}

//-------------------------------------------
//
//		Constructor
//
//-------------------------------------------
	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		initializeFields();

	}

	public Summary() {

	}

//-------------------------------------------
//
//		Regression Analysis
//
//-------------------------------------------
	public void performRegressionAnalysis() {
		//Convert to arrays
		double[] xVals = new double[xValues.size()];
		double[] yVals = new double[yValues.size()];

		for(int i = 0; i < xValues.size(); i ++) {
			xVals[i] = xValues.get(i);
			yVals[i] = yValues.get(i);
		}

		//In order: mean, median, range, min, max, variance, stdDev
		double[] xBasic = StatisticalMethods.getBasicAnalysis(xVals);
		double[] yBasic = StatisticalMethods.getBasicAnalysis(yVals);

		//Perform Regression Analysis
		double[] linear = StatisticalMethods.linearRegression(xVals, yVals);
		double[] log = StatisticalMethods.logReg(xVals, yVals);
		double[] exp = StatisticalMethods.expReg(xVals, yVals);
		double[] power = StatisticalMethods.powerReg(xVals, yVals);

		//Round the values to avoid crazy trailing decimals
		for(int i = 0; i < linear.length; i++) {
			linear[i] = Math.round(linear[i] * 10000) / 10000;
			log[i] = Math.round(log[i] * 10000) / 10000;
			exp[i] = Math.round(exp[i] * 10000) / 10000;
			power[i] = Math.round(power[i] * 10000) / 10000;

		}

		for(int i = 0; i < xBasic.length; i++) {
			xBasic[i] = Math.round(xBasic[i] * 10000) / 10000;
			yBasic[i] = Math.round(yBasic[i] * 10000) / 10000;

		}


		//Construct a string for the summary
		String basic = makeBasicString(xBasic, yBasic);
		String regression = makeRegressionString(linear, log, exp, power);

		summary.setText(basic + regression);

	} //End public void performRegressionAnalysis()

	public String makeBasicString(double[] xBasic, double[] yBasic) {
		String returnString = "Basic Analysis:\n=======================\nX Values\n";

		//X Values
		returnString += "\tMean: " + xBasic[0] + "\n";
		returnString += "\tMedian: " + xBasic[1] + "\n";
		returnString += "\tRange: " + xBasic[2] + "\n";
		returnString += "\tMin: " + xBasic[3] + "\n";
		returnString += "\tMax: " + xBasic[4] + "\n";
		returnString += "\tVariance: " + xBasic[5] + "\n";
		returnString += "\tStd Dev: " + xBasic[6] + "\n";

		returnString += "\n\nY Values\n";

		//Y Values
		returnString += "\tMean: " + yBasic[0] + "\n";
		returnString += "\tMedian: " + yBasic[1] + "\n";
		returnString += "\tRange: " + yBasic[2] + "\n";
		returnString += "\tMin: " + yBasic[3] + "\n";
		returnString += "\tMax: " + yBasic[4] + "\n";
		returnString += "\tVariance: " + yBasic[5] + "\n";
		returnString += "\tStd Dev: " + yBasic[6] + "\n\n";

		return returnString;

	} //End public String makeBasicString(double[], double[])

	public String makeRegressionString(double[] linear, double[] log, double[] exp, double[] power) {
		String returnString = "Regression Analysis:\n=======================\n";

		//Linear
		returnString += "Linear Regression:\n\ty=" + linear[0] + "x + (" + linear[1] + ")\n\t";
		returnString += "r: " + linear[2] + "\n\tr²: " + linear[3] + "\n\n";

		//Log
		returnString += "Logarithmic Regression:\n\ty=ln(" + log[0] + "x + (" + log[1] + "))\n\t";
		returnString += "r: " + log[2] + "\n\tr²: " + log[3] + "\n\n";

		//Exp
		returnString += "Exponential Regression:\n\ty=e ^ (" + exp[0] + "x + (" + exp[1] + "))\n\t";
		returnString += "r: " + exp[2] + "\n\tr²: " + exp[3] + "\n\n";

		//Power
		returnString += "Power Regression:\n\ty=" + power[0] + "x ^ (" + power[1] + ")\n\t";
		returnString += "r: " + power[2] + "\n\tr²: " + power[3] + "\n\n";

		return returnString;

	} //End public String makeRegressionString(double[], double[], double[], double[])


//-------------------------------------------
//
//		Utility Methods
//
//-------------------------------------------
	public void initializeFields() {
		pullFromSharedPrefs();

		summary = (TextView) rootView.findViewById(R.id.analysis);

		performRegressionAnalysis();

	} //End public void initializeFields()

	public void pullFromSharedPrefs() {
		SharedPreferences sp = rootView.getContext().getSharedPreferences("prefs", rootView.getContext().MODE_PRIVATE);
		String xVals = sp.getString("XVALS", "");
		String yVals = sp.getString("YVALS", "");

		if(!(xVals.equals("") || yVals.equals(""))) {
			try {
				//Pull Xs out
				byte[] bytes = xVals.getBytes("ISO-8859-1");
				ObjectInputStream io = new ObjectInputStream( new ByteArrayInputStream(bytes) );
				xValues = (ArrayList<Double>) io.readObject();


				//Pull Ys out
				bytes = yVals.getBytes("ISO-8859-1");
				io = new ObjectInputStream( new ByteArrayInputStream(bytes) );
				yValues = (ArrayList<Double>) io.readObject();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			xValues = new ArrayList<>();
			yValues = new ArrayList<>();

		}

	} //End public void pullFromSharedPrefs
} //End Class
