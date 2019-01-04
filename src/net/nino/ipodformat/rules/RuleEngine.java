package net.nino.ipodformat.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 12:36
 */
public class RuleEngine
{
  private Logger logger = LoggerFactory.getLogger(RuleEngine.class);

  private Set<Rule> rules;

  public RuleEngine()
  {
  }

  @Required
  public void setRules(Set<Rule> rules)
  {
    // we sort the rule set by runLevel
    this.rules = new TreeSet<Rule>(new RuleComparator());
    this.rules.addAll(rules);
  }

  public void run(String startingPath) throws FileNotFoundException
  {
    File startFile = new File(startingPath);
    run(startFile);
  }

  public void run(File startingFile) throws FileNotFoundException
  {
    logger.info("Processing [" + startingFile.getPath() + "].");

    if (!startingFile.exists())
      throw new FileNotFoundException("Non existing file " + startingFile.getPath());
    if (!startingFile.isDirectory())
      throw new IllegalArgumentException(startingFile.getPath() + " is not a directory");

    File[] subFolders = startingFile.listFiles(new FileFilter(){
      public boolean accept(File file)
      {
        return (file.exists() && file.isDirectory());
      }
    });

    // for each sub folder
    for (File subFolder : subFolders)
    {
      // we recursively call the run engine on the sub folder
      run(subFolder);

      // and apply the rules to the sub folder (in the runLevel order)
      for (Rule rule : rules)
      {
        rule.callBack(subFolder);
      }
    }
  }// END of RUN
}// END of CLASS
