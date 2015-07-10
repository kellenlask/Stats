package com.kellen.stats;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
	//UI elements
	View rootView;
	SensorManager sensorManager;
	Sensor accelerometer;
	ShakeDetector shakeDetector;
	boolean alertActive;

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
//		Constructors and Such
//
//-------------------------------------------
	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		initializeFields();
		setActionHandlers();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Add the following line to register the Session Manager Listener onResume
		sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause() {
		// Add the following line to unregister the Sensor Manager onPause
		sensorManager.unregisterListener(shakeDetector);
		super.onPause();
	}

	public Calculators() {

	}

//-------------------------------------------
//
//		Action Handlers
//
//-------------------------------------------
	public void setActionHandlers() {
		//Shake Detection
		shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

			@Override
			public void onShake(int count) {
				showDialog();
			}
		});

		//Bayesian Probability
		bayesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					double[] valueArray = new double[4];

					if (pOfA.getText().toString().equals("")) { //Solve for P(A)
						valueArray[3] = Double.parseDouble(pOfAGivenB.getText().toString());
						valueArray[1] = Double.parseDouble(pOfB.getText().toString());
						valueArray[2] = Double.parseDouble(pOfBGivenA.getText().toString());

						valueArray = StatisticalMethods.calcBayes(valueArray, 0);

						pOfA.setText("" + valueArray[0]);

					} else if (pOfB.getText().toString().equals("")) { //Solve for P(B)
						valueArray[0] = Double.parseDouble(pOfA.getText().toString());
						valueArray[3] = Double.parseDouble(pOfAGivenB.getText().toString());
						valueArray[2] = Double.parseDouble(pOfBGivenA.getText().toString());

						valueArray = StatisticalMethods.calcBayes(valueArray, 1);

						pOfB.setText("" + valueArray[1]);

					} else if (pOfBGivenA.getText().toString().equals("")) { //Solve for P(B|A)
						valueArray[0] = Double.parseDouble(pOfA.getText().toString());
						valueArray[1] = Double.parseDouble(pOfB.getText().toString());
						valueArray[3] = Double.parseDouble(pOfAGivenB.getText().toString());

						valueArray = StatisticalMethods.calcBayes(valueArray, 2);

						pOfBGivenA.setText("" + valueArray[2]);

					} else { //Solve for P(A|B)
						valueArray[0] = Double.parseDouble(pOfA.getText().toString());
						valueArray[1] = Double.parseDouble(pOfB.getText().toString());
						valueArray[2] = Double.parseDouble(pOfBGivenA.getText().toString());

						valueArray = StatisticalMethods.calcBayes(valueArray, 3);

						pOfAGivenB.setText("" + valueArray[3]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		//Poisson Distribution
		poissonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					double[] values = new double[3];

					if (llambdaView.getText().toString().equals("")) {
						values[2] = Double.parseDouble(pOfXEqualK.getText().toString());
						values[1] = Double.parseDouble(kView.getText().toString());

						values = StatisticalMethods.calcPoisson(values, 0);

						llambdaView.setText("" + values[0]);

					} else if (kView.getText().toString().equals("")) {
						values[0] = Double.parseDouble(llambdaView.getText().toString());
						values[2] = Double.parseDouble(pOfXEqualK.getText().toString());

						values = StatisticalMethods.calcPoisson(values, 1);

						kView.setText("" + values[1]);

					} else {
						values[0] = Double.parseDouble(llambdaView.getText().toString());
						values[1] = Double.parseDouble(kView.getText().toString());

						values = StatisticalMethods.calcPoisson(values, 2);

						pOfXEqualK.setText("" + values[2]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//t Score
		tScoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					double[] values = new double[5];

					if (tX.getText().toString().equals("")) {
						values[4] = Double.parseDouble(tT.getText().toString());
						values[1] = Double.parseDouble(tMu.getText().toString());
						values[2] = Double.parseDouble(tS.getText().toString());
						values[3] = Double.parseDouble(tN.getText().toString());

						values = StatisticalMethods.calcTScore(values, 0);

						tX.setText("" + values[0]);

					} else if (tMu.getText().toString().equals("")) {
						values[0] = Double.parseDouble(tX.getText().toString());
						values[4] = Double.parseDouble(tT.getText().toString());
						values[2] = Double.parseDouble(tS.getText().toString());
						values[3] = Double.parseDouble(tN.getText().toString());

						values = StatisticalMethods.calcTScore(values, 1);

						tMu.setText("" + values[1]);

					} else if (tS.getText().toString().equals("")) {
						values[0] = Double.parseDouble(tX.getText().toString());
						values[1] = Double.parseDouble(tMu.getText().toString());
						values[4] = Double.parseDouble(tT.getText().toString());
						values[3] = Double.parseDouble(tN.getText().toString());

						values = StatisticalMethods.calcTScore(values, 2);

						tS.setText("" + values[2]);

					} else if (tN.getText().toString().equals("")) {
						values[0] = Double.parseDouble(tX.getText().toString());
						values[1] = Double.parseDouble(tMu.getText().toString());
						values[2] = Double.parseDouble(tS.getText().toString());
						values[4] = Double.parseDouble(tT.getText().toString());

						values = StatisticalMethods.calcTScore(values, 3);

						tN.setText("" + values[3]);

					} else {
						values[0] = Double.parseDouble(tX.getText().toString());
						values[1] = Double.parseDouble(tMu.getText().toString());
						values[2] = Double.parseDouble(tS.getText().toString());
						values[3] = Double.parseDouble(tN.getText().toString());

						values = StatisticalMethods.calcTScore(values, 4);

						tT.setText("" + values[4]);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


	} //End public void setActionHandlers()

//-------------------------------------------
//
//		GUI Methods
//
//-------------------------------------------
	public void clearTextFields() {
		//Bayesian
		pOfA.setText("");
		pOfB.setText("");
		pOfAGivenB.setText("");
		pOfBGivenA.setText("");

		//Poisson
		llambdaView.setText("");
		kView.setText("");
		pOfXEqualK.setText("");

		//t score
		tX.setText("");
		tMu.setText("");
		tN.setText("");
		tS.setText("");
		tT.setText("");
	} //End public void clearTextFields()

	public void showDialog() {
		if (!alertActive) {
			alertActive = true;

			AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

			builder.setTitle("Clear All");
			builder.setMessage("Do you want to clear all fields?");

			builder.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					clearTextFields();
					dialog.dismiss();
					alertActive = false;
				}

			});

			builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Do nothing
					dialog.dismiss();
					alertActive = false;
				}
			});

			AlertDialog alert = builder.create();
			alert.show();
		}
	} //End public void showDialog()

//-------------------------------------------
//
//		Utility Methods
//
//-------------------------------------------
	public void initializeFields() {
		alertActive = false;

		//Shake Detection
		sensorManager = (SensorManager) rootView.getContext().getSystemService(rootView.getContext().SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		shakeDetector = new ShakeDetector();

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
