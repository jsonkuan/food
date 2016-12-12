package iths.com.food.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import iths.com.food.helper.DatabaseHelper;
import iths.com.food.helper.DialogHandler;
import iths.com.food.helper.MealAdapter;
import iths.com.food.helper.SwipeDismissListViewTouchListener;
import iths.com.food.model.Category;
import iths.com.food.model.Meal;
import iths.com.food.R;

/**
 * Created by jas0n on 2016-11-25.
 *
 */

public class MealListFragment extends Fragment {

    public static final String MEAL_ID = "meal_id";
    DatabaseHelper db;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_list, container, false);

        setHasOptionsMenu(true);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.meallist_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new DatabaseHelper(getActivity());

        bundle = this.getArguments();
        String category = bundle.getString(CategoryFragment.CHOSEN_CATEGORY);
        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();
        Category listCategory = db.getCategory(category);
        final ArrayList<Meal> mealsInCategory = listCategory.getMeals();
        ArrayList<String> idArray = new ArrayList<>();
        for(int i = 0; i < mealsInCategory.size(); i++) {
            idArray.add(Long.toString(mealsInCategory.get(i).getId()));
        }


        MealAdapter adapter = new MealAdapter(getActivity(), idArray);
        ListView listView = (ListView) view.findViewById(R.id.fragmentMealList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         long chosenMeal = Long.valueOf(String.valueOf(adapterView.getItemAtPosition(i)));
                        openMeal(chosenMeal);
                    }
                }
        );

        db.close();



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
                            //db.deleteCategory(foodtypes.get(position));
                            long ID = mealsInCategory.get(position).getId();


                            Toast.makeText(getActivity(), ""+ID, Toast.LENGTH_SHORT).show();


                            doSwipe((int)ID);
                            /*db.deleteMeal(ID);
                            MealListFragment newFragment = new MealListFragment();
                            newFragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();*/
                        }
                    }
                }
        );
        listView.setOnTouchListener(touchListener);


        return view;
    }




    public void doSwipe(int ID) {
        DialogHandler appdialog = new DialogHandler();


        double score = db.getMeal(ID).getTotalScore();


        appdialog.Confirm(getActivity(), "Are you sure you want to delete?", "This meal has " + score + " score.",
                "Cancel", "OK", okPressed(ID), cancelPressed());
    }
    public Runnable okPressed(int ID){
        final int finalID = ID;
        return new Runnable() {
            public void run() {
                db.deleteMeal(finalID);
                MealListFragment newFragment = new MealListFragment();
                newFragment.setArguments(bundle);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //TODO: Implement back button
                break;
            default: super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMeal(long id) {
        MealFragment newFragment = new MealFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(MEAL_ID, id);
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }
}

