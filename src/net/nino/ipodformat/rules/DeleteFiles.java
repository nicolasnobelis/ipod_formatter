package net.nino.ipodformat.rules;

import net.nino.ipodformat.filters.RegExpFilter;
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
public class DeleteFiles extends Rule
{
  private Logger logger = LoggerFactory.getLogger(DeleteFiles.class);

  // the regexp of the file that will be deleted (case insensitive)
  // e.g. erase anything but mp3 files : a '.', then the extension that shouldn't be mp3
  // "^.*\.(?!mp3)\w+$" <- bug: it doesn't erase files w/out extension
  private String deletedFilesExpr;

  public DeleteFiles()
  {
  }

  @Required
  public void setDeletedFilesExpr(String deletedFilesExpr)
  {
    this.deletedFilesExpr = deletedFilesExpr;
  }

  @Override
  public String toString()
  {
    return "File deletion rule";
  }

  public void callBack(File directory)
  {
    File[] filesToDelete = directory.listFiles(new RegExpFilter(deletedFilesExpr));

    if (filesToDelete.length == 0)
    {
      if (logger.isDebugEnabled())
        logger.debug("No file to delete.");
      return;
    }

    if (logger.isDebugEnabled())
      logger.debug("Deleting " + filesToDelete.length + " files.");

    for (File fileToDelete : filesToDelete)
    {
      if (!fileToDelete.delete())
        fileToDelete.deleteOnExit();
    }
  }
}// END of CLASS
