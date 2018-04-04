package org.firstinspires.ftc.teamcode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcrenshaw on 4/2/18.
 *
 * Simple convenience class to store controller states and time lengths recorded by NXRecorder
 */

public class NXStateHistory implements Serializable {
    private List<byte[]> controllerStates = new ArrayList<>();
    private List<Long> timeHistories = new ArrayList<>();

    public byte[][] getByteArray2d() {
        // Simple list-array conversion
        byte[][] a = new byte[controllerStates.size()][42];
        for (int x = 0; x < controllerStates.size(); x++) {
            a[x] = controllerStates.get(x);
        }
        return a;
    }
    public long[] getTimeHistory() {
        long[] a = new long[timeHistories.size()];
        for (int x = 0; x < timeHistories.size(); x++) {
            a[x] = timeHistories.get(x);
        }
        return a;
    }
    public void appendTime(long a) {
        timeHistories.add(a);
    }
    public void appendState(byte[] a) {
        controllerStates.add(a);
    }
}
