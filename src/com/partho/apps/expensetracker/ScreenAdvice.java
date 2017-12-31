package com.partho.apps.expensetracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.partho.apps.R;

public class ScreenAdvice extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
	{
		View view = inflater.inflate(R.layout.expense_tracker_view_expenses, container, false);
		System.out.println("view: " + ((view == null) ? "NULL" : "Exists"));
		return view;
	}
}
