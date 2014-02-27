package com.serverSide;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jsp.ah.datastoreViewerBody_jsp;

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
	
	//handling to income request
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		
		//getting the known parameters
		String from = req.getParameter("fromCity");
		String to = req.getParameter("toCity");
		
		//write back to the user the output of the function "check" as response
		resp.getWriter().println(check(from,to));
	}
	
	public String check(String fromCity, String toCity){
		
		//set the datastore mechanism for searching the request values
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		//set a filter for the "from" property
		Filter filterFrom = new FilterPredicate("fromCity",FilterOperator.IN,fromCity);
		
		//set a filter for the "to" property
		Filter filterTo = new FilterPredicate("toCity",FilterOperator.IN,toCity);
		
		//combine the to filters with "and" operator
		Filter filterFromAndTo = CompositeFilterOperator.and(filterFrom,filterTo);
		
		//set a new query
		Query q = new Query("Ride").setFilter(filterFromAndTo);
		
		//execute the query
		PreparedQuery pq = datastore.prepare(q);
		
		//return the corresponding result
		return pq.asIterator().hasNext() ? "Found ride" : "Did not find ride";
	}

}
