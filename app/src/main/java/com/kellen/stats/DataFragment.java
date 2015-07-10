package com.kellen.stats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DataFragment extends Fragment {
//-------------------------------------------
//
//		Fields
//
//-------------------------------------------
	//GUI elements
	Button addCoordsButton;
	TextView xValue;
	TextView yValue;
	ListView enteredValues;
	View rootView;

	//Logical Elements
	ArrayList<Double> xValues;
	ArrayList<Double> yValues;

//-------------------------------------------
//
//		System
//
//-------------------------------------------
	private static final String ARG_SECTION_NUMBER = "section_number";

	public static DataFragment newInstance(int sectionNumber) {
		DataFragment fragment = new DataFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_data, container, false);
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


	public DataFragment() {
	}

//-------------------------------------------
//
//		Action Handlers
//
//-------------------------------------------
	public void setActionHandlers() {
		addCoordsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bothXandYPopulated()) {
					Double x = Double.parseDouble(xValue.getText().toString());
					Double y = Double.parseDouble(yValue.getText().toString());

					xValues.add(x);
					yValues.add(y);

					xValue.setText("");
					yValue.setText("");

					storeToSharedPrefs();
					updateListView();

				}
			}
		});

		//On Long-Press: remove item
		enteredValues.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				xValues.remove(position);
				yValues.remove(position);

				DataFragment.this.storeToSharedPrefs();
				DataFragment.this.updateListView();

				return true;
			}
		});


	} //End public void setActionHandlers()

//-------------------------------------------
//
//		GUI Methods
//
//-------------------------------------------
	//Update the ListView on the screen to represent the contents of the x and y arrays
	public void updateListView() {
		ArrayList<String> listText = new ArrayList<>();

		for(int i = 0; i < xValues.size(); i ++) {
			listText.add("(" + xValues.get(i) + ", " + yValues.get(i) + ")");
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, listText);
		enteredValues.setAdapter(adapter);

	} //End public void updateListView()

//-------------------------------------------
//
//		Utility Methods
//
//-------------------------------------------
	public void initializeFields() {
		//Set the GUI Elements
		addCoordsButton = (Button) rootView.findViewById(R.id.addButton);
		xValue = (TextView) rootView.findViewById(R.id.xValue);
		yValue = (TextView) rootView.findViewById(R.id.yValue);
		enteredValues = (ListView) rootView.findViewById(R.id.enteredValues);

		//Set the other elements
		pullFromSharedPrefs();
		updateListView();

	} //End public void initializeFields()

	public boolean bothXandYPopulated() {
		boolean xEmpty = xValue.getText().toString().equals("");
		boolean yEmpty = yValue.getText().toString().equals("");

		return !xEmpty && !yEmpty;

	} //End public boolean bothXandYPopulated()

//-------------------------------------------
//
//		Storage Methods
//
//-------------------------------------------

	public void storeToSharedPrefs() {
		//Prepare SharedPreferences
		SharedPreferences sp = rootView.getContext().getSharedPreferences("prefs", rootView.getContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		try {
			//Store X Values
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(xValues);
			so.close();

			editor.putString("XVALS", bo.toString("ISO-8859-1"));

			//Store Y Values
			bo = new ByteArrayOutputStream();
			so = new ObjectOutputStream(bo);
			so.writeObject(yValues);
			so.close();

			editor.putString("YVALS", bo.toString("ISO-8859-1"));

			editor.commit();

		} catch(IOException e) {
			e.printStackTrace();
		}

	} //End public void storeToSharedPrefs()

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
