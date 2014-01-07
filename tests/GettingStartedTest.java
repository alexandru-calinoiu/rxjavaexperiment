import junit.framework.TestCase;

public class GettingStartedTest extends TestCase {
    private GettingStarted subject;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        subject = new GettingStarted();
    }

    public void testHello() {
        subject.hello("Ion", "Din", "Deal");
    }
}
