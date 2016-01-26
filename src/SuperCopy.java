import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("static-access")
public class SuperCopy {
	
	public static void checkFiles(String inputSrc, String inputTgt, String output) throws IOException {
		File folder = new File(inputSrc);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File inputSrcFile = listOfFiles[i];
			if (inputSrcFile.isFile()) {
				if (SuperCopy.hasFile(inputTgt, inputSrcFile.getName())) {
					log.info("File " + listOfFiles[i].getName() + " has been founded in input target folder ! Try to copy it from input source folder to output folder...");
					File ouputFile = new File(Paths.get(output,listOfFiles[i].getName()).toString());
					Files.copy(listOfFiles[i].toPath(), ouputFile.toPath());
					log.info("Copy successfull !");
				}
			} 
		}
	}
	
	public static boolean hasFile(String dir, String file) {
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().toLowerCase().equals(file.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	private static Log log = LogFactory.getLog(SuperCopy.class);

	private static Option inputSourceDirOpt = OptionBuilder
			.withArgName("inputSrcDir").hasArg()
			.withDescription("Input source directory").withLongOpt("inputsrc")
			.create("s");

	private static Option inputTargetDirOpt = OptionBuilder
			.withArgName("inputTgtDir").hasArg()
			.withDescription("Input target directory").withLongOpt("inputtgt")
			.create("t");

	private static Option outputDirOpt = OptionBuilder.withArgName("outputDir")
			.hasArg().withDescription("Input source directory")
			.withLongOpt("output").create("o");

	public static void main(String[] args) {
		// Get all options
		Options options = new Options();
		options.addOption(inputSourceDirOpt);
		options.addOption(inputTargetDirOpt);
		options.addOption(outputDirOpt);

		// Init parameters
		String inputSource = null;
		String inputTarget = null;
		String output = null;

		// Option parsing
		// Create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			// Input source directory
			if (!line.hasOption("s")) {
				// automatically generate the help statement
				log.error("inputsrc is required. Use -s option to set it.");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SuperCopy", options);
				System.exit(-1);
			} else {
				inputSource = line.getOptionValue("inputsrc");
			}

			// Input target directory
			if (!line.hasOption("t")) {
				// automatically generate the help statement
				log.error("inputTgtDir is required. Use -t option to set it.");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SuperCopy", options);
				System.exit(-1);
			} else {
				inputTarget = line.getOptionValue("inputtgt");
			}

			// Output directory
			if (!line.hasOption("o")) {
				// automatically generate the help statement
				log.error("outputDir is required. Use -o option to set it.");
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("SuperCopy", options);
				System.exit(-1);
			} else {
				output = line.getOptionValue("output");
			}
		} catch (ParseException exp) {
			// oops, something went wrong

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("SuperCopy", options);
			System.exit(-1);
		}
		
		File dir = new File(output);
		if (!dir.exists()){
			log.info("Output folder seems to not exist. Try to make it...");
			dir.mkdir();
			log.info("Make done !");
		}
		try {
			log.info("SuperCopy is ready ! The copy will begin in a few seconds...");
			SuperCopy.checkFiles(inputSource, inputTarget, output);
			log.info("The copy is done ! alors ca rocks du poney ?");
		} catch (IOException e) {
			log.error("Error during copy : " + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
