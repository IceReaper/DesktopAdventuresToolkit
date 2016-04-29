# DesktopAdventuresToolkit
Application to unpack and repack Desktop Adventure Games (Indiana Jones and Yoda Stories).
This application also allows to play Indiana Jones in Yoda Stories Engine by unpacking and repacking the archive.

Unpacking will generate an output directory with a set of simple formats. No information will be truncated when unpacking.
- Zones are saved as .tmx files. (viewable with Tiled)
- Sounds are saved as .wav (or .mid) files.
- Images are saved as .png files.
- Chracters, Weapons, Puzzles etc. are saved as .xml files.
- For simple modding purpose, the palette file is stored as reference.
- When unpacking, a question ocurrs if some minor bugs in the original archives should be fixed or extracted as-is.

Packing will generate an output game container from an unpacked folder structure.
- Using a parameter, this process will generate an Indiana Jones instead of Yoda Stories archive.
- This process will validate the content for errors.

Current progress:
- Archive reader: 100% (every single byte is read without skipping anything)
- Archive writer: 100% (every single byte is written without skipping anything)
- Specify values: 90% (missing actions and need to verify some type-flags)
- Unpacking: 90% (missing whats not specified yet)
- Packing: 30% (a little behind the unpacking progress, as i try to generate the exact same output as the original input was)
- Bugfixes: 10% (basic implementation, need to verify final output to find more bugs)
- Output documentation wiki: 0% (when everything is done, the wiki will contain the information on how to mod)
