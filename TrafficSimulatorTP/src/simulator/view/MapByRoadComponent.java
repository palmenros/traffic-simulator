package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver {
	
	//TODO: Remove everything and create Map By Road
	
	private static final long serialVersionUID = 1L;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _ROAD_LABEL_COLOR = Color.BLACK ;

	private RoadMap _map;

	private Image _car;
	private Image[] _contamination = new Image[6];
	
	private Image _rain;
	private Image _cloud;
	private Image _storm;
	private Image _wind;
	private Image _sun;
	
	MapByRoadComponent(Controller ctrl) {
		setPreferredSize(new Dimension(300, 200));
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		
		_rain = loadImage("rain.png");
		_cloud = loadImage("cloud.png");
		_storm = loadImage("storm.png");
		_wind = loadImage("wind.png");
		_sun = loadImage("sun.png");
		
		_car = loadImage("car.png");
		for(int i = 0; i < _contamination.length; ++i) {
			_contamination[i] = loadImage("cont_" + i + ".png");
		}
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setBackground(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());
		
		if (_map == null || _map.getRoads().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void updatePrefferedSize() {
		
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}		
	}

	
	private void drawMap(Graphics g) {
		int i = 0;
		for(Road r : _map.getRoads()) {
			drawRoad(i, r, g);
			i++;
		}
	}
	
	private void drawRoad(int i, Road r, Graphics g) {
		
		int y = (i + 1) * 50;
		int startX = 50;
		int endX = getWidth() - 100;
		Junction origin = r.getOrigin();
		Junction destination = r.getDestination();	
		
		//Dibujar la linea de cada carretera
		
		g.setColor(Color.black);
		g.drawLine(startX, y, endX, y);
		
		//Dibujar circulo al comienzo del cruce
		int radius = 10;
		
		g.setColor(_JUNCTION_COLOR);
		g.fillOval(startX - radius / 2, y - radius / 2, radius, radius);
		
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(origin.getId(), startX - 5, y - 10);
		
		boolean roadIsGreen = false;
		
		//Dibujar circulo al final del cruce
		//boolean green = destination.isRoadGreen(r.getId());
		int greenLightIndex = destination.getGreenLightIndex();
		if(greenLightIndex != -1)
		{
			roadIsGreen = destination.getInRoads().get(greenLightIndex) == r;
		}

		Color color;
		if(roadIsGreen) {
			color = _GREEN_LIGHT_COLOR;
		} else {
			color = _RED_LIGHT_COLOR;
		}
		
		g.setColor(color);
		g.fillOval(endX - radius / 2, y - radius / 2, radius, radius);
		
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(destination.getId(), endX - 5, y - 10);
		
		//Draw cars
		for(Vehicle v : r.getVehicles()) {
			int vehicleX = startX + (int) ((endX - startX) * ((double) v.getLocation() / (double) r.getLength()));
			int side = 16;
			g.drawImage(_car, vehicleX - side / 2, y - side / 2, side, side, this);

			int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
			g.setColor(new Color(0, vLabelColor, 0));

			g.drawString(v.getId(), vehicleX - 6, y - 6);
		}
		
		//Draw road id
		g.setColor(_ROAD_LABEL_COLOR );
		g.drawString(r.getId(), startX - 40, y + 5);
		
		
		//Draw weather
		
		Image weatherImage = null; 
		
		switch(r.getWeather()) {
		case CLOUDY:
			weatherImage = _cloud;
			break;
		case RAINY:
			weatherImage = _rain;
			break;
		case STORM:
			weatherImage = _storm;
			break;
		case SUNNY:
			weatherImage = _sun;
			break;
		case WINDY:
			weatherImage = _wind;
			break;
		default:
			break;
		}
		
		int weatherSide = 32;
		int padding = 30;
		g.drawImage(weatherImage, endX + padding - weatherSide / 2, y - weatherSide / 2, weatherSide, weatherSide, this);
		
		//Draw contamination
		int contaminationPadding = 10;
		int C = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getCO2Limit()),1.0) / 0.19);
		g.drawImage(_contamination[C], endX + contaminationPadding + weatherSide + padding - weatherSide / 2, y - weatherSide / 2, weatherSide, weatherSide, this);
		
	}
	

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
