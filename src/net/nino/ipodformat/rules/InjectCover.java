package net.nino.ipodformat.rules;

import net.nino.ipodformat.filters.ExtensionFilter;
import net.nino.ipodformat.filters.RegExpFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.StandardArtwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 13:04
 */
public class InjectCover extends Rule
{
  private Logger logger = LoggerFactory.getLogger(InjectCover.class);

  // the regexp of the file that contains the cover we have to inject (case insensitive)
  // "^Cover.(jpg|jpeg|png)$"
  private String coverFileExpr;
  // the extension of the audio files that will be injected w/ the cover (case insensitive)
  // ".mp3"
  private String audioFilesExt;

  public InjectCover()
  {
  }

  @Required
  public void setCoverFileExpr(String coverFileExpr)
  {
    this.coverFileExpr = coverFileExpr;
  }

  @Required
  public void setAudioFilesExt(String audioFilesExt)
  {
    this.audioFilesExt = audioFilesExt;
  }

  @Override
  public String toString()
  {
    return "Cover injection rule";
  }

  public void callBack(File directory)
  {
    File[] audioFiles = directory.listFiles(new ExtensionFilter(audioFilesExt));

    if (audioFiles.length == 0) // no files will be injected, so we ignore it
      return;

    File[] coverFiles = directory.listFiles(new RegExpFilter(coverFileExpr));

    if (coverFiles.length == 0)
    {
      logger.warn("No cover found for folder " + directory.getPath() + ". Ignored.");
      return;
    }

    if (coverFiles.length > 1)
      logger.warn("Multiple covers found for folder " + directory.getPath() + ". Taking the first one.");

    Artwork cover;
    try
    {
      cover = StandardArtwork.createArtworkFromFile(coverFiles[0]);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return;
    }

    cover.setDescription("Injected w. IpodFormatter"); // buggy ?
    // the mime type is set automatically, nice !

    if (logger.isDebugEnabled())
      logger.debug("Injecting cover in " + audioFiles.length + " files.");

    for (File audioFile : audioFiles)
    {
      try
      {
        MP3File af = (MP3File) AudioFileIO.read(audioFile);
        Tag tag = af.getID3v2Tag();

        if (tag == null)
        {
          logger.warn("Audio file has no ID3v2 tag. Ignored.");
          continue;
        }

        //logger.debug(tag.getFirst(FieldKey.TITLE));
        if (tag.getFirstArtwork() == null) // there is no artwork yet
        {
          tag.addField(cover);
          af.commit();
        }
      } catch (Exception e)
      {
        logger.error("Error during cover injection", e);
      }

    }
  }
}// END of CLASS
