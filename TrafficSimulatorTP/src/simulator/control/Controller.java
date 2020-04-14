package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator simulator;
	private Factory<Event> eventFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory)
	{
		if(sim == null || eventsFactory == null) {
			throw new IllegalArgumentException("Controller arguments cannot be null");
		}
		
		this.simulator = sim;
		this.eventFactory = eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		
		try {
			JSONObject jo = new JSONObject(new JSONTokener(in));
			JSONArray jarr = jo.getJSONArray("events");
			
			for(int i = 0; i < jarr.length(); i++) {
				JSONObject obj = jarr.getJSONObject(i);
				Event e = eventFactory.createInstance(obj);
				
				simulator.addEvent(e);
			}
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	//TODO: Review that command line mode still works properly
	public void run(int n)
	{
		run(n, null);
	}
	
	public void run(int n, OutputStream out) {
		
		JSONObject jo = new JSONObject();
		JSONArray jarr = new JSONArray();
				
		for(int i = 0; i < n; i++) {
			simulator.advance();
			
			//For performance reasons (branch predictors will eliminate the cost of this if), 
			//we won't update the JSON objects if they aren't going to be displayed
			if(out != null) {
				jarr.put(simulator.report());				
			}
		}
		
		//Only display if out is not null
		if(out != null) {
			jo.put("states", jarr);
			
			//Print JSON to out
			PrintStream ps = new PrintStream(out);
			ps.println(jo);
			ps.flush();
			ps.close();			
		}
	}
	
	public void reset() {
		simulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		simulator.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		simulator.addEvent(e);
	}
	
}
