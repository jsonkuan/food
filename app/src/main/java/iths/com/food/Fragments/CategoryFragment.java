package iths.com.food.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import iths.com.food.helper.CategoryAdapter;
import iths.com.food.helper.DatabaseHelper;
import iths.com.food.helper.DialogHandler;
import iths.com.food.helper.GPSHelper;
import iths.com.food.helper.SwipeDismissListViewTouchListener;
import iths.com.food.model.Category;
import iths.com.food.R;

/**
 * Created by asakwarnmark on 2016-11-23.
 *
 */

public class CategoryFragment extends Fragment {

    public static final String CHOSEN_CATEGORY = "category";
    public ArrayList<String> foodtypes;
    ListAdapter listAdapter;
    DatabaseHelper db;
    CategoryAdapter adapter;
    //boolean deleteDB = true;    // UNCOMMENT this code block to reset the database in the emulator
    GPSHelper gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gps = new GPSHelper(getActivity());
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) v.findViewById(R.id.category_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        myToolbar.setTitle("FoodFlash!");
        myToolbar.setLogo(R.drawable.empty_heart);

        db = new DatabaseHelper(this.getActivity().getApplicationContext());
        /**
         * UNCOMMENT this code block to reset the database in the emulator
         */
        /*if(deleteDB) {
            context.deleteDatabase("food.db");
            deleteDB = false;
            Toast.makeText(getActivity(), "Database deleted", Toast.LENGTH_SHORT).show();
        }*/
        ArrayList<Category> categories = db.getCategories();

        //sort categories by score, highest first
        sortCategories(categories);

        foodtypes = new ArrayList<>(categories.size());
        for (int i = 0; i < categories.size(); i++) {
            foodtypes.add(categories.get(i).getName());
        }

        adapter = new CategoryAdapter(getActivity(), foodtypes);
        ListView listView = (ListView) v.findViewById(R.id.fragmentCategory);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String chosenCategory = String.valueOf(adapterView.getItemAtPosition(i));
                        showCategory(chosenCategory);
                    }
                }
        );

        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
                listView,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }


                        @Override
                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                            for (int position : reverseSortedPositions) {
                                doSwipe(position);


                            }
                        }
                    }
            );
            listView.setOnTouchListener(touchListener);

            db.close();
            return v;
        }

    public void doSwipe(int position) {
        DialogHandler appdialog = new DialogHandler();

        int numMeals = db.getCategory(adapter
                .getItem(position))
                .getMeals().size();

        appdialog.Confirm(getActivity(), "Are you sure you want to delete?", "There is " + numMeals+ " meals in this category.",
                "Cancel", "OK", okPressed(position), cancelPressed());
    }
    public Runnable okPressed(int position){
        final int finalPsition = position;
        return new Runnable() {
            public void run() {
                db.deleteCategory(foodtypes.get(finalPsition));
                CategoryFragment newFragment = new CategoryFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
            }
        };
    }
    public Runnable cancelPressed(){
        return new Runnable() {
            public void run() {
            }
        };
    }
    
/*
db.deleteCategory(foodtypes.get(position));
        CategoryFragment newFragment = new CategoryFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
*/










    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.meal_category_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_category_item:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new NewCategoryFragment()).commit();
                break;
            default:
                System.out.println("error");
        }

        return true;

    }
    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ((BaseAdapter) listAdapter).notifyDataSetChanged();
    }
    private void showCategory(String category) {
        MealListFragment newFragment = new MealListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHOSEN_CATEGORY, category);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }

    public void sortCategories(ArrayList<Category> categories){
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category c1, Category c2) {
                if (c1.getAverageScore() > c2.getAverageScore())
                    return -1;
                else if (c1.getAverageScore() < c2.getAverageScore())
                    return 1;
                else return 0;
            }
        });
    }
}