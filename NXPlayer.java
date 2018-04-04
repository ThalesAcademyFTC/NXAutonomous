package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by dcrenshaw on 4/3/18.
 *
 * NXPlayer plays back recordings made by NXRecorder.
 */

public class NXPlayer extends OpMode{

    private NXSerializer serializer = new NXSerializer();
    private Object historian; //This will be a StateHistory later
    private byte[][] a;

    public void init() {
        //At least half of these lines will be pointless exception handling. Thanks, Java
        try {
            RandomAccessFile history = new RandomAccessFile("NXHistorypermanent", "r");
            try {
                long l = history.length();
                int xl = (int) l;
                if (xl != l) {
                    telemetry.addLine("ERROR: Epic fail: What did you put in this file?!");
                    return;
                }
                byte[] recoveredData = new byte[xl];
                history.readFully(recoveredData);
                history.close();
                try {
                    historian = serializer.deserialize(recoveredData);

                }
                catch (ClassNotFoundException c) {
                    telemetry.addLine("History has been corrupted. There's nothing else to be done.");
                    return;
                }
            }
            catch (IOException er) {
                telemetry.addLine("I have no idea what happened, but something went wrong.");
            }
        }
        catch (FileNotFoundException e) {
            telemetry.addLine("Oops! There's nothing to play.");
            return;
        }
    }
    public void loop() {} // We don't need a loop
}
