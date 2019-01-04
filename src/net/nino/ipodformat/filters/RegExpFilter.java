package net.nino.ipodformat.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 13:22
 */
public class RegExpFilter implements FilenameFilter
{
  private String regExp;

  public RegExpFilter(String regExp)
  {
    this.regExp = regExp;
  }

  public boolean accept(File file, String fileName)
  {
     return fileName.matches(regExp);
  }
}//END of CLASS
