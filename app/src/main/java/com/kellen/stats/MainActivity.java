package com.kellen.stats;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {
//-------------------------------------------
//
//		Fields & Constants
//
//-------------------------------------------

	SectionsPagerAdapter sectionsPagerAdapter;
	ViewPager viewPager;

//-------------------------------------------
//
//		Initialization
//
//-------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create a FragmentPagerAdapter to control the ViewPager
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		// Set MainActivity to listen on the ViewPager's page events (So the ActionBar text can be
		// changed to match the current fragment)
		viewPager.setOnPageChangeListener(this);

		// I guess you have to set the title text the first time...
		setTitle(R.string.title_section1);

	} //End protected void onCreate(Bundle)

//-------------------------------------------
//
//		Action Handlers
//
//-------------------------------------------

	@Override
	public void onPageSelected(int position) {
		// Change the ActionBar Title to match the current fragment
		switch(position) {
			case 0:
				setTitle(R.string.title_section1);
				break;

			case 1:
				setTitle(R.string.title_section2);
				break;

			default:
				setTitle(R.string.title_section3);
				break;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// Purposefully blank
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// Purposefully blank
	}

//-------------------------------------------
//
//		Accessors
//
//-------------------------------------------

	public int getCurrentPage() {
		return viewPager.getCurrentItem();
	}

} //End public class MainActivity extends ActionBarActivity
