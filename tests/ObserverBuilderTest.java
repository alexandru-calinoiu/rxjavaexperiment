import junit.framework.TestCase;
import rx.Observable;
import rx.util.functions.Action1;
import rx.util.functions.Func1;
import rx.util.functions.Func2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ObserverBuilderTest extends TestCase {
    private ObserverBuilder subject;
    private CountDownLatch lock = new CountDownLatch(1);

    public void setUp() throws Exception {
        super.setUp();

        subject = new ObserverBuilder();
    }

    public void testCustomObservableBlocking() throws Exception {
        subject.customObservableBlocking().subscribe(new Action1<String>() {
            Integer counter = 0;

            @Override
            public void call(String s) {
                assertEquals("value_" + counter++, s);
            }
        });
    }

    public void testCustomObservableNonBlocking() throws Exception {
        subject.customObservableNonBlocking().subscribe(new Action1<String>() {
            Integer counter = 0;

            @Override
            public void call(String s) {
                assertEquals("value_" + counter++, s);
            }
        });
    }

    public void testCustomObserverNonBlockingChain() throws Exception {
        subject.customObservableBlocking()
                .skip(10)
                .take(5)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s + "_xform";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        assertEquals("xform", s);
                    }
                });

        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    public void testZip() throws Exception {
        Observable.zip(subject.customObservableBlocking(), subject.customObservableNonBlocking(), new Func2<String, String, String>() {
            @Override
            public String call(String s, String s2) {
                return s + s2;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String o) {
                assertEquals("something", o);
            }
        });
    }
}
