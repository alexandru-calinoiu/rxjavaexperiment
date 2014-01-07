import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ObserverBuilder {
    public Observable<String> customObservableBlocking() {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> observer) {
                for(int i = 0; i < 50; i++) {
                    observer.onNext("value_" + i);
                }

                return Subscriptions.empty();
            }
        });
    }

    public Observable<String> customObservableNonBlocking() {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(final Observer<? super String> observer) {
                final Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 50; i++) {
                            observer.onNext("value_" + i);
                        }

                        observer.onCompleted();
                    }
                });

                t.start();

                return new Subscription() {
                    @Override
                    public void unsubscribe() {
                        t.interrupt();
                    }
                };
            }
        });
    }


    public Observable<String> fetchWikipediaArticles(final String... articleNames) {
        return Observable.create(new Observable.OnSubscribeFunc<String>() {
            @Override
            public Subscription onSubscribe(Observer<? super String> observer) {
                for (String articleName : articleNames) {
                    try {
                        String result = new URL("http://en.wikipedia.org/wiki/" + articleName).getContent().toString();
                        observer.onNext(result);
                    } catch (MalformedURLException e) {
                        observer.onError(e);
                    } catch (IOException e) {
                        observer.onError(e);
                    }
                }

                observer.onCompleted();

                return Subscriptions.empty();
            }
        });
    }
}
