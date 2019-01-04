package net.nino.ipodformat.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 19/01/11
 * Time: 12:41

 */
public class RuleComparator implements Comparator<Rule>
{
  private Logger logger = LoggerFactory.getLogger(RuleComparator.class);

  public int compare(Rule r1, Rule r2)
  {
    if (r1.getRunLevel() == r2.getRunLevel())
    {
      logger.warn(String.format("Warning : rule %s and rule %s have the same runlevel", r1, r2));
      return -1; // false order because rule are not equal
    }

    return (r1.getRunLevel() > r2.getRunLevel())? 1 : -1;
  }
}// END of CLASS
