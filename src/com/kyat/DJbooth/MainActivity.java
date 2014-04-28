package com.kyat.DJbooth;

import info.kyat.DJBooth.adapter.NavDrawerListAdapter;
import info.kyat.DJBooth.model.NavDrawerItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	
	/*
	//////////////////////////////////
	/ Begin code for NewSnap section /
	//////////////////////////////////
	*/

	/*
	//////////////////////////////////
	/ Begin code for Account section /
	//////////////////////////////////
	*/
	
	/*
	//////////////////////////////////
	/ Begin Google Calendar integration /
	//////////////////////////////////
	*/	

	/*
	//////////////////////////////////
	/ Begin code for Snaps section /
	//////////////////////////////////
	*/
	
	public File origPhoto, outputPhoto;
	private Boolean takenSnapFlag = false, snapSavedFlag = false;
	private int TAKENPHOTO = 0, rotation = 0;
	public Bitmap photoBMP;
	String modelNo = "THX-1138", userName = "Anonymouse";
	ImageView ivdisplayphoto;
	
	
	//open a new fragment to take a snap
	public void newSnap (View newSnap){
		Fragment fragment = null;
		fragment = new NewSnapFragment();
		
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
		} else
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");		
	}
	
	//function to open the RMTCamera folder in gallery
	public void openGallery (View gallery){
		 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
		 "content://media/internal/images/media")); 
		 startActivity(intent); 
	}
	
	//function to take a photo with the default camera app
		public void takePhoto(View v){
	    	//
	    	File picStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    	origPhoto = new File(picStorage, (System.currentTimeMillis()) + ".jpg");
	    	
	    	//Intent to start the camera
	    	Intent getPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    	getPhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origPhoto));
	    	startActivityForResult(getPhoto, TAKENPHOTO);
	    	
	    	
	    }
	
		public void genBMP(){
			try{
	            photoBMP = PhotoFunctions.getPhotoBitmap(origPhoto.getAbsolutePath());
	            photoBMP = PhotoFunctions.rotateBitmap(photoBMP,rotation);
	            }
	            catch(NullPointerException ex){
	                     photoBMP = PhotoFunctions.getPhotoBitmap(origPhoto.getAbsolutePath());
	            }
			if(photoBMP != null){
			showThumbnail();    
			}
		}
		
		public void rotateTheRainbow(View assAnTitties){
	    	if(takenSnapFlag == true){
				try{
				 rotation = (rotation %360) + 90;
				 
				 photoBMP = PhotoFunctions.rotateBitmap(photoBMP,90);
				 showThumbnail();
		    	}
		    	catch(NullPointerException ne){}
	    	}
	    	else{
	    		Toast.makeText(this, "You must take a snap first", Toast.LENGTH_SHORT).show();
	    	}
	    }
		
		public void savePhoto(View assNipples){
		    if(takenSnapFlag == true){
			    try{
			    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setTitle("Model Number");
					alert.setMessage("Please input a model number if applicable, otherwise leave this blank.");
					genBMP();
			
					// Set an EditText view to get user input 
					final EditText modelNoInput = new EditText(this);
					alert.setView(modelNoInput);
					modelNoInput.setText(modelNo);
					alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						//convert editable to a string
						modelNo = modelNoInput.getText().toString();
						
						if(modelNo.equals("")){
						Toast.makeText(MainActivity.this, "Okay, will not display model number.", Toast.LENGTH_LONG).show();	
						}
						else{
						Toast.makeText(MainActivity.this, "Model number is: " + modelNo, Toast.LENGTH_LONG).show();
						}
						
						//editPhoto();
						photoBMP = PhotoFunctions.addTextToBitmap(photoBMP, userName, modelNo, PhotoFunctions.getTextScale(origPhoto.getAbsolutePath()));
						savePart2();
					  }
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
					    //do nothing
					  }
					});
					alert.show();
			    	}catch(NullPointerException npe){Toast.makeText(MainActivity.this, "dicks", Toast.LENGTH_LONG).show();}
		    	}
		    else{
		    	Toast.makeText(this,"You need to take a photoBMP first.", Toast.LENGTH_LONG).show();
		    	}
		    }
		
		public void savePart2(){
    		try{
		  		String textPhotoName;
		  		textPhotoName = userName + "photoBMP-" + PhotoFunctions.getDate() + ".jpg";
		    	
		    	File newDir;
		    	
		    	//make a new directory at root/RMT Camera
		    	newDir = new File(Environment.getExternalStorageDirectory().toString() + "/RMT Camera");
		    	newDir.mkdirs();
		    	outputPhoto = new File(newDir, textPhotoName);
		    	
		    	//Check if the picture file already exists.
		    	//if so, PURGE IN HOLY NIPPLES.
		    	if(photoBMP != null){
			    	if(outputPhoto.exists()){
			    		outputPhoto.delete();
			    	}
			    	try{
			    		FileOutputStream out = new FileOutputStream(outputPhoto);
			    		photoBMP.compress(Bitmap.CompressFormat.JPEG, 100, out);
			    		out.flush();
			    		out.close();
			    		Toast.makeText(getApplicationContext(), "Image saved to your gallery! ", Toast.LENGTH_SHORT ).show();
			    		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()))); //update the gallery once saved
			    		snapSavedFlag = true;
			    	} catch (Exception e) {
			        	e.printStackTrace();
			        }
		    	}
		    	else Toast.makeText(getApplicationContext(), "Error, image not found in gallery", Toast.LENGTH_SHORT ).show();
		    	
    		}catch(NullPointerException ne){Toast.makeText(this,"Please take a photoBMP first", Toast.LENGTH_LONG).show();}
    	
    }
		
		public void showThumbnail(){
			ivdisplayphoto = (ImageView)findViewById(R.id.iv_displayphoto);
			if(photoBMP != null){
                ivdisplayphoto.setImageBitmap(photoBMP);
                //set this to visible to get the slider back :)
                //sbSeekBar.setVisibility(View.GONE);

			}
			else{
                takenSnapFlag = false;
			}
		}
		
		
	/*
	//////////////////////////////////
	/ Begin code for help section	 /
	//////////////////////////////////
	*/
	
	//function for the report a bug button
	public void reportBug (View reportBug){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getResources().getString(R.string.contactEmail)});
		i.putExtra(Intent.EXTRA_SUBJECT, "RMT Tools Feedback");
		i.putExtra(Intent.EXTRA_TEXT   , "Your feedback here...");
		try {
		    startActivity(Intent.createChooser(i, "Send Feedback"));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}
	
	//function to display terms and license information
	public void showTerms (View showTerms){
	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	alert.setTitle(getResources().getString(R.string.termsTitle));
	//this should really be a string...
	alert.setMessage(getResources().getString(R.string.termsMessage));
	alert.setPositiveButton("View Full License", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			//open the license page
			Uri uri = Uri.parse("http://creativecommons.org/licenses/by-sa/4.0/legalcode");
			 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 startActivity(intent);
		}
	});
	alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton){
			//Close
		}
	});
	alert.show();
	}
	
    
	/*
	//////////////////////////////////
	/ Begin menu code				 /
	//////////////////////////////////
	*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Show the "What's New" screen once for each new release of the application
	    new WhatsNewScreen(this).show();

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// counters will need to point to a variable showing unread items later. for now we can just pretend~
		// Newsfeed
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1), true, "20+"));
		// Snaps
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Planner
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Leaderboard
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Badges
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// Vacancies
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "2"));
		// My Account
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		// Settings
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		// Help
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
		
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new SnapFragment();
			break;
		case 2:
			fragment = new PlannerFragment();
			break;
		case 3:
			fragment = new LeaderboardsFragment();
			break;
		case 4:
			fragment = new BadgesFragment();
			break;
		case 5:
			fragment = new VacanciesFragment();
			break;
		case 6:
			fragment = new AccountFragment();
			break;
		case 7:
			fragment = new SettingsFragment();
			break;
		case 8:
			fragment = new HelpFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	//Will be generalised function, so im not going to group it with any of the specific fragment stuff.
	//Just add Activity result codes here.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKENPHOTO){
    		takenSnapFlag = true;
    		genBMP();
        }

}
}
