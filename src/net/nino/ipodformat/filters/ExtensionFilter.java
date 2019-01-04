package net.nino.ipodformat.filters;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 13:27
 */
public class ExtensionFilter implements FilenameFilter
{
  private List<String> acceptedExtensions = new ArrayList<String>(5);

  public ExtensionFilter(String... extensions)
  {
    for (String extension : extensions)
    {
      acceptedExtensions.add(extension);
    }
  }

  public boolean accept(File parent, String fileName)
  {
    for (String acceptedExtension : acceptedExtensions)
    {
      if (fileName.endsWith(acceptedExtension))
        return true;
    }

    return false;
  }
}// END of CLASS
