package dk.dma.enav.integration.aton;

import dk.dma.enav.integration.aton.shape.dbf.DbfParser;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtonInRouter extends FatJarRouter {

    @Override
    public void configure() {
        from("file:{{shape.source.dir}}?consumer.bridgeErrorHandler=true")
                .bean(DbfParser.class, "parse")
                .marshal().json(JsonLibrary.Jackson)
                .to("direct:insertAtonData");

        from("direct:insertAtonData").routeId("insert")
                .to("couchdb:http://{{couchdb.host}}:{{couchdb.port}}/{{couchdb.name}}?username={{couchdb.username}}&password={{couchdb.password}}");
    }

}
