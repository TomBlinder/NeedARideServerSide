package com.serverSide;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class SearchSimilarRides extends HttpServlet{
	
	public static enum rideFields {
		fromCity, fromLat, fromLng, fromStreet, fromHouseNo,
		toCity, toLat, toLng, toStreet, toHouseNo,
		date, sits, fee, driverId, comment;
		
		// The DB field name
		public String toString(rideFields rf) {
			switch (rf) {	
			case fromCity: return "fromCity";
			case fromLat: return "fromLat";
			case fromLng: return "fromLng";
			case fromStreet: return "fromStreet";
			case fromHouseNo: return "fromHouseNo";
			case toCity: return "toCity";
			case toLat: return "toLat";
			case toLng: return "toLng";
			case toStreet: return "toStreet";
			case toHouseNo: return "toHouseNo";
			case date: return "date";
			case sits: return "availableSits";
			case fee: return "rideFee";
			case driverId: return "userID";
			case comment: return "driversComment";
			}
			return ""; // we'll never get here
		}
	};

	
	//handling to income request
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		HashMap<rideFields, String> driverFields = new HashMap<rideFields, String>(); 
		//getting the known parameters
		for(rideFields f: rideFields.values()) {
			if (req.getParameter(f.toString()) != null) {
				driverFields.put(f, req.getParameter(f.toString()));
			}
		}
		
		//write back to the user the output of the function "check" as response
		resp.getWriter().println(check(driverFields));
	}
	
	public String check(HashMap<rideFields, String> fields){
		
		//set the datastore mechanism for searching the request values
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// get the filter
		Filter similarRidesFilter = getFilter(fields);
		
		//set a new query
		Query q = new Query("Ride").setFilter(similarRidesFilter);
		
		//execute the query
		PreparedQuery pq = datastore.prepare(q);
		
		//return the corresponding result
		return pq.asIterator().hasNext() ? "Found ride" : "Did not find ride";
	}
	
	private Filter getFilter(HashMap<rideFields, String> fields) {

		//set a filter for the "fromCity" property
		Filter filterFrom = new FilterPredicate(rideFields.fromCity.toString(),FilterOperator.IN,fields.get(rideFields.fromCity));
		
		//set a filter for the "to" property
		Filter filterTo = new FilterPredicate(rideFields.toCity.toString(),FilterOperator.IN,fields.get(rideFields.toCity));
		
		//combine the to filters with "and" operator
		Filter filterFromAndTo = CompositeFilterOperator.and(filterFrom,filterTo);
		
		return filterFromAndTo;
	}

}
