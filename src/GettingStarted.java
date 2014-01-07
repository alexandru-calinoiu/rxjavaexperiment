import rx.Observable;
import rx.util.functions.Action1;

public final class GettingStarted {
    public void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String name) {
                System.out.println(name);
            }
        });
    }
}
