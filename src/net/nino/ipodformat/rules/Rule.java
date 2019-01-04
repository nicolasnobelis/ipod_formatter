package net.nino.ipodformat.rules;

import org.springframework.beans.factory.annotation.Required;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 12:38
 */
public abstract class Rule
{
  protected int runLevel;

  public int getRunLevel()
  {
    return  runLevel;
  }

  @Required
  public void setRunLevel(int runLevel)
  {
    this.runLevel = runLevel;
  }

  public abstract void callBack(File directory);
}
