
public class HomeWork {
    private final Object mon = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) throws InterruptedException {
        HomeWork hw = new HomeWork();
        Thread a = new Thread(()->{hw.printLetter('A');});
        Thread b = new Thread(()->{hw.printLetter('B');});
        Thread c = new Thread(()->{hw.printLetter('C');});

        a.start();
        b.start();
        c.start();
    }

    public void printLetter (char symbol){
        synchronized (mon){
            try{
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != symbol){
                        mon.wait();
                    }
                    System.out.print(symbol);
                    switch (symbol){
                        case 'A':
                            currentLetter ='B';
                            mon.notifyAll();
                            break;
                        case 'B':
                            currentLetter = 'C';
                            mon.notifyAll();
                            break;
                        case 'C':
                            currentLetter = 'A';
                            mon.notifyAll();
                            break;
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }


}
