package com.serverSide;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class AddNewRide extends HttpServlet {

	//handling to incoming request
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		//getting the known parameters
		String userID = req.getParameter("userID");
		
		String fromStreet = req.getParameter("fromStreet");
		String fromCity = req.getParameter("fromCity");
		String fromHouseNo = req.getParameter("fromHouseNo");
		String fromLat = req.getParameter("fromLat");
		String fromLng = req.getParameter("fromLng");
		
		String toStreet = req.getParameter("toStreet");
		String toCity = req.getParameter("toCity");
		String toHouseNo = req.getParameter("toHouseNo");
		String toLat = req.getParameter("toLat");
		String toLng = req.getParameter("toLng");
		
		String date = req.getParameter("date");
		
		String availableSits = req.getParameter("availableSits");
		String rideFee = req.getParameter("rideFee");
		String driversComment = req.getParameter("driversComment");
		
		//write back to the user the output of the function "add" as response
		resp.getWriter().println(add(userID,fromStreet,fromCity,fromHouseNo,fromLat,fromLng,toStreet, toCity,toHouseNo,
				toLat,toLng,date,availableSits,rideFee, driversComment));
	}
	
	public String add(String userID,String fromStreet,String fromCity,String fromHouseNo,String fromLat,String fromLng,
					  String toStreet, String toCity,String toHouseNo,String toLat,String toLng,
					  String date,String availableSits,String rideFee, String driversComment){
		
	//set the datastore mechanism for writing the new ride
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	//set a new entity from kind Ride
	Entity ride = new Entity("Ride");
	
	//set its Properties
	ride.setProperty("userID", userID);
	
	ride.setProperty("fromStreet", fromStreet);
	ride.setProperty("fromCity",fromCity);
	ride.setProperty("fromHouseNo",fromHouseNo);
	ride.setProperty("fromLat",fromLat);
	ride.setProperty("fromLng",fromLng);
	
	ride.setProperty("toStreet", toStreet);
	ride.setProperty("toCity", toCity);
	ride.setProperty("toHouseNo",toHouseNo);
	ride.setProperty("toLat",toLat);
	ride.setProperty("toLng",toLng);
	
	ride.setProperty("date", date);
	ride.setProperty("availableSits",availableSits);
	ride.setProperty("rideFee",rideFee);
	ride.setProperty("driversComment",driversComment);
	
	//write the Ride to DB
	datastore.put(ride);
	
	return "Added OK to DB";
	}
	
	
	
}
