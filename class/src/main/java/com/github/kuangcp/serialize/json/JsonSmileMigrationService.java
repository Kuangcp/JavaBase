package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-04-21 10:53 PM
 */
@Slf4j
public class JsonSmileMigrationService {

  public static byte[] convertToSmile(byte[] json, JsonFactory jsonFactory,
      SmileFactory smileFactory) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try // try-with-resources
        (
            JsonGenerator jg = smileFactory.createGenerator(bos);
            JsonParser jp = jsonFactory.createParser(json)
        ) {
      while (jp.nextToken() != null) {
        jg.copyCurrentEvent(jp);
      }
    } catch (Exception e) {
      log.error("Error while converting json to smile", e);
    }

    return bos.toByteArray();
  }

  public static byte[] convertToJson(byte[] smile, JsonFactory jsonFactory,
      SmileFactory smileFactory) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    try (
        JsonParser sp = smileFactory.createParser(smile);
        JsonGenerator jg = jsonFactory.createGenerator(bos)
    ) {
      while (sp.nextToken() != null) {
        jg.copyCurrentEvent(sp);
      }
    } catch (Exception e) {
      log.error("Error while converting smile to json", e);
    }

    return bos.toByteArray();
  }
}