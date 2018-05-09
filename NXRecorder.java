package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dcrenshaw on 4/1/18.
 *
 * NXRecorder records controller state changes which can be played back through a teleop or controller
 * input handler to create a hard-coded autonomous.
 *
 * NXRecorder is built to handle input from gamepad1 only. A future version is planned to handle both
 * gamepad1 and gamepad2.
 *
 * Fair warning: gamepad2 will be usable while running, but input from it will not be recorded.
 * If you absolutely need gamepad2, you will need to find a different way to go about doing this
 * until version 2 is available
 */

public class NXRecorder extends OpMode implements Gamepad.GamepadCallback {

    public ClaspTeleop teleConnect;
    private NXStateHistory historian = new NXStateHistory();
    long startTime = System.currentTimeMillis();
    private NXSerializer serializer = new NXSerializer();
    //private byte[] lastKnownState; Uncomment if recorder fails to catch every state change
    public void loop() {
        /* Uncomment if recorder fails to catch every state change
        try {
            if (gamepad1.toByteArray() != lastKnownState) gamepadChanged(gamepad1);
        } catch(RobotCoreException e) {} // This is actually completely superfluous. Thanks, Java.
        */
        teleConnect.loop();
    }
    public void init() {
        try {
            gamepad1.copy(new Gamepad(this));
        }
        catch (RobotCoreException e) {} //Error handling is not necessary
        teleConnect = new ClaspTeleop();
        //This is technically parameter injection.
        teleConnect.telemetry = telemetry;
        teleConnect.hardwareMap = hardwareMap;
        teleConnect.init();
        teleConnect.gamepad1 = gamepad1;
        //try{lastKnownState = gamepad1.toByteArray();} catch(RobotCoreException e){} Uncomment if recorder fails to catch every state change
    }
    @Override
    public void gamepadChanged(Gamepad gamepad) { //Log every time the controller changes
        try {
            historian.appendState(gamepad.toByteArray());
        }
        catch (RobotCoreException e) {
            //This will never actually matter, but it has to be here anyways.
            telemetry.addLine("Gamepad library is in an invalid state. NXRecorder cannot operate properly.");
        }
        historian.appendTime(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
    }
    public void stop() {
        try {
            byte[] a = serializer.serialize(historian);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("NXHistorypermanent"));
                bos.write(a); bos.close();
            }
            catch (FileNotFoundException e) {
                File history = new File("NXHistorypermanent");
                try {
                    boolean s = history.createNewFile();
                    if (!s) {
                        telemetry.addLine("Unable to create NX history");
                    }
                }
                catch (IOException er) {
                    telemetry.addLine(er.getMessage());
                    return;
                }
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("NXHistorypermanent"));
                    bos.write(a); bos.close();
                }
                catch (FileNotFoundException f){return;} // This will never be hit
            }
        }
        catch (IOException e) {
            telemetry.addLine("Unable to serialize historian object");
        }
    }
}
