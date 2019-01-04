package net.nino.ipodformat.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 13:04
 */
public class DeleteEmptyDirs extends Rule
{
  private Logger logger = LoggerFactory.getLogger(DeleteEmptyDirs.class);


  public DeleteEmptyDirs()
  {
  }

  @Override
  public String toString()
  {
    return "Empty directory deletion rule";
  }

  public void callBack(File directory)
  {
    File[] files = directory.listFiles();

    if (files.length != 0)
    {
      if (logger.isDebugEnabled())
        logger.debug("Directory is not empty.");
      return;
    }

    if (logger.isDebugEnabled())
      logger.debug("Deleting directory.");

    if (!directory.delete())
      directory.deleteOnExit();
  }
}// END of CLASS
