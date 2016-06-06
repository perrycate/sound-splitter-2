import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class AudioSplitter {

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
    private int totalLength;
    // Frame position on the audio stream where we last cut from.
    private long lastSnipPos = 0;

    public AudioSplitter(File inputAudio, String output) {
        outputName = output;
        try {
            fileType = AudioSystem.getAudioFileFormat(inputAudio).getType();
            stream = AudioSystem.getAudioInputStream(inputAudio);
            if (!stream.markSupported()) {
                throw new UnsupportedOperationException();
            }
            format = stream.getFormat();
            player = AudioSystem.getClip();
            player.open(stream);
            totalLength = player.getFrameLength();
            stream.mark(totalLength);
        } catch (UnsupportedAudioFileException | IOException
                | LineUnavailableException e) {
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

        try {
            stream.skip(length);
            stream.mark(totalLength);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Begins playing audio. Does not block.
     */
    public void play() {
        this.player.start();
    }

    /**
     * Stops playing audio. If audio is resumed later, it will continue playing
     * from this point.
     */
    public void pause() {
        this.player.stop();
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
}
