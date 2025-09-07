for file in *.wav; do
    ffmpeg -i "$file" -filter_complex "[0:a][0:a]concat=n=2:v=0:a=1" "${file%.*}_doubled.${file##*.}"
done
