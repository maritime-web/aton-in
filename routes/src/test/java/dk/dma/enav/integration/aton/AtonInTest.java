package dk.dma.enav.integration.aton;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.ToDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

/**
 * Created by Steen on 09-02-2016.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AtonInRouter.class)
@ActiveProfiles("unittest")
public class AtonInTest {
    @Autowired
    private ModelCamelContext context;

    @Produce(uri = "file:{{shape.source.dir}}")
    private ProducerTemplate producer;

    @Test
    public void shouldReadADBFFileAndSendItToTheCOuchDb() throws Exception {
        context.getRouteDefinition("insert").adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // replace couchdb route with mock
                weaveByType(ToDefinition.class).selectFirst().replace().to("mock:output");
            }
        });


        context.getEndpoints().forEach(System.out::println);
        context.getRestDefinitions().forEach(System.out::println);
        MockEndpoint end = context.getEndpoint("mock:output", MockEndpoint.class);

        end.expectedMessageCount(1);

        producer.sendBodyAndHeader(getTestDbfAsStream(), Exchange.FILE_NAME, "testfyr.dbf");

        end.assertIsSatisfied();
    }

    private InputStream getTestDbfAsStream() {
        return getClass().getClassLoader().getResourceAsStream("Fyr_Gronland_13jan2016.dbf");
    }
}
