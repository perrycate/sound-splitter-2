import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SoundSplit {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("ERROR: No input file specified.");
            System.exit(1);
        }
        if (args.length < 2) {
            System.err.println("ERROR: No output file template specified.");
            System.exit(1);
        }
        File input = new File(args[0]);
        String outputFormat = args[1] + "_";
        AudioSplitter splitter = new AudioSplitter(input, outputFormat);

        System.out.println("Playing audio");
        splitter.play();
        while (!splitter.isDonePlaying()) {
            waitForEnter();
            splitter.snip();
            System.out.println("snip");
        }
    }

    /**
     * Blocks until the user has pressed the enter key. If there is a console
     * attached, suppress all output to it except for enter key.
     * 
     * TODO this is just for development purposes. Eclipse's console doesn't
     * actually register as a console, so we can't simply use
     * System.console().readPassword() if we want to run it from within eclipse.
     * In reality, we should print an error and quit if this program is being
     * run without a console, since it simply doesn't make sense for the user to
     * pipe input into this program.
     * 
     * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=122429
     */
    private static void waitForEnter() {
        if (System.console() != null) {
            // Don't actually care about the result, just wait for enter without
            // printing keystrokes to console.
            System.console().readPassword();
        }
        // "should never happen" (see note in javadoc above)
        else {
            Scanner s = new Scanner(System.in);
            // Doesn't suppress console output, but if we get to this point the
            // user has other problems.
            s.nextLine();
            s.close();
        }
    }

}
