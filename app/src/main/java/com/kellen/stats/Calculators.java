package com.kellen.stats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Calculators extends Fragment {
//-------------------------------------------
//
//		Fields
//
//-------------------------------------------
	//GUI elements
	View rootView;

	//Bayesian Probability
	Button bayesButton;
	TextView pOfA;
	TextView pOfB;
	TextView pOfAGivenB;
	TextView pOfBGivenA;

	//Poisson Distribution
	Button poissonButton;
	TextView llambdaView;
	TextView kView;
	TextView pOfXEqualK;

	//t Score
	Button tScoreButton;
	TextView tX;
	TextView tMu;
	TextView tN;
	TextView tS;
	TextView tT;


//-------------------------------------------
//
//		System
//
//-------------------------------------------
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static Calculators newInstance(int sectionNumber) {
		Calculators fragment = new Calculators();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_calculators, container, false);
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
		setActionHandlers();
	}

	public Calculators() {

	}

//-------------------------------------------
//
//		Action Handlers
//
//-------------------------------------------
	public void setActionHandlers() {
		//Bayesian Probability
		bayesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//All the necessary fields are filled
				if(!(pOfA.getText().toString().equals("") || pOfB.getText().toString().equals("") || pOfBGivenA.getText().toString().equals(""))) {
					double a = Double.parseDouble(pOfA.getText().toString());
					double b = Double.parseDouble(pOfB.getText().toString());
					double bGa = Double.parseDouble(pOfBGivenA.getText().toString());

					double aGb = StatisticalMethods.calcBayes(a, bGa, b);

					pOfAGivenB.setText("" + aGb);

				}

			}
		});

		//Poisson Distribution
		poissonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!llambdaView.getText().toString().equals("")) {
					double llambda = Double.parseDouble(llambdaView.getText().toString());
					double k = Double.parseDouble(kView.getText().toString());

					double probability = StatisticalMethods.calcPoisson(llambda, k);

					pOfXEqualK.setText("" + probability);

				}
			}
		});

		//t Score
		tScoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//If all the necessary fields have values
				if(!(tX.getText().toString().equals("") ||
					 tMu.getText().toString().equals("") ||
					 tS.getText().toString().equals("") ||
					 tN.getText().toString().equals(""))) {

					double x = Double.parseDouble(tX.getText().toString());
					double mu = Double.parseDouble(tMu.getText().toString());
					double s = Double.parseDouble(tS.getText().toString());
					double n = Double.parseDouble(tN.getText().toString());

					double t = StatisticalMethods.calcTScore(x, s, mu, n);

					tT.setText("" + t);
				}
			}
		});


	} //End public void setActionHandlers()

//-------------------------------------------
//
//		GUI Methods
//
//-------------------------------------------

//-------------------------------------------
//
//		Utility Methods
//
//-------------------------------------------
	public void initializeFields() {
		//Bayesian Probability
		bayesButton = (Button) rootView.findViewById(R.id.bayesButton);
		pOfA = (TextView) rootView.findViewById(R.id.pOfA);
		pOfB = (TextView) rootView.findViewById(R.id.pOfB);
		pOfAGivenB = (TextView) rootView.findViewById(R.id.pOfAGivenB);
		pOfBGivenA = (TextView) rootView.findViewById(R.id.pOfBGivenA);

		//Poisson Distribution
		poissonButton = (Button) rootView.findViewById(R.id.poissonButton);
		llambdaView = (TextView) rootView.findViewById(R.id.llambda);
		kView = (TextView) rootView.findViewById(R.id.k);
		pOfXEqualK = (TextView) rootView.findViewById(R.id.pOfXEqualK);

		//t Score
		tScoreButton = (Button) rootView.findViewById(R.id.tScoreButton);
		tX = (TextView) rootView.findViewById(R.id.tX);
		tMu = (TextView) rootView.findViewById(R.id.tMu);
		tN = (TextView) rootView.findViewById(R.id.tN);
		tS = (TextView) rootView.findViewById(R.id.tS);
		tT = (TextView) rootView.findViewById(R.id.tT);

		//Z Score

	} //End public void initializeFields()
} //End Class
