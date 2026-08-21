package com.kw.common.dict.fst;

import java.io.File;
import java.nio.file.Files;

public class App {
    public static void main(String[] args) {

        FSTBuilder builder =
                new FSTBuilder();



        try {
            Files.readAllLines(
                    new File("D:\\java\\LibGdxTool\\assets\\dict\\word.txt")
                            .toPath()
            ).forEach(it -> builder.add(it, 1));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        builder.add(
//                "apple",
//                100
//        );
//
//
//        builder.add(
//                "apply",
//                200
//        );
//
//
//        builder.add(
//                "apt",
//                300
//        );
//
//
//        builder.add(
//                "banana",
//                400
//        );



        FST fst =
                builder.build();



        System.out.println(
                fst.lookup("apple")
        );


        System.out.println(
                fst.lookup("apply")
        );


        System.out.println(
                fst.lookup("apt")
        );


        System.out.println(
                fst.lookup("banana")
        );


        System.out.println(
                fst.lookup("abc")
        );

    }
}
