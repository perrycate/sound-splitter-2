import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.*;

public class Splitter {

    // Name that output files will start with, followed by the index of cuts
    private String outputName;
    // Number of files saved so far (second half of output file names)
    private int numSaved = 0;
    // input stream of the file we're working with
    private AudioInputStream stream;
    private AudioFormat format;
    private AudioFileFormat.Type fileType;
    // used to actually play the input stream for the user.
    private Clip player;
    // Frame position on the audio stream where we last cut from.
    private long lastSnipPos = 0;

    public Splitter(File inputAudio, String output) {
        outputName = output;
        try {
            fileType = AudioSystem.getAudioFileFormat(inputAudio).getType();
            stream = AudioSystem.getAudioInputStream(inputAudio);
            format = stream.getFormat();
            // TODO check if mark supported?
        } catch (UnsupportedAudioFileException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Creates a new audio file that is a copy of the input audio from the
     * most recent mark set to the input audio's current position, then resets
     * the mark to the current position.
     */
    public void snip() {
        // Calculate number of frames to snip
        long length = player.getLongFramePosition() - lastSnipPos;

        // Return to the last place we snipped from
        try {
            stream.reset();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        save(new AudioInputStream(stream, format, length));
    }

    /**
     * Saves numFrames frames from data to a new file with name based on
     * outputName and numSaved, starting from the most recent mark set on data.
     */
    private void save(AudioInputStream data) {
        File outputFile = new File(outputName + numSaved);
        try {
            AudioSystem.write(data, fileType, outputFile);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to save file " + outputFile);
            e.printStackTrace();
        }
        numSaved++;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("ERROR: No input file specified.");
        }
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
            // Doesn't surpress console output, but at this point the user has
            // other problems.
            s.nextLine();
            s.close();
        }
    }

}
