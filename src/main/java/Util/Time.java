package Util;

public class Time {
//  Time of start
    public static float timeStarted = System.nanoTime();

//  Time passed since start in seconds
    public static float getTime() {return (float)((System.nanoTime() - timeStarted) * 1E-9);}

}
