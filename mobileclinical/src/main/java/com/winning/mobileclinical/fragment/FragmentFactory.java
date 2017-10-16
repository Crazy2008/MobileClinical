package com.winning.mobileclinical.fragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	public static Fragment getFragment(String name){
		try {
			Class c = Class.forName(name);
			Fragment fragment = (Fragment) c.newInstance();
			return fragment;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
