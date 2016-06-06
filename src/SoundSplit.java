import java.util.Scanner;

public class SoundSplit {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("ERROR: No input file specified.");
            //TODO quit program
        }
        AudioSplitter splitter = new AudioSplitter()
        
    }

    /**
     * Blocks until the user has pressed the enter key. If there is a console
     * attached, supress all output to it except for enter key.
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
