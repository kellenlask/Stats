package com.kellen.stats;

/**
 * Created by Kellen on 12/23/2015.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
//-------------------------------------------
//
//		Fields & Constants
//
//-------------------------------------------

	private static final int PAGE_COUNT = 3;
	public Activity mainActivity;

//-------------------------------------------
//
//		PagerAdapter Methods
//
//-------------------------------------------

	public SectionsPagerAdapter(FragmentManager manager, Activity mainActivity) {
		super(manager);

		this.mainActivity = mainActivity;

	}

	@Override
	public Fragment getItem(int position) {
		switch(position) {
			case 0:
				return Calculators.newInstance();

			case 1:
				return DataFragment.newInstance();

			default:
				return Summary.newInstance();

		} //End switch
	} //End public Fragment getItem(int)

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return mainActivity.getString(R.string.title_section1).toUpperCase();
			case 1:
				return mainActivity.getString(R.string.title_section2).toUpperCase();
			case 2:
				return mainActivity.getString(R.string.title_section3).toUpperCase();
		}
		return null;
	}
} //End public class SectionsPagerAdapter extends FragmentPagerAdapter
