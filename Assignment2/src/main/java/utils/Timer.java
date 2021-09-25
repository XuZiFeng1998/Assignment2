package utils;

import Sort.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Timer {



    /**
     * Construct a new Timer and set it running.
     */
    public Timer() {
        resume();
    }

    /**
     * Run the given function n times, once per "lap" and then return the result of calling stop().
     *
     * @param n        the number of repetitions.
     * @param function a function which yields a T (T may be Void).
     * @return the average milliseconds per repetition.
     */
    public <T> double repeat(int n, Supplier<T> function) {

        for (int i = 0; i < n; i++) {
            function.get();
            lap();
        }
        pause();
        return meanLapTime();
    }

    /**
     * Run the given functions n times, once per "lap" and then return the result of calling stop().
     *
     * @param n        the number of repetitions.
     * @param supplier a function which supplies a different T value for each repetition.
     * @param function a function T=>U and which is to be timed (U may be Void).
     * @return the average milliseconds per repetition.
     */
    public <T, U> double repeat(int n, Supplier<T> supplier, Function<T, U> function) {

        return repeat(n, supplier, function, null, null);
    }

    /**
     * Pause (without counting a lap); run the given functions n times while being timed, i.e. once per "lap", and finally return the result of calling meanLapTime().
     *
     * @param n            the number of repetitions.
     * @param supplier     a function which supplies a T value.
     * @param function     a function T=>U and which is to be timed.
     * @param preFunction  a function which pre-processes a T value and which precedes the call of function, but which is not timed (may be null).
     * @param postFunction a function which consumes a U and which succeeds the call of function, but which is not timed (may be null).
     * @return the average milliseconds peir repetition.
     */
    public <T, U> double repeat(int n, Supplier<T> supplier, Function<T, U> function, UnaryOperator<T> preFunction, Consumer<U> postFunction) {
        logger.trace("repeat: with " + n + " runs");
        for (int i = 0; i < n; i++) {
            T t = supplier.get();
            if(preFunction!=null){
                t = preFunction.apply(t);
                lap();
            }
            U u = function.apply(t);
            if(postFunction!=null){
                postFunction.accept(u);
                lap();
            }
            lap();
        }
        pause();
        double res = meanLapTime();
        if(preFunction!=null&&postFunction!=null){
            laps = laps/3;
        }
        return res;
    }

    /**
     * Stop this Timer and return the mean lap time in milliseconds.
     *
     * @return the average milliseconds used by each lap.
     * @throws TimerException if this Timer is not running.
     */
    public double stop() {
        pauseAndLap();
        return meanLapTime();
    }

    /**
     * Return the mean lap time in milliseconds for this paused timer.
     *
     * @return the average milliseconds used by each lap.
     * @throws TimerException if this Timer is running.
     */
    public double meanLapTime() {
        if (running) throw new TimerException();
        return toMillisecs(ticks) / laps;
    }

    /**
     * Pause this timer at the end of a "lap" (repetition).
     * The lap counter will be incremented by one.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void pauseAndLap() {
        lap();
        ticks += getClock();
        running = false;
    }

    /**
     * Resume this timer to begin a new "lap" (repetition).
     *
     * @throws TimerException if this Timer is already running.
     */
    public void resume() {
        if (running) throw new TimerException();
        ticks -= getClock();
        running = true;
    }

    /**
     * Increment the lap counter without pausing.
     * This is the equivalent of calling pause and resume.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void lap() {
        if (!running) throw new TimerException();
        laps++;
    }

    /**
     * Pause this timer during a "lap" (repetition).
     * The lap counter will remain the same.
     *
     * @throws TimerException if this Timer is not running.
     */
    public void pause() {
        pauseAndLap();
        laps--;
    }

    /**
     * Method to yield the total number of milliseconds elapsed.
     * NOTE: an exception will be thrown if this is called while the timer is running.
     *
     * @return the total number of milliseconds elapsed for this timer.
     */
    public double millisecs() {
        if (running) throw new TimerException();
        return toMillisecs(ticks);
    }

    @Override
    public String toString() {
        return "Timer{" +
                "ticks=" + ticks +
                ", laps=" + laps +
                ", running=" + running +
                '}';
    }

    private long ticks = 0L;
    private int laps = 0;
    private boolean running = false;

    // NOTE: Used by unit tests
    private long getTicks() {
        return ticks;
    }

    // NOTE: Used by unit tests
    private int getLaps() {
        return laps;
    }

    // NOTE: Used by unit tests
    private boolean isRunning() {
        return running;
    }

    /**
     * Get the number of ticks from the system clock.
     * <p>
     * NOTE: (Maintain consistency) There are two system methods for getting the clock time.
     * Ensure that this method is consistent with toMillisecs.
     *
     * @return the number of ticks for the system clock. Currently defined as nano time.
     */
    private static long getClock() {
        // TO BE IMPLEMENTED
        return System.currentTimeMillis() * 1000000L + System.nanoTime() % 1000000L;

    }

    /**
     * NOTE: (Maintain consistency) There are two system methods for getting the clock time.
     * Ensure that this method is consistent with getTicks.
     *
     * @param ticks the number of clock ticks -- currently in nanoseconds.
     * @return the corresponding number of milliseconds.
     */
    private static double toMillisecs(long ticks) {
        return ticks/1000000L;
        // TO BE IMPLEMENTED


    }

    final static LazyLogger logger = new LazyLogger(Timer.class);

    static class TimerException extends RuntimeException {
        public TimerException() {
        }

        public TimerException(String message) {
            super(message);
        }

        public TimerException(String message, Throwable cause) {
            super(message, cause);
        }

        public TimerException(Throwable cause) {
            super(cause);
        }
    }
    public static void generateArray(){
        Random random = new Random();
        ra = new ArrayList<>();
        or = new ArrayList<>();
        po = new ArrayList<>();
        re = new ArrayList<>();
        for(int i=0;i<50000;i++){
            ra.add(random.nextInt(50000));
        }
        for(int i=0;i<50000;i++){
            or.add(i);
        }
        for(int i=0;i<25000;i++){
            po.add(random.nextInt(25000));
        }
        for(int i=25000;i<50000;i++){
            po.add(i);
        }
        for(int i=0;i<50000;i++){
            re.add(50000-i);
        }
        xs1 = ra.toArray(new Integer[0]);
        xs2 = or.toArray(new Integer[0]);
        xs3 = po.toArray(new Integer[0]);
        xs4 = re.toArray(new Integer[0]);

    }

    public double test1(int n,Integer[] arr){
        for(int i=0;i<n;i++){
            sorter.sort(arr);
            lap();
        }
        pause();
        return meanLapTime();

    }

    public static void main(String[] args) {
        generateArray();
        double res_ra = 0;
        double res_or = 0;
        double res_po = 0;
        double res_re = 0;
//        for(int i=8;i<13;i++){
            Timer timer1 = new Timer();
            res_ra = timer1.test1(20,xs1);
            Timer timer2 = new Timer();
            res_or = timer2.test1(20,xs2);
            Timer timer3 = new Timer();
            res_po = timer3.test1(20,xs3);
            Timer timer4 = new Timer();
            res_re = timer4.test1(20,xs4);
            System.out.println(res_ra);
            System.out.println(res_or);
            System.out.println(res_po);
            System.out.println(res_re);
//        }




    }
    static List<Integer> ra;
    static List<Integer> or;
    static List<Integer> po;
    static List<Integer> re;
    static Integer[] xs1;
    static Integer[] xs2;
    static Integer[] xs3;
    static Integer[] xs4;
    static Config config = ConfigTest.setupConfig("true", "0", "1", "", "");
    static Helper<Integer> helper = HelperFactory.create("InsertionSort", 50000, config);
    static SortWithHelper<Integer> sorter = new InsertionSort<Integer>(helper);
}
