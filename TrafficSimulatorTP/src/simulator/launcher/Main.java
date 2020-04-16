package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private final static Integer _timeLimitDefaultValue = 10;
	private static String _inFile = null;
	private static String _outFile = null;
	private static Integer _timeLimit;
	private static Factory<Event> _eventsFactory = null;
	private static boolean _guiMode;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseGuiOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseStepsOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}
	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Gui / console").build());
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10)").build());
		return cmdLineOptions;
	}

	private static void parseGuiOption(CommandLine line) {
		String str = line.getOptionValue("m", "gui");
		if("gui".equals(str)) {
			_guiMode = true;
		} else if ("console".equals(str)) {
			_guiMode = false;
		} else {
			throw new IllegalArgumentException("Illegal value for mode option");
		}
	}
	
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && !_guiMode) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseStepsOption(CommandLine line) throws ParseException
	{
		try {
			_timeLimit = Integer.parseInt(line.getOptionValue("t", _timeLimitDefaultValue.toString()));
		} catch(NumberFormatException e) {
			throw new ParseException("The ticks to the main loop cannot be parsed to an integer.");
		}
	}

	private static void initFactories() {
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add(new MoveFirstStrategyBuilder() );
		dqbs.add(new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory,dqsFactory) );
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws IOException {
		Controller controller = new Controller(new TrafficSimulator(), _eventsFactory);
		controller.loadEvents(new FileInputStream(_inFile));
		
		OutputStream out = System.out;
		if(_outFile != null) {
			out = new FileOutputStream(_outFile);
		}
		controller.run(_timeLimit, out);
	}

	private static void startGuiMode() throws IOException {
		Controller controller = new Controller(new TrafficSimulator(), _eventsFactory);
		if(_inFile != null) {
			//TODO: If _inFile is invalid,  maybe handle this error inside GUI?
			controller.loadEvents(new FileInputStream(_inFile));			
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});
		
	}
	
	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_guiMode) {
			startGuiMode();
		} else {
			startBatchMode();			
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help)

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Exception:" + e.getMessage());
			e.printStackTrace();
		}

	}

}
