package com.satelliteview;

//import android.app.Activity;
//import java.io.IOException;
import java.util.List;
//import java.util.Locale;

//import android.location.Address;
//import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
//import android.widget.ZoomControls;
//import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import com.google.android.maps.Overlay;
//import android.view.MotionEvent;
import android.widget.Toast;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class SatelliteViewActivity extends MapActivity {
    /** Called when the activity is first created. */

		MapView mapView;
		MapController mc;
		GeoPoint p;
		
		private LocationManager lm;
		private LocationListener locationListener;
		
		/* If in case you want to define zoom size use ZoomControls class
		//ZoomControls zoomControls;
		//MapController mapController;*/
		
		class MapOverlay extends com.google.android.maps.Overlay
		{
			@Override
			
			public boolean draw(Canvas canvas,MapView mapview,boolean shadow,long when)
			{
				super.draw(canvas, mapView, shadow); // Draws a canvas for displaying Map
				
				Point screenPts=new Point();
				mapView.getProjection().toPixels(p, screenPts);
				
				Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.pushpin2);
				canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50,null);
				return true;
			}
			
			//@Override
			
			// Following onTouchEvent will Get the current location and displays the address of the location when touched on the mobile screen
			
			/*public boolean onTouchEvent(MotionEvent event, MapView mapView)
			{
				if(event.getAction()==1)
				{
					GeoPoint p=mapView.getProjection().fromPixels(
							(int) event.getX(),
							(int) event.getY());
					
				Toast.makeText(getBaseContext(),"Location: "+
							p.getLatitudeE6() / 1E6 + "," + 
							p.getLongitudeE6() / 1E6 ,
							Toast.LENGTH_SHORT).show();//
				
					
					Geocoder geocoder = new Geocoder( getBaseContext(), Locale.getDefault());
					
					try
					{
						List<Address> addresses = geocoder.getFromLocation(p.getLatitudeE6() / 1E6, p.getLongitudeE6() / 1E6, 1);
						String add = "";
						if (addresses.size() > 0)
						{
							for (int i =0;i<addresses.get(0).getMaxAddressLineIndex();i++)
							{
								add += addresses.get(0).getAddressLine(i) + "\n";
							}
						}
						Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
					}

					catch(IOException e)
					{
						e.printStackTrace();
					}
					
					return true;
				}
				return false;
			}*/
			
		}
		
	    @SuppressWarnings("deprecation")
		/** Called when the activity is first created. */
	    
	    // This is one of the method of android life cycle which will be always called first
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        mapView=(MapView)findViewById(R.id.mapView);
	        mapView.setBuiltInZoomControls(true);	//Uses built in zoom controls of MapView class
	        mapView.setSatellite(true);	// Sets to satellite view to true.. So displays satellite view of map
	        
	        mapView.setStreetView(true);	
	        //mapView.setTraffic(true);	//
	        //mapView.displayZoomControls(true);
	        mc=mapView.getController();
	        String coordinates[] = {"12.934044","77.61119"}; //To set a startup coordinates or location when the project will be loaded
	        double lat=Double.parseDouble(coordinates[0]);
	        double lan=Double.parseDouble(coordinates[1]);
	 
	        p=new GeoPoint(
	        		(int)(lat * 1E6),
	        		(int)(lan * 1E6));
	        
	        mc.animateTo(p);
	        mc.setZoom(13);
	    
	        //----Add a location marker
	        MapOverlay mapOverlay=new MapOverlay(); 
	        List<Overlay> listOfOverlays= mapView.getOverlays();
	        listOfOverlays.clear();
	        listOfOverlays.add(mapOverlay);
	        
	        mapView.invalidate();	        
	    
	        
	        //---- Use the location class to obtain location data
	        /*String number=returnNumber();
	        Toast.makeText(getBaseContext(), "User Number:"+number, Toast.LENGTH_SHORT).show();*/
	        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        locationListener =new MyLocationListener();
	        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,0,locationListener);
	    
	    }
	    
	    /*-- To fetch the telephone number of the user
	     public String returnNumber() {
	        String number = null;
	        String service = Context.TELEPHONY_SERVICE;
	        TelephonyManager tel_manager = (TelephonyManager) getSystemService(service);
	        int device_type = tel_manager.getPhoneType();

	        switch (device_type) {
	              case (TelephonyManager.PHONE_TYPE_GSM):
	                 number = tel_manager.getLine1Number();
	              break;
	              default:
	                //return something else
	                number = "no number";
	               break;
	        }
	        return number;
	   }*/
	    
	    //-- following method helps to relocate the user on change of location
	    private class MyLocationListener implements LocationListener
	    {
	    	public void onLocationChanged(Location loc)
	    	{
	    		if(loc!=null)
	    		{
	    			p = new GeoPoint((int)(loc.getLatitude() * 1E6),
							(int)(loc.getLongitude() * 1E6));
	    			
	    			Toast.makeText(getBaseContext(),"Location: "+
							loc.getLatitude() + "," + 
							loc.getLongitude() ,
							Toast.LENGTH_SHORT).show();//
					
	    		}
	    		
	    		// to re-animate the marker to the changed location
	    		mc=mapView.getController();
	    			
					
					
					mc.animateTo(p);
					mc.setZoom(16);
					mapView.invalidate();
			
			
	    		}
	    	
	    	public void onProviderDisabled(String provider)
	    	{
	    		
	    	}
	    	
	    	public void onProviderEnabled(String provider)
	    	{
	    		
	    	}
	    	
	    	public void onStatusChanged(String provider, int status, Bundle extras)
	    	{
	    		
	    	}
	    	
	    	    	
	    }
	    
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	MapController mc=mapView.getController();
	    	switch(keyCode)
	    	{
	    	case KeyEvent.KEYCODE_3:
	    		mc.zoomIn();
	    		break;
	    		
	    	case KeyEvent.KEYCODE_1:
	    		mc.zoomOut();
	    		break;
	    	}
	    	
	    	return super.onKeyDown(keyCode, event);
	    }
	    @Override
	    protected boolean isRouteDisplayed()
	    {
			return false;
	    	
	    }
	    
	}
	