# Sound-Splitter-2
Split an audio file into pieces while listening

This is a utility I wrote and use for splitting one audio file into smaller pieces.
My use case is splitting up audio files containing vocabulary into pieces for my
[anki](https://github.com/dae/anki) flashcards. This is a better rewrite of my
[original python script](https://github.com/TheGuyWithTheFace/Sound-Splitter), which
attempted to do the same thing but automatically based on silence in the file,
albeit not well.

After compiling, run with `java SoundSplit filename.wav outputname`,
where `outputname` is the string that newly created files should start with.

Press Enter to snip the file.

Currently SoundSplitter will only work with .wav files. To convert an mp3 to a .wav
file, use `ffmpeg -i <mp3 file> <new filename>.wav
