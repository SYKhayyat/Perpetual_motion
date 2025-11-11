package com.example.perpetual_motion.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.perpetual_motion.R;
import com.example.perpetual_motion.classes.CardPilesAdapter;
import com.example.perpetual_motion.databinding.MainIncludeActivityBottomBarAndFabBinding;
import com.example.perpetual_motion.interfaces.AdapterOnItemClickListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.perpetual_motion.databinding.ActivityMainBinding;
import com.mintedtech.perpetual_motion.pm_game.PMGame;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Game (current game) object
    private PMGame mCurrentGame;
    // Adapter (current board) object
    private CardPilesAdapter mAdapter;
    // Status Bar and SnackBar View references
    private TextView mTv_cardsRemaining, mTv_cardsInDeck;
    private View mSnackbarView;
    private Snackbar mSnackbar;
    // UI Strings
    private String mWINNER_MSG, mNON_WINNER_MSG;
    private boolean mIsNightMode; // Sent to Adapter for when it sets the suit colors
    private ActivityMainBinding binding;
    private MainIncludeActivityBottomBarAndFabBinding
            bottomBarAndFabBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        setSupportActionBar(binding.includeActivityToolbar.toolbar);


        bottomBarAndFabBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFABClick(view);
            }
        });
        setIsNightMode();
        setFieldReferencesToViewsAndSnackbar();
        setupBoard();
    }

    private void setupBoard() {
        // create the adapter which will drive the RecyclerView (Model portion of MVC)
        mAdapter = new CardPilesAdapter(mIsNightMode,
                getString(R.string.cards_in_stack));
// Set the listener object to respond to clicks in the RecyclerView
// clicks are forwarded to the listener by the ViewHolder via the Adapter
        mAdapter.setOnItemClickListener(getNewOnItemClickListener());
//mAdapter.setAnimationsEnabled(mPrefShowAnimations);
// get a reference to the RecyclerView - not a field because it's not needed elsewhere.
        RecyclerView rvPiles = findViewById(R.id.rv_piles);
// Please note the use of an xml integer here: portrait==2x2, landscape/square==1x4; neat!
        final int RV_COLUMN_COUNT =
                getResources().getInteger(R.integer.rv_columns);
// optimization setting - since there are always exactly four piles
        rvPiles.setHasFixedSize(true);
// Create a new LayoutManager object to be used in the RecyclerView
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager
                (this, RV_COLUMN_COUNT);
// apply the Layout Manager object just created to the RecyclerView
        rvPiles.setLayoutManager(layoutManager);
// apply the adapter object to the RecyclerView
        rvPiles.setAdapter(mAdapter);
    }

    /**
     * This anon implementation of our Listener interface handles adapter
     events
     * The object created here is passed to the adapter via the adapter's
     setter method
     * This object's onItemClick() is called from the Adapter when the
     user clicks on the board.
     * This leaves the Adapter to handle only the RV's data, and
     MainActivity to MVC Control...
     *
     * @return an object that responds to clicks inside a RecyclerView
    whose ViewHolder implements this interface
     */
    private AdapterOnItemClickListener getNewOnItemClickListener() {
        return (position, view) -> handleItemClick(position);
    }
    private void handleItemClick(int position) {
        try {
            if (mCurrentGame.getNumberOfCardsInStackAtPosition(position) >
                    0) {
                if (mCurrentGame.isGameOver()) {
                    showSB_AlreadyGameOver();
                } else {
                    dismissSnackBarIfShown();
                    mAdapter.toggleCheck(position);
                }
            }
// otherwise, if this stack is empty (and no card shown), then ignore the click
        } catch (Exception e) {
            Log.d("STACK", "Toggle Crashed: " + e.getMessage());
// If it crashes during testing, you might do something like this to debug...
        }
    }
    private void showSB_AlreadyGameOver() {
    }
    private void dismissSnackBarIfShown() {
        if (mSnackbar.isShown())
            mSnackbar.dismiss();
    }


    private void setIsNightMode(){
    mIsNightMode =  (getApplicationContext().getResources().getConfiguration().uiMode &
    Configuration.UI_MODE_NIGHT_MASK) ==
    Configuration.UI_MODE_NIGHT_YES;
}
    private void setContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        bottomBarAndFabBinding= MainIncludeActivityBottomBarAndFabBinding.bind(binding.getRoot());
        setContentView(binding.getRoot());
    }

    private void setFieldReferencesToViewsAndSnackbar() {
        mSnackbarView = findViewById(android.R.id.content);
        mSnackbar.make(mSnackbarView, "Welcome!", Snackbar.LENGTH_LONG);
        mTv_cardsRemaining =
                bottomBarAndFabBinding.tvCardsRemainingToDiscard; //findViewById(R.id.tv_cards_remaining_to_discard);
        mTv_cardsInDeck = bottomBarAndFabBinding.tvCardsInDeck; //findViewById(R.id.tv_cards_in_deck);
    }

    private static void handleFABClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}