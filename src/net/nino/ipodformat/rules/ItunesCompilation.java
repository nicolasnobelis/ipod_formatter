package net.nino.ipodformat.rules;

import net.nino.ipodformat.filters.ExtensionFilter;
import net.nino.ipodformat.filters.RegExpFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.*;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTCMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 13:04
 */
public class ItunesCompilation extends Rule
{
  private Logger logger = LoggerFactory.getLogger(ItunesCompilation.class);

  // the regexp of the file that markups the compilation
  // "^Compilation.tag$"
  private String tagFileExpr;
  // the extension of the audio files that will be injected w/ the compilation flag (case insensitive)
  // ".mp3"
  private String audioFilesExt;

  public ItunesCompilation()
  {
  }

  @Required
  public void setTagFileExpr(String tagFileExpr)
  {
    this.tagFileExpr = tagFileExpr;
  }

  @Required
  public void setAudioFilesExt(String audioFilesExt)
  {
    this.audioFilesExt = audioFilesExt;
  }

  @Override
  public String toString()
  {
    return "Itunes compilation flag rule";
  }

  public void callBack(File directory)
  {
    File[] audioFiles = directory.listFiles(new ExtensionFilter(audioFilesExt));

    if (audioFiles.length == 0) // no files will be injected, so we ignore it
      return;

    File[] tagFiles = directory.listFiles(new RegExpFilter(tagFileExpr));

    if (tagFiles.length == 0)
    {
      logger.debug("No compilation tag file found for folder " + directory.getPath() + ". Ignored.");
      return;
    }

    if (tagFiles.length > 1)
      logger.warn("Multiple compilation tag files found for folder " + directory.getPath() + ". One is enough.");


    if (logger.isDebugEnabled())
      logger.debug("Injecting compilation flag in " + audioFiles.length + " files.");

    AbstractID3v2Frame tcmpFrameV23 = new ID3v23Frame(ID3v23Frames.FRAME_ID_V3_IS_COMPILATION);
    AbstractID3v2Frame tcmpFrameV24 = new ID3v24Frame(ID3v24Frames.FRAME_ID_IS_COMPILATION);
    tcmpFrameV23.setBody(new FrameBodyTCMP());
    tcmpFrameV24.setBody(new FrameBodyTCMP());

    for (File audioFile : audioFiles)
    {
      try
      {
        MP3File af = (MP3File) AudioFileIO.read(audioFile);
        AbstractID3v2Tag tag = af.getID3v2Tag() ;

        if (tag == null)
        {
          logger.warn("Audio file has no ID3v2 tag. Ignored.");
          continue;
        }

        if (tag instanceof ID3v23Tag)
          tag.setFrame(tcmpFrameV23);
        else if (tag instanceof ID3v24Tag)
          tag.setFrame(tcmpFrameV24);
        else
        {
          logger.warn("File " + audioFile.getName() + " has neither 2.3 nor 2.4 id3 tag.");
          continue;
        }

        af.commit();
      }
      catch (Exception e)
      {
        logger.error("Error during compilation flag injection", e);
      }

    }
  }
}// END of CLASS
