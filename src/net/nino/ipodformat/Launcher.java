package net.nino.ipodformat;

import net.nino.ipodformat.rules.RuleEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: nino
 * Date: 20/01/11
 * Time: 17:04
 */
public class Launcher
{
  private static Logger logger = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) throws FileNotFoundException
  {
    logger.info("Ipod Formatter started.");

    if (args.length != 1)
    {
      logger.error("Missing startup directory as launcher parameter.");
      System.exit(1);
    }

    String startupDir = args[0];

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

    if (logger.isDebugEnabled())
      logger.debug("Spring context loaded.");

    logger.info("Processing " + startupDir);

    RuleEngine engine = (RuleEngine) context.getBean("ruleEngine");
    engine.run(startupDir);

    logger.info("Ipod Formatter finished.");
  }
}// END of CLASS
