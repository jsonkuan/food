package iths.com.food.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import iths.com.food.Helper.CategoryAdapter;
import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import iths.com.food.R;

/**
 * Created by asakwarnmark on 2016-11-23.
 */

public class CategoryFragment extends Fragment implements OnClickListener{

    public static final String CHOSEN_CATEGORY = "category";
    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        ArrayList<Category> categories = db.getCategories();
        foodtypes = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            foodtypes.add(categories.get(i).getName());
        }

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), foodtypes);
        ListView listView = (ListView) v.findViewById(R.id.fragmentCategory);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String chosenCategory = String.valueOf(adapterView.getItemAtPosition(i));
                        showCategory(chosenCategory);
                    }
                }
        );

        listView.setAdapter(adapter);
        db.close();
        return v;
    }

    private void showCategory(String category) {
        MealFragment newFragment = new MealFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHOSEN_CATEGORY, category);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }

    //TODO: Change NewCategoryActivity to Fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == NewCategoryActivity.REQUEST_CODE) {
        String text = data.getStringExtra(NewCategoryActivity.EDIT_TEXT_KEY);
        foodtypes.add(text);
        ((BaseAdapter) listAdapter).notifyDataSetChanged();
        //}
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_heart_health_2:

                break;
            case R.id.edit_health_hearts:

                break;
            default:
                System.out.println("WTF is going on?");
        }
    }



}
