package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;

import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
    public static CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        // TODO: 14/12/2020 remove try input poutput
        //read input
        Input input = null;
        Gson gson = new Gson();
        //todo
        try (Reader reader = new FileReader("input.json")) {
           //  try (Reader reader = new FileReader(args[0])) {
            input = gson.fromJson(reader, Input.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //construct passive+ microservices
        Diary diary = Diary.getInstance();
        Ewoks.getInstance().setEwoksList(input.getEwoks());

        Thread leia = new Thread(new LeiaMicroservice(input.getAttacks()));
        Thread r2d2 = new Thread(new R2D2Microservice(input.getR2D2()));
        Thread lando = new Thread(new LandoMicroservice(input.getLando()));
        Thread hans = new Thread(new HanSoloMicroservice());
        Thread c3po = new Thread(new C3POMicroservice());

        long startTime = System.currentTimeMillis();        //todo remove

        //simulate
        hans.start();
        c3po.start();
        leia.start();
        r2d2.start();
        lando.start();

        //wait for threads termination
        try {
            leia.join();
            hans.join();
            c3po.join();
            r2d2.join();
            lando.join();
        } catch (InterruptedException ex) {
            System.out.println("still working");
        }

        //create output
        try (Writer writer = new FileWriter("Output.json")) {
            // try (Writer writer = new FileWriter(args[1])) {
            gson.toJson(diary, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: 14/12/2020 delete
        System.out.println("\ntotalAttacks: "+ diary.getTotalAttacks()+
          "\nHanSoloFinish: " + (diary.getHanSoloFinish()-startTime)+
          "\nC3POFinish: " +(diary.getC3POFinish()-startTime)+
          "\nR2D2Deactivate: " +(diary.getR2D2Deactivate()-startTime)+
          "\nLeiaTerminate: " +(diary.getLeiaTerminate()-startTime)+
          "\nHanSoloTerminate: " +(diary.getHanSoloTerminate()-startTime)+
          "\nC3POTerminate: " +(diary.getC3POTerminate()-startTime)+
          "\nLandoTerminate: " +(diary.getLandoTerminate()-startTime)+
          "\nR2D2Terminate: " +(diary.getR2D2Terminate()-startTime));

    }
}
