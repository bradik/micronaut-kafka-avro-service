package my.kafka.service;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    LOG.warn("******************************************************");
    LOG.warn("******************* my-kafka-service *****************");
    LOG.warn("******************************************************");

    Map<String, String> env = System.getenv();
    for (Map.Entry<String, String> envVal : env.entrySet()) {
      LOG.warn(envVal.getKey() + '=' + envVal.getValue());
    }


    try {

      ApplicationContext context = Micronaut.run();

      for (PropertySource propertySource : context.getEnvironment().getPropertySources()) {
        String propName = propertySource.getName();
        LOG.warn(propName);
        for (String name : propertySource) {
          LOG.warn(propName + ':' + name + '=' + propertySource.get(name));
        }
      }
    } catch (Exception e) {
      LOG.error(e.getMessage());
      System.exit(1);
    }
  }
}