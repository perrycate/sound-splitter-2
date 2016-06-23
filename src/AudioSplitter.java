import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class AudioSplitter {

    // Name that output files will start with, to be followed by the index of cuts
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
            // Open audio to be read by player
            fileType = AudioSystem.getAudioFileFormat(inputAudio).getType();
            AudioInputStream s = AudioSystem.getAudioInputStream(inputAudio);

            // Create player
            player = AudioSystem.getClip();
            player.open(s);

            // get various metadata we need
            format = s.getFormat();
            totalLength = player.getFrameLength();
            System.out.println("Frame info:");
            System.out.println("Frame size: " + format.getFrameSize());
            System.out.println("Frames in player: " + totalLength);

            // Re-open independent stream to copy from.
            // By having this stream separate from clip, we don't need to
            // rewind anything when we copy data for snipping.
            this.stream = AudioSystem.getAudioInputStream(inputAudio);
            System.out.println("bytes in stream: " + stream.available());

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
        lastSnipPos += length;
        System.out.println(length);

        AudioInputStream thingToSave = new AudioInputStream(stream, format,
                length);

        try {
            System.out.println(thingToSave.available());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        save(thingToSave);
        System.out.println(player.getLongFramePosition());

    }

    /**
     * Saves numFrames frames from data to a new file with name based on
     * outputName and numSaved, starting from the most recent mark set on data.
     */
    private void save(AudioInputStream data) {
        File outputFile = new File(
                outputName + numSaved + "." + fileType.getExtension());
        try {
            AudioSystem.write(data, fileType, outputFile);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to save file " + outputFile);
            e.printStackTrace();
        }
        numSaved++;
    }

    /**
     * Begins playing audio. Does not block.
     */
    public void play() {
        this.player.start();
    }

    public boolean isDonePlaying() {
        return this.player.getFrameLength() < totalLength;
    }

    /**
     * Stops playing audio. If audio is resumed later, it will continue playing
     * from this point.
     */
    public void pause() {
        this.player.stop();
    }

}
